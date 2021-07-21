package br.com.ufsc;

public class Config {
  private Integer lightProcessingTimeMs;
  private Integer mediumProcessingTimeMs;
  private Integer heavyProcessingTimeMs;
  private Integer maxNumberOfDependenciesPerCommand;
  private Integer dependencyModulus;
  private Integer numberOfCommands;

  public Config() {
    lightProcessingTimeMs = 200;
    mediumProcessingTimeMs = 500;
    heavyProcessingTimeMs = 1500;

  }

  public Integer getLightProcessingTimeMs() {
    return lightProcessingTimeMs;
  }

  public void setLightProcessingTimeMs(Integer lightProcessingTimeMs) {
    this.lightProcessingTimeMs = lightProcessingTimeMs;
  }

  public Integer getMediumProcessingTimeMs() {
    return mediumProcessingTimeMs;
  }

  public void setMediumProcessingTimeMs(Integer mediumProcessingTimeMs) {
    this.mediumProcessingTimeMs = mediumProcessingTimeMs;
  }

  public Integer getHeavyProcessingTimeMs() {
    return heavyProcessingTimeMs;
  }

  public void setHeavyProcessingTimeMs(Integer heavyProcessingTimeMs) {
    this.heavyProcessingTimeMs = heavyProcessingTimeMs;
  }

  public Integer getDependencyModulus() {
    return dependencyModulus;
  }

  public void setDependencyModulus(Integer dependencyModulus) {
    this.dependencyModulus = dependencyModulus;
  }

  public Integer getMaxNumberOfDependenciesPerCommand() {
    return maxNumberOfDependenciesPerCommand;
  }

  public void setMaxNumberOfDependenciesPerCommand(Integer maxNumberOfDependenciesPerCommand) {
    this.maxNumberOfDependenciesPerCommand = maxNumberOfDependenciesPerCommand;
  }

  
  public Integer getNumberOfCommands() {
    return numberOfCommands;
  }

  public void setNumberOfCommands(Integer numberOfCommands) {
    this.numberOfCommands = numberOfCommands;
  }

  
}
