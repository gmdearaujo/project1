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
		private Paint linePaint = new Paint();
		
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
		
		public void RotateDrawing(float ca, float sa, float x1, float y1)
		{
			float xp, yp;
			for (int i=0; i<this.xLocations.size(); i++) {
				xp = (this.xLocations.get(i) - x1) * ca - (this.yLocations.get(i) - y1) * sa + x1;
		        yp = (this.xLocations.get(i) - x1) * sa + (this.yLocations.get(i) - y1) * ca + y1;
				
		        this.xLocations.set(i, this.xLocations.get(i) + xp);
		        this.yLocations.set(i, this.yLocations.get(i) + yp);
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
    private Paint currentPaint;
	
    float angle = 0;
	
	
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
    	initializePaint(Color.BLACK, (float)4);
    }
    
    /**
     * Create new paint
     */
    private void initializePaint(int color, float width) {
    	currentPaint = new Paint();
        currentPaint.setColor(color);
        currentPaint.setStrokeWidth(width);
    }

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		for (Drawing drawing : drawings)
			drawing.DrawLine(canvas);
		
		if (currentDrawing != null) 
			currentDrawing.DrawLine(canvas);
		
		//canvas.rotate(angle);
		
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
            // set color and line width
            currentDrawing.linePaint = currentPaint;
            currentDrawing.xLocations.add(touch1.x);
            currentDrawing.yLocations.add(touch1.y);
            return true;
            
        case MotionEvent.ACTION_POINTER_DOWN:
        	if(touch1.id >= 0 && touch2.id < 0) {
                touch2.id = id;
                getPositions(event);
                touch2.copyToLast();
                // finish current drawing, now rotating/scaling not drawing
                if (currentDrawing != null)
                {
    	            drawings.add(currentDrawing);
    	            currentDrawing = null;
    	            invalidate();
                }
                return true;
            }
            break;
            
        case MotionEvent.ACTION_UP:
        case MotionEvent.ACTION_CANCEL:
        	touch1.id = -1;
            touch2.id = -1;
            // finish current drawing
            if (currentDrawing != null)
            {
	            drawings.add(currentDrawing);
	            currentDrawing = null;
	            invalidate();
            }
	        return true;
            
        case MotionEvent.ACTION_POINTER_UP:
        	if(id == touch2.id) {
                touch2.id = -1;
            }
        	else if(id == touch1.id) {
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
        	if (touch2.id < 0 && currentDrawing != null) {
        		currentDrawing.xLocations.add(touch1.x);
        		currentDrawing.yLocations.add(touch1.y);
        	}
        	move();
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
    
    /**
     * Handle movement of the touches
     */
    private void move() {
    	// If no touch1, we have nothing to do
        // This should not happen, but it never hurts
        // to check.
        if(touch1.id < 0) { 
            return;
        }

        // when one finger is down we want to draw, not move.
    	// so, do not do anything unless two fingers are down
        if(touch1.id >= 0 && touch2.id >= 0) {
            // Two touches
            
            /*
             * Rotation
             */
            float angle1 = angle(touch1.lastX, touch1.lastY, touch2.lastX, touch2.lastY);
            float angle2 = angle(touch1.x, touch1.y, touch2.x, touch2.y);
            float da = angle2 - angle1;
            rotate(da, touch1.x, touch1.y);
            
            /*
             * Scaling
             */
            //float distLast = (float) Math.sqrt(Math.pow((touch2.lastX - touch1.lastX), 2) + Math.pow((touch2.lastY - touch1.lastY), 2));
            //float distNow = (float) Math.sqrt(Math.pow((touch2.x - touch1.x), 2) + Math.pow((touch2.y - touch1.y), 2));
            //float scaleFactor = distNow / distLast;
            //params.hatScale = scaleFactor * params.hatScale;
        }
    }
    
    /**
     * Rotate the image around the point x1, y1
     * @param dAngle Angle to rotate in degrees
     * @param x1 rotation point x
     * @param y1 rotation point y
     */
    public void rotate(float dAngle, float x1, float y1) {
        //params.hatAngle += dAngle;
        this.angle += dAngle;
    	
        
        // Compute the radians angle
        double rAngle = Math.toRadians(dAngle);
        float ca = (float) Math.cos(rAngle);
        float sa = (float) Math.sin(rAngle);

        // do the rotation operations to each point in each Drawing in Drawings
        for (Drawing drawing : drawings)
			drawing.RotateDrawing(ca,sa,x1,y1);
        
        /**
        float xp = (params.hatX - x1) * ca - (params.hatY - y1) * sa + x1;
        float yp = (params.hatX - x1) * sa + (params.hatY - y1) * ca + y1;

        params.hatX = xp;
        params.hatY = yp;
        **/
    }
    
    /**
     * Determine the angle for two touches
     * @param x1 Touch 1 x
     * @param y1 Touch 1 y
     * @param x2 Touch 2 x
     * @param y2 Touch 2 y
     * @return computed angle in degrees
     */
    private float angle(float x1, float y1, float x2, float y2) {
        float dx = x2 - x1;
        float dy = y2 - y1;
        return (float) Math.toDegrees(Math.atan2(dy, dx));
    }

	public int getCurrentPaintColor() {
		return currentPaint.getColor();
	}

	public void setCurrentPaintColor(int color) {
		initializePaint(color, currentPaint.getStrokeWidth());
	}
    
	public float getCurrentPaintWidth() {
		return currentPaint.getStrokeWidth();
	}

	public void setCurrentPaintWidth(float width) {
		initializePaint(currentPaint.getColor(), width);
	}
	
}
