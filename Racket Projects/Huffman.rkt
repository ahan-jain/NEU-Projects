import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Collections;
import tester.*;

//represents the Huffman prefix coding algorithm
class Huffman {
  ArrayList<String> stringList;
  ArrayList<Integer> integerList;
  ArrayList<AForest> forest;

  Huffman(ArrayList<String> stringList, ArrayList<Integer> integerList) {
    
    //checks if the two lists of alphabets and frequencies respectively are of the same length
    if (stringList.size() != integerList.size()) {

      throw new IllegalArgumentException("lists are of different sizes");
    }

    //checks if the string list's size if lesser than 2, since there wouldn't be too much
    //of a point to encode a 1-letter string
    else if (stringList.size() < 2) {

      throw new IllegalArgumentException("list of strings less than 2");
    }

    this.stringList = stringList;
    this.integerList = integerList;
    this.forest = new ArrayList<AForest>();

    //constructs the list of leaves once the lists have passed the conditions
    for (int i = 0; i < stringList.size(); i++) {
      forest.add(new Leaf(stringList.get(i), integerList.get(i)));

    }

  }


  //constructs the forest
  public ArrayList<AForest> makeForest() {
    while (forest.size() > 1) {
      Collections.sort(forest, new ByFrequency());
      AForest lowerNode = forest.remove(0);
      AForest higherNode = forest.remove(0);
      forest.add(new Node(lowerNode, higherNode));
    }
    return forest;
  }

  //encodes a string into a list of booleans that contain the path of the letters in the forest,
  //with false depicting a 0, and true depicting a 1 in binary code
  public ArrayList<Boolean> encode(String input) {
    forest = this.makeForest();
    ArrayList<Boolean> arr = new ArrayList<Boolean>();

    while (!input.isEmpty()) {

      arr.addAll(forest.get(0).getPath(input.substring(0, 1), new ArrayList<Boolean>()));
      input = input.substring(1);
    }

    return arr;
  }

  //decodes a list of booleans containing the path of the letters into a string,
  //where false dictates to go to the left in the forest and true dictates to go to the 
  //right in order to retrieve the letter
  public String decode(ArrayList<Boolean> arr) {
    forest = this.makeForest();
    String decoded = "";
    while (!arr.isEmpty()) {
      
      decoded = decoded + this.forest.get(0).decodeHelper(arr);

    }

    return decoded;

  }

}

//to represent a tree of data, where the data is composed of characters and their 
//frequencies
abstract class AForest {
  public int frequency;

  AForest(int frequency) {

    this.frequency = frequency;
  }

  //gets the path of the given letter in the forest in the form of a list of booleans
  public abstract ArrayList<Boolean> getPath(String input, ArrayList<Boolean> arr);

  //determines whether the given letter is present in a specific node of a forest or a leaf 
  public abstract boolean isPresent(String input);

  //finds the character in the forest with the help of the list of booleans indicating the path
  //to find the letter, and returns "?" if you run out of booleans before you 
  //can reach a terminal node in the tree
  public abstract String decodeHelper(ArrayList<Boolean> arr);

  public int addFreq(AForest that) {
    return this.frequency + that.frequency;
  }
}

//represents a node in the forest
class Node extends AForest {
  AForest left;
  AForest right;

  Node(AForest left, AForest right) {

    super(left.addFreq(right));
    this.left = left;
    this.right = right;

  }

  //gets the path of the given letter in the forest in the form of a list of booleans
  public ArrayList<Boolean> getPath(String input, ArrayList<Boolean> arr) {

    if (this.left.isPresent(input)) {
      arr.add(false);
      this.left.getPath(input, arr);
    }

    else if (this.right.isPresent(input)) {

      arr.add(true);
      this.right.getPath(input, arr);

    }

    else {

      throw new IllegalArgumentException(
          "Tried to encode " + input + " but that is not part of the language.");
    }

    return arr;

  }
  
