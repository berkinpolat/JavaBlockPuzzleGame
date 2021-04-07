package application;

import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;

public class Empty_None extends Empty{
	private double mouse_x, mouse_y;
	private double position_x , position_y;
	private double dragAmountX , dragAmountY;
	private double newPositionX , newPositionY;
	private static boolean canNoneMove = true;
	
	public static boolean isCanNoneMove() {
		return canNoneMove;
	}
	public static void setCanNoneMove(boolean canNoneMove) {
		Empty_None.canNoneMove = canNoneMove;
	}
	public double getMouse_x() {
		return mouse_x;
	}
	public void setMouse_x(double mouse_x) {
		this.mouse_x = mouse_x;
	}
	public double getMouse_y() {
		return mouse_y;
	}
	public void setMouse_y(double mouse_y) {
		this.mouse_y = mouse_y;
	}
	public double getPosition_x() {
		return position_x;
	}
	public void setPosition_x(double position_x) {
		this.position_x = position_x;
	}
	public double getPosition_y() {
		return position_y;
	}
	public void setPosition_y(double position_y) {
		this.position_y = position_y;
	}
	public double getDragAmountX() {
		return dragAmountX;
	}
	public void setDragAmountX(double dragAmountX) {
		this.dragAmountX = dragAmountX;
	}
	public double getDragAmountY() {
		return dragAmountY;
	}
	public void setDragAmountY(double dragAmountY) {
		this.dragAmountY = dragAmountY;
	}
	public double getNewPositionX() {
		return newPositionX;
	}
	public void setNewPositionX(double newPositionX) {
		this.newPositionX = newPositionX;
	}
	public double getNewPositionY() {
		return newPositionY;
	}
	public void setNewPositionY(double newPositionY) {
		this.newPositionY = newPositionY;
	}
	
	/* Instead of giving movement ability to every object in the game and limiting it, we preferred to give it to
	 * tiles which can move. These tiles are all of the pipes and empty-none tile.
	 * In the constructor of this class, we used Cursor to set mouse icon.
	 * OnPressed, OnMouseDragged and OnMouseReleased methods will help object to move between places.
	 */

