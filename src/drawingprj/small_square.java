package drawingprj;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Point;

public class small_square{
	void draw1(Graphics g, int x, int y, int x1, int y1) {
		g.setColor(Color.BLACK);
		g.drawRect(x, y, x1, y1);
		g.fillRect(x, y, x1, y1);
	}
}
