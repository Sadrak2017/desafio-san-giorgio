package br.com.uol.services;

import br.com.uol.handler.messages.ApiResponseRemessaPagamento;
import br.com.uol.handler.messages.MessageSQS;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.GetQueueUrlResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.UUID;

@Service
@Log4j2
public class Publisher {

  private final AmazonSQS amazonSQSClient;
  private final ObjectMapper objectMapper;

  public Publisher(AmazonSQS amazonSQSClient, ObjectMapper objectMapper) {
    this.amazonSQSClient = amazonSQSClient;
    this.objectMapper = objectMapper;
  }

  public void publishMessage(ApiResponseRemessaPagamento message, String nameQueue) {
    try {
      GetQueueUrlResult queueUrl = amazonSQSClient.getQueueUrl(nameQueue);
      var body = MessageSQS.builder()
        .id(UUID.randomUUID().toString())
        .content(message)
        .createdAt(new Date()).build();
      amazonSQSClient.sendMessage(queueUrl.getQueueUrl(), objectMapper.writeValueAsString(body));
    } catch (Exception e) {
      log.error("Queue Exception Message: {}", e.getMessage());
    }

  }
}
