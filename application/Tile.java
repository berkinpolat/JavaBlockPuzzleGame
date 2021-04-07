package application;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

public class Tile extends Node{

	private static final int WIDHT = 150;
	private static final int HEIGHT = 150;
	
	private Image image;
	private int number;
	private Rectangle rectangle;
	private String type, property;


	
	
	public Tile() {
		this.rectangle = new Rectangle();
		this.rectangle.setHeight(HEIGHT);
		this.rectangle.setWidth(WIDHT);
	}
	public Tile(Image image, int number, Rectangle rectangle) {
		this.image = image;
		this.number = number;
		this.rectangle = rectangle;
		
	}

	
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getProperty() {
		return property;
	}
	public void setProperty(String property) {
		this.property = property;
	}
	public Rectangle getRectangle() {
		return rectangle;
	}

	public void setRectangle(Rectangle rectangle) {
		this.rectangle = rectangle;
	}


	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public static int getWidht() {
		return WIDHT;
	}

	public static int getHeight() {
		return HEIGHT;
	}
	
	
	public void setImageOfRectangle() {
		try {
			this.rectangle.setFill(new ImagePattern(this.getImage()));
		}catch (Exception e) {
			
		}
	}
	
	public void SayClassName() {
		System.out.println(this.getClass().getName());
	}
	
	public String SayClassNameString() {
		return this.getClass().getName();
	}
}