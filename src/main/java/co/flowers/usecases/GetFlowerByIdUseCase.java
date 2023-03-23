package co.flowers.usecases;

import co.flowers.domain.dto.FlowerDTO;
import co.flowers.repository.IFlowerRepository;
import com.mongodb.Function;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class GetFlowerByIdUseCase implements Function<String, Mono<FlowerDTO>> {

    private final IFlowerRepository flowerRepository;
    private final ModelMapper mapper;

    @Override
    public Mono<FlowerDTO> apply(String id) {
        return this.flowerRepository.findById(id)
                //.switchIfEmpty(Mono.error(new Throwable(HttpStatus.NOT_FOUND.toString())))
                .switchIfEmpty(Mono.error(new IllegalArgumentException("There is not " +
                        "flower with id: " + id)))
                .map(flower -> mapper.map(flower, FlowerDTO.class));
    }
}
