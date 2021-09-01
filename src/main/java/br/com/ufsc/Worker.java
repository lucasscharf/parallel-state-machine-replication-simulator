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
    setCommand(commandToProcess);
    command.startProcessing();
    while (!command.doneProcessing()) {
      int i = 0; // dumb processing
    }
  }

  public void run() {
    while(scheduler.hasNext()) {
      Command command = scheduler.getNextCommand();
      processing(command);
      scheduler.finalizedCommand();
    }
  }
}
