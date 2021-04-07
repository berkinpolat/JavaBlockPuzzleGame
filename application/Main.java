//Created by @berkinpolat & @ozgur-taylan

/* "THE GAME" a Java game.
 * Purpose: Find the correct path to move the ball from start point to end point by moving tiles on the board.
 */
package application;
	
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import javafx.animation.PathTransition;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.ArcTo;
import javafx.scene.shape.Circle;
import javafx.scene.shape.HLineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.VLineTo;
import javafx.scene.text.Font;

public class Main extends Application {
	private static  GridPane gp;
	private static Tile[][] tile_array;
	private static ArrayList<Tile> tile_list;
	private static File file;
	private static Scanner sc;
	private static int moves, level = 1, unlockLevelUntil = 1;
	private static boolean isRead = false;
	private static Label lblStatus = new Label(" Level: " + getLevel() + "\nMoves: " + getMoves());
	private static BorderPane borderPane;
	private static Circle ball;
	private static String mouseHover = "-fx-background-color: linear-gradient(from 50% 0% to 50% 100%, #c4a15e, #ffe3a9)";
	private static String buttonNormal = "-fx-background-color: linear-gradient(from 50% 0% to 50% 100%, #ffe3a9, #c4a15e)";
	private static Path path = new Path();
	
	public void start(Stage primaryStage) {
		ball = new Circle();
		tile_array = new Tile[4][4];
		tile_list = new ArrayList<>();
		
		lblStatus.setPadding(new Insets(2,0,2,10));
		lblStatus.setFont(new Font("Arial Black", 13));
		lblStatus.setTextFill(Color.web("#ffe0a3"));
		
		gp = new GridPane();
		borderPane = new BorderPane();

		gp.setStyle("-fx-background-color:rgb(61,61,61)");
		
		levelMenu();
		
		Scene scene = new Scene(borderPane, 600, 644 );
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.setTitle("The Game");
		primaryStage.initStyle(StageStyle.DECORATED);
		primaryStage.getIcons().add(new Image("images/ball/ball.png"));
		primaryStage.show();
		
	}
///////////////////////////////////////////////////////////////////////////////////
	public static void readInput() {
//This method creates new levels by reading inputs from level files.
//Clears the gridpane, tile list and tile array in order to create new level without any issues.
		gp.getChildren().clear();
		tile_list.clear();
		for(int a = 0 ; a<tile_array.length ; a++) {
			for(int b = 0 ; b< tile_array.length ; b++) {
				tile_array[a][b] = null;
			}
		}
//Selecting the file by the value of 'level' variable.
		if(level==1) {
			file = new File("levels/level1.txt");
		}
		else if(level==2) {
			file = new File("levels/level2.txt");
		}
		else if(level==3) {
			file = new File("levels/level3.txt");
		}
		else if(level==4) {
			file = new File("levels/level4.txt");
		}
		else if(level==5) {
			file = new File("levels/level5.txt");
		}
/*
 **creating level**
Places the game board to center of pane.
Sets the location of status label to middle of upper pane.
Sets the text of status label
Sets the background color of pane to rgb(61,61,61) which is the color of free tile.*/
		borderPane.setCenter(gp);
		
		borderPane.setTop(lblStatus);
		borderPane.setMargin(lblStatus, new Insets(0, 0, 0, 260));
		
		lblStatus.setText(" Level: " + level + "\nMoves: " + moves);
		borderPane.setStyle("-fx-background-color:rgb(61,61,61)");
		
		try {
			sc = new Scanner(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
//By using these methods, readInput method applies the properties to the game.
		createTiles(tile_list, sc, file);
		sortArray(tile_array, tile_list);
		adjustGridPane(gp, tile_array);
		
	}
///////////////////////////////////////////////////////////////////////////////////
	public static void levelMenu() {
/*This method creates the level select menu.
Creating 5 level buttons and an exit button.*/
		Button level1 = new Button("Level 1");
		Button level2 = new Button("Level 2");
		Button level3 = new Button("Level 3");
		Button level4 = new Button("Level 4");
		Button level5 = new Button("Level 5");
		Button exit = new Button("Exit");
//Sets buttons fill color and text color to proper values.
		level1.setStyle(buttonNormal);
		level1.setTextFill(Color.web("#000000"));
		
		level2.setStyle(buttonNormal);
		level2.setTextFill(Color.web("#000000"));
		
		level3.setStyle(buttonNormal);
		level3.setTextFill(Color.web("#000000"));
		
		level4.setStyle(buttonNormal);
		level4.setTextFill(Color.web("#000000"));
		
		level5.setStyle(buttonNormal);
		level5.setTextFill(Color.web("#000000"));
		
		exit.setStyle("-fx-background-color: linear-gradient(from 50% 0% to 50% 100%, #5a7fdb, #384d81)");
		exit.setTextFill(Color.web("#ffffff"));
//Sets the background image to a custom made image.
		borderPane.setStyle("-fx-background-image: url(images/design/1142bg.png)");
//Unlocks levels until a level which is represented by 'unlockLevelUntil' variable.
		if (unlockLevelUntil == 1) {
			level2.setDisable(true);
			level3.setDisable(true);
			level4.setDisable(true);
			level5.setDisable(true);
			
		}
		else if (unlockLevelUntil == 2) {
			level2.setDisable(false);
			level3.setDisable(true);
			level4.setDisable(true);
			level5.setDisable(true);
		}
		else if (unlockLevelUntil == 3) {
			level2.setDisable(false);
			level3.setDisable(false);
			level4.setDisable(true);
			level5.setDisable(true);
		}
		else if (unlockLevelUntil == 4) {
			level2.setDisable(false);
			level3.setDisable(false);
			level4.setDisable(false);
			level5.setDisable(true);
		}
		else if (unlockLevelUntil == 5) {
			level2.setDisable(false);
			level3.setDisable(false);
			level4.setDisable(false);
			level5.setDisable(false);
		}
//Sets buttons sizes.
		level1.setMaxWidth(Double.MAX_VALUE);
		level1.setMinHeight(25);
		
		level2.setMaxWidth(Double.MAX_VALUE);
		level2.setMinHeight(25);
		
		level3.setMaxWidth(Double.MAX_VALUE);
		level3.setMinHeight(25);
		
		level4.setMaxWidth(Double.MAX_VALUE);
		level4.setMinHeight(25);
		
		level5.setMaxWidth(Double.MAX_VALUE);
		level5.setMinHeight(25);
		
		exit.setMinSize(100, 30);
//Creates a VBox object to place buttons in a column and arranges the padding.
		VBox vbButtons = new VBox();
		vbButtons.setSpacing(10);
		vbButtons.setPadding(new Insets(250, 150, 15, 150)); 
		vbButtons.getChildren().addAll(level1, level2, level3, level4, level5);
//Creates a BorderPane object to place buttons on it.
		BorderPane pane = new BorderPane();
//Clears game board and the pane used in the game, and puts BorderPane object to Center of it.
		pane.setCenter(vbButtons);
		pane.setBottom(exit);
		BorderPane.setMargin(exit, new Insets(0, 0, 150, 250));
		gp.getChildren().clear();
		borderPane.getChildren().clear();
		borderPane.setCenter(pane);
/*Setting button colors to different values while hovering above them (for a better visuality), and resetting them to their default values.
Adds tasks to buttons to run when a user clicks on them (ex: press to level4 button to play level 4)*/
		level1.setOnMouseEntered(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				level1.setStyle(mouseHover);
			}
		});
		level1.setOnMouseExited(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				level1.setStyle(buttonNormal);
			}
		});
		level1.setOnMouseClicked(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
			setLevel(1);
			readInput();
			}
		});
		level2.setOnMouseEntered(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				level2.setStyle(mouseHover);
			}
		});
		level2.setOnMouseExited(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				level2.setStyle(buttonNormal);
			}
		});
		level2.setOnMouseClicked(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
			setLevel(2);
			readInput();
			}
		});
		level3.setOnMouseEntered(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				level3.setStyle(mouseHover);
			}
		});
		level3.setOnMouseExited(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				level3.setStyle(buttonNormal);
			}
		});
		level3.setOnMouseClicked(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) { 
			setLevel(3);
			readInput();
			}
		});
		level4.setOnMouseEntered(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				level4.setStyle(mouseHover);
			}
		});
		level4.setOnMouseExited(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				level4.setStyle(buttonNormal);
			}
		});
		level4.setOnMouseClicked(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
			setLevel(4);
			readInput();
			}
		});
		level5.setOnMouseEntered(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				level5.setStyle(mouseHover);
			}
		});
		level5.setOnMouseExited(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				level5.setStyle(buttonNormal);
			}
		});
		level5.setOnMouseClicked(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
			setLevel(5);
			readInput();
			}
		});
		
		exit.setOnMouseEntered(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				exit.setStyle("-fx-background-color: linear-gradient(from 50% 0% to 50% 100%, #384d81, #5a7fdb)");
			}
		});
		exit.setOnMouseExited(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				exit.setStyle("-fx-background-color: linear-gradient(from 50% 0% to 50% 100%, #5a7fdb, #384d81)");
			}
		});
		exit.setOnMouseClicked(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
			System.exit(0);
			}
		});
	}
