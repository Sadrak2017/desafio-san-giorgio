package br.com.uol.services;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import br.com.uol.enums.StatusPagamento;
import br.com.uol.handler.messages.ApiResponseRemessaPagamento;
import br.com.uol.model.Cobranca;
import br.com.uol.repositories.CobrancaRepository;
import br.com.uol.repositories.PedidoRepository;
import br.com.uol.repositories.VendedorRepository;
import br.com.uol.representation.dto.Pagamento;
import br.com.uol.representation.dto.RemessaPagamento;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.stubbing.Answer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.math.BigDecimal;
import java.util.*;

class ProcessarPagamentoServiceTest {

  private ProcessarPagamentoService service;
  private final CobrancaRepository cobrancaRepository = mock(CobrancaRepository.class);
  private final PedidoRepository pedidoRepository = mock(PedidoRepository.class);
  private final VendedorRepository vendedorRepository = mock(VendedorRepository.class);
  private final Publisher publisher = mock(Publisher.class);

  @BeforeEach
  void setUp() {
    service = new ProcessarPagamentoService(cobrancaRepository, pedidoRepository, vendedorRepository, publisher);
  }

  @Test
  void testVendedorInexistenteRetornaBadRequest() {
    RemessaPagamento remessa = new RemessaPagamento();
    remessa.setCodVendedor(999L);
    when(vendedorRepository.existsById(999L)).thenReturn(false);
    ResponseEntity<ApiResponseRemessaPagamento> response = service.processar(remessa);
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertTrue(response.getBody().getMessages().stream().anyMatch(msg -> msg.getMessage().contains("Código do vendedor não consta na base")));
  }

  @Test
  void testListaPagamentosVaziaRetornaBadRequest() {
    RemessaPagamento remessa = new RemessaPagamento();
    remessa.setCodVendedor(1L);
    remessa.setPagamentos(new ArrayList<>());
    when(vendedorRepository.existsById(1L)).thenReturn(true);
    ResponseEntity<ApiResponseRemessaPagamento> response = service.processar(remessa);
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertTrue(Objects.requireNonNull(response.getBody()).getMessages().stream().anyMatch(msg -> msg.getMessage().contains("Nenhum pagamento informado")));
  }

  @Test
  void testProcessamentoPagamentoTotal() {
    RemessaPagamento remessa = new RemessaPagamento();
    Pagamento pgtos = new Pagamento();
    pgtos.setCodCobranca("1d0cd4f1-dffe-447f-ae1e-8336f72e5bb5");
    pgtos.setValor(new BigDecimal("100.00"));
    remessa.setCodVendedor(1L);
    remessa.setPagamentos(List.of(pgtos));

    br.com.uol.model.Cobranca cobranca = new br.com.uol.model.Cobranca();
    cobranca.setValor(new BigDecimal("100.00"));
    Answer<Optional<Cobranca>> expected = invocationOnMock -> Optional.of(cobranca);

    when(vendedorRepository.existsById(1L)).thenReturn(true);
    when(cobrancaRepository.findById(pgtos.getCodCobranca())).then(expected);
    doNothing().when(publisher).publishMessage(any(), anyString());

    ResponseEntity<ApiResponseRemessaPagamento> response = service.processar(remessa);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(StatusPagamento.TOTAL.getCodigo(), Objects.requireNonNull(response.getBody()).getData().getPagamentos().getFirst().getStatus());
  }

  @Test
  void testProcessamentoPagamentoParcial() {
    RemessaPagamento remessa = new RemessaPagamento();
    Pagamento pgtos = new Pagamento();
    pgtos.setCodCobranca("1d0cd4f1-dffe-447f-ae1e-8336f72e5bb5");
    pgtos.setValor(new BigDecimal("50.00"));
    remessa.setCodVendedor(1L);
    remessa.setPagamentos(List.of(pgtos));

    br.com.uol.model.Cobranca cobranca = new br.com.uol.model.Cobranca();
    cobranca.setValor(new BigDecimal("100.00"));
    Answer<Optional<Cobranca>> expected = invocationOnMock -> Optional.of(cobranca);

    when(vendedorRepository.existsById(1L)).thenReturn(true);
    when(cobrancaRepository.findById(pgtos.getCodCobranca())).then(expected);
    doNothing().when(publisher).publishMessage(any(), anyString());

    ResponseEntity<ApiResponseRemessaPagamento> response = service.processar(remessa);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(StatusPagamento.PARCIAL.getCodigo(), Objects.requireNonNull(response.getBody()).getData().getPagamentos().getFirst().getStatus());
  }

  @Test
  void testExcecaoNoProcessamento() {
    RemessaPagamento remessa = new RemessaPagamento();
    remessa.setCodVendedor(1L);
    when(vendedorRepository.existsById(1L)).thenReturn(true);
    when(cobrancaRepository.findById(anyString())).thenThrow(new RuntimeException("Erro de banco de dados"));

    ResponseEntity<ApiResponseRemessaPagamento> response = service.processar(remessa);

    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    assertTrue(Objects.requireNonNull(response.getBody()).getMessages().stream()
      .anyMatch(msg -> msg.getMessage().contains("Falha ao processar pagamentos"))
    );
  }


}
