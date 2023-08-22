package pjv.sp.chess.controller;

import pjv.sp.chess.Chess;
import pjv.sp.chess.model.Continue;
import pjv.sp.chess.model.Game;
import pjv.sp.chess.model.Result;
import pjv.sp.chess.model.board.File;
import pjv.sp.chess.model.board.Rank;
import pjv.sp.chess.model.pgn.Disambiguation;
import pjv.sp.chess.model.pgn.PGNRecorder;
import pjv.sp.chess.model.pieces.Color;
import pjv.sp.chess.model.pieces.Move;
import pjv.sp.chess.model.pieces.MoveType;
import pjv.sp.chess.model.pieces.Piece;
import pjv.sp.chess.model.pieces.Pawn;
import pjv.sp.chess.model.pieces.Position;
import pjv.sp.chess.model.player.CompleteMove;
import pjv.sp.chess.model.player.Player;
import pjv.sp.chess.model.settings.Settings;
import pjv.sp.chess.model.time.RoundTimer;
import pjv.sp.chess.view.GameView;
import pjv.sp.chess.view.ViewUtil;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Game controller interprets inputs from user and requests appropriate changes
 * on both view and model. It also handles states of the game and its behaviour.
 * @author Jakub Rada
 * @version 1.0
 */
public class GameController {

    private static final Logger LOG = Logger.getLogger(GameController.class.getName());

    /**
     * View that visualizes the game
     */
    private final GameView gameView;

    /**
     * Model that holds representation and values of the game
     */
    private Game game;

    /**
     * Model that is resposible for recording and saving performed moves in
     * PGN format.
     */
    private PGNRecorder pgn;

    /**
     * Property that holds timer for each player
     */
    private final List<RoundTimer> timers = new ArrayList<>();

    /**
     * Creates new GameController object and initializes its view
     */
    public GameController() {
        this.gameView = new GameView();
    }

    /**
     * Gets game object that is currently handled by this controller
     * @return Game object that is saved in the game property
     * @see Game
     */
    public Game getGame() {
        return this.game;
    }

    /**
     * Sets Game object to be handled by this controller and displayed by its view.
     * It also creates PGNRecorder for the passed game
     * @param game game to save into the game property
     * @see Game
     */
    public void setGame(Game game) {
        this.game = game;
        this.pgn = new PGNRecorder(game, this.gameView.getPGN());
    }

    /**
     * Sets Game object to be handled by this controller and displayed by its view
     * and sets pgn recorder if it already was initialized for the passed game
     * @param game game to save into the game property
     * @param pgn pgn recorder that is initialized for the passed game
     * @see Game
     * @see PGNRecorder
     */
    public void setGame(Game game, PGNRecorder pgn) {
        this.game = game;
        this.pgn = pgn;
    }

    /**
     * Gets view that displays actions of this controller
     * @return GameView object that visualizes the game
     */
    public GameView getGameView() {
        return this.gameView;
    }

    /**
     * Clears all fields and board of the view
     */
    public void resetGamePage() {
        this.gameView.reset();
    }

    /**
     * Resets state of properties and view, then show the view
     */
    public void showGamePage() {
        // if the initial position is not standard, create appropriate pgn tag
        if (this.game.getChessBoard().getCustom()) {
            this.pgn.setFEN();
        }
        this.initView();
        this.start();
        ViewUtil.setRoot(this.gameView.getRoot());
        // create new timers with respect to global settings for each player on their own threads
        timers.add(Color.WHITE.getIndex(), new RoundTimer(Settings.getInstance().getLengthSettings(), this.gameView.getWhiteTimer()));
        timers.add(Color.BLACK.getIndex(), new RoundTimer(Settings.getInstance().getLengthSettings(), this.gameView.getBlackTimer()));
        this.timers.get(Color.WHITE.getIndex()).start();
        this.timers.get(Color.BLACK.getIndex()).start();
        // if game was ended when started, show message and end game
        if (!Result.UNKNOWN.equals(this.game.getResult())) {
            this.endGame(this.game.getResult(), "(from record)");
            return;
        }
        // check if player second to move is not in check - it would be illegal start
        Continue continueGame = this.continueGame(this.game.getPlayerToPlay().getColor());
        if (this.game.checkCheck(this.game.getPlayerToPlay().getColor().getOppositeColor())) {
            this.endGame(Result.UNKNOWN, String.format("%s player cannot start in check",
                    this.game.getPlayerToPlay().getColor().getOppositeColor().getLabel()));
        } else if (continueGame.getContinueGame()) { // else activate color to play
            this.activateColor(this.game.getPlayerToPlay().getColor());
        } else {
            this.endGame(continueGame.getResult(), continueGame.getMessage());
        }
    }

