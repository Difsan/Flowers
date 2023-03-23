package co.flowers.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FlowerDTO {

    private String id;

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

    private Boolean inStock = true;

}
