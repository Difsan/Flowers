package co.flowers.publisher;

import co.flowers.domain.dto.FlowerDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class FlowerPublisher {

    private final RabbitTemplate rabbitTemplate;

    private final ObjectMapper objectMapper;

    public FlowerPublisher(RabbitTemplate rabbitTemplate, ObjectMapper objectMapper) {
        this.rabbitTemplate = rabbitTemplate;
        this.objectMapper = objectMapper;
    }

    public void publish(FlowerDTO flowerDTO, String customerId) throws JsonProcessingException {

        String message = objectMapper.writeValueAsString(new FlowerEvent(customerId, flowerDTO, "buy"));

        rabbitTemplate.convertAndSend("flowers-exchange-events", "events.flower.routing.key", message);
    }

    public void publishReturn(FlowerDTO flowerDTO, String customerId) throws JsonProcessingException {

        String message = objectMapper.writeValueAsString(new FlowerEvent(customerId, flowerDTO, "return"));

        rabbitTemplate.convertAndSend("flowers-exchange-events", "events.flower.routing.key", message);
    }
}
