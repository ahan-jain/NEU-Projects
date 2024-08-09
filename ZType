import tester.Tester;
import javalib.funworld.*;
import javalib.worldimages.*;
import java.awt.Color;
import java.util.Random;


interface IZType {

  int SCREEN_HEIGHT = 600; // defining the height of the worldScene
  
  int SCREEN_WIDTH = 400; // defining the width of the worldScene
 
  WorldScene makeScene(); // draws the current state of the world

  ZTypeWorld onKeyEvent(String key); //method for testing onTick by providing 
  
  ZTypeWorld onTick(); //updates the world on tick, after 25 ticks spawn a new word
  
  WorldEnd worldEnds(); //decides whether or not the game has ended, if so, display the lost screen
  
  WorldScene finalScene();//constructs the final scene
  
  ZTypeWorld onTickForTesting(); //method for testing onTick by providing 
  
}

class ZTypeWorld extends World implements IZType {

  int screenHeight;
  int screenWidth;
  ILoWord words;
  int tick;
  boolean isGameOver;
  int score;
  int level;
  

  ZTypeWorld(int screenWidth, int screenHeight, ILoWord words, int tick, boolean isGameOver,
      int score, int level) {

    this.screenWidth = screenWidth;
    this.screenHeight = screenHeight;
    this.words = words;
    this.tick = tick;
    this.isGameOver = isGameOver;
    this.score = score;
    this.level = level;
  }

  ZTypeWorld(ILoWord words, int tick, boolean isGameOver, int score, int level) {
    
    this(SCREEN_WIDTH, SCREEN_HEIGHT, words, tick, isGameOver, score, level);
  }
  
  
  /* Fields:
  * ... this.words ...                                -- ILoWord
  * ... this.tick  ...                                -- int
  * ... this.isGameOver ...                           -- Boolean
  * ... this.screenHeight ...                         -- int
  * ... this.screenWidth ...                          -- int
  * ... this.screenHeight ...                         -- int
  * Methods:
  * ... this.makeScene() ...                          -- WorldScene
  * ... this.finalScene() ...                         -- WorldScene
  * ... this.onTick() ...                             -- ZTypeWorld
  * ... this.onKeyEvent() ...                         -- ZTypeWorld
  * ... this.worldEnds() ...                          -- worldEnd
  * ... this.onTickForTesting()...                    -- ZTypeWorld
  
  * Methods on fields:
  * 
  ... this.words.sort() ...                         -- ILoWord
  ... this.words.insert(IWord that)...              -- ILoWord
  ... this.words.isSorted() ...                     -- Boolean
  ... this.words.isSortedHelp(IWord that)...        -- Boolean
  ... this.words.interleave(ILoWord that) ...       -- ILoWord
  ... this.words.merge(ILoWord that) ...            -- ILoWord
  ... this.words.checkAndReduce(String s) ...       -- ILoWord
  ... this.words.addToEnd(IWord that) ...           -- ILoWord
  ... this.words.filterOutEmpties() ...             -- ILoWord  
  ... this.words.draw(WorldScene ws) ...            -- WorldScene
  ... this.filterOutEmpties...                      -- ILoWord
  ... this.words.move() ...                         -- ILoWord
  ... this.words.inactiveToActive() ...             -- ILoWord
  ... this.words.anyToReduce() ...                    -- Boolean
  ... this.words.lost() ...                         -- Boolean
  
   
  */
  

  //draws the current state of the world
  public WorldScene makeScene() {

    if (isGameOver) {

      return this.finalScene();
    }

    return this.words.draw(new WorldScene(screenWidth, screenHeight))
        .placeImageXY(new TextImage("Score: " + this.score, 12, Color.BLACK), 365, 590)
        .placeImageXY(new TextImage("Level: " + this.level, 12, Color.BLACK), 25, 590).placeImageXY(
            new OverlayImage(new CircleImage(6, OutlineMode.SOLID, Color.CYAN),
                new OverlayImage(new CircleImage(10, OutlineMode.SOLID, Color.BLACK),
                    new EquilateralTriangleImage(50, OutlineMode.SOLID, Color.lightGray))),
            200, 580);

  }

  // constructs the final scene
  public WorldScene finalScene() {

    return new WorldScene(SCREEN_WIDTH, SCREEN_HEIGHT)
        .placeImageXY(new RectangleImage(SCREEN_WIDTH, SCREEN_HEIGHT, "solid", Color.BLACK), 200,
            300)
        .placeImageXY(new TextImage("Game Over!", 24, FontStyle.BOLD, Color.WHITE), SCREEN_WIDTH / 2,
            SCREEN_HEIGHT / 2 - 20)
        .placeImageXY(new TextImage("Final Score: " + this.score, 22, FontStyle.BOLD, Color.WHITE),
            SCREEN_WIDTH / 2, SCREEN_HEIGHT / 2 + 20)
        .placeImageXY(
            new TextImage("Level Reached: " + this.level, 22, FontStyle.BOLD, Color.WHITE),
            SCREEN_WIDTH / 2, SCREEN_HEIGHT / 2 + 60)
        .placeImageXY(new TextImage("(click to restart)", 18, FontStyle.BOLD, Color.WHITE),
            SCREEN_WIDTH / 2, SCREEN_HEIGHT / 2 + 200);
  }

  // updates the world on tick, after 25 ticks spawn a new word
  public ZTypeWorld onTick() {
    this.tick += 1;
    Utils u = new Utils();

    if ((tick % 30 == 0)) {

      return new ZTypeWorld(
          this.words.addToEnd(new InactiveWord(u.makeWord(), u.wordPlacer(), 10).move()), this.tick,
          this.words.lost(), this.score, this.level);

    }

    else {

      return new ZTypeWorld(this.words.move(), this.tick, this.words.lost(), this.score,
          this.level);
    }

  }

  // method for testing onTick by providing
  public ZTypeWorld onTickForTesting() {
    this.tick += 1;
    Utils u = new Utils(new Random(10));
    if ((tick % 25 == 0)) {
      return new ZTypeWorld(
          this.words.addToEnd(new InactiveWord(u.makeWord(), u.wordPlacer(), 10).move()), this.tick,
          this.words.lost(), this.score, this.level);

    }

    return new ZTypeWorld(this.words.move(), this.tick, this.words.lost(), this.score, this.level);
  }

  // logs the key strokes, if the word has an activeWord, check and reduce if not
  public ZTypeWorld onKeyEvent(String key) {

    if (this.words.anyToReduce()) {

      if (this.words.checkAndReduce(key).containsEmpty()) {

        if (this.score > 0 && this.score % 6 == 0) {

          this.level += 1;

        }

        return new ZTypeWorld(this.words.checkAndReduce(key).filterOutEmpties(), this.tick,
            this.words.lost(), this.score + 1, this.level);

      }

      return new ZTypeWorld(this.words.checkAndReduce(key).filterOutEmpties(), this.tick,
          this.words.lost(), this.score, this.level);

    }

    else {

      return new ZTypeWorld(this.words.inactiveToActive(key).checkAndReduce(key).filterOutEmpties(),
          this.tick, this.words.lost(), this.score, this.level);
    }

  }

  public ZTypeWorld onMouseClicked(Posn pos) {

    if (isGameOver) {

      if (pos.x >= 0 && pos.x <= (SCREEN_WIDTH + 50) && pos.y >= 0
          && pos.y <= (SCREEN_HEIGHT + 50)) {

        return new ZTypeWorld(new MtLoWord(), 0, false, 0, 1);
      }

      else {
        return this;
      }

    }

    else {
      return this;
    }
  }

}

//utility class for the makeWord() function and helpers
class Utils {

  Random rand;

  // passing a completely random Random object to the parameterized Utils
  // constructor
  Utils() {

    this(new Random());
  }

  // initializing the random object to whatever seeded/unseeded value is passed to
  // the constructor
  Utils(Random rand) {

    this.rand = rand;
  }
  
  /* Fields:
  * ... this.rand ...                                       -- Random
  *    
  * Methods:
  * ... new Utils().makeWord() ...                          -- String
  * ... new Utils().makeWordHelper(String word) ...         -- String
  * ... new Utils().wordPlacer()                            -- int
  * 
  * Methods on fields:
  */

  // creates a string containing six random letters
  public String makeWord() {

    return makeWordHelper("", Math.round(rand.nextInt(3)) + 5);
  }

  // helper for makeWord that creates the string using a random object
  public String makeWordHelper(String word, int length) {

    String letters = "abcdefghijklmnopqrstuvwxyz";
    int position = Math.round(rand.nextInt(25));

    if (word.length() == length) {
      return word;
    }

    else {

      return makeWordHelper(word + letters.charAt(position), length);
    }

  }

  // places a word at a random x position in the game
  public int wordPlacer() {
    int x = rand.nextInt(360);
    if (x >= 30 && x <= 375) {

      return x;
    }

    else {

      return new Utils().wordPlacer();
    }

  }

}

//represents a list of words
interface ILoWord {

  ILoWord sort(); // produces a new list, with words sorted in alphabetical order

  ILoWord insert(IWord that); // inserts an IWord into an ILoWord

  boolean isSorted(); // determines whether this list of IWords has words sorted in alphabetical
  // order

  boolean isSortedHelp(IWord that); // checks whether the first word in this list comes before the
  // given word and returns a true or false value accordingly

  ILoWord interleave(ILoWord that); // takes this list of IWords and a given list of IWords, and
  // produces a list with every other element from both lists

  ILoWord merge(ILoWord that); // merges this sorted list of IWords and a given sorted list of
  // IWords

