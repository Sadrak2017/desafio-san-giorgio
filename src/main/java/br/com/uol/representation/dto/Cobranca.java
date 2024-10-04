package br.com.uol.representation.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Cobrança")
public class Cobranca implements Serializable {

  @Schema(description = "Código pedido (UUID) - RFC-4122", example = "1d0cd4f1-dffe-447f-ae1e-8336f72e5bb5")
  private String codCobranca;

  @Schema(description = "Valor cobrança", example = "152.00")
  private BigDecimal valor;

  @Schema(description = "Status [0:Pendente, -1:Finalizado, 1:Parcial", example = "2")
  private Integer status;

  @Schema(description = "Data atualização", example = "3")
  private Date dataAtualizacao;

}
