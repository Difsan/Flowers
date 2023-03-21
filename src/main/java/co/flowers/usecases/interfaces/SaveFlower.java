package co.flowers.usecases.interfaces;

import co.flowers.domain.dto.FlowerDTO;
import reactor.core.publisher.Mono;

@FunctionalInterface
public interface SaveFlower {
    Mono<FlowerDTO> save(FlowerDTO flowerDTO);
}
