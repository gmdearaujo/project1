package edu.msu.scrabble.project1;

import java.io.Serializable;
import java.util.ArrayList;
import android.graphics.Canvas;
import android.graphics.Paint;

public class Drawing implements Serializable {
	
	// the paint for the freehand drawing - color and width
	private transient Paint linePaint = new Paint();
	
	PaintParameters params = new PaintParameters();
	
	// the location of a Drawing is a list of x and y coordinates
	// that a touch passes through while drawing.
	// to rotate the drawing, etc, each of these points must be changed
	// according to the rotation, etc, function
	private ArrayList<Point> points = new ArrayList<Point>();
	
	private class PaintParameters implements Serializable {
		int color;
		float width;
	}
	
	private class Point implements Serializable {
		public float x;
		public float y;
		
		Point(float a, float b) {
			x = a;
			y = b;
		}
	}
	
	// should scale and rotation variables be kept here?
	
	
	// to draw a Drawing each of the coordinates in points
	// must be connected, in order, by a line of
	// 'color' and 'lineWidth'
	public void DrawLine(Canvas canvas) {
		// connect the points with a line of the specified color
		// and width
		/*for (int i=1; i<this.points.size(); i++) {
			canvas.drawLine(this.points.get(i-1), this.points.get(i-1), 
					this.points.get(i), this.points.get(i), linePaint);
		}*/
		if (linePaint == null) {
			linePaint = new Paint();
			linePaint.setColor(params.color);
			linePaint.setStrokeWidth(params.width);
		}
		for (int i=1; i<this.points.size(); i++) {
			canvas.drawLine(this.points.get(i-1).x, this.points.get(i-1).y, 
					this.points.get(i).x, this.points.get(i).y, linePaint);
		}
	}
	
	public void RotateDrawing(final float ca, final float sa, final float x1, final float y1)
	{
		float xp, yp;
		for (int i=0; i<this.points.size(); i++) {
			xp = (this.points.get(i).x - x1) * ca - (this.points.get(i).y - y1) * sa + x1;
	        yp = (this.points.get(i).x - x1) * sa + (this.points.get(i).y - y1) * ca + y1;
			
	        this.points.set(i, new Point(xp, yp));
		}
	}
	
	public void ScaleDrawing(final float scaleFactor, final float centerX, final float centerY) {
		
		for (int i=0; i<this.points.size(); i++) {
			this.points.get(i).x *= scaleFactor;
			this.points.get(i).y *= scaleFactor;
		}
	}
	
	public void addPoint(float x, float y) {
		points.add(new Point(x, y));
	}

	public Paint getLinePaint() {
		return linePaint;
	}

	public void setLinePaint(Paint linePaint) {
		this.linePaint = linePaint;
		params.color = linePaint.getColor();
		params.width = linePaint.getStrokeWidth();
	}
	
}
