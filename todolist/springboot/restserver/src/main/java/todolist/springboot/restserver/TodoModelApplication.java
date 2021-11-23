package todolist.springboot.restserver;

import java.util.EnumSet;

import com.fasterxml.jackson.databind.Module;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import todolist.json.TodoPersistence;
import todolist.json.TodoPersistence.TodoModelParts;

/**
 * The Spring application.
 */
@SpringBootApplication
public class TodoModelApplication {

  @Bean
  public Module objectMapperModule() {
    return TodoPersistence.createJacksonModule(EnumSet.of(TodoModelParts.LISTS));
  }

  public static void main(String[] args) {
    SpringApplication.run(TodoModelApplication.class, args);
  }
}