	public Empty_None() {
		super();
	
		if(canNoneMove == true) {
			this.getRectangle().setCursor(Cursor.HAND);
			}
			else {
				this.getRectangle().setCursor(Cursor.DEFAULT);
			}
		
		//In this section, row and column of selected tile's will be taken in order to use later.
				//In order to avoid overlapping, selected tile will be removed and created again.
				//When pressed, X and Y positions of mouse will be taken.
				//Transition of tile is taken.
		this.getRectangle().setOnMousePressed(new EventHandler<MouseEvent>() {
			

			@Override
			public void handle(MouseEvent t) {
				
				if(canNoneMove == true) {
				int mevcut_row = Main.getGp().getRowIndex(((Rectangle) t.getSource()));
				int mevcut_column =  Main.getGp().getColumnIndex(((Rectangle) t.getSource()));
				
				
				Main.getGp().getChildren().remove(((Rectangle)t.getSource()));
				
				Main.getGp().add(((Rectangle) t.getSource()), mevcut_column, mevcut_row);	
				

				mouse_x = t.getSceneX();
	        	mouse_y = t.getSceneY();
	        	
	        	position_x = ((Rectangle)(t.getSource())).getTranslateX();
	        	position_y = ((Rectangle)(t.getSource())).getTranslateY();
	        	
				}
				
			}
		});
	
	
		//Drag amount will be determined by substitution.
		//New position will be determined by adding drag amount to positions.
		//New position will be the translate of the selected tile.
	this.getRectangle().setOnMouseDragged(new EventHandler<MouseEvent>() {

		@Override
		public void handle(MouseEvent t) {
			if(canNoneMove == true) {
			 dragAmountX = t.getSceneX() - mouse_x;
			 dragAmountY = t.getSceneY() - mouse_y;	
			
		

			 newPositionX = position_x + dragAmountX;
			 newPositionY = position_y + dragAmountY;
			
			((Rectangle)(t.getSource())).setTranslateX(newPositionX);
			((Rectangle)(t.getSource())).setTranslateY(newPositionY);
			}
			
		}
	});
	
	//In this section, selected tile may move or not depending on target tile and distance between them.
	this.getRectangle().setOnMouseReleased(new EventHandler<MouseEvent>() {

		@Override
		public void handle(MouseEvent t) {
			if(canNoneMove == true) {
			
			giveMove(t);
			//Status label will be updated by current move count and level.
			Main.getLblStatus().setText(" Level: " + Main.getLevel() + "\nMoves: "  + Main.getMoves());
			
			}
		}

		
	});
	
	}
	
	
	/* This method has 4 main parts. These parts help to move tile to adjacent place by using locations of mousePress and mouseRelease.
	 * When a movement is completed, tile_array locations will be switched between old and new locations.
	 */
	public void giveMove(MouseEvent t) {
		Tile temp_tile = new Tile();
		int mevcut_row = Main.getGp().getRowIndex(((Rectangle) t.getSource()));
		int mevcut_column =  Main.getGp().getColumnIndex(((Rectangle) t.getSource()));
		
		//If statement will check mouse movements to determine tile's movement direction.(up,down...)
		if(this.dragAmountY>-250 && -100>this.dragAmountY && this.dragAmountX<50 && this.dragAmountX>-50) {
			
			
			//For loop finds the type of the tile in the selected tile's movement direction.
			//Saves the found tile as temporary tile object. (temp_tile)
			for(Tile tile : Main.getTile_list()) {
					if(Main.getGp().getRowIndex(tile.getRectangle()) == mevcut_row-1 && Main.getGp().getColumnIndex(tile.getRectangle()) == mevcut_column ) {
						 temp_tile =  tile;
					}
				}
			
			
			//If statement checks whether temporary tile is empty-free or not.
			if(temp_tile.SayClassNameString().equals("application.Empty_Free")) {
				//In this part, temporary tile and selected tile's numbers are switched.
				temp_tile.setNumber(getNumber());
				this.setNumber(getNumber()-4);
				
				//Game board (gp) will be cleared and created again(sortArray() and adjustGridPane()).
				//Movement increases by 1.
				Main.getGp().getChildren().clear();
				Main.sortArray(Main.getTile_array(), Main.getTile_list());
				Main.adjustGridPane(Main.getGp(), Main.getTile_array());
				int a = Main.getMoves() + 1;
				Main.setMoves(a);
					
				//Variables of this method and selected tile's translations will be set to 0.
				((Rectangle) t.getSource()).setTranslateX(0);
				((Rectangle) t.getSource()).setTranslateY(0);
				this.setDragAmountX(((Rectangle)t.getSource()).getTranslateX());
				this.setDragAmountY(((Rectangle)t.getSource()).getTranslateY());
				this.setNewPositionX(((Rectangle)t.getSource()).getTranslateX());
				this.setNewPositionY(((Rectangle)t.getSource()).getTranslateY());
				this.setDragAmountX(((Rectangle)t.getSource()).getTranslateX());
				this.setDragAmountY(((Rectangle)t.getSource()).getTranslateY());
				
				
					
			}else {
				//Movement cannot be done.
				//Variables of this method and selected tile's translations will be set to 0.
				//Selected tile will return to its old position.
				((Rectangle) t.getSource()).setTranslateX(0);
				((Rectangle) t.getSource()).setTranslateY(0);
				this.setDragAmountX(((Rectangle)t.getSource()).getTranslateX());
				this.setDragAmountY(((Rectangle)t.getSource()).getTranslateY());
				this.setNewPositionX(((Rectangle)t.getSource()).getTranslateX());
				this.setNewPositionY(((Rectangle)t.getSource()).getTranslateY());
				this.setDragAmountX(((Rectangle)t.getSource()).getTranslateX());
				this.setDragAmountY(((Rectangle)t.getSource()).getTranslateY());
			}
				
		
		
			}
		else if(this.dragAmountY>100 && 250>this.dragAmountY && this.dragAmountX <50 && this.dragAmountX>-50) {
			for(Tile tile : Main.getTile_list()) {
				if(Main.getGp().getRowIndex(tile.getRectangle()) == mevcut_row+1 && Main.getGp().getColumnIndex(tile.getRectangle()) == mevcut_column ) {
					 temp_tile =  tile;
				}
			}	
			if(temp_tile.SayClassNameString().equals("application.Empty_Free")) {
				temp_tile.setNumber(getNumber());
				this.setNumber(getNumber()+4);
				
				//Game board (gp) will be cleared and created again(sortArray() and adjustGridPane()).
				//Movement increases by 1.
				Main.getGp().getChildren().clear();
				Main.sortArray(Main.getTile_array(), Main.getTile_list());
				Main.adjustGridPane(Main.getGp(), Main.getTile_array());
				int a = Main.getMoves() + 1;
				Main.setMoves(a);
					
				//Variables of this method and selected tile's translations will be set to 0.
				((Rectangle) t.getSource()).setTranslateX(0);
				((Rectangle) t.getSource()).setTranslateY(0);
				this.setDragAmountX(((Rectangle)t.getSource()).getTranslateX());
				this.setDragAmountY(((Rectangle)t.getSource()).getTranslateY());
				this.setNewPositionX(((Rectangle)t.getSource()).getTranslateX());
				this.setNewPositionY(((Rectangle)t.getSource()).getTranslateY());
				this.setDragAmountX(((Rectangle)t.getSource()).getTranslateX());
				this.setDragAmountY(((Rectangle)t.getSource()).getTranslateY());
				
				
					
			}else {
				//Movement cannot be done.
				//Variables of this method and selected tile's translations will be set to 0.
				//Selected tile will return to its old position.
				((Rectangle) t.getSource()).setTranslateX(0);
				((Rectangle) t.getSource()).setTranslateY(0);
				this.setDragAmountX(((Rectangle)t.getSource()).getTranslateX());
				this.setDragAmountY(((Rectangle)t.getSource()).getTranslateY());
				this.setNewPositionX(((Rectangle)t.getSource()).getTranslateX());
				this.setNewPositionY(((Rectangle)t.getSource()).getTranslateY());
				this.setDragAmountX(((Rectangle)t.getSource()).getTranslateX());
				this.setDragAmountY(((Rectangle)t.getSource()).getTranslateY());
			}
		}
		
		else if(this.dragAmountX > -250 && -100>this.dragAmountX && this.dragAmountY <50 && this.dragAmountY>-50) {
			for(Tile tile : Main.getTile_list()) {
				if(Main.getGp().getColumnIndex(tile.getRectangle()) == mevcut_column-1 && Main.getGp().getRowIndex(tile.getRectangle()) == mevcut_row ) {
					 temp_tile =  tile;
				}
			}
			

			if(temp_tile.SayClassNameString().equals("application.Empty_Free")) {
				temp_tile.setNumber(getNumber());
				this.setNumber(getNumber()-1);
				
				//Game board (gp) will be cleared and created again(sortArray() and adjustGridPane()).
				//Movement increases by 1.
				Main.getGp().getChildren().clear();
				Main.sortArray(Main.getTile_array(), Main.getTile_list());
				Main.adjustGridPane(Main.getGp(), Main.getTile_array());
				int a = Main.getMoves() + 1;
				Main.setMoves(a);
					
				//Variables of this method and selected tile's translations will be set to 0.
				((Rectangle) t.getSource()).setTranslateX(0);
				((Rectangle) t.getSource()).setTranslateY(0);
				this.setDragAmountX(((Rectangle)t.getSource()).getTranslateX());
				this.setDragAmountY(((Rectangle)t.getSource()).getTranslateY());
				this.setNewPositionX(((Rectangle)t.getSource()).getTranslateX());
				this.setNewPositionY(((Rectangle)t.getSource()).getTranslateY());
				this.setDragAmountX(((Rectangle)t.getSource()).getTranslateX());
				this.setDragAmountY(((Rectangle)t.getSource()).getTranslateY());
				
				
					
			}else {
				//Movement cannot be done.
				//Variables of this method and selected tile's translations will be set to 0.
				//Selected tile will return to its old position.
				((Rectangle) t.getSource()).setTranslateX(0);
				((Rectangle) t.getSource()).setTranslateY(0);
				this.setDragAmountX(((Rectangle)t.getSource()).getTranslateX());
				this.setDragAmountY(((Rectangle)t.getSource()).getTranslateY());
				this.setNewPositionX(((Rectangle)t.getSource()).getTranslateX());
				this.setNewPositionY(((Rectangle)t.getSource()).getTranslateY());
				this.setDragAmountX(((Rectangle)t.getSource()).getTranslateX());
				this.setDragAmountY(((Rectangle)t.getSource()).getTranslateY());
			}
		}
		
		else if(this.dragAmountX < 250 && 100<this.dragAmountX && this.dragAmountY <50 && this.dragAmountY>-50) {
			for(Tile tile : Main.getTile_list()) {
				if(Main.getGp().getColumnIndex(tile.getRectangle()) == mevcut_column+1 && Main.getGp().getRowIndex(tile.getRectangle()) == mevcut_row ) {
					 temp_tile =  tile;
				}
			}
			

			if(temp_tile.SayClassNameString().equals("application.Empty_Free")) {
				temp_tile.setNumber(getNumber());
				this.setNumber(getNumber()+1);
				
				//Game board (gp) will be cleared and created again(sortArray() and adjustGridPane()).
				//Movement increases by 1.
				Main.getGp().getChildren().clear();
				Main.sortArray(Main.getTile_array(), Main.getTile_list());
				Main.adjustGridPane(Main.getGp(), Main.getTile_array());
				int a = Main.getMoves() + 1;
				Main.setMoves(a);
					
				//Variables of this method and selected tile's translations will be set to 0.
				((Rectangle) t.getSource()).setTranslateX(0);
				((Rectangle) t.getSource()).setTranslateY(0);
				this.setDragAmountX(((Rectangle)t.getSource()).getTranslateX());
				this.setDragAmountY(((Rectangle)t.getSource()).getTranslateY());
				this.setNewPositionX(((Rectangle)t.getSource()).getTranslateX());
				this.setNewPositionY(((Rectangle)t.getSource()).getTranslateY());
				this.setDragAmountX(((Rectangle)t.getSource()).getTranslateX());
				this.setDragAmountY(((Rectangle)t.getSource()).getTranslateY());
				
			
				
			}else {
				//Movement cannot be done.
				//Variables of this method and selected tile's translations will be set to 0.
				//Selected tile will return to its old position.
				((Rectangle) t.getSource()).setTranslateX(0);
				((Rectangle) t.getSource()).setTranslateY(0);
				this.setDragAmountX(((Rectangle)t.getSource()).getTranslateX());
				this.setDragAmountY(((Rectangle)t.getSource()).getTranslateY());
				this.setNewPositionX(((Rectangle)t.getSource()).getTranslateX());
				this.setNewPositionY(((Rectangle)t.getSource()).getTranslateY());
				this.setDragAmountX(((Rectangle)t.getSource()).getTranslateX());
				this.setDragAmountY(((Rectangle)t.getSource()).getTranslateY());
			}
		}
		
		
		else {
			
			
			
			getRectangle().setTranslateX(position_x);
			getRectangle().setTranslateY(position_y);
		}
	}
}