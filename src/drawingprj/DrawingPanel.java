package drawingprj;

import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Ellipse2D;
import java.util.Random;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.KeyStroke;

public class DrawingPanel extends JPanel implements KeyListener, MouseListener,
		MouseMotionListener {

	public static final int DM_RECT = 0;
	public static final int DM_OVAL = 1;
	public static final int DM_TEXT = 2;

	public static final int Drag_Draw = 0;
	public static final int Drag_Move = 1;
	public static final int Drag_Resize = 2;

	private static int moveStep = 10;
	private static int ignorableShapeArea = 20;

	private Vector<Shape> shapes = new Vector<Shape>();
	private Vector<Shape> selectedShapes = new Vector<Shape>();

	private Color drawingColor = Color.RED;
	private static Color[] colors = { Color.MAGENTA, Color.BLUE, Color.CYAN,
			Color.LIGHT_GRAY, Color.GREEN, Color.PINK, Color.ORANGE,
			Color.YELLOW };

	private boolean randomColor;
	private boolean selectingMode;
	private int drawingMode;
	private int draggingMode;
	private int cutt=0;
	
	private Point pointMouseDown;
	private Shape selectedShape, buffer1=null, buffer=null;
	private Shape currentDrawingShape = null;

	// ======== popupMenu ========
	private JPopupMenu popupMenu = new JPopupMenu();
	private JMenuItem pmiCut = new JMenuItem();
	private JMenuItem pmiCopy = new JMenuItem();
	private JMenuItem pmiPaste = new JMenuItem();
	private JMenuItem pmiDelete = new JMenuItem();
	private JMenuItem pmiClearAll = new JMenuItem();
	private JMenuItem pmiUp = new JMenuItem();
	private JMenuItem pmiDown = new JMenuItem();
	private JMenu pmiChangeColor = new JMenu();
	private JRadioButtonMenuItem prdmiDCRed = new JRadioButtonMenuItem();
	private JRadioButtonMenuItem prdmiDCBlue = new JRadioButtonMenuItem();
	private JRadioButtonMenuItem prdmiDCYellow = new JRadioButtonMenuItem();
	private JRadioButtonMenuItem prdmiDCGreen = new JRadioButtonMenuItem();
	private JRadioButtonMenuItem prdmiDCCyan = new JRadioButtonMenuItem();
	private JRadioButtonMenuItem prdmiDCOrange = new JRadioButtonMenuItem();
	private JRadioButtonMenuItem prdmiDCMagenta = new JRadioButtonMenuItem();

	// ======== popupMenu ========
	private void initListeners() {
		popupMenu.setAutoscrolls(true);
		popupMenu.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		popupMenu.setFocusCycleRoot(true);
		popupMenu.setEnabled(true);
		// ---- pmiCut ----
		pmiCut.setText("Cut");
		pmiCut.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X,
				KeyEvent.CTRL_MASK));
		// pmiCut.setIcon(new ImageIcon(getClass().getResource(
		// "/drawingprj/images/cut.jpg")));
		pmiCut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pmiCutActionPerformed(e);
			}

			private void pmiCutActionPerformed(ActionEvent e) {
				cutSelectedShape();

			}
		});
		popupMenu.add(pmiCut);

		// ---- pmiCopy ----
		pmiCopy.setText("Copy");
		pmiCopy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C,
				KeyEvent.CTRL_MASK));
		pmiCopy.setIcon(new ImageIcon(getClass().getResource(
				"/drawingprj/images/copy.jpg")));
		pmiCopy.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pmiCopyActionPerformed(e);
			}

			private void pmiCopyActionPerformed(ActionEvent e) {
				copySelectedShape();
			}
		});
		popupMenu.add(pmiCopy);

		// ---- pmiPaste ----
		pmiPaste.setText("Paste");
		pmiPaste.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V,
				KeyEvent.CTRL_MASK));
		pmiPaste.setIcon(new ImageIcon(getClass().getResource(
				"/drawingprj/images/paste.jpg")));
		pmiPaste.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pmiPasteActionPerformed(e);
			}

			private void pmiPasteActionPerformed(ActionEvent e) {
				pasteSelectedShape();

			}
		});
		popupMenu.add(pmiPaste);
		popupMenu.addSeparator();

		// ---- pmiDelete ----
		pmiDelete.setText("Delete");
		pmiDelete.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0));
		pmiDelete.setIcon(new ImageIcon(getClass().getResource(
				"/drawingprj/images/delete3.jpg")));
		pmiDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pmiDeleteActionPerformed(e);
			}

			private void pmiDeleteActionPerformed(ActionEvent e) {
				deleteSelectedShape();

			}
		});
		popupMenu.add(pmiDelete);

		// ---- pmiClearAll ----
		pmiClearAll.setText("Clear All");
		pmiClearAll.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,
				KeyEvent.CTRL_MASK | KeyEvent.SHIFT_MASK));
		pmiClearAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pmiClearAllActionPerformed(e);
			}

			private void pmiClearAllActionPerformed(ActionEvent e) {
				clearAllShapes();

			}
		});
		popupMenu.add(pmiClearAll);
		popupMenu.addSeparator();

		// ---- pmiUp ----
		pmiUp.setText("Up");
		pmiUp.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, 0));
		pmiUp.setIcon(new ImageIcon(getClass().getResource(
				"/drawingprj/images/upg.gif")));
		pmiUp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pmiUpActionPerformed(e);
			}
			
			private void pmiUpActionPerformed(ActionEvent e) {
				moveSelectedShape( Shape.NORTH);

			}
		});
		popupMenu.add(pmiUp);

		// ---- pmiDown ----
		pmiDown.setText("Down");
		pmiDown.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0));
		pmiDown.setIcon(new ImageIcon(getClass().getResource("/drawingprj/images/downg.gif")));
		pmiDown.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pmiDownActionPerformed(e);
			}

			private void pmiDownActionPerformed(ActionEvent e) {
				moveSelectedShape( Shape.SOUTH );

			}
		});
		popupMenu.add(pmiDown);
		popupMenu.addSeparator();

		// ======== pmiChangeColor ========
		{
			pmiChangeColor.setText("Change Color");

			// ---- prdmiDCRed ----
			prdmiDCRed.setText("Red");
			prdmiDCRed.setBackground(Color.RED);
			pmiChangeColor.add(prdmiDCRed);

			// ---- prdmiDCBlue ----
			prdmiDCBlue.setText("Blue");
			prdmiDCBlue.setBackground(new Color(51, 51, 255));
			pmiChangeColor.add(prdmiDCBlue);

			// ---- prdmiDCYellow ----
			prdmiDCYellow.setText("Yellow");
			prdmiDCYellow.setBackground(new Color(255, 255, 51));
			pmiChangeColor.add(prdmiDCYellow);

			// ---- prdmiDCGreen ----
			prdmiDCGreen.setText("Green");
			prdmiDCGreen.setBackground(new Color(0, 255, 51));
			pmiChangeColor.add(prdmiDCGreen);

			// ---- prdmiDCCyan ----
			prdmiDCCyan.setText("Cyan");
			prdmiDCCyan.setBackground(Color.cyan);
			pmiChangeColor.add(prdmiDCCyan);

			// ---- prdmiDCOrange ----
			prdmiDCOrange.setText("Orange");
			prdmiDCOrange.setBackground(new Color(255, 204, 0));
			pmiChangeColor.add(prdmiDCOrange);

			// ---- prdmiDCMagenta ----
			prdmiDCMagenta.setText("Magenta");
			prdmiDCMagenta.setBackground(new Color(102, 0, 102));
			pmiChangeColor.add(prdmiDCMagenta);
		}
		popupMenu.add(pmiChangeColor);
	}

	private class ChangeColorRdButtonsActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			JRadioButtonMenuItem button = (JRadioButtonMenuItem) e.getSource();
			selectedShape.setColor(button.getBackground());
			repaint();
		}
	}

	public DrawingPanel() {
		initListeners();
		// ---- buttonGroupPMiDC ----
		ButtonGroup buttonGroupPMiDC = new ButtonGroup();
		buttonGroupPMiDC.add(prdmiDCRed);
		buttonGroupPMiDC.add(prdmiDCBlue);
		buttonGroupPMiDC.add(prdmiDCYellow);
		buttonGroupPMiDC.add(prdmiDCGreen);
		buttonGroupPMiDC.add(prdmiDCCyan);
		buttonGroupPMiDC.add(prdmiDCOrange);
		buttonGroupPMiDC.add(prdmiDCMagenta);

		ChangeColorRdButtonsActionListener dcrdbtal = new ChangeColorRdButtonsActionListener();

		prdmiDCCyan.addActionListener(dcrdbtal);
		prdmiDCMagenta.addActionListener(dcrdbtal);
		prdmiDCBlue.addActionListener(dcrdbtal);
		prdmiDCGreen.addActionListener(dcrdbtal);
		prdmiDCOrange.addActionListener(dcrdbtal);
		prdmiDCRed.addActionListener(dcrdbtal);
		prdmiDCYellow.addActionListener(dcrdbtal);

		add(popupMenu);
		addMouseListener(this);
		addMouseMotionListener(this);
		addKeyListener(this);

		setPreferredSize(new Dimension(700, 400));
		startChangColor();
		startMation();
	}

	private void startMation()
    {
		Thread startMotion=new Thread()
		   {
			   @Override
		   public void run()
		   {
			   for ( int i = 0; i < shapes.size(); i++ ) {
				   //From here we can change direction of shapes
				   shapes.get( i ).scroll(getWidth(), getHeight());
				   repaint();
				   //TODO
			   }
			   try {
		        sleep(10);  //From here we can give SPEED of shapes
	        } catch ( InterruptedException e ) {
		        e.printStackTrace();
	        }
			   run();
		    }
		   };
		   startMotion.start();
	    
    }

	private void startChangColor()
    {
	   Thread changeColorStart=new Thread()
	   {
		   @Override
	   public void run()
	   {
		   for ( int i = 0; i < shapes.size(); i++ ) {
			Random r=new Random();
			shapes.get( i ).setColor( new Color(r.nextInt(255),r.nextInt(255),r.nextInt(255) ));
			repaint();
        }
		   try {
	        sleep(150);
        } catch ( InterruptedException e ) {
	        e.printStackTrace();
        }
		   run();
	    }
	   };
	   changeColorStart.start();
    }

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		for (Shape shape : shapes) {
			shape.draw(g);
		}
	}

	/**
	 * 
	 * @param p
	 * @return The top most shape in shapes vector
	 */
	private Shape findTopMostShape(Point p) {
		
		for (int i=shapes.size()-1; i>=0; i--)
		{if (shapes.get(i).containsPoint(p))
			{return shapes.get(i);}			
		}
		
		// TODO
		return null;
	}

	@Override
	public void keyPressed(KeyEvent ke) {
		if (ke.getKeyCode() == KeyEvent.VK_CONTROL) {
			
			setSelectingMode(true);
		}
	}

	@Override
	public void keyReleased(KeyEvent ke) {
		if (ke.getKeyCode() == KeyEvent.VK_CONTROL) {
			setSelectingMode(false);
		}
	}

	@Override
	public void keyTyped(KeyEvent ke) {

	}

	@Override
	public void mouseClicked(MouseEvent me) {
		if (me.isMetaDown())
		{
			popupMenu.show( this, me.getPoint().x, me.getPoint().y );
		}
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		requestFocus();
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
	}

	@Override
	public void mousePressed(MouseEvent me) {
		// TODO
		
		if (selectingMode) {
				selectedShapes.add( selectedShape );
			}
		
		else if(selectedShape!=null) {
			selectedShape.setSelected(false);
			for (int i=0; i<shapes.size(); i++)
				shapes.get( i).setSelected( false );
			selectedShapes.clear();
			selectedShapes.add( selectedShape );
		}
		
		pointMouseDown = me.getPoint();
		Shape shape=findTopMostShape(pointMouseDown);
				
		if (shape!=null) 
		{	shape.setSelected(true);
			selectedShape=shape;	}
		else selectedShape=null;
		
		if (isRandomColor()) selectRandomColor();
		repaint();
	}

	@Override
	public void mouseReleased(MouseEvent me) {
		// TODO
		if (selectedShape==null)
		{
		if (currentDrawingShape != null) {
			currentDrawingShape.setFilled(true);
			currentDrawingShape = null;
			repaint();
		}
		}
	}

	@Override
	public void mouseDragged(MouseEvent me) {
		// TODO
				
		Point md = me.getPoint();
		switch ( draggingMode )
	        {
			case Drag_Draw :
					switch ( drawingMode )
	                    {
						case DM_RECT:
							shapes.remove(currentDrawingShape);
							Rectangle rectangle = new Rectangle(pointMouseDown, md);
							rectangle.setColor(drawingColor);
							if (rectangle.getArea()>ignorableShapeArea) { shapes.add(rectangle);
							currentDrawingShape = rectangle;	}
							break;
						
						case DM_OVAL:
							shapes.remove(currentDrawingShape);
							Ovala oval= new Ovala(pointMouseDown, md);
							oval.setColor(drawingColor);
							if (oval.getArea()>ignorableShapeArea) { shapes.add(oval);
							currentDrawingShape = oval;	}
							break;
//						
						case DM_TEXT:
							
							break;
						
						default :
							break;
						}
				break;

			case Drag_Move:
				if (selectedShapes!=null)
				{
					for ( int i = 0; i < selectedShapes.size(); i++ ) {
		                selectedShapes.get( i ).move( pointMouseDown, md );
	                }
				pointMouseDown=md;
				}
				
				else if (selectedShape!=null)
				{
				selectedShape.move( pointMouseDown, md );
				pointMouseDown=md;
				}
				
				
				break;
			
			case Drag_Resize:
				if (selectedShape!=null)
				{
				selectedShape.resize( pointMouseDown, md, getCursor().getType());
				pointMouseDown=md;
				}
				break;

			default :
				break;
			}
		repaint();
	}

	@Override
	public void mouseMoved(MouseEvent me) {
		Shape topmost=findTopMostShape( me.getPoint() );
		if (topmost!=null)
		{
			Cursor cursor = topmost.getResizingCursor( me.getPoint() );
			if (cursor!=null) setCursor( cursor );
			else setCursor( Cursor.getDefaultCursor() );
		}
		else setCursor( Cursor.getDefaultCursor());
		
		if (getCursor().equals( Cursor.getDefaultCursor() )){
			draggingMode=Drag_Draw;
		}else if ( getCursor().equals( Cursor.getPredefinedCursor( Cursor.MOVE_CURSOR ) ) ) {
	        draggingMode=Drag_Move;
		}else draggingMode=Drag_Resize;
	}

	public void deleteSelectedShape() {
		if (selectedShape!=null)
		{
			shapes.remove(selectedShape);
			repaint();
		}
		// TODO
	}

	public void moveSelectedShape(int direction) {
		// TODO
		 if(selectedShape!=null) {
		 selectedShape.move(moveStep, direction);
		 repaint();
		 }
	}

	public void cutSelectedShape() {
		if (selectedShape!=null)   //If there is selected shape then we can CUT
		{
			buffer=selectedShape;   //Selected shape is our shape, which we selected and Buffer is temporary Shape variable
			buffer1=selectedShape.copy(); //This is for making many copy of CUTTED shape
			shapes.remove( selectedShape ); //we deleted our selected shape from our list of shapes
			repaint(); //we are using this, for making window repaint;
			//THat's OK....
		}
	}

	public void copySelectedShape() {
		 if(selectedShape!=null) {
			 buffer=selectedShape.copy();
			 buffer1=selectedShape.copy();
		 }
	}

	public void copyAgain(Shape selectedBefore)
	{
			buffer=selectedBefore.copy();
	}
	public void pasteSelectedShape() {
				if (buffer==null) copyAgain( buffer1 );
				if (buffer!=null)
				{
				buffer.updatePos(pointMouseDown);
				shapes.add( buffer );
				repaint();
				buffer=null;
				}
		}

	public void increaseIndexOfSelectedShape() {
		if (selectedShape!=null && shapes.indexOf(selectedShape)<shapes.size()-1)
		{
			int index=shapes.indexOf(selectedShape);
			shapes.remove(selectedShape);
			shapes.add(++index, selectedShape);
			repaint();
		}
	}

	public void decreaseIndexOfSelectedShape() {
		if (selectedShape!=null && shapes.indexOf(selectedShape)>0)
		{
			int index=shapes.indexOf(selectedShape);
			if (index>=0)
			{
			shapes.remove(selectedShape);
			shapes.add(--index, selectedShape);
			repaint();
			}
		}
	}

	public void selectRandomColor() {
		Random r=new Random();
		int r1=r.nextInt(7);
		int R=r.nextInt(255);
		int G=r.nextInt(255);
		int B=r.nextInt(255);
		//Setting only COLORS from array
		//setDrawingColor( colors[r1]);

		//Setting RGB colors;
		setDrawingColor(new Color(R,G,B));
	}

	public void clearAllShapes() {
		shapes.clear();
		repaint();
	}

	public void setDrawingMode(int drawingMode) {
		this.drawingMode = drawingMode;
		if (drawingMode == DM_TEXT) {
			setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
		} else {
			setCursor(Cursor.getDefaultCursor());
		}
	}

	public Color getDrawingColor() {
		return drawingColor;
	}

	public void setDrawingColor(Color drawingColor) {
		this.drawingColor = drawingColor;
	}

	public int getDrawingMode() {
		return drawingMode;
	}

	public int getDraggingMode() {
		return draggingMode;
	}

	public void setDraggingMode(int draggingMode) {
		this.draggingMode = draggingMode;
	}

	public Vector<Shape> getShapes() {
		return shapes;
	}

	public void setShapes(Vector<Shape> shapes) {
		this.shapes = shapes;
		repaint();
	}

	public boolean isRandomColor() {
		return randomColor;
	}

	public void setRandomColor(boolean randomColor) {
		this.randomColor = randomColor;
	}

	public Shape getSelectedShape() {
		return selectedShape;
	}

	public void setSelectedShape(Shape selectedShape) {
		this.selectedShape = selectedShape;
	}

	public boolean isSelectingMode() {
		return selectingMode;
	}

	public void setSelectingMode(boolean selectingMode) {
		this.selectingMode = selectingMode;
	}

}