  //determines whether the given letter is present in a specific node of a forest or a leaf 
  public boolean isPresent(String input) {

    return this.left.isPresent(input) || this.right.isPresent(input);

  }

  //finds the character in the forest with the help of the list of booleans indicating the path
  //to find the letter, and returns "?" if you run out of booleans before you 
  //can reach a terminal node in the tree
  public String decodeHelper(ArrayList<Boolean> arr) {
    if (arr.size() == 0) {
      return "?";
    }

    else if (arr.remove(0)) {
      return this.right.decodeHelper(arr);
    }
    else {
      return this.left.decodeHelper(arr);
    }

  }

}

//represents a leaf in the forest
class Leaf extends AForest {
  String character;

  Leaf(String character, int frequency) {
    super(frequency);
    this.character = character;
  }

  //determines whether the given letter is present in a specific node of a forest or a leaf 
  public boolean isPresent(String input) {

    return this.character.equals(input);

  }
  
  //gets the path of the given letter in the forest in the form of a list of booleans
  public ArrayList<Boolean> getPath(String input, ArrayList<Boolean> arr) {

    return arr;
  }

  //finds the character in the forest with the help of the list of booleans indicating the path
  //to find the letter, and returns "?" if you run out of booleans before you 
  //can reach a terminal node in the tree
  public String decodeHelper(ArrayList<Boolean> arr) {

    return character;
  }

}

//to compute the comparison between two parts of a forest 
class ByFrequency implements Comparator<AForest> {
  
  // Returns a positive number if t1 is greater than t2 in this order
  // Returns zero              if t1 is equal to t2 in this order
  // Returns a negative number if t1 is lesser than t2 in this order
  public int compare(AForest t1, AForest t2) {
    if (t1.frequency > t2.frequency) {
      return 1;

    }
    if (t1.frequency == t2.frequency) {
      return 0;

    }
    else {
      return -1;
    }

  }

}

//examples and tests
class ExamplesHuffman {

  Leaf a;
  Leaf b;
  Leaf c;
  Leaf d;
  Leaf e;
  Leaf f;
  
  Leaf g;
  Leaf h;
  Leaf i;
  Leaf j;
  Leaf k;
  Leaf l;
  
  Node n1;
  Node n2;
  Node n3;
  ArrayList<String> stringList;
  ArrayList<Integer> intList;
  ArrayList<String> stringList2;
  ArrayList<Boolean> boolList;
  ArrayList<Boolean> boolList2;
  ArrayList<Boolean> boolList3;
  ArrayList<Boolean> boolList4;
  Huffman testHuff;
  Huffman testHuff2;
  Huffman testHuff3;
  Huffman testHuff4;
  Huffman testHuff5;

  //initializes values
  public void reset() {

    stringList = new ArrayList<String>(Arrays.asList("a", "b", "c", "d", "e", "f"));
    intList = new ArrayList<Integer>(Arrays.asList(12, 45, 5, 13, 9, 16));
    

    stringList2 = new ArrayList<String>(Arrays.asList("g", "h", "i", "j", "k", "l"));
    
   
    boolList = new ArrayList<Boolean>(Arrays.asList(false, true, false));
    boolList2 = new ArrayList<Boolean>();
    boolList3 = new ArrayList<Boolean>(
        Arrays.asList(false, true, false, false, false, false, false, true));
    boolList4 = new ArrayList<Boolean>(
        Arrays.asList(true, true, false, false, false, false, false, true, false, true));
    
    testHuff = new Huffman(stringList, intList);
    
    testHuff2 = new Huffman(stringList, intList);
    

    testHuff3 = new Huffman(new ArrayList<String>(Arrays.asList("a", "b")),
        new ArrayList<Integer>(Arrays.asList(12, 45)));
    
    
    testHuff5 = new Huffman(stringList2, intList);

    a = new Leaf("a", 12);
    b = new Leaf("b", 45);
    c = new Leaf("c", 5);
    d = new Leaf("d", 13);
    e = new Leaf("e", 9);
    f = new Leaf("f", 16);
    
    g = new Leaf("g", 12);
    h = new Leaf("h", 45);
    i = new Leaf("i", 5);
    j = new Leaf("j", 13);
    k = new Leaf("k", 9);
    l = new Leaf("l", 16);
  

    n1 = new Node(a, c);
    n2 = new Node(b, e);
    n3 = new Node(d, f);

  }



