Go bach to [homepage](Home)

Chess is a GUI application programmed in Java and maintained with Maven.

# Used programs and libraries

- OpenJDK 13 - programming language ([Oracle](https://openjdk.java.net/))
- Maven - program for automation and project handling ([Apache](https://maven.apache.org/))
- Graphviz - tool for generating class diagrams ([Graphviz](https://www.graphviz.org/))
- JavaFX 13 - graphics java library ([JavaFX](https://openjfx.io/))
- GSON - java library for JSON serialization ([GSON](https://github.com/google/gson))

# Structure

Main class is **Chess** and its purpose is to launch the application and hold references
to all controllers that handle separate views and other globally shared data such as
*primary stage* and *paths* to file locations.

The remaining classes are divided into three cathegories which follow the *MVC* pattern:

- controller
- model
- view

## Controllers

Controllers are designed to interpret user input and perform changes to model and view.
They are saved as **static** properties in **Chess** class to avoid passing multiple controllers
to next controller unnecesarily.

Each controller also holds its references for view and model. View is usually instantiated
in the constructor of the controller and then reset on swtiching scenes, rather than
creating new instance each time.

It follows the convention that for each view/page there is one controller to handle it.

## Model

Model classes holds data of the application. Apart from **Settings**, other model classes
are set to controllers when needed and not in their constructors. **Settings**, on the other hand,
follow the *Singleton* pattern to make them available throughout whole application without the need
to pass them by argument to every object.

The central model for most of the application is **Game** class that does not do much on
its own, but wraps around a lot of other models and provides access to them.
For example, a **Game** contains **Board** (matrix 8x8 of Pieces), **MoveControl** (it scans the board
for possible moves performed by selected icon), and both **Players**. It also provides switching
**Players** after each round.

The *pgn* package contains everyting related to the standard PGN format. It provides
utilities to create and parse PGN tags (only the 7 mandatory + FEN for non-standard 
starts, see ([PGN](http://www.saremba.de/chessgml/standards/pgn/pgn-complete.htm))),
it contains recorder to observe played game, browser and loader to read *.pgn* files.

Chess pieces are realized as classes that extends **Piece** abstract class. That makes
possible to store them in one matrix and approach all the pieces the same.

## View

View classes provide visual representation of application's model. The all inherit
from **View** abstract class that for now contains only abstract function to put pieces
into *root* pane. This package also contains utility class ViewUtil, which holds
reference to *scene* that is permanently placed in *primary stage*. Changing view than works
as swapping root panels of that *scene*.

Almost whole application takes place in *primary stage*, except for **PromotionView** that
shows selection of **Piece** to promote to from **Pawn**. This view has its own stage
because it is implemented as modal dialogue.

The most compicated **View** is **BoardView**. It mirrors the **Board** matrix in model
and provides interactive chessboard that can be placed in any other **View**.
It consists of matrix of **Cell** objects, that shows **Piece** icons and overlays
for targets. This objects provides the *"clickable"* elements on the chessboard.
It can also hold move type, when in target mode.

View classes serve as layout managers. Styling of elements should be done in *.css*
files in *resources*.

# Custom layout

Application provides creating initial layouts before game. For this purpose there
is **BoardChecker** that checks the board if it is valid before enabling to *confirm*
the layout and start game.

It is also defined that when creating custom layout, there is no possibility to
castle. Behind this decision is uncertainty about whether the **Rook** and **King**
pieces were moved or not.

## Board validity

- there has to be exactly one **white King** and one **black King**
- **Pawns** cannot reach first rank of their color (White on rank 1 and Black on rank 8)
- The sum of **non-king** pieces has to be less or equals to 15 while considering all possible promotions

# Threads

There are three threads in this application. Main thread for most of functions and
two separate threads for **RoundTimer**. Time controll is implemented as time limit
for one player to conduct all moves and terminate the game. If the time runs out,
the player curently on the move automatically loses. That results in having one
**RoundTimer** for each player. The timer has methods **pause**, that stops countdown by setting
the *pause* property to **true**, and **proceed**, that resumes the countdown where it stopped by
setting the *pause* property to **false**. When method **end** is called, the Thread becomes inactive
and is terminated when possible.

This is one of two cases when view is manipulated directly by model and not through controller.
The **RoundTimer** uses **Platform.runLater()** functionality to make changes on the main
thread.

# Settings

Settings, as mentioned, are a *Singleton*. They also provides functionality to save
it to external file. **JAR** cannot be modified while running so settings are saved
into *settings/settings.json* file in the same directory as the **JAR**.

*GSON* library is used for the serialization and it is made through class. All properties
has to be saved in **SerializedObject** class in order to serialize them. The same
happens in deserialization when all data from the JSON are put into new **SerializedObject**.
Desired properties have to be *getted* from that object.

# PGN

As Settings, **PGN** files have to be saved outside the **JAR**. They are divided into
*data/finished/* and *data/continue/* directories based on result of the game.
When result is **UNKNOWN**, then a game is considered unfinished and was saved before
proper game end, otherwise it is considered finished.

Data are saved into *.pgn* files in format defined ([here](http://www.saremba.de/chessgml/standards/pgn/pgn-complete.htm)).

Browsing games firstly creates game with parameters given by tags in the file.
Then on command it decodes next move and makes appropriate changes to model and view.
After running out of moves it waits for exit.

Loading games works very similarly to browsing, only view is not modified during loading
but only after handover of the created **Game** object to **GameController**.
It, however, still needs to make all moves saved in the *.pgn* file. Loader also
has to check if castling is still possible after all the moves. It also has to
detect if player was *computer* or *human* (that is not defined in the format).
In this application **ComputerPlayer** always has *"Computer"* as *surname* so
when parsing this *surname* from the file, the Loader assumes it was *computer*.

## Computer player

Computer player has method that retrieves move that is to be played as by the computer.
In this implementation it selects random piece, which can perform legal move, and random move for it.

This **CompleteMove** object is give to controller. Controller simulates click
events on appropriate squares to conduct the move.

Also coputer cannot accept draw so the function to offer draw is disabled.
