package game.objects.special;

import game.art.Renderer;
import game.level.Level;
import game.objects.GameObject;

public class Sign extends GameObject {

    public String msg;

    public Sign(Level level, double x, double y) {
	super(level, x, y, 1, 1);
	msg = null;
    }

    public void setMessage(String msg) {
	this.msg = msg;
    }

    public void draw(int z) {
	Renderer.draw(x, y, z, Renderer.IMAGE.SIGN);
    }

}