  //tests the makeForest method
  public void testMakeForest(Tester t) {
    reset();
    AForest lowerNode;
    AForest higherNode;
    t.checkExpect(testHuff, testHuff2);
    
    //no matter what methods are applied on these two, they are different because they have
    //different alphabets even though they have the same corresponding frequencies
    t.checkFail(testHuff, testHuff5);
    t.checkFail(testHuff2, testHuff5);

    while (testHuff2.forest.size() > 1) {
      Collections.sort(testHuff2.forest, new ByFrequency());
      lowerNode = testHuff2.forest.remove(0);
      higherNode = testHuff2.forest.remove(0);
      testHuff2.forest.add(new Node(lowerNode, higherNode));
    }
    testHuff.makeForest();
    
    //checks if a forest is the same as one that is identically constructed
    t.checkExpect(testHuff, testHuff2);
    
   
    t.checkFail(testHuff3, testHuff4);
    
    testHuff4 = new Huffman(new ArrayList<String>(Arrays.asList("a", "b")),
        new ArrayList<Integer>(Arrays.asList(12, 45)));
    
    while (testHuff3.forest.size() > 1) {
      
      Collections.sort(testHuff3.forest, new ByFrequency());
      lowerNode = testHuff3.forest.remove(0);
      higherNode = testHuff3.forest.remove(0);
      testHuff3.forest.add(new Node(lowerNode, higherNode));
    }
    
    testHuff4.makeForest();
    
    //different lengths, alphabets and sizes
    t.checkFail(testHuff4, testHuff5);

    
    //checks if a forest is the same as one that is identically constructed
    t.checkExpect(testHuff3, testHuff4);//checks if a forest

    testHuff2.makeForest();
    t.checkExpect(testHuff2, testHuff2);//checks if the forest is the same as itself

  }

  //tests the encode method
  public void testEncode(Tester t) {
    reset();
    t.checkExpect(testHuff.encode(""), new ArrayList<Boolean>());//empty string will stay the same
    t.checkExpect(testHuff.encode("abc"),
        new ArrayList<Boolean>(Arrays.asList(true, false, false, false, true, 
            true, false, false)));//encodes successfully
    
    //fails for g in one list since its not present but passes for the forest that 
    //actually has it
    t.checkException(
        new IllegalArgumentException("Tried to encode g but that is not part of the language."),
        testHuff, "encode", "ghi");//p is an alphabet but not in the forest
    t.checkExpect(testHuff5.encode("gg"),
        new ArrayList<Boolean>(Arrays.asList(true, false, false, true, false, false)));
    
    t.checkException(
        new IllegalArgumentException("Tried to encode 0 but that is not part of the language."),
        testHuff, "encode", "0");//a number is not an alphabet

  }

  //tests the decode method
  public void testDecode(Tester t) {
    reset();
    
    //runs out of booleans
    t.checkExpect(new Huffman(stringList, intList).decode(boolList), "b?");

    //no path given in the list of booleans so nothing will be decoded
    t.checkExpect(new Huffman(stringList, intList).decode(boolList2), "");

    //runs out of booleans
    t.checkExpect(new Huffman(stringList, intList).decode(boolList3), "babbb?");
    
    
    //different results for different forests
    t.checkExpect(new Huffman(stringList, intList).decode(boolList4), "cbbbd");
    
    reset();
    t.checkExpect(new Huffman(stringList2, intList).decode(boolList4), "ihhhj");
    
  }
  