///////////////////////////////////////////////////////////////////////////////////
	public static void endLevel(int level) {
/*This method prints level end screen when a level ends.
Creating buttons to retry level, go to main menu or to play next level.*/
		Button retryButton = new Button("Retry");
		Button mainMenuButton = new Button("Main Menu");
		Button nextLevelButton = new Button("Next Level");
//Creates a BorderPane object to place buttons on it.
		BorderPane pane = new BorderPane();
//Creates a label object to print level name and moves.
		Label levelPassedLabel = new Label("Level " + getLevel() + " passed!" + "\nMoves: " + getMoves());
		levelPassedLabel.setPadding(new Insets(2,0,2,10));
		levelPassedLabel.setFont(new Font("Arial Black", 13));
		levelPassedLabel.setTextFill(Color.web("#ffe0a3"));
//Sets buttons sizes and their visuality.
		retryButton.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		mainMenuButton.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		nextLevelButton.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

		retryButton.setStyle(buttonNormal);
		retryButton.setTextFill(Color.web("#ac6c39"));
		
		mainMenuButton.setStyle(buttonNormal);
		mainMenuButton.setTextFill(Color.web("#ac6c39"));
		
		nextLevelButton.setStyle(buttonNormal);
		nextLevelButton.setTextFill(Color.web("#ac6c39"));
//Creates a TilePane objects to place buttons on it
		TilePane tileButtons = new TilePane(Orientation.HORIZONTAL);
		tileButtons.setPadding(new Insets(7, 10, 2, 7));
		tileButtons.setHgap(10.0);
//Unlocking the next level.
		if (level == 1 && unlockLevelUntil < 2)
			unlockLevelUntil = 2;
		else if (level == 2 && unlockLevelUntil < 3)
			unlockLevelUntil = 3;
		else if (level == 3 && unlockLevelUntil < 4)
			unlockLevelUntil = 4;
		else if (level == 4 && unlockLevelUntil < 5)
			unlockLevelUntil = 5;
//Since there are no more levels than 5, next level button will not be in the end of level.
		if (level < 5) {
			/*Setting button colors to different values while hovering above them (for a better visuality), and resetting them to their default values.
			Adds tasks to buttons to run when a user clicks on them:
			retry -> clears the pane, sets movement skills to true, places status label to true, re-creates the level.
			main menu -> clears the pane, sets movement skills to true, creates the level menu.
			next level -> clears the pane, sets movement skills to true, places status label to true, creates the next level.
			*/
			tileButtons.getChildren().addAll(retryButton, mainMenuButton, nextLevelButton);
			
			pane.setLeft(levelPassedLabel);
			pane.setRight(tileButtons);
			
			borderPane.setTop(pane);
			
			moves = 0;
			
			retryButton.setOnMouseEntered(new EventHandler<MouseEvent>() {
				public void handle(MouseEvent event) {
					retryButton.setStyle(mouseHover);
				}
			});
			retryButton.setOnMouseExited(new EventHandler<MouseEvent>() {
				public void handle(MouseEvent event) {
					retryButton.setStyle(buttonNormal);
				}
			});
			retryButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
				public void handle(MouseEvent event) {
					pane.getChildren().clear();
					Pipe.setCanPipeMove(true);
					Empty_None.setCanNoneMove(true);
					borderPane.setTop(lblStatus);
					setLevel(level);
					readInput();
				}
			});
			
			mainMenuButton.setOnMouseEntered(new EventHandler<MouseEvent>() {
				public void handle(MouseEvent event) {
					mainMenuButton.setStyle(mouseHover);
				}
			});
			mainMenuButton.setOnMouseExited(new EventHandler<MouseEvent>() {
				public void handle(MouseEvent event) {
					mainMenuButton.setStyle(buttonNormal);
				}
			});
			mainMenuButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
				public void handle(MouseEvent event) {
					pane.getChildren().clear();
					Pipe.setCanPipeMove(true);
					Empty_None.setCanNoneMove(true);
					levelMenu();
				}
			});
			
			nextLevelButton.setOnMouseEntered(new EventHandler<MouseEvent>() {
				public void handle(MouseEvent event) {
					nextLevelButton.setStyle(mouseHover);
				}
			});
			nextLevelButton.setOnMouseExited(new EventHandler<MouseEvent>() {
				public void handle(MouseEvent event) {
					nextLevelButton.setStyle(buttonNormal);
				}
			});
			nextLevelButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
				public void handle(MouseEvent event) {
					pane.getChildren().clear();
					Pipe.setCanPipeMove(true);
					Empty_None.setCanNoneMove(true);
					borderPane.setTop(lblStatus);
					int newLevel = getLevel() + 1;
					setLevel(newLevel);
					readInput();
				}
			});
		}
		else {
/*Setting button colors to different values while hovering above them (for a better visuality), and resetting them to their default values.
Adds tasks to buttons to run when a user clicks on them:
retry -> clears the pane, sets movement skills to true, places status label to true, re-creates the level.
main menu -> clears the pane, sets movement skills to true, creates the level menu.
*/
			tileButtons.getChildren().addAll(retryButton, mainMenuButton);
			
			pane.setLeft(levelPassedLabel);
			pane.setRight(tileButtons);
			
			borderPane.setTop(pane);
			
			moves = 0;
			
			retryButton.setOnMouseEntered(new EventHandler<MouseEvent>() {
				public void handle(MouseEvent event) {
					retryButton.setStyle(mouseHover);
				}
			});
			retryButton.setOnMouseExited(new EventHandler<MouseEvent>() {
				public void handle(MouseEvent event) {
					retryButton.setStyle(buttonNormal);
				}
			});
			retryButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
				public void handle(MouseEvent event) {
					pane.getChildren().clear();
					Pipe.setCanPipeMove(true);
					Empty_None.setCanNoneMove(true);
					borderPane.setTop(lblStatus);
					setLevel(level);
					readInput(); 
				}
			});
			
			mainMenuButton.setOnMouseEntered(new EventHandler<MouseEvent>() {
				public void handle(MouseEvent event) {
					mainMenuButton.setStyle(mouseHover);
				}
			});
			mainMenuButton.setOnMouseExited(new EventHandler<MouseEvent>() {
				public void handle(MouseEvent event) {
					mainMenuButton.setStyle(buttonNormal);
				}
			});
			mainMenuButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
				public void handle(MouseEvent event) {
					pane.getChildren().clear();
					Pipe.setCanPipeMove(true);
					Empty_None.setCanNoneMove(true);
					levelMenu();
				}
			});
		}
	}
///////////////////////////////////////////////////////////////////////////////////
	public static void sortArray( Tile[][] tile , ArrayList<Tile> tileList) {
		//This method puts tile objects into tile array properly by using tile arraylist.
		//Nested for loops will detect tile number from tile list and when the variable 'testId' is equal to number, array will be filled with the tile.
		for (int i = 0; i < 4; i++) {
	    	for (int j = 0; j < 4; j++) {
	    		int testId = (i * 4) + (j + 1);
	    		for (int k = 0 ; k < tileList.size(); k++) {
	    			if (testId == tileList.get(k).getNumber()) {
	    				tile[i][j] = tileList.get(k);
	    			}
	    		}
		    }
	    }
	}	
