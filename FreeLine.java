package paint;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;

public class FreeLine extends Tool {
	
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
			var x = this.pnt.elementAt(i-1).x - (int)this.size/2;
			var y = this.pnt.elementAt(i-1).y - (int)this.size/2;
			var x2 = this.pnt.elementAt(i).x - (int)this.size/2;
			var y2 = this.pnt.elementAt(i).y - (int)this.size/2;
			
			g2d.drawLine(x, y, x2, y2);
		}

	}
}
