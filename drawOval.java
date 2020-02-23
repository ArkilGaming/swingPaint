package paint;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;

public class drawOval extends Tool {
	
	drawOval() {
		super();
	}
	
	drawOval(Point p, Color c, float s) {
		super(p, c, s);
	}
	
	public void draw(Graphics g) {
		var g2d = (Graphics2D)g;
		g2d.setStroke(new BasicStroke(this.size, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER, 1.0f));
		g2d.setColor(this.drawHue);
		
		var x = this.pnt.elementAt(0).x;
		var y = this.pnt.elementAt(0).y;
		var x2 = this.pnt.elementAt(1).x - x;
		var y2 = this.pnt.elementAt(1).y - y;
			
		g2d.drawOval(x, y, x2, y2);
	}
}
