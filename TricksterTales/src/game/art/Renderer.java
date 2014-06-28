package game.art;

import java.awt.Graphics;

import main.Main;
import utils.GeneralUtils;

public class Renderer {

    public static final int BLOCKSIZE = 16;

    public static enum IMAGE {
	NONE, GROUND, DOOR, SIGN, count
    };

    private static IMAGE[] images = IMAGE.values();

    private static Texture Textures[];

    private static Screen view;

    public static void draw(double x, double y, double z, IMAGE pic) {
	if (!GeneralUtils.contains(images, pic))
	    return;
	if (view == null)
	    return;
	if (Textures == null)
	    return;
	switch (pic) {
	case NONE:
	    break;
	case GROUND:
	    view.drawTexture((int) (x * BLOCKSIZE), (int) (y * BLOCKSIZE),
		    (int) z, Textures[1]);
	    break;
	case DOOR:
	    view.drawTexture((int) (x * BLOCKSIZE), (int) (y * BLOCKSIZE),
		    (int) z, Textures[2]);
	    break;
	case SIGN:
	    view.drawTexture((int) (x * BLOCKSIZE), (int) (y * BLOCKSIZE),
		    (int) z, Textures[3]);
	    break;
	default:
	    break;
	}
    }

    public static void clear(int col) {
	if (view == null)
	    return;
	view.clear(col);
    }

    public static void drawOnScreen(Graphics g) {
	if (g == null)
	    return;
	if (view == null)
	    return;
	view.toImage();
	g.drawImage(Renderer.view.getImage(), 0, 0, Main.VIRTUAL_WIDTH,
		Main.VIRTUAL_HEIGHT, null);
    }

    public static void drawTexture(int x, int y, int z, Texture tex) {
	if (tex == null)
	    return;
	if (view == null)
	    return;
	view.drawTexture(x, y, z, tex);
    }

    public static void loadContent() {
	view = new Screen(Main.VIRTUAL_WIDTH, Main.VIRTUAL_HEIGHT);
	Textures = new Texture[IMAGE.count.ordinal()];
	// NONE
	Textures[0] = null;
	// GROUND
	Textures[1] = Texture.makeTexture(BLOCKSIZE, BLOCKSIZE, 0xff804000,
		0xff000000);
	// DOOR
	Textures[2] = Texture.makeTexture(BLOCKSIZE, BLOCKSIZE * 2, 0xffff8040,
		0xff000000);
	// SIGN
	Textures[3] = Texture.makeTexture(BLOCKSIZE, BLOCKSIZE, 0xffffff80,
		0xff000000);
    }

}
