package br.com.ufsc;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class KotlaScheduler implements Scheduler {
  public static final int WITHOUT_PARENT = -1;
  // If the boolean in position (i,j) true so the command i deppends on command j
  boolean[][] commandsDag;
  public int[] commandParent;
  private List<Command> commandsToProcess;
  Config config;
  private AtomicInteger commandsExecuted;
  Logger logger = LogManager.getLogger();

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
    this.commandParent = new int[commandsToProcess.size()];

    // building dag
    for (int i = 0; i < commandsToProcess.size(); i++) {
      Command commandToAdd = commandsToProcess.get(i);
      commandParent[i] = WITHOUT_PARENT;
      for (int j = 0; j < i; j++) {
        Command commandToCompare = commandsToProcess.get(j);
        if (hasSameDependencies(commandToAdd, commandToCompare)) {
          // found the node
          List<Integer> allLastElementsInDag = getAllLastPositionsInDag(commandToAdd, commandToCompare);
          for (Integer lastElement : allLastElementsInDag) {
            // adding the new element in the dag
            commandsDag[i][lastElement] = true;
            commandParent[i] = lastElement;
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
    Integer commandId = commandToCompare.getId();

    while(commandParent[commandId] !=WITHOUT_PARENT) {
      commandId = commandParent[commandId] ;
    }

    return commandId;
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
    Integer dep11 = null;
    Integer dep12 = null;
    Integer dep21 = null;
    Integer dep22 = null;
    if (dependencies1.isEmpty() || dependencies2.isEmpty())
      return false;

    if (dependencies1.size() >= 1)
      dep11 = dependencies1.get(0);

    if (dependencies2.size() >= 1)
      dep21 = dependencies1.get(0);

    if (dependencies1.size() >= 2)
      dep12 = dependencies1.get(0);

    if (dependencies1.size() >= 2)
      dep22 = dependencies1.get(0);

    if (dep11 == dep21 || dep11 == dep22)
      return true;
    if (dep12 == dep21 || (dep12 == dep22 && dep12 != null))
      return true;
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
