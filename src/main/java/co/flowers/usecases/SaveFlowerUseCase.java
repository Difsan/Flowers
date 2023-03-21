package co.flowers.usecases;

import co.flowers.domain.collection.Flower;
import co.flowers.domain.dto.FlowerDTO;
import co.flowers.repository.IFlowerRepository;
import co.flowers.usecases.interfaces.SaveFlower;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class SaveFlowerUseCase implements SaveFlower {

    private final IFlowerRepository flowerRepository;

    private final ModelMapper mapper;

    @Override
    public Mono<FlowerDTO> save(FlowerDTO flowerDTO) {
        return this.flowerRepository
                .save(mapper.map(flowerDTO, Flower.class))
                .switchIfEmpty(Mono.empty())
                .map(flower -> mapper.map(flower, FlowerDTO.class));
    }
}
