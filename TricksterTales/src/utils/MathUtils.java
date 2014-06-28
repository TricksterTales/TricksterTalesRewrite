package utils;

import java.math.BigDecimal;
import java.math.BigInteger;

public class MathUtils {

    public static int WIDTH = 768;
    public static int HEIGHT = 480;

    public static final long SECOND = 1000000000L;
    public static final long FRAME_30 = SECOND / 30L;
    public static final long FRAME_45 = SECOND / 45L;
    public static final long FRAME_60 = SECOND / 60L;

    public static int mod(int a, int b) {
	return a % b;// - b * (int) Math.floor(a * 1.0 / b);
    }

    public static long mod(long a, long b) {
	return a % b;// - b * (long) Math.floor(a * 1.0 / b);
    }

    public static float mod(float a, float b) {
	return a % b;// - b * (float) Math.floor(a / b);
    }

    public static double mod(double a, double b) {
	return a % b;// - b * Math.floor(a / b);
    }

    public static BigInteger mod(BigInteger a, BigInteger b) {
	return a.remainder(b);
    }

    public static BigDecimal mod(BigDecimal a, BigDecimal b) {
	return a.remainder(b);
    }

    public static boolean inRect(Number x1, Number y1, Number x2, Number y2,
	    Number x, Number y) {
	double dx1 = x1.doubleValue(), dy1 = y1.doubleValue(), dx2 = x2
		.doubleValue(), dy2 = y2.doubleValue(), dx = x.doubleValue(), dy = y
		.doubleValue();
	if (dx < dx1 || dx >= dx2)
	    return false;
	if (dy < dy1 || dy >= dy2)
	    return false;

	return true;
    }

    public static class Position2D {
	public double x, y;

	public Position2D() {
	    x = y = 0;
	}

	public Position2D(double x, double y) {
	    this.x = x;
	    this.y = y;
	}
    }

    public static class Position3D {
	public double x, y, z;

	public Position3D() {
	    x = y = z = 0;
	}

	public Position3D(double x, double y, double z) {
	    this.x = x;
	    this.y = y;
	    this.z = z;
	}
    }

}
