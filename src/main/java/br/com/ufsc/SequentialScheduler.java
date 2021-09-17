package br.com.ufsc;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class SequentialScheduler implements Scheduler {
  private List<Command> commandsToProcess;

  Config config;
  private AtomicInteger commandsExecuted;

  public SequentialScheduler(List<Command> commandsToProcess, Config config) {
    this.commandsToProcess = commandsToProcess;
    commandsExecuted = new AtomicInteger();
    this.config = config;
  }

  public void startScheduling() {
    for (int i = 0; i < config.getNumberOfThreads(); i++) {
      Worker worker = new Worker(this);

      new Thread(worker).start();
    }
  }

  public AtomicInteger getCommandsExecuted() {
    return commandsExecuted;
  }

  public boolean hasNext() {
    if (config.isTimeBasedExecution() && LocalDateTime.now().isAfter(config.getMaxTimeExecution()))
     return false;
    return commandsToProcess.size() > commandsExecuted.get();
  }

  public List<Command> getNextCommand() {
    if (config.isTimeBasedExecution() && LocalDateTime.now().isAfter(config.getMaxTimeExecution()))
      return new ArrayList<>();
    return Arrays.asList(commandsToProcess.get(commandsExecuted.get()));
  }

  public void finalizedCommand() {
    commandsExecuted.incrementAndGet();
  }

  @Override
  public boolean hasFinalizedGeneratingCommands() {
    return true;
  }

  @Override
  public boolean hasFinalizedProccessing() {
    if (config.isTimeBasedExecution()) {
      return LocalDateTime.now().isAfter(config.getMaxTimeExecution());
    }
    return !hasNext();
  }
}