    /**
     * Initalize view according to the game and board models
     */
    private void initView() {
        for (Rank rank : Rank.VALUES) {
            for (File file : File.VALUES) {
                this.gameView.getBoard().putPieceIcon(this.game.getChessBoard().getPiece(file, rank).getIcon(), file,
                        rank);
            }
        }
        if (this.game.getWhitePlayer().getComputer() || this.game.getBlackPlayer().getComputer()) {
            this.gameView.getOfferDraw().setDisable(true);
        }
        this.gameView.setTitle(this.createTitle());
    }

    /**
     * Creates title for the window from event and player names
     * @return String that represents title of the game
     */
    private String createTitle() {
        return String.format("%s: %s vs %s", this.game.getEvent().isEmpty() ? "?" : this.game.getEvent(),
                this.game.getWhitePlayer().getFullName(), this.game.getBlackPlayer().getFullName());
    }

    /**
     * For piece on passed coordinates show target overlay for each its possible move performed as next
     * @param file File position on the board of the displayed piece
     * @param rank Rank position on the board of the displayed piece
     */
    private void setTargets(File file, Rank rank) {
        for (Move move : this.game.getMoves(this.game.getChessBoard().getPiece(file, rank))) {
            this.gameView.getBoard().addTarget(move.getPosition().getFile(), move.getPosition().getRank(),
                    move.getType());
        }
    }

    /**
     * Method that is invoked by the timers when they reach 0. It means lost game
     * for player who has run of time
     */
    public void timeRunOut() {
        this.endGame(Color.WHITE.equals(this.game.getPlayerToPlay().getColor().getOppositeColor()) ? Result.WHITE_WINS
                : Result.BLACK_WINS, "(time for move ran out)");
    }

    /**
     * Decides whether the game should end or not
     * @param color Color for which check possible results
     * @return Continue object that holds result and message
     * @see Color
     * @see Continue
     */
    private Continue continueGame(Color color) {
        // check if the game cannot be win by either side due to insufficient number of pieces
        if (this.game.getInsufficientMaterial()) {
            GameController.LOG.info(String.format("Insufficient material -> draw", color.getLabel()));
            return new Continue(false, Result.DRAW, "(insufficient material)");
        }
        // check if the color is in check
        this.game.setCheck(this.game.checkCheck(color));
        int possibleMovesCount = 0;
        // check possible moves for each piece of the passed color
        for (Rank rank : Rank.VALUES) {
            for (File file : File.VALUES) {
                if (color.equals(this.game.getChessBoard().getPiece(file, rank).getColor())) {
                    possibleMovesCount += this.game.getMoves(this.game.getChessBoard().getPiece(file, rank)).size();
                }
            }
        }
        // if player cannot make any move
        if (possibleMovesCount == 0) {
            // checkmate
            if (this.game.isCheck()) {
                this.game.setCheckmate(true);
                GameController.LOG.info(String.format("Checkmate -> %s player wins",
                        Color.WHITE.equals(color) ? Color.BLACK.getLabel() : Color.WHITE.getLabel()));
                return new Continue(false, Color.WHITE.equals(color) ? Result.BLACK_WINS : Result.WHITE_WINS,
                        "(Checkmate)");
            } else { // stalemate
                GameController.LOG.info("Stalemate -> draw");
                return new Continue(false, Result.DRAW, "(Stalemate)");
            }
        }
        return new Continue(true, Result.UNKNOWN, "");
    }

