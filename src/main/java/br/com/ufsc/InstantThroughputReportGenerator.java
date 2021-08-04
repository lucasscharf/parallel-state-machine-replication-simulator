package br.com.ufsc;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.AbstractMap.SimpleEntry;
import java.util.concurrent.atomic.AtomicInteger;
import java.time.temporal.ChronoUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class InstantThroughputReportGenerator {
  private AtomicInteger remainingCommandsToExecuteSize;
  private List<SimpleEntry<LocalDateTime, Integer>> throughputs;
  private Integer timeBetweenChecks;
  private Integer lastCommandCount;
  private LocalDateTime lastCommandTime;
  private Logger logger = LogManager.getLogger();
  private Config config;

  @SuppressWarnings("unchecked")
  private Thread monitor = new Thread(() -> {
    while (remainingCommandsToExecuteSize.get() > 0) {
      LocalDateTime now = LocalDateTime.now();

      Duration duration = Duration.between(lastCommandTime, now);
      Integer durationTime = (int) duration.get(ChronoUnit.SECONDS);

      try {
        if (durationTime == 0) {
          Thread.sleep(timeBetweenChecks);
          continue;
        }
      } catch (InterruptedException e) {
        // TODO ignore
      }

      Integer commandCount = remainingCommandsToExecuteSize.get();
      Integer commandDiff = lastCommandCount - commandCount;
      Integer throughput = (commandDiff) / durationTime;

      throughputs.add(new SimpleEntry(now, throughput));

      lastCommandCount = commandCount;
      lastCommandTime = now;

      try {
        Thread.sleep(timeBetweenChecks);
      } catch (InterruptedException e) {
        // TODO ignore
      }
    }
  });

  public InstantThroughputReportGenerator(Config config, AtomicInteger remainingCommandsToExecuteSize) {
    this.config = config;
    this.remainingCommandsToExecuteSize = remainingCommandsToExecuteSize;
    this.lastCommandCount = config.getNumberOfCommands();
    this.lastCommandTime = LocalDateTime.now();

    timeBetweenChecks = config.getTimeBetweenChecksMsInInstantThroughputReportGenerator();
    throughputs = new ArrayList<>();
  }

  public void startRegistering() {
    logger.info("Start registering");
    monitor.start();
  }

  public void generateReport() {
    logger.info("Instant throughput:");
    for (SimpleEntry<LocalDateTime, Integer> throughput : throughputs) {
      logger.info("[{}] -> [{}] messages/s", throughput.getKey(), throughput.getValue());
    }
  }
}
