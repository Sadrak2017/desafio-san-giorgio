package br.com.uol.services;

import br.com.uol.enums.StatusPagamento;
import br.com.uol.handler.messages.ApiResponseRemessaPagamento;
import br.com.uol.handler.messages.Message;
import br.com.uol.model.Cobranca;
import br.com.uol.repositories.CobrancaRepository;
import br.com.uol.repositories.PedidoRepository;
import br.com.uol.repositories.VendedorRepository;
import br.com.uol.representation.dto.RemessaPagamento;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ProcessarPagamentoService {
  public final CobrancaRepository cobrancaRepository;
  public final PedidoRepository pedidoRepository;
  public final VendedorRepository vendedorRepository;
  public final Publisher publisher;
  private static final String globalMessage = "UOL Compass - 2024 - Processamento de Remessa de Pagamentos";

  @Value("${aws.queueName-total}")
  private String queueNameTotal;

  @Value("${aws.queueName-partial}")
  private String queueNamePartial;

  @Value("${aws.queueName-surplus}")
  private String queueNameSurplus;

  public ProcessarPagamentoService(CobrancaRepository r, PedidoRepository p, VendedorRepository v, Publisher k) {
    this.cobrancaRepository = r;
    this.pedidoRepository = p;
    this.vendedorRepository = v;
    this.publisher = k;
  }

  public ResponseEntity<ApiResponseRemessaPagamento> processar(RemessaPagamento remessaPagamento) {
    try {
      if (!vendedorRepository.existsById(remessaPagamento.getCodVendedor()))
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponseRemessaPagamento(HttpStatus.OK, ProcessarPagamentoService.class.getName(), List.of(new Message("Pagamentos não processado!", Message.MessageTypeEnum.I), new Message("Código do vendedor não consta na base! Verifique o ID informado.", Message.MessageTypeEnum.A)), "UOL Compass - 2024 - Processamento de Remessa de Pagamentos"));

      if (remessaPagamento.getPagamentos().isEmpty())
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponseRemessaPagamento(HttpStatus.OK, ProcessarPagamentoService.class.getName(), List.of(new Message("Nenhum pagamento informado para processamento", Message.MessageTypeEnum.I)), "UOL Compass - 2024 - Processamento de Remessa de Pagamentos"));

      remessaPagamento.getPagamentos().forEach(pgtos -> {

        Optional<Cobranca> cobranca = cobrancaRepository.findById(pgtos.getCodCobranca());

        cobranca.ifPresentOrElse(itemCobranca -> {

          StatusPagamento statusPagamento = StatusPagamento
            .fromCompare(pgtos.getValor().compareTo(itemCobranca.getValor()));
          itemCobranca.setStatus(statusPagamento.getCodigo());

          if(statusPagamento == StatusPagamento.TOTAL || statusPagamento == StatusPagamento.PARCIAL)
            itemCobranca.setValor(itemCobranca.getValor().subtract(pgtos.getValor())); //TODO: Precisa ser ajustado

          pgtos.setStatus(statusPagamento.getCodigo());
          pgtos.setLog(List.of(new Message(statusPagamento.getMessage(), Message.MessageTypeEnum.I)));

          ApiResponseRemessaPagamento msg = responseMenssage(
            new RemessaPagamento(remessaPagamento.getCodVendedor(), List.of(pgtos))
          );

          //Send Simple Queue Service - SQS
          processarStatusPagamentoSQS(statusPagamento, msg);
          //Persistence
          cobrancaRepository.save(itemCobranca);

        }, () -> pgtos.setLog(List.of(new Message("Código de cobrança não encontrado!", Message.MessageTypeEnum.A), new Message("Pagamento não realizado", Message.MessageTypeEnum.I))));
      });

      return ResponseEntity.status(HttpStatus.OK).body(responseMenssage(remessaPagamento));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponseRemessaPagamento(HttpStatus.INTERNAL_SERVER_ERROR, ProcessarPagamentoService.class.getName(), new Message("Falha ao gravar lote. " + (Objects.isNull(e.getMessage()) ? e.toString() : e.getMessage()), Message.MessageTypeEnum.E)));
    }
  }

  public void processarStatusPagamentoSQS(StatusPagamento status, ApiResponseRemessaPagamento message) {
    switch (status) {
      case TOTAL:
        publisher.publishMessage(message, queueNameTotal);
        break;
      case PARCIAL:
        publisher.publishMessage(message, queueNamePartial);
        break;
      case EXCEDENTE:
        publisher.publishMessage(message, queueNameSurplus);
        break;
      default:
        throw new RuntimeException("Opção desconhecida");
    }
  }

  private static ApiResponseRemessaPagamento responseMenssage(RemessaPagamento remessaPagamento) {
    return new ApiResponseRemessaPagamento(HttpStatus.OK,
      ProcessarPagamentoService.class.getName(),
      List.of(new Message("Pagamentos processado!", Message.MessageTypeEnum.O)),
      remessaPagamento, globalMessage
    );
  }
}