  //tests the getPath method
  public void testGetPath(Tester t) {
    reset();
    testHuff.makeForest();
    ArrayList<AForest> forest = testHuff.forest;

    t.checkExpect(forest.get(0).getPath("a", new ArrayList<Boolean>()),
        new ArrayList<Boolean>(Arrays.asList(true, false, false)));//successfully finds path
    t.checkException(
        
        //i is an alphabet but not in the forest
        new IllegalArgumentException("Tried to encode i but that is not part of the language."),
        forest.get(0), "getPath", "i", new ArrayList<Boolean>());
    
    //i is an alphabet AND in the forest
    t.checkExpect(testHuff5.forest.get(0).getPath("i", new ArrayList<Boolean>()),
        new ArrayList<Boolean>());
    
    
    t.checkException(
        new IllegalArgumentException("Tried to encode 0 but that is not part of the language."),
        forest.get(0), "getPath", "0", new ArrayList<Boolean>());//a number is not an alphabet

  }

  //tests the isPresent method
  public void testIsPresent(Tester t) {
    reset();
    testHuff.makeForest();
    ArrayList<AForest> forest = testHuff.forest;
    AForest leaf = new Leaf("x", 15);
    t.checkExpect(forest.get(0).isPresent("b"), true);//"b" is in the forest
    t.checkExpect(forest.get(0).isPresent("x"), false);//"x" is not in the forest
    reset();
    testHuff.forest.add(leaf);
    testHuff.makeForest();
    t.checkExpect(testHuff.forest.get(0).isPresent("x"), true);//"x" is NOW in the forest
    t.checkExpect(leaf.isPresent("p"), false);//"x" and "p" are different characters
    t.checkExpect(leaf.isPresent("x"), true);
    t.checkExpect(leaf.isPresent(""), false);//empty string does not count as being present

  }

  //tests the decodeHelper method
  public void testDecodeHelper(Tester t) {
    reset();
    testHuff.makeForest();
    ArrayList<AForest> forest = testHuff.forest;
    AForest leaf = new Leaf("x", 15);
    
    //returns the character in the leaf in the leaf class regardless of path
    t.checkExpect(leaf.decodeHelper(new ArrayList<Boolean>(Arrays.asList(true, false))), "x");
    t.checkExpect(leaf.decodeHelper(new ArrayList<Boolean>(Arrays.asList())), "x");
    
    //ran out of booleans
    t.checkExpect(forest.get(0).decodeHelper(new ArrayList<Boolean>(Arrays.asList(true, false))),
        "?");
    t.checkExpect(
        //successfully found letter
        forest.get(0).decodeHelper(new ArrayList<Boolean>(Arrays.asList(true, false, false))), "a");

  }

  //tests the Huffman constructor
  public void testHuffman(Tester t) {
    t.checkConstructorException(new IllegalArgumentException("list of strings less than 2"),
        "Huffman", new ArrayList<String>(), new ArrayList<Integer>());
    t.checkConstructorException(new IllegalArgumentException("lists are of different sizes"),
        "Huffman", new ArrayList<String>(Arrays.asList("g", "j", "k")),
        new ArrayList<Integer>(Arrays.asList(1, 2)));
    t.checkConstructorException(new IllegalArgumentException("lists are of different sizes"),
        "Huffman", new ArrayList<String>(Arrays.asList("a", "b", "c")),
        new ArrayList<Integer>(Arrays.asList(1)));

  }
  
  //tests the addFreq method
  public void testAddFreq(Tester t) {
    reset();
    t.checkExpect(n1.addFreq(c), 22);//adds a node's and a leaf's frequencies
    t.checkExpect(d.addFreq(f), 29);//adds two leaves' frequencies
    t.checkExpect(n3.addFreq(n2), 83);//adds two nodes' frequencies
  }

}
