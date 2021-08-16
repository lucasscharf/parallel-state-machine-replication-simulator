package br.com.ufsc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

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

    assertEquals(1, commandsDag.length);
    assertEquals(1, commandsDag[0].length);
    assertFalse(commandsDag[0][0]);
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

    assertEquals(2, commandsDag.length);
    assertEquals(2, commandsDag[0].length);
    assertEquals(2, commandsDag[1].length);
    assertFalse(commandsDag[0][0]);
    assertFalse(commandsDag[1][0]);
    assertFalse(commandsDag[0][1]);
    assertFalse(commandsDag[1][1]);
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

    assertEquals(2, commandsDag.length);
    assertEquals(2, commandsDag[0].length);
    assertEquals(2, commandsDag[1].length);
    assertFalse(commandsDag[0][0]);
    assertTrue(commandsDag[1][0]);
    assertFalse(commandsDag[0][1]);
    assertFalse(commandsDag[1][1]);
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
  }

}