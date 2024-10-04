package br.com.uol.utils.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(
  code = HttpStatus.NOT_FOUND,
  reason = "info.patterns-required"
)
public class TPatternsRequiredException extends RuntimeException {
  @Serial
  private static final long serialVersionUID = 1L;

  public TPatternsRequiredException() {
  }
}
