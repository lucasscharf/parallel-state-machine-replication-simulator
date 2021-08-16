package br.com.ufsc;

import java.util.List;

import org.junit.jupiter.api.Test;

public class DumbSchedulerTest {

  @Test
  public void schedulerTesting() throws InterruptedException {
    Config config = new Config();
    config.setNumberOfCommands(5);
    config.setNumberOfThreads(3);
    config.setHeavyProcessingTimeMs(100);
    CommandWeight.config = config;

    CommandGenerator commandGenerator = new CommandGenerator(config);
    List<Command> commandsToProcess = commandGenerator.generateCommands();

    ReportGenerator reportGenerator = new ReportGenerator(commandsToProcess, config);
    DumbScheduler scheduler = new DumbScheduler(commandsToProcess, config);
    reportGenerator.startRegistering();
    scheduler.startScheduling();
    reportGenerator.registerEndTime();
    reportGenerator.generateReport();
  }

}
