package co.flowers.domain.customer;

import co.flowers.domain.collection.Flower;
import co.flowers.domain.dto.FlowerDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDTO {

    private String id;

    private String name;

    private String lastName;

    private String email;

    private String phoneNumber;

    private Set<FlowerDTO> flowers = new HashSet<>();
}
