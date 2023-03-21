package co.flowers.usecases;

import co.flowers.domain.dto.FlowerDTO;
import co.flowers.repository.IFlowerRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
public class GetAllFlowerUseCase implements Supplier<Flux<FlowerDTO>> {

    private final IFlowerRepository flowerRepository;
    private final ModelMapper mapper;

    @Override
    public Flux<FlowerDTO> get() {
        return this.flowerRepository
                .findAll()
                .switchIfEmpty(Flux.error(new Throwable(HttpStatus.NO_CONTENT.toString())))
                //.switchIfEmpty(Flux.empty())
                .map(flower -> mapper.map(flower, FlowerDTO.class));
    }
}