  ILoWord checkAndReduce(String s); // takes in a String of length 1 and produces an ILoWord where
  // any active words in this ILoWord are reduced by removing the first letter

  ILoWord addToEnd(IWord that); // takes in an IWord and produces an ILoWord that is like this
  // ILoWord but with the given IWord added at the end

  ILoWord filterOutEmpties(); // produces an ILoWord with any IWords that have an empty string are
  // filtered out

  WorldScene draw(WorldScene ws); // draws all of the words in this ILoWord onto the
  // given WorldScene
 
  ILoWord move(); // moves the y coordinate in the ILoWord down by one

  ILoWord inactiveToActive(String s); // changes an InActiveWord to ActiveWord with the given String
  // s

  boolean anyToReduce(); // determines whether or not an ILoWord has an ActiveWord

  boolean lost(); // determines whether or not a player has lost the game

  boolean containsEmpty(); // determines whether a list contains a word with an empty string

}

//represents an empty list of words
class MtLoWord implements ILoWord {

  // empty constructor
  MtLoWord() {
  }

  //In MtLoWord
  /* TEMPLATE:

  Methods:
  ... this.sort() ...                         -- ILoWord
  ... this.insert(IWord that)...              -- ILoWord
  ... this.isSorted() ...                     -- Boolean
  ... this.isSortedHelp(IWord that)...        -- Boolean
  ... this.interleave(ILoWord that) ...       -- ILoWord
  ... this.merge(ILoWord that) ...            -- ILoWord
  ... this.checkAndReduce(String s) ...       -- ILoWord
  ... this.addToEnd(IWord that) ...           -- ILoWord
  ... this.filterOutEmpties() ...             -- ILoWord  
  ... this.draw(WorldScene ws) ...            -- WorldScene
  ... this.filterOutEmpties...                -- ILoWord
  ... this.move() ...                         -- ILoWord
  ... this.inactiveToActive() ...             -- ILoWord
  ... this.anyToReduce() ...                    -- Boolean
  ... this.lost() ...                         -- Boolean

  Methods on Fields:
   
  */

  //produces a new list, with words sorted in alphabetical order
  public ILoWord sort() {

    return this;
  }

  // inserts an IWord into an ILoWord
  public ILoWord insert(IWord that) {

    /* TEMPLATE: everything in the template for MtLoWord, plus

    Methods on parameters:
    ... this.comesBefore() ...                    -- Boolean
    ... this.comesBeforeHelp(IWord that)...       -- Boolean
    ... this.charIsEqual() ...                    -- Boolean
    ... this.shorten(ILoWord that) ...            -- IWord
    ... this.isEmptyString(ILoWord that) ...      -- Boolean
    ... this.drawWords(String s) ...              -- WorldScene
    ... this.drawText(IWord that) ...             -- TextImage
    ... this.move() ...                           -- Boolean   
    ... this.lost() ...                           -- Boolean
    ... this.inActiveToActiveHelper(String s)     --IWord
    ... this.anyToReduce() ...                       --boolean

    */
  
    return new ConsLoWord(that, this);
  }

  // determines whether this list of IWords has words sorted in alphabetical order

  public boolean isSorted() {

    return true;
  }

  // determines whether or not a player has lost the game
  public boolean lost() {

    return false;
  }

  // checks whether the first word in this list comes before the given word
  // and returns a true or false value accordingly
  public boolean isSortedHelp(IWord that) {
  
    /* TEMPLATE: everything in the template for MtLoWord, plus

    Methods on parameters:
    ... this.comesBefore() ...                    -- Boolean
    ... this.comesBeforeHelp(IWord that)...       -- Boolean
    ... this.charIsEqual() ...                    -- Boolean
    ... this.shorten(ILoWord that) ...            -- IWord
    ... this.isEmptyString(ILoWord that) ...      -- Boolean
    ... this.drawWords(String s) ...              -- WorldScene
    ... this.drawText(IWord that) ...             -- TextImage
    ... this.move() ...                           -- Boolean   
    ... this.lost() ...                           -- Boolean
    ... this.inActiveToActiveHelper(String s)     --IWord
    ... this.anyToReduce() ...                       --boolean

    */
  
  
    return true;
  }

  //takes this list of IWords and a given list of IWords, and produces a list 
  //with every other element from both lists
  public ILoWord interleave(ILoWord that) {
  
    /* TEMPLATE: Everything in the template for MtLoWord, plus
    
    Methods on parameters:
    ... this.sort() ...                         -- ILoWord
    ... this.insert(IWord that)...              -- ILoWord
    ... this.isSorted() ...                     -- Boolean
    ... this.isSortedHelp(IWord that)...        -- Boolean
    ... this.interleave(ILoWord that) ...       -- ILoWord
    ... this.merge(ILoWord that) ...            -- ILoWord
    ... this.checkAndReduce(String s) ...       -- ILoWord
    ... this.addToEnd(IWord that) ...           -- ILoWord
    ... this.filterOutEmpties() ...             -- ILoWord  
    ... this.draw(WorldScene ws) ...            -- WorldScene
    ... this.filterOutEmpties...                -- ILoWord
    ... this.move() ...                         -- ILoWord
    ... this.inactiveToActive() ...             -- ILoWord
    ... this.anyToReduce() ...                    -- Boolean
    ... this.lost() ...                         -- Boolean
  
    */
    
    return that;
  }

  //merges this sorted list of IWords and a given sorted list of IWords
  public ILoWord merge(ILoWord that) { 
  
    /* TEMPLATE: everything in the template for MtLoWord, plus
  
    Methods on parameters:
    ... this.sort() ...                         -- ILoWord
    ... this.insert(IWord that)...              -- ILoWord
    ... this.isSorted() ...                     -- Boolean
    ... this.isSortedHelp(IWord that)...        -- Boolean
    ... this.interleave(ILoWord that) ...       -- ILoWord
    ... this.merge(ILoWord that) ...            -- ILoWord
    ... this.checkAndReduce(String s) ...       -- ILoWord
    ... this.addToEnd(IWord that) ...           -- ILoWord
    ... this.filterOutEmpties() ...             -- ILoWord  
    ... this.draw(WorldScene ws) ...            -- WorldScene
    ... this.filterOutEmpties...                -- ILoWord
    ... this.move() ...                         -- ILoWord
    ... this.inactiveToActive() ...             -- ILoWord
    ... this.anyToReduce() ...                    -- Boolean
    ... this.lost() ...                         -- Boolean
   
    */
    
    return that;
  }
  
  

  //takes in a String of length 1 and produces an ILoWord where 
  //any active words in this ILoWord are reduced by removing the first letter
  public ILoWord checkAndReduce(String s) { 
    
    return this;
  }
  
  //takes in an IWord and produces an ILoWord that is like this ILoWord 
  //but with the given IWord added at the end
  public ILoWord addToEnd(IWord that) { 
    
    /* TEMPLATE: everything in the template for MtLoWord, plus
  
   Methods on parameters:
    ... this.comesBefore() ...                    -- Boolean
    ... this.comesBeforeHelp(IWord that)...       -- Boolean
    ... this.charIsEqual() ...                    -- Boolean
    ... this.shorten(ILoWord that) ...            -- IWord
    ... this.isEmptyString(ILoWord that) ...      -- Boolean
    ... this.drawWords(String s) ...              -- WorldScene
    ... this.drawText(IWord that) ...             -- TextImage
    ... this.move() ...                           -- Boolean   
    ... this.lost() ...                           -- Boolean
    ... this.inActiveToActiveHelper(String s)     --IWord
    ... this.anyToReduce() ...                       --boolean
  
    */
    
    return new ConsLoWord(that, new MtLoWord());
  }
  
  //produces an ILoWord with any IWords that have an empty string are filtered out
  public ILoWord filterOutEmpties() {
    
    return this;
  }
  
  //draws all of the words in this ILoWord onto the given WorldScene
  public WorldScene draw(WorldScene ws) { 
    /* TEMPLATE: everything in the template for MtLoWord, plus
    
   Methods on parameters:
   ... this.comesBefore() ...                    -- Boolean
   ... this.comesBeforeHelp(IWord that)...       -- Boolean
   ... this.charIsEqual() ...                    -- Boolean
   ... this.shorten(ILoWord that) ...            -- IWord
   ... this.isEmptyString(ILoWord that) ...      -- Boolean
   ... this.drawWords(String s) ...              -- WorldScene
   ... this.drawText(IWord that) ...             -- TextImage
  
    */
    
    return ws;
  }
  
  //moves the y coordinate in the ILoWord down by one
  public ILoWord move() {

    return this;

  }

  // changes an InActiveWord to ActiveWord with the given String s
  public ILoWord inactiveToActive(String s) {

    return this;
  }

  // determines whether or not an ILoWord is an active
  public boolean anyToReduce() {

    return false;
  }

  // checks if the list contains any words with an empty string
  public boolean containsEmpty() {

    return false;

  }

}

//represents a list of words
class ConsLoWord implements ILoWord {
  IWord first;
  ILoWord rest;
  
  ConsLoWord(IWord first, ILoWord rest) {
    this.first = first;
    this.rest = rest;
  }
  
