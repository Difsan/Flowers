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

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class DeleteFlowerUseCaseTest {

    @Mock
    IFlowerRepository repository;

    ModelMapper mapper;

    DeleteFlowerUseCase deleteFlowerUseCase;

    @BeforeEach
    void setUp(){
        mapper = new ModelMapper();
        deleteFlowerUseCase = new DeleteFlowerUseCase(repository, mapper);
    }

    @Test
    @DisplayName("deleteFlowerById_Success")
    void deleteFlowerById(){
        var flower = new Flower("Rose" , "Rosaceae", "pink", "Virginia rose", "Peru");
        flower.setId("1");

        Mockito.when(repository.findById(Mockito.any(String.class))).thenReturn(Mono.just(flower));

        Mockito.when(repository.deleteById(Mockito.any(String.class))).thenReturn(Mono.empty());

        var result = deleteFlowerUseCase.apply("1");

        StepVerifier.create(result)
                .expectNext()
                .expectComplete()
                .verify();

        Mockito.verify(repository, times(1)).findById("1");
        Mockito.verify(repository, times(1)).deleteById("1");
    }

    @Test
    @DisplayName("deleteFlowerById_Failed")
    void deleteFlowerById_Failed(){
        Mockito.when(repository.findById(Mockito.any(String.class)))
                .thenReturn(Mono.error(new Throwable(HttpStatus.NOT_FOUND.toString())));

        var result = deleteFlowerUseCase.apply("1");

        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable != null &&
                        throwable.getMessage().equals(HttpStatus.NOT_FOUND.toString()))
                .verify();

        Mockito.verify(repository, times(1)).findById("1");
        Mockito.verify(repository, never()).deleteById("1");
    }
}