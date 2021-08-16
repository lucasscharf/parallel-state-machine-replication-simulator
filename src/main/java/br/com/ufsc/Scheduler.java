package br.com.ufsc;

import java.util.concurrent.atomic.AtomicInteger;

public interface Scheduler {
  public AtomicInteger getCommandsExecuted();

  public boolean hasNext();

  public Command getNextCommand();

  public void finalizedCommand();
}
