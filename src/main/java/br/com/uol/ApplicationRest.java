package br.com.uol;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ApplicationRest {
  @Getter
  @Setter
  private static String version;

  public static void main(String[] args) {
    SpringApplication.run(ApplicationRest.class, args);
  }
}
