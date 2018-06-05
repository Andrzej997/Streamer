package pl.polsl.helper;

public enum Resolution {

    H240(320, 240),
    H480(640, 480),
    H720(1280, 720),
    H1080(1920, 1080);

    private int width;
    private int height;

    Resolution(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}