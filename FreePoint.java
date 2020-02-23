package paint;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;

public class FreePoint extends Tool {
	
	FreePoint() {
		super();
	}
	
	FreePoint(Point p, Color c, float s) {
		super(p, c, s);
	}
	
	public void draw(Graphics g) {
		var g2d = (Graphics2D)g;
		g2d.setStroke(new BasicStroke(this.size, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER, 1.0f));
		g2d.setColor(this.drawHue);
		var x = this.pnt.elementAt(0).x - (int)(this.size/2);
		var y = this.pnt.elementAt(0).y - (int)(this.size/2);
		g2d.fillOval(x, y, (int)this.size, (int)this.size);

	}
}
