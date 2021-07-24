package br.com.ufsc;

public enum CommandWeight {
  LIGHT(0), MEDIUM(1), HEAVY(2);

  public static Config config;
  private int id;

  private CommandWeight(int id) {
    this.id = id;
  }



  public int getProcessingTimeMs() {
    switch (this) {
      case HEAVY:
        return config.getHeavyProcessingTimeMs();
      case LIGHT:
        return config.getLightProcessingTimeMs();
      case MEDIUM:
        return config.getMediumProcessingTimeMs();
      default:
        return 0;
    }
  }
}
