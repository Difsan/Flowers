package co.flowers.router;

import co.flowers.domain.dto.FlowerDTO;
import co.flowers.usecases.GetAllFlowerUseCase;
import co.flowers.usecases.SaveFlowerUseCase;
import co.flowers.usecases.interfaces.SaveFlower;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
//@RequestMapping("/flowers")
public class FlowerRouter {

    @Bean
    public RouterFunction<ServerResponse> getAllFlowers (GetAllFlowerUseCase getAllFlowerUseCase){
        return route(GET("/flowers"),
                request -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromPublisher(getAllFlowerUseCase.get(), FlowerDTO.class))
                        .onErrorResume(throwable -> ServerResponse.noContent().build()));
    }

    @Bean
    public RouterFunction<ServerResponse> saveFlower (SaveFlowerUseCase saveFlowerUseCase){
        return route(POST("/flowers").and(accept(MediaType.APPLICATION_JSON)),
                request -> request.bodyToMono(FlowerDTO.class)
                        .flatMap(flowerDTO -> saveFlowerUseCase.save(flowerDTO)
                                .flatMap(result -> ServerResponse.status(201)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .bodyValue(result))
                                .onErrorResume(throwable -> ServerResponse.status(HttpStatus.BAD_REQUEST).build()))
        );
    }
}