    /**
     * Sets parameter of the view objects that contain pieces of passed color as clickable
     * @param color Color of the pieces whose squares are activated
     * @see Color
     */
    private void activateColor(Color color) {
        // switch timers
        this.timers.get(color.getOppositeColor().getIndex()).pause();
        this.timers.get(color.getIndex()).proceed();
        // actiavate/deactivate colors
        for (Rank rank : Rank.VALUES) {
            for (File file : File.VALUES) {
                if (color.equals(this.game.getChessBoard().getPiece(file, rank).getColor())) {
                    this.gameView.getBoard().getCell(file, rank).setActive(true);
                } else {
                    this.gameView.getBoard().getCell(file, rank).setActive(false);
                }
            }
        }
        // set status label according to game state
        if (this.game.isCheck()) {
            GameController.LOG.info(String.format("%s in in check", color.getLabel()));
            this.gameView.setStatus(String.format("Check - %s player plays", color.getLabel()));
        } else {
            GameController.LOG.info(String.format("%s pieces were activated", color.getLabel()));
            this.gameView.setStatus(String.format("%s player plays", color.getLabel()));
        }
        // if player to play is computer, get his move and perform it
        if (this.game.getPlayerToPlay().getComputer()) {
            // get move from computer
            CompleteMove computerMove = this.game.getPlayerToPlay().getCompleteMove(this.game);
            Position source = computerMove.getPiece().getPosition();
            Position target = computerMove.getMove().getPosition();
            // simulate events as if computer clicked on the screen
            Event.fireEvent(this.gameView.getBoard().getCell(source.getFile(), source.getRank()).getPane(), Player.click);
            Event.fireEvent(this.gameView.getBoard().getCell(target.getFile(), target.getRank()).getPane(), Player.click);
        }
    }

    /**
     * Deactivate all squares on the board
     */
    private void deactivateAll() {
        for (Rank rank : Rank.VALUES) {
            for (File file : File.VALUES) {
                this.gameView.getBoard().getCell(file, rank).setActive(false);
            }
        }
    }

    /**
     * Check if piece on passed position shoud be promoted and perform promotion if necessary
     * @param file File position of the checked piece
     * @param rank Rank position of the checked piece
     */
    private void testPromotion(File file, Rank rank) {
        Piece testPiece = this.game.getChessBoard().getPiece(file, rank);
        // get position of the square in absolute numbers
        Bounds a = this.gameView.getBoard().getBoard().localToScreen(this.gameView.getBoard().getBoard().getCellBounds(file.getValue() + 1, rank.getValue() + 1));
        double cellSize = this.gameView.getBoard().getCellSize();
        // check promotion
        if (Pawn.class.equals(testPiece.getClass())) {
            if (Color.WHITE.equals(testPiece.getColor()) && Rank.EIGHT.equals(rank)) {
                // get promoted piece
                PromotionController promo = new PromotionController(Color.WHITE, cellSize, a.getMaxX(), a.getMinY());
                promo.getPromotion(file, rank);
            } else if (Color.BLACK.equals(testPiece.getColor()) && Rank.ONE.equals(rank)) {
                // get promoted piece
                PromotionController promo = new PromotionController(Color.BLACK, cellSize, a.getMaxX(), a.getMinY());
                promo.getPromotion(file, rank);
            } else {
                this.finishMove(file, rank, false);
            }
        } else {
            this.finishMove(file, rank, false);
        }
    }

    /**
     * Method that resets and disables everything when game ends
     * @param result Result of the game
     * @param message message to show as status (specification of the result)
     */
     private void endGame(Result result, String message) {
        // end timers
        this.timers.get(Color.WHITE.getIndex()).end();
        this.timers.get(Color.BLACK.getIndex()).end();
        this.gameView.getBoard().clearTargets();
        this.deactivateAll();
        this.game.setResult(result);
        this.gameView.getOfferDraw().setDisable(true);
        this.gameView.getResign().setDisable(true);
        this.gameView.getSave().setDisable(true);
        this.gameView.getAcceptDraw().setDisable(true);
        this.gameView.getDeclineDraw().setDisable(true);
        this.gameView.setStatus(String.format("Game ends: %s %s", result.getResult(), message));
        // save game into pgn file 
        this.pgn.setPath(Chess.finishedPath);
        this.pgn.saveToFile();
    }

