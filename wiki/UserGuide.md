Go bach to [homepage](Home)

# Installation and execution

For application to run it is necessary to have *Java* installed (*JRE* version is sufficient).
This application is written to run on *Java 13*.
It can be downloaded either proprietary from [Oracle](https://www.oracle.com/java/), but they require registration, or the opensource [OpenJDK](https://openjdk.java.net/).

After that this application can be run by executing `chess.jar` file.

# Game controls

Whole application can be controlled by clicking with mouse on buttons or squares on the chessboard.
Sometimes a dialogue pops up to select file or a chess piece.

Most of buttons have self-explanatory labels and are described later on.

## Move on chessboard

Moves on chessboard are made by **click-click** method.

When a player is on move, pieces of his color are **active**.
This is signaled by change of background of the clicked square if it contains **allied piece**.
When is another piece clicked on after one pices is selected, it changes the selection
and highlights **the last one**.

After selecting an **allied piece** on some empty or enemy squares are displayed
circles that signal that these squares are potential targets of the move and the selected
chessman can be moved there. If **no circle overlay** is shown, it means that there is **no possible**
move for the selected piece. In that case **another piece** has to be selected.

When clicked on a targetable square, the selected ally piece is moved to the targeted location.
For some special moves another action happens (see **Moves**).

# Rules

The game provides control of all rules of chess including time control, pawn promotions,
en passant and castlings.

## Basic rules

White and Black players take turns moving **one pice at the time** (except for **castling**) and trying
to trap opponent's **King** into *"inescapable capture"*. Each round player has
to make move, otherwise the game is lost. Game can end by several other ways (see **End**).

## Board

Board is a matrix of 8x8 sqaures with alternating light and dark squares (top-left square is white).
Horizontal positions are called **files** and are labeled from *a* to *h*.
Vertical positions are called **ranks** and are labeled from *1* to *8*.
Black starts on Rank 8 and White starts on Rank 1.

## Start

Game usually starts in *Standard starting position*:

- 2x **Rooks** are placed on the outside corners
- 2x **Knights** are placed immediately inside of Rooks
- 2x **Bishops** are placed immediately inside of Knights
- 1X **Queen** is placed next to one of the Bishops on square with hers color
- 1x **King** is placed on vacant square next to Queen
- 8x **Pawn** are placd one square in front of other Pieces

## Moves

Piece can be moved on any empty square or replace opponent's piece. Replacing opponents piece is called *capture*.
There are also special moves that involves more pieces. On the path made by the piece
there cannot be opponent's piece as well. King cannot be caputured, only put into check.

- **King** moves exactly one square vertically, horizontally or diagonally
    - special move **Castling**
- **Queen** moves any number of vacant sqaures vertically, horizontally or diagonally
- **Rook** moves any number of vacant squares vertically or horizontally
- **Knight** moves two squares vertically or horizontally and then one square in perpendicular direction (it is not blocked by other pieces - can *"jump over them"*))
- **Bishop** moves any number of vacant squares diagonally
- **Pawn** moves exactly one square forward
    - if Pawn was not moved yet, it can move two squares forward
    - if an opponent's piece is one square forward diagonally from the Pawn, it can capture it and move to its position
    - special moves **en passant**, **Promotion**

### Castling

If these conditions are met, King can move two squares in direction to Rook and then the Rook is moved next to the King in the opposite direction:

- King and Rook has not been moved yet
- There are no pieces between them
- King cannot be in check or travel through square that is under attack

Castlings are divided into **Kingside** and **Queenside** depending on the used Rook.

### EnPassant

If these conditions are met, Pawn can move one square forward on the same file as opponent's Pawn and capture it even though it is not replaced on the board:

- Pawns has to be on the same Rank
- Opponent's Pawn has moved two squares forward the previous opponent's move

### Promotion

If a Pawn moves to the end of board opposite of its starting position it is immediately replaced by new piece of the same color.
This move is called Pawn promotion.

Pieces Pawn can be promoted to:

- **Rook**
- **Knight**
- **Bishop**
- **Queen**

## Check

A situation when allied **King** is under attack by at least one opponent's piece is called **Check**.
It is illegal to make moves that would place or leave **King** in **Chech**.
Player has to get out of **Check** immediately in the next move, otherwise the game ends in **Checkmate**.

## End

Game can end in multiple results

### Checkmate

When a player is **checkmated** the game ends and the player loses. The other one wins.

### Resigning

Any player can resign at any time. It result in player's loss and other player's win.

### Draw

There are some possible states of game resulting in draw

- **Stalemate** - player is not in check, but cannot make any legal move
- **Insufficient material** - it is impossible to deliver checkmate for both players
    - King vs King
    - King vs King and Bishop
    - King vs King and Knight
    - King and Bishop vs King and Bishop with both bishops on the same color
- Players agree on the draw

### Time control

When player runs out of time, he automatically loses the game.

# Time

Each player has limited amount of time to perform all his moves and end the game.
Time countdowns only when player is playing, it is paused during opponent's moves.

# Sections

## Main page

Main page offers buttons to start any action that the application offers.

## Settings

In settings there is yet only one possible setting present and that is length of time control.
it indicates how many minutes one player has to perform **all** moves and end the game.
Settings is selected by moving sliders thumb next to label with desired setting.

There are three possible settings:

- Blitz - 5 minutes
- Rapid - 25 minutes
- Long - 60 minutes

When a new settings is selected it is persistable only in this run of the application.
To preserve the settings for future use of the application press button *Save*.

## New Game

Start game shows page full of test fields and radio buttons. Here a new game is
configurated. Text fields can be either filled (recommended) or left empty.

By default the game will be started as **player-player** game.
If game **player-computer** is desired, the checkbox in the middle should be checked.
By radio buttons that appear is determined for which **color** plays *human* player.

Starg game button either starts game with *Standard layout* or if the *Custom layout* button
was toggled, goes to *creation of custom layout*.

### Custom layout

There is empty board ready to be filled by pieces.

There is plenty of buttons by which is selected what piece to place next.
Color and piece buttons are not dependent on each other and can create all combinations
of pieces and colors. (Note that for Empty piece selected color does not matter)

When clicked on a square, piece with paramters selected by the buttons is placed
on the board on the clicked square.

When created board is valid, button to *confirm layout* is enabled. If it is not
valid, there is a message that says why.

### Game

Moves are performed as described in **Game Control**.

There are other buttons to make other actions with the game than move pieces.

- **Save** saves current state of game into *.pgn* file in *data/* folder
- **Exit** exits to main page without saving
- **Resign** ends game in loss of player whose move is it
- **Offer Draw** pauses the game and timers, waits for other player to respond
    - **Accept Draw** ends the game in draw
    - **Decline Draw** resumes the game with player who offered draw

When the game ends it is automatically saved into a *.pgn* file.

In top-right corner is displayed remaining time for each player.

Below time control are listed made moves in **PGN** format.

## Browse PGN

This functinality servers to browse games saved in **PGN** format.

First a file chooser pops up and a valid *.pgn* file has to be selected to proceed.
If *.pgn* file is selected a chessboard is shown with starting position of the browsed game.
Next to the board are shown information about the game extracted from the file.

By clicking the **Next** button a next move is performed on the board. For each move
a starting and target squares are hightlighted.

When reached last move the button is disabled and this functionality ends

## Load Game

This functionality loads game saved in **PGN** format, and continues game where
it ended.

After selecting file to load. It continues same as when playing new game.
