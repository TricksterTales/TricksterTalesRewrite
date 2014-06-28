package game.level;

import errors.LevelLoadFailed;
import errors.NoCollisionException;
import game.level.LevelInfo.DoorInfo;
import game.level.LevelInfo.SignInfo;
import game.objects.GameObject;
import game.objects.special.Door;
import game.objects.special.Ground;
import game.objects.special.Sign;

import java.awt.Dimension;
import java.util.LinkedList;
import java.util.Set;

import utils.ConsoleUtils;
import utils.MathUtils;
import utils.Reference;

public class Level {

    public static final char NOTHING = '-';

    public final String NAME;
    public final int WIDTH, HEIGHT;
    private boolean VALID;

    // Ordering: Background, Scenery, Tiles, Objects, Player
    // Foreground, Highlights, Gui

    public LinkedList<GameObject> HIGHLIGHTS;
    public GameObject[] TILES_FOREGROUND;
    public LinkedList<GameObject> OBJECTS;
    public GameObject[] TILES;
    public GameObject[] TILES_SCENERY;
    public GameObject[] TILES_BACKGROUND;

    public Level(String name) {
	NAME = name;

	Dimension size = LevelLoader.LEVEL_SIZES.get(name);
	if (size == null) {
	    VALID = false;
	    WIDTH = 0;
	    HEIGHT = 0;
	} else {
	    VALID = true;
	    WIDTH = size.width;
	    HEIGHT = size.height;
	}
    }

    public boolean isValid() {
	return VALID;
    }

    public boolean addHighlight(GameObject obj) {
	if (!VALID)
	    return false;
	if (obj == null)
	    return false;
	HIGHLIGHTS.add(obj);
	return false;
    }

    public boolean setForeground(GameObject obj, int x, int y) {
	if (!VALID)
	    return false;
	if (obj == null)
	    return false;
	if (!MathUtils.inRect(0, 0, WIDTH, HEIGHT, x, y))
	    return false;
	GameObject cobj = TILES_FOREGROUND[x + y * WIDTH];
	if (cobj != null)
	    cobj.unload();
	TILES_FOREGROUND[x + y * WIDTH] = obj;
	return (cobj != null);
    }

    public boolean addObject(GameObject obj) {
	if (!VALID)
	    return false;
	if (obj == null)
	    return false;
	OBJECTS.add(obj);
	return false;
    }

    public boolean setTile(GameObject obj, int x, int y) {
	if (!VALID)
	    return false;
	if (obj == null)
	    return false;
	if (!MathUtils.inRect(0, 0, WIDTH, HEIGHT, x, y))
	    return false;
	GameObject cobj = TILES[x + y * WIDTH];
	if (cobj != null)
	    cobj.unload();
	TILES[x + y * WIDTH] = obj;
	return (cobj != null);
    }

    public GameObject getTile(int x, int y) {
	return null;
    }

    public boolean setScenery(GameObject obj, int x, int y) {
	if (!VALID)
	    return false;
	if (obj == null)
	    return false;
	if (!MathUtils.inRect(0, 0, WIDTH, HEIGHT, x, y))
	    return false;
	GameObject cobj = TILES_SCENERY[x + y * WIDTH];
	if (cobj != null)
	    cobj.unload();
	TILES_SCENERY[x + y * WIDTH] = obj;
	return (cobj != null);
    }

    public boolean setBackground(GameObject obj, int x, int y) {
	if (!VALID)
	    return false;
	if (obj == null)
	    return false;
	if (!MathUtils.inRect(0, 0, WIDTH, HEIGHT, x, y))
	    return false;
	GameObject cobj = TILES_BACKGROUND[x + y * WIDTH];
	if (cobj != null)
	    cobj.unload();
	TILES_BACKGROUND[x + y * WIDTH] = obj;
	return (cobj != null);
    }

