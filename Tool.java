package paint;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.Vector;

public abstract class Tool {
	protected Vector<Point> pnt = new Vector<Point>(); //mouse position
	protected Color drawHue; //brush and background color
	protected float size; //brush size
	private boolean drawing = false;

	
	Tool(Point p, Color c, float s) {
		this.pnt.add(p);
		this.drawHue = c;
		this.size = s;
	}

	Tool() {}
	
	void draw(Graphics g) {
		
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
		pnt.set(index, temPnt);
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
}
