package game.objects.special;

import game.art.Renderer;
import game.level.Level;
import game.objects.GameObject;

public class Ground extends GameObject {

    public Ground(Level level, double x, double y) {
	super(level, x, y, 1, 1);
	solid = GameObject.SOLIDITY.SOLID;
    }

    public void draw(int z) {
	Renderer.draw(x, y, z, Renderer.IMAGE.GROUND);
    }

}
