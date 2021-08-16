package br.com.ufsc;

import org.apache.logging.log4j.Logger;

import java.util.Date;

import org.apache.logging.log4j.LogManager;

public class Main {
  private static Logger logger = LogManager.getLogger(Main.class);

  public static void main(final String... args) throws Exception {
    logger.error("Class [{}]", logger.getClass());
    logger.trace("TRACE.");
    logger.info("INFO [{}].", "INFORMATION", "teste");
    logger.debug("DEBUG.", 1, 2, new Date());
    logger.warn("WARN");
    logger.error("ERROR. Level [{}]", logger.getLevel().toString());
  }
}