///////////////////////////////////////////////////////////////////////////////////
	public static void adjustGridPane(GridPane gp , Tile[][] tile_array) {
		//This method will add game tiles to the game board which is called 'gridPane' by taking array's location and adjusting it to gridpane's location.
		
		try{
			for(int as = 0 ; as<tile_array.length ; as++) {
				for(int bs = 0 ; bs<tile_array.length ; bs++) {
						gp.add(tile_array[as][bs].getRectangle(), bs, as);
					}
				}
			
		}catch(Exception e) {
			System.out.println(e);	
		}
		createBall(gp);
	}
///////////////////////////////////////////////////////////////////////////////////
	public static void createTiles(ArrayList<Tile> tile_list,	Scanner sc ,  File file ) {
/*This method takes the input file and scans it to get its content to create tile objects.
 * It reads lines and splits them by using split() method and saves them to an array called "var".
 * By using array's index' it creates Tile objects specified as Starter, Empty, Pipe, PipeStatic or End and sets their properties.
 * Method assigns images to tiles in order to represent them on game board.
 * Finally, method adds created object to the arraylist called tile_list.
 */
		String value;
		try {
			
			while(sc.hasNextLine()) {
				value = sc.nextLine();
				if(value.equals("")) {
				}else {
				value = value.toLowerCase();
				String[] var = value.split(",");
				
				
				if(var[1].equals("starter")) {
					Starter starter = new Starter();
					int number = Integer.parseInt(var[0]);
					starter.setNumber(number);
					starter.setType("Starter");
					if(var[2].equals("vertical")) {
						Image image = new Image("images/starter/starterVertical.png");
						starter.setImage(image);
						starter.setImageOfRectangle();
						starter.setProperty("Vertical");
						}
					if(var[2].equals("horizontal")) {
						Image image = new Image("images/starter/starterHorizontal.png");
						starter.setImage(image);
						starter.setImageOfRectangle();
						starter.setProperty("Horizontal");
						}
					tile_list.add(starter);
					}
//***************************************************************************************************************
				else if(var[1].equals("empty")) {
					if(var[2].equals("free")) {
						Empty_Free empty = new Empty_Free();
						int number = Integer.parseInt(var[0]);
						empty.setNumber(number);
						Image image = new Image("images/empty/emptyFree.png");
						empty.setImage(image);
						empty.setImageOfRectangle();
						empty.setType("Empty");
						empty.setProperty("Free");
						tile_list.add(empty);
						}
					
					if(var[2].equals("none")) {
						Empty_None empty = new Empty_None();
						int number = Integer.parseInt(var[0]);
						empty.setNumber(number);
						Image image = new Image("images/empty/emptyNone.png");
						empty.setImage(image);
						empty.setImageOfRectangle();
						empty.setType("Empty");
						empty.setProperty("None");
						tile_list.add(empty);
						}
					}			
//***************************************************************************************************************
				else if(var[1].equals("pipe")) {
					Pipe pipe = new Pipe();
					int number = Integer.parseInt(var[0]);
					pipe.setNumber(number);
					if(var[2].equals("vertical")) {
						Image image = new Image("images/pipe/pipeVertical.png");
						pipe.setImage(image);
						pipe.setImageOfRectangle();
						pipe.setType("Pipe");
						pipe.setProperty("Vertical");
					}
						
					if(var[2].equals("horizontal")) {
						Image image = new Image("images/pipe/pipeHorizontal.png");
						pipe.setImage(image);
						pipe.setImageOfRectangle();
						pipe.setType("Pipe");
						pipe.setProperty("Horizontal");
					}
				
					if(var[2].equals("00")) {
						Image image = new Image("images/pipe/pipe00.png");
						pipe.setImage(image);
						pipe.setImageOfRectangle();
						pipe.setType("Pipe");
						pipe.setProperty("00");
					}
					
					if(var[2].equals("01")) {
						Image image = new Image("images/pipe/pipe01.png");
						pipe.setImage(image);
						pipe.setImageOfRectangle();
						pipe.setType("Pipe");
						pipe.setProperty("01");
					}
					
					if(var[2].equals("10")) {
						Image image = new Image("images/pipe/pipe10.png");
						pipe.setImage(image);
						pipe.setImageOfRectangle();
						pipe.setType("Pipe");
						pipe.setProperty("10");
					}
					
					if(var[2].equals("11")) {
						Image image = new Image("images/pipe/pipe11.png");
						pipe.setImage(image);
						pipe.setImageOfRectangle();
						pipe.setType("Pipe");
						pipe.setProperty("11");
					}
					tile_list.add(pipe);
				}		
//***************************************************************************************************************
				else if(var[1].equals("pipestatic")) {	
					PipeStatic pipeStatic = new PipeStatic();
					int number = Integer.parseInt(var[0]);
					pipeStatic.setNumber(number);
					if(var[2].equals("horizontal")) {
						Image image = new Image("images/pipeStatic/pipeHorizontal.png");
						pipeStatic.setImage(image);
						pipeStatic.setImageOfRectangle();
						pipeStatic.setType("PipeStatic");
						pipeStatic.setProperty("Horizontal");
					}
					
					if(var[2].equals("vertical")) {
						Image image = new Image("images/pipeStatic/pipeVertical.png");
						pipeStatic.setImage(image);
						pipeStatic.setImageOfRectangle();
						pipeStatic.setType("PipeStatic");
						pipeStatic.setProperty("Vertical");
					}
					
					if(var[2].equals("00")) {
						Image image = new Image("images/pipeStatic/pipe00.png");
						pipeStatic.setImage(image);
						pipeStatic.setImageOfRectangle();
						pipeStatic.setType("PipeStatic");
						pipeStatic.setProperty("00");
					}
					
					if(var[2].equals("01")) {
						Image image = new Image("images/pipeStatic/pipe01.png");
						pipeStatic.setImage(image);
						pipeStatic.setImageOfRectangle();
						pipeStatic.setType("PipeStatic");
						pipeStatic.setProperty("01");
					}
					
					if(var[2].equals("10")) {
						Image image = new Image("images/pipeStatic/pipe10.png");
						pipeStatic.setImage(image);
						pipeStatic.setImageOfRectangle();
						pipeStatic.setType("PipeStatic");
						pipeStatic.setProperty("10");
					}
					
					if(var[2].equals("11")) {
						Image image = new Image("images/pipeStatic/pipe11.png");
						pipeStatic.setImage(image);
						pipeStatic.setImageOfRectangle();
						pipeStatic.setType("PipeStatic");
						pipeStatic.setProperty("11");
					}
					tile_list.add(pipeStatic);
				}		
//***************************************************************************************************************
				else {
					End end = new End();
					int number = Integer.parseInt(var[0]);
					end.setNumber(number);
					if(var[2].equals("horizontal")) {
						Image image = new Image("images/end/endHorizontal.png");
						end.setImage(image);
						end.setImageOfRectangle();
						end.setType("End");
						end.setProperty("Horizontal");
					}
					if(var[2].equals("vertical")) {
						Image image = new Image("images/end/endVertical.png");
						end.setImage(image);
						end.setImageOfRectangle();
						end.setType("End");
						end.setProperty("Vertical");
					}
					tile_list.add(end);
				}		
//***************************************************************************************************************
			}
			}
		
		} catch (Exception e) {
			System.out.println(e);
			
		}
	}	