  //In ConsLoWord
  /* TEMPLATE:
   
     Fields:
     ... this.first ...                          -- IWord
     ... this.rest ...                           -- ILoWord
     
  Methods:
  ... this.sort() ...                         -- ILoWord
  ... this.insert(IWord that)...              -- ILoWord
  ... this.isSorted() ...                     -- Boolean
  ... this.isSortedHelp(IWord that)...        -- Boolean
  ... this.interleave(ILoWord that) ...       -- ILoWord
  ... this.merge(ILoWord that) ...            -- ILoWord
  ... this.checkAndReduce(String s) ...       -- ILoWord
  ... this.addToEnd(IWord that) ...           -- ILoWord
  ... this.filterOutEmpties() ...             -- ILoWord  
  ... this.draw(WorldScene ws) ...            -- WorldScene
  ... this.filterOutEmpties...                -- ILoWord
  ... this.move() ...                         -- ILoWord
  ... this.inactiveToActive() ...             -- ILoWord
  ... this.anyToReduce() ...                    -- Boolean
  ... this.lost() ...                         -- Boolean

  Methods on Fields:
  */
  
  //produces a new list, with words sorted in alphabetical order
  public ILoWord sort() {
    
    return this.rest.sort().insert(this.first);
    
  }
  
  //inserts a given word in a list in alphabetical order
  public ILoWord insert(IWord that) {
    
    /* TEMPLATE: everything in the template for ConsLoWord, plus
    
    
    Methods on parameters:
    ... this.comesBefore() ...                    -- Boolean
    ... this.comesBeforeHelp(IWord that)...       -- Boolean
    ... this.charIsEqual() ...                    -- Boolean
    ... this.shorten(ILoWord that) ...            -- IWord
    ... this.isEmptyString(ILoWord that) ...      -- Boolean
    ... this.drawWords(String s) ...              -- WorldScene
    ... this.drawText(IWord that) ...             -- TextImage
    ... this.lost() ...                           -- Boolean
    
    */
    
    if (this.first.comesBefore(that)) {

      return new ConsLoWord(this.first, this.rest.insert(that));
    }

    else {
      return new ConsLoWord(that, this);
    }

  }

  // determines whether or not a player has lost the game
  public boolean lost() {

    return this.first.lost() || this.rest.lost();
  }

  // determines whether this list of IWords has words sorted in alphabetical order
  public boolean isSorted() {

    if (this.rest.isSortedHelp(this.first)) {
      return this.rest.isSorted();
    }

    else {
      return false;
    }

  }
  
  // checks whether the first word in this list comes before the given word
  // and returns a true or false value accordingly
  public boolean isSortedHelp(IWord that) {
    
    /* TEMPLATE: everything in the template for ConsLoWord, plus
  
       Methods on parameters:
    ... this.comesBefore() ...                    -- Boolean
    ... this.comesBeforeHelp(IWord that)...       -- Boolean
    ... this.charIsEqual() ...                    -- Boolean
    ... this.shorten(ILoWord that) ...            -- IWord
    ... this.isEmptyString(ILoWord that) ...      -- Boolean
    ... this.drawWords(String s) ...              -- WorldScene
    ... this.drawText(IWord that) ...             -- TextImage
    ... this.move() ...                           -- Boolean   
    ... this.lost() ...                           -- Boolean
    ... this.inActiveToActiveHelper(String s)     --IWord
    ... this.anyToReduce() ...                       --boolean
   
    */
    
    return !(this.first.comesBefore(that));
  }
  
  // takes this list of IWords and a given list of IWords, and produces a list
  // with every other element from both lists
  public ILoWord interleave(ILoWord that) {
    
    /* TEMPLATE: everything in the template for ConsLoWord, plus
  
    Methods on parameters:
  ... this.sort() ...                         -- ILoWord
  ... this.insert(IWord that)...              -- ILoWord
  ... this.isSorted() ...                     -- Boolean
  ... this.isSortedHelp(IWord that)...        -- Boolean
  ... this.interleave(ILoWord that) ...       -- ILoWord
  ... this.merge(ILoWord that) ...            -- ILoWord
  ... this.checkAndReduce(String s) ...       -- ILoWord
  ... this.addToEnd(IWord that) ...           -- ILoWord
  ... this.filterOutEmpties() ...             -- ILoWord  
  ... this.draw(WorldScene ws) ...            -- WorldScene
  ... this.filterOutEmpties...                -- ILoWord
  ... this.move() ...                         -- ILoWord
  ... this.inactiveToActive() ...             -- ILoWord
  ... this.anyToReduce() ...                    -- Boolean
  ... this.lost() ...                         -- Boolean

    
   
    */
    
    return new ConsLoWord(this.first, that.interleave(this.rest));
  }
  
  //merges this sorted list of IWords and a given sorted list of IWords
  public ILoWord merge(ILoWord that) { 
    
    /* TEMPLATE: everything in the template for ConsLoWord, plus
    
    Methods on parameters:
  ... this.sort() ...                         -- ILoWord
  ... this.insert(IWord that)...              -- ILoWord
  ... this.isSorted() ...                     -- Boolean
  ... this.isSortedHelp(IWord that)...        -- Boolean
  ... this.interleave(ILoWord that) ...       -- ILoWord
  ... this.merge(ILoWord that) ...            -- ILoWord
  ... this.checkAndReduce(String s) ...       -- ILoWord
  ... this.addToEnd(IWord that) ...           -- ILoWord
  ... this.filterOutEmpties() ...             -- ILoWord  
  ... this.draw(WorldScene ws) ...            -- WorldScene
  ... this.filterOutEmpties...                -- ILoWord
  ... this.move() ...                         -- ILoWord
  ... this.inactiveToActive() ...             -- ILoWord
  ... this.anyToReduce() ...                    -- Boolean
  ... this.lost() ...                         -- Boolean

    
   
    */
    if (that.isSortedHelp(this.first)) {

      return new ConsLoWord(this.first, this.rest.merge(that));

    }

    else {

      return that.merge(this);
    }
  }

  // takes in a String of length 1 and produces an ILoWord where
  // any active words in this ILoWord are reduced by removing the first letter
  public ILoWord checkAndReduce(String s) {
    if (this.first.charIsEqual(s)) {

      return new ConsLoWord(this.first.shorten(), this.rest.checkAndReduce(s));
    }

    else {

      return new ConsLoWord(this.first, this.rest.checkAndReduce(s));
    }
  }

  // takes in an IWord and produces an ILoWord that is like this ILoWord
  // but with the given IWord added at the end
  public ILoWord addToEnd(IWord that) {
    
    /* TEMPLATE: everything in the template for ConsLoWord, plus
  
    Methods on parameters:
    ... this.comesBefore() ...                    -- Boolean
    ... this.comesBeforeHelp(IWord that)...       -- Boolean
    ... this.charIsEqual() ...                    -- Boolean
    ... this.shorten(ILoWord that) ...            -- IWord
    ... this.isEmptyString(ILoWord that) ...      -- Boolean
    ... this.drawWords(String s) ...              -- WorldScene
    ... this.drawText(IWord that) ...             -- TextImage
    ... this.inactiveToActive() ...               -- ILoWord
    ... this.anyToReduce() ...                      -- boolean
  
    */
    
  
      
    return new ConsLoWord(this.first, this.rest.addToEnd(that));
    
  }
  
  // produces an ILoWord with any IWords that have an empty string are filtered
  // out
  public ILoWord filterOutEmpties() {
    if (this.first.isEmptyString()) {

      return this.rest.filterOutEmpties();
    }

    else {

      return new ConsLoWord(this.first, this.rest.filterOutEmpties());
    }
  }

  // draws all of the words in this ILoWord onto the given WorldScene
  public WorldScene draw(WorldScene ws) {

    return this.first.drawWords(ws, this.rest);

  }

  // moves the y coordinates of each word by 1
  public ILoWord move() {
    return new ConsLoWord(this.first.move(), this.rest.move());

  }

  // changes a word with a given first letter to an ActiveWord
  public ILoWord inactiveToActive(String s) {
    if (this.first.checkEqual(s)) {

      return new ConsLoWord(this.first.inactiveToActiveHelper(s), this.rest);

    }

    else {

      return new ConsLoWord(this.first, this.rest.inactiveToActive(s));
    }

  }

  // determines whether or not an ILoWord has an ActiveWord
  public boolean anyToReduce() {

    return this.first.anyToReduce() || this.rest.anyToReduce();
  }

  // checks if the list contains any words with an empty string
  public boolean containsEmpty() {

    return this.first.isEmptyString();
  }

}

//represents a word in the ZType game
interface IWord {

  boolean comesBefore(IWord that); // checks if a word comes before a given word lexicographically

  boolean comesBeforeHelp(String otherWord); // to return either this word or the given otherWord

  boolean charIsEqual(String other); // checks if the first character of a string is equal to the
  // given string

  boolean checkEqual(String other); // checks if a string is equal to another string

  IWord shorten(); // shortens an active word by removing its first letter

  boolean isEmptyString(); // checks whether string is empty

  WorldScene drawWords(WorldScene ws, ILoWord that);

  TextImage drawText(); // creates a text image out of an active/inactive word

  IWord move(); // moves the y value of an IWord down one

  boolean lost(); // determines whether or not a player has lost the game, if y is above the
  // screen height

  IWord inactiveToActiveHelper(String s); // helps turn an IWord into an Active word

  boolean anyToReduce(); // determines whether or not a word is active
}

//represents an active word in the ZType game
class ActiveWord implements IWord {
  String word;
  int x;
  int y;
  

  ActiveWord(String word, int x, int y) {
    this.word = word;
    this.x = x;
    this.y = y;
  }

