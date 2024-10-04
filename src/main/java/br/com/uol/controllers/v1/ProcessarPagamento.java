package br.com.uol.controllers.v1;

import br.com.uol.handler.messages.ApiResponseRemessaPagamento;
import br.com.uol.representation.dto.RemessaPagamento;
import br.com.uol.services.ProcessarPagamentoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@Tag(name = "Processamento de pagamento", description = "Gestão de controle de pagamentos")
@CrossOrigin(origins = "*")
@RequestMapping(value = "/payment", produces = "application/json")
public class ProcessarPagamento {

  private final ProcessarPagamentoService processarPagamentoService;

  @Autowired
  public ProcessarPagamento(ProcessarPagamentoService processarPagamentoService) {
    this.processarPagamentoService = processarPagamentoService;
  }

  @Operation(
    description = "Solicita processamento de remessa de pagamentos.",
    responses = {@io.swagger.v3.oas.annotations.responses.ApiResponse(content =
      @Content(schema = @Schema(implementation = ApiResponseRemessaPagamento.class)))},
    summary = "Retorna status da operação.",
    security = {@SecurityRequirement(name = "Authorization Bearer")}
  )
  @PostMapping(value = "/process", consumes = "application/json", produces = "application/json")
  public ResponseEntity<ApiResponseRemessaPagamento> criar(
    @RequestBody RemessaPagamento remessaPagamento) {
    return processarPagamentoService.processar(remessaPagamento);
  }

}
