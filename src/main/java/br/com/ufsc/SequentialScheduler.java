package br.com.ufsc;

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
    return commandsToProcess.size() > commandsExecuted.get();
  }

  public Command getNextCommand() {
    return commandsToProcess.get(commandsExecuted.get());
  }

  public void finalizedCommand() {
    commandsExecuted.incrementAndGet();
  }

  @Override
  public boolean hasFinalizedGeneratingCommands() {
    return false;
  }

  @Override
  public boolean hasFinalizedProccessing() {
    return false;
  }
}