  /* TEMPLATE:
  
   Fields:
   ... this.word ...                             -- String
   ... this.x ...                                -- int
   ... this.y ...                                -- int
  
   Methods:
    
    ... this.comesBefore() ...                    -- Boolean
    ... this.comesBeforeHelp(IWord that)...       -- Boolean
    ... this.charIsEqual() ...                    -- Boolean
    ... this.shorten(ILoWord that) ...            -- IWord
    ... this.isEmptyString(ILoWord that) ...      -- Boolean
    ... this.drawWords(String s) ...              -- WorldScene
    ... this.drawText(IWord that) ...             -- TextImage
    ... this.inactiveToActiveHelper(String s)     -- IWord
    ... this.anyToReduce() ...                       -- boolean
    ... this.lost() ...                           -- boolean
    
    
    Methods on Fields:
   */ 
  
  
  //checks if a word comes before a given word lexicographically
  public boolean comesBefore(IWord that) {
    
    /* TEMPLATE: everything in the template for ConsLoWord, plus
    
    Methods on parameters:
    ... this.comesBefore() ...                    -- Boolean
    ... this.comesBeforeHelp(IWord that)...       -- Boolean
    ... this.charIsEqual() ...                    -- Boolean
    ... this.shorten(ILoWord that) ...            -- IWord
    ... this.isEmptyString(ILoWord that) ...      -- Boolean
    ... this.drawWords(String s) ...              -- WorldScene
    ... this.drawText(IWord that) ...             -- TextImage
    ... this.inactiveToActiveHelper(String s)     -- IWord
    ... this.anyToReduce() ...                       -- boolean
    ... this.lost() ...                           -- boolean
  
    */
    return that.comesBeforeHelp(this.word);

  }

  // moves the y value of an IWord down one
  public IWord move() {
    return new ActiveWord(this.word, this.x, this.y + 1);
  }

  // determines whether or not a player has lost the game
  public boolean lost() {
    return this.y >= (IZType.SCREEN_HEIGHT - 5);
  }

  // determines whether or not a word is active
  public boolean anyToReduce() {

    return true;
  }

  // to return either this word or the given otherWord
  public boolean comesBeforeHelp(String otherWord) {

    return this.word.toLowerCase().compareTo(otherWord.toLowerCase()) > 0;

  }

  // checks if a string is equal to another string
  public boolean checkEqual(String other) {

    return false;
  }

  // checks if the first character of a string is equal to the given string
  public boolean charIsEqual(String other) {

    return this.word.substring(0, 1).equals(other);
  }

  // shortens an active word by removing its first letter
  public IWord shorten() {

    return new ActiveWord(this.word.substring(1), this.x, this.y);

  }

  // checks whether string is empty
  public boolean isEmptyString() {

    return this.word.equals("");

  }
  
  //places text images of every word in a list at their given coordinates
  //on a given WorldScene
  public WorldScene drawWords(WorldScene ws, ILoWord that) {
    
    /* TEMPLATE: everything in the template for ActiveWord, plus
     * 
  
    Methods on parameters:
  ... this.sort() ...                         -- ILoWord
  ... this.insert(IWord that)...              -- ILoWord
  ... this.isSorted() ...                     -- Boolean
  ... this.isSortedHelp(IWord that)...        -- Boolean
  ... this.interleave(ILoWord that) ...       -- ILoWord
  ... this.merge(ILoWord that) ...            -- ILoWord
  ... this.checkAndReduce(String s) ...       -- ILoWord
  ... this.addToEnd(IWord that) ...           -- ILoWord
  ... this.filterOutEmpties() ...             -- ILoWord  
  ... this.draw(WorldScene ws) ...            -- WorldScene
  ... this.filterOutEmpties...                -- ILoWord
  ... this.move() ...                         -- ILoWord
  ... this.inactiveToActive() ...             -- ILoWord
  ... this.anyToReduce() ...                    -- Boolean
    
    */
    
    
    return that.draw(ws.placeImageXY(this.drawText(), this.x, this.y));
  }

  // creates a text image out of an active/inactive word
  public TextImage drawText() {

    return new TextImage(this.word, Color.BLUE);
  }

  // helps turn a IWord into an ActiveWord
  public IWord inactiveToActiveHelper(String s) {

    return this;
  }

}

//represents an inactive word in the ZType game
class InactiveWord implements IWord {
  String word;
  int x;
  int y;

  // moves the y coordinate of an IWord e down by 1
  public IWord move() {
    return new InactiveWord(this.word, this.x, this.y + 1);
  }

  // determines whether or not a player has lost the game, if y is above the
  // screen height
  public boolean lost() {
    return this.y >= (IZType.SCREEN_HEIGHT - 5);
  }

  InactiveWord(String word, int x, int y) {
    this.word = word;
    this.x = x;
    this.y = y;
  }
  
  /* TEMPLATE:
  
   Fields:
   ... this.word ...                             -- String
   ... this.x ...                                -- int
   ... this.y ...                                -- int
  
   Methods:
    ... this.comesBefore() ...                    -- Boolean
    ... this.comesBeforeHelp(IWord that)...       -- Boolean
    ... this.charIsEqual() ...                    -- Boolean
    ... this.shorten(ILoWord that) ...            -- IWord
    ... this.isEmptyString(ILoWord that) ...      -- Boolean
    ... this.drawWords(String s) ...              -- WorldScene
    ... this.drawText(IWord that) ...             -- TextImage
    ... this.inactiveToActiveHelper(String s)     -- IWord
    ... this.anyToReduce() ...                       -- boolean
    ... this.lost() ...                           -- boolean
  
    Methods on Fields:
  */
  
  //checks if a word comes before a given word lexicographically
  public boolean comesBefore(IWord that) {
    
    /* TEMPLATE: everything in the template for ConsLoWord, plus
    
    Methods on parameters:
    ... this.comesBefore() ...                    -- Boolean
    ... this.comesBeforeHelp(IWord that)...       -- Boolean
    ... this.charIsEqual() ...                    -- Boolean
    ... this.shorten(ILoWord that) ...            -- IWord
    ... this.isEmptyString(ILoWord that) ...      -- Boolean
    ... this.drawWords(String s) ...              -- WorldScene
    ... this.drawText(IWord that) ...             -- TextImage
    ... this.inactiveToActiveHelper(String s)     -- IWord
    ... this.anyToReduce() ...                       -- boolean
    ... this.lost() ...                           -- boolean
  
    */
    return that.comesBeforeHelp(this.word);
  }
  
  // checks if a word is active
  public boolean anyToReduce() {
    
    return false;
  }
  
  //to return either this word or the given otherWord
  public boolean comesBeforeHelp(String otherWord) {
    
    return this.word.toLowerCase().compareTo(otherWord.toLowerCase()) > 0;
      
  
  }
  
  //checks if the first character of a string is equal to the given string
  public boolean charIsEqual(String other) {
    
    return false;
  
  }
  
  // checks if two strings are equal
  public boolean checkEqual(String other) {
    
    return this.word.substring(0,1).equals(other);
  }
  
  //shortens an active word by removing its first letter
  public IWord shorten() {
    
    
    return this;
  }
  
  //checks whether string is empty
  public boolean isEmptyString() {
     
    return this.word.equals(""); 
  
  }
  
  //places text images of every word in a list at their given coordinates
  //on a given WorldScene
  public WorldScene drawWords(WorldScene ws, ILoWord that) {
    
    /* TEMPLATE: everything in the template for InactiveWord, plus
     * 
   
    Methods on parameters:
  ... this.sort() ...                         -- ILoWord
  ... this.insert(IWord that)...              -- ILoWord
  ... this.isSorted() ...                     -- Boolean
  ... this.isSortedHelp(IWord that)...        -- Boolean
  ... this.interleave(ILoWord that) ...       -- ILoWord
  ... this.merge(ILoWord that) ...            -- ILoWord
  ... this.checkAndReduce(String s) ...       -- ILoWord
  ... this.addToEnd(IWord that) ...           -- ILoWord
  ... this.filterOutEmpties() ...             -- ILoWord  
  ... this.draw(WorldScene ws) ...            -- WorldScene
  ... this.filterOutEmpties...                -- ILoWord
  ... this.move() ...                         -- ILoWord
  ... this.inactiveToActive() ...             -- ILoWord
  ... this.anyToReduce() ...                    -- Boolean
    
    */
    
    return that.draw(ws.placeImageXY(this.drawText(), this.x, this.y));
  }
  
  //creates a text image out of an active/inactive word
  public TextImage drawText() {
    
    return new TextImage(this.word, Color.RED);
  }
  
  // helps turn an IWord into an Active word
  public IWord inactiveToActiveHelper(String s) {
    
    if (this.word.substring(0,1).equals(s)) {
      
      
      return new ActiveWord(this.word.substring(1), this.x, this.y);
    }
    
    return this;
  }

}



//all examples and tests for ILoWord
class ExamplesZType {

  // empty constructor
  ExamplesZType() {
  }

  ILoWord emptyList = new MtLoWord();
  IWord word1 = new ActiveWord("Banana", 30, 10);
  IWord word1Down = new ActiveWord("Banana", 30, 11);

  IWord word2 = new ActiveWord("mango", 52, 10);
  IWord word2Down = new ActiveWord("mango", 52, 11);
  IWord word2NoM = new ActiveWord("ango", 52, 10);
  IWord word3 = new InactiveWord("Hello", 100, 10);
  IWord word3Down = new InactiveWord("Hello", 100, 11);
  IWord wordEnd = new InactiveWord("Hello", 100, 595);
  IWord wordEndDown = new InactiveWord("Hello", 100, 596);
  IWord word4 = new ActiveWord("bourgeoisie", 150, 10);
  IWord word5 = new InactiveWord("cat", 200, 10);
  IWord word6 = new InactiveWord("Spectacle", 78, 10);

