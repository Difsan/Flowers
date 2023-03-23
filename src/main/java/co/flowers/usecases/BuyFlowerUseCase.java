package co.flowers.usecases;

import co.flowers.domain.collection.Flower;
import co.flowers.domain.dto.FlowerDTO;
import co.flowers.publisher.FlowerPublisher;
import co.flowers.repository.IFlowerRepository;
import co.flowers.usecases.interfaces.BuyFlower;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class BuyFlowerUseCase implements BuyFlower {

    private final IFlowerRepository flowerRepository;

    private final ModelMapper mapper;

    private final FlowerPublisher flowerPublisher;

    @Override
    public Mono<FlowerDTO> buy(String flowerID, String customerId) {
        System.out.println("Customer id: " +customerId);
        System.out.println("Flower: "+flowerID);
        return this.flowerRepository
                .findById(flowerID)
                .switchIfEmpty(Mono.empty())
                //.filter(Flower::getInStock)
                //.switchIfEmpty(Mono.empty())
                .flatMap(flower -> {
                    flower.setInStock(false);
                    return this.flowerRepository.save(flower);
                })
                .map(flower -> mapper.map(flower, FlowerDTO.class))
                .doOnSuccess(flowerDTO -> {
                    try {
                        flowerPublisher.publish(flowerDTO, customerId);
                    } catch (JsonProcessingException e){
                        throw new RuntimeException(e);
                    }
                });
    }
}
