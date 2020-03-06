package paint;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;

public class drawRect extends Tool {
	
	drawRect() {
		super();
	}
	
	drawRect(Point p, Color c, float s) {
		super(p, c, s);
	}
	
	public void draw(Graphics g) {
		var g2d = (Graphics2D)g;
		g2d.setStroke(new BasicStroke(this.size, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER, 1.0f));
		g2d.setColor(this.drawHue);
		
		var x = this.pnt.elementAt(0).x;
		var y = this.pnt.elementAt(0).y;
		var x2 = this.pnt.elementAt(1).x;
		var y2 = this.pnt.elementAt(1).y;
		
		int[] temp = super.checkNegative(x, x2, y, y2);
		
		g2d.drawRect(temp[0], temp[2], temp[1] - temp[0], temp[3] - temp[2]);
	}
}