  ILoWord l1 = new ConsLoWord(this.word1, this.emptyList);

  ILoWord l2 = new ConsLoWord(this.word1, new ConsLoWord(this.word2, this.emptyList));

  ILoWord l3 = new ConsLoWord(this.word1,
      new ConsLoWord(this.word2, new ConsLoWord(this.word3, this.emptyList)));
  ILoWord l3NoM = new ConsLoWord(this.word1,
      new ConsLoWord(this.word2NoM, new ConsLoWord(this.word3, this.emptyList)));
  ILoWord l3Down = new ConsLoWord(this.word1Down,
      new ConsLoWord(this.word2Down, new ConsLoWord(this.word3Down, this.emptyList)));
  ILoWord l3GameEnd = new ConsLoWord(this.word1,
      new ConsLoWord(this.word2, new ConsLoWord(this.wordEnd, this.emptyList)));
  ILoWord l3GameEndDown = new ConsLoWord(this.word1Down,
      new ConsLoWord(this.word2Down, new ConsLoWord(this.wordEndDown, this.emptyList)));

  ILoWord l4 = new ConsLoWord(this.word1, new ConsLoWord(this.word2,
      new ConsLoWord(this.word3, new ConsLoWord(this.word4, this.emptyList))));

  ILoWord l5 = new ConsLoWord(this.word1, new ConsLoWord(this.word2, new ConsLoWord(this.word3,
      new ConsLoWord(this.word4, new ConsLoWord(this.word5, this.emptyList)))));

  ILoWord l6 = new ConsLoWord(this.word1,
      new ConsLoWord(this.word2, new ConsLoWord(this.word3, new ConsLoWord(this.word4,
          new ConsLoWord(this.word5, new ConsLoWord(this.word6, this.emptyList))))));
  IZType emptyWorld = new ZTypeWorld(emptyList, 0, false, 0, 1);

  IZType world1 = new ZTypeWorld(l3, 0, false, 0, 1);

  IZType world1End = new ZTypeWorld(l3, 0, true, 0, 1);
  IZType world1NoM = new ZTypeWorld(l3NoM, 2, false, 0, 1);
  IZType world1Down = new ZTypeWorld(l3Down, 1, false, 0, 1);
  IZType world1AddNew = new ZTypeWorld(l3Down, 24, false, 0, 1);
  IZType world1GameEnd = new ZTypeWorld(l3GameEnd, 0, false, 0, 1);

  WorldScene ws = new WorldScene(400, 600)
      .placeImageXY(new RectangleImage(400, 600, "solid", Color.BLACK), 200, 300);

  // testing the method sort()
  public boolean testerSort(Tester t) {

    // an empty list is already sorted since there's no elements
    return t.checkExpect(this.emptyList.sort(), this.emptyList)

        // already sorted since there's only one element
        && t.checkExpect(this.l1.sort(), this.l1)

        // already sorted since both elements are in order
        && t.checkExpect(this.l2.sort(), this.l2)

        // tests for lists that are unsorted that become sorted

        && t.checkExpect(
            new ConsLoWord(word3,
                new ConsLoWord(new InactiveWord("", 189, 212), new ConsLoWord(word1, emptyList)))
                .sort(),
            new ConsLoWord(new InactiveWord("", 189, 212),
                new ConsLoWord(word1, new ConsLoWord(word3, emptyList))))

        && t.checkExpect(this.l3.sort(),
            new ConsLoWord(word1,
                new ConsLoWord(this.word3, new ConsLoWord(this.word2, this.emptyList))))

        && t.checkExpect(this.l4.sort(),
            new ConsLoWord(this.word1,
                new ConsLoWord(this.word4,
                    new ConsLoWord(this.word3, new ConsLoWord(this.word2, this.emptyList)))))

        && t.checkExpect(this.l5.sort(),
            new ConsLoWord(this.word1,
                new ConsLoWord(this.word4,
                    new ConsLoWord(this.word5,
                        new ConsLoWord(this.word3, new ConsLoWord(this.word2, this.emptyList))))))

        && t.checkExpect(this.l6.sort(),
            new ConsLoWord(this.word1,
                new ConsLoWord(this.word4, new ConsLoWord(this.word5, new ConsLoWord(this.word3,
                    new ConsLoWord(this.word2, new ConsLoWord(this.word6, this.emptyList)))))));

  }

  public boolean testerInsert(Tester t) {

    return t.checkExpect(new ConsLoWord(this.word2, this.emptyList).insert(this.word1), this.l2)

        && t.checkExpect(emptyList.insert(this.word1), this.l1)

        && t.checkExpect(this.l3.insert(this.word4),
            new ConsLoWord(this.word1, new ConsLoWord(this.word4,
                new ConsLoWord(this.word2, new ConsLoWord(this.word3, this.emptyList)))));

  }

  // testing the method isSorted()
  public boolean testerIsSorted(Tester t) {

    // tests for already sorted lists (empty lists count as being sorted
    return t.checkExpect(this.emptyList.isSorted(), true)

        && t.checkExpect(this.l1.isSorted(), true)

        && t.checkExpect(this.l2.isSorted(), true)

        && t.checkExpect(
            new ConsLoWord(new InactiveWord("", 189, 212),
                new ConsLoWord(this.word1, new ConsLoWord(this.word3, this.emptyList))).isSorted(),
            true)

        && t.checkExpect(new ConsLoWord(word1,
            new ConsLoWord(this.word4,
                new ConsLoWord(this.word3, new ConsLoWord(this.word2, this.emptyList))))
            .isSorted(), true)

        && t.checkExpect(
            new ConsLoWord(this.word1,
                new ConsLoWord(this.word4,
                    new ConsLoWord(this.word5, new ConsLoWord(this.word3,
                        new ConsLoWord(this.word2, new ConsLoWord(this.word6, this.emptyList))))))
                .isSorted(),
            true)

        // tests for unsorted lists
        && t.checkExpect(this.l3.isSorted(), false)

        && t.checkExpect(
            new ConsLoWord(word1,
                new ConsLoWord(word3,
                    new ConsLoWord(new InactiveWord("", 189, 212), this.emptyList)))
                .isSorted(),
            false)

        && t.checkExpect(this.l5.isSorted(), false)
        && t.checkExpect(
            new ConsLoWord(new ActiveWord("b", 10, 10),
                new ConsLoWord(new ActiveWord("b", 20, 20),
                    new ConsLoWord(new InactiveWord("b", 20, 20), this.emptyList)))
                .isSorted(),
            true);

  }

  // testing the method isSortedHelp()
  public boolean testerIsSortedHelp(Tester t) {

    return t.checkExpect(new ConsLoWord(this.word2, this.emptyList).isSortedHelp(this.word1), true)

        && t.checkExpect(new ConsLoWord(this.word1, this.emptyList).isSortedHelp(this.word1), true)

        && t.checkExpect(new ConsLoWord(this.word4, this.emptyList).isSortedHelp(this.word3), false)

        && t.checkExpect(
            new ConsLoWord(new InactiveWord("", 189, 212), this.emptyList).isSortedHelp(this.word1),
            false)

        && t.checkExpect(
            new ConsLoWord(this.word4, this.emptyList).isSortedHelp(new InactiveWord("", 189, 212)),
            true);

  }

  // testing the method interleave()
  public boolean testerInterleave(Tester t) {

    // tests in which first or second list is empty
    return t.checkExpect(this.emptyList.interleave(this.l1), this.l1)

        && t.checkExpect(this.emptyList.interleave(emptyList), emptyList)

        && t.checkExpect(this.l1.interleave(emptyList), this.l1)

        && t.checkExpect(this.emptyList.interleave(this.l2), this.l2)

        // tests with lists of different length
        && t.checkExpect(this.l2.interleave(this.l1), new ConsLoWord(this.word1,
            new ConsLoWord(this.word1, new ConsLoWord(this.word2, this.emptyList))))

        // tests with lists of same length
        && t.checkExpect(
            this.l4.interleave(new ConsLoWord(this.word1,
                new ConsLoWord(this.word3,
                    new ConsLoWord(this.word4, new ConsLoWord(this.word2, this.emptyList))))),
            new ConsLoWord(this.word1,
                new ConsLoWord(this.word1,
                    new ConsLoWord(this.word2,
                        new ConsLoWord(this.word3,
                            new ConsLoWord(this.word3,
                                new ConsLoWord(this.word4,
                                    new ConsLoWord(this.word4,
                                        new ConsLoWord(this.word2, this.emptyList)))))))))

        && t.checkExpect(this.l3.interleave(this.l1),
            new ConsLoWord(this.word1,
                new ConsLoWord(this.word1,
                    new ConsLoWord(this.word2, new ConsLoWord(this.word3, this.emptyList)))))

        && t.checkExpect(
            this.l5
                .interleave(new ConsLoWord(this.word1,
                    new ConsLoWord(this.word4, new ConsLoWord(this.word3,
                        new ConsLoWord(this.word2, this.emptyList))))),
            new ConsLoWord(this.word1,
                new ConsLoWord(this.word1,
                    new ConsLoWord(this.word2,
                        new ConsLoWord(this.word4,
                            new ConsLoWord(this.word3,
                                new ConsLoWord(this.word3,
                                    new ConsLoWord(this.word4,
                                        new ConsLoWord(this.word2,
                                            new ConsLoWord(this.word5, this.emptyList))))))))))

        && t.checkExpect(
            this.l6.interleave(new ConsLoWord(this.word1,
                new ConsLoWord(this.word4, new ConsLoWord(this.word3,
                    new ConsLoWord(this.word2, this.emptyList))))),
            new ConsLoWord(this.word1,
                new ConsLoWord(this.word1,
                    new ConsLoWord(this.word2,
                        new ConsLoWord(this.word4,
                            new ConsLoWord(this.word3,
                                new ConsLoWord(this.word3,
                                    new ConsLoWord(this.word4,
                                        new ConsLoWord(this.word2, new ConsLoWord(this.word5,
                                            new ConsLoWord(this.word6, this.emptyList)))))))))));

  }

