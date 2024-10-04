package br.com.uol.representation.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Pedido")
public class Pedido implements Serializable {

  @Schema(description = "Código pedido (UUID) - RFC-4122", example = "1d0cd4f1-dffe-447f-ae1e-8336f72e5bb5")
  private String codPedido;

  @Schema(description = "Valor total", example = "152.00")
  private BigDecimal valor;

  @Schema(description = "Status do pagamento [1-Total, 2-Parcial, 3-Excedente]", example = "1")
  private Integer status;

}
