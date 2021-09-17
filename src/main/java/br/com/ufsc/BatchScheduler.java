package br.com.ufsc;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;

public class BatchScheduler implements Scheduler {
  Config config;
  private AtomicInteger commandsExecuted;
  Logger logger = LogManager.getLogger();
  Graph<BatchSchedulerNode, DefaultEdge> graph;
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
  public BatchScheduler(List<Command> commandsToProcess, Config config) {
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
  }

  public void startScheduling() {
    logger.info("Start scheduling");
    for (int i = 0; i < config.getNumberOfThreads(); i++) {
      Worker worker = new Worker(this);

      new Thread(worker).start();
    }

    for (int i = currentCommandToAdd; (i < commandsToProcess.size() && !hasFinalizedTime()); i += config
        .getBatchSize()) {
      currentCommandToAdd++;
      addBatch(i, i + config.getBatchSize());
    }

  }

  private void addBatch(int begin, int end) {
    BatchSchedulerNode node = new BatchSchedulerNode();
    for (int i = begin; i < end; i++) {
      Command commandToAdd = commandsToProcess.get(i);
      node.addCommand(commandToAdd);
    }

    // System.out.println("Adding new batch begin: " + begin + " end: " + end);
    dgInsertBatch(node);
  }

  private void dgInsertBatch(BatchSchedulerNode batchToInsert) {
    synchronized (lock) {
      graph.addVertex(batchToInsert);
      for (BatchSchedulerNode batch : graph.vertexSet()) {
        if (hasConflict(batch, batchToInsert)) {
          graph.addEdge(batchToInsert, batch);
        }
      }
    }
  }

  private boolean hasConflict(BatchSchedulerNode node1, BatchSchedulerNode node2) {
    return node1.hasConflict(node2);
  }

  @Override
  public AtomicInteger getCommandsExecuted() {
    return commandsExecuted;
  }

  @Override
  public boolean hasNext() {
    return !graph.vertexSet().isEmpty();
  }

  @Override
  public List<Command> getNextCommand() {
    if (config.isTimeBasedExecution() && LocalDateTime.now().isAfter(config.getMaxTimeExecution()))
      return new ArrayList<>();
    BatchSchedulerNode commandToExecute;
    synchronized (lock) {
      commandToExecute = graph //
          .vertexSet().stream().filter(command -> graph.inDegreeOf(command) == 0).findAny()//
          .orElse(null);
      if (commandToExecute != null)
        graph.removeVertex(commandToExecute);//
    }
    if (commandToExecute == null) {
      return new ArrayList<>();
    }
    return commandToExecute.getBatch();
  }

  @Override
  public void finalizedCommand() {
    commandsExecuted.incrementAndGet();
  }

  private boolean hasFinalizedTime() {
    if (config.isTimeBasedExecution()) {
      return LocalDateTime.now().isAfter(config.getMaxTimeExecution());
    }
    return false;
  }

  @Override
  public boolean hasFinalizedGeneratingCommands() {
    if (config.isTimeBasedExecution()) {
      return LocalDateTime.now().isAfter(config.getMaxTimeExecution());
    }

    return currentCommandToAdd >= config.getNumberOfCommands();
  }

  @Override
  public boolean hasFinalizedProccessing() {
    if (config.isTimeBasedExecution()) {
      return LocalDateTime.now().isAfter(config.getMaxTimeExecution());
    }

    return commandsExecuted.get() >= config.getNumberOfCommands();
  }
}
