package co.flowers.usecases;

import co.flowers.domain.collection.Flower;
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
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class GetAllFlowerUseCaseTest {

    @Mock
    IFlowerRepository repository;

    ModelMapper mapper;

    GetAllFlowerUseCase getAllFlowerUseCase;

    @BeforeEach
    void setUp() {
        mapper = new ModelMapper();
        getAllFlowerUseCase = new GetAllFlowerUseCase(repository, mapper);
    }

    @Test
    @DisplayName("getAllFlowers_Success")
    void getAllFlowers(){
        var fluxFlowers = Flux.just(new Flower("Daisy", "Asteraceae",
                        "white", "crown", "Mediterranean coast", 50),
                new Flower("Rose" , "Rosaceae", "pink", "Virginia rose", "Peru",
                97),
                new Flower("sunflower", "Asteraceae", "yellow", "sunflower", "Mexico",
                        70)
                );

        Mockito.when(repository.findAll()).thenReturn(fluxFlowers);

        var response = getAllFlowerUseCase.get();

        StepVerifier.create(response)
                .expectNextCount(3)
                .verifyComplete();

        Mockito.verify(repository).findAll();
    }

    @Test
    @DisplayName("getAllFlowers_Failed")
    void getAllFlowers_Failed(){
        Mockito.when(repository.findAll()).thenReturn(Flux.error(new Throwable(HttpStatus.NOT_FOUND.toString())));

        var response = getAllFlowerUseCase.get();

        StepVerifier.create(response)
                .expectErrorMatches(throwable -> throwable != null &&
                throwable.getMessage().equals(HttpStatus.NOT_FOUND.toString()))
                .verify();

        Mockito.verify(repository).findAll();
    }
}