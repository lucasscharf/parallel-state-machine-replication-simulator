package br.com.ufsc;

import java.time.LocalDateTime;

public class Config {
  private Integer lightProcessingTimeMs;
  private Integer mediumProcessingTimeMs;
  private Integer heavyProcessingTimeMs;
  private Boolean parallelOperation;

  private Integer percentHeavyCommand;
  private Integer percentMediumCommand;
  private Integer percentLightCommand;

  private Integer maxNumberOfDependenciesPerCommand; // number non-inclusive
  private Integer dependencyModulus;
  private Integer numberOfCommands;

  private Integer numberOfThreads;

  private Integer timeBetweenChecksMsInInstantThroughputReportGenerator;
  private final static String DONT_SAVE = "AAAAAAAAAAa"; 
  private String fileName = DONT_SAVE;
  private LocalDateTime maxTimeExecution;
  private Integer executionTimeMs;

  public Config() {
    lightProcessingTimeMs = 200;
    mediumProcessingTimeMs = 500;
    heavyProcessingTimeMs = 1500;

    dependencyModulus = 100;
    maxNumberOfDependenciesPerCommand = 3;

    timeBetweenChecksMsInInstantThroughputReportGenerator = 1_000;

    numberOfCommands = 100;
    numberOfThreads = 1;

    percentHeavyCommand = 33;
    percentMediumCommand = 33;
    percentLightCommand = 33;

    parallelOperation = false;
  }

  public Integer getTimeBetweenChecksMsInInstantThroughputReportGenerator() {
    return timeBetweenChecksMsInInstantThroughputReportGenerator;
  }

  public Boolean getParallelOperation() {
    return parallelOperation;
  }

  public void setParallelOperation(Boolean parallelOperation) {
    this.parallelOperation = parallelOperation;
  }

  public void setTimeBetweenChecksMsInInstantThroughputReportGenerator(
      Integer timeBetweenChecksMsInInstantThroughputReportGenerator) {
    this.timeBetweenChecksMsInInstantThroughputReportGenerator = timeBetweenChecksMsInInstantThroughputReportGenerator;
  }

  public Integer getLightProcessingTimeMs() {
    return lightProcessingTimeMs;
  }

  public Integer getPercentHeavyCommand() {
    return percentHeavyCommand;
  }

  public void setPercentHeavyCommand(Integer percentHeavyCommand) {
    this.percentHeavyCommand = percentHeavyCommand;
  }

  public Integer getPercentMediumCommand() {
    return percentMediumCommand;
  }

  public void setPercentMediumCommand(Integer percentMediumCommand) {
    this.percentMediumCommand = percentMediumCommand;
  }

  public Integer getPercentLightCommand() {
    return percentLightCommand;
  }

  public void setPercentLightCommand(Integer percentLightCommand) {
    this.percentLightCommand = percentLightCommand;
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
    this.maxNumberOfDependenciesPerCommand++;
  }

  public Integer getNumberOfCommands() {
    return numberOfCommands;
  }

  public void setNumberOfCommands(Integer numberOfCommands) {
    this.numberOfCommands = numberOfCommands;
  }

  public Integer getNumberOfThreads() {
    return numberOfThreads;
  }

  public void setNumberOfThreads(Integer numberOfThreads) {
    this.numberOfThreads = numberOfThreads;
  }

  public String getFileName() {
    return fileName;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  public boolean saveFile() {
    return !fileName.equals(DONT_SAVE);
  }

  public boolean loadFile() {
    return saveFile();
  }

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Config [dependencyModulus=");
		builder.append(dependencyModulus);
		builder.append(", executionTimeMs=");
		builder.append(executionTimeMs);
		builder.append(", fileName=");
		builder.append(fileName);
		builder.append(", heavyProcessingTimeMs=");
		builder.append(heavyProcessingTimeMs);
		builder.append(", lightProcessingTimeMs=");
		builder.append(lightProcessingTimeMs);
		builder.append(", maxNumberOfDependenciesPerCommand=");
		builder.append(maxNumberOfDependenciesPerCommand);
		builder.append(", maxTimeExecution=");
		builder.append(maxTimeExecution);
		builder.append(", mediumProcessingTimeMs=");
		builder.append(mediumProcessingTimeMs);
		builder.append(", numberOfCommands=");
		builder.append(numberOfCommands);
		builder.append(", numberOfThreads=");
		builder.append(numberOfThreads);
		builder.append(", parallelOperation=");
		builder.append(parallelOperation);
		builder.append(", percentHeavyCommand=");
		builder.append(percentHeavyCommand);
		builder.append(", percentLightCommand=");
		builder.append(percentLightCommand);
		builder.append(", percentMediumCommand=");
		builder.append(percentMediumCommand);
		builder.append(", timeBetweenChecksMsInInstantThroughputReportGenerator=");
		builder.append(timeBetweenChecksMsInInstantThroughputReportGenerator);
		builder.append("]");
		return builder.toString();
	}

}
