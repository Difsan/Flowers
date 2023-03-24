package co.flowers.usecases;

import co.flowers.domain.dto.FlowerDTO;
import co.flowers.publisher.FlowerPublisher;
import co.flowers.repository.IFlowerRepository;
import co.flowers.usecases.interfaces.BuyFlower;
import co.flowers.usecases.interfaces.ReturnFlower;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ReturnFlowerUseCase implements ReturnFlower {

    private final IFlowerRepository flowerRepository;

    private final ModelMapper mapper;

    private final FlowerPublisher flowerPublisher;

    @Override
    public Mono<FlowerDTO> returnFlower(String flowerID, String customerId) {
        System.out.println("Customer id: " +customerId);
        System.out.println("Flower: "+flowerID);
        return this.flowerRepository
                .findById(flowerID)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("There is not " +
                        "flower with id: " + flowerID)))
                //.filter(Flower::getInStock)
                //.switchIfEmpty(Mono.empty())
                .flatMap(flower -> {
                    flower.setInStock(true);
                    return this.flowerRepository.save(flower);
                })
                .map(flower -> mapper.map(flower, FlowerDTO.class))
                .doOnSuccess(flowerDTO -> {
                    try {
                        flowerPublisher.publishReturn(flowerDTO, customerId);
                    } catch (JsonProcessingException e){
                        throw new RuntimeException(e);
                    }
                });
    }
}
