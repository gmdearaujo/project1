package edu.msu.scrabble.project1;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class DrawingView extends View {

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
	
    private float pictureAngle = 0;
    private float pictureScale = 1;
    private float offsetX = 0;
    private float offsetY = 0;
    
    private boolean isEditable = true;

	// this list contains all the Drawings that should be shown in the view
	//private ArrayList<Drawing> drawings = new ArrayList<Drawing>();
	private Picture picture = new Picture();
	private transient Drawing currentDrawing = null;
	
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
		
		canvas.translate(offsetX, offsetY);
		
		for (Drawing drawing : picture.getDrawings())
			drawing.DrawLine(canvas);
		if (currentDrawing != null) 
			currentDrawing.DrawLine(canvas);
		
		canvas.scale(pictureScale, pictureScale);
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
        	if (isEditable) {
	            // start new drawing, add to list of drawings
	            currentDrawing = new Drawing();
	            // set color and line width
	            currentDrawing.setLinePaint(currentPaint);
	            currentDrawing.addPoint(touch1.x, touch1.y);
        	} else {
        		offsetX += touch1.x - touch1.lastX;
        		offsetY += touch1.y - touch1.lastY;
        	}
        	return true;
            
        case MotionEvent.ACTION_POINTER_DOWN:
        	if(touch1.id >= 0 && touch2.id < 0) {
                touch2.id = id;
                getPositions(event);
                touch2.copyToLast();
                // finish current drawing, now rotating/scaling not drawing
                if (currentDrawing != null)
                {
                	picture.AddDrawing(currentDrawing);
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
            	picture.AddDrawing(currentDrawing);
            	//currentDrawing = null;
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
        		currentDrawing.addPoint(touch1.x, touch1.y);
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
        
    	if (touch1.id >= 0 && !isEditable) {
    		// Moving
    		touch1.computeDeltas();
    		
    		offsetX += touch1.dX;
    		offsetY += touch1.dY;
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
            float distLast = length(touch1.lastX, touch1.lastY, touch2.lastX, touch2.lastY);
            float distNow = length(touch1.x, touch1.y, touch2.x, touch2.y);
            
            scale(distNow / distLast, touch1.x, touch1.y);
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
        pictureAngle += dAngle;
    	
        // Compute the radians angle
        double rAngle = Math.toRadians(dAngle);
        float ca = (float) Math.cos(rAngle);
        float sa = (float) Math.sin(rAngle);

        // do the rotation operations to each point in each Drawing in Drawings
        for (Drawing drawing : picture.getDrawings())
			drawing.RotateDrawing(ca,sa,x1,y1);
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

    private float length(float x1, float y1, float x2, float y2) {        
    	float dx = x2 - x1;        
    	float dy = y2 - y1;        
    	return (float)Math.sqrt(dx * dx + dy * dy);
    }
    
    public void scale(float scale, float x1, float y1) {        
    	pictureScale *= scale;
    	
    	// do the rotation operations to each point in each Drawing in Drawings
        for (Drawing drawing : picture.getDrawings())
			drawing.ScaleDrawing(scale,x1,y1);	
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
	
	public boolean getEditable() {
		return isEditable;
	}
	
	public void setEditable(boolean b) {
		isEditable = b;
	}

	public void setCurrentPaintWidth(float width) {
		initializePaint(currentPaint.getColor(), width);
	}
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * Alex's failed attempts at serializing drawingList
	 */
	public void putDrawings(Intent intent) {
		intent.putExtra("PICTURE", picture);
	}
	
	public void getDrawings(Intent intent) {
		if (intent.getSerializableExtra("PICTURE") != null) {
			picture = (Picture)intent.getSerializableExtra("PICTURE");
		}
	}
}