  // testing the method merge()
  public boolean testerMerge(Tester t) {

    // tests with empty lists to merge
    return t.checkExpect(this.emptyList.merge(this.l1), this.l1)

        && t.checkExpect(this.emptyList.merge(emptyList), emptyList)

        && t.checkExpect(this.l1.merge(emptyList), this.l1)

        && t.checkExpect(this.emptyList.merge(this.l2), this.l2)

        // tests with non-empty lists to merge
        && t.checkExpect(this.l2.merge(this.l1),
            new ConsLoWord(this.word1,
                new ConsLoWord(this.word1, new ConsLoWord(this.word2, this.emptyList))))

        && t.checkExpect(
            new ConsLoWord(this.word1, new ConsLoWord(this.word3, new ConsLoWord(word2, emptyList)))
                .merge(this.l2),
            new ConsLoWord(this.word1,
                new ConsLoWord(this.word1,
                    new ConsLoWord(this.word3,
                        new ConsLoWord(this.word2, new ConsLoWord(this.word2, this.emptyList))))))

        && t.checkExpect(
            new ConsLoWord(this.word1,
                new ConsLoWord(this.word4,
                    new ConsLoWord(this.word5,
                        new ConsLoWord(this.word3, new ConsLoWord(this.word2, this.emptyList)))))
                .merge(new ConsLoWord(this.word1,
                    new ConsLoWord(this.word4, new ConsLoWord(this.word3,
                        new ConsLoWord(this.word2, this.emptyList))))),
            new ConsLoWord(this.word1,
                new ConsLoWord(this.word1,
                    new ConsLoWord(this.word4,
                        new ConsLoWord(this.word4,
                            new ConsLoWord(this.word5,
                                new ConsLoWord(this.word3,
                                    new ConsLoWord(this.word3,
                                        new ConsLoWord(this.word2,
                                            new ConsLoWord(this.word2, this.emptyList))))))))))

        && t.checkExpect(
            new ConsLoWord(this.word1,
                new ConsLoWord(this.word4, new ConsLoWord(this.word5,
                    new ConsLoWord(this.word3,
                        new ConsLoWord(this.word2, new ConsLoWord(this.word6, this.emptyList))))))
                .merge(new ConsLoWord(this.word1,
                    new ConsLoWord(this.word4, new ConsLoWord(this.word3,
                        new ConsLoWord(this.word6, this.emptyList))))),
            new ConsLoWord(this.word1,
                new ConsLoWord(this.word1,
                    new ConsLoWord(this.word4,
                        new ConsLoWord(this.word4,
                            new ConsLoWord(this.word5,
                                new ConsLoWord(this.word3,
                                    new ConsLoWord(this.word3,
                                        new ConsLoWord(this.word2, new ConsLoWord(this.word6,
                                            new ConsLoWord(this.word6, this.emptyList)))))))))));
  }

  // testing the method checkAndReduce()
  public boolean testerCheckAndReduce(Tester t) {

    // empty lists/blank strings tests
    return t.checkExpect(this.emptyList.checkAndReduce("h"), this.emptyList)

        && t.checkExpect(
            new ConsLoWord(new InactiveWord("", 100, 169), emptyList).checkAndReduce("z"),
            new ConsLoWord(new InactiveWord("", 100, 169), emptyList))

        // non-empty strings/non-blank strings tests
        && t.checkExpect(this.l1.checkAndReduce("j"), this.l1)

        && t.checkExpect(this.l1.checkAndReduce("b"),
            new ConsLoWord(new ActiveWord("Banana", 30, 10), this.emptyList))

        && t.checkExpect(this.l2.checkAndReduce("m"),
            new ConsLoWord(this.word1,
                new ConsLoWord(new ActiveWord("ango", 52, 10), this.emptyList)))

        && t.checkExpect(this.l3.checkAndReduce("h"), l3)

        && t.checkExpect(
            new ConsLoWord(new ActiveWord("h", 150, 10), emptyList).checkAndReduce("h"),
            new ConsLoWord(new ActiveWord("", 150, 10), emptyList))

        && t.checkExpect(this.l5.checkAndReduce("B"),
            new ConsLoWord(new ActiveWord("anana", 30, 10),
                new ConsLoWord(this.word2,
                    new ConsLoWord(this.word3,
                        new ConsLoWord(new ActiveWord("bourgeoisie", 150, 10),
                            new ConsLoWord(this.word5, this.emptyList))))));

  }

  // testing the method addToEnd()
  public boolean testerAddToEnd(Tester t) {

    return t.checkExpect(this.emptyList.addToEnd(this.word2),
        new ConsLoWord(this.word2, this.emptyList))

        && t.checkExpect(this.l1.addToEnd(this.word1),
            new ConsLoWord(this.word1, new ConsLoWord(this.word1, this.emptyList)))

        && t.checkExpect(this.l2.addToEnd(new ActiveWord("monday", 162, 50)),
            new ConsLoWord(this.word1,
                new ConsLoWord(this.word2,
                    new ConsLoWord(new ActiveWord("monday", 162, 50), this.emptyList))))

        && t.checkExpect(this.l5.addToEnd(this.word6), this.l6);

  }

  // testing the method filterOutEmpties()
  public boolean testerFilterOutEmpties(Tester t) {

    return t.checkExpect(new ConsLoWord(new ActiveWord("", 50, 60), emptyList).filterOutEmpties(),
        emptyList)

        && t.checkExpect(this.l1.filterOutEmpties(), l1)

        && t.checkExpect(this.emptyList.filterOutEmpties(), emptyList)

        && t.checkExpect(this.l2.filterOutEmpties(), this.l2)

        && t.checkExpect(
            new ConsLoWord(this.word2,
                new ConsLoWord(new ActiveWord("", 58, 103),
                    new ConsLoWord(this.word4, this.emptyList)))
                .filterOutEmpties(),
            new ConsLoWord(this.word2, new ConsLoWord(this.word4, this.emptyList)))

        && t.checkExpect(
            new ConsLoWord(this.word1,
                new ConsLoWord(new ActiveWord("", 88, 12), new ConsLoWord(this.word5,
                    new ConsLoWord(new ActiveWord("", 102, 167),
                        new ConsLoWord(this.word2, new ConsLoWord(this.word4, this.emptyList))))))
                .filterOutEmpties(),
            new ConsLoWord(this.word1,
                new ConsLoWord(this.word5,
                    new ConsLoWord(this.word2, new ConsLoWord(this.word4, this.emptyList)))))

        && t.checkExpect(
            new ConsLoWord(this.word1,
                new ConsLoWord(this.word2,
                    new ConsLoWord(this.word3,
                        new ConsLoWord(this.word4, new ConsLoWord(this.word5,
                            new ConsLoWord(this.word6,
                                new ConsLoWord(new ActiveWord("", 110, 39), this.emptyList)))))))
                .filterOutEmpties(),
            this.l6);

  }

  // testing the method draw()
  public boolean testerDraw(Tester t) {

    // test with no words present
    return t.checkExpect(this.emptyList.draw(ws),
        new WorldScene(400, 600).placeImageXY(new RectangleImage(400, 600, "solid", Color.BLACK),
            200, 300))

        // tests with active words, inactive words, or a mix of both
        && t.checkExpect(this.l1.draw(ws),
            new WorldScene(400, 600)
                .placeImageXY(new RectangleImage(400, 600, "solid", Color.BLACK), 200, 300)
                .placeImageXY(new TextImage("Banana", Color.BLUE), 30, 10))

        && t.checkExpect(this.l2.draw(ws),
            new WorldScene(400, 600)
                .placeImageXY(new RectangleImage(400, 600, "solid", Color.BLACK), 200, 300)
                .placeImageXY(new TextImage("Banana", Color.BLUE), 30, 10)
                .placeImageXY(new TextImage("mango", Color.BLUE), 52, 10))

        && t.checkExpect(this.l3.draw(ws),
            new WorldScene(400, 600)
                .placeImageXY(new RectangleImage(400, 600, "solid", Color.BLACK), 200, 300)
                .placeImageXY(new TextImage("Banana", Color.BLUE), 30, 10)
                .placeImageXY(new TextImage("mango", Color.BLUE), 52, 10)
                .placeImageXY(new TextImage("Hello", Color.RED), 100, 10))

        && t.checkExpect(this.l4.draw(ws),
            new WorldScene(400, 600)
                .placeImageXY(new RectangleImage(400, 600, "solid", Color.BLACK), 200, 300)
                .placeImageXY(new TextImage("Banana", Color.BLUE), 30, 10)
                .placeImageXY(new TextImage("mango", Color.BLUE), 52, 10)
                .placeImageXY(new TextImage("Hello", Color.RED), 100, 10)
                .placeImageXY(new TextImage("bourgeoisie", Color.BLUE), 150, 10))

        && t.checkExpect(this.l5.draw(ws),
            new WorldScene(400, 600)
                .placeImageXY(new RectangleImage(400, 600, "solid", Color.BLACK), 200, 300)
                .placeImageXY(new TextImage("Banana", Color.BLUE), 30, 10)
                .placeImageXY(new TextImage("mango", Color.BLUE), 52, 10)
                .placeImageXY(new TextImage("Hello", Color.RED), 100, 10)
                .placeImageXY(new TextImage("bourgeoisie", Color.BLUE), 150, 10)
                .placeImageXY(new TextImage("cat", Color.RED), 200, 10))

        && t.checkExpect(this.l6.draw(ws),
            new WorldScene(400, 600)
                .placeImageXY(new RectangleImage(400, 600, "solid", Color.BLACK), 200, 300)
                .placeImageXY(new TextImage("Banana", Color.BLUE), 30, 10)
                .placeImageXY(new TextImage("mango", Color.BLUE), 52, 10)
                .placeImageXY(new TextImage("Hello", Color.RED), 100, 10)
                .placeImageXY(new TextImage("bourgeoisie", Color.BLUE), 150, 10)
                .placeImageXY(new TextImage("cat", Color.RED), 200, 10)
                .placeImageXY(new TextImage("Spectacle", Color.RED), 78, 10));

  }

