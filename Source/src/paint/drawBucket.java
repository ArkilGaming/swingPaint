/*
 * It was an attempt
 * Still mad at it
 * because the problem is oblivious
 * the solution isn't
 * or i'm stupid
 * also very likely
 * now I'm writing this instead of
 * fixing the controls
 * UUUUUUUUUUGH anyway
 * it's my project, I can afford to be lazy
 */

package paint;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;

public class drawBucket extends Tool {
	
	private BufferedImage img;
	private Point origin = new Point();
	private boolean dup = false;
	
	drawBucket() {
		super();
	}
	
	drawBucket(Point p, Color c, BufferedImage i) {
		super(p, c);
		img = i;
	}
	
	public void draw(Graphics g) {
		var g2d = (Graphics2D)g;
		g2d.setStroke(new BasicStroke(this.size, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER, 1.0f));
		g2d.setColor(this.drawHue);
		
		origin = this.pnt.elementAt(0);
		
		getPoints(origin.x, origin.y, g2d);
		
		for (int i = 0; i < pnt.size(); ++i) {
			g2d.fillOval(pnt.elementAt(i).x, pnt.elementAt(i).y, 1, 1);
		}
	}
	
	void getPoints(int x, int y, Graphics2D g2d) {
		System.out.println(new Color(img.getRGB(x,y)));
		//check for duplicate
		dup = false;
		for (int i = 1; i < pnt.size();) {
			if (pnt.size() != 0) {
				if (pnt.elementAt(i).x != x || pnt.elementAt(i).y != y)
					++i;
				else
					dup = true;
					i = pnt.size();
			}
			else
				i = pnt.size();
		}
		if (dup) {
			System.out.println("dup = tru");
			return;
		}
		//out of bound
		if (x > img.getWidth() || x < 0) return;
		else if (y > img.getHeight() || y < 0) return;
		//Already the color that is supposed to be replaced
		else if (new Color(img.getRGB(origin.x, origin.y)) == this.drawHue) {
			return;
		}
		//Not the color supposed to be replaced
		else if (img.getRGB(x, y) != img.getRGB(origin.x, origin.y)) return; 
		//Right color, do same in 4 directions
		else if (img.getRGB(x, y) == img.getRGB(origin.x, origin.y)) {
			System.out.println(x + " " + y);
			pnt.add(new Point(x, y));
			getPoints(x+1, y, g2d);
			getPoints(x, y+1, g2d);
			getPoints(x-1, y, g2d);
			getPoints(x, y-1, g2d);
			return;
		}
	}
}
