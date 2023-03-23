package co.flowers.publisher;


import co.flowers.domain.dto.FlowerDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FlowerEvent {
    private String customerId;
    private FlowerDTO flowerBought;
    private String eventType;
}
