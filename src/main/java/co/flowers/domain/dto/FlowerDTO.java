package co.flowers.domain.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FlowerDTO {

    private String id;

    @NotNull (message = "Common Name can't be null")
    private String commonName;

    @NotNull (message = "Scientific Name can't be null")
    private String scientificName;

    @NotNull(message = "Type can't be null")
    private String type;

    @NotNull(message = "Origin can't be null")
    private String origin;

    @NotNull(message = "Stock can't be null")
    private Integer stock;

    private Boolean available;


}
