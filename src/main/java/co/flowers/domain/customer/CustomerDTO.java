package co.flowers.domain.customer;

import co.flowers.domain.collection.Flower;
import co.flowers.domain.dto.FlowerDTO;
import lombok.Data;

import java.util.List;

@Data
public class CustomerDTO {

    private String id;

    private String name;

    private String lastName;

    private String email;

    private String phoneNumber;

    private List<FlowerDTO> flowers;
}
