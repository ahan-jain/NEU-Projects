import java.util.ArrayList;
import java.util.Arrays;
import tester.*;
import javalib.impworld.*;
import java.awt.*;
import javalib.worldimages.*;
import java.util.Random;


//represents a grid
interface IGrid {
  
  int SCREEN_HEIGHT = 480; //height of the worldScene
  int SCREEN_WIDTH = 900; //width of the worldScene
  int CELL_SIZE = 25; //size of a cell in a grid
}

//represents a grid
class Grid extends World implements IGrid {
  
  ArrayList<ArrayList<Cell>> minesweeper;
  int mines;
  int x;
  int y;
  Utils u;
  int gameState;
  int clicks;
  
  /* 
   * USEFUL INFORMATION:
   * 
   * - Our x and y represent the rows and columns of the grid, i.e. its dimensions
   * 
   * - We keep track of the score in the form of the number of times 
   * a user clicked and revealed a new cell
   * 
   * - The gameState variable can have 4 different values:
   * 1. -2 -> it's on the menu screen
   * 2. -1 -> it's on a grid
   * 3. 0 -> lose screen
   * 4. 1 -> win screen
   * 
   */

  // constructor
  Grid(int mines, int x, int y) {

    this.minesweeper = new ArrayList<ArrayList<Cell>>();
    this.mines = mines;
    this.x = x;
    this.y = y;
    this.u = new Utils(new Random());
    this.clicks = 0;
    this.gameState = -2;

    for (int i = 0; i < this.x; i++) {
      this.minesweeper.add(new ArrayList<Cell>());
      for (int j = 0; j < this.y; j++) {
        this.minesweeper.get(i).add(new Cell());

      }

    }

    this.setMines();
    for (int i = 0; i < this.x; i++) {
      for (int j = 0; j < this.y; j++) {
        this.populateNeighbors(i, j);
      }
    }

  }

  // constructor for testing
  Grid(int mines, int x, int y, int seedValue, int clicks, int gameState) {
    this.minesweeper = new ArrayList<ArrayList<Cell>>();
    this.mines = mines;
    this.x = x;
    this.y = y;
    this.u = new Utils(new Random(seedValue));
    this.clicks = clicks;
    this.gameState = gameState;

    for (int i = 0; i < this.x; i++) {
      this.minesweeper.add(new ArrayList<Cell>());
      for (int j = 0; j < this.y; j++) {
        this.minesweeper.get(i).add(new Cell());

      }

    }

  }

  // randomly sets mines across the grid
  public void setMines() {

    int numberOfMines = 0;
    while (numberOfMines < this.mines) {
      if (!this.minesweeper.get(u.getRandomValue(x)).get(u.getRandomValue(y)).hasMine) {
        this.minesweeper.get(u.getRandomValue(x)).get(u.getRandomValue(y)).hasMine = true;
        numberOfMines++;
      }
    }
  }

  // gets the corresponding neighbors of all the cells in a list
  public void populateNeighbors(int r, int c) {

    for (int i = r - 1; i <= r + 1; i++) {
      for (int j = c - 1; j <= c + 1; j++) {
        if (i != r || j != c) {
          if (i < this.minesweeper.size() && j < this.minesweeper.get(0).size() && i >= 0
              && j >= 0) {
            this.minesweeper.get(r).get(c).neighbors.add(this.minesweeper.get(i).get(j));
          }
        }
      }
    }

  }

  // draws the current state of the game
  public WorldScene makeScene() {

    WorldScene world = new WorldScene(SCREEN_WIDTH, SCREEN_HEIGHT);

    // if game is won or lost
    if (this.gameState > -1) {

      // if lost
      if (this.gameState == 0) {

        world.placeImageXY(new RectangleImage(SCREEN_WIDTH, SCREEN_HEIGHT, "solid", Color.BLACK),
            SCREEN_WIDTH / 2, SCREEN_HEIGHT / 2);
        world.placeImageXY(new TextImage("GAME OVER", 24, FontStyle.BOLD, Color.WHITE),
            SCREEN_WIDTH / 2, (SCREEN_HEIGHT / 2) - 20);
        world.placeImageXY(new TextImage("Number of times clicked: " + this.clicks, 20,
            FontStyle.BOLD, Color.WHITE), SCREEN_WIDTH / 2, (SCREEN_HEIGHT / 2) + 15);
        world.placeImageXY(new TextImage("(click to restart)", 18, FontStyle.BOLD, Color.WHITE),
            SCREEN_WIDTH / 2, (SCREEN_HEIGHT / 2) + 50);
      }

      // if won
      else {

        world.placeImageXY(new RectangleImage(SCREEN_WIDTH, SCREEN_HEIGHT, "solid", Color.BLACK),
            SCREEN_WIDTH / 2, SCREEN_HEIGHT / 2);
        world.placeImageXY(new TextImage("YOU WON!", 24, FontStyle.BOLD, Color.WHITE),
            SCREEN_WIDTH / 2, (SCREEN_HEIGHT / 2) - 20);
        world.placeImageXY(new TextImage("Number of times clicked: " + this.clicks, 20,
            FontStyle.BOLD, Color.WHITE), SCREEN_WIDTH / 2, (SCREEN_HEIGHT / 2) + 15);
        world.placeImageXY(new TextImage("(click to restart)", 18, FontStyle.BOLD, Color.WHITE),
            SCREEN_WIDTH / 2, (SCREEN_HEIGHT / 2) + 50);
      }
      return world;
    }

    // intro scene
    else if (this.gameState == -2) {
      world.placeImageXY(new RectangleImage(SCREEN_WIDTH, SCREEN_HEIGHT, "solid", Color.BLACK), 450,
          240);
      world.placeImageXY(new TextImage("Welcome to Minesweeper!", 24, FontStyle.BOLD, Color.WHITE),
          SCREEN_WIDTH / 2, (SCREEN_HEIGHT / 2) - 20);
      world.placeImageXY(new TextImage("(press 1 for easy, 2 for medium, 3 for hard)", 18,
          FontStyle.BOLD, Color.WHITE), SCREEN_WIDTH / 2, (SCREEN_HEIGHT / 2) + 15);

      return world;
    }

    // if game isn't won or lost
    else {

      int xCoord = 10;

      ArrayList<ArrayList<Cell>> grid = this.minesweeper;

      for (ArrayList<Cell> row : grid) {
        int yCoord = 10;
        for (Cell column : row) {

          world.placeImageXY(column.draw(), xCoord, yCoord);
          yCoord = yCoord + CELL_SIZE;

        }

        xCoord = xCoord + CELL_SIZE;

      }

      world.placeImageXY(
          new TextImage("Number of clicks: " + Integer.toString(clicks), 20, Color.BLACK), 100,
          450);

      return world;
    }
  }

  // monitors mouse inputs and enforces flood fill for left clicks and flags with right clicks
  public void onMouseClicked(Posn pos, String buttonName) {

    if (this.gameState > -1) {

      if (buttonName.equals("LeftButton")) {

        this.clicks = 0;
        this.minesweeper = new Grid(this.mines, this.x, this.y).minesweeper;
        this.gameState = -2;
      }
    }

    if ((pos.x / CELL_SIZE) < this.x && (pos.y / CELL_SIZE) < this.y && this.gameState == -1) {

      Cell target = this.minesweeper.get(pos.x / CELL_SIZE).get(pos.y / CELL_SIZE);
      
      if (this.minesweeper.get(pos.x / CELL_SIZE).get(pos.y / CELL_SIZE).unknownChecker()) {
        
        target.updateOnClick(buttonName);
        
        if (buttonName.equals("LeftButton") && !target.flagChecker()) {
          clicks++;
          
        }
        

      }


      //checks for lose condition
      if (target.mineChecker() && !(target.unknownChecker())) {

        this.gameState = 0;

      }

      //checks for win condition
      int revealedTiles = 0;

      for (ArrayList<Cell> row : this.minesweeper) {
        for (Cell column : row) {
          if (!column.unknownChecker()) {
            revealedTiles++;

          }

        }

      }
      
      if (revealedTiles == ((x * y) - mines)) {
        this.gameState = 1;

      }

    }

  }

