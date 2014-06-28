package game.objects;

import game.level.Level;

public class GameObject {

    public static enum SOLIDITY {
	NONSOLID, SEMISOLID, SOLID
    };

    public static final int MASK_ALL = 1;

    public double x, y, w, h;
    protected Level level;
    private boolean shouldRemove;
    public SOLIDITY solid;
    public int MASK;

    public GameObject(Level level, double x, double y, double w, double h) {
	this.level = level;
	this.x = x;
	this.y = y;
	this.w = w;
	this.h = h;
	solid = SOLIDITY.NONSOLID;
	MASK = MASK_ALL;
    }

    public Level getLevel() {
	return level;
    }

    public boolean shouldRemove() {
	return shouldRemove;
    }

    public void load() {
    }

    public void unload() {
    }

    public void update(double dt) {
    }

    public void draw(int z) {
    }

}
