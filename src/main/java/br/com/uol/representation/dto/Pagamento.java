package br.com.uol.representation.dto;

import br.com.uol.handler.messages.Message;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Pagamento")
public class Pagamento implements Serializable {

  @Schema(description = "Código pagamento(UUID) - RFC-4122", example = "1d0cd4f1-dffe-447f-ae1e-8336f72e5bb5")
  private String codPagamento;

  @Schema(description = "Código cobrança (UUID) - RFC-4122", example = "1d0cd4f1-dffe-447f-ae1e-8336f72e5bb5")
  private String codCobranca;

  @Schema(description = "Código pedido (UUID) - RFC-4122", example = "1d0cd4f1-dffe-447f-ae1e-8336f72e5bb5")
  private String codPedido;

  @Schema(description = "Valor do pagamento", example = "200.00")
  private BigDecimal Valor;

  @Schema(description = "Status do pagamento [1-Total, 2-Parcial, 3-Excedente]")
  private Integer status;

  @Schema(description = "Log de processamento")
  private List<Message> log;

}
