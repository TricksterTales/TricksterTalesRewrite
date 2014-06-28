package game.objects.special;

import game.art.Renderer;
import game.level.Level;
import game.objects.GameObject;

public class Door extends GameObject {

    public String doorDest, levelDest;

    public Door(Level level, double x, double y) {
	super(level, x, y, 1, 1);
	doorDest = null;
	levelDest = null;
    }

    public void setGoal(String doorDest, String levelDest) {

    }
    
    public void draw(int z) {
	Renderer.draw(x, y, z, Renderer.IMAGE.DOOR);
    }

}