    /**
     * Pause game and disable buttons when one player asks for draw
     */
    private void offerDraw() {
        this.timers.get(Color.WHITE.getIndex()).pause();
        this.timers.get(Color.BLACK.getIndex()).pause();
        this.gameView.getBoard().clearTargets();
        this.deactivateAll();
        this.gameView.getResign().setDisable(true);
        this.gameView.getOfferDraw().setDisable(true);
        this.gameView.getSave().setDisable(true);
        this.gameView.getAcceptDraw().setDisable(false);
        this.gameView.getDeclineDraw().setDisable(false);
        if (this.gameView.getBoard().getSelected() != null) {
            this.gameView.getBoard().getSelected().unsetSelectedBackground();
        }
        this.gameView.setStatus(String.format("%s player offered draw", this.game.getPlayerToPlay().getColor().getLabel()));
    }

    /**
     * Resume game when draw offer was declined
     */
    private void declineDraw() {
        this.gameView.getResign().setDisable(false);
        this.gameView.getOfferDraw().setDisable(false);
        this.gameView.getSave().setDisable(false);
        this.gameView.getAcceptDraw().setDisable(true);
        this.gameView.getDeclineDraw().setDisable(true);
        this.activateColor(this.game.getPlayerToPlay().getColor());
        this.gameView.setStatus(String.format("draw was declined - %s player makes move", this.game.getPlayerToPlay().getColor().getLabel()));
    }

    /**
     * End game when draw was accepted
     */
    private void acceptDraw() {
        this.endGame(Result.DRAW, "(draw accepted)");
    }

    /**
     * Replaces piece that should be promoted to selected new piece
     * @param file File position of the promoted piece
     * @param rank Rank position of the promoted piece
     * @param promoteTo Piece that should be placed instead of the promoted Piece
     * @see File
     * @see Rank
     * @see Piece
     */
    public void applyPromotion(File file, Rank rank, Piece promoteTo ) {
        this.game.getChessBoard().putPiece(promoteTo, file, rank);
        this.gameView.getBoard().putPieceIcon(this.game.getChessBoard().getPiece(file, rank).getIcon(), file, rank);
        this.finishMove(file, rank, true);
    }

    /**
     * Perform actions after move - decide to continue or end and save move to pgn recorder
     * @param file File position of the move
     * @param rank Rank position of the move
     * @param promotion whether the move ended in promotion or not
     */
    private void finishMove(File file, Rank rank, boolean promotion) {
        // solve ambiguities for pgn
        Disambiguation disambiguation = this.game.getDisambiguation(file, rank, this.gameView.getBoard().getSelectedPosition());
        // initiate next round
        this.game.nextRound();
        Continue continueGame = this.continueGame(GameController.this.game.getPlayerToPlay().getColor());
        // add move to pgn recorder
        this.pgn.addMove(
            this.game.getChessBoard().getPiece(file, rank),
            this.gameView.getBoard().getSelectedPosition(),
            new Position(file, rank),
            this.gameView.getBoard().getCell(file, rank).getMoveType(),
            disambiguation,
            this.game.getRound() / 2,
            promotion,
            this.game.isCheck(),
            this.game.isCheckmate()
        );
        // end or continue game
        if (continueGame.getContinueGame()) {
            GameController.this.activateColor(GameController.this.game.getPlayerToPlay().getColor());
        } else {
            this.endGame(continueGame.getResult(), continueGame.getMessage());
        }
    }

