package bg.sofia.uni.fmi.mjt.football;

public enum Foot {
    LEFT(0),
    RIGHT(1);

    private final int key;

    Foot(int key) {
        this.key = key;
    }

    public int getKey() {
        return key;
    }

    public static Foot of(String key) {
        if (key.equals(values()[0].key)) {
            return  Foot.LEFT;
        } else if (key.equals(values()[1].key)) {
            return Foot.RIGHT;
        } else {
            throw new IllegalArgumentException("The key for preferred foot is invalid.");
        }
    }
}