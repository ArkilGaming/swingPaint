package paint;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.JLabel;

public class paintMain extends JFrame {
	private static final long serialVersionUID = 1L;

	//UI Size
	private final int btnSize = 30;
	
	//Code variables
	private ButtonGroup groupBtn;
	private JPanel contentPane = new JPanel();
	private Vector<Tool> strokes = new Vector<Tool>();
	private Vector<JButton> btns = new Vector<JButton>();
	private Vector<JButton> palette = new Vector<JButton>();
	
	private JButton col1btn, col2btn,
		playBtn;
	private layer baseLayer;
	private boolean Lclick = false, //False = left, right = true
			nuLine = false;
	private int playLen = 0,
			playback = 0,
			playSpd = 150;
	private BufferedImage img;

	private final JLabel sizeLabel = new JLabel("Size : ");
	private JTextField sizeField;
	
	//User variables
	private state curState = state.free, tempState;
	private Color curColor, col1 = Color.blue, col2 = Color.green;
	private float size = 10;
		
	public Color[] cols = new Color[] {Color.red, Color.green, Color.blue, Color.black, Color.white, Color.cyan, Color.magenta, Color.yellow};
	public enum state {
		free, oval, rect, tri, erease, bucket, pick;
	}
	
	public BufferedImage getImage (JPanel surface)
  	{
		Dimension size = new Dimension(surface.getWidth(), surface.getHeight());
		BufferedImage image = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_RGB);
    	Graphics2D g2 = image.createGraphics();
    	surface.paint(g2);
    	return image;
     }
	
	//A single layer
	@SuppressWarnings("serial")
	class layer extends JPanel {

		Timer play = new Timer(playSpd, new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (playLen < strokes.size()) {
					System.out.println(playLen);
					repaint();
					++playLen;
				}
			}
		});
		
			layer() {
				setFocusable(true);
				requestFocusInWindow();
				setOpaque(true);
				
				setBackground(Color.white);
			}
			
			//Draw strokes
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				
				switch(playback) {
				//No timer
				case 0: playLen = strokes.size(); break;
				//Start timer
				case 1:
					playLen = 0;
					play.start();
					playback = 2;
					break;
				//Test if playback is done
				default:
					if(playLen == strokes.size()) {
	 					play.stop();
	 					playback = 0;
	 					System.out.println("done");
	 				}
					break;
				}
					
					if(!strokes.isEmpty()) {
						for (int i = 0; i < playLen; ++i) {
							
							//If ereaser, make same color as background
							var tempTool = strokes.elementAt(i);
							
							if (tempTool instanceof FreePoint || tempTool instanceof FreeLine)
								if(tempTool.getErease() == true)
									tempTool.setDrawHue(baseLayer.getBackground());
									
							tempTool.draw(g);
						}
					}
				}
		}
	
	//MOUSE MOVEMENTS
	public class mouseMotion implements MouseMotionListener {
		@Override
		public void mouseDragged(MouseEvent e) {
			if (e.getSource() == baseLayer) {
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
					case erease:
						strokes.add(new FreeLine(temPnt, baseLayer.getBackground(), size));
						strokes.elementAt(strokes.size()-1).setErease(true);
						break;
					}
					nuLine = false;
				}
				else {
					switch(curState) {
					case free:
					case erease: 
						strokes.elementAt(strokes.size()-1).addPnt(temPnt);
						break;
						default:
							strokes.elementAt(strokes.size()-1).setPnt(1, temPnt);
					}
				}
				repaint();
			}
		}

		@Override
		public void mouseMoved(MouseEvent e) {}}
	
	//MOUSE CLICKS
	public class mouse implements MouseListener {
		
		@Override
		public void mouseClicked(MouseEvent e) {
			
			if (e.getSource() instanceof layer) {
				var temPnt = new Point(e.getX(), e.getY());
				
				switch(curState) {
				case free: 
					strokes.add(new FreePoint(temPnt, curColor, size));
					break;
				case erease:
					strokes.add(new FreePoint(temPnt, baseLayer.getBackground(), size));
					strokes.elementAt(strokes.size()-1).setErease(true);
				case bucket:
					baseLayer.setBackground(curColor);
					//strokes.add(new drawBucket(temPnt, curColor, getImage(contentPane)));
				default:
					break;
				}
				repaint();
			}
			
			if (e.getSource() == sizeField)
				sizeField.setFocusable(true);
		}

		@Override
		public void mousePressed(MouseEvent e) {
			
			if (SwingUtilities.isLeftMouseButton(e))
				Lclick = false;
			else 
				Lclick = true;
			
			//pickColor
			if (curState == state.pick) {
				img = getImage(baseLayer);
				if (SwingUtilities.isLeftMouseButton(e))
					col1 = new Color(img.getRGB(e.getX(), e.getY()));
				
				if (SwingUtilities.isRightMouseButton(e))
					col2 = new Color(img.getRGB(e.getX(), e.getY()));;
				curState = tempState;
			}
			
			//Make right click is possible
			if (e.getSource() instanceof JButton)
				((JButton)e.getSource()).doClick();
			
			//Change color
			if (!Lclick)
				curColor = col1;
			else
				curColor = col2;
			
			
			col1btn.setBackground(col1);
			col2btn.setBackground(col2);
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
	
	//SHORTCUTS
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
			case KeyEvent.VK_Q: curState = state.free; System.out.println("Free"); break; //free (state 0 and 1 used here)
			case KeyEvent.VK_W: curState = state.oval; System.out.println("Oval"); break; //Oval
			case KeyEvent.VK_E: curState = state.rect; System.out.println("Rect"); break; //Rectangle
			case KeyEvent.VK_R: curState = state.tri; System.out.println("Tri"); break; //Triangles
			case KeyEvent.VK_A: curState = state.erease; System.out.println("Ereaser"); break; //Ereaser
			case KeyEvent.VK_DELETE: strokes.removeAllElements(); repaint(); break;
			
			case KeyEvent.VK_Z: strokes.remove(strokes.size()-1); repaint(); break;
			}
		}

		@Override
		public void keyReleased(KeyEvent e) {}

	}

	
	//BUTTONS
	public class action implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			
			//Text Field for size
			if (e.getSource() == sizeField) {
				size = Integer.parseInt(sizeField.getText());
				sizeField.setFocusable(false);
			}
			
			//Tools buttons
			for (int i = 0; i < btns.size(); ++i) {
				if (e.getSource() == btns.elementAt(i)) {
					switch(i) {
					case 0: curState = state.free; System.out.println("Free"); break; //free
					case 1: curState = state.erease; System.out.println("Erease"); break; //erease
					case 2: curState = state.bucket; System.out.println("Bucket"); break; //bucket
					case 3: tempState = curState; curState = state.pick; System.out.println("Pick"); break; //pick
					case 4: curState = state.oval; System.out.println("Oval"); break; //oval
					case 5: curState = state.rect; System.out.println("Rectangle"); break; //rectangle
					case 6: curState = state.tri; System.out.println("Triangle"); break; //triangle
					
					case 7: //save
						try {
							img = getImage(baseLayer);
							File output = new File("img.png");
							ImageIO.write(img, "png", output);
							System.out.println("file created");
						} catch (IOException e1) {}
						break;
					}
				}
			}
			
			//Color buttons
			for (int i = 0; i < cols.length; ++i) {
				if (e.getSource() == palette.elementAt(i)) {
					if (!Lclick)
						col1 = cols[i];
					else
						col2 = cols[i];
				}
			}
			
			//PLAYBACK
			if (e.getSource() == playBtn) {
				playback = 1;
				repaint();
			}
		}

	}

	//RESIZE UI
	public class Adapt extends ComponentAdapter {
		public void componentResized(ComponentEvent componentEvent) {
			//Resizing Layer when resizing panel
			baseLayer.setBounds(10, btnSize + 20, getContentPane().getWidth() - 20, getContentPane().getHeight() - (btnSize + 30));
		}
	}
	
	//MAIN --------------------------------------------------------------------
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

	public paintMain() throws IOException {
		setTitle("Wannabe paint but wait there is more...");
		setFont(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		contentPane.setBackground(Color.darkGray);
		setBounds(100, 100, 1000, 800);
		setContentPane(contentPane);
		
		groupBtn = new ButtonGroup();
		
		//drawing layers setup
		mouse ml = new mouse();
		mouseMotion mml = new mouseMotion();
		key kl = new key();
		action al = new action();
		Adapt cl = new Adapt();
		
		baseLayer = new layer();
		baseLayer.addMouseListener(ml);
		baseLayer.addMouseMotionListener(mml);
		baseLayer.addKeyListener(kl);
		contentPane.addComponentListener(cl);
		
		baseLayer.setBounds(10, btnSize + 20, getContentPane().getWidth() - btnSize, getContentPane().getHeight() - btnSize);
		contentPane.add(baseLayer);
		contentPane.setLayout(null);

		
		//setup buttons
		for (int i = 0; i < 8; ++i) {
			
			btns.add(new JButton());
			btns.elementAt(i).addActionListener(al);
			btns.elementAt(i).setBounds(10 + ((btnSize + 10)*i), 10, btnSize, btnSize);
			groupBtn.add(btns.elementAt(i));
			
			switch(i) {
			case 0: btns.elementAt(i).setIcon(new ImageIcon("icon/crayon.gif")); break;
			case 1: btns.elementAt(i).setIcon(new ImageIcon("icon/efface.gif")); break;
			case 2: btns.elementAt(i).setIcon(new ImageIcon("icon/remplissage.gif")); break;
			case 3: btns.elementAt(i).setIcon(new ImageIcon("icon/pipette.gif")); break;
			case 4: btns.elementAt(i).setIcon(new ImageIcon("icon/cercle.gif")); break;
			case 5: btns.elementAt(i).setIcon(new ImageIcon("icon/rectangle.gif")); break;
			case 6: btns.elementAt(i).setIcon(new ImageIcon("icon/triangle.gif")); break;
			case 7: btns.elementAt(i).setIcon(new ImageIcon("icon/save.gif")); break;
			}
			
			contentPane.add(btns.elementAt(i));
		}
		groupBtn.clearSelection();
		
		//Size selector
		sizeLabel.setForeground(Color.white);
		sizeLabel.setFont(new Font("", Font.PLAIN, 24));
		sizeLabel.setBounds((btns.size() + 3)*btnSize + 10, 10, 100, 25);
		contentPane.add(sizeLabel);
		
		sizeField = new JTextField(3);
		sizeField.setBounds((btns.size() + 5)*btnSize + 10, 10, 100, 25);
		sizeField.addActionListener(al);
		sizeField.addMouseListener(ml);
		contentPane.add(sizeField);
		
		//Color buttons
		for (int i = 0; i < cols.length; ++i) {
			
			palette.add(new JButton());
			palette.elementAt(i).addActionListener(al);
			palette.elementAt(i).addMouseListener(ml);
			palette.elementAt(i).setBounds((btns.size() + (9 + i))*btnSize + 10, 10, btnSize, btnSize);
			//bullshit just to make the UI scaleable
			//why dafuk do I bother
			
			palette.elementAt(i).setBackground(cols[i]);
			contentPane.add(palette.elementAt(i));
		}
		
		col1btn = new JButton();
		col2btn = new JButton();
		col1btn.setBounds(27*btnSize, 10, btnSize, btnSize);
		col2btn.setBounds(28*btnSize, 10, btnSize, btnSize);
		col1btn.setBackground(col1);
		col2btn.setBackground(col2);
		contentPane.add(col1btn);
		contentPane.add(col2btn);
		
		playBtn = new JButton();
		playBtn.setBounds(30*btnSize, 10, btnSize, btnSize);
		playBtn.addActionListener(al);
		contentPane.add(playBtn);
		
		//Set default focus
		for (var component : getContentPane().getComponents()) {
			component.setFocusable(false);
		}
		
		baseLayer.setFocusable(true);
	}
}