package br.com.ufsc;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Worker implements Runnable {

  private final int id;
  private static int id_counter = 0;
  private Command command;
  Logger logger = LogManager.getLogger();
  private Scheduler scheduler;

  public Worker(Scheduler scheduler) {
    id = id_counter;
    id_counter++;
    this.scheduler = scheduler;
  }

  private void setCommand(Command command) {
    this.command = command;
  }

  public int getId() {
    return id;
  }

  public void processing(Command commandToProcess) {
    // logger.trace("Start processing command [{}] with time [{}]ms", commandToProcess.getId(),
    //     commandToProcess.getWeight().getProcessingTimeMs());
    setCommand(commandToProcess);
    command.startProcessing();
    while (!command.doneProcessing()) {
      int i = 0; // dumb processing
    }
    // logger.trace("End processing [{}]", commandToProcess.getId());
  }

  public void run() {
    // logger.traceEntry("Running...");
    while (!scheduler.hasFinalizedGeneratingCommands() || scheduler.hasNext()) {
      // logger.trace("Get next command");
      Command command;
      while ((command = scheduler.getNextCommand()) == null) {
        try {
          // logger.trace("We dont have command. Lets sleep");
          Thread.sleep(1);
        } catch (InterruptedException e) {
          // DO Nothing
        }
        if (scheduler.hasFinalizedProccessing())
          return;
      }
      // logger.trace("Processing command [{}]", command.getId());
      processing(command);
      // logger.trace("Finalized command [{}]", command.getId());
      scheduler.finalizedCommand();
    }
  }
}
