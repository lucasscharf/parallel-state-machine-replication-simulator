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
    graph = new DefaultDirectedGraph<>(DefaultEdge.class);
    for (int i = 0; i < commandsToProcess.size(); i++) {
      Command commandToAdd = commandsToProcess.get(i);
      graph.addVertex(commandToAdd);

      for (Command command : graph.vertexSet()) {
        if (commandToAdd != command && hasSameDependencies(commandToAdd, command) && (graph.inDegreeOf(command) == 0)) {
          graph.addEdge(commandToAdd, command);
        }
      }
    }
    logger.trace("Graph edgeSet size [{}]", graph.edgeSet().size());
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
    synchronized (lock) {
      return !graph.vertexSet().isEmpty();
    }
  }

  public Command getNextCommand() {
    synchronized (lock) {
      Command commandToExecute = graph //
          .vertexSet().stream().filter(command -> graph.inDegreeOf(command) == 0).findAny()//
          .get();
      graph.removeVertex(commandToExecute);//
      return commandToExecute;
    }
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
