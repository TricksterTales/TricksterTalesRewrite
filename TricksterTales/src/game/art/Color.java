package game.art;

public class Color {

    private int value;

    public Color(int colVal) {
	this.value = colVal;
    }

    public Color(int r, int g, int b) {
	this.value = getHex(r, g, b);
    }

    public int getValue() {
	return value;
    }

    public int[] getRGB() {
	return getRGB(value);
    }

    public static int[] getRGB(int col) {
	int r = (col & 0xff0000) >> 16;
	int g = (col & 0xff00) >> 8;
	int b = (col & 0xff);
	return new int[] { r, g, b };
    }

    public static int[] getRGBA(int col) {
	int a = (col & 0xff000000) >> 24;
	int r = (col & 0xff0000) >> 16;
	int g = (col & 0xff00) >> 8;
	int b = (col & 0xff);
	return new int[] { r, g, b, a };
    }

    public static int getHex(int r, int g, int b) {
	return r << 16 | g << 8 | b;
    }

    public static int getHex(int r, int g, int b, int a) {
	return a << 24 | r << 16 | g << 8 | b;
    }

    private static int blendbyte(int a, int b, int p) {
	float d = (float) (p & 0xff) / 0xff;
	return (int) ((1 - d) * (a & 0xff) + d * (b & 0xff));
    }

    private static int tintbyte(int a, int b) {
	float d = (float) (a & 0xff) / 0xff;
	return (int) (d * (b & 0xff));
    }

    public static int blend(int a, int b) {
	int per = (b & 0xff000000) >> 24;
	return (0xff << 24)
		| (blendbyte((a & 0xff0000) >> 16, (b & 0xff0000) >> 16, per) << 16)
		| (blendbyte((a & 0xff00) >> 8, (b & 0xff00) >> 8, per) << 8)
		| blendbyte(a & 0xff, b & 0xff, per);
    }

    public static int tint(int a, int b) {
	return (0xff << 24)
		| (tintbyte((a & 0xff0000) >> 16, (b & 0xff0000) >> 16) << 16)
		| (tintbyte((a & 0xff00) >> 8, (b & 0xff00) >> 8) << 8)
		| tintbyte(a & 0xff, b & 0xff);
    }

    public static Color blend(Color a, Color b) {
	return new Color(blend(a.getValue(), b.getValue()));
    }

    public static Color tint(Color a, Color b) {
	return new Color(tint(a.getValue(), b.getValue()));
    }

    public static int getAlpha(int col) {
	return (col & 0xff000000) >> 24;
    }

}