///////////////////////////////////////////////////////////////////////////////////
public static boolean isPassed(Tile[][] tile) {
//This method returns a boolean value with the help of findTile() method by testing whether the level is completed or not.
//Creating a Tile object called "checkpoint" to remember the tile in hand.	
		Tile checkpoint = null;
//Creating a Tile double array called "cloneArray" which is a clone of the main tile array which stores the information about tiles on board.
		Tile[][] cloneArray = new Tile[4][4];
		for (int i = 0; i < tile.length; i++) {
			for (int j = 0; j < tile.length; j++) {
				cloneArray[i][j] = tile[i][j];
			}
		}
//Finding the starter tile and setting it as checkpoint and and puts a Tile object named "Test" into its place in order to say "this tile has already read".
		for (int i = 0; i < tile.length; i++) {
			for (int j = 0; j < tile.length; j++) {
				if (cloneArray[i][j].getType().equals("Starter")) {
					checkpoint = cloneArray[i][j];
					Tile test = new Tile();
					test.setType("Test");
					test.setNumber(checkpoint.getNumber());
					cloneArray[i][j] = test;
				}
			}
		}
//Creating a count integer as 16, because computer can search for 16 tiles maximum.
		int count = 16;
		while (count > 0){
//While counting down to 0, this method uses findTile() to get new tiles for checkpoint Tile.
			checkpoint = findTile(cloneArray, checkpoint);
			if (checkpoint.getType().equals("End")) {
//If checkpoint is an end tile, method will return true. Because this means that method found an end tile, starting from starter tile. Level is complete.
				return true;
			}
			count--;
		}
//If count is 0, this means the method couldn't find any end tiles, starting from starter tile.
		return false;
	}
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public static Tile findTile(Tile[][] cloneArray, Tile checkpoint) {
//This method reads the cloneArray which was sent from previous method, and tries to return a Tile which is adjacent to the checkpoint.
//Nested for loops to read all of the cloneArray.
	for (int i = 0; i < 4; i++) {
		for (int j = 0; j < 4; j++) {
//If checkpoint's number is equal to a tile in array, method will try to find a matching tile adjacent to the checkpoint.
			if (checkpoint.getNumber() == cloneArray[i][j].getNumber()) {
				
				/*If any tiles have been found, it will be saved as checkpoint and put a Tile object named "Test" into its place in order to say "this tile has already read".

				EX: This method searches like this:
					If the checkpoints number is equal to arrays current position, let's say:
					Checkpoint's type and property is Horizontal Pipe.
					Horizontal Pipe tile has 2 ends: left and right.
					Left and right means j-1 and j+1.
					If j-1 or j+1 in array has any of the tiles which are appropriate types (When looking right, tile on the right must have an end on left.)
					Let's say that the tile on the right is appropriate. (j+1)
					Tile on the right will be saved as checkpoint.
					Position of the tile is set as a Test Tile called "Test" and its number is set as Test Tile's number.
					Since the method searches for the types "Pipe" "PipeStatic" or "End" , test tile will be invisible to method.
					Checkpoint will be returned as the selected tile.
											
																			TILE MAP
																		Pipe-PipeStatic, 10
																		Pipe-PipeStatic, 11
																		Pipe-PipeStatic, Vertical
																		END - VERTICAL
																		
																				^
										Pipe-PipeStatic, 01						|				Pipe-PipeStatic, 00
										Pipe-PipeStatic, 11	       		 <-Checkpoint->			Pipe-PipeStatic, 10
										Pipe-PipeStatic, Horizontal				|				Pipe-PipeStatic, Horizontal
																				v				END - HORIZONTAL
												
																		Pipe-PipeStatic, 00
																		Pipe-PipeStatic, 01
																		Pipe-PipeStatic, Vertical
				*/
				
				if(checkpoint.getType().equals("Starter")) {
					if (checkpoint.getProperty().equals("Horizontal")) {
						int a = j - 1;
						if (a > -1) {
							if ((cloneArray[i][a].getType().equals("Pipe") || cloneArray[i][a].getType().equals("PipeStatic")) &&
									(cloneArray[i][a].getProperty().equals("01") || cloneArray[i][a].getProperty().equals("11") ||cloneArray[i][a].getProperty().equals("Horizontal"))) {
								checkpoint = cloneArray[i][a];
								Tile test = new Tile();
								test.setType("Test");
								test.setNumber(checkpoint.getNumber());
								cloneArray[i][a] = test;
								return checkpoint;
							}
						}
					}
					else {
						int a = i + 1;
						if (a < 4) {
							if ((cloneArray[a][j].getType().equals("Pipe") || cloneArray[a][j].getType().equals("PipeStatic")) &&
									(cloneArray[a][j].getProperty().equals("01") || cloneArray[a][j].getProperty().equals("00") ||cloneArray[a][j].getProperty().equals("Vertical"))) {
								checkpoint = cloneArray[a][j];
								Tile test = new Tile();
								test.setType("Test");
								test.setNumber(checkpoint.getNumber());
								cloneArray[a][j] = test;
								return checkpoint;
							}
						}
					}
				}
//******************************************************************
				else if(checkpoint.getType().equals("Pipe") || checkpoint.getType().equals("PipeStatic")) {
					if (checkpoint.getProperty().equals("Horizontal")) {
						int a = j - 1;
						int b = j + 1;
						if (a > -1) {
							if ((cloneArray[i][a].getType().equals("Pipe") || cloneArray[i][a].getType().equals("PipeStatic")) &&
									(cloneArray[i][a].getProperty().equals("01") || cloneArray[i][a].getProperty().equals("11") ||cloneArray[i][a].getProperty().equals("Horizontal"))) {
								checkpoint = cloneArray[i][a];
								Tile test = new Tile();
								test.setType("Test");
								test.setNumber(checkpoint.getNumber());
								cloneArray[i][a] = test;
								return checkpoint;
							}
						}
						if (b < 4) {
							if ((cloneArray[i][b].getType().equals("Pipe") || cloneArray[i][b].getType().equals("PipeStatic")) &&
									(cloneArray[i][b].getProperty().equals("00") || cloneArray[i][b].getProperty().equals("10") ||cloneArray[i][b].getProperty().equals("Horizontal"))) {
								checkpoint = cloneArray[i][b];
								Tile test = new Tile();
								test.setType("Test");
								test.setNumber(checkpoint.getNumber());
								cloneArray[i][b] = test;
								return checkpoint;
							}
							else if (cloneArray[i][b].getType().equals("End") && cloneArray[i][b].getProperty().equals("Horizontal")) {
								checkpoint = cloneArray[i][b];
								Tile test = new Tile();
								test.setType("Test");
								test.setNumber(checkpoint.getNumber());
								cloneArray[i][b] = test;
								return checkpoint;
							}
						}
					}
					else if (checkpoint.getProperty().equals("Vertical")) {
						int a = i + 1;
						int b = i - 1;
						if (a < 4) {
							if ((cloneArray[a][j].getType().equals("Pipe") || cloneArray[a][j].getType().equals("PipeStatic")) &&
									(cloneArray[a][j].getProperty().equals("01") || cloneArray[a][j].getProperty().equals("00") ||cloneArray[a][j].getProperty().equals("Vertical"))) {
								checkpoint = cloneArray[a][j];
								Tile test = new Tile();
								test.setType("Test");
								test.setNumber(checkpoint.getNumber());
								cloneArray[a][j] = test;
								return checkpoint;
							}
						}
						if (b > -1) {
							if ((cloneArray[b][j].getType().equals("Pipe") || cloneArray[b][j].getType().equals("PipeStatic")) &&
									(cloneArray[b][j].getProperty().equals("10") || cloneArray[b][j].getProperty().equals("11") ||cloneArray[b][j].getProperty().equals("Vertical"))) {
								checkpoint = cloneArray[b][j];
								Tile test = new Tile();
								test.setType("Test");
								test.setNumber(checkpoint.getNumber());
								cloneArray[b][j] = test;
								return checkpoint;
							}
							else if (cloneArray[b][j].getType().equals("End") && cloneArray[b][j].getProperty().equals("Vertical")) {
								checkpoint = cloneArray[b][j];
								Tile test = new Tile();
								test.setType("Test");
								test.setNumber(checkpoint.getNumber());
								cloneArray[b][j] = test;
								return checkpoint;
							}
						}
					}
					else if (checkpoint.getProperty().equals("00")) {
						int a = j - 1;
						int b = i - 1;
						if (b > -1) {
							if ((cloneArray[b][j].getType().equals("Pipe") || cloneArray[b][j].getType().equals("PipeStatic")) &&
									(cloneArray[b][j].getProperty().equals("10") || cloneArray[b][j].getProperty().equals("11") ||cloneArray[b][j].getProperty().equals("Vertical"))) {
								checkpoint = cloneArray[b][j];
								Tile test = new Tile();
								test.setType("Test");
								test.setNumber(checkpoint.getNumber());
								cloneArray[b][j] = test;
								return checkpoint;
							}
							else if (cloneArray[b][j].getType().equals("End") && cloneArray[b][j].getProperty().equals("Vertical")) {
								checkpoint = cloneArray[b][j];
								Tile test = new Tile();
								test.setType("Test");
								test.setNumber(checkpoint.getNumber());
								cloneArray[b][j] = test;
								return checkpoint;
							}
						}
						if (a > -1) {
							if ((cloneArray[i][a].getType().equals("Pipe") || cloneArray[i][a].getType().equals("PipeStatic")) &&
									(cloneArray[i][a].getProperty().equals("01") || cloneArray[i][a].getProperty().equals("11") ||cloneArray[i][a].getProperty().equals("Horizontal"))) {
								checkpoint = cloneArray[i][a];
								Tile test = new Tile();
								test.setType("Test");
								test.setNumber(checkpoint.getNumber());
								cloneArray[i][a] = test;
								return checkpoint;
							}
						}
					}
					else if (checkpoint.getProperty().equals("01")) {
						int a = j + 1;
						int b = i - 1;
						if (b > -1) {
							if ((cloneArray[b][j].getType().equals("Pipe") || cloneArray[b][j].getType().equals("PipeStatic")) &&
									(cloneArray[b][j].getProperty().equals("10") || cloneArray[b][j].getProperty().equals("11") ||cloneArray[b][j].getProperty().equals("Vertical"))) {
								checkpoint = cloneArray[b][j];
								Tile test = new Tile();
								test.setType("Test");
								test.setNumber(checkpoint.getNumber());
								cloneArray[b][j] = test;
								return checkpoint;
							}
							else if (cloneArray[b][j].getType().equals("End") && cloneArray[b][j].getProperty().equals("Vertical")) {
								checkpoint = cloneArray[b][j];
								Tile test = new Tile();
								test.setType("Test");
								test.setNumber(checkpoint.getNumber());
								cloneArray[b][j] = test;
								return checkpoint;
							}
						}
						if (a < 4) {
							if ((cloneArray[i][a].getType().equals("Pipe") || cloneArray[i][a].getType().equals("PipeStatic")) &&
									(cloneArray[i][a].getProperty().equals("00") || cloneArray[i][a].getProperty().equals("10") ||cloneArray[i][a].getProperty().equals("Horizontal"))) {
								checkpoint = cloneArray[i][a];
								Tile test = new Tile();
								test.setType("Test");
								test.setNumber(checkpoint.getNumber());
								cloneArray[i][a] = test;
								return checkpoint;
							}
							else if (cloneArray[i][a].getType().equals("End") && cloneArray[i][a].getProperty().equals("Horizontal")) {
								checkpoint = cloneArray[i][a];
								Tile test = new Tile();
								test.setType("Test");
								test.setNumber(checkpoint.getNumber());
								cloneArray[i][a] = test;
								return checkpoint;
							}
						}
					}
					else if (checkpoint.getProperty().equals("10")) {
						int a = j - 1;
						int b = i + 1;
						if (a > -1) {
							if ((cloneArray[i][a].getType().equals("Pipe") || cloneArray[i][a].getType().equals("PipeStatic")) &&
									(cloneArray[i][a].getProperty().equals("01") || cloneArray[i][a].getProperty().equals("11") ||cloneArray[i][a].getProperty().equals("Horizontal"))) {
								checkpoint = cloneArray[i][a];
								Tile test = new Tile();
								test.setType("Test");
								test.setNumber(checkpoint.getNumber());
								cloneArray[i][a] = test;
								return checkpoint;
							}
						}
						if (b < 4) {
							if ((cloneArray[b][j].getType().equals("Pipe") || cloneArray[b][j].getType().equals("PipeStatic")) &&
									(cloneArray[b][j].getProperty().equals("01") || cloneArray[b][j].getProperty().equals("00") ||cloneArray[b][j].getProperty().equals("Vertical"))) {
								checkpoint = cloneArray[b][j];
								Tile test = new Tile();
								test.setType("Test");
								test.setNumber(checkpoint.getNumber());
								cloneArray[b][j] = test;
								return checkpoint;
							}
						}
					}
					else if (checkpoint.getProperty().equals("11")) {
						int a = j + 1;
						int b = i + 1;
						if (a < 4) {
							if ((cloneArray[i][a].getType().equals("Pipe") || cloneArray[i][a].getType().equals("PipeStatic")) &&
									(cloneArray[i][a].getProperty().equals("00") || cloneArray[i][a].getProperty().equals("10") ||cloneArray[i][a].getProperty().equals("Horizontal"))) {
								checkpoint = cloneArray[i][a];
								Tile test = new Tile();
								test.setType("Test");
								test.setNumber(checkpoint.getNumber());
								cloneArray[i][a] = test;
								return checkpoint;
							}
							else if (cloneArray[i][a].getType().equals("End") && cloneArray[i][a].getProperty().equals("Horizontal")) {
								checkpoint = cloneArray[i][a];
								Tile test = new Tile();
								test.setType("Test");
								test.setNumber(checkpoint.getNumber());
								cloneArray[i][a] = test;
								return checkpoint;
							}
						}
						if (b < 4) {
							if ((cloneArray[b][j].getType().equals("Pipe") || cloneArray[b][j].getType().equals("PipeStatic")) &&
									(cloneArray[b][j].getProperty().equals("01") || cloneArray[b][j].getProperty().equals("00") ||cloneArray[b][j].getProperty().equals("Vertical"))) {
								checkpoint = cloneArray[b][j];
								Tile test = new Tile();
								test.setType("Test");
								test.setNumber(checkpoint.getNumber());
								cloneArray[b][j] = test;
								return checkpoint;
							}
						}
					}
				}
			}
		}
	}
	return checkpoint;
}
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public static void createBall(GridPane gridPane) {
/* This method finds the starter tile and takes its row and column values in order to use them to prompt the ball.
 * By using for loop, method reaches to tiles in tile arraylist.
 * Method takes their properties and types to find the starter tile and its property.
 * Method takes its row and column values to prompt the ball object in correct location.
 * Method adds the ball object to gridpane which is game board.
 */
		Image ball_image = new Image("images/ball/ball.png");
		Main.ball.setRadius(25);
		Main.ball.setTranslateY(0);
		Main.ball.setTranslateX(50);
		Main.ball.setFill(new ImagePattern(ball_image));
		for(int a = 0 ; a< Main.getTile_list().size() ; a++) {
			String type = Main.getTile_list().get(a).getType();
			String prop = Main.getTile_list().get(a).getProperty();
			type = type.toLowerCase();
			prop = prop.toLowerCase();
		
			if( ( type.equals("starter") && prop.equals("vertical") ) || ( type.equals("starter") && prop.equals("horizontal") ) ) {
				int row = Main.getGp().getRowIndex(Main.getTile_list().get(a).getRectangle());
				int column = Main.getGp().getColumnIndex(Main.getTile_list().get(a).getRectangle());
				
				gridPane.getChildren().remove(ball);
				gridPane.add(Main.ball, column, row);
			}
		}
	}
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public static void	animateBall(ArrayList<Tile> tile_list1 , Circle c1 , GridPane gp1 ) {
/* This method detects ball object's position and by using it's tile, it creates a path by following pipes; starting from starter tile, ending at end tile.
 * This path will be used to create an animation for ending phase of the level.
 * 
 * FOR EX:
 * 			First of all, by using position variable of the ball, a MoveTo object will be created.
 * 			If blocks will compare previous row and column values to current values to select a direction to move.
 * 			After that, a appropriate movement will be created according to tile's properties. (arcto, vlineto ...)
 * 			According to selected direction, ball's row and column will be changed.
 * 			Current tile's row and column values will be saved as previous column and row. (to maintain further usage)
 * 			These actions will be applied until the end tile is found.
 * 
 * 			(NEEDS EXPLANATION IN DEMO SESSION FOR A BETTER ACCURACY)
 */
	
		path.getElements().clear();
		PathTransition transition = new PathTransition();

		int ball_row = gp.getRowIndex(c1);
		int ball_column = gp.getColumnIndex(c1);
		int previousRow = gp.getRowIndex(c1);
		int previousColumn = gp.getRowIndex(c1);
		
		double positionX = c1.getTranslateX();
		double positionY = c1.getTranslateY();
	
		String type;
		String prop;
		
		boolean checker = true;
			while(checker) {
				for(Tile tile : tile_list1) {
				
					if(ball_column == gp1.getColumnIndex(tile.getRectangle()) && ball_row == gp1.getRowIndex(tile.getRectangle())) {
						type = tile.getType();
						prop = tile.getProperty();
						type = type.toLowerCase();
						prop = prop.toLowerCase();
						
						if( ( type.equals("starter") ) && ( prop.equals("vertical") ) ) {
							
							previousColumn = gp1.getColumnIndex(tile.getRectangle());
							previousRow =  gp1.getRowIndex(tile.getRectangle());
							ball_row++;
						MoveTo start_starter_vertical = new MoveTo();
						start_starter_vertical.setX(positionX);
						start_starter_vertical.setY(positionY);
						VLineTo vLineTo_starter_vertical = new VLineTo();
						positionY+=100;
						vLineTo_starter_vertical.setY(positionY);
						path.getElements().add(start_starter_vertical);
						path.getElements().add(vLineTo_starter_vertical);

						}
						else if( ( type.equals("starter") ) && ( prop.equals("horizontal") )) {
							
							previousColumn = gp1.getColumnIndex(tile.getRectangle());
							previousRow =  gp1.getRowIndex(tile.getRectangle());
							ball_column--;	
							
							MoveTo start_starter_horizontal = new MoveTo();
							start_starter_horizontal.setX(positionX);
							start_starter_horizontal.setY(positionY);
							HLineTo hLineTo_starter_horizontal = new HLineTo();
							positionX-=100;
							hLineTo_starter_horizontal.setX(positionX);
							path.getElements().add(start_starter_horizontal);
							path.getElements().add(hLineTo_starter_horizontal);
						}	
						
						else if( ( type.equals("end") ) && ( prop.equals("vertical") )) {
							MoveTo start_end_vertical = new MoveTo();
							start_end_vertical.setX(positionX);
							start_end_vertical.setY(positionY);
							VLineTo vline_end_vertical = new VLineTo();
							positionY-=50;
							vline_end_vertical.setY(positionY);
							path.getElements().add(start_end_vertical);
							path.getElements().add(vline_end_vertical);
							checker = false;
							break;
						}
						else if( ( type.equals("end") ) && ( prop.equals("horizontal") )) {
							MoveTo start_end_horizontal = new MoveTo();
							start_end_horizontal.setX(positionX);
							start_end_horizontal.setY(positionY);
							HLineTo hline_end_horizontal = new HLineTo();
							positionX+=50;
							hline_end_horizontal.setX(positionX);
							path.getElements().add(start_end_horizontal);
							path.getElements().add(hline_end_horizontal);
							checker = false;
							break;
						}
						
						else if( ( type.equals("pipe") ) && ( prop.equals("horizontal") )) {
							if(previousColumn<gp.getColumnIndex(tile.getRectangle())) {	
								MoveTo start_pipe_horizontal = new MoveTo();
								start_pipe_horizontal.setX(positionX);
								start_pipe_horizontal.setY(positionY);
								HLineTo hline_pipe_horizontal = new HLineTo();
								positionX+=150;
								hline_pipe_horizontal.setX(positionX);
								path.getElements().add(start_pipe_horizontal);
								path.getElements().add(hline_pipe_horizontal);	
								ball_column++;				
							}
							else {		
								MoveTo start_pipe_horizontal = new MoveTo();
								start_pipe_horizontal.setX(positionX);
								start_pipe_horizontal.setY(positionY);
								HLineTo hline_pipe_horizontal = new HLineTo();
								positionX-=150;
								hline_pipe_horizontal.setX(positionX);
								path.getElements().add(start_pipe_horizontal);
								path.getElements().add(hline_pipe_horizontal);	
								ball_column--;	
							}
							previousRow = gp.getRowIndex(tile.getRectangle());
							previousColumn = gp.getColumnIndex(tile.getRectangle());	
						}
						
						else if( ( type.equals("pipe") ) && ( prop.equals("vertical") )) {
							
							if(previousRow<gp.getRowIndex(tile.getRectangle())) {		
								MoveTo start_pipe_vertical = new MoveTo();
								start_pipe_vertical.setX(positionX);
								start_pipe_vertical.setY(positionY);
								VLineTo vline_pipe_vertical = new VLineTo();
								positionY+=150;
								path.getElements().add(start_pipe_vertical);
								vline_pipe_vertical.setY(positionY);
								path.getElements().add(vline_pipe_vertical);
								ball_row++;								
							
							}
							else {		
								
								MoveTo start_pipe_vertical = new MoveTo();
								start_pipe_vertical.setX(positionX);
								start_pipe_vertical.setY(positionY);
								VLineTo vline_pipe_vertical = new VLineTo();
								positionY-=150;
								vline_pipe_vertical.setY(positionY);
								path.getElements().add(start_pipe_vertical);	
								path.getElements().add(vline_pipe_vertical);							
								ball_row--;		
							}
							previousRow = gp.getRowIndex(tile.getRectangle());
							previousColumn = gp.getColumnIndex(tile.getRectangle());
						}
						
						else if( ( type.equals("pipe") ) && ( prop.equals("10") )) {
							if(previousRow > gp.getRowIndex(tile.getRectangle())) {	
								MoveTo start_pipe_10 = new MoveTo();
								start_pipe_10.setX(positionX);
								start_pipe_10.setY(positionY);	
								ArcTo arc_pipe_10 = new ArcTo();
								positionX-=50;
								positionY-=50;
								arc_pipe_10.setX(positionX);
								arc_pipe_10.setY(positionY);
								HLineTo hline_pipe_10 = new HLineTo();
								positionX-=50;
								hline_pipe_10.setX(positionX);
								path.getElements().add(start_pipe_10);
								path.getElements().add(arc_pipe_10);
								path.getElements().add(hline_pipe_10);		
								ball_column--;				
							}
							
							if(previousColumn<gp.getColumnIndex(tile.getRectangle())) {				
								MoveTo start_pipe_10 = new MoveTo();
								start_pipe_10.setX(positionX);
								start_pipe_10.setY(positionY);						
								ArcTo arc_pipe_10 = new ArcTo();
								positionX+=50;
								positionY+=50;
								arc_pipe_10.setX(positionX);
								arc_pipe_10.setY(positionY);	
								VLineTo vline_pipe_10 = new VLineTo();
								positionY+=50;
								vline_pipe_10.setY(positionY);
								path.getElements().add(start_pipe_10);
								path.getElements().add(arc_pipe_10);
								path.getElements().add(vline_pipe_10);		
								ball_row++;
								}
								previousRow = gp.getRowIndex(tile.getRectangle());
								previousColumn = gp.getColumnIndex(tile.getRectangle());
						}
						
						else if( ( type.equals("pipe") ) && ( prop.equals("01") )) {	
							if(previousRow < gp.getRowIndex(tile.getRectangle())) {
								
								MoveTo start_pipe_01 = new MoveTo();
								start_pipe_01.setX(positionX);
								start_pipe_01.setY(positionY);	
								ArcTo arc_pipe_01 = new ArcTo();
								positionX+=50;
								positionY+=50;
								arc_pipe_01.setX(positionX);
								arc_pipe_01.setY(positionY);	
								HLineTo hline_pipe_01 = new HLineTo();
								positionX+=50;
								hline_pipe_01.setX(positionX);
								path.getElements().add(start_pipe_01);
								path.getElements().add(arc_pipe_01);
								path.getElements().add(hline_pipe_01);	
								ball_column++;			
							}
						
							if(previousColumn>gp.getColumnIndex(tile.getRectangle())) {							
								
								MoveTo start_pipe_01 = new MoveTo();
								start_pipe_01.setX(positionX);
								start_pipe_01.setY(positionY);							
								ArcTo arc_pipe_01 = new ArcTo();
								positionX-=50;
								positionY-=50;
								arc_pipe_01.setX(positionX);
								arc_pipe_01.setY(positionY);				
								VLineTo vline_pipe_01 = new VLineTo();
								positionY-=50;
								vline_pipe_01.setY(positionY);
								path.getElements().add(start_pipe_01);
								path.getElements().add(arc_pipe_01);
								path.getElements().add(vline_pipe_01);					
								ball_row--;
							}
							previousRow = gp.getRowIndex(tile.getRectangle());
							previousColumn = gp.getColumnIndex(tile.getRectangle());						
						}
						
						
						else if( ( type.equals("pipe") ) && ( prop.equals("00") )) {
							if(previousRow < gp.getRowIndex(tile.getRectangle())) {							
								MoveTo start_pipe_00 = new MoveTo();
								start_pipe_00.setX(positionX);
								start_pipe_00.setY(positionY);						
								ArcTo arc_pipe_00 = new ArcTo();
								positionX-=50;
								positionY+=50;
								arc_pipe_00.setX(positionX);
								arc_pipe_00.setY(positionY);			
								HLineTo hline_pipe_00 = new HLineTo();
								positionX-=50;
								hline_pipe_00.setX(positionX);
								path.getElements().add(start_pipe_00);
								path.getElements().add(arc_pipe_00);
								path.getElements().add(hline_pipe_00);	
								ball_column--;
							}
							
							if(previousColumn<gp.getColumnIndex(tile.getRectangle())) {	
								MoveTo start_pipe_00 = new MoveTo();
								start_pipe_00.setX(positionX);
								start_pipe_00.setY(positionY);			
								ArcTo arc_pipe_00 = new ArcTo();
								positionX+=50;
								positionY-=50;
								arc_pipe_00.setX(positionX);
								arc_pipe_00.setY(positionY);	
								VLineTo vline_pipe_00 = new VLineTo();
								positionY-=50;
								vline_pipe_00.setY(positionY);
								path.getElements().add(start_pipe_00);
								path.getElements().add(arc_pipe_00);
								path.getElements().add(vline_pipe_00);	
								ball_row--;
							}
							
							previousRow = gp.getRowIndex(tile.getRectangle());
							previousColumn = gp.getColumnIndex(tile.getRectangle());
								
						}
						
						else if( ( type.equals("pipe") ) && ( prop.equals("11") )) {
							if(previousRow > gp.getRowIndex(tile.getRectangle())) {
								
								MoveTo start_pipe_11 = new MoveTo();
								start_pipe_11.setX(positionX);
								start_pipe_11.setY(positionY);	
								ArcTo arc_pipe_11 = new ArcTo();
								positionX+=50;
								positionY-=50;
								arc_pipe_11.setX(positionX);
								arc_pipe_11.setY(positionY);
								HLineTo hline_pipe_11 = new HLineTo();
								positionX+=50;
								hline_pipe_11.setX(positionX);
								path.getElements().add(start_pipe_11);
								path.getElements().add(arc_pipe_11);
								path.getElements().add(hline_pipe_11);
								ball_column++;						
								
							}
							if(previousColumn>gp.getColumnIndex(tile.getRectangle())) {
								
								MoveTo start_pipe_11 = new MoveTo();
								start_pipe_11.setX(positionX);
								start_pipe_11.setY(positionY);
								ArcTo arc_pipe_11 = new ArcTo();
								positionX-=50;
								positionY+=50;
								arc_pipe_11.setX(positionX);
								arc_pipe_11.setY(positionY);
								VLineTo vline_pipe_11 = new VLineTo();
								positionY+=50;
								vline_pipe_11.setY(positionY);
								path.getElements().add(start_pipe_11);
								path.getElements().add(arc_pipe_11);
								path.getElements().add(vline_pipe_11);
								ball_row++;	
							}
							
							previousColumn = gp1.getColumnIndex(tile.getRectangle());
							previousRow =  gp1.getRowIndex(tile.getRectangle());	
						}
					
						else if( ( type.equals("pipestatic") ) && ( prop.equals("horizontal") )) {
							if(previousColumn<gp.getColumnIndex(tile.getRectangle())) {	
								MoveTo start_pipeStatic_horizontal = new MoveTo();
								start_pipeStatic_horizontal.setX(positionX);
								start_pipeStatic_horizontal.setY(positionY);
								HLineTo hline_pipeStatic_horizontal = new HLineTo();
								positionX+=150;
								hline_pipeStatic_horizontal.setX(positionX);
								path.getElements().add(start_pipeStatic_horizontal);
								path.getElements().add(hline_pipeStatic_horizontal);	
								ball_column++;				
							}
							else {		
								MoveTo start_pipeStatic_horizontal = new MoveTo();
								start_pipeStatic_horizontal.setX(positionX);
								start_pipeStatic_horizontal.setY(positionY);
								HLineTo hline_pipeStatic_horizontal = new HLineTo();
								positionX-=150;
								hline_pipeStatic_horizontal.setX(positionX);
								path.getElements().add(start_pipeStatic_horizontal);
								path.getElements().add(hline_pipeStatic_horizontal);	
								ball_column--;	
							}
							previousRow = gp.getRowIndex(tile.getRectangle());
							previousColumn = gp.getColumnIndex(tile.getRectangle());	
						}
						
						else if( ( type.equals("pipestatic") ) && ( prop.equals("vertical") )) {
							if(previousRow<gp.getRowIndex(tile.getRectangle())) {	
								MoveTo start_pipeStatic_vertical = new MoveTo();
								start_pipeStatic_vertical.setX(positionX);
								start_pipeStatic_vertical.setY(positionY);
								VLineTo vline_pipeStatic_vertical = new VLineTo();
								positionY+=150;
								vline_pipeStatic_vertical.setY(positionY);
								path.getElements().add(start_pipeStatic_vertical);
								path.getElements().add(vline_pipeStatic_vertical);
								ball_row++;								
							}
							else {		
								MoveTo start_pipeStatic_vertical = new MoveTo();
								start_pipeStatic_vertical.setX(positionX);
								start_pipeStatic_vertical.setY(positionY);
								VLineTo vline_pipeStatic_vertical = new VLineTo();
								positionY-=150;
								vline_pipeStatic_vertical.setY(positionY);
								path.getElements().add(start_pipeStatic_vertical);
								path.getElements().add(vline_pipeStatic_vertical);							
								ball_row--;		
							}
							previousRow = gp.getRowIndex(tile.getRectangle());
							previousColumn = gp.getColumnIndex(tile.getRectangle());
						}
						
						
						else if( ( type.equals("pipestatic") ) && ( prop.equals("00") )) {
							if(previousRow < gp.getRowIndex(tile.getRectangle())) {							
								MoveTo start_pipeStatic_00 = new MoveTo();
								start_pipeStatic_00.setX(positionX);
								start_pipeStatic_00.setY(positionY);						
								ArcTo arc_pipeStatic_00 = new ArcTo();
								positionX-=50;
								positionY+=50;
								arc_pipeStatic_00.setX(positionX);
								arc_pipeStatic_00.setY(positionY);			
								HLineTo hline_pipeStatic_00 = new HLineTo();
								positionX-=50;
								hline_pipeStatic_00.setX(positionX);
								path.getElements().add(start_pipeStatic_00);
								path.getElements().add(arc_pipeStatic_00);
								path.getElements().add(hline_pipeStatic_00);	
								ball_column--;
							}
							
							if(previousColumn<gp.getColumnIndex(tile.getRectangle())) {	
								MoveTo start_pipeStatic_00 = new MoveTo();
								start_pipeStatic_00.setX(positionX);
								start_pipeStatic_00.setY(positionY);			
								ArcTo arc_pipeStatic_00 = new ArcTo();
								positionX+=50;
								positionY-=50;
								arc_pipeStatic_00.setX(positionX);
								arc_pipeStatic_00.setY(positionY);	
								VLineTo vline_pipeStatic_00 = new VLineTo();
								positionY-=50;
								vline_pipeStatic_00.setY(positionY);
								path.getElements().add(start_pipeStatic_00);
								path.getElements().add(arc_pipeStatic_00);
								path.getElements().add(vline_pipeStatic_00);	
								ball_row--;
							}
							
							previousRow = gp.getRowIndex(tile.getRectangle());
							previousColumn = gp.getColumnIndex(tile.getRectangle());
						}
						
						
						else if( ( type.equals("pipestatic") ) && ( prop.equals("01") )) {
							if(previousRow < gp.getRowIndex(tile.getRectangle())) {
								
								MoveTo start_pipeStatic_01 = new MoveTo();
								start_pipeStatic_01.setX(positionX);
								start_pipeStatic_01.setY(positionY);	
								ArcTo arc_pipeStatic_01 = new ArcTo();
								positionX+=50;
								positionY+=50;
								arc_pipeStatic_01.setX(positionX);
								arc_pipeStatic_01.setY(positionY);	
								HLineTo hline_pipeStatic_01 = new HLineTo();
								positionX+=50;
								hline_pipeStatic_01.setX(positionX);
								path.getElements().add(start_pipeStatic_01);
								path.getElements().add(arc_pipeStatic_01);
								path.getElements().add(hline_pipeStatic_01);	
								ball_column++;			
							}
						
							if(previousColumn>gp.getColumnIndex(tile.getRectangle())) {							
								
								MoveTo start_pipeStatic_01 = new MoveTo();
								start_pipeStatic_01.setX(positionX);
								start_pipeStatic_01.setY(positionY);							
								ArcTo arc_pipeStatic_01 = new ArcTo();
								positionX-=50;
								positionY-=50;
								arc_pipeStatic_01.setX(positionX);
								arc_pipeStatic_01.setY(positionY);				
								VLineTo vline_pipeStatic_01 = new VLineTo();
								positionY-=50;
								vline_pipeStatic_01.setY(positionY);
								path.getElements().add(start_pipeStatic_01);
								path.getElements().add(arc_pipeStatic_01);
								path.getElements().add(vline_pipeStatic_01);					
								ball_row--;
							}
							previousRow = gp.getRowIndex(tile.getRectangle());
							previousColumn = gp.getColumnIndex(tile.getRectangle());
						}
						
						
						else if( ( type.equals("pipestatic") ) && ( prop.equals("10") )) {
							if(previousRow > gp.getRowIndex(tile.getRectangle())) {	
								MoveTo start_pipeStatic_10 = new MoveTo();
								start_pipeStatic_10.setX(positionX);
								start_pipeStatic_10.setY(positionY);	
								ArcTo arc_pipeStatic_10 = new ArcTo();
								positionX-=50;
								positionY-=50;
								arc_pipeStatic_10.setX(positionX);
								arc_pipeStatic_10.setY(positionY);
								HLineTo hline_pipeStatic_10 = new HLineTo();
								positionX-=50;
								hline_pipeStatic_10.setX(positionX);
								path.getElements().add(start_pipeStatic_10);
								path.getElements().add(arc_pipeStatic_10);
								path.getElements().add(hline_pipeStatic_10);		
								ball_column--;				
							}
							
							if(previousColumn<gp.getColumnIndex(tile.getRectangle())) {				
								MoveTo start_pipeStatic_10 = new MoveTo();
								start_pipeStatic_10.setX(positionX);
								start_pipeStatic_10.setY(positionY);						
								ArcTo arc_pipeStatic_10 = new ArcTo();
								positionX+=50;
								positionY+=50;
								arc_pipeStatic_10.setX(positionX);
								arc_pipeStatic_10.setY(positionY);	
								VLineTo vline_pipeStatic_10 = new VLineTo();
								positionY+=50;
								vline_pipeStatic_10.setY(positionY);
								path.getElements().add(start_pipeStatic_10);
								path.getElements().add(arc_pipeStatic_10);
								path.getElements().add(vline_pipeStatic_10);		
								ball_row++;
								}
								previousRow = gp.getRowIndex(tile.getRectangle());
								previousColumn = gp.getColumnIndex(tile.getRectangle());
						}
						
						
						
						else if( ( type.equals("pipestatic") ) && ( prop.equals("11") )) {
							if(previousRow > gp.getRowIndex(tile.getRectangle())) {
							MoveTo start_pipeStatic_11 = new MoveTo();
							start_pipeStatic_11.setX(positionX);
							start_pipeStatic_11.setY(positionY);	
							ArcTo arc_pipeStatic_11 = new ArcTo();
							positionX+=50;
							positionY-=50;
							arc_pipeStatic_11.setX(positionX);
							arc_pipeStatic_11.setY(positionY);
							HLineTo hline_pipeStatic_11 = new HLineTo();
							positionX+=50;
							hline_pipeStatic_11.setX(positionX);
							path.getElements().add(start_pipeStatic_11);
							path.getElements().add(arc_pipeStatic_11);
							path.getElements().add(hline_pipeStatic_11);
							ball_column++;						
							
						}
						if(previousColumn>gp.getColumnIndex(tile.getRectangle())) {
							
							MoveTo start_pipeStatic_11 = new MoveTo();
							start_pipeStatic_11.setX(positionX);
							start_pipeStatic_11.setY(positionY);
							ArcTo arc_pipeStatic_11 = new ArcTo();
							positionX-=50;
							positionY+=50;
							arc_pipeStatic_11.setX(positionX);
							arc_pipeStatic_11.setY(positionY);
							VLineTo vline_pipeStatic_11 = new VLineTo();
							positionY+=50;
							vline_pipeStatic_11.setY(positionY);
							path.getElements().add(start_pipeStatic_11);
							path.getElements().add(arc_pipeStatic_11);
							path.getElements().add(vline_pipeStatic_11);
							ball_row++;	
						}
						
						previousColumn = gp1.getColumnIndex(tile.getRectangle());
						previousRow =  gp1.getRowIndex(tile.getRectangle());	
						}
						
						else if( ( type.equals("empty") ) && ( prop.equals("free") )) {
							checker = false;
						}
						else if( ( type.equals("empty") ) && ( prop.equals("none") )) {
							checker = false;
						}
					}
				}
			}
//Setting transition
		transition.setNode(c1);
		transition.setPath(path);
		transition.setDuration(Duration.seconds(3));
		transition.play();
		
//Clearing the path and ending the level by using endLevel() method at the end of the animation.		
		transition.setOnFinished(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
					path.getElements().clear();
					endLevel(level);
			}
		});
}
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

//GETTERS AND SETERS

	public static ArrayList<Tile> getTile_list() {
		return tile_list;
	}
	public static void setTile_list(ArrayList<Tile> tile_list) {
		Main.tile_list = tile_list;
	}
	public static GridPane getGp() {
		return gp;
	}
	public static void setGp(GridPane gp) {
		Main.gp = gp;
	}
	public static Tile[][] getTile_array() {
		return tile_array;
	}
	public static void setTile_array(Tile[][] tile_array) {
		Main.tile_array = tile_array;
	}
	public static int getMoves() {
		return moves;
	}
	public static void setMoves(int moves) {
		Main.moves = moves;
	}
	public static int getLevel() {
		return level;
	}
	public static void setLevel(int level) {
		Main.level = level;
	}
	public static boolean isRead() {
		return isRead;
	}
	public static void setRead(boolean isRead) {
		Main.isRead = isRead;
	}
	public static Label getLblStatus() {
		return lblStatus;
	}
	public static void main(String[] args) {
		launch(args);
	}
	public static Circle getBall() {
		return ball;
	}
	public static void setBall(Circle ball) {
		Main.ball = ball;
	}
}