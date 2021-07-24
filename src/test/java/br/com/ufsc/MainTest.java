package br.com.ufsc;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MainTest {
  @Test
  public void testLogging() throws Exception {
    Main.main("nnnn");
  }

  @Test
  public void testOpenFile() throws Exception {
    InputStream in = Main.class.getResourceAsStream("/applicationConfig.json");
    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
    reader.lines().forEach(System.out::println);
  }

  @Test
  public void convertFromJson() throws Exception {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.disable(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES);
    objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

    InputStream in = Main.class.getResourceAsStream("/applicationConfig.json");
    BufferedReader reader = new BufferedReader(new InputStreamReader(in));

    Config config = objectMapper.readValue(in, Config.class);
    assertEquals(500, config.getLightProcessingTimeMs());
  }
}