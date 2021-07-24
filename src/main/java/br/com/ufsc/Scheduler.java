package br.com.ufsc;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Scheduler {
  private List<Command> commandsToProcess;
  private Config config;
  private int nextCommand = 0;
  private Logger logger = LogManager.getLogger();

  public Scheduler(List<Command> commandsToProcess, Config config) {
    this.commandsToProcess = commandsToProcess;
    this.config = config;
  }

  public void finishedProcessing(Worker worker) {
    processNext(worker);
  }

  private void processNext(Worker worker) {
    Command commandToProcess;

    synchronized (commandsToProcess) {
      if (config.getNumberOfCommands() <= nextCommand) {
        logger.debug("Stopped processing commands. The limit of [{}] is reached", config.getNumberOfCommands());
        return;
      }
      logger.debug("Giving next command to worker [{}]. Command ID [{}]. Number of commands [{}]", worker.getId(),
          nextCommand, config.getNumberOfCommands());

      commandToProcess = commandsToProcess.get(nextCommand);
      nextCommand++;
    }
    worker.processing(commandToProcess);
  }

  public void startScheduling() {
    List<Thread> threads = new ArrayList<>();
    logger.debug("Starting [{}] workers ", config.getNumberOfThreads());

    for (int i = 0; i < config.getNumberOfThreads(); i++) {
      Command commandToProcess = commandsToProcess.get(nextCommand);
      nextCommand++;
      Worker worker = new Worker(this);
      worker.setCommand(commandToProcess);

      threads.add(//
          new Thread(() -> {
            while (config.getNumberOfCommands() > nextCommand) {
              worker.processing(commandToProcess);
              processNext(worker);
            }
          }));
    }

    for (Thread thread : threads) {
      thread.start();
    }

    logger.debug("Now I wait =D");

    for (Thread thread : threads) {
      try {
        thread.join();
      } catch (Exception e) {
        // TODO: handle exception
      }
    }
  }
}
