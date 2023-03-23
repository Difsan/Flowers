package co.flowers.usecases.interfaces;


import co.flowers.domain.dto.FlowerDTO;
import reactor.core.publisher.Mono;

@FunctionalInterface
public interface BuyFlower {

    Mono<FlowerDTO> buy(String flowerID, String customerId);
}
