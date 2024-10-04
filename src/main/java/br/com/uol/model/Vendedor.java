package br.com.uol.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.io.Serializable;
import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Table(name = "vendedor")
@Entity(name = "vendedor")
public class Vendedor implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "codVendedor")
  private Long codVendedor;

  @Column(name = "nome")
  private String nome;

  @Column(name = "atualizacao", nullable = false)
  private Timestamp dataAtualizacao = new Timestamp(System.currentTimeMillis());

  @Column(name = "inclusao", updatable = false, nullable = false)
  private Timestamp dataInclusao = new Timestamp(System.currentTimeMillis());

}
