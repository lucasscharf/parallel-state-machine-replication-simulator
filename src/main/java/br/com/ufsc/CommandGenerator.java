package br.com.ufsc;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CommandGenerator {
  private Config config;

  public CommandGenerator(Config config) {
    this.config = config;
  }

  public List<Command> generateCommands() {
    List<Command> commands = new ArrayList<>();
    for (int i = 0; i < config.getNumberOfCommands(); i++) {
      commands.add(new Command(i, fabricateCommandWeight(), fabricateDependencies()));
    }
    return commands;
  }

  private CommandWeight fabricateCommandWeight() {
    int random = nextInt() % 100;
    if (random < config.getPercentLightCommand()) {
      return CommandWeight.LIGHT;
    }

    if (random < config.getPercentLightCommand() + config.getPercentMediumCommand()) {
      return CommandWeight.MEDIUM;
    }

    return CommandWeight.HEAVY;
  }

  private List<Integer> fabricateDependencies() {
    int numberOfDependencies = nextInt() % config.getMaxNumberOfDependenciesPerCommand();
    return Stream.generate(() -> nextInt() % config.getDependencyModulus())//
        .limit(numberOfDependencies)//
        .collect(Collectors.toList());
  }

  private int nextInt() {
    return (int) (Math.random() * 1000000);
  }
}
