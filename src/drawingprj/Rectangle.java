package drawingprj;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Point;
import java.nio.channels.SelectionKey;

public class Rectangle extends Shape {
	protected int x;
	protected int y;
	protected int width;
	protected int height;

	public Rectangle(Point pointMouseDown, Point mr) {
		// TODO
		this.width = Math.abs(mr.x - pointMouseDown.x);
		this.height = Math.abs(mr.y - pointMouseDown.y);
		//----Changing X-axis if pushed coordinate bigger
		if (pointMouseDown.x>mr.x)
		{   this.x=mr.x;
			mr.x=pointMouseDown.x;
		} else 
			this.x = pointMouseDown.x;
		//----Changing Y-axis if pushed coordinate bigger
		if (pointMouseDown.y>mr.y)
		{   this.y=mr.y;
			mr.y=pointMouseDown.y;
		} else 
			this.y = pointMouseDown.y;
	}

	public Rectangle(int x, int y, int w, int h) {
		this.x = x;
		this.y = y;
		this.height = h;
		this.width = w;
	}

	@Override
	public boolean containsPoint(Point p) {
		if((p.x>x && p.x<x+width) && (p.y>y && p.y<y+height)) return true;
		return false;
	}

	@Override
	public void draw(Graphics g) {
		// TODO
		g.setColor(Color.BLACK);
		g.drawRect(x-1, y-1, width+1, height+1);
		
		if (isFilled()) {
			g.setColor(getColor());
			g.fillRect(x, y, width, height);

		}		
		if (isSelected())
		{		
		int z1=6;
		int z=z1/2;
		//Small black squares in the corner
			small_square square=new small_square();
			g.setColor(Color.BLACK);
			g.drawRect(x, y, width+1, height+1);
			square.draw1(g, x-z, y-z, z1, z1);			
			square.draw1(g, x+(width/2)-z, y-z, z1, z1);
			square.draw1(g, x+width-z, y-z, z1, z1);			
			square.draw1(g, x-z, y+(height/2)-z, z1, z1);	
			square.draw1(g, x+width-z1/2, y+(height/2)-z, z1, z1);						
			square.draw1(g, x-z, y+height-z, z1, z1);			
			square.draw1(g, x+(width/2)-z, y+height-z, z1, z1);			
			square.draw1(g, x+width-z, y+height-z, z1, z1);			
		}
		
	}

	@Override
	public int getArea() {
		return width * height;
	}

	@Override
	public Cursor getResizingCursor(Point p) {
		if ((p.x > x - 5 && p.x < x + 5 && p.y > y - 5 && p.y < y + 5)) {
			return Cursor.getPredefinedCursor(Cursor.NW_RESIZE_CURSOR);
		} else if ((p.x > x + width - 5 && p.x < x + width + 5 && p.y > y - 5 && p.y < y + 5)) {
			return Cursor.getPredefinedCursor(Cursor.NE_RESIZE_CURSOR);
		} else if ((p.x > x - 5 && p.x < x + 5 && p.y > y + height - 5 && p.y < y
				+ height + 5)) {
			return Cursor.getPredefinedCursor(Cursor.SW_RESIZE_CURSOR);
		} else if ((p.x > x + width - 5 && p.x < x + width + 5
				&& p.y > y + height - 5 && p.y < y + height + 5)) {
			return Cursor.getPredefinedCursor(Cursor.SE_RESIZE_CURSOR);
		} else if ((p.x > x + width - 5 && p.x < x + width + 5 && p.y > y && p.y < y
				+ height)) {
			return Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR);
		} else if ((p.x > x - 5 && p.x < x + 5 && p.y > y && p.y < y + height)) {
			return Cursor.getPredefinedCursor(Cursor.W_RESIZE_CURSOR);
		} else if ((p.y > y + height - 5 && p.y < y + height + 5 && p.x > x && p.x < x
				+ width)) {
			return Cursor.getPredefinedCursor(Cursor.S_RESIZE_CURSOR);
		} else if ((p.y > y - 5 && p.y < y + 5 && p.x > x && p.x < x + width)) {
			return Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR);
		} else if (containsPoint(p)) {
			return Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR);
		}

		return null;
	}

	@Override
	public void move(int px, int direction) {
		switch (direction) {
		case NORTH:
			this.y -= px;
			break;
		case SOUTH:
			this.y += px;
			break;
		case WEST:
			this.x -= px;
			break;
		case EAST:
			this.x += px;
			break;
		default:
			break;
		}
	}

	@Override
	public void move(Point ref, Point target) {
		int xd = target.x - ref.x;
		int yd = target.y - ref.y;
		this.x += xd;
		this.y += yd;

	}

	@Override
	public void resize(int px, int direction) {
		// TODO
	}

	@Override
	public void resize(Point ref, Point target, int direction) {
		int xd = target.x - ref.x;
		int yd = target.y - ref.y;

		switch (direction) {
		case SOUTH_EAST:
			this.width += xd;
			this.height += yd;
			break;
		case SOUTH_WEST:
			this.x += xd;
			this.width -= xd;
			this.height += yd;
			break;
		case NORTH_EAST:
			this.width += xd;
			this.height -= yd;
			this.y += yd;
			break;
		case NORTH_WEST:
			this.x += xd;
			this.y += yd;
			this.width -= xd;
			this.height -= yd;
			break;
		case SOUTH:
			this.height += yd;
			break;
		case NORTH:
			this.y += yd;
			this.height -= yd;
			break;
		case WEST:
			this.x += xd;
			this.width -= xd;
			break;
		case EAST:
			this.width += xd;
			break;
		default:
			break;
		}
	}

	@Override
	public void paste(Point p) {
		this.x = p.x;
		this.y = p.y;
	}

	public String getText() {
		return null;
	}

	@Override
    public void updatePos( Point mousePosition)
    {
	   x=mousePosition.x;
	   y=mousePosition.y;
	    
    }

	@Override
    public Shape copy()
    {
	    Rectangle rect=new Rectangle( x,y, width, height);
	    rect.setColor( getColor() );
	    rect.setFilled( isFilled() );
	    return rect;
    }
	private int c=0,c1=0;
	@Override
    public void scroll(int width1, int height1)
    {
			if (x+width<width1 && c==0) x++;
			else { x--;
				 if (x<1) c=0;
				 else c=1;
				 }
			if (y+height<height1 && c1==0) y++;
			else {y--;
				if (y<1) c1=0;
				else c1=1;
				}
    }
}
