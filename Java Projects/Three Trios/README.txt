# Three Trios

## OVERVIEW:

The Three Trios game is a turn-based strategy game where two players (RED and BLUE) compete to
place cards on a grid, flip their opponent’s cards, and ultimately control the majority of the grid.
The goal is to "win" over neighboring cards and flip them to your own color, creating a chain
reaction of flips based on the game’s rules.

This codebase implements the core mechanics of the Three Trios game, including the grid setup,
card interactions, turn-based player mechanics, and card flipping logic. It provides the necessary
model structure to simulate the game and manage game state, grid, and player actions.

----------------------------------------------------------------------------------------------------
## ASSUMPTIONS AND PREREQUISITES:

Assumptions: Players are familiar with the concept of turn-based strategy games and grid-based
systems.

Prerequisites: Basic understanding of Java, object-oriented programming, and file-based
configuration.
The game requires two configuration files: one for the grid layout and another for the card deck
configuration.

Extensibility: The code is built in a modular manner, allowing for extensions like custom card
types, different grid sizes, or additional game rules.

----------------------------------------------------------------------------------------------------
## QUICK START

Here is a simple example of how to instantiate and start a game of Three Trios:

public class Main {
    public static void main(String[] args) {
        TTModel game = new ThreeTriosModel();
        game.startGame("path/to/gridConfig.txt", "path/to/cardConfig.txt");

        // Player actions
        game.placeCard(0, 1, 1); // Place the first card of Player.RED at position (1, 1)
        System.out.println("Current turn: " + game.getTurn()); // Get current player

        if (game.isGameOver()) {
            System.out.println("Winning Player: " + game.findWinningPlayer());
        }
    }
}

In this example:
The game is initialized with two configuration files.
The first card from the RED player’s hand is placed at grid position (1,1).
The game checks for the winner once all cells are filled.

----------------------------------------------------------------------------------------------------
## KEY COMPONENTS

1. Model
The ThreeTriosModel class is the main driver of the game mechanics. It handles the logic for:
- Starting the game (with a grid and deck configuration).
- Managing player turns and actions.
- Flipping cards based on interactions with neighboring cards.

2. View
The TextualView provides a console-based rendering of the game. It allows players to view the
current game state, the positions of cards, and the progression of the game as they make moves.
- TTView.java: Interface for the view, defining how the game state is displayed.
- TextualView.java: Implements the TTView interface and provides a textual representation of
                    the game.

2. Grid
The grid (Grid.java) represents the game board. It is composed of cells that can either be CardCells
(which hold cards) or Holes (empty spaces). The grid determines where cards can be placed and
interacts with the model to evaluate card flipping based on neighbors.

3. Cards
Cards (Card.java) represent the units placed by players. They contain attributes like attack values
and player ownership. Cards can "win" over adjacent cards based on their attributes and the
direction of placement (North, South, East, West).

4. Player
The Player.java class represents the two players in the game: RED and BLUE. Each player has their
own hand of cards, and the game alternates between them on each turn.

----------------------------------------------------------------------------------------------------
## KEY SUBCOMPONENTS

1. CardCell.java & Hole.java
CardCell: A cell on the grid that can hold a card.
Hole: An empty space on the grid where no card can be placed.

2. Card.java
Each card has an attack value (represented by AttackValue.java) and belongs to a specific player.
Cards interact with each other based on attack values and directions.

3. TTModel.java
The interface that defines the core methods for interacting with the game model (e.g., starting
the game, placing cards, determining the winner).

4. Grid.java
Handles grid management, including populating the grid from the configuration file, determining
valid placements, and interacting with the cards.

----------------------------------------------------------------------------------------------------
## SOURCE ORGANIZATION
src/cs3500/model Directory: Contains all model-related files, including the game grid, cards,
cells, and player logic.

ThreeTriosModel.java: The main model class that handles the game mechanics.

Card.java: Defines the card objects and their attributes.

Grid.java: Represents the game board and its cells.

CardCell.java: Represents a cell that can hold a card.

Hole.java: Represents an empty, unusable cell on the grid.

Player.java: Represents the two players in the game.

TTModel.java: Interface for the game model.

TTReadOnlyModel.java: Interface providing read-only access to the model.

