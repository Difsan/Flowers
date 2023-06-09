package co.flowers.router;

import co.flowers.domain.customer.CustomerDTO;
import co.flowers.domain.dto.FlowerDTO;
import co.flowers.usecases.*;
import co.flowers.usecases.interfaces.SaveFlower;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration

//@Component
//@AllArgsConstructor
//@RequestMapping("/flowers")
public class FlowerRouter {

    private WebClient customerAPI;

    public FlowerRouter(){ customerAPI = WebClient.create("http://localhost:8081");}
    @Bean
    public RouterFunction<ServerResponse> getAllFlowers (GetAllFlowerUseCase getAllFlowerUseCase){
        return route(GET("/flowers"),
                request -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromPublisher(getAllFlowerUseCase.get(), FlowerDTO.class))
                        //.onErrorResume(throwable -> ServerResponse.noContent().build()));
                        .onErrorResume(throwable -> ServerResponse.status(HttpStatus.NOT_FOUND).bodyValue(throwable.getMessage())));
    }

    @Bean
    public RouterFunction<ServerResponse> getFlowerById(GetFlowerByIdUseCase getFlowerByIdUseCase){
        return route(GET("flowers/{id}"),
                request -> getFlowerByIdUseCase.apply(request.pathVariable("id"))
                        .flatMap(flowerDTO -> ServerResponse.ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(flowerDTO))
                        //.onErrorResume(throwable -> ServerResponse.notFound().build())
                        .onErrorResume(throwable -> ServerResponse.status(HttpStatus.NOT_FOUND).bodyValue(throwable.getMessage()))
        );
    }

    @Bean
    public RouterFunction<ServerResponse> saveFlower (SaveFlowerUseCase saveFlowerUseCase){
        return route(POST("/flowers").and(accept(MediaType.APPLICATION_JSON)),
                request -> request.bodyToMono(FlowerDTO.class)
                        .flatMap(flowerDTO -> saveFlowerUseCase.save(flowerDTO)
                                .flatMap(result -> ServerResponse.status(201)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .bodyValue(result))
                                .onErrorResume(throwable -> ServerResponse.status(HttpStatus.BAD_REQUEST).bodyValue(throwable.getMessage())))
        );
    }



    @Bean
    public RouterFunction<ServerResponse> updateFlower(UpdateFlowerUseCase updateFlowerUseCase){
        return route(PUT("/flowers/{id}").and(accept(MediaType.APPLICATION_JSON)),
                request -> request.bodyToMono(FlowerDTO.class)
                        .flatMap(flowerDTO -> updateFlowerUseCase.update(request.pathVariable("id"),
                                        flowerDTO)
                                .flatMap(result -> ServerResponse.status(201)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .bodyValue(result))
                                .onErrorResume(throwable -> ServerResponse.status(HttpStatus.BAD_REQUEST)
                                        .bodyValue(throwable.getMessage()))
                        )
        );
    }

    @Bean
    public RouterFunction<ServerResponse> buyFlower(BuyFlowerUseCase buyFlowerUseCase){
        return route(POST("flowers/{id_f}/buy/{id_c}"),
                request ->
                        customerAPI.get()
                                .uri("/customers/"+request.pathVariable("id_c"))
                                .retrieve()
                                .bodyToMono(CustomerDTO.class)
                                .flatMap(customerDTO -> buyFlowerUseCase
                                        .buy(request.pathVariable("id_f"), customerDTO.getId())
                                        .flatMap(flowerDTO -> ServerResponse.ok()
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .bodyValue(customerDTO))
                                        //.onErrorResume(throwable -> ServerResponse.badRequest().build())));
                                        .onErrorResume(throwable -> ServerResponse.status(HttpStatus.BAD_REQUEST)
                                                .bodyValue(throwable.getMessage()))));
    }

    @Bean
    public RouterFunction<ServerResponse> returnFlower(ReturnFlowerUseCase returnFlowerUseCase){
        return route(POST("flowers/{id_f}/return/{id_c}"),
                request ->
                        customerAPI.get()
                                .uri("/customers/"+request.pathVariable("id_c"))
                                .retrieve()
                                .bodyToMono(CustomerDTO.class)
                                .flatMap(customerDTO -> returnFlowerUseCase
                                        .returnFlower(request.pathVariable("id_f"), customerDTO.getId())
                                        .flatMap(flowerDTO -> ServerResponse.ok()
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .bodyValue(customerDTO))
                                        .onErrorResume(throwable -> ServerResponse.status(HttpStatus.BAD_REQUEST)
                                                .bodyValue(throwable.getMessage())))
        );
    }

    @Bean
    public RouterFunction<ServerResponse> deleteFlower (DeleteFlowerUseCase deleteFlowerUseCase){
        return route(DELETE("/flowers/{id}"),
                request -> deleteFlowerUseCase.apply(request.pathVariable("id"))
                        .thenReturn(ServerResponse.ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue("Flower deleted"))
                        .flatMap(serverResponseMono -> serverResponseMono)
                        //.onErrorResume(throwable -> ServerResponse.status(HttpStatus.NOT_FOUND).build())
                        .onErrorResume(throwable -> ServerResponse.status(HttpStatus.NOT_FOUND).bodyValue(throwable.getMessage()))
        );
    }




}
