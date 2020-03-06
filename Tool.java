package paint;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.Vector;

public abstract class Tool {
	protected Vector<Point> pnt = new Vector<Point>(); //mouse position
	protected Color drawHue; //brush and background color
	protected float size; //brush size
	
	Tool(Point p, Color c, float s) {
		this.pnt.add(p);
		this.drawHue = c;
		this.size = s;
	}
	
	Tool(Point p, Color c) {
		this.drawHue = c;
		this.pnt.add(p);
	}
	
	Tool() {}
	
	void draw(Graphics g) {
		
	}
	
	public int[] checkNegative(int x1, int x2, int y1, int y2) {
		if (x2 < x1) {
			var tempX = x1;
			x1 = x2;
			x2 = tempX;
		}
		
		if (y2 < y1) {
			var tempY = y1;
			y1 = y2;
			y2 = tempY;
		}
		
		return new int[] {x1, x2, y1, y2};
	}
	
	//GETTERS & SETTERS
	public Vector<Point> getMousePos() {
		return pnt;
	}

	public void setMousePos(Vector<Point> mousePos) {
		this.pnt = mousePos;
	}

	public void addPnt(Point temPnt) {
		pnt.add(temPnt);
	}
	
	public void setPnt(int index, Point temPnt) {
		pnt.remove(index);
		pnt.add(index, temPnt);
	}
	
	public void remLastPnt() {
		pnt.remove(pnt.size()-1);
	}

	public Color getDrawHue() {
		return drawHue;
	}

	public void setDrawHue(Color drawHue) {
		this.drawHue = drawHue;
	}

	public float getSize() {
		return size;
	}

	public void setSize(float size) {
		this.size = size;
	}
	
	public boolean getErease() {
		return false;
	};
	
	public void setErease(boolean e) {};
}
