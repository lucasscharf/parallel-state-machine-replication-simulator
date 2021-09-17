package br.com.ufsc;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

public class BatchSchedulerTest {

  @Test
  public void schedulerTestingWithParallelOperations() throws InterruptedException {
    Config config = new Config();
    config.setNumberOfThreads(4);
    config.setNumberOfCommands(50_000);
    config.setLightProcessingTimeMs(1);
    config.setMediumProcessingTimeMs(5);
    config.setHeavyProcessingTimeMs(10);
    config.setMaxNumberOfDependenciesPerCommand(2);
    config.setParallelOperation(true);
    config.setFileName("fileName_rapido");
    int executionTimeMs = 60_000;
    config.setExecutionTimeMs(executionTimeMs);

    CommandWeight.config = config;

    CommandGenerator commandGenerator = new CommandGenerator(config);
    List<Command> commandsToProcess = new ArrayList<>(config.getNumberOfCommands());

    commandsToProcess = commandGenerator.generateCommands();

    ReportGenerator reportGenerator = new ReportGenerator(commandsToProcess, config);
    BatchScheduler scheduler = new BatchScheduler(commandsToProcess, config);
    InstantThroughputReportGenerator instantThroughputReportGenerator = new InstantThroughputReportGenerator(config,
        scheduler.getCommandsExecuted());

    reportGenerator.startRegistering();
    instantThroughputReportGenerator.startRegistering();
    scheduler.startScheduling();

    while (!scheduler.hasFinalizedProccessing()) {
    }

    reportGenerator.registerEndTime();
    config.setNumberOfCommands(scheduler.getCommandsExecuted().get());
    reportGenerator.generateReport();
    instantThroughputReportGenerator.generateReport();
  }

}
