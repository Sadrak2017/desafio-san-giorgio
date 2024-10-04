package br.com.uol.representation.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Remessa de pagamentos")
public class RemessaPagamento implements Serializable {
  @Serial
  private static final long serialVersionUID = 1L;

  @Schema(description = "Código vendor", example = "1473")
  private Long codVendedor;

  @Schema(description = "Lista de pagamentos")
  private List<Pagamento> pagamentos;

}
