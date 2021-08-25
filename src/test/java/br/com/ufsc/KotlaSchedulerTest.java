package br.com.ufsc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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

    boolean[][] commandsDag = kotlaScheduler.commandsDag;
    int[] commandParent = kotlaScheduler.commandParent;

    assertEquals(1, commandsDag.length);
    assertEquals(1, commandsDag[0].length);
    assertFalse(commandsDag[0][0]);
    assertEquals(KotlaScheduler.WITHOUT_PARENT, commandParent[0]);
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

    boolean[][] commandsDag = kotlaScheduler.commandsDag;
    int[] commandParent = kotlaScheduler.commandParent;

    assertEquals(2, commandsDag.length);
    assertEquals(2, commandsDag[0].length);
    assertEquals(2, commandsDag[1].length);
    assertFalse(commandsDag[0][0]);
    assertFalse(commandsDag[1][0]);
    assertFalse(commandsDag[0][1]);
    assertFalse(commandsDag[1][1]);
    assertEquals(KotlaScheduler.WITHOUT_PARENT, commandParent[0]);
    assertEquals(KotlaScheduler.WITHOUT_PARENT, commandParent[1]);
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

    boolean[][] commandsDag = kotlaScheduler.commandsDag;
    int[] commandParent = kotlaScheduler.commandParent;

    assertEquals(2, commandsDag.length);
    assertEquals(2, commandsDag[0].length);
    assertEquals(2, commandsDag[1].length);
    assertFalse(commandsDag[0][0]);
    assertTrue(commandsDag[1][0]);
    assertFalse(commandsDag[0][1]);
    assertFalse(commandsDag[1][1]);

    assertEquals(KotlaScheduler.WITHOUT_PARENT, commandParent[0]);
    assertEquals(0, commandParent[1]);
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

    boolean[][] commandsDag = kotlaScheduler.commandsDag;
    int[] commandParent = kotlaScheduler.commandParent;

    assertEquals(3, commandsDag.length);
    assertEquals(3, commandsDag[0].length);
    assertEquals(3, commandsDag[1].length);
    assertFalse(commandsDag[0][0]);
    assertFalse(commandsDag[1][0]);
    assertFalse(commandsDag[2][0]);

    assertFalse(commandsDag[0][1]);
    assertFalse(commandsDag[1][1]);
    assertTrue(commandsDag[2][1]);

    assertFalse(commandsDag[0][2]);
    assertFalse(commandsDag[1][2]);
    assertFalse(commandsDag[2][2]);

    assertEquals(KotlaScheduler.WITHOUT_PARENT, commandParent[0]);
    assertEquals(KotlaScheduler.WITHOUT_PARENT, commandParent[1]);
    assertEquals(1, commandParent[2]);
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

    boolean[][] commandsDag = kotlaScheduler.commandsDag;
    int[] commandParent = kotlaScheduler.commandParent;

    assertEquals(3, commandsDag.length);
    assertEquals(3, commandsDag[0].length);
    assertEquals(3, commandsDag[1].length);
    assertFalse(commandsDag[0][0]);
    assertTrue(commandsDag[1][0]);
    assertFalse(commandsDag[2][0]);

    assertFalse(commandsDag[0][1]);
    assertFalse(commandsDag[1][1]);
    assertFalse(commandsDag[2][1]);

    assertFalse(commandsDag[0][2]);
    assertFalse(commandsDag[1][2]);
    assertFalse(commandsDag[2][2]);

    assertEquals(KotlaScheduler.WITHOUT_PARENT, commandParent[0]);
    assertEquals(0, commandParent[1]);
    assertEquals(KotlaScheduler.WITHOUT_PARENT, commandParent[2]);
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

    boolean[][] commandsDag = kotlaScheduler.commandsDag;
    int[] commandParent = kotlaScheduler.commandParent;

    assertEquals(3, commandsDag.length);
    assertEquals(3, commandsDag[0].length);
    assertEquals(3, commandsDag[1].length);
    assertFalse(commandsDag[0][0]);
    assertFalse(commandsDag[1][0]);
    assertTrue(commandsDag[2][0]);

    assertFalse(commandsDag[0][1]);
    assertFalse(commandsDag[1][1]);
    assertFalse(commandsDag[2][1]);

    assertFalse(commandsDag[0][2]);
    assertFalse(commandsDag[1][2]);
    assertFalse(commandsDag[2][2]);

    assertEquals(KotlaScheduler.WITHOUT_PARENT, commandParent[0]);
    assertEquals(KotlaScheduler.WITHOUT_PARENT, commandParent[1]);
    assertEquals(0, commandParent[2]);
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

    boolean[][] commandsDag = kotlaScheduler.commandsDag;
    int[] commandParent = kotlaScheduler.commandParent;

    assertEquals(3, commandsDag.length);
    assertEquals(3, commandsDag[0].length);
    assertEquals(3, commandsDag[1].length);
    assertFalse(commandsDag[0][0]);
    assertFalse(commandsDag[1][0]);
    assertTrue(commandsDag[2][0]);

    assertFalse(commandsDag[0][1]);
    assertFalse(commandsDag[1][1]);
    assertFalse(commandsDag[2][1]);

    assertFalse(commandsDag[0][2]);
    assertFalse(commandsDag[1][2]);
    assertFalse(commandsDag[2][2]);

    assertEquals(KotlaScheduler.WITHOUT_PARENT, commandParent[0]);
    assertEquals(KotlaScheduler.WITHOUT_PARENT, commandParent[1]);
    assertEquals(0, commandParent[2]);
  }

  @Test
  public void schedulerTesting() throws InterruptedException {
    Config config = new Config();
    config.setNumberOfCommands(1000);
    config.setNumberOfThreads(3);
    config.setLightProcessingTimeMs(10);
    config.setMediumProcessingTimeMs(50);
    config.setHeavyProcessingTimeMs(100);
    config.setTimeBetweenChecksMsInInstantThroughputReportGenerator(2_000);
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

}
