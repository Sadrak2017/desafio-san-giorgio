package br.com.uol.handler.messages;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.servlet.http.HttpServletResponse;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import java.util.List;

@Setter
@Getter
@Slf4j
@EqualsAndHashCode(callSuper = false)
@Schema(description = "Resposta padrão da API")
public class ApiResponse<T> extends ApiResponseMessage {

  @Schema(description = "Dados da resposta")
  private transient T data;

  private ApiResponse(HttpStatus status, String globalMessage) {
    super(status, globalMessage);
    status();
  }

  private ApiResponse(T body, HttpStatus status) {
    super(status, null);
    this.data = body;
    status();
  }

  private ApiResponse(HttpStatus status) {
    super(status, null);
    status();
  }

  private void status() {
    try {
      ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
      HttpServletResponse response = requestAttributes.getResponse();
      response.setStatus(getStatus() != null ? getStatus().value() : HttpStatus.OK.value());
    } catch (Exception ignored) {
      log.warn("::api-response:: a requisição não possui um objeto de response");
    }
  }

  /**
   * Adiciona as informações no Objeto de retorno , logo em seguida pode ser usado em conjunto com o método body para adicionar o conteuo da requisisição
   *
   * @param status        status status da requisição
   * @param globalMessage mensagem global opcional
   * @implNote ApiResponse.<?>>info(HttpStatus.CREATED, "Mensagem de retorno(Opcional)").body(Objeto);
   */
  public static <T> ApiResponse<T> info(HttpStatus status, String globalMessage) {
    return new ApiResponse<>(status, globalMessage);
  }

  /**
   * Adiciona as informações no Objeto de retorno , logo em seguida pode ser usado em conjunto com o método body para adicionar o conteuo da requisisição
   *
   * @param status status status da requisição
   * @implNote ApiResponse.<?>>info(HttpStatus.CREATED, "Mensagem de retorno(Opcional)").body(Objeto);
   */
  public static <T> ApiResponse<T> info(HttpStatus status) {
    return new ApiResponse<>(status);
  }

  /**
   * Retorna com status definido no parametros
   *
   * @param status status da requisição
   * @param body   conteudo da requisição
   */
  public static <T> ApiResponse<T> info(HttpStatus status, T body) {
    return new ApiResponse<>(body, status);
  }

  /**
   * Retorna com status definido no parametros
   *
   * @param status        status da requisição
   * @param globalMessage mensagem global opcional
   * @param body          conteudo da requisição
   */
  public static <T> ApiResponse<T> info(HttpStatus status, String globalMessage, T body) {
    return new ApiResponse<T>(status).body(body).global(globalMessage);
  }

  /**
   * Retorna com status definido no parametros
   *
   * @param status        status da requisição
   * @param globalMessage mensagem global opcional
   * @param message       conteudo da requisição
   */
  public static <T> ApiResponse<T> info(HttpStatus status, String globalMessage, Message message) {
    return new ApiResponse<T>(status).messages(message).global(globalMessage);
  }

  /**
   * Retorna com status definido no parametros
   *
   * @param status        status da requisição
   * @param globalMessage mensagem global opcional
   * @param body          conteudo da requisição
   */
  public static <T> ApiResponse<T> info(HttpStatus status, String globalMessage, T body, Message message) {
    return new ApiResponse<T>(status).body(body).messages(message).global(globalMessage);
  }

  /**
   * Retorna com status 201
   *
   * @param body - conteudo do body
   */
  public static <T> ApiResponse<T> created(T body) {
    return new ApiResponse<>(body, HttpStatus.CREATED);
  }

  public ApiResponse<T> body(T content) {
    this.data = content;
    return this;
  }

  /**
   * Adiciona mensagens ao response
   *
   * @param message {@link Message}
   * @return {@link ApiResponse<T>}
   */
  public ApiResponse<T> messages(Message message) {
    this.addMessage(message);
    return this;
  }

  /**
   * Adiciona mensagens ao response
   *
   * @param global {@link Message}
   * @return {@link ApiResponse<T>}
   */
  public ApiResponse<T> global(String global) {
    super.setGlobalMessage(global);
    return this;
  }

  /**
   * Adiciona mensagens ao response
   *
   * @param mensagens - Lista de String de mensagens
   * @return {@link ApiResponse<T>}
   */
  public ApiResponse<T> messages(List<String> mensagens) {
    mensagens.forEach(msg -> {
      Message message = new Message();
      message.setMessage(msg);
      this.addMessage(message);
    });
    return this;
  }

  /**
   * Adiciona mensagens ao response
   *
   * @param mensagem Mensagem
   * @param type     Tipo da mensagem {@link Message.MessageTypeEnum }
   * @return {@link ApiResponse<T>}
   */
  public ApiResponse<T> messages(String mensagem, Message.MessageTypeEnum type) {
    this.addMessage(new Message(mensagem, type));
    return this;
  }

  /**
   * Adiciona mensagens ao response
   *
   * @param mensagem Mensagem
   * @return {@link ApiResponse<T>}
   */
  public ApiResponse<T> messages(String mensagem) {
    Message message = new Message();
    message.setMessage(mensagem);
    return this;
  }

}
