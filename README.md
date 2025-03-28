# NEU-Projects
Some code i've worked on so far at Northeastern University as a Computer Science Major :D

*The following programs are some of the projects that I coded in DrRacket during my first semester as a student in the accelerated section of Fundamentals of Computer Science I (CS 2500), and I used the languages isl+ and asl.*

**Shannon_Game:** This was the course project which we developed throughout the semester. It is a game that allows a user to look at a digital circuit with some pre-existing input and output gates and prompts it to input some components of a grid (i.e. a conductive plate, a logical gate or an empty cell), which triggers a visual representation of the propogation of positive and/or negative charge in the circuit according to the components the user has inputted. The goal is to see of their version of the grid can match a set of 'goals', i.e. the given starting charges of the input gates should become the expected output charge by end of the propogation of charge. For the time being, all of these things are hard coded and nothing is user prompted unless the code is modified.

**Calculator:** A small "calculator," which has the same functionality as a calculator, but can support user input and "memory" operations (storing, recalling, and clearing). It does so using continuation-passing style (CPS), a style of programming in which control is passed explicitly in the form of a continuation.

**Chat Program:** A chat program that connects to a single server (a Northeastern Server) that is shared by all students in the class. The program is able to show a basic visual representation of a chat room, where one can send and recieve messages from other students, as well as block particular students and turn on and off rate limiting.

**Photo Shoot:** A game which simulates a remote controlled photo shoot. In it, a stationary photographer begins the photo shoot on the ground, with some part of the ground as their goal to capture. They will then launch a drone with a camera and fly it. The shoot ends when the drone can capture the whole surface area of the goal... or it crashes.

**Simple Version of NY Times Spelling Bee:** Spelling Bee is a vocabulary game hosted by the New York Times. This is a miniature version that implements similar aspects to the online game, but has some restrictions, such as the ability to enter nonsensical words, not being able to correct mistakes (i.e., backspace will not work), being able to enter the same word several times, and no score tracking.

**StateLang Implementation:** A subset of Advanced Student Language in DrRacket, StateLang is a lang we created in class that has the same functionality as ASL but was implemented by us in isl+. It consists of functions, numbers, variable mutation, and box mutation. This program implements concepts like stores/memory, locations, boxes, and threading state through evaluation.

*The following programs are some of the projects that I coded in Java during my second semester as a student in Fundamentals of Computer Science II (CS 2510).*

**Minesweeper:** This was one of our course projects which we worked on during the semester. It's a fully functional version of the original Minesweeper game, with the ability to flag cells, the "flood fill" effect that reveals all non-bombed cells around a selected blank cells, etc. In addition, I also implemented the ability to choose difficulty levels (with each level changing the number of mines and the dimensions of the minesweeper grid).

**Huffman Algorithm:** This program implemented the Huffman prefix coding algorithm on data trees.

**ZType:** ZType is the other course project we worked on during the semester. It is a fun and educational typing game where you shoot down enemy spaceships by typing the words that appear on them (in my game however, the program generates random strings of characters rather than pre-defined words). As you type each letter of the word correctly, your spaceship fires at the enemies. The faster and more accurately you type, the quicker you destroy the ships. It's a great way to improve your typing speed while enjoying a space-shooter game! I implemented features like some basic fun graphics, a scoreboard to track the number of words you've successfully typed, the ability to restart the game, and words that have random variable lengths to make the game more interesting.

*The following programs is/are project(s) that I coded in Java during my third semester as a student in Object Oriented Design (CS 3500).*


**Solo Red: ** This was my course project for the semester. Solo Red is a solitaire-style card game inspired by Red7, which follows a structured set of rules based on colors, numbers, and winning conditions. In the game, the player must continuously modify their hand and canvas (the cards they have played) to ensure they are always winning according to the active game rule.
