package game.art;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

public class Texture {

    public static final int TYPE_RGB = 0x0, TYPE_ARGB = 0x1;

    private int width, height;
    private int[] pixels, pixelsrgb;

    private Texture(int w, int h, int[] pix) {
	this.width = w;
	this.height = h;
	this.pixels = pix;
    }

    public static Texture getTexture(BufferedImage img) {
	int width = img.getWidth();
	int height = img.getHeight();
	int[] pixels = new int[width * height];
	byte[] pix = ((DataBufferByte) img.getData().getDataBuffer()).getData();
	int pos = 0;
	for (int x = 0; x < width; ++x) {
	    for (int y = 0; y < height; ++y) {
		pos = 4 * (x + y * width);
		pixels[x + (height - y - 1) * width] = ((pix[pos] & 0xff) << 24)
			| ((pix[pos + 3] & 0xff) << 16)
			| ((pix[pos + 2] & 0xff) << 8) | (pix[pos + 1] & 0xff);
	    }
	}
	return new Texture(width, height, pixels);
    }

    public static Texture makeTexture(int width, int height, int col, int bcol) {
	if (width <= 0 || height <= 0)
	    return null;
	int[] pixels = new int[width * height];
	col = col & 0xffffffff;
	for (int i = 0; i < width * height; ++i) {
	    if (i < width || i >= width * (height - 1) || i % width == 0
		    || (i + 1) % width == 0)
		pixels[i] = bcol;
	    else
		pixels[i] = col;
	}
	return new Texture(width, height, pixels);
    }

    public static Texture makeTexture(int width, int height, int col) {
	return makeTexture(width, height, col, col);
    }

    public static Texture makeTexture(int width, int height, Color col) {
	if (col == null)
	    return makeTexture(width, height, 0);
	return makeTexture(width, height, col.getValue());
    }

    public int getWidth() {
	return width;
    }

    public int getHeight() {
	return height;
    }

    public int[] getData(int type) {
	if (type == TYPE_ARGB)
	    return pixels;
	if (pixelsrgb != null)
	    return pixelsrgb;
	pixelsrgb = new int[width * height];
	for (int i = 0; i < width * height; i++) {
	    int r = (pixels[i] & 0xff0000) >> 16;
	    int g = (pixels[i] & 0xff00) >> 8;
	    int b = (pixels[i] & 0xff);
	    pixelsrgb[i] = r << 16 | g << 8 | b;
	}
	return pixelsrgb;
    }

    public void removeColor(int col) {
	col = col & 0xffffff;
	for (int i = 0; i < width * height; i++) {
	    if ((pixels[i] & 0xffffff) == col) {
		pixels[i] = 0x0;
	    }
	}
	if (pixelsrgb == null)
	    return;
	for (int i = 0; i < width * height; i++) {
	    if (pixels[i] == 0x0)
		pixelsrgb[i] = 0x0;
	}
    }

}
