package br.com.ufsc;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class Main {
  private static Logger logger = LogManager.getLogger(Main.class);

  public static void main(final String... args) throws Exception {
    logger.error("Class [{}]", logger.getClass());
    logger.trace("TRACE.");
    logger.info("INFO [{}].", "INFORMATION");
    logger.debug("DEBUG.");
    logger.warn("WARN");
    logger.error("ERROR. Level [{}]", logger.getLevel().toString());
  }
}