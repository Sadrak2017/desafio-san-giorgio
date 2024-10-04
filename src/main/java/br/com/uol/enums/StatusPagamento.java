package br.com.uol.enums;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import java.io.Serializable;

@Getter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public enum StatusPagamento implements Serializable {

  TOTAL(0, "Pagamento finalizado com sucesso."),
  PARCIAL(-1, "Pagamento parcial realizado com sucesso."),
  EXCEDENTE(1, "Pagamento excedente"),
  NA(99, "Operação desconhecida");

  private final Integer codigo;
  private final String message;

  public static StatusPagamento fromCompare(int cod) {
    for (StatusPagamento statusPagamento : StatusPagamento.values()) {
      if (statusPagamento.getCodigo().equals(cod)) {
        return statusPagamento;
      }
    }
    return NA;
  }
}
