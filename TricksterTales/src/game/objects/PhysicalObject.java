package game.objects;

import errors.NoCollisionException;
import game.level.Level;

import java.util.concurrent.Callable;

import utils.Reference;

public class PhysicalObject extends GameObject {

    // Collision stuff
    private boolean boundbl, boundbr, boundbu, boundbd;
    private double boundl, boundr, boundu, boundd;

    public double xspeed, yspeed, gravity;
    public double maxxspeed, maxyspeed;
    private boolean onGround;

    private Callable<Object> hitl, hitr, hitu, hitd;

    public PhysicalObject(Level level, double x, double y, double w, double h) {
	super(level, x, y, w, h);
	xspeed = 0;
	yspeed = 0;
	maxxspeed = 0;
	maxyspeed = 0;
	gravity = 0;
	onGround = false;
	boundbl = boundbr = boundbu = boundbd = false;
	boundl = boundr = boundu = boundd = 0;
	hitl = hitr = hitu = hitd = null;
    }

    public boolean isOnGround() {
	return onGround;
    }

    public void setHitLeft(Callable<Object> func) {
	if (func == null)
	    hitl = null;
	else
	    hitl = func;
    }

    public void setHitRight(Callable<Object> func) {
	if (func == null)
	    hitr = null;
	else
	    hitr = func;
    }

    public void setHitUp(Callable<Object> func) {
	if (func == null)
	    hitu = null;
	else
	    hitu = func;
    }

    public void setHitDown(Callable<Object> func) {
	if (func == null)
	    hitd = null;
	else
	    hitd = func;
    }

    protected void hitLeft() {
    }

    protected void hitRight() {
    }

    protected void hitUp() {
    }

    protected void hitDown() {
    }

    public void update(double dt) {
	updateCollisionsHoriz();
	if (xspeed > maxxspeed || xspeed < -maxxspeed)
	    xspeed = Math.signum(xspeed) * maxxspeed;
	x += xspeed;
	if (xspeed >= 0) {
	    if (boundbr) {
		if (x + w >= boundr) {
		    if (xspeed != 0 && (hitr != null)) {
			try {
			    hitr.call();
			} catch (Exception e) {
			    e.printStackTrace();
			}
			hitRight();
		    }
		    xspeed = 0;
		}
		x = Math.min(boundr, x + w) - w;
	    }
	}
	if (boundbl) {
	    if (x <= boundl) {
		if (xspeed != 0 && (hitl != null)) {
		    try {
			hitl.call();
		    } catch (Exception e) {
			e.printStackTrace();
		    }
		    hitLeft();
		}
		xspeed = 0;
	    }
	    x = Math.max(boundl, x);
	}

	updateCollisionsVert();
	if (yspeed > maxyspeed || yspeed < -maxyspeed)
	    yspeed = Math.signum(yspeed) * maxyspeed;
	if (onGround == false)
	    yspeed = Math.max(-maxyspeed, yspeed - gravity);
	if (yspeed > maxyspeed)
	    yspeed = maxyspeed;
	y += yspeed;
	if (yspeed >= 0) {
	    if (boundbu) {
		if (y + h >= boundu) {
		    if (yspeed != 0 && (hitu != null)) {
			try {
			    hitu.call();
			} catch (Exception e) {
			    e.printStackTrace();
			}
			hitUp();
		    }
		    yspeed = 0;
		}
		y = Math.min(boundu, y + h) - h;
	    }
	}
	if (boundbd) {
	    if (y <= boundd) {
		if (yspeed != 0 && (hitd != null)) {
		    try {
			hitd.call();
		    } catch (Exception e) {
			e.printStackTrace();
		    }
		    hitDown();
		}
		yspeed = 0;
		onGround = true;
	    } else
		onGround = false;
	    y = Math.max(boundu, y);
	} else
	    onGround = false;
    }

    private void updateCollisionsHoriz() {
	if (level == null)
	    return;
	try {
	    boundl = level.collideLeft(this, maxxspeed * Reference.BLOCKSIZE);
	    boundbl = true;
	} catch (NoCollisionException e) {
	    boundl = 0;
	    boundbl = false;
	}
	try {
	    boundr = level.collideRight(this, maxxspeed * Reference.BLOCKSIZE);
	    boundbr = true;
	} catch (NoCollisionException e) {
	    boundr = 0;
	    boundbr = false;
	}
    }

    private void updateCollisionsVert() {
	if (level == null)
	    return;
	try {
	    boundu = level.collideUp(this, maxyspeed * Reference.BLOCKSIZE);
	    boundbu = true;
	} catch (NoCollisionException e) {
	    boundu = 0;
	    boundbu = false;
	}
	try {
	    boundd = level.collideDown(this, maxyspeed * Reference.BLOCKSIZE);
	    boundbd = true;
	} catch (NoCollisionException e) {
	    boundd = 0;
	    boundbd = false;
	}
    }

}
