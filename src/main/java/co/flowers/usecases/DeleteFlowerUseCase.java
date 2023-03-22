package co.flowers.usecases;

import co.flowers.repository.IFlowerRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class DeleteFlowerUseCase implements Function<String, Mono<Void>> {

    private final IFlowerRepository flowerRepository;
    private final ModelMapper mapper;


    @Override
    public Mono<Void> apply(String id) {
        return this.flowerRepository
                .findById(id)
                .switchIfEmpty(Mono.error(new Throwable(HttpStatus.NOT_FOUND.toString())))
                .flatMap(flower -> this.flowerRepository.deleteById(flower.getId()));
    }
}