  //monitors key events during the intro scene to choose difficulty of the game
  public void onKeyEvent(String key) {
    if (this.gameState == -2) {

      if (key.equals("1")) {
        this.gameState = -1;
        this.x = 9;
        this.y = 9;
        this.mines = 10;
        this.minesweeper = new Grid(this.mines, this.x, this.y).minesweeper;

      }
      if (key.equals("2")) {
        this.gameState = -1;
        this.x = 16;
        this.y = 16;
        this.mines = 40;
        this.minesweeper = new Grid(this.mines, this.x, this.y).minesweeper;

      }
      if (key.equals("3")) {
        this.gameState = -1;
        this.x = 30;
        this.y = 16;
        this.mines = 99;
        this.minesweeper = new Grid(this.mines, this.x, this.y).minesweeper;

      }

    }

  }
}

//represents a cell
class Cell implements IGrid {
  boolean unknown;
  boolean isFlagged;
  boolean hasMine;
  ArrayList<Cell> neighbors;

  // Constructor
  Cell() {

    this.unknown = true;
    this.hasMine = false;
    this.isFlagged = false;
    this.neighbors = new ArrayList<Cell>(); // Initialize with an empty list
  }

  // constructor for testing
  Cell(boolean unknown, boolean isFlagged, boolean hasMine, ArrayList<Cell> neighbors) {

    this.unknown = unknown;
    this.hasMine = hasMine;
    this.isFlagged = isFlagged;
    this.neighbors = neighbors; // Initialize with an empty list
  }

  // counts the total number of mines a cell's neighbors have
  public int countMines() {
    int count = 0;
    for (int i = 0; i < this.neighbors.size(); i++) {
      if (this.neighbors.get(i).hasMine) {
        count++;

      }
    }
    return count;

  }

  // draws a cell
  public WorldImage draw() {
    WorldImage tile = new OverlayImage(new RectangleImage(CELL_SIZE, CELL_SIZE, 
        OutlineMode.OUTLINE, Color.BLACK),
        new RectangleImage(CELL_SIZE, CELL_SIZE, OutlineMode.SOLID, Color.CYAN));

    if (this.unknown) {
      if (this.isFlagged) {
        return new OverlayImage(new CircleImage(10, OutlineMode.SOLID, Color.BLUE), tile);
      }

      else {

        return tile;
      }

    }
    
    if (this.hasMine) {
      return new OverlayImage(new StarImage(10, 9, OutlineMode.SOLID, Color.RED), tile);
    }
    
    //all the different mine configurations
    if (this.countMines() > 0) {
      
      if (this.countMines() == 1) {
        return new OverlayImage(new TextImage(Integer.toString(this.countMines()), Color.BLUE),
            new OverlayImage(
                new RectangleImage(CELL_SIZE, CELL_SIZE, OutlineMode.OUTLINE, Color.BLACK),
                new RectangleImage(CELL_SIZE, CELL_SIZE, OutlineMode.SOLID, Color.GRAY)));
      }
      
      else if (this.countMines() == 2) {
        return new OverlayImage(new TextImage(Integer.toString(this.countMines()), Color.GREEN),
            new OverlayImage(new RectangleImage(CELL_SIZE, CELL_SIZE, 
                OutlineMode.OUTLINE, Color.BLACK),
                new RectangleImage(CELL_SIZE, CELL_SIZE, OutlineMode.SOLID, Color.GRAY)));
      }
      
      else if (this.countMines() == 3) {
        return new OverlayImage(new TextImage(Integer.toString(this.countMines()), Color.RED),
            new OverlayImage(new RectangleImage(CELL_SIZE, CELL_SIZE, 
                OutlineMode.OUTLINE, Color.BLACK),
                new RectangleImage(CELL_SIZE, CELL_SIZE, OutlineMode.SOLID, Color.GRAY)));
      }

      else if (this.countMines() == 4) {
        return new OverlayImage(new TextImage(Integer.toString(this.countMines()), Color.YELLOW),
            new OverlayImage(
                new RectangleImage(CELL_SIZE, CELL_SIZE, OutlineMode.OUTLINE, Color.BLACK),
                new RectangleImage(CELL_SIZE, CELL_SIZE, OutlineMode.SOLID, Color.GRAY)));
      }

      else if (this.countMines() == 5) {
        return new OverlayImage(new TextImage(Integer.toString(this.countMines()), Color.ORANGE),
            new OverlayImage(
                new RectangleImage(CELL_SIZE, CELL_SIZE, OutlineMode.OUTLINE, Color.BLACK),
                new RectangleImage(CELL_SIZE, CELL_SIZE, OutlineMode.SOLID, Color.GRAY)));
      }

      else if (this.countMines() == 6) {
        return new OverlayImage(new TextImage(Integer.toString(this.countMines()), Color.WHITE),
            new OverlayImage(
                new RectangleImage(CELL_SIZE, CELL_SIZE, OutlineMode.OUTLINE, Color.BLACK),
                new RectangleImage(CELL_SIZE, CELL_SIZE, OutlineMode.SOLID, Color.GRAY)));
      }

      else if (this.countMines() == 7) {
        return new OverlayImage(new TextImage(Integer.toString(this.countMines()), Color.PINK),
            new OverlayImage(
                new RectangleImage(CELL_SIZE, CELL_SIZE, OutlineMode.OUTLINE, Color.BLACK),
                new RectangleImage(CELL_SIZE, CELL_SIZE, OutlineMode.SOLID, Color.GRAY)));
      }

      else if (this.countMines() == 8) {
        return new OverlayImage(new TextImage(Integer.toString(this.countMines()), Color.BLACK),
            new OverlayImage(
                new RectangleImage(CELL_SIZE, CELL_SIZE, OutlineMode.OUTLINE, Color.BLACK),
                new RectangleImage(CELL_SIZE, CELL_SIZE, OutlineMode.SOLID, Color.GRAY)));
      }

      else {
        return tile; // it's not possible for a cell to have more than 8 neighboring mines
      }
      
      
      

    }
    
    else {
      return new OverlayImage(new RectangleImage(CELL_SIZE, CELL_SIZE, 
          OutlineMode.OUTLINE, Color.BLACK),
          new RectangleImage(CELL_SIZE, CELL_SIZE, OutlineMode.SOLID, Color.GRAY));
    }
    


  }

  //enforces the flood fill functionality of minesweeper in a cell and its neighbors
  public void floodFill() {
    if (this.countMines() == 0 && !this.hasMine && this.unknown && !this.isFlagged) {

      this.unknown = false;

      for (Cell neighbor : this.neighbors) {
        neighbor.floodFill();
      }

    }
    else {
      if (!this.isFlagged) {
        this.unknown = false;

      }

    }

  }

  
  //updates a cell based on right or left click: it flags/unflags an unrevealed cell 
  //if its a right click and enforces flood fill on an unrevealed cell if left click
  public void updateOnClick(String button) {

    if (button.equals("RightButton")) {
      this.isFlagged = !(this.isFlagged);

    }
    else if (button.equals("LeftButton") && !this.isFlagged) {

      this.floodFill();

    }

  }

  //checks if a cell has a mine
  public boolean mineChecker() {
    return this.hasMine;
  }

  //checks if a cell us unknown
  public boolean unknownChecker() {
    return this.unknown;
  }
  
  //checks if a cell is flagged
  public boolean flagChecker() {
    return this.isFlagged;
  }
}

//utils class
class Utils {

  Random rand;

  Utils() {

    this(new Random());
  }

  Utils(Random rand) {

    this.rand = rand;
  }

  // gets random value upto the given number
  public int getRandomValue(int dim) {

    return Math.round(rand.nextInt(dim));

  }
}

//Examples and tests
class ExamplesMinesweeper {

  Cell numberCell1;
  Cell numberCell2;
  Cell numberCell3;
  Cell numberCell4;
  Cell numberCell5;
  Cell numberCell6;
  Cell numberCell7;
  Cell numberCell8;
  Cell numberCell9;

  Cell numberCell12;
  Cell numberCell22;
  Cell numberCell32;
  Cell numberCell42;
  Cell numberCell52;
  Cell numberCell62;
  Cell numberCell72;
  Cell numberCell82;
  Cell numberCell92;

  Cell numberCell1U;
  Cell numberCell2U;
  Cell numberCell3U;
  Cell numberCell4U;
  Cell mineCell5U;
  Cell numberCell6U;
  Cell numberCell7U;
  Cell numberCell8U;
  Cell numberCell9U;

  Cell flagCell;
  Cell mineCell1;
  Cell mineCell2;
  Cell mineCell3;
  Cell mineCell4;

  Cell flagCell2;
  Cell mineCell12;
  Cell mineCell22;
  Cell mineCell32;
  Cell mineCell42;

