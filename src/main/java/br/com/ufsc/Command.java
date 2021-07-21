package br.com.ufsc;

import java.util.List;

public class Command {
  private Integer id;
  private CommandWeight weight;
  private List<Integer> dependencies;

  public Command(Integer id, CommandWeight weight, List<Integer> dependencies) {
    this.weight = weight;
    this.dependencies = dependencies;
    this.id = id;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public CommandWeight getWeight() {
    return weight;
  }

  public void setWeight(CommandWeight weight) {
    this.weight = weight;
  }

  public List<Integer> getDependencies() {
    return dependencies;
  }

  public void setDependencies(List<Integer> dependencies) {
    this.dependencies = dependencies;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append("Command [dependencies=");
    builder.append(dependencies);
    builder.append(", id=");
    builder.append(id);
    builder.append(", weight=");
    builder.append(weight);
    builder.append("]");
    return builder.toString();
  }
}
