package br.com.uol.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import java.io.Serializable;
import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Table(name = "pagamento")
@Entity(name = "pagamento")
public class Pagamento implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO, generator = "UUID")
  @GenericGenerator(name = "UUID", strategy = "uuid2" )
  @Column(name = "codPagamento")
  private String codPagamento;

  @Column(name = "codCobranca")
  private String codPedido;

  @Column(name = "status", nullable = false)
  private Integer status;

  @Column(name = "atualizacao", nullable = false)
  private Timestamp dataAtualizacao = new Timestamp(System.currentTimeMillis());

  @Column(name = "inclusao", updatable = false, nullable = false)
  private Timestamp dataInclusao = new Timestamp(System.currentTimeMillis());

}
