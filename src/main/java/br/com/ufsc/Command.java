package br.com.ufsc;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class Command implements Serializable {
  private static final long serialVersionUID = 1L;
  
  private Integer id;
  private CommandWeight weight;
  private List<Integer> dependencies;
  private LocalDateTime startTime;
  private LocalDateTime limitTime;

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

  public void startProcessing() {
    startTime = LocalDateTime.now();
    limitTime = startTime.plus(weight.getProcessingTimeMs(), ChronoUnit.MILLIS);
  }

  public boolean doneProcessing() {
    return LocalDateTime.now().isAfter(limitTime);
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
