package br.com.ufsc;
import java.util.List;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CommandGeneratorTest {
  @Test

  public void createSimpleOnlyOneCommand() {
    Config config = new Config();
    config.setNumberOfCommands(1);

    List<Command> commands = new CommandGenerator(config).generateCommand(); 
    assertEquals(1, commands.size());
  }

}
