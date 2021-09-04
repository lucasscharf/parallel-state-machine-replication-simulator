package br.com.ufsc;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;

public class KotlaScheduler implements Scheduler {
  Config config;
  private AtomicInteger commandsExecuted;
  Logger logger = LogManager.getLogger();
  Graph<Command, DefaultEdge> graph;
  Integer lock = 0;
  Integer currentCommandToAdd;
  List<Command> commandsToProcess;

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
    logger.traceEntry("Starting mounting graph to [{}] commands", commandsToProcess.size());
    this.commandsExecuted = new AtomicInteger();
    this.config = config;
    this.commandsToProcess = commandsToProcess;
    this.currentCommandToAdd = 0;
    this.lock = 0;
    graph = new DefaultDirectedGraph<>(DefaultEdge.class);

    if (config.getParallelOperation()) {
      return;
    }

    for (int i = 0; i < commandsToProcess.size(); i++) {
      addCommandToGraph(i);
    }
    logger.trace("Graph edgeSet size [{}]", graph.edgeSet().size());
  }

  private void addCommandToGraph(int commandPosition) {
    logger.trace("Adding command [{}] in graph", commandPosition);
    Command commandToAdd = commandsToProcess.get(commandPosition);
    graph.addVertex(commandToAdd);

    for (Command command : graph.vertexSet()) {
      if (commandToAdd != command && hasSameDependencies(commandToAdd, command) && (graph.inDegreeOf(command) == 0)) {
        graph.addEdge(commandToAdd, command);
      }
    }
  }

  public void startScheduling() {
    logger.info("Start scheduling");
    for (int i = 0; i < config.getNumberOfThreads(); i++) {
      Worker worker = new Worker(this);

      new Thread(worker).start();
    }

    if (!config.getParallelOperation()) {
      logger.debug("Execute algorithm and generate commands aren't parallel");
      return;
    }

    while (!hasFinalizedGeneratingCommands()) {
      Integer maxCommandsToAdd = commandsToProcess.size();
      for (int i = currentCommandToAdd; i < maxCommandsToAdd; i++) {
        logger.trace("Get lock to add command in graph. Get lock");
        synchronized (lock) {
          addCommandToGraph(i);
        }
        logger.trace("Release lock to add command in graph. Release lock");
      }

      try {
        currentCommandToAdd = maxCommandsToAdd;
        logger.debug("Sleep scheduling...");
        Thread.sleep(100);
      } catch (InterruptedException e) {
        // DO NOTHING
      }
    }
  }

  public AtomicInteger getCommandsExecuted() {
    return commandsExecuted;
  }

  public boolean hasNext() {
    logger.trace("Calling has next. Get lock");
    Boolean hasNextCommand;
    synchronized (lock) {
      hasNextCommand = !graph.vertexSet().isEmpty();
    }
    logger.trace("Calling has next with value [{}]. Release lock", hasNextCommand);
    return hasNextCommand;
  }

  public Command getNextCommand() {
    logger.trace("Get next command. Get lock");
    Command commandToExecute;
    synchronized (lock) {
      commandToExecute = graph //
          .vertexSet().stream().filter(command -> graph.inDegreeOf(command) == 0).findAny()//
          .orElse(null);
      if (commandToExecute != null)
        graph.removeVertex(commandToExecute);//
    }
    logger.trace("Get next command. Release lock");
    return commandToExecute;
  }

  public boolean hasFinalizedProccessing() {
    logger.trace("Checking if finalized commands executed [{}] and number of commands [{}] commandsToProcess [{}]", //
         commandsExecuted.get(), config.getNumberOfCommands(), graph.vertexSet().size());
    return commandsExecuted.get() >= config.getNumberOfCommands();
  }

  public boolean hasFinalizedGeneratingCommands() {
    logger.trace("Checking if finalized commands executed [{}] and number of commands [{}] commandsToProcess [{}] commands generated [{}]", //
         commandsExecuted.get(), config.getNumberOfCommands(), graph.vertexSet().size(), currentCommandToAdd);
    return currentCommandToAdd >= config.getNumberOfCommands();
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

}
