package edu.msu.scrabble.project1;

import java.util.ArrayList;

public class DrawingViewTemp {

	public class Line {
		/*
		 * X and Y locations of the first point
		 */
		private float x1;
		private float y1;
		
		/*
		 * X and Y locations of the second point
		 */
		private float x2;
		private float y2;
		
		/* 
		 * Line color
		 */
		private int color;
		
		/*
		 * Line width
		 */
		private float size;
	}
	
	/*
	 * An array of lines. Drawing will iterate through the list to draw each line in order
	 */
	ArrayList lineList;
	
	public DrawingViewTemp() {
		// TODO Auto-generated constructor stub
	}

}