    public void loadContent() throws LevelLoadFailed {
	unloadContent();
	if (!VALID)
	    return;
	HIGHLIGHTS = new LinkedList<GameObject>();
	TILES_FOREGROUND = new GameObject[WIDTH * HEIGHT];
	OBJECTS = new LinkedList<GameObject>();
	TILES = new GameObject[WIDTH * HEIGHT];
	TILES_SCENERY = new GameObject[WIDTH * HEIGHT];
	TILES_BACKGROUND = new GameObject[WIDTH * HEIGHT];

	String data = LevelLoader.LEVEL_BLOCKS.get(NAME);
	if (data == null) {
	    unloadContent();
	    VALID = false;
	    throw new LevelLoadFailed("Data not found for \'" + NAME + "\'");
	}

	int len = data.length();
	GameObject obj;
	char ch;
	int x, y;
	for (int i = 0; i < len; ++i) {
	    ch = data.charAt(i);
	    x = i % WIDTH;
	    y = i / WIDTH;
	    switch (ch) {
	    case 'G':
		obj = new Ground(this, x, y);
		obj.load();
		TILES[i] = obj;
		break;
	    default:
		TILES[i] = null;
		continue;
	    }
	}

	LevelInfo linfo = LevelLoader.LEVEL_DATA.get(NAME);
	if (linfo == null)
	    return;
	Door dobj;
	DoorInfo dinfo;
	Set<String> dset = linfo.Doors.keySet();
	for (String key : dset) {
	    dinfo = linfo.Doors.get(key);
	    if (dinfo == null)
		continue;
	    if (!MathUtils.inRect(0, 0, WIDTH, HEIGHT, dinfo.x, dinfo.y))
		continue;
	    dobj = new Door(this, dinfo.x, dinfo.y);
	    dobj.setGoal(dinfo.doorDest, dinfo.levelDest);
	    if (setScenery(dobj, dinfo.x, dinfo.y)) {
		ConsoleUtils.println("Door position contains contents ("
			+ dinfo.x + "," + dinfo.y + ")");
	    }
	}

	Sign sobj;
	SignInfo sinfo;
	Set<String> sset = linfo.Signs.keySet();
	for (String key : sset) {
	    sinfo = linfo.Signs.get(key);
	    if (sinfo == null)
		continue;
	    if (!MathUtils.inRect(0, 0, WIDTH, HEIGHT, sinfo.x, sinfo.y))
		continue;
	    sobj = new Sign(this, sinfo.x, sinfo.y);
	    sobj.setMessage(sinfo.msg);
	    if (setScenery(sobj, sinfo.x, sinfo.y)) {
		ConsoleUtils.println("Sign position contains contents ("
			+ sinfo.x + "," + sinfo.y + ")");
	    }
	}
    }

    public void unloadContent() {
	GameObject obj;
	if (HIGHLIGHTS != null && HIGHLIGHTS.size() > 0) {
	    while (HIGHLIGHTS.size() > 0) {
		obj = HIGHLIGHTS.get(0);
		if (obj != null)
		    obj.unload();
		HIGHLIGHTS.remove(0);
	    }
	}
	HIGHLIGHTS = null;

	if (TILES_FOREGROUND != null) {
	    for (int i = 0; i < TILES_FOREGROUND.length; ++i) {
		obj = TILES_FOREGROUND[i];
		if (obj != null)
		    obj.unload();
		TILES_FOREGROUND[i] = null;
	    }
	}
	TILES_FOREGROUND = null;

	if (OBJECTS != null && OBJECTS.size() > 0) {
	    while (OBJECTS.size() > 0) {
		obj = OBJECTS.get(0);
		if (obj != null)
		    obj.unload();
		OBJECTS.remove(0);
	    }
	}
	OBJECTS = null;

	if (TILES != null) {
	    for (int i = 0; i < TILES.length; ++i) {
		obj = TILES[i];
		if (obj != null)
		    obj.unload();
		TILES[i] = null;
	    }
	}
	TILES = null;

	if (TILES_SCENERY != null) {
	    for (int i = 0; i < TILES_SCENERY.length; ++i) {
		obj = TILES_SCENERY[i];
		if (obj != null)
		    obj.unload();
		TILES_SCENERY[i] = null;
	    }
	}
	TILES_SCENERY = null;

	if (TILES_BACKGROUND != null) {
	    for (int i = 0; i < TILES_BACKGROUND.length; ++i) {
		obj = TILES_BACKGROUND[i];
		if (obj != null)
		    obj.unload();
		TILES_BACKGROUND[i] = null;
	    }
	}
	TILES_BACKGROUND = null;
    }

