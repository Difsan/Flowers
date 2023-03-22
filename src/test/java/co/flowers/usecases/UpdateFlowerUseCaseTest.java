package co.flowers.usecases;

import co.flowers.domain.collection.Flower;
import co.flowers.domain.dto.FlowerDTO;
import co.flowers.repository.IFlowerRepository;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class UpdateFlowerUseCaseTest {

    @Mock
    IFlowerRepository repository;

    ModelMapper mapper;

    UpdateFlowerUseCase updateFlowerUseCase;

    @BeforeEach
    void setUp() {
        mapper = new ModelMapper();
        updateFlowerUseCase = new UpdateFlowerUseCase(repository, mapper);
    }

    @Test
    @DisplayName("updateFlower_Success")
    void updateFlower(){

        var flower = new Flower("Daisy", "Asteraceae",
                "white", "crown", "Mediterranean coast", 50);
        flower.setId("1");

        var monoFlower = Mono.just(flower);

        Mockito.when(repository.findById(Mockito.any(String.class))).thenReturn(monoFlower);

        var flowerUpdated = new Flower("Daisy", "Asteraceae",
                "red", "English", "England", 25);
        flowerUpdated.setId("1");

        Mockito.when(repository.save(Mockito.any(Flower.class))).thenReturn(Mono.just(flowerUpdated));

        var result = updateFlowerUseCase.update("1",
                mapper.map(flowerUpdated, FlowerDTO.class));

        StepVerifier.create(result)
                .expectNext(mapper.map(flowerUpdated, FlowerDTO.class))
                .expectComplete()
                .verify();

        Mockito.verify(repository, times(1)).findById("1");
        Mockito.verify(repository, times(1)).save(flowerUpdated);
    }

    @Test
    @DisplayName("updateFlower_Failed")
    void updateFlower_Failed(){
        var flowerUpdated = new Flower("Daisy", "Asteraceae",
                "red", "English", "England", 25);
        flowerUpdated.setId("1");

        Mockito.when(repository.findById(Mockito.any(String.class)))
                .thenReturn(Mono.error(new Throwable(HttpStatus.NOT_FOUND.toString())));

        var result = updateFlowerUseCase.update("1",
                mapper.map(flowerUpdated, FlowerDTO.class));

        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable != null &&
                        throwable.getMessage().equals(HttpStatus.NOT_FOUND.toString()))
                .verify();

        Mockito.verify(repository).findById("1");
        Mockito.verify(repository, never()).save(flowerUpdated);
    }
}