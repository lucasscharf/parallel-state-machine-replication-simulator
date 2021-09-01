package br.com.ufsc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommandGenerator {
  private Logger logger = LoggerFactory.getLogger(getClass());
  private Config config;

  public CommandGenerator(Config config) {
    this.config = config;
  }

  public List<Command> generateCommands() {
    if (config.loadFile()) {
      try (FileInputStream fi = new FileInputStream(new File(config.getFileName()));
          ObjectInputStream oi = new ObjectInputStream(fi)) {
        logger.info("Loading file [{}]", config.getFileName());
        return (List<Command>) oi.readObject();
      } catch (Exception e) {
        logger.warn("Error loading file [{}]. Ex [{}]", config.getFileName(), e);
      }
    }

    List<Command> commands = new ArrayList<>();
    for (int i = 0; i < config.getNumberOfCommands(); i++) {
      commands.add(new Command(i, fabricateCommandWeight(), fabricateDependencies()));
    }

    if (config.saveFile()) {
      try (FileOutputStream fo = new FileOutputStream(new File(config.getFileName()));
          ObjectOutputStream oo = new ObjectOutputStream(fo)) {
        logger.info("Writing file [{}]", config.getFileName());
        oo.writeObject(commands);
      } catch (Exception e) {
        logger.warn("Error writing file [{}]. Ex [{}]", config.getFileName(), e);
      }
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
