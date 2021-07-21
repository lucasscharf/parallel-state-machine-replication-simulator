package br.com.ufsc;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import java.io.InputStreamReader;
import org.apache.logging.log4j.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.io.BufferedReader;
import java.io.InputStream;

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