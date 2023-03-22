package co.flowers.usecases;

import co.flowers.domain.collection.Flower;
import co.flowers.domain.dto.FlowerDTO;
import co.flowers.repository.IFlowerRepository;
import co.flowers.usecases.interfaces.SaveFlower;
import co.flowers.usecases.interfaces.UpdateFlower;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UpdateFlowerUseCase implements UpdateFlower {

    private final IFlowerRepository flowerRepository;

    private final ModelMapper mapper;

    @Override
    public Mono<FlowerDTO> update(String id, FlowerDTO flowerDTO) {
        return this.flowerRepository
                .findById(id)
                .switchIfEmpty(Mono.error(new Throwable(HttpStatus.NOT_FOUND.toString())))
                .flatMap(flower -> {
                    flowerDTO.setId(flowerDTO.getId());
                    return flowerRepository.save(mapper.map(flowerDTO, Flower.class));
                })
                .map(flower -> mapper.map(flower, FlowerDTO.class));
    }
}
