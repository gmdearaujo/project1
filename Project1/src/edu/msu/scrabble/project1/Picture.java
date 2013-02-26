package edu.msu.scrabble.project1;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

public class Picture implements Serializable {

	/**
	 * Compiler generated serialID
	 */
	private static final long serialVersionUID = -7698564561406540923L;
	
	// a list of the Drawings that make up the Picture
	private ArrayList<Drawing> drawings = new ArrayList<Drawing>();
	
	private float angle = 0;
    private float scale = 1;
    private float offsetX = 0;
    private float offsetY = 0;
	
	public Picture() {
		// TODO Auto-generated constructor stub
	}
	
	public void AddDrawing(Drawing drawing) {
		drawings.add(drawing);
	}
	
	public Drawing GetDrawing(int index) {
		return drawings.get(index);
	}

	public ArrayList<Drawing> getDrawings() {
		return drawings;
	}

	public void setDrawings(ArrayList<Drawing> drawings) {
		this.drawings = drawings;
	}

	private void writeObject(java.io.ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
    }

	private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
		//in.registerValidation(this, 0);
		in.defaultReadObject();
	}

	public void setOffsetX(float offsetX) {
		this.offsetX = offsetX;
	}

	public float getOffsetY() {
		return offsetY;
	}

	public void setOffsetY(float offsetY) {
		this.offsetY = offsetY;
	}

	public float getAngle() {
		return angle;
	}

	public void setAngle(float angle) {
		this.angle = angle;
	}

	public float getScale() {
		return scale;
	}

	public void setScale(float scale) {
		this.scale = scale;
	}

	public float getOffsetX() {
		return offsetX;
	}
	
}
