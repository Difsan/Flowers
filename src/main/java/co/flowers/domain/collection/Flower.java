package co.flowers.domain.collection;

import jakarta.validation.constraints.NotBlank;
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
    private String id ;

    @NotNull (message = "Common Name can't be null")
    @NotBlank(message = "commonName can't be empty")
    private String commonName;

    @NotNull (message = "Family can't be null")
    @NotBlank(message = "Family can't be empty")
    private String family;

    @NotNull (message = "Color can't be null")
    @NotBlank(message = "Color can't be empty")
    private String color;

    @NotNull(message = "Type can't be null")
    @NotBlank(message = "Type can't be empty")
    private String type;

    @NotNull(message = "Origin can't be null")
    @NotBlank(message = "Origin can't be empty")
    private String origin;

    private Boolean inStock;

    public Flower(String commonName, String family,
                  String color, String type, String origin) {
        this.id = UUID.randomUUID().toString().substring(0, 10);
        this.commonName = commonName;
        this.family = family;
        this.color = color;
        this.type = type;
        this.origin = origin;
        this.inStock = true;
    }
}
