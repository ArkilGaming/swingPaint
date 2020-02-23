package paint;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class paintMain extends JFrame {

	//Code variables
	private ButtonGroup groupTools;
	private int[] canevas = {800, 600}; //canevas size
	private JPanel contentPane = new JPanel();
	private Vector<Tool> strokes = new Vector<Tool>();
	
	//User variables
	private state curState = state.free;
	private Color curColor = Color.blue;
	private float size = 10;
	private boolean nuLine = false;
	
	public enum state {
		free, oval, rect, tri, remove;
	}
	
	
	public class mouseMotion implements MouseMotionListener {
		@Override
		public void mouseDragged(MouseEvent e) {
			
			Point temPnt = new Point(e.getX(), e.getY());

			if (nuLine) {
				switch(curState) {
				default:
					strokes.add(new FreeLine(temPnt, curColor, size));
					break;
				case oval:
					strokes.add(new drawOval(temPnt, curColor, size));
					strokes.elementAt(strokes.size()-1).addPnt(temPnt);
					break;
				case rect:
					strokes.add(new drawRect(temPnt, curColor, size));
					strokes.elementAt(strokes.size()-1).addPnt(temPnt);
					break;
				case tri:
					strokes.add(new drawTri(temPnt, curColor, size));
					strokes.elementAt(strokes.size()-1).addPnt(temPnt);
					break;
				}
				nuLine = false;
			}
			else {
				switch(curState) {
				case free:					
					strokes.elementAt(strokes.size()-1).addPnt(temPnt);
					break;
				default :
					strokes.elementAt(strokes.size()-1).setPnt(1, temPnt);
					}
			}
			repaint();
		}

		@Override
		public void mouseMoved(MouseEvent e) {}}
	
	public class mouse implements MouseListener {
		
		@Override
		public void mouseClicked(MouseEvent e) {
			switch(curState) {
			default: 
				strokes.add(new FreePoint(new Point(e.getX(), e.getY()), curColor, size));
				System.out.println("stroke added");
				repaint();
			}
		}

		@Override
		public void mousePressed(MouseEvent e) {
			nuLine = true;
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			nuLine = false;
		}

		@Override
		public void mouseEntered(MouseEvent e) {}

		@Override
		public void mouseExited(MouseEvent e) {}

	}
	
	public class key implements KeyListener {

		@Override
		public void keyTyped(KeyEvent e) {}

		@Override
		public void keyPressed(KeyEvent e) {
			switch(e.getKeyCode()) {
			//COLORS (1 - 4)
			case KeyEvent.VK_1:
				curColor = Color.blue;
				break;
			case KeyEvent.VK_2:
				curColor = Color.green;
				break;
			case KeyEvent.VK_3:
				curColor = Color.red;
				break;
			case KeyEvent.VK_4:
				curColor = Color.black;
				break;
			
			//MODES (QWER)
			case KeyEvent.VK_Q: curState = state.free; break; //free (state 1 used here)
			case KeyEvent.VK_W: curState = state.oval; System.out.println("Oval"); break; //Oval
			case KeyEvent.VK_E: curState = state.rect; break; //Rectangle
			case KeyEvent.VK_R: curState = state.tri; break; //Triangles
			case KeyEvent.VK_DELETE: strokes.removeAllElements(); repaint(); break;
			}
		}

		@Override
		public void keyReleased(KeyEvent e) {}

	}
	
	class layer extends JPanel {
		
		layer() {
			setFocusable(true);
			requestFocusInWindow();
			
			setBackground(Color.white);
		};
		
		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			
			if(!strokes.isEmpty()) {
				for (int i = 0; i < strokes.size(); ++i) {
					strokes.elementAt(i).draw(g);
				}
			}
		}
	}
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					paintMain frame = new paintMain();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public paintMain() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		contentPane.setBackground(Color.darkGray);
		setBounds(100, 100, 1000, 1000);
		contentPane.setLayout(null);
		setContentPane(contentPane);
		
		groupTools = new ButtonGroup();
		
		//drawing layers setup
		mouse ml = new mouse();
		mouseMotion mml = new mouseMotion();
		key kl = new key();
		
		layer baseLayer = new layer();
		
		baseLayer.addMouseListener(ml);
		baseLayer.addMouseMotionListener(mml);
		baseLayer.addKeyListener(kl);
		
		baseLayer.setBounds(getWidth()/2 - canevas[0]/2, getHeight()/2 - canevas[1]/2, canevas[0], canevas[1]);
		contentPane.add(baseLayer);
	}
}