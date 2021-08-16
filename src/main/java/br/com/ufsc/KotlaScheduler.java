package br.com.ufsc;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class KotlaScheduler implements Scheduler {
  // If the boolean in position (i,j) true so the command i deppends on command j
  boolean[][] commandsDag;
  private List<Command> commandsToProcess;
  Config config;
  private AtomicInteger commandsExecuted;

  /**
   * Ao adicionar um novo comando, o sistema verificará se o novo comando tem
   * dependência com algum nó já presente no grafo. Caso tenha, ele irá percorrer
   * o grafo em busca do nó que não tenha arestas chegando (e que o novo comando
   * seja dependente) (vou chamar esse nó de último nó). Após isso, ele adicionará
   * uma dependência do novo comando ao comando encontrado.
   * 
   * Quando o sistema pedir um novo comando para processar, o sistema irá
   * percorrer todo o grafo em busca de um comando que não tenha nenhuma
   * dependência.
   */
  public KotlaScheduler(List<Command> commandsToProcess, Config config) {
    this.commandsToProcess = commandsToProcess;
    this.commandsExecuted = new AtomicInteger();
    this.config = config;
    this.commandsDag = new boolean[commandsToProcess.size()][commandsToProcess.size()];

    // building dag
    for (int i = 0; i < commandsToProcess.size(); i++) {
      Command commandToAdd = commandsToProcess.get(i);

      for (int j = 0; j < i; j++) {
        Command commandToCompare = commandsToProcess.get(j);
        if (hasSameDependencies(commandToAdd, commandToCompare)) {
          // found the node
          List<Integer> allLastElementsInDag = getAllLastPositionsInDag(commandToAdd, commandToCompare);
          for (Integer lastElement : allLastElementsInDag) {
            // adding the new element in the dag
            commandsDag[i][lastElement] = true;
          }
        }
      }
    }
  }

  private List<Integer> getAllLastPositionsInDag(Command commandToAdd, Command commandToCompare) {
    List<Integer> dependenciesToAdd = commandToAdd.getDependencies();
    List<Integer> dependenciesToCompare = commandToCompare.getDependencies();
    List<Integer> allLastElements = new ArrayList<>();

    for (Integer dependencyToAdd : dependenciesToAdd) {
      for (Integer dependencyToCompare : dependenciesToCompare) {
        if (dependencyToAdd == dependencyToCompare) {
          Integer lastElementInDag = getLastPositionInDag(dependencyToAdd, commandToCompare);
          allLastElements.add(lastElementInDag);
        }
      }
    }

    return allLastElements;
  }

  private Integer getLastPositionInDag(Integer dependenciesToCompare, Command commandToCompare) {
    Integer nodeIndex = commandToCompare.getId();

    for (int i = 0; i < commandsDag.length; i++) {
      Command commmandCandidate = commandsToProcess.get(i);
      if (commandsDag[i][nodeIndex] && hasDependency(dependenciesToCompare, commmandCandidate)) {
        return getLastPositionInDag(dependenciesToCompare, commmandCandidate);
      }
    }
    return commandToCompare.getId();
  }

  public void startScheduling() {
    for (int i = 0; i < config.getNumberOfThreads(); i++) {
      Worker worker = new Worker(this);

      new Thread(worker).start();
    }
  }

  public AtomicInteger getCommandsExecuted() {
    return commandsExecuted;
  }

  public boolean hasNext() {
    return commandsToProcess.size() > commandsExecuted.get();
  }

  public Command getNextCommand() {
    return commandsToProcess.get(commandsExecuted.get());
  }

  public void finalizedCommand() {
    commandsExecuted.incrementAndGet();
  }

  private boolean hasSameDependencies(Command command1, Command command2) {
    List<Integer> dependencies1 = command1.getDependencies();
    List<Integer> dependencies2 = command2.getDependencies();

    for (int i = 0; i < dependencies1.size(); i++) {
      for (int j = 0; j < dependencies2.size(); j++) {
        if (dependencies1.get(i) == dependencies2.get(j))
          return true;
      }
    }
    return false;
  }

  private boolean hasDependency(Integer dependency, Command commmand) {
    List<Integer> dependencies = commmand.getDependencies();

    for (int j = 0; j < dependencies.size(); j++) {
      if (dependency == dependencies.get(j))
        return true;
    }
    return false;
  }
}
