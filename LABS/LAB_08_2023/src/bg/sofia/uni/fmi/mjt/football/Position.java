package bg.sofia.uni.fmi.mjt.football;

public enum Position {
    ST("ST"), // Striker
    LM("LM"), // Left Midfielder
    CF("CF"), // Centre Forward
    GK("GK"), // Goalkeeper
    RW("RW"), // Right Winger
    CM("CM"), // Centre Midfielder
    LW("LW"), // Left Winger
    CDM("CDM"), // Defensive Midfielder
    CAM("CAM"), // Attacking midfielder
    RB("RB"), // Right back
    LB("LB"), // Left back
    LWB("LWB"), // Left Wing-back
    RM("RM"), // Right Midfielder
    RWB("RWB"), // Right Wing-back
    CB("CB"); // Centre Back

    private final String key;

    Position(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public static Position of(String input) {
        for (var position : values()) {
              if (input.equalsIgnoreCase(position.key)) {
                  return position;
              }
        }
        throw new IllegalArgumentException("The value does not correspond to a position.");
    }
}