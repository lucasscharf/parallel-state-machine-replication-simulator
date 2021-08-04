package br.com.ufsc;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ReportGenerator {
  private static final int MILLI_MULTIPLIER = 1000;
  private LocalDateTime startTime;
  private LocalDateTime endTime;
  private Config config;
  private Long theoreticalTimeMs;
  private Logger logger = LogManager.getLogger();

  public ReportGenerator(List<Command> commands, Config config) {
    this.config = config;
    theoreticalTimeMs = 0L;
    for (Command command : commands) {
      theoreticalTimeMs += command.getWeight().getProcessingTimeMs();
    }
  }

  public void startRegistering() {
    startTime = LocalDateTime.now();
  }

  public void registerEndTime() {
    endTime = LocalDateTime.now();
  }

  public void generateReport() {
    Long makepan = Duration.between(startTime, endTime).toMillis();
    Long differeceBetweenTheoreticalTimeAndMakespamMs = makepan - theoreticalTimeMs;

    Double speedup = (double) theoreticalTimeMs / (double) makepan;
    Double efficiency = 1.0 / speedup;
    Long meanThroughput = config.getNumberOfCommands() / (makepan / MILLI_MULTIPLIER);

    logger.info("Number of threads [{}]", config.getNumberOfThreads());
    logger.trace("theoretical time: [{}] ms", theoreticalTimeMs);
    logger.trace("differeceBetweenTheoreticalTimeAndMakespamMs: [{}] ms", differeceBetweenTheoreticalTimeAndMakespamMs);
    logger.info("makepan: [{}] ms", makepan);
    logger.info("speedup: [{}]x", speedup);
    logger.info("efficiency: [{}]%", efficiency);

    logger.info("meanThroughput: [{}] messages/s", meanThroughput);
  }
}
