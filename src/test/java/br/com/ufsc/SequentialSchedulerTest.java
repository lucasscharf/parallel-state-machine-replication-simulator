package br.com.ufsc;

import java.util.List;

import org.junit.jupiter.api.Test;

public class SequentialSchedulerTest {

  @Test
  public void schedulerTesting() throws InterruptedException {
    Config config = new Config();
    config.setNumberOfCommands(500);
    config.setNumberOfThreads(1);
    config.setLightProcessingTimeMs(10);
    config.setMediumProcessingTimeMs(50);
    config.setHeavyProcessingTimeMs(100);
    config.setTimeBetweenChecksMsInInstantThroughputReportGenerator(2_000);
    CommandWeight.config = config;

    CommandGenerator commandGenerator = new CommandGenerator(config);
    List<Command> commandsToProcess = commandGenerator.generateCommands();

    ReportGenerator reportGenerator = new ReportGenerator(commandsToProcess, config);
    SequentialScheduler scheduler = new SequentialScheduler(commandsToProcess, config);
    InstantThroughputReportGenerator instantThroughputReportGenerator = new InstantThroughputReportGenerator(config, scheduler.getRemainingCommands());

    reportGenerator.startRegistering();
    instantThroughputReportGenerator.startRegistering();
    scheduler.startScheduling();
    
    reportGenerator.registerEndTime();

    reportGenerator.generateReport();
    instantThroughputReportGenerator.generateReport();
  }

}
