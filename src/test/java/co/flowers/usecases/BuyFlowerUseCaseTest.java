package co.flowers.usecases;

import co.flowers.domain.collection.Flower;
import co.flowers.domain.customer.CustomerDTO;
import co.flowers.domain.dto.FlowerDTO;
import co.flowers.publisher.FlowerPublisher;
import co.flowers.repository.IFlowerRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.HashSet;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class BuyFlowerUseCaseTest {

    @Mock
    IFlowerRepository repository;

    ModelMapper mapper;

    @Mock
    FlowerPublisher flowerPublisher;

    BuyFlowerUseCase buyFlowerUseCase;

    @BeforeEach
    void setUp() {
        mapper = new ModelMapper();
        buyFlowerUseCase = new BuyFlowerUseCase(repository, mapper, flowerPublisher);
    }

    @Test
    @DisplayName("buyFlower_successfully")
    void buyFlower() {
        // flower
        Flower flower = new Flower("sunflower", "Asteraceae",
                "yellow", "sunflower", "Mexico");
        flower.setId("1");

        var monoFlower = Mono.just(flower);

        Mockito.when(repository.findById(any(String.class))).thenReturn(monoFlower);

        //flower updated
        Flower flowerUpdated = new Flower("sunflower", "Asteraceae",
                "yellow", "sunflower", "Mexico");
        flowerUpdated.setId("1");
        flowerUpdated.setInStock(false);

        Mockito.when(repository.save(any(Flower.class))).thenReturn(Mono.just(flowerUpdated));

        var result = buyFlowerUseCase.buy("1", "customerId");

        StepVerifier.create(result)
                .expectNext(mapper.map(flowerUpdated, FlowerDTO.class))
                .expectComplete()
                .verify();

        Mockito.verify(repository, times(1)).findById("1");
        Mockito.verify(repository, times(1)).save(flowerUpdated);
        try{
            Mockito.verify(flowerPublisher, times(1)).publish(any(FlowerDTO.class), any(String.class));
        } catch (JsonProcessingException e){
            System.out.println(e.getMessage());
        }
    }

    @Test
    @DisplayName("buyFlower_Failed")
    void buyFlower_Failed(){
        Flower flower = new Flower("sunflower", "Asteraceae",
                "yellow", "sunflower", "Mexico");
        flower.setId("1");

        Mockito.when(repository.findById(any(String.class)))
                .thenReturn(Mono.error(new Throwable(HttpStatus.BAD_REQUEST.toString())));

        var result = buyFlowerUseCase.buy("1", "customerId");

        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable != null &&
                        throwable.getMessage().equals(HttpStatus.BAD_REQUEST.toString()))
                .verify();

        Mockito.verify(repository, times(1)).findById("1");
        Mockito.verify(repository, never()).save(Mockito.any(Flower.class));
        try{
            Mockito.verify(flowerPublisher, never()).publish(any(FlowerDTO.class), any(String.class));
        } catch (JsonProcessingException e){
            System.out.println(e.getMessage());
        }
    }
}