  // testing the method comesBefore()
  public boolean testerComesBefore(Tester t) {

    return t.checkExpect(this.word1.comesBefore(this.word2), true)

        && t.checkExpect(this.word1.comesBefore(this.word1), false)

        && t.checkExpect(this.word4.comesBefore(this.word3), true)

        && t.checkExpect(new InactiveWord("", 189, 212).comesBefore(this.word1), true);

  }

  // testing the method comesBeforeHelp()
  public boolean testerComesBeforeHelp(Tester t) {

    return t.checkExpect(this.word2.comesBeforeHelp("Banana"), true)

        && t.checkExpect(this.word1.comesBeforeHelp("Banana"), false)

        && t.checkExpect(this.word6.comesBeforeHelp("cat"), true)

        && t.checkExpect(new InactiveWord("", 189, 212).comesBeforeHelp("vinny"), false);

  }

  // tests for the method charIsEqual
  public boolean testerCharIsEqual(Tester t) {

    return t.checkExpect(this.word2.charIsEqual("B"), false)

        && t.checkExpect(this.word1.charIsEqual("B"), true)

        && t.checkExpect(this.word4.charIsEqual(""), false);

  }

  // tests for the method shorten()
  public boolean testerShorten(Tester t) {

    return t.checkExpect(this.word2.shorten(), new ActiveWord("ango", 52, 10))

        && t.checkExpect(this.word6.shorten(), this.word6)

        && t.checkExpect(new InactiveWord("", 189, 212).shorten(), new InactiveWord("", 189, 212));

  }

  // tests for the method isEmptyString()
  public boolean testerIsEmptyString(Tester t) {

    return t.checkExpect(this.word2.isEmptyString(), false)

        && t.checkExpect(new ActiveWord("", 218, 199).isEmptyString(), true)

        && t.checkExpect(this.word6.isEmptyString(), false)

        && t.checkExpect(new InactiveWord("", 189, 212).isEmptyString(), true);

  }

  // tests for the method drawWords()
  public boolean testerDrawWords(Tester t) {

    return t.checkExpect(this.word1.drawWords(ws, this.emptyList),
        new WorldScene(400, 600)
            .placeImageXY(new RectangleImage(400, 600, "solid", Color.BLACK), 200, 300)
            .placeImageXY(new TextImage("Banana", Color.BLUE), 30, 10))

        && t.checkExpect(this.word1.drawWords(ws, new ConsLoWord(this.word2, emptyList)),
            new WorldScene(400, 600)
                .placeImageXY(new RectangleImage(400, 600, "solid", Color.BLACK), 200, 300)
                .placeImageXY(new TextImage("Banana", Color.BLUE), 30, 10)
                .placeImageXY(new TextImage("mango", Color.BLUE), 52, 10))

        && t.checkExpect(
            this.word1.drawWords(ws,
                new ConsLoWord(this.word2, new ConsLoWord(this.word3, emptyList))),
            new WorldScene(400, 600)
                .placeImageXY(new RectangleImage(400, 600, "solid", Color.BLACK), 200, 300)
                .placeImageXY(new TextImage("Banana", Color.BLUE), 30, 10)
                .placeImageXY(new TextImage("mango", Color.BLUE), 52, 10)
                .placeImageXY(new TextImage("Hello", Color.RED), 100, 10))

        && t.checkExpect(
            this.word1.drawWords(ws,
                new ConsLoWord(this.word2,
                    new ConsLoWord(this.word3, new ConsLoWord(this.word4, emptyList)))),
            new WorldScene(400, 600)
                .placeImageXY(new RectangleImage(400, 600, "solid", Color.BLACK), 200, 300)
                .placeImageXY(new TextImage("Banana", Color.BLUE), 30, 10)
                .placeImageXY(new TextImage("mango", Color.BLUE), 52, 10)
                .placeImageXY(new TextImage("Hello", Color.RED), 100, 10)
                .placeImageXY(new TextImage("bourgeoisie", Color.BLUE), 150, 10))

        && t.checkExpect(
            this.word1.drawWords(ws,
                new ConsLoWord(this.word2,
                    new ConsLoWord(this.word3,
                        new ConsLoWord(this.word4, new ConsLoWord(this.word5, emptyList))))),
            new WorldScene(400, 600)
                .placeImageXY(new RectangleImage(400, 600, "solid", Color.BLACK), 200, 300)
                .placeImageXY(new TextImage("Banana", Color.BLUE), 30, 10)
                .placeImageXY(new TextImage("mango", Color.BLUE), 52, 10)
                .placeImageXY(new TextImage("Hello", Color.RED), 100, 10)
                .placeImageXY(new TextImage("bourgeoisie", Color.BLUE), 150, 10)
                .placeImageXY(new TextImage("cat", Color.RED), 200, 10))

        && t.checkExpect(
            this.word1.drawWords(ws,
                new ConsLoWord(this.word2,
                    new ConsLoWord(this.word3,
                        new ConsLoWord(this.word4,
                            new ConsLoWord(this.word5, new ConsLoWord(this.word6, emptyList)))))),
            new WorldScene(400, 600)
                .placeImageXY(new RectangleImage(400, 600, "solid", Color.BLACK), 200, 300)
                .placeImageXY(new TextImage("Banana", Color.BLUE), 30, 10)
                .placeImageXY(new TextImage("mango", Color.BLUE), 52, 10)
                .placeImageXY(new TextImage("Hello", Color.RED), 100, 10)
                .placeImageXY(new TextImage("bourgeoisie", Color.BLUE), 150, 10)
                .placeImageXY(new TextImage("cat", Color.RED), 200, 10)
                .placeImageXY(new TextImage("Spectacle", Color.RED), 78, 10));

  }

  // tests for the method drawText()
  public boolean testerDrawText(Tester t) {

    return t.checkExpect(this.word1.drawText(), new TextImage("Banana", Color.BLUE))

        && t.checkExpect(this.word2.drawText(), new TextImage("mango", Color.BLUE))

        && t.checkExpect(this.word5.drawText(), new TextImage("cat", Color.RED))

        && t.checkExpect(new InactiveWord("", 189, 212).drawText(), new TextImage("", Color.RED));

  }

  // testing the makeWord() method
  public boolean testMakeWord(Tester t) {

    Utils rand1 = new Utils(new Random(11));
    Utils rand2 = new Utils(new Random(50));
    Utils rand3 = new Utils(new Random(100));

    return t.checkExpect(rand1.makeWord(), "slfih") && t.checkExpect(rand2.makeWord(), "nsmbll")
        && t.checkExpect(rand3.makeWord(), "aynqql");

  }

  // testing the makeWordHelper() method
  public boolean testMakeWordHelper(Tester t) {

    // creating three Random objects with different seed values
    Utils rand1 = new Utils(new Random(11));
    Utils rand2 = new Utils(new Random(50));
    Utils rand3 = new Utils(new Random(100));

    return t.checkExpect(rand1.makeWordHelper("a", 6), "anslfi")
        && t.checkExpect(rand2.makeWordHelper("", 5), "rnsmb")
        && t.checkExpect(rand3.makeWordHelper("he", 7), "hepaynq");

  }