  Cell flagCellU;
  Cell mineCell1U;
  Cell mineCell2U;
  Cell mineCell3U;
  Cell mineCell4U;

  Cell flagCell0U;
  Cell mineCell11U;
  Cell mineCell22U;
  Cell mineCell33U;
  Cell mineCell44U;

  ArrayList<Cell> row1;
  ArrayList<Cell> row2;
  ArrayList<Cell> row3;
  ArrayList<Cell> row4;
  ArrayList<Cell> row5;
  ArrayList<Cell> row6;

  ArrayList<Cell> row12;
  ArrayList<Cell> row22;
  ArrayList<Cell> row32;
  ArrayList<Cell> row42;
  ArrayList<Cell> row52;
  ArrayList<Cell> row62;

  ArrayList<Cell> row1U;
  ArrayList<Cell> row2U;
  ArrayList<Cell> row3U;
  ArrayList<Cell> row4U;
  ArrayList<Cell> row5U;
  ArrayList<Cell> row6U;

  ArrayList<ArrayList<Cell>> testMinesweeper;
  ArrayList<ArrayList<Cell>> testMinesweeper2;
  ArrayList<ArrayList<Cell>> testMinesweeper3;
  ArrayList<ArrayList<Cell>> testMinesweeper4;

  ArrayList<ArrayList<Cell>> testMinesweeperU;
  ArrayList<ArrayList<Cell>> testMinesweeper2U;
  ArrayList<ArrayList<Cell>> testMinesweeper3U;
  ArrayList<ArrayList<Cell>> testMinesweeper4U;
  Grid testGrid;
  Grid testGrid2;

  // initalises values
  void reset() {
    numberCell1 = new Cell();
    numberCell2 = new Cell();
    numberCell3 = new Cell();
    numberCell4 = new Cell();
    numberCell5 = new Cell();
    numberCell6 = new Cell();
    numberCell7 = new Cell();
    numberCell8 = new Cell();
    numberCell9 = new Cell();

    flagCell = new Cell(true, true, false, new ArrayList<Cell>());
    mineCell1 = new Cell(true, false, true, new ArrayList<Cell>());
    mineCell2 = new Cell(true, false, true, new ArrayList<Cell>());
    mineCell3 = new Cell(true, false, true, new ArrayList<Cell>());
    mineCell4 = new Cell(true, false, true, new ArrayList<Cell>());

    numberCell12 = new Cell();
    numberCell22 = new Cell();
    numberCell32 = new Cell();
    numberCell42 = new Cell();
    numberCell52 = new Cell();
    numberCell62 = new Cell();
    numberCell72 = new Cell();
    numberCell82 = new Cell();
    numberCell92 = new Cell();

    flagCell2 = new Cell(true, true, false, new ArrayList<Cell>());
    mineCell12 = new Cell(true, false, true, new ArrayList<Cell>());
    mineCell22 = new Cell(true, false, true, new ArrayList<Cell>());
    mineCell32 = new Cell(true, false, true, new ArrayList<Cell>());
    mineCell42 = new Cell(true, false, true, new ArrayList<Cell>());

    row1 = new ArrayList<Cell>(Arrays.asList(numberCell1, numberCell2, numberCell3));
    row2 = new ArrayList<Cell>(Arrays.asList(numberCell4, flagCell, mineCell1));
    row3 = new ArrayList<Cell>(Arrays.asList(mineCell2, mineCell3, mineCell4));

    row4 = new ArrayList<Cell>(Arrays.asList(numberCell1, numberCell2, numberCell3));
    row5 = new ArrayList<Cell>(Arrays.asList(numberCell4, numberCell5, numberCell6));
    row6 = new ArrayList<Cell>(Arrays.asList(numberCell7, numberCell8, numberCell9));

    row12 = new ArrayList<Cell>(Arrays.asList(numberCell12, numberCell22, numberCell32));
    row22 = new ArrayList<Cell>(Arrays.asList(numberCell42, flagCell2, mineCell12));
    row32 = new ArrayList<Cell>(Arrays.asList(mineCell22, mineCell32, mineCell42));

    row42 = new ArrayList<Cell>(Arrays.asList(numberCell12, numberCell22, numberCell32));
    row52 = new ArrayList<Cell>(Arrays.asList(numberCell42, numberCell52, numberCell62));
    row62 = new ArrayList<Cell>(Arrays.asList(numberCell72, numberCell82, numberCell92));

    testMinesweeper = new ArrayList<ArrayList<Cell>>(Arrays.asList(row1, row2, row3));
    testMinesweeper2 = new ArrayList<ArrayList<Cell>>(Arrays.asList(row4, row5, row6));

    testMinesweeper3 = new ArrayList<ArrayList<Cell>>(Arrays.asList(row12, row22, row32));
    testMinesweeper4 = new ArrayList<ArrayList<Cell>>(Arrays.asList(row42, row52, row62));

    numberCell1U = new Cell(false, false, false, new ArrayList<Cell>());
    numberCell2U = new Cell(false, false, false, new ArrayList<Cell>());
    numberCell3U = new Cell(false, false, false, new ArrayList<Cell>());
    numberCell4U = new Cell(false, false, false, new ArrayList<Cell>());
    mineCell5U = new Cell(true, true, true, new ArrayList<Cell>());
    numberCell6U = new Cell(false, false, false, new ArrayList<Cell>());
    numberCell7U = new Cell(false, false, false, new ArrayList<Cell>());
    numberCell8U = new Cell(false, false, false, new ArrayList<Cell>());
    numberCell9U = new Cell(false, false, false, new ArrayList<Cell>());

    flagCellU = new Cell(true, true, false, new ArrayList<Cell>());
    mineCell1U = new Cell(false, false, true, new ArrayList<Cell>());
    mineCell2U = new Cell(false, false, true, new ArrayList<Cell>());
    mineCell3U = new Cell(false, false, true, new ArrayList<Cell>());
    mineCell4U = new Cell(false, false, true, new ArrayList<Cell>());

    row1U = new ArrayList<Cell>(Arrays.asList(numberCell1U, numberCell2U, numberCell3U));
    row2U = new ArrayList<Cell>(Arrays.asList(numberCell4U, flagCellU, mineCell1U));
    row3U = new ArrayList<Cell>(Arrays.asList(numberCell7U, numberCell8U, mineCell4U));

    row4U = new ArrayList<Cell>(Arrays.asList(numberCell1U, numberCell2U, numberCell3U));
    row5U = new ArrayList<Cell>(Arrays.asList(numberCell4U, mineCell5U, numberCell6U));
    row6U = new ArrayList<Cell>(Arrays.asList(numberCell7U, numberCell8U, numberCell9U));

    testMinesweeperU = new ArrayList<ArrayList<Cell>>(Arrays.asList(row1U, row2U, row3U));
    testMinesweeper2U = new ArrayList<ArrayList<Cell>>(Arrays.asList(row4U, row5U, row6U));

    testMinesweeper3U = new ArrayList<ArrayList<Cell>>(Arrays.asList(row1U, row2U, row3U));
    testMinesweeper4U = new ArrayList<ArrayList<Cell>>(Arrays.asList(row4U, row5U, row6U));

  }

  // tests the populateNeighbors
  void testPopulateNeighbors(Tester t) {
    reset();
    Grid grid1 = new Grid(4, 3, 3, 2, 0, -1);
    Grid grid2 = new Grid(4, 3, 3, 2, 0, -1);
    grid1.minesweeper = testMinesweeper;
    grid2.minesweeper = testMinesweeper3;

    t.checkExpect(grid1, grid2);
    grid1.populateNeighbors(2, 2);
    t.checkFail(grid1, grid2); // only grid1 is populated right now

    for (int i = 2 - 1; i <= 2 + 1; i++) {
      for (int j = 2 - 1; j <= 2 + 1; j++) {
        if (i != 2 || j != 2) {
          if (i < grid2.minesweeper.size() && j < grid2.minesweeper.get(0).size() && i >= 0
              && j >= 0) {
            grid2.minesweeper.get(2).get(2).neighbors.add(grid2.minesweeper.get(i).get(j));
          }
        }
      }
    }
    t.checkExpect(grid1, grid2); // both are equal now

    reset();
    grid1.populateNeighbors(0, 1);
    t.checkFail(grid1, grid2); // grid1 is only partially populated (in the edge)
    // whereas is grid2 is not populated
    for (int i = 0 - 1; i <= 0 + 1; i++) {
      for (int j = 1 - 1; j <= 1 + 1; j++) {
        if (i != 0 || j != 1) {
          if (i < grid2.minesweeper.size() && j < grid2.minesweeper.get(0).size() && i >= 0
              && j >= 0) {
            grid2.minesweeper.get(0).get(1).neighbors.add(grid2.minesweeper.get(i).get(j));
          }
        }
      }
    }
    t.checkExpect(grid1, grid2); // both are equal now

    reset();
    grid1.populateNeighbors(2, 0); // grid1 is only partially populated (in the corner)
    // whereas is grid2 is not populated
    for (int i = 2 - 1; i <= 2 + 1; i++) {
      for (int j = 0 - 1; j <= 0 + 1; j++) {
        if (i != 2 || j != 0) {
          if (i < grid2.minesweeper.size() && j < grid2.minesweeper.get(0).size() && i >= 0
              && j >= 0) {
            grid2.minesweeper.get(2).get(0).neighbors.add(grid2.minesweeper.get(i).get(j));
          }
        }
      }
    }
    t.checkExpect(grid1, grid2); // both are equal now

  }

