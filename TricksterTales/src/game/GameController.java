package game;

import errors.LevelLoadFailed;
import game.level.Level;
import game.level.LevelLoader;

public class GameController {

    private Level lvl;

    public GameController() {
	String first = "NOLEVEL";
	for (String key : LevelLoader.LEVEL_SIZES.keySet()) {
	    first = key;
	    break;
	}
	lvl = new Level(first);
    }

    public void update(double dt) {
	lvl.update(dt);
    }

    public void draw() {
	lvl.draw();
    }

    public void loadContent() {
	try {
	    lvl.loadContent();
	} catch (LevelLoadFailed e) {
	    e.printStackTrace();
	}
    }

    public void unloadContent() {
	lvl.unloadContent();
    }

}
