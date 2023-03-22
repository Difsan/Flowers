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
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class SaveFlowerUseCaseTest {

    @Mock
    IFlowerRepository repository;

    ModelMapper mapper;

    SaveFlowerUseCase saveFlowerUseCase;

    @BeforeEach
    void setUp() {
        mapper = new ModelMapper();
        saveFlowerUseCase = new SaveFlowerUseCase(repository, mapper);
    }

    @Test
    @DisplayName("saveFlower_Success")
    void saveFlower(){
        var flower =  new Flower("sunflower", "Asteraceae", "yellow",
                "sunflower", "Mexico");

        var monoFlower = Mono.just(flower);

        Mockito.when(repository.save(Mockito.any(Flower.class))).thenReturn(monoFlower);

        var result = saveFlowerUseCase.save(mapper.map(flower, FlowerDTO.class));

        StepVerifier.create(result)
                .expectNext(mapper.map(flower, FlowerDTO.class))
                .expectComplete()
                .verify();

        Mockito.verify(repository, times(1)).save(flower);
    }

    @Test
    @DisplayName("saveFlower_Failed")
    void saveFlower_Failed(){
        var flower =  new Flower("sunflower", "Asteraceae", "yellow",
                "sunflower", "Mexico");

        Mockito.when(repository.save(Mockito.any(Flower.class)))
                .thenReturn(Mono.error(new Throwable(HttpStatus.BAD_REQUEST.toString())));

        var result = saveFlowerUseCase.save(mapper.map(flower, FlowerDTO.class));

        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable != null &&
                        throwable.getMessage().equals(HttpStatus.BAD_REQUEST.toString()))
                .verify();

        Mockito.verify(repository, times(1)).save(flower);
    }
}