  // tests the makeScene method
  void testMakeScene(Tester t) {

    // example 1 - partially discovered grid with 2 mines
    reset();
    Grid grid1 = new Grid(2, 3, 3, 2, 0, -1);
    grid1.minesweeper = testMinesweeperU;
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 3; j++) {
        grid1.populateNeighbors(i, j);
      }
    }

    reset();
    Grid grid2 = new Grid(2, 3, 3, 2, 0, -1);
    grid2.minesweeper = testMinesweeper3U;
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 3; j++) {
        grid2.populateNeighbors(i, j);
      }
    }

    int xCoord = 10;

    ArrayList<ArrayList<Cell>> grid = grid2.minesweeper;

    WorldScene world = new WorldScene(900, 480);
    
    for (ArrayList<Cell> row : grid) {
      int yCoord = 10;
      for (Cell column : row) {

        world.placeImageXY(column.draw(), xCoord, yCoord);
        yCoord = yCoord + 25;

      }

      xCoord = xCoord + 25;

    }
    
    world.placeImageXY(
        new TextImage("Number of clicks: " + Integer.toString(0), 20, Color.BLACK), 100,
        450);
    
    t.checkExpect(grid1.makeScene(), world);

    // example 2 - one unrevealed mine surrounded by neighboring cells
    reset();

    Grid grid3 = new Grid(4, 3, 3, 2, 0, -1);
    grid3.minesweeper = testMinesweeper2U;
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 3; j++) {
        grid3.populateNeighbors(i, j);
      }
    }

    reset();
    Grid grid4 = new Grid(4, 3, 3, 2, 0, -1);
    grid4.minesweeper = testMinesweeper4U;
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 3; j++) {
        grid4.populateNeighbors(i, j);
      }
    }

    xCoord = 10;

    grid = grid3.minesweeper;

    world = new WorldScene(900, 480);
    for (ArrayList<Cell> row : grid) {
      int yCoord = 10;
      for (Cell column : row) {

        world.placeImageXY(column.draw(), xCoord, yCoord);
        yCoord = yCoord + 25;

      }

      xCoord = xCoord + 25;

    }
    
    world.placeImageXY(
        new TextImage("Number of clicks: " + Integer.toString(0), 20, Color.BLACK), 100,
        450);
    t.checkExpect(grid4.makeScene(), world);
    

    // example 3 - one mine in the corner and otherwise fully discovered grid

    numberCell1 = new Cell(true, false, true, new ArrayList<Cell>());
    numberCell2 = new Cell(false, false, false, new ArrayList<Cell>());
    numberCell3 = new Cell(false, false, false, new ArrayList<Cell>());
    numberCell4 = new Cell(false, false, false, new ArrayList<Cell>());
    numberCell5 = new Cell(false, false, false, new ArrayList<Cell>());
    numberCell6 = new Cell(false, false, false, new ArrayList<Cell>());
    numberCell7 = new Cell(false, false, false, new ArrayList<Cell>());
    numberCell8 = new Cell(false, false, false, new ArrayList<Cell>());
    numberCell9 = new Cell(false, false, false, new ArrayList<Cell>());

    row4 = new ArrayList<Cell>(Arrays.asList(numberCell1, numberCell2, numberCell3));
    row5 = new ArrayList<Cell>(Arrays.asList(numberCell4, numberCell5, numberCell6));
    row6 = new ArrayList<Cell>(Arrays.asList(numberCell7, numberCell8, numberCell9));
    testMinesweeper = new ArrayList<ArrayList<Cell>>(Arrays.asList(row4, row5, row6));
    grid1 = new Grid(1, 3, 3, 2, 0, -1);
    grid1.minesweeper = testMinesweeper;
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 3; j++) {
        grid1.populateNeighbors(i, j);
      }
    }

    grid = grid1.minesweeper;
    xCoord = 10;

    world = new WorldScene(900, 480);
    for (ArrayList<Cell> row : grid) {
      int yCoord = 10;
      for (Cell column : row) {

        world.placeImageXY(column.draw(), xCoord, yCoord);
        yCoord = yCoord + 25;

      }

      xCoord = xCoord + 25;

    }

    numberCell1 = new Cell(true, false, true, new ArrayList<Cell>());
    numberCell2 = new Cell(false, false, false, new ArrayList<Cell>());
    numberCell3 = new Cell(false, false, false, new ArrayList<Cell>());
    numberCell4 = new Cell(false, false, false, new ArrayList<Cell>());
    numberCell5 = new Cell(false, false, false, new ArrayList<Cell>());
    numberCell6 = new Cell(false, false, false, new ArrayList<Cell>());
    numberCell7 = new Cell(false, false, false, new ArrayList<Cell>());
    numberCell8 = new Cell(false, false, false, new ArrayList<Cell>());
    numberCell9 = new Cell(false, false, false, new ArrayList<Cell>());

    row4 = new ArrayList<Cell>(Arrays.asList(numberCell1, numberCell2, numberCell3));
    row5 = new ArrayList<Cell>(Arrays.asList(numberCell4, numberCell5, numberCell6));
    row6 = new ArrayList<Cell>(Arrays.asList(numberCell7, numberCell8, numberCell9));
    testMinesweeper2 = new ArrayList<ArrayList<Cell>>(Arrays.asList(row4, row5, row6));

    grid2 = new Grid(1, 3, 3, 2, 0, -1);
    grid2.minesweeper = testMinesweeper2;
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 3; j++) {
        grid2.populateNeighbors(i, j);
      }
    }
    
    world.placeImageXY(
        new TextImage("Number of clicks: " + Integer.toString(0), 20, Color.BLACK), 100,
        450);
    t.checkExpect(grid2.makeScene(), world);


    // example 4 - no mines
    reset();

    numberCell1 = new Cell(false, false, false, new ArrayList<Cell>());
    numberCell2 = new Cell(false, false, false, new ArrayList<Cell>());
    numberCell3 = new Cell(false, false, false, new ArrayList<Cell>());
    numberCell4 = new Cell(false, false, false, new ArrayList<Cell>());
    numberCell5 = new Cell(false, false, false, new ArrayList<Cell>());
    numberCell6 = new Cell(false, false, false, new ArrayList<Cell>());
    numberCell7 = new Cell(false, false, false, new ArrayList<Cell>());
    numberCell8 = new Cell(false, false, false, new ArrayList<Cell>());
    numberCell9 = new Cell(false, false, false, new ArrayList<Cell>());

    row4 = new ArrayList<Cell>(Arrays.asList(numberCell1, numberCell2, numberCell3));
    row5 = new ArrayList<Cell>(Arrays.asList(numberCell4, numberCell5, numberCell6));
    row6 = new ArrayList<Cell>(Arrays.asList(numberCell7, numberCell8, numberCell9));
    testMinesweeper = new ArrayList<ArrayList<Cell>>(Arrays.asList(row4, row5, row6));
    grid1 = new Grid(0, 3, 3, 2, 0, -1);
    grid1.minesweeper = testMinesweeper;
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 3; j++) {
        grid1.populateNeighbors(i, j);
      }
    }
    grid = grid1.minesweeper;
    xCoord = 10;

    world = new WorldScene(900, 480);
    for (ArrayList<Cell> row : grid) {
      int yCoord = 10;
      for (Cell column : row) {

        world.placeImageXY(column.draw(), xCoord, yCoord);
        yCoord = yCoord + 25;

      }

      xCoord = xCoord + 25;

    }

    //tests the intro scene
    reset();
    numberCell1 = new Cell(false, false, false, new ArrayList<Cell>());
    numberCell2 = new Cell(false, false, false, new ArrayList<Cell>());
    numberCell3 = new Cell(false, false, false, new ArrayList<Cell>());
    numberCell4 = new Cell(false, false, false, new ArrayList<Cell>());
    numberCell5 = new Cell(false, false, false, new ArrayList<Cell>());
    numberCell6 = new Cell(false, false, false, new ArrayList<Cell>());
    numberCell7 = new Cell(false, false, false, new ArrayList<Cell>());
    numberCell8 = new Cell(false, false, false, new ArrayList<Cell>());
    numberCell9 = new Cell(false, false, false, new ArrayList<Cell>());

    row4 = new ArrayList<Cell>(Arrays.asList(numberCell1, numberCell2, numberCell3));
    row5 = new ArrayList<Cell>(Arrays.asList(numberCell4, numberCell5, numberCell6));
    row6 = new ArrayList<Cell>(Arrays.asList(numberCell7, numberCell8, numberCell9));
    testMinesweeper2 = new ArrayList<ArrayList<Cell>>(Arrays.asList(row4, row5, row6));
    grid2 = new Grid(0, 3, 3, 2, 0, -2);
    grid2.minesweeper = testMinesweeper2;
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 3; j++) {
        grid2.populateNeighbors(i, j);
      }
    }
    
    world.placeImageXY(new RectangleImage(900, 480, "solid", Color.BLACK), 450, 240);
    world.placeImageXY(new TextImage("Welcome to Minesweeper!", 24, FontStyle.BOLD, Color.WHITE),
        450, 220);
    world.placeImageXY(new TextImage("(press 1 for easy, 2 for medium, 3 for hard)", 18,
        FontStyle.BOLD, Color.WHITE), 450, 255);
    
    t.checkExpect(grid2.makeScene(), world);
    
    //tests the win screen
    reset();

    grid2 = new Grid(0, 3, 3, 2, 0, 1);

    world.placeImageXY(new RectangleImage(900, 480, "solid", Color.BLACK), 450, 240);
    world.placeImageXY(new TextImage("YOU WON!", 24, FontStyle.BOLD, Color.WHITE), 450, 220);
    world.placeImageXY(new TextImage("Number of times clicked: " + 0, 20,
        FontStyle.BOLD, Color.WHITE), 450, 255);
    world.placeImageXY(new TextImage("(click to restart)", 18, FontStyle.BOLD, Color.WHITE),
        450, 290);
    
    t.checkExpect(grid2.makeScene(), world);
    
    
    //tests the game over screen
    reset();
    numberCell1 = new Cell(false, false, false, new ArrayList<Cell>());
    numberCell2 = new Cell(false, false, false, new ArrayList<Cell>());
    numberCell3 = new Cell(false, false, false, new ArrayList<Cell>());
    numberCell4 = new Cell(false, false, false, new ArrayList<Cell>());
    numberCell5 = new Cell(false, false, false, new ArrayList<Cell>());
    numberCell6 = new Cell(false, false, false, new ArrayList<Cell>());
    numberCell7 = new Cell(false, false, false, new ArrayList<Cell>());
    numberCell8 = new Cell(false, false, false, new ArrayList<Cell>());
    numberCell9 = new Cell(false, false, false, new ArrayList<Cell>());

    row4 = new ArrayList<Cell>(Arrays.asList(numberCell1, numberCell2, numberCell3));
    row5 = new ArrayList<Cell>(Arrays.asList(numberCell4, numberCell5, numberCell6));
    row6 = new ArrayList<Cell>(Arrays.asList(numberCell7, numberCell8, numberCell9));
    testMinesweeper2 = new ArrayList<ArrayList<Cell>>(Arrays.asList(row4, row5, row6));
    grid2 = new Grid(0, 3, 3, 2, 0, 0);
    grid2.minesweeper = testMinesweeper2;
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 3; j++) {
        grid2.populateNeighbors(i, j);
      }
    }
    world.placeImageXY(new RectangleImage(900, 480, "solid", Color.BLACK), 450, 240);
    world.placeImageXY(new TextImage("GAME OVER", 24, FontStyle.BOLD, Color.WHITE), 450, 220);
    world.placeImageXY(new TextImage("Number of times clicked: " + 0, 20,
        FontStyle.BOLD, Color.WHITE), 450, 255);
    world.placeImageXY(new TextImage("(click to restart)", 18, FontStyle.BOLD, Color.WHITE),
        450, 290);
    
    t.checkExpect(grid2.makeScene(), world);
    
    // tests different possible mine colors

    numberCell1 = new Cell(true, false, true, new ArrayList<Cell>());
    numberCell2 = new Cell(false, false, false, new ArrayList<Cell>());
    numberCell3 = new Cell(false, false, false, new ArrayList<Cell>());
    numberCell4 = new Cell(false, false, true, new ArrayList<Cell>());
    numberCell5 = new Cell(false, false, true, new ArrayList<Cell>());
    numberCell6 = new Cell(false, false, false, new ArrayList<Cell>());
    numberCell7 = new Cell(false, false, false, new ArrayList<Cell>());
    numberCell8 = new Cell(false, false, false, new ArrayList<Cell>());
    numberCell9 = new Cell(false, false, false, new ArrayList<Cell>());

    row4 = new ArrayList<Cell>(Arrays.asList(numberCell1, numberCell2, numberCell3));
    row5 = new ArrayList<Cell>(Arrays.asList(numberCell4, numberCell5, numberCell6));
    row6 = new ArrayList<Cell>(Arrays.asList(numberCell7, numberCell8, numberCell9));
    testMinesweeper = new ArrayList<ArrayList<Cell>>(Arrays.asList(row4, row5, row6));
    grid1 = new Grid(1, 3, 3, 2, 0, -1);
    grid1.minesweeper = testMinesweeper;
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 3; j++) {
        grid1.populateNeighbors(i, j);
      }
    }

    grid = grid1.minesweeper;
    xCoord = 10;

    world = new WorldScene(900, 480);
    for (ArrayList<Cell> row : grid) {
      int yCoord = 10;
      for (Cell column : row) {

        world.placeImageXY(column.draw(), xCoord, yCoord);
        yCoord = yCoord + 25;

      }

      xCoord = xCoord + 25;

    }

    numberCell1 = new Cell(true, false, true, new ArrayList<Cell>());
    numberCell2 = new Cell(false, false, false, new ArrayList<Cell>());
    numberCell3 = new Cell(false, false, false, new ArrayList<Cell>());
    numberCell4 = new Cell(false, false, true, new ArrayList<Cell>());
    numberCell5 = new Cell(false, false, true, new ArrayList<Cell>());
    numberCell6 = new Cell(false, false, false, new ArrayList<Cell>());
    numberCell7 = new Cell(false, false, false, new ArrayList<Cell>());
    numberCell8 = new Cell(false, false, false, new ArrayList<Cell>());
    numberCell9 = new Cell(false, false, false, new ArrayList<Cell>());

    row4 = new ArrayList<Cell>(Arrays.asList(numberCell1, numberCell2, numberCell3));
    row5 = new ArrayList<Cell>(Arrays.asList(numberCell4, numberCell5, numberCell6));
    row6 = new ArrayList<Cell>(Arrays.asList(numberCell7, numberCell8, numberCell9));
    testMinesweeper2 = new ArrayList<ArrayList<Cell>>(Arrays.asList(row4, row5, row6));

    grid2 = new Grid(1, 3, 3, 2, 0, -1);
    grid2.minesweeper = testMinesweeper2;
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 3; j++) {
        grid2.populateNeighbors(i, j);
      }
    }
    
    world.placeImageXY(
        new TextImage("Number of clicks: " + Integer.toString(0), 20, Color.BLACK), 100,
        450);
    t.checkExpect(grid2.makeScene(), world);
 
    

    
 

  }

  // tests the setMines method
  void testSetMines(Tester t) {

    reset();

    testGrid = new Grid(2, 3, 3, 5, 0, -1);
    testGrid.setMines();

    testGrid2 = new Grid(2, 3, 3, 5, 0, -1);

    row1 = new ArrayList<Cell>(Arrays.asList(numberCell1, numberCell2, numberCell3));
    row2 = new ArrayList<Cell>(Arrays.asList(numberCell4, numberCell5, mineCell1));
    row3 = new ArrayList<Cell>(Arrays.asList(numberCell7, numberCell8, mineCell2));

    testGrid2.minesweeper = new ArrayList<ArrayList<Cell>>(Arrays.asList(row1, row2, row3));

    t.checkExpect(testGrid, testGrid2); // testGrid2 goes through the same procedure as testGrid

    reset();

    testGrid = new Grid(3, 3, 3, 10, 0, -1);
    testGrid.setMines();

    testGrid2 = new Grid(3, 3, 3, 10, 0, -1);

    row1 = new ArrayList<Cell>(Arrays.asList(mineCell1, numberCell2, numberCell3));
    row2 = new ArrayList<Cell>(Arrays.asList(numberCell4, mineCell2, mineCell3));
    row3 = new ArrayList<Cell>(Arrays.asList(numberCell7, numberCell8, numberCell9));

    testGrid2.minesweeper = new ArrayList<ArrayList<Cell>>(Arrays.asList(row1, row2, row3));

    t.checkExpect(testGrid, testGrid2); // testGrid2 goes through the same procedure as testGrid

    reset();

    testGrid = new Grid(1, 3, 3, 21, 0, -1);
    testGrid.setMines();

    testGrid2 = new Grid(1, 3, 4, 21, 0, -1);
    testGrid2.setMines();

    t.checkFail(testGrid, testGrid2); // different dimensions

    reset();

    testGrid = new Grid(4, 3, 3, 18, 0, -1);
    testGrid.setMines();

    testGrid2 = new Grid(4, 3, 3, 11, 0, -1);
    testGrid2.setMines();

    t.checkFail(testGrid, testGrid2); // different seed values

    reset();

    testGrid = new Grid(2, 3, 3, 14, 0, -1);
    testGrid.setMines();

    testGrid2 = new Grid(4, 3, 3, 14, 0, -1);
    testGrid2.setMines();

    t.checkFail(testGrid, testGrid2); // different number of mines

  }

  // tests the countMines method
  void testCountMines(Tester t) {
    reset();
    numberCell1 = new Cell(true, false, false,
        new ArrayList<Cell>(Arrays.asList(numberCell3, numberCell6, mineCell1, flagCell)));
    mineCell2 = new Cell(true, false, true,
        new ArrayList<Cell>(Arrays.asList(numberCell7, numberCell3, mineCell3, numberCell9)));
    numberCell2 = new Cell(true, false, true,
        new ArrayList<Cell>(Arrays.asList(numberCell1, numberCell3, numberCell4, numberCell7)));
    flagCell = new Cell(true, true, false,
        new ArrayList<Cell>(Arrays.asList(mineCell1, mineCell3, numberCell4, numberCell1)));

    t.checkExpect(numberCell8.countMines(), 0);// empty list
    t.checkExpect(numberCell2.countMines(), 0);// has no mines
    t.checkExpect(mineCell2.countMines(), 1);// has 1 mine as a NEIGHBOR, doesn't include itself
    t.checkExpect(flagCell.countMines(), 2);// has 2 mines
  }

  // tests the getRandomValue method
  void testGetRandomValue(Tester t) {

    // random seed values
    Utils u = new Utils(new Random(11));
    Utils u2 = new Utils(new Random(38));
    Utils u3 = new Utils(new Random(47));

    t.checkExpect(u.getRandomValue(20), 18);
    t.checkExpect(u2.getRandomValue(68), 36);
    t.checkExpect(u3.getRandomValue(13), 11);
  }
  
  // testing the updateOnClick method
  void testUpdateOnClick(Tester t) {
    
    reset();
    
    testGrid = new Grid(2, 3, 3, 17, 0, -1);
    testGrid.minesweeper = testMinesweeper;
    
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 3; j++) {
        testGrid.populateNeighbors(i, j);
      }
    }
    
    Cell randomCell = testGrid.minesweeper.get(0).get(2);
    
    
    
    randomCell.updateOnClick("LeftButton");
    
  
    // it is now revealed, but no flood fill occured
    t.checkExpect(testGrid.minesweeper.get(0).get(2).unknown, false);
    
    reset();
    
    testGrid = new Grid(2, 3, 3, 17, 0, -1);
    testGrid.minesweeper = testMinesweeperU;
    
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 3; j++) {
        testGrid.populateNeighbors(i, j);
      }
    }
    
    randomCell = testGrid.minesweeper.get(2).get(0);
    
    randomCell.updateOnClick("LeftButton");
    
    //initiates flood fill and hits all cells upto the ones with neighboring mines,
    //AND leaves the flagged cell untouched even though it lies inside the flood fill
    t.checkExpect(numberCell1U.unknown, false);
    t.checkExpect(numberCell2U.unknown, false);
    t.checkExpect(numberCell3U.unknown, false);
    t.checkExpect(numberCell7U.unknown, false);
    t.checkExpect(numberCell8U.unknown, false);
    t.checkExpect(flagCellU.unknown, true);
    
    
    reset();
    
    testGrid = new Grid(2, 3, 3, 17, 0, -1);
    testGrid.minesweeper = testMinesweeperU;
    
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 3; j++) {
        testGrid.populateNeighbors(i, j);
      }
    }
    
    randomCell = testGrid.minesweeper.get(1).get(1);
    
    //flagged cell becomes unflagged
    randomCell.updateOnClick("RightButton");
    t.checkExpect(flagCellU.isFlagged, false);
    
    //unflagged cell becomes flagged
    randomCell = testGrid.minesweeper.get(1).get(2);
    randomCell.updateOnClick("RightButton");
    t.checkExpect(mineCell1U.isFlagged, true);
    
    //revealed cells cannot be flagged so it doesn't register
    randomCell = testGrid.minesweeper.get(0).get(2);
    randomCell.unknown = false;
    randomCell.updateOnClick("RightButton");
    t.checkExpect(numberCell7U.isFlagged, false);
   
    
  }

  //tests onKeyEvent
  void testOnKeyEvent(Tester t) {
    reset();
    
    testGrid = new Grid(3, 3, 3, 10, 0, -2);
    testGrid.onKeyEvent("1");
    t.checkExpect(testGrid.gameState, -1); // goes to the easy grid
    
    reset();
    testGrid = new Grid(3, 3, 3, 10, 0, -2);
    testGrid.onKeyEvent("2");
    t.checkExpect(testGrid.gameState, -1); // goes to the medium grid
    
    reset();
    testGrid = new Grid(3, 3, 3, 10, 0, -2);
    testGrid.onKeyEvent("3");
    t.checkExpect(testGrid.gameState, -1); // goes to the hard grid
    
    testGrid.onKeyEvent("2");
    t.checkExpect(testGrid.gameState, -1); // doesn't affect it anymore
    
    reset();
    testGrid = new Grid(3, 3, 3, 10, 0, -1);
    testGrid.onKeyEvent("2");
    t.checkExpect(testGrid.gameState, -1); // doesn't affect any non intro-scene state
    
    reset();
    testGrid = new Grid(3, 3, 3, 10, 0, 1);
    testGrid.onKeyEvent("2");
    t.checkExpect(testGrid.gameState, 1); //doesn't exit the win screen
    
    reset();
    testGrid = new Grid(3, 3, 3, 10, 0, 0);
    testGrid.onKeyEvent("2");
    t.checkExpect(testGrid.gameState, 0); //doesn't exit the lose screen
    
  }
  
  //tests mineChecker()
  void testMineChecker(Tester t) {
    
    reset();
    testGrid = new Grid(3, 4, 4, 13, 0, -1);
    testGrid.minesweeper = testMinesweeperU;
    
    Cell randomCell = testGrid.minesweeper.get(1).get(2);
    
    t.checkExpect(randomCell.mineChecker(), true); //mineCell1U is here
    
    randomCell = testGrid.minesweeper.get(0).get(2);
    t.checkExpect(randomCell.mineChecker(), false); //a non-mine cell is here
    
    randomCell = testGrid.minesweeper.get(2).get(2);
    t.checkExpect(randomCell.mineChecker(), true); //mineCell4U is here
    
    randomCell.unknown = false;
    t.checkExpect(randomCell.mineChecker(), true); //revealed cells are detected too
    
    randomCell = testGrid.minesweeper.get(1).get(2);
    randomCell.isFlagged = true;
    t.checkExpect(randomCell.mineChecker(), true);//flagged cells are detected too
    
  }
  
  //tests unknownChecker()
  void testUnknownChecker(Tester t) {
    
    reset();
    testGrid = new Grid(3, 4, 4, 13, 0, -1);
    testGrid.minesweeper = testMinesweeper;
    
    Cell randomCell = testGrid.minesweeper.get(2).get(1);
    
    t.checkExpect(randomCell.unknownChecker(), true); //cell isn't revealed yet
    
    randomCell.unknown = false;
    t.checkExpect(randomCell.unknownChecker(), false); //cell is revealed now
    
    randomCell = testGrid.minesweeper.get(1).get(2);
    randomCell.isFlagged = true;
    t.checkExpect(randomCell.unknownChecker(), true);//flagged cells are unknown
    
    randomCell = testGrid.minesweeper.get(0).get(0);
    randomCell.unknown = false;
    randomCell.isFlagged = true;
    t.checkExpect(randomCell.unknownChecker(), false);//revealed cells cannot be flagged
  }
  
  
  //tests flagChecker()
  void testFlagChecker(Tester t) {
    
    reset();
    testGrid = new Grid(3, 4, 4, 13, 0, -1);
    testGrid.minesweeper = testMinesweeper;
    
    Cell randomCell = testGrid.minesweeper.get(2).get(1);
    
    randomCell.unknown = false;
    t.checkExpect(randomCell.flagChecker(), false); //cell is revealed
    
    randomCell.unknown = true;
    randomCell.isFlagged = true;
    t.checkExpect(randomCell.flagChecker(), true);//cell is now flagged
   
    
    randomCell = testGrid.minesweeper.get(1).get(1);
    t.checkExpect(randomCell.flagChecker(), true);//cell is already flagged
    
    randomCell = testGrid.minesweeper.get(1).get(0);
    randomCell.unknown = false;
    t.checkExpect(randomCell.flagChecker(), false);//revealed cells cannot be flagged
    
  }

  void testFloodFill(Tester t) {
    reset();
    Grid grid1 = new Grid(4, 3, 3, 5, 0, -1);
    Grid grid2 = new Grid(4, 3, 3, 5, 0, -1);

    grid1.setMines();
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 3; j++) {
        grid1.populateNeighbors(i, j);
      }
    }

    grid2.setMines();
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 3; j++) {
        grid2.populateNeighbors(i, j);
      }
    }

    t.checkExpect(grid1, grid2);// shows both are equal, tests a floodfill on a lone mine
    grid1.minesweeper.get(1).get(2).floodFill();
    Cell cell = grid2.minesweeper.get(1).get(2);
    if (cell.countMines() == 0 && !cell.hasMine && cell.unknown && !cell.isFlagged) {

      cell.unknown = false;

      for (Cell neighbor : cell.neighbors) {
        neighbor.floodFill();
      }

    }
    else {
      if (!cell.isFlagged) {
        cell.unknown = false;

      }

    }

    t.checkExpect(grid1, grid2); // both are equal now

    reset();
    grid1 = new Grid(4, 4, 4, 7, 0, -1);
    grid2 = new Grid(4, 4, 4, 7, 0, -1);

    grid1.setMines();
    for (int i = 0; i < 4; i++) {
      for (int j = 0; j < 4; j++) {
        grid1.populateNeighbors(i, j);
      }
    }

    grid2.setMines();
    for (int i = 0; i < 4; i++) {
      for (int j = 0; j < 4; j++) {
        grid2.populateNeighbors(i, j);
      }
    }

    t.checkExpect(grid1, grid2);// tests floodfill on a single square
    grid1.minesweeper.get(1).get(2).floodFill();
    cell = grid2.minesweeper.get(1).get(2);
    if (cell.countMines() == 0 && !cell.hasMine && cell.unknown && !cell.isFlagged) {

      cell.unknown = false;

      for (Cell neighbor : cell.neighbors) {
        neighbor.floodFill();
      }

    }
    else {
      if (!cell.isFlagged) {
        cell.unknown = false;

      }

    }

    t.checkExpect(grid1, grid2); // both are equal now

    reset();
    grid1 = new Grid(0, 4, 4, 10, 0, -1);
    grid2 = new Grid(0, 4, 4, 10, 0, -1);

    grid1.setMines();
    for (int i = 0; i < 4; i++) {
      for (int j = 0; j < 4; j++) {
        grid1.populateNeighbors(i, j);
      }
    }

    grid2.setMines();
    for (int i = 0; i < 4; i++) {
      for (int j = 0; j < 4; j++) {
        grid2.populateNeighbors(i, j);
      }
    }
    grid1.minesweeper.get(3).get(3).isFlagged = true;
    grid2.minesweeper.get(3).get(3).isFlagged = true;

    t.checkExpect(grid1, grid2);// tests floodfill recursively with a flag in the grid
    grid1.minesweeper.get(1).get(2).floodFill();

    cell = grid2.minesweeper.get(1).get(2);
    if (cell.countMines() == 0 && !cell.hasMine && cell.unknown && !cell.isFlagged) {

      cell.unknown = false;

      for (Cell neighbor : cell.neighbors) {
        neighbor.floodFill();
      }

    }
    else {
      if (!cell.isFlagged) {
        cell.unknown = false;

      }

    }

    t.checkExpect(grid1, grid2); // both are equal now

  }
  
  // tests for onMouseClick
  void testOnMouseClick(Tester t) {
    reset();
    Grid grid1 = new Grid(0, 4, 4, 10, 0, -1);
    Grid grid2 = new Grid(0, 4, 4, 10, 0, -1);
    int clicks = 0;

    grid1.setMines();
    for (int i = 0; i < 4; i++) {
      for (int j = 0; j < 4; j++) {
        grid1.populateNeighbors(i, j);
      }
    }

    grid2.setMines();
    for (int i = 0; i < 4; i++) {
      for (int j = 0; j < 4; j++) {
        grid2.populateNeighbors(i, j);
      }
    }
    t.checkExpect(grid1, grid2);// both grids are initially the same
    grid1.onMouseClicked(new Posn(50, 50), "RightButton");

    if (grid2.gameState > -1) {

      if ("RightButton".equals("LeftButton")) {

        grid2.clicks = 0;
        grid2.minesweeper = new Grid(grid2.mines, 50, 50).minesweeper;
        grid2.gameState = -2;
      }
    }

    if ((50 / 25) < 50 && (50 / 25) < 50 && grid2.gameState == -1) {

      Cell target = grid2.minesweeper.get(50 / 25).get(50 / 25);

      if (grid2.minesweeper.get(50 / 25).get(50 / 25).unknownChecker()) {

        target.updateOnClick("RightButton");

        if ("RightButton".equals("LeftButton") && !target.flagChecker()) {
          clicks++;

        }

      }

      if (target.mineChecker() && !(target.unknownChecker())) {

        grid2.gameState = 0;

      }

      int revealedTiles = 0;

      for (ArrayList<Cell> row : grid2.minesweeper) {
        for (Cell column : row) {
          if (!column.unknownChecker()) {
            revealedTiles++;

          }

        }

      }

      if (revealedTiles == ((50 * 50) - 0)) {
        grid2.gameState = 1;

      }

    }
    t.checkExpect(grid1, grid2);// both grids mutated tests flagging

    
    
    
    
    reset();
    grid1 = new Grid(1, 4, 4, 10,0, -1);
    grid2 = new Grid(1, 4, 4, 10,0, -1);

    
    grid1.setMines();
    for (int i = 0; i < 4; i++) {
      for (int j = 0; j < 4; j++) {
        grid1.populateNeighbors(i, j);
      }
    }
    
    grid2.setMines();
    for (int i = 0; i < 4; i++) {
      for (int j = 0; j < 4; j++) {
        grid2.populateNeighbors(i, j);
      }
    }
    t.checkExpect(grid1, grid2);// both grids are initially the same
    grid1.onMouseClicked(new Posn(50,50), "LeftButton");

    

    if (grid2.gameState > -1) {

      if ("LeftButton".equals("LeftButton")) {

        grid2.clicks = 0;
        grid2.minesweeper = new Grid(grid2.mines, grid2.x, grid2.y).minesweeper;
        grid2.gameState = -2;
      }
    }

    if ((50 / 25) < grid2.x && (50 / 25) < grid2.y && grid2.gameState == -1) {

      Cell target = grid2.minesweeper.get(50 / 25).get(50 / 25);
      
      if (grid2.minesweeper.get(50 / 25).get(50 / 25).unknownChecker()) {
        
        target.updateOnClick("LeftButton");
        
        if ("LeftButton".equals("LeftButton") && !target.flagChecker()) {
          grid2.clicks++;
          
        }
        

      }



      if (target.mineChecker() && !(target.unknownChecker())) {

        grid2.gameState = 0;

      }


      int revealedTiles = 0;

      for (ArrayList<Cell> row : grid2.minesweeper) {
        for (Cell column : row) {
          if (!column.unknownChecker()) {
            revealedTiles++;

          }

        }

      }
      
      if (revealedTiles == ((50 * 50) - 1)) {
        grid2.gameState = 1;

      }

    }

    t.checkExpect(grid1, grid2);// tests the winning condition

    
    
    reset();
    grid1 = new Grid(1, 4, 4, 11,0, -1);
    grid2 = new Grid(1, 4, 4, 11,0, -1);

    
    grid1.setMines();
    for (int i = 0; i < 4; i++) {
      for (int j = 0; j < 4; j++) {
        grid1.populateNeighbors(i, j);
      }
    }
    
    grid2.setMines();
    for (int i = 0; i < 4; i++) {
      for (int j = 0; j < 4; j++) {
        grid2.populateNeighbors(i, j);
      }
    }
    t.checkExpect(grid1, grid2);
    
    grid1.onMouseClicked(new Posn(50,50), "LeftButton");
    

    if (grid2.gameState > -1) {

      if ("LeftButton".equals("LeftButton")) {

        grid2.clicks = 0;
        grid2.minesweeper = new Grid(grid2.mines, grid2.x, grid2.y).minesweeper;
        grid2.gameState = -2;
      }
    }

    if ((50 / 25) < grid2.x && (50 / 25) < grid2.y && grid2.gameState == -1) {

      Cell target = grid2.minesweeper.get(50 / 25).get(50 / 25);
      
      if (grid2.minesweeper.get(50 / 25).get(50 / 25).unknownChecker()) {
        
        target.updateOnClick("LeftButton");
        
        if ("LeftButton".equals("LeftButton") && !target.flagChecker()) {
          grid2.clicks++;
          
        }
        

      }



      if (target.mineChecker() && !(target.unknownChecker())) {

        grid2.gameState = 0;

      }


      int revealedTiles = 0;

      for (ArrayList<Cell> row : grid2.minesweeper) {
        for (Cell column : row) {
          if (!column.unknownChecker()) {
            revealedTiles++;

          }

        }

      }
      
      if (revealedTiles == ((50 * 50) - 1)) {
        grid2.gameState = 1;

      }

    }

    t.checkExpect(grid1, grid2);// both grids are initially the same
   

    
    reset();
    grid1 = new Grid(1, 4, 4, 11,0, -1);
    grid2 = new Grid(1, 4, 4, 11,0, -1);

    
    grid1.setMines();
    for (int i = 0; i < 4; i++) {
      for (int j = 0; j < 4; j++) {
        grid1.populateNeighbors(i, j);
      }
    }
    
    grid2.setMines();
    for (int i = 0; i < 4; i++) {
      for (int j = 0; j < 4; j++) {
        grid2.populateNeighbors(i, j);
      }
    }
    t.checkExpect(grid1, grid2);
    
    //tests clicking on a tile out of bounds
    grid1.onMouseClicked(new Posn(10000,10000), "LeftButton");
    t.checkExpect(grid1, grid2);
    
    

    
  }
  
  //tests the draw function
  void testDraw(Tester t) {

    reset();
    WorldImage tile = new OverlayImage(new RectangleImage(25, 25, OutlineMode.OUTLINE, Color.BLACK),
        new RectangleImage(25, 25, OutlineMode.SOLID, Color.CYAN));

    numberCell1 = new Cell(true, false, false,
        new ArrayList<Cell>(Arrays.asList(numberCell3, numberCell6, mineCell1, flagCell)));
    mineCell2 = new Cell(false, false, true,
        new ArrayList<Cell>(Arrays.asList(numberCell7, numberCell3, mineCell3, numberCell9)));
    numberCell2 = new Cell(true, false, true,
        new ArrayList<Cell>(Arrays.asList(numberCell1, numberCell3, numberCell4, numberCell7)));
    flagCell = new Cell(true, true, false,
        new ArrayList<Cell>(Arrays.asList(mineCell1, mineCell3, numberCell4, numberCell1)));
    t.checkExpect(flagCell.draw(),
        new OverlayImage(new CircleImage(10, OutlineMode.SOLID, Color.BLUE), tile));
    // tests a flag

    tile = new OverlayImage(new RectangleImage(25, 25, OutlineMode.OUTLINE, Color.BLACK),
        new RectangleImage(25, 25, OutlineMode.SOLID, Color.CYAN));
    t.checkExpect(mineCell2.draw(),
        new OverlayImage(new StarImage(10, 9, OutlineMode.SOLID, Color.RED), tile));
    // tests the image of a mine
    reset();
    // tests the images of numbers 1,2,3,4 and 7

    numberCell1 = new Cell(false, false, false, new ArrayList<Cell>(Arrays.asList(mineCell1)));
    t.checkExpect(numberCell1.draw(),
        new OverlayImage(new TextImage(Integer.toString(1), Color.BLUE),
            new OverlayImage(new RectangleImage(25, 25, OutlineMode.OUTLINE, Color.BLACK),
                new RectangleImage(25, 25, OutlineMode.SOLID, Color.GRAY))));
    numberCell1 = new Cell(false, false, false,
        new ArrayList<Cell>(Arrays.asList(mineCell1, mineCell1)));
    t.checkExpect(numberCell1.draw(),
        new OverlayImage(new TextImage(Integer.toString(2), Color.GREEN),
            new OverlayImage(new RectangleImage(25, 25, OutlineMode.OUTLINE, Color.BLACK),
                new RectangleImage(25, 25, OutlineMode.SOLID, Color.GRAY))));
    numberCell1 = new Cell(false, false, false,
        new ArrayList<Cell>(Arrays.asList(mineCell1, mineCell1, mineCell1)));
    t.checkExpect(numberCell1.draw(),
        new OverlayImage(new TextImage(Integer.toString(3), Color.RED),
            new OverlayImage(new RectangleImage(25, 25, OutlineMode.OUTLINE, Color.BLACK),
                new RectangleImage(25, 25, OutlineMode.SOLID, Color.GRAY))));
    numberCell1 = new Cell(false, false, false,
        new ArrayList<Cell>(Arrays.asList(mineCell1, mineCell1, mineCell1, mineCell1)));
    t.checkExpect(numberCell1.draw(),
        new OverlayImage(new TextImage(Integer.toString(4), Color.YELLOW),
            new OverlayImage(new RectangleImage(25, 25, OutlineMode.OUTLINE, Color.BLACK),
                new RectangleImage(25, 25, OutlineMode.SOLID, Color.GRAY))));

    numberCell1 = new Cell(false, false, false, new ArrayList<Cell>(Arrays.asList(mineCell1,
        mineCell1, mineCell1, mineCell1, mineCell1, mineCell1, mineCell1)));
    t.checkExpect(numberCell1.draw(),
        new OverlayImage(new TextImage(Integer.toString(7), Color.PINK),
            new OverlayImage(new RectangleImage(25, 25, OutlineMode.OUTLINE, Color.BLACK),
                new RectangleImage(25, 25, OutlineMode.SOLID, Color.GRAY))));

    numberCell1 = new Cell(false, false, false, new ArrayList<Cell>(Arrays.asList()));
    //tests a cell without  mines around it
    t.checkExpect(numberCell1.draw(),
        new OverlayImage(new RectangleImage(25, 25, OutlineMode.OUTLINE, Color.BLACK),
            new RectangleImage(25, 25, OutlineMode.SOLID, Color.GRAY)));
  
    numberCell1 = new Cell(true, false, false, new ArrayList<Cell>(Arrays.asList()));
    t.checkExpect(numberCell1.draw(), tile);// tests a cell that has not been revealed
    
    
    
  }
  
  // the big bang method
  void testBigBang(Tester t) {
    reset();
    Grid grid = new Grid(2, 3, 3);

    int worldWidth = 900;
    int worldHeight = 480;
    double tickRate = .0000000001;

    // NOTE: if you comment out the 'else' subcondition in the if(unknown) condition
    // in the
    // draw method, you will be able to see the entire "solved" grid.
    grid.bigBang(worldWidth, worldHeight, tickRate);
  }
  
}