  // testing the makeScene() method
  public boolean testMakeScene(Tester t) {

    IZType world1 = new ZTypeWorld(l6, 10, false, 0, 1);
    IZType world2 = new ZTypeWorld(l5, 10, false, 0, 1);
    IZType world3 = new ZTypeWorld(l4, 10, false, 0, 1);

    return t.checkExpect(world1.makeScene(),
        new WorldScene(400, 600).placeImageXY(new TextImage("Banana", Color.BLUE), 30, 10)
            .placeImageXY(new TextImage("mango", Color.BLUE), 52, 10)
            .placeImageXY(new TextImage("Hello", Color.RED), 100, 10)
            .placeImageXY(new TextImage("bourgeoisie", Color.BLUE), 150, 10)
            .placeImageXY(new TextImage("cat", Color.RED), 200, 10)
            .placeImageXY(new TextImage("Spectacle", Color.RED), 78, 10)
            .placeImageXY(new TextImage("Score: 0", 12, Color.BLACK), 365, 590)
            .placeImageXY(new TextImage("Level: 1", 12, Color.BLACK), 25, 590).placeImageXY(
                new OverlayImage(new CircleImage(6, OutlineMode.SOLID, Color.CYAN),
                    new OverlayImage(new CircleImage(10, OutlineMode.SOLID, Color.BLACK),
                        new EquilateralTriangleImage(50, OutlineMode.SOLID, Color.lightGray))),
                200, 580))

        && t.checkExpect(world2.makeScene(),
            new WorldScene(400, 600).placeImageXY(new TextImage("Banana", Color.BLUE), 30, 10)
                .placeImageXY(new TextImage("mango", Color.BLUE), 52, 10)
                .placeImageXY(new TextImage("Hello", Color.RED), 100, 10)
                .placeImageXY(new TextImage("bourgeoisie", Color.BLUE), 150, 10)
                .placeImageXY(new TextImage("cat", Color.RED), 200, 10)
                .placeImageXY(new TextImage("Score: 0", 12, Color.BLACK), 365, 590)
                .placeImageXY(new TextImage("Level: 1", 12, Color.BLACK), 25, 590).placeImageXY(
                    new OverlayImage(new CircleImage(6, OutlineMode.SOLID, Color.CYAN),
                        new OverlayImage(new CircleImage(10, OutlineMode.SOLID, Color.BLACK),
                            new EquilateralTriangleImage(50, OutlineMode.SOLID, Color.lightGray))),
                    200, 580))

        && t.checkExpect(world3.makeScene(),
            new WorldScene(400, 600).placeImageXY(new TextImage("Banana", Color.BLUE), 30, 10)
                .placeImageXY(new TextImage("mango", Color.BLUE), 52, 10)
                .placeImageXY(new TextImage("Hello", Color.RED), 100, 10)
                .placeImageXY(new TextImage("bourgeoisie", Color.BLUE), 150, 10)
                .placeImageXY(new TextImage("Score: 0", 12, Color.BLACK), 365, 590)
                .placeImageXY(new TextImage("Level: 1", 12, Color.BLACK), 25, 590).placeImageXY(
                    new OverlayImage(new CircleImage(6, OutlineMode.SOLID, Color.CYAN),
                        new OverlayImage(new CircleImage(10, OutlineMode.SOLID, Color.BLACK),
                            new EquilateralTriangleImage(50, OutlineMode.SOLID, Color.lightGray))),
                    200, 580));

  }

  // testing the inactiveToActive(String s)
  public boolean testInactiveToActive(Tester t) {

    return t.checkExpect(this.l3.inactiveToActive("H"),
        new ConsLoWord(this.word1,
            new ConsLoWord(this.word2,
                new ConsLoWord(new ActiveWord("ello", 100, 10), this.emptyList))))
        && t.checkExpect(this.emptyList.inactiveToActive("a"), this.emptyList)
        && t.checkExpect(this.l5.sort().inactiveToActive("c"),
            new ConsLoWord(this.word1,
                new ConsLoWord(this.word4,
                    new ConsLoWord(new ActiveWord("at", 200, 10),
                        new ConsLoWord(this.word3, new ConsLoWord(this.word2, this.emptyList))))))
        && t.checkExpect(this.l6.inactiveToActive("x"), this.l6);

  }

  // testing the InactiveToActiveHelper(String s) method
  public boolean testInactiveToActiveHelper(Tester t) {

    return t.checkExpect(this.word2.inactiveToActiveHelper("M"), this.word2)
        && t.checkExpect(new InactiveWord("e", 289, 10).inactiveToActiveHelper("e"),
            new ActiveWord("", 289, 10))
        && t.checkExpect(this.word4.inactiveToActiveHelper("b"),
            new ActiveWord("bourgeoisie", 150, 10));

  }

  // testing the lost() method that is in the ILoWord interface and the IWord
  // interface
  public boolean testMove(Tester t) {

    return t.checkExpect(this.l3.move(),
        new ConsLoWord(new ActiveWord("Banana", 30, 11),
            new ConsLoWord(new ActiveWord("mango", 52, 11),
                new ConsLoWord(new InactiveWord("Hello", 100, 11), this.emptyList))))
        && t.checkExpect(this.word4.move(), new ActiveWord("bourgeoisie", 150, 11))
        && t.checkExpect(
            new ConsLoWord(this.word2,
                new ConsLoWord(new InactiveWord("jelly", 166, 26),
                    new ConsLoWord(new InactiveWord("harry", 475, 178), this.emptyList)))
                .move(),
            new ConsLoWord(new ActiveWord("mango", 52, 11),
                new ConsLoWord(new InactiveWord("jelly", 166, 27),
                    new ConsLoWord(new InactiveWord("harry", 475, 179), this.emptyList))))
        && t.checkExpect(this.emptyList.move(), this.emptyList);

  }

  // testing the anyToReduce() method
  public boolean testIsActive(Tester t) {

    return t.checkExpect(this.word1.anyToReduce(), true)
        && t.checkExpect(this.word3.anyToReduce(), false)
        && t.checkExpect(new ActiveWord("", 226, 200).anyToReduce(), true);
  }

  // testing the anyToReduce() method
  public boolean testAnyToReduce(Tester t) {

    return t.checkExpect(this.l2.anyToReduce(), true)
        && t.checkExpect(this.emptyList.anyToReduce(), false)
        && t.checkExpect(
            new ConsLoWord(new InactiveWord("ohlord", 229, 300),
                new ConsLoWord(new InactiveWord("heyy", 333, 33), this.emptyList)).anyToReduce(),
            false)
        && t.checkExpect(
            new ConsLoWord(new InactiveWord("ohlord", 229, 300),
                new ConsLoWord(new InactiveWord("heyy", 333, 33),
                    new ConsLoWord(new ActiveWord("sure", 199, 51), this.emptyList)))
                .anyToReduce(),
            true);
  }

  // testing the lost() method that is in the ILoWord interface and the IWord
  // interface
  public boolean testLost(Tester t) {

    return t.checkExpect(this.l6.lost(), false)
        && t.checkExpect(
            new ConsLoWord(this.word1,
                new ConsLoWord(this.word4,
                    new ConsLoWord(new ActiveWord("baseball", 152, 599), this.emptyList)))
                .lost(),
            true)
        && t.checkExpect(new ConsLoWord(this.word1,
            new ConsLoWord(this.word4,
                new ConsLoWord(new ActiveWord("rat", 245, 590), this.emptyList)))
            .lost(), false)
        && t.checkExpect(this.emptyList.lost(), false)
        && t.checkExpect(new InactiveWord("range", 300, 596).lost(), true)
        && t.checkExpect(new ActiveWord("pizza", 277, 591).lost(), false);
  }

  // testing the checkEqual(String s) method
  public boolean testCheckEqual(Tester t) {

    return t.checkExpect(this.word3.checkEqual("h"), false)

        && t.checkExpect(this.word5.checkEqual("c"), true)

        && t.checkExpect(this.word2.checkEqual("m"), false)

        && t.checkExpect(this.word6.checkEqual(""), false);

  }

  // testing the onKeyEvent(String key) method
  public boolean testOnKeyEvent(Tester t) {

    return t.checkExpect(world1.onKeyEvent("r"), world1)

        && t.checkExpect(world1.onKeyEvent("m"), world1NoM)

        && t.checkExpect(world1.onKeyEvent("t"), world1);

  }

  // testing the onTickForTesting() method
  public boolean testOnTickForTesting(Tester t) {

    System.out.println(world1.onTickForTesting());
    Utils u = new Utils(new Random(10));

    return t.checkExpect(world1.onTickForTesting(), new ZTypeWorld(this.l3.move(), 2, false, 0, 1))
        && t.checkExpect(world1AddNew.onTickForTesting(),
            new ZTypeWorld(
                this.l3Down.addToEnd(new InactiveWord(u.makeWord(), u.wordPlacer(), 10).move()), 25,
                false, 0, 1))
        && t.checkExpect(world1GameEnd.onTickForTesting(),
            new ZTypeWorld(this.l3GameEnd.move(), 1, true, 0, 1));

  }

  // testing the finalScene() method
  public boolean testFinalScene(Tester t) {

    return t
        .checkExpect(world1.finalScene(),
            new WorldScene(IZType.SCREEN_WIDTH, IZType.SCREEN_HEIGHT)
                .placeImageXY(new RectangleImage(IZType.SCREEN_WIDTH, IZType.SCREEN_HEIGHT, "solid",
                    Color.BLACK), 200, 300)
                .placeImageXY(new TextImage("Game Over!", 24, FontStyle.BOLD, Color.WHITE),
                    IZType.SCREEN_WIDTH / 2, IZType.SCREEN_HEIGHT / 2 - 20)
                .placeImageXY(new TextImage("Final Score: 0", 22, FontStyle.BOLD, Color.WHITE),
                    IZType.SCREEN_WIDTH / 2, IZType.SCREEN_HEIGHT / 2 + 20)
                .placeImageXY(new TextImage("Level Reached: 1", 22, FontStyle.BOLD, Color.WHITE),
                    IZType.SCREEN_WIDTH / 2, IZType.SCREEN_HEIGHT / 2 + 60)
                .placeImageXY(new TextImage("(click to restart)", 18, FontStyle.BOLD, Color.WHITE),
                    IZType.SCREEN_WIDTH / 2, IZType.SCREEN_HEIGHT / 2 + 200));

  }


  // the big bang method
  public boolean testBigBang(Tester t) {

    ZTypeWorld world = new ZTypeWorld(this.emptyList, 0, false, 0, 1);
    int worldWidth = 400;
    int worldHeight = 600;
    double tickRate = .05;
    return world.bigBang(worldWidth, worldHeight, tickRate);

  }


}
