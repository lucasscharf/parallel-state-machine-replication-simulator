package br.com.ufsc;

import java.util.ArrayList;
import java.util.List;

public class CommandGenerator {
  private Config config;

  public CommandGenerator(Config config) {
    this.config = config;
  }

  public List<Command> generateCommands() {
    List<Command> commands = new ArrayList<>();
    for (int i = 0; i < config.getNumberOfCommands(); i++) {
      commands.add(new Command(i, CommandWeight.HEAVY, new ArrayList<>()));
    }
    return commands;
  }
}