    public void update(double dt) {
	if (!VALID)
	    return;

    }

    // Ordering: Background, Scenery, Tiles, Objects, Player
    // Foreground, Highlights, Gui
    public void draw() {
	if (!VALID)
	    return;
	GameObject obj;
	// Background tiles at z0
	for (int i = 0; i < WIDTH * HEIGHT; ++i) {
	    if (TILES_BACKGROUND[i] == null)
		continue;
	    TILES_BACKGROUND[i].draw(0);
	}
	// Scenery tiles at z5
	for (int i = 0; i < WIDTH * HEIGHT; ++i) {
	    if (TILES_SCENERY[i] == null)
		continue;
	    TILES_SCENERY[i].draw(5);
	}
	// Standard tiles at z10
	for (int i = 0; i < WIDTH * HEIGHT; ++i) {
	    if (TILES[i] == null)
		continue;
	    TILES[i].draw(10);
	}
	// Standard objects at z15
	for (int i = 0; i < OBJECTS.size(); ++i) {
	    obj = OBJECTS.get(i);
	    if (obj == null)
		continue;
	    obj.draw(15);
	}
    }

    public double collideLeft(GameObject obj, double maxDist)
	    throws NoCollisionException {
	if (!VALID || (maxDist < 0) || (obj == null))
	    throw new NoCollisionException();
	double bsize = Reference.BLOCKSIZE;
	double x1 = obj.x, y1 = obj.y, y2 = y1 + obj.h;
	int ix1 = (int) Math.floor(x1 / bsize), iy1 = (int) Math.floor(y1
		/ bsize), iy2 = (int) Math.floor(y2 / bsize), d = (int) Math
		.floor(maxDist / bsize);
	int mask = obj.MASK;
	int c = 0, h = 0, m = 0;
	GameObject g;
	while (c <= d) {
	    if (ix1 - c < 0)
		break;
	    h = 0;
	    while (h <= iy2 - iy1) {
		g = getTile(ix1 - c, iy1 + h);
		if (g == null) {
		    ++h;
		    continue;
		}
		m = g.MASK;
		if (g.solid == GameObject.SOLIDITY.SOLID && ((mask & m) != 0)) {
		    return g.x + g.w;
		}
		++h;
	    }
	    ++c;
	}
	throw new NoCollisionException();
    }

    public double collideLeft(GameObject obj) throws NoCollisionException {
	return collideLeft(obj, 10 * Reference.BLOCKSIZE);
    }

    public double collideRight(GameObject obj, double maxDist)
	    throws NoCollisionException {
	if (!VALID || (maxDist < 0) || (obj == null))
	    throw new NoCollisionException();
	double bsize = Reference.BLOCKSIZE;
	double x2 = obj.x + obj.w, y1 = obj.y, y2 = y1 + obj.h;
	int ix2 = (int) Math.floor(x2 / bsize), iy1 = (int) Math.floor(y1
		/ bsize), iy2 = (int) Math.floor(y2 / bsize), d = (int) Math
		.floor(maxDist / bsize);
	int mask = obj.MASK;
	int c = 0, h = 0, m = 0;
	GameObject g;
	while (c <= d) {
	    if (ix2 + c >= WIDTH)
		break;
	    h = 0;
	    while (h <= iy2 - iy1) {
		g = getTile(ix2 + c, iy1 + h);
		if (g == null) {
		    ++h;
		    continue;
		}
		m = g.MASK;
		if (g.solid == GameObject.SOLIDITY.SOLID && ((mask & m) != 0)) {
		    return g.x;
		}
		++h;
	    }
	    ++c;
	}
	throw new NoCollisionException();
    }

