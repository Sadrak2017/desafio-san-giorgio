package br.com.uol.representation.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "vendedor")
public class Vendedor implements Serializable {

  @Id
  @Schema(description = "Código", example = "1000")
  private Long codigo;

  @Schema(description = "Nome", example = "João")
  private String nome;

}
