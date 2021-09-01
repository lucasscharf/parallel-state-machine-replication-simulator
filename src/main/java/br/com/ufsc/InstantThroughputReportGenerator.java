package br.com.ufsc;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.AbstractMap.SimpleEntry;
import java.util.concurrent.atomic.AtomicInteger;
import java.time.temporal.ChronoUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class InstantThroughputReportGenerator {
  private AtomicInteger commandsExecuted;
  private List<SimpleEntry<LocalDateTime, Integer>> throughputs;
  private List<SimpleEntry<LocalDateTime, Integer>> commandsProcessed;
  private Integer timeBetweenChecks;
  private Integer lastCommandCount;
  private LocalDateTime lastCommandTime;
  private Logger logger = LogManager.getLogger();
  private Config config;

  @SuppressWarnings("unchecked")
  private Thread monitor = new Thread(() -> {
    while (true) {
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

      Integer commandCount = commandsExecuted.get();
      Integer commandDiff = commandCount - lastCommandCount;
      Integer throughput = (commandDiff) / durationTime;

      throughputs.add(new SimpleEntry(now, throughput));
      commandsProcessed.add(new SimpleEntry(now, commandCount));

      lastCommandCount = commandCount;
      lastCommandTime = now;

      try {
        Thread.sleep(timeBetweenChecks);
      } catch (InterruptedException e) {
        // TODO ignore
      }
    }
  });

  @SuppressWarnings("unchecked")
  private void updateTimes(LocalDateTime now) {
    Duration duration = Duration.between(lastCommandTime, now);
    Integer durationTime = (int) duration.get(ChronoUnit.SECONDS);

    if (durationTime == 0)
      durationTime = 1;

    Integer commandCount = commandsExecuted.get();
    Integer commandDiff = commandCount - lastCommandCount;
    Integer throughput = (commandDiff) / durationTime;

    throughputs.add(new SimpleEntry(now, throughput));
    commandsProcessed.add(new SimpleEntry(now, commandCount));

    lastCommandCount = commandCount;
    lastCommandTime = now;

  }

  public InstantThroughputReportGenerator(Config config, AtomicInteger commandsExecuted) {
    this.config = config;
    this.commandsExecuted = commandsExecuted;
    this.lastCommandCount = config.getNumberOfCommands();
    this.lastCommandTime = LocalDateTime.now();

    timeBetweenChecks = config.getTimeBetweenChecksMsInInstantThroughputReportGenerator();
    throughputs = new ArrayList<>();
    commandsProcessed = new ArrayList<>();
  }

  public void startRegistering() {
    logger.info("Start registering");
    monitor.start();
  }

  public void generateReport() {
    updateTimes(LocalDateTime.now());
    logger.info("Config [{}]", config);
    logger.info("Instant throughput:");

    logger.info("Commands processed without processing:");
    for (SimpleEntry<LocalDateTime, Integer> commandsProcessed : commandsProcessed) {
      System.out.println(String.format("%s,%s", //
          commandsProcessed.getKey().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli(), //
          commandsProcessed.getValue()));
    }

  }
}
