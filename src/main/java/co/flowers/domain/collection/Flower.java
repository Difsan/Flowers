package co.flowers.domain.collection;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Data
@NoArgsConstructor
@Document(collection = "flowers")
public class Flower {

    @Id
    private String id = UUID.randomUUID().toString().substring(0, 10);

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

    private Boolean available = true;


}
