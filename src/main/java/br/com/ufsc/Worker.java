package br.com.ufsc;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class Worker {

  private Scheduler scheduler;
  private final int id;
  private static int id_counter = 0;
  private Command command;
  Logger logger = LogManager.getLogger();

  public Worker(Scheduler scheduler) {
    this.scheduler = scheduler;
    id = id_counter;
    id_counter++;
  }

  public void setCommand(Command command) {
    this.command = command;
  }

  public int getId() {
    return id;
  }

  public void processing(Command commandToProcess) {
    setCommand(commandToProcess);
    logger.trace("Worker [{}] starting processing command [{}] with weight [{}]", getId(), command.getId(),
        command.getWeight());
    command.startProcessing();
    while (!command.doneProcessing()) {
      int i = 0; // dumb processing
    }
    logger.trace("Worker [{}] done processing command [{}]", getId(), command.getId());
  }
}
