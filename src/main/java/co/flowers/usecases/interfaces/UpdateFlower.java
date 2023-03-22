package co.flowers.usecases.interfaces;

import co.flowers.domain.dto.FlowerDTO;
import reactor.core.publisher.Mono;

@FunctionalInterface
public interface UpdateFlower {

    Mono<FlowerDTO> update(String id, FlowerDTO flowerDTO);
}
