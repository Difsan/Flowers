package co.flowers.usecases.interfaces;


import co.flowers.domain.dto.FlowerDTO;
import reactor.core.publisher.Mono;

@FunctionalInterface
public interface ReturnFlower {

    Mono<FlowerDTO> returnFlower(String flowerID, String customerId);
}
