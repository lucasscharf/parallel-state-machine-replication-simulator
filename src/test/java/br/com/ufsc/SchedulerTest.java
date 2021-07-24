package br.com.ufsc;

import java.util.List;

import org.junit.jupiter.api.Test;

public class SchedulerTest {

  @Test
  public void schedulerTesting() throws InterruptedException {
    Config config = new Config();
    config.setNumberOfCommands(20);
    config.setNumberOfThreads(4);
    config.setHeavyProcessingTimeMs(200);
    CommandWeight.config = config;

    CommandGenerator commandGenerator = new CommandGenerator(config);
    List<Command> commandsToProcess = commandGenerator.generateCommands();

    Scheduler scheduler = new Scheduler(commandsToProcess, config);
    scheduler.startScheduling();
  }

}
