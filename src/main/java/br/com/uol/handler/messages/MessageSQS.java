package br.com.uol.handler.messages;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageSQS {
  private String id;
  private ApiResponseRemessaPagamento content;
  private Date createdAt;
}