    /**
     * Set action listeners on user input components
     */
    private void start() {

        /** save game to pgn file when save button is clicked */
        this.gameView.getSave().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                GameController.this.game.setResult(Result.UNKNOWN);
                GameController.this.pgn.setPath(Chess.unfinishedPath);
                GameController.this.pgn.saveToFile();
            }
        });

        /** end game when player clicks resign button */
        this.gameView.getResign().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                GameController.this.endGame(Color.WHITE.equals(GameController.this.game.getPlayerToPlay().getColor()) ? Result.BLACK_WINS : Result.WHITE_WINS, "player resigned");
            }
        });

        /** reset everithing and got to start page if player exits */
        this.gameView.getExit().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                GameController.this.timers.get(Color.WHITE.getIndex()).end();
                GameController.this.timers.get(Color.BLACK.getIndex()).end();
                GameController.this.game = null;
                GameController.this.gameView.reset();
                GameController.this.gameView.getBoard().clearTargets();
                GameController.this.pgn = null;
                Chess.START_PAGE_CONTROLLER.showStartPage();
            }
        });

        /** offer draw when requested */
        this.gameView.getOfferDraw().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                GameController.this.offerDraw();
            }
        });

        /** decline draw after offering */
        this.gameView.getDeclineDraw().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                GameController.this.declineDraw();
            }
        });

        /** accpept draw after offering */
        this.gameView.getAcceptDraw().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                GameController.this.acceptDraw();
            }
        });

        /** for each square define action when is active or targetable */
        for (Rank rank : Rank.VALUES) {
            for (File file : File.VALUES) {
                this.gameView.getBoard().getCell(file, rank).getPane().setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        // if the square is active set targets for its possible moves
                        if (GameController.this.gameView.getBoard().getCell(file, rank).getActive()) {
                            GameController.this.gameView.getBoard().clearTargets();
                            // highlight currently selected square
                            if (GameController.this.gameView.getBoard().getSelected() != null) {
                                GameController.this.gameView.getBoard().getSelected().unsetSelectedBackground();
                            }
                            GameController.this.gameView.getBoard().setSelected(file, rank);
                            GameController.this.gameView.getBoard().getSelected().setSelectedBackground();
                            // set targets for piece on the selected square
                            GameController.this.setTargets(file, rank);
                        }
                        // if is targetable perform move
                        if (GameController.this.gameView.getBoard().getCell(file, rank).getTargetable()) {
                            GameController.this.gameView.getBoard().getSelected().unsetSelectedBackground();
                            // remove piece next to pawn when the move is enpassant
                            if (MoveType.ENPASSANT.equals(GameController.this.gameView.getBoard().getCell(file, rank).getMoveType())) {
                                Rank removeRank = Color.WHITE.equals(GameController.this.game.getChessBoard().getPiece(GameController.this.gameView.getBoard().getSelectedPosition()).getColor()) ? rank.getPrevious() : rank.getNext();
                                GameController.this.game.getChessBoard().removePiece(new Position(file, removeRank));
                                GameController.this.gameView.getBoard().removePiece(file, removeRank);
                            } else if (MoveType.QUEENSIDE_CASTLING.equals(GameController.this.gameView.getBoard().getCell(file, rank).getMoveType())) {
                                // move rook when its queenside castling
                                GameController.this.game.getChessBoard().movePiece(
                                    new Position(File.A, rank),
                                    new Position(file.getNext(), rank),
                                    GameController.this.game.getRound()
                                );
                                GameController.this.gameView.getBoard().movePiece(
                                    GameController.this.gameView.getBoard().getCell(file.getNext(), rank).getIcon(),
                                    new Position(File.A, rank),
                                    GameController.this.gameView.getBoard().getCell(File.A, rank).getIcon(),
                                    new Position(file.getNext(), rank)
                                );
                            } else if (MoveType.KINGSIDE_CASTLING.equals(GameController.this.gameView.getBoard().getCell(file, rank).getMoveType())) {
                                // move rook when its kingside castling
                                GameController.this.game.getChessBoard().movePiece(
                                    new Position(File.H, rank),
                                    new Position(file.getPrevious(), rank),
                                    GameController.this.game.getRound()
                                );
                                GameController.this.gameView.getBoard().movePiece(
                                    GameController.this.gameView.getBoard().getCell(file.getPrevious(), rank).getIcon(),
                                    new Position(File.H, rank),
                                    GameController.this.gameView.getBoard().getCell(File.H, rank).getIcon(),
                                    new Position(file.getPrevious(), rank)
                                );
                            }
                            // move piece on selected square to currently clicked square
                            GameController.this.game.getChessBoard().movePiece(
                                GameController.this.gameView.getBoard().getSelectedPosition(),
                                new Position(file, rank),
                                GameController.this.game.getRound()
                            );
                            GameController.this.gameView.getBoard().clearTargets();
                            GameController.this.gameView.getBoard().movePiece(
                                GameController.this.game.getChessBoard().getPiece(GameController.this.gameView.getBoard().getSelectedPosition()).getIcon(),
                                GameController.this.gameView.getBoard().getSelectedPosition(),
                                GameController.this.game.getChessBoard().getPiece(file, rank).getIcon(),
                                new Position(file, rank)
                            );
                            // test promotion for state after move
                            GameController.this.testPromotion(file, rank);
                        }
                    }
                });
            }
        }
    }
}
