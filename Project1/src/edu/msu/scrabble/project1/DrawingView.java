package edu.msu.scrabble.project1;

import java.util.ArrayList;
import java.util.Iterator;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;

public class DrawingView extends View {
	
	private class Drawing {
		// the paint for the freehand drawing - color and width
		private Paint linePaint;
		
		// the location of a Drawing is a list of x and y coordinates
		// that a touch passes through while drawing.
		// to rotate the drawing, etc, each of these points must be changed
		// according to the rotation, etc, function
		public ArrayList<Float> xLocations = new ArrayList<Float>();
		public ArrayList<Float> yLocations = new ArrayList<Float>();
		
		// should scale and rotation variables be kept here?
		
		
		// to draw a Drawing each of the coordinates in xLocations
		// and yLocations must be connected in order by a line of
		// 'color' and 'lineWidth'
		public void DrawLine(Canvas canvas) {
			// connect the points with a line of the specified color
			// and width
			for (int i=1; i<this.xLocations.size(); i++) {
				canvas.drawLine(this.xLocations.get(i-1), this.yLocations.get(i-1), 
						this.xLocations.get(i), this.yLocations.get(i), linePaint);
			}
		}
	}
	
	// this list contains all the Drawings that should be shown in the view
	private ArrayList<Drawing> drawings = new ArrayList<Drawing>();
	
	private Drawing currentDrawing = null;
	
	/**
     * Local class to handle the touch status for one touch.
     * We will have one object of this type for each of the 
     * two possible touches.
     */
    private class Touch {
        /**
         * Touch id
         */
        public int id = -1;
        
        /**
         * Current x location
         */
        public float x = 0;
        
        /**
         * Current y location
         */
        public float y = 0;
        
        /**
         * Previous x location
         */
        public float lastX = 0;
        
        /**
         * Previous y location
         */
        public float lastY = 0;
        
        /**
         * Change in x value from previous
         */
        public float dX = 0;
        
        /**
         * Change in y value from previous
         */
        public float dY = 0;
        
        /**
         * Copy the current values to the previous values
         */
        public void copyToLast() {
            lastX = x;
            lastY = y;
        }
        
        /**
         * Compute the values of dX and dY
         */
        public void computeDeltas() {
            dX = x - lastX;
            dY = y - lastY;
        }
    }
	
    /**
     * First touch status
     */
    private Touch touch1 = new Touch();
    
    /**
     * Second touch status
     */
    private Touch touch2 = new Touch();
    
    /**
     * Paint to set when different color/line width is selected
     */
    private Paint currentPaint = new Paint();
	
	
	
	
	public DrawingView(Context context) {
		super(context);
		init(context);
	}

	public DrawingView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public DrawingView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}
	
	/**
     * Initialize the view
     * @param context
     */
    private void init(Context context) {
        currentPaint.setColor(Color.BLACK);
    }

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		for (Drawing drawing : drawings)
			drawing.DrawLine(canvas);
		
		if (currentDrawing != null) 
			currentDrawing.DrawLine(canvas);
		
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int id = event.getPointerId(event.getActionIndex());
        
        switch(event.getActionMasked()) {
        case MotionEvent.ACTION_DOWN:
        	touch1.id = id;
            touch2.id = -1;
            getPositions(event);
            touch1.copyToLast();
            // start new drawing, add to list of drawings
            currentDrawing = new Drawing();
            currentDrawing.xLocations.add(touch1.x);
            currentDrawing.yLocations.add(touch1.y);
            // set color and line width
            currentDrawing.linePaint = currentPaint;
            return true;
            
        case MotionEvent.ACTION_POINTER_DOWN:
        	if(touch1.id >= 0 && touch2.id < 0) {
                touch2.id = id;
                getPositions(event);
                touch2.copyToLast();
                return true;
            }
            break;
            
        case MotionEvent.ACTION_UP:
        case MotionEvent.ACTION_CANCEL:
        	touch1.id = -1;
            touch2.id = -1;
            drawings.add(currentDrawing);
            currentDrawing = null;
            invalidate();
            return true;
            
        case MotionEvent.ACTION_POINTER_UP:
        	if(id == touch2.id) {
                touch2.id = -1;
            } else if(id == touch1.id) {
                // Make what was touch2 now be touch1 by 
                // swapping the objects.
                Touch t = touch1;
                touch1 = touch2;
                touch2 = t;
                touch2.id = -1;
            }
            invalidate();
            return true;
            
        case MotionEvent.ACTION_MOVE:
        	getPositions(event);
        	currentDrawing.xLocations.add(touch1.x);
            currentDrawing.yLocations.add(touch1.y);
            return true;
        }
        
        return super.onTouchEvent(event);
	}
	
	/**
     * Get the positions for the two touches and put them
     * into the appropriate touch objects.
     * @param event the motion event
     */
    private void getPositions(MotionEvent event) {
        for(int i=0;  i<event.getPointerCount();  i++) {
            
            // Get the pointer id
            int id = event.getPointerId(i);
            
            float x = event.getX(i); 
            float y = event.getY(i);
            
            if(id == touch1.id) {
            	touch1.copyToLast();
                touch1.x = x;
                touch1.y = y;
            } else if(id == touch2.id) {
            	touch2.copyToLast();
            	touch2.x = x;
                touch2.y = y;
            }
        }
        
        invalidate();
    }

	public Paint getCurrentPaint() {
		return currentPaint;
	}

	public void setCurrentPaint(Paint currentPaint) {
		this.currentPaint = currentPaint;
	}
    
}
