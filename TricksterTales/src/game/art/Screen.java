package game.art;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

public class Screen {

    private int width, height;
    private int[] pixels, imagePixels, depth;
    private BufferedImage img;

    public Screen(int width, int height) {
	this.width = width;
	this.height = height;
	this.pixels = new int[width * height];
	this.depth = new int[width * height];
	this.img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	this.imagePixels = ((DataBufferInt) this.img.getData().getDataBuffer())
		.getData();
    }

    public void toImage() {
	for (int x = 0; x < width; ++x) {
	    for (int y = 0; y < height; ++y) {
		imagePixels[x + y * width] = pixels[x + (height - y - 1)
			* width];
	    }
	}
	this.img.setRGB(0, 0, width, height, imagePixels, 0, width);
    }

    public BufferedImage getImage() {
	return img;
    }

    public int getWidth() {
	return width;
    }

    public int getHeight() {
	return height;
    }

    public void clear(int col) {
	for (int i = 0; i < pixels.length; ++i) {
	    pixels[i] = col;
	    depth[i] = -1;
	}
    }

    public void clear(Color col) {
	clear(col.getValue());
    }

    public void clear() {
	clear(0x0);
    }

    public void resetHeights() {
	for (int i = 0; i < depth.length; ++i) {
	    depth[i] = -1;
	}
    }

    public void drawRect(int x, int y, int z, int w, int h, int col) {
	if (z < 0)
	    return;
	if (w < 0) {
	    x -= w;
	    w = -w;
	}
	if (h < 0) {
	    y -= h;
	    h = -h;
	}
	for (int xp = x; xp < x + w; ++xp) {
	    if (xp < 0 || xp >= width)
		continue;
	    for (int yp = y; yp < y + h; ++yp) {
		if (yp < 0 || yp >= height)
		    continue;
		if (depth[xp + yp * width] > z)
		    continue;
		depth[xp + yp * width] = z;
		pixels[xp + yp * width] = col;
	    }
	}
    }

    public void drawRect(int x, int y, int z, int w, int h, Color col) {
	drawRect(x, y, z, w, h, col.getValue());
    }

    public void drawTopRect(int x, int y, int w, int h, Color col) {
	drawRect(x, y, Integer.MAX_VALUE, w, h, col);
    }

    public void drawTexture(int x, int y, int z, Texture tex) {
	if (tex == null)
	    return;
	if (z < 0)
	    return;
	int xp = 0, yp = 0;
	int[] texdata = tex.getData(Texture.TYPE_ARGB);
	for (int xx = 0; xx < tex.getWidth(); ++xx) {
	    xp = x + xx;
	    if (xp < 0 || xp >= width)
		continue;
	    for (int yy = 0; yy < tex.getHeight(); ++yy) {
		yp = y + yy;
		if (yp < 0 || yp >= height)
		    continue;
		if (depth[xp + yp * width] > z)
		    continue;
		if (Color.getAlpha(texdata[xx + yy * tex.getWidth()]) == 0x0)
		    continue;
		depth[xp + yp * width] = z;
		this.pixels[xp + yp * width] = texdata[xx + yy * tex.getWidth()];
	    }
	}
    }

    public void drawTopTexture(int x, int y, Texture tex) {
	drawTexture(x, y, Integer.MAX_VALUE, tex);
    }

    public void drawScreen(int x, int y, int z, Screen scr) {
	if (scr == null)
	    return;
	if (z < 0)
	    return;
	int xp = 0, yp = 0;
	int[] scrdata = scr.pixels;
	for (int xx = 0; xx < scr.getWidth(); ++xx) {
	    xp = x + xx;
	    if (xp < 0 || xp >= width)
		continue;
	    for (int yy = 0; yy < scr.getHeight(); ++yy) {
		yp = y + yy;
		if (yp < 0 || yp >= height)
		    continue;
		if (depth[xp + yp * width] > z)
		    continue;
		if (Color.getAlpha(scrdata[xx + yy * scr.getWidth()]) == 0x0)
		    continue;
		depth[xp + yp * width] = z;
		this.pixels[xp + yp * width] = scrdata[xx + yy * scr.getWidth()];
	    }
	}
    }

    public void placeScreen(int x, int y, int z, Screen scr, boolean back) {
	if (scr == null)
	    return;
	if (z < 0)
	    return;
	int xp = 0, yp = 0, scrz = 0;
	int[] scrdata, scrdepth;
	if (scr == this) {
	    scrdata = new int[scr.pixels.length];
	    System.arraycopy(scr.pixels, 0, scrdata, 0, scr.pixels.length);
	    scrdepth = new int[scr.depth.length];
	    System.arraycopy(scr.depth, 0, scrdepth, 0, scr.depth.length);
	} else {
	    scrdata = scr.pixels;
	    scrdepth = scr.depth;
	}
	for (int xx = 0; xx < scr.getWidth(); ++xx) {
	    xp = x + xx;
	    if (xp < 0 || xp >= width)
		continue;
	    for (int yy = 0; yy < scr.getHeight(); ++yy) {
		yp = y + yy;
		if (yp < 0 || yp >= height)
		    continue;
		scrz = scrdepth[xx + yy * width];
		if (!back && scrz < 0)
		    continue;
		if (depth[xp + yp * width] > z + scrz)
		    continue;
		if (Color.getAlpha(scrdata[xx + yy * scr.getWidth()]) == 0x0)
		    continue;
		depth[xp + yp * width] = z + scrz;
		this.pixels[xp + yp * width] = scrdata[xx + yy * scr.getWidth()];
	    }
	}
    }

    public void placeScreen(int x, int y, int z, Screen scr) {
	placeScreen(x, y, z, scr, true);
    }

    public void placeScreen(int x, int y, Screen scr) {
	placeScreen(x, y, 0, scr, true);
    }

}