    public double collideRight(GameObject obj) throws NoCollisionException {
	return collideRight(obj, 10 * Reference.BLOCKSIZE);
    }

    public double collideUp(GameObject obj, double maxDist)
	    throws NoCollisionException {
	if (!VALID || (maxDist < 0) || (obj == null))
	    throw new NoCollisionException();
	double bsize = Reference.BLOCKSIZE;
	double x1 = obj.x, x2 = x1 + obj.w, y2 = obj.y + obj.h;
	int ix1 = (int) Math.floor(x1 / bsize), ix2 = (int) Math.floor(x2
		/ bsize), iy2 = (int) Math.floor(y2 / bsize), d = (int) Math
		.floor(maxDist / bsize);
	int mask = obj.MASK;
	int c = 0, h = 0, m = 0;
	GameObject g;
	while (c <= d) {
	    if (iy2 + c >= HEIGHT)
		break;
	    h = 0;
	    while (h <= ix2 - ix1) {
		g = getTile(ix1 + h, iy2 + c);
		if (g == null) {
		    ++h;
		    continue;
		}
		m = g.MASK;
		if (g.solid == GameObject.SOLIDITY.SOLID && ((mask & m) != 0)) {
		    return g.y;
		}
		++h;
	    }
	    ++c;
	}
	throw new NoCollisionException();
    }

    public double collideUp(GameObject obj) throws NoCollisionException {
	return collideUp(obj, 10 * Reference.BLOCKSIZE);
    }

    public double collideDown(GameObject obj, double maxDist)
	    throws NoCollisionException {
	if (!VALID || (maxDist < 0) || (obj == null))
	    throw new NoCollisionException();
	double bsize = Reference.BLOCKSIZE;
	double x1 = obj.x, x2 = x1 + obj.w, y1 = obj.y;
	int ix1 = (int) Math.floor(x1 / bsize), ix2 = (int) Math.floor(x2
		/ bsize), iy1 = (int) Math.floor(y1 / bsize), d = (int) Math
		.floor(maxDist / bsize);
	int mask = obj.MASK;
	int c = 0, h = 0, m = 0;
	GameObject g;
	while (c <= d) {
	    if (iy1 - c < 0)
		break;
	    h = 0;
	    while (h <= ix2 - ix1) {
		g = getTile(ix1 + h, iy1 - c);
		if (g == null) {
		    ++h;
		    continue;
		}
		m = g.MASK;
		if ((g.solid == GameObject.SOLIDITY.SOLID || g.solid == GameObject.SOLIDITY.SEMISOLID)
			&& ((mask & m) != 0)) {
		    return g.y + g.h;
		}
		++h;
	    }
	    ++c;
	}
	throw new NoCollisionException();
    }

    public double collideDown(GameObject obj) throws NoCollisionException {
	return collideDown(obj, 10 * Reference.BLOCKSIZE);
    }

    public boolean isColliding(GameObject obj) {
	if (!VALID)
	    return false;
	double bsize = Reference.BLOCKSIZE;
	double x1 = obj.x, x2 = x1 + obj.w, y1 = obj.y, y2 = y1 + obj.h;
	int ix1 = (int) Math.floor(x1 / bsize), ix2 = (int) Math.floor(x2
		/ bsize), iy1 = (int) Math.floor(y1 / bsize), iy2 = (int) Math
		.floor(y2 / bsize);

	int x = 0, y = 0, m = 0;
	GameObject g;
	int mask = obj.MASK;
	x = ix1;
	while (x <= ix2) {
	    y = iy1;
	    while (y <= iy2) {
		g = getTile(x, y);
		if (g == null) {
		    ++y;
		    continue;
		}
		m = g.MASK;
		if (g.solid == GameObject.SOLIDITY.SOLID && ((mask & m) != 0))
		    return true;
		++y;
	    }
	    ++x;
	}
	return false;
    }

}
