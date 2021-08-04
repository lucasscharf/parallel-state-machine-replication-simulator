package br.com.ufsc;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SequentialScheduler {
  private List<Command> commandsToProcess;
  private Logger logger = LogManager.getLogger();
  private AtomicInteger remainingCommands;

  public SequentialScheduler(List<Command> commandsToProcess, Config config) {
    this.commandsToProcess = commandsToProcess;
    remainingCommands = new AtomicInteger();
    remainingCommands.set(config.getNumberOfCommands());
  }

  public void startScheduling() {
    for (int i = 0; i < commandsToProcess.size(); i++) {
      Command command = commandsToProcess.get(i);
      Worker worker = new Worker();
      worker.processing(command);
      remainingCommands.set(commandsToProcess.size() - i);
    }
  }

  public AtomicInteger getRemainingCommands() {
    return remainingCommands;
  }

}
