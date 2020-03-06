package paint;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;

public class FreeLine extends Tool {
	
	private boolean erease = false;
	
	FreeLine() {
		super();
	}
	
	FreeLine(Point p, Color c, float s) {
		super(p, c, s);
	}
	
	public void draw(Graphics g) {
		var g2d = (Graphics2D)g;
		g2d.setStroke(new BasicStroke(this.size, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER, 1.0f));
		g2d.setColor(this.drawHue);
		
		for (int i = 1; i < this.pnt.size(); ++i) {
			var x = this.pnt.elementAt(i-1).x;
			var y = this.pnt.elementAt(i-1).y;
			var x2 = this.pnt.elementAt(i).x;
			var y2 = this.pnt.elementAt(i).y;
			
			g2d.drawLine(x, y, x2, y2);
		}
	}

	public boolean getErease() {
		return erease;
	}

	public void setErease(boolean erease) {
		this.erease = erease;
	}
}
