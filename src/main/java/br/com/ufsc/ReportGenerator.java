package br.com.ufsc;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ReportGenerator {
  private static final Double MILLI_MULTIPLIER = 1000d;
  private LocalDateTime startTime;
  private LocalDateTime endTime;
  private Config config;
  private Long theoreticalTimeMs;
  private Logger logger = LogManager.getLogger();
  private List<Command> commands;

  public ReportGenerator(List<Command> commands, Config config) {
    this.commands = commands;
    this.config = config;
    this.theoreticalTimeMs = 0L;

  }

  public void startRegistering() {
    startTime = LocalDateTime.now();
  }

  public void registerEndTime() {
    endTime = LocalDateTime.now();
  }

  public void generateReport() {
    for (Command command : commands) {
      theoreticalTimeMs += command.getWeight().getProcessingTimeMs();
    }

    Long makepan = Duration.between(startTime, endTime).toMillis();
    Long differeceBetweenTheoreticalTimeAndMakespamMs = makepan - theoreticalTimeMs;

    Double speedup = (double) theoreticalTimeMs / (double) makepan;
    Double efficiency = 100 * speedup / config.getNumberOfThreads();
    Double meanThroughput = config.getNumberOfCommands() / (makepan / MILLI_MULTIPLIER);

    logger.info("Number of threads [{}]", config.getNumberOfThreads());
    logger.trace("theoretical time: [{}] ms", theoreticalTimeMs);
    logger.trace("differeceBetweenTheoreticalTimeAndMakespamMs: [{}] ms", differeceBetweenTheoreticalTimeAndMakespamMs);
    logger.info("makepan: [{}] ms", makepan);
    logger.info("speedup: [{}]x", speedup);
    logger.info("efficiency: [{}]%", efficiency);

    logger.info("meanThroughput: [{}] messages/s", meanThroughput);
  }
}
