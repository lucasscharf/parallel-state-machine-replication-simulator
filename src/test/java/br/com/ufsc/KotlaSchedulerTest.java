package br.com.ufsc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class KotlaSchedulerTest {

  // Gerar 2 comandos com dependência e um sem (alterando as ordens de inserção)
  // Tentar gerar um grafo com ciclo (só jesus sabe como vou fazer isso)

  @DisplayName("GIVEN a single command without dependency WHEN create dag in kotla THEN generate a graph with one node and without dependencies")
  @Test
  public void createSingleCommandWithoutDependency() {
    List<Command> commands = Arrays.asList(new Command(0, null, Collections.emptyList()));
    Config config = new Config();
    config.setNumberOfCommands(commands.size());

    KotlaScheduler kotlaScheduler = new KotlaScheduler(commands, config);

    Graph<Command, DefaultEdge> graph = kotlaScheduler.graph;

    assertEquals(1, graph.vertexSet().size());
    assertEquals(0, graph.inDegreeOf(commands.get(0)));
    assertEquals(0, graph.outDegreeOf(commands.get(0)));
  }

  @DisplayName("GIVEN two commands without dependency WHEN create dag in kotla THEN generate a graph with two nodes and without dependencies")
  @Test
  public void createTwoCommandsWithoutDependency() {
    List<Command> commands = Arrays.asList(//
        new Command(0, null, Collections.emptyList()), //
        new Command(1, null, Arrays.asList(1)));
    Config config = new Config();
    config.setNumberOfCommands(commands.size());

    KotlaScheduler kotlaScheduler = new KotlaScheduler(commands, config);

    Graph<Command, DefaultEdge> graph = kotlaScheduler.graph;

    assertEquals(2, graph.vertexSet().size());
    assertEquals(0, graph.inDegreeOf(commands.get(0)));
    assertEquals(0, graph.outDegreeOf(commands.get(0)));
    assertEquals(0, graph.inDegreeOf(commands.get(1)));
    assertEquals(0, graph.outDegreeOf(commands.get(1)));
  }

  @DisplayName("GIVEN two commands with dependency WHEN create dag in kotla THEN generate a graph with two nodes and only one dependency")
  @Test
  public void createTwoCommandsWithDependency() {
    List<Command> commands = Arrays.asList(//
        new Command(0, null, Arrays.asList(1)), //
        new Command(1, null, Arrays.asList(1)));
    Config config = new Config();
    config.setNumberOfCommands(commands.size());

    KotlaScheduler kotlaScheduler = new KotlaScheduler(commands, config);

    Graph<Command, DefaultEdge> graph = kotlaScheduler.graph;

    assertEquals(2, graph.vertexSet().size());
    assertEquals(1, graph.inDegreeOf(commands.get(0)));
    assertEquals(0, graph.outDegreeOf(commands.get(0)));
    assertEquals(0, graph.inDegreeOf(commands.get(1)));
    assertEquals(1, graph.outDegreeOf(commands.get(1)));
    assertNotNull(graph.getEdge(commands.get(1), commands.get(0)));
  }

  @DisplayName("GIVEN three commands with second and last with dependency WHEN create dag in kotla THEN generate a graph with three nodes and only one dependency")
  @Test
  public void createThreeComandsWithSecondAndLast() {
    List<Command> commands = Arrays.asList(//
        new Command(0, null, Arrays.asList(120)), //
        new Command(1, null, Arrays.asList(10)), //
        new Command(2, null, Arrays.asList(10)));
    Config config = new Config();
    config.setNumberOfCommands(commands.size());

    KotlaScheduler kotlaScheduler = new KotlaScheduler(commands, config);

    Graph<Command, DefaultEdge> graph = kotlaScheduler.graph;

    assertEquals(3, graph.vertexSet().size());
    assertEquals(0, graph.inDegreeOf(commands.get(0)));
    assertEquals(0, graph.outDegreeOf(commands.get(0)));
    assertEquals(1, graph.inDegreeOf(commands.get(1)));
    assertEquals(0, graph.outDegreeOf(commands.get(1)));
    assertEquals(0, graph.inDegreeOf(commands.get(2)));
    assertEquals(1, graph.outDegreeOf(commands.get(2)));

    assertNotNull(graph.getEdge(commands.get(2), commands.get(1)));
  }

  @DisplayName("GIVEN three commands with first and second with dependency WHEN create dag in kotla THEN generate a graph with three nodes and only one dependency")
  @Test
  public void createThreeComandsWithFirstAndSecond() {
    List<Command> commands = Arrays.asList(//
        new Command(0, null, Arrays.asList(10)), //
        new Command(1, null, Arrays.asList(10)), //
        new Command(2, null, Arrays.asList(120)));
    Config config = new Config();
    config.setNumberOfCommands(commands.size());

    KotlaScheduler kotlaScheduler = new KotlaScheduler(commands, config);

    Graph<Command, DefaultEdge> graph = kotlaScheduler.graph;

    assertEquals(3, graph.vertexSet().size());
    assertEquals(1, graph.inDegreeOf(commands.get(0)));
    assertEquals(0, graph.outDegreeOf(commands.get(0)));
    assertEquals(0, graph.inDegreeOf(commands.get(1)));
    assertEquals(1, graph.outDegreeOf(commands.get(1)));
    assertEquals(0, graph.inDegreeOf(commands.get(2)));
    assertEquals(0, graph.outDegreeOf(commands.get(2)));

    assertNotNull(graph.getEdge(commands.get(1), commands.get(0)));
  }

  @DisplayName("GIVEN three commands with first and last with dependency WHEN create dag in kotla THEN generate a graph with three nodes and only one dependency")
  @Test
  public void createThreeComandsWithFirstAndLast() {

    List<Command> commands = Arrays.asList(//
        new Command(0, null, Arrays.asList(10)), //
        new Command(1, null, Arrays.asList(120)), //
        new Command(2, null, Arrays.asList(10)));
    Config config = new Config();
    config.setNumberOfCommands(commands.size());

    KotlaScheduler kotlaScheduler = new KotlaScheduler(commands, config);

    Graph<Command, DefaultEdge> graph = kotlaScheduler.graph;

    assertEquals(3, graph.vertexSet().size());
    assertEquals(1, graph.inDegreeOf(commands.get(0)));
    assertEquals(0, graph.outDegreeOf(commands.get(0)));
    assertEquals(0, graph.inDegreeOf(commands.get(1)));
    assertEquals(0, graph.outDegreeOf(commands.get(1)));
    assertEquals(0, graph.inDegreeOf(commands.get(2)));
    assertEquals(1, graph.outDegreeOf(commands.get(2)));

    assertNotNull(graph.getEdge(commands.get(2), commands.get(0)));
  }

  @DisplayName("GIVEN three commands with first, second and last having dependency WHEN create dag in kotla THEN generate a graph with three nodes and only two dependencies")
  @Test
  public void createThreeComandsWithAllDependencies() {

    List<Command> commands = Arrays.asList(//
        new Command(0, null, Arrays.asList(10)), //
        new Command(1, null, Arrays.asList(10)), //
        new Command(2, null, Arrays.asList(10)));
    Config config = new Config();
    config.setNumberOfCommands(commands.size());

    KotlaScheduler kotlaScheduler = new KotlaScheduler(commands, config);

    Graph<Command, DefaultEdge> graph = kotlaScheduler.graph;

    assertEquals(3, graph.vertexSet().size());
    assertEquals(1, graph.inDegreeOf(commands.get(0)));
    assertEquals(0, graph.outDegreeOf(commands.get(0)));
    assertEquals(1, graph.inDegreeOf(commands.get(1)));
    assertEquals(1, graph.outDegreeOf(commands.get(1)));
    assertEquals(0, graph.inDegreeOf(commands.get(2)));
    assertEquals(1, graph.outDegreeOf(commands.get(2)));

    assertNotNull(graph.getEdge(commands.get(1), commands.get(0)));
    assertNotNull(graph.getEdge(commands.get(2), commands.get(1)));
  }

  @DisplayName("GIVEN three commands with respective dependecies as C0={0}, C1={1} e C2={0,1} WHEN create dag in kotla THEN with the command C2 depending from C0 e C1")
  @Test
  public void createThreeComandsWithMultipleDependencies() {

    List<Command> commands = Arrays.asList(//
        new Command(0, null, Arrays.asList(0)), //
        new Command(1, null, Arrays.asList(1)), //
        new Command(2, null, Arrays.asList(1, 2)));
    Config config = new Config();
    config.setNumberOfCommands(commands.size());

    KotlaScheduler kotlaScheduler = new KotlaScheduler(commands, config);

    Graph<Command, DefaultEdge> graph = kotlaScheduler.graph;

    assertEquals(3, graph.vertexSet().size());
    assertEquals(0, graph.inDegreeOf(commands.get(0)));
    assertEquals(0, graph.outDegreeOf(commands.get(0)));
    assertEquals(1, graph.inDegreeOf(commands.get(1)));
    assertEquals(0, graph.outDegreeOf(commands.get(1)));
    assertEquals(0, graph.inDegreeOf(commands.get(2)));
    assertEquals(1, graph.outDegreeOf(commands.get(2)));

    assertNotNull(graph.getEdge(commands.get(2), commands.get(1)));
  }

  @Test
  public void schedulerTesting() throws InterruptedException {
    Config config = new Config();
    config.setNumberOfCommands(1_000);
    config.setNumberOfThreads(2);
    config.setLightProcessingTimeMs(50);
    config.setMediumProcessingTimeMs(100);
    config.setHeavyProcessingTimeMs(200);
    config.setMaxNumberOfDependenciesPerCommand(2);
    config.setParallelOperation(false);
    config.setFileName("fileName_grande");

    CommandWeight.config = config;

    CommandGenerator commandGenerator = new CommandGenerator(config);
    List<Command> commandsToProcess = commandGenerator.generateCommands();

    ReportGenerator reportGenerator = new ReportGenerator(commandsToProcess, config);
    KotlaScheduler scheduler = new KotlaScheduler(commandsToProcess, config);
    InstantThroughputReportGenerator instantThroughputReportGenerator = new InstantThroughputReportGenerator(config,
        scheduler.getCommandsExecuted());

    reportGenerator.startRegistering();
    instantThroughputReportGenerator.startRegistering();
    scheduler.startScheduling();

    while (scheduler.hasNext()) {
    }

    reportGenerator.registerEndTime();

    reportGenerator.generateReport();
    instantThroughputReportGenerator.generateReport();
  }

  @Test
  public void schedulerTestingWithParallelOperations() throws InterruptedException {
    Config config = new Config();
    config.setNumberOfCommands(100_000);
    config.setNumberOfThreads(4);
    config.setLightProcessingTimeMs(10);
    config.setMediumProcessingTimeMs(50);
    config.setHeavyProcessingTimeMs(100);
    config.setMaxNumberOfDependenciesPerCommand(2);
    config.setParallelOperation(true);
    int executionTimeMs = 10_000;
    config.setExecutionTimeMs(executionTimeMs);

    CommandWeight.config = config;

    CommandGenerator commandGenerator = new CommandGenerator(config);
    List<Command> commandsToProcess = new ArrayList<>(config.getNumberOfCommands());

    commandsToProcess = commandGenerator.generateCommands();
    // commandGenerator.generateCommandsInteractively(commandsToProcess);

    ReportGenerator reportGenerator = new ReportGenerator(commandsToProcess, config);
    KotlaScheduler scheduler = new KotlaScheduler(commandsToProcess, config);
    InstantThroughputReportGenerator instantThroughputReportGenerator = new InstantThroughputReportGenerator(config,
        scheduler.getCommandsExecuted());

    reportGenerator.startRegistering();
    instantThroughputReportGenerator.startRegistering();
    scheduler.startScheduling();

    while (!scheduler.hasFinalizedProccessing()) {
    }

    reportGenerator.registerEndTime();
    config.setNumberOfCommands(scheduler.getCommandsExecuted().get());
    reportGenerator.generateReport();
    instantThroughputReportGenerator.generateReport();
  }

}
