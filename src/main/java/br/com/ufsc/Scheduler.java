package br.com.ufsc;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public interface Scheduler {
  public AtomicInteger getCommandsExecuted();

  public boolean hasNext();

  public List<Command> getNextCommand();

  public void finalizedCommand();

  public boolean hasFinalizedGeneratingCommands();

  public boolean hasFinalizedProccessing();
}