AttackValue.java: Defines the attack value logic for each card.

--------------------------------------------------------
src/cs3500/view Directory: Contains view-related files.

TTView.java: Interface for rendering the game state.

TextualView.java: Implements TTView and renders the game state in a textual format.

--------------------------------------------------------
/test Directory: Contains unit tests for the game components.

ThreeTriosModelTests.java: Tests for the game model, verifying game logic, turn handling,
card placement, and flipping mechanics.

implementation.CardTest.java: Tests for the card object, ensuring attack values and interactions are correct.

implementation.AttackValueTests.java: Tests for the attack values and how they affect card interactions.

implementation.TTCellTest.java: Tests for the individual cells on the grid, particularly CardCell and Hole.

/test/cs3500/model Directory:

GridTests.java: Tests for the grid structure and cell placements.
----------------------------------------------------------------------------------------------------
## GRID CONFIGURATION AND CARD CONFIGURATION
The game is initialized with a configuration file that describes the layout of the grid.
This file specifies:

C: A card cell.
X: A hole or blocked space.

Example of a simple grid configuration:
C X C
C C C
X C X

Cards are configured in a file where each line defines a card with attributes (e.g., attack values).

Example card configuration:

BlazeTail 7 A 6 2
AquaBlade 4 8 1 2
NightStalker 9 9 9 9
SkyWarden 8 A 1 2
FrostBite A 8 6 4
---------------------------------------------------------------------------------------------------
## CHANGES FOR PART 2:
- Added getters for the rows and columns of the grid, as this was very useful for the setup of
  the view.
- We added methods getContentsAt(row, col) and getPlayerAt(row, col), which return the contents
  of the cell at the given row and column, and the player who owns the card in that cell,
  respectively. These allow the view to have an easier time rendering the model.
- We added a method getScore(Player player) to the model, which returns the score of the given
  player. This was useful in determining who was currently winning
- We added a new method, countFlip, that calculates possible flips by placing a card in a given
  position. This was useful in determining the best move for the AI player.
- Improved Javadoc for clarity and info
---------------------------------------------------------------------------------------------------
## NEW CLASSES FOR PART 2:
- AIPlayer.java: This class represents an AI player that can play the game. It implements the
  AIPlayerStrategy interface to determine the best move based on the current game state.
- TTGUIView: The interface for our GUI view, holding a method to update the view before displaying.
- GUIView.java: This class represents a GUI view for the game. It extends the JFrame class and
  implements the TTView interface to render the game state in a graphical user interface.
- GridPanel: Builds the grid component for our JFrame, holding cells and various behaviors.
- SidePanel: Builds the components for the two hands to be represented, holding behaviors to ensure
  cards are highlighted and appropriately selected.
- AbstractPanel: Holds common behaviors between GridPanel and SidePanel to minimize code
  duplication.
- TTMouseListener: A mouse listener for this class that handles highlighting and printing the row
  and column of each selected cell.
---------------------------------------------------------------------------------------------------
## CHANGES FOR PART 3:
- AbstractController.java: Holds the common behaviors between the Human and AI controllers to
  minimize code duplication.
- HumanController.java: Represents a human player controller that interacts with the game model
  and view based on user input.
- AIController.java: Represents an AI player controller that interacts with the game model and view.
- ViewModel.java: An adapter class that ensures that the view cannot cast the model to a mutable
  model
- TTModelFeatures.java: An interface that holds the necessary behaviors for the model to interact
  with the view through the controller.
- TTViewFeatures.java: An interface that holds the necessary behaviors for the view to interact
  with the controller.
- PlayerActions.java: An interface that holds the necessary behaviors for a player to interact with
  the controller and game model.
- HumanPlayer.java: Represents a human player that can interact with the game controller.
- AIPlayer.java: Represents an AI player that can interact with the game controller.
- AIStrategy.java: Broke our strategies into an interface that follows the strategy pattern,
  allowing for implementations of different AI strategies.
- BestDefense.java: An AI strategy that focuses on minimizing the opponent's score.
- CheckCorners.java: An AI strategy that focuses on controlling the corners of the grid.
- MaxFlip.java: An AI strategy that focuses on maximizing the number of flips.
---------------------------------------------------------------------------------
