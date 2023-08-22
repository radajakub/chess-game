package pjv.sp.chess.model.pgn;

import pjv.sp.chess.model.Game;
import pjv.sp.chess.model.Result;
import pjv.sp.chess.model.board.Board;
import pjv.sp.chess.model.board.MoveControl;
import pjv.sp.chess.model.board.Rank;
import pjv.sp.chess.model.pieces.Bishop;
import pjv.sp.chess.model.pieces.Color;
import pjv.sp.chess.model.pieces.Empty;
import pjv.sp.chess.model.pieces.King;
import pjv.sp.chess.model.pieces.Knight;
import pjv.sp.chess.model.pieces.Move;
import pjv.sp.chess.model.pieces.Pawn;
import pjv.sp.chess.model.pieces.Piece;
import pjv.sp.chess.model.pieces.Position;
import pjv.sp.chess.model.pieces.Queen;
import pjv.sp.chess.model.pieces.Rook;
import pjv.sp.chess.model.player.ComputerPlayer;
import pjv.sp.chess.model.player.HumanPlayer;
import pjv.sp.chess.model.player.Player;
import pjv.sp.chess.model.board.File;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * PGNLoader is very similar to PGNBrowser, but loader does not show moves on view
 * and creates game from last position.
 * @author Jakub Rada
 * @version 1.0
 */
public class PGNLoader {

    private static final Logger LOG = Logger.getLogger(PGNLoader.class.getName());

    /**
     * Board where the changes are applied
     */
    private Board board;

    /**
     * Move control that seeks moves when ambiguity occurrs
     */
    private MoveControl moveControl;

    /**
     * File from which are loaded data
     */
    private final java.io.File file;

    /**
     * if start was custom
     */
    private boolean custom;

    /**
     * when custom is true, there is stored layout in FEN format
     */
    private String fen;

    /**
     * Color of the current move's player
     */
    private Color currentColor;

    /**
     * List of all moves
     */
    private List<String> moves = new ArrayList<>();

    /**
     * Number of the move
     */
    private int currentMove;

    /**
     * name of the event where game was played
     */
    private String event;

    /**
     * site of the game
     */
    private String site;

    /**
     * date of the game
     */
    private String date;

    /**
     * name of the white player
     */
    private String white;

    /**
     * name of the black player
     */
    private String black;

    /**
     * Result of the game
     */
    private String result;

    /**
     * Array that holds movements of rooks and kings
     */
    private boolean[][] moved = new boolean[3][2];

    /**
     * Creates new PGNLoader object
     * @param file file from data are loaded
     */
    public PGNLoader(java.io.File file) {
        this.file = file;
        this.board = new Board(true);
        this.moveControl = new MoveControl(this.board);
        this.custom = false;
        this.fen = null;
        this.processFile();
        this.currentMove = 0;
        for (int i = 0; i < 3; i++) {
            for (int n = 0; n < 2; n++) {
                this.moved[i][n] = false;
            }
        }
        while (this.hasNextMove()) {
            this.nextMove();
        }
    }

    /**
     * Gets array in format
     * [whiteRookA][whiteRookH]
     * [blackRookA][blackRookH]
     * [whiteKing][blackKing]
     * @return Arra that holds information about kings and 
     */
    public boolean[][] getMovedForCastling() {
        return this.moved;
    }

    /**
     * Gets result of the game
     * @return Result object
     */
    public Result getResult() {
        for (Result res : Result.values()) {
            if (this.result.equals(res.getResult())) {
                return res;
            }
        }
        return null;
    }

    /**
     * Gets if this game started with custom layout
     * @return custom or standard start
     */
    public boolean getCustom() {
        return this.custom;
    }

    /**
     * Gets color of player that plays next
     * @return Color of the next player
     */
    public Color getColorToPlay() {
        return this.currentColor;
    }

    /**
     * Get date of the game from pgn
     * @return String that contains date
     */
    public String getDate() {
        return this.date;
    }

    /**
     * Gets all moves from pgn
     * @return List of Strings (moves in pgn notation)
     */
    public List<String> getMoves() {
        return this.moves;
    }

    /**
     * Creates new Game with parameters from tags
     * @return new Game object
     */
    public Game createGame() {
        Game game = new Game(
            this.event,
            this.site,
            this.createPlayer(Color.WHITE),
            this.createPlayer(Color.BLACK),
            this.board
        );
        return game;
    }

    /**
     * Creates new player for game
     * @param color Color of the player
     * @return new Player with parameters from tags
     */
    private Player createPlayer(Color color) {
        Player player;
        String name = Color.WHITE.equals(color) ? this.white : this.black;
        String[] parts = name.split(", ");
        if (parts.length == 2) {
            if ("Computer".equals(parts[1])) {
                player = new ComputerPlayer(parts[1], parts[0], color);
            } else {
                player = new HumanPlayer(parts[1], parts[0], color);
            }
        } else {
            player = new HumanPlayer("", name, color);
        }
        return player;
    }

    /**
     * Decode and apply next move to model
     */
    private void nextMove() {
        int index = 0;
        if (PGNTag.isKingsideCastling(this.moves.get(this.currentMove)) || PGNTag.isQueensideCastling(this.moves.get(this.currentMove))) {
            Rank rank = Color.WHITE.equals(this.currentColor) ? Rank.ONE : Rank.EIGHT;
            Position king = new Position(File.E, rank);;
            Position rook;
            Position newKing;
            Position newRook;
            if (PGNTag.isKingsideCastling(this.moves.get(this.currentMove))) {
                rook = new Position(File.H, rank);
                newKing = new Position(File.G, rank);
                newRook = new Position(File.F, rank);
            } else {
                rook = new Position(File.A, rank);
                newKing = new Position(File.C, rank);
                newRook = new Position(File.D, rank);
            }
            this.board.movePiece(king, newKing, this.currentMove + 1);
            this.board.movePiece(rook, newRook, this.currentMove + 1);
        } else {
            char[] move = this.moves.get(this.currentMove).toCharArray();
            int size = move.length;
            Piece piece;
            if (Character.isLowerCase(move[index])) {
                piece = new Pawn(this.currentColor, new Position(File.A, Rank.ONE));
            } else {
                piece = PGNTag.decodePiece(Color.BLACK.equals(this.currentColor) ? Character.toLowerCase(move[index]) : Character.toUpperCase(move[index]));
                index++;
            }
            File srcFile = null;
            if (index < size) {
                srcFile = File.getFileOfLabel(Character.toString(move[index++]));
                if (srcFile == null) {
                    index--;
                }
            }
            Rank srcRank = null;
            if (index < size) {
                srcRank = Rank.getRankOfLabel(Character.toString(move[index++]));
                if (srcRank == null) {
                    index--;
                }
            }
            if (index < size && move[index] == 'x') {
                index++;
            }
            File tgtFile = null;
            if (index < size) {
                tgtFile = File.getFileOfLabel(Character.toString(move[index++]));
                if (tgtFile == null) {
                    index--;
                }
            }
            Rank tgtRank = null;
            if (index < size) {
                tgtRank = Rank.getRankOfLabel(Character.toString(move[index++]));
                if (tgtRank == null) {
                    index--;
                }
            }
            boolean promotion = false;
            char promoted = '\0';
            if (index < size && move[index] == '=') {
                promotion = true;
                index++;
                promoted = move[index++];
            }
            Position target;
            if (tgtFile == null) {
                target = new Position(srcFile, srcRank);
            } else {
                target = new Position(tgtFile, tgtRank);
            }
            List<Piece> selectedType = this.board.getPiecesOfType(piece, this.board.getPieces(this.currentColor));
            List<Piece> possibleMoved = new ArrayList<>();
            for (Piece possible : selectedType) {
                for (Move realMove : this.moveControl.getFilteredMoves(possible, this.currentMove + 1)) {
                    if (this.moveControl.areEqualPositions(realMove.getPosition(), target)) {
                        possibleMoved.add(possible);
                        break;
                    }
                }
            }
            Position source = new Position(File.A, Rank.ONE);
            if (tgtFile == null && tgtRank == null) {
                source = possibleMoved.get(0).getPosition();
            } else {
                if (srcFile == null && srcRank != null) {
                    for (Piece possible : possibleMoved) {
                        if (possible.getPosition().getRank().equals(srcRank)) {
                            source = possible.getPosition();
                            break;
                        }
                    }
                } else if (srcRank == null && srcFile != null) {
                    for (Piece possible : possibleMoved) {
                        if (possible.getPosition().getFile().equals(srcFile)) {
                            source = possible.getPosition();
                            break;
                        }
                    }
                } else {
                    source = possibleMoved.get(0).getPosition();
                }
            }
            if (Rook.class.equals(piece.getClass())) {
                if (File.A.equals(source.getFile())) {
                    this.moved[piece.getColor().getIndex()][0] = true;
                } else if (File.H.equals(source.getFile())) {
                    this.moved[piece.getColor().getIndex()][1] = true;
                }
            } else if (King.class.equals(piece.getClass())) {
                this.moved[2][piece.getColor().getIndex()] = true;
            }
            if (
                Pawn.class.equals(piece.getClass()) &&
                !source.getFile().equals(target.getFile()) &&
                Empty.class.equals(this.board.getPiece(target).getClass())
            ) {
                Position enPassant = new Position(target.getFile(), Color.WHITE.equals(piece.getColor()) ? target.getRank().getPrevious() : target.getRank().getNext());
                this.board.putPiece(new Empty(enPassant), enPassant.getFile(), enPassant.getRank());
            }
            this.board.movePiece(new Position(source.getFile(), source.getRank()), new Position(target.getFile(), target.getRank()), this.currentMove + 1);
            if (promotion) {
                Piece promoPiece = this.decodePiece(Color.BLACK.equals(this.currentColor) ? Character.toLowerCase(promoted) : Character.toUpperCase(promoted), target.getFile(), target.getRank());
                this.board.putPiece(promoPiece, target.getFile(), target.getRank());
            }
        }
        this.currentMove++;
        this.currentColor = this.currentColor.getOppositeColor();
    }

    /**
     * If there is any move left
     * @return true, if another move can be show, false, if not
     */
    private boolean hasNextMove() {
        return this.currentMove < this.moves.size() - 1;
    }

    /**
     * Decodes tag and sets it to proper field
     * @param line
     * @throws IllegalArgumentException
     */
    private void decodeTag(String line) throws IllegalArgumentException {
        String[] parts = PGNTag.splitTag(line);
        parts[1] = PGNTag.stripValue(parts[1]);
        if (PGNTagLabels.EVENT.getLabel().equals(parts[0])) {
            this.event = parts[1];
        } else if (PGNTagLabels.SITE.getLabel().equals(parts[0])) {
            this.site = parts[1];
        } else if (PGNTagLabels.DATE.getLabel().equals(parts[0])) {
            this.date = parts[1];
        } else if (PGNTagLabels.WHITE.getLabel().equals(parts[0])) {
            this.white = parts[1];
        } else if (PGNTagLabels.BLACK.getLabel().equals(parts[0])) {
            this.black = parts[1];
        } else if (PGNTagLabels.RESULT.getLabel().equals(parts[0])) {
            this.result = parts[1];
        } else if (PGNTagLabels.SETUP.getLabel().equals(parts[0])) {
            this.custom = "1".equals(parts[1]);
        } else if (PGNTagLabels.FEN.getLabel().equals(parts[0]) && this.custom) {
            this.fen = parts[1];
        }
    }

    /**
     * Decodes piece from SAN notation
     * @param c char that represents the piece
     * @param file File position of the new Piece
     * @param rank Rank position of the new Piece
     * @return new Piece that was decoded
     */
    private Piece decodePiece(char c, File file, Rank rank) {
        Piece piece;
        switch (c) {
            case 'r':
                piece = new Rook(Color.BLACK, new Position(file, rank));
                break;
            case 'R':
                piece = new Rook(Color.WHITE, new Position(file, rank));
                break;
            case 'n':
                piece = new Knight(Color.BLACK, new Position(file, rank));
                break;
            case 'N':
                piece = new Knight(Color.WHITE, new Position(file, rank));
                break;
            case 'b':
                piece = new Bishop(Color.BLACK, new Position(file, rank));
                break;
            case 'B':
                piece = new Bishop(Color.WHITE, new Position(file, rank));
                break;
            case 'q':
                piece = new Queen(Color.BLACK, new Position(file, rank));
                break;
            case 'Q':
                piece = new Queen(Color.WHITE, new Position(file, rank));
                break;
            case 'k':
                piece = new King(Color.BLACK, new Position(file, rank));
                break;
            case 'K':
                piece = new King(Color.WHITE, new Position(file, rank));
                break;
            case 'p':
                piece = new Pawn(Color.BLACK, new Position(file, rank));
                break;
            case 'P':
                piece = new Pawn(Color.WHITE, new Position(file, rank));
                break;
            default:
                piece = new Empty(new Position(file, rank));
        }
        return piece;
    }

    /**
     * Creats board from fen notation
     * @param fen String in fen notation
     */
    private void fillBoard(String fen) {
        File file = File.A;
        Rank rank = Rank.EIGHT;
        for (char c : fen.toCharArray()) {
            if (c == '/') {
                file = File.A;
                rank = rank.getPrevious();
                continue;
            }
            if (c >= '1' && c <= '8') {
                for (int i = 0; i < c - '0'; i++) {
                    this.board.putPieceInitial(new Empty(new Position(file, rank)));
                    file = file.getNext();
                }
                continue;
            }
            this.board.putPieceInitial(this.decodePiece(c, file, rank));
            file = file.getNext();
        }
    }

    /**
     * Parse and process file
     */
    private void processFile() {
        try (BufferedReader r = new BufferedReader(new InputStreamReader(new FileInputStream(this.file), "utf-8"))) {
            String line;
            while ((line = r.readLine()) != null && !line.equals("")) {
                this.decodeTag(line);
            }
            if (this.custom && this.fen != null) {
                String[] temp = this.fen.split(" ");
                this.fillBoard(temp[0]);
                this.currentColor = "b".equals(temp[1]) ? Color.BLACK : Color.WHITE;
            } else if (!this.custom) {
                this.fillBoard("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR");
                this.currentColor = Color.WHITE;
            } else {
                throw new IllegalArgumentException("No FEN present");
            }
            while ((line = r.readLine()) != null) {
                String[] parts = line.split(" ");
                for (String part : parts) {
                    if (";".equals(part)) {
                        break;
                    }
                    if (part.matches("[01]-[01]") || part.matches("<1/2-1/2>")) {
                        break;
                    }
                    if (!part.matches("[0-9]+[.]+")) {
                        this.moves.add(part);
                    }
                    
                }
            }
        } catch (IllegalArgumentException e) {
            PGNLoader.LOG.severe(String.format("Error while parsing file: %s", e.getMessage()));
        } catch (UnsupportedEncodingException e) {
            PGNLoader.LOG.severe("File with wrong encoding");
        } catch (FileNotFoundException e) {
            PGNLoader.LOG.severe("File not found");
        } catch (IOException e) {
            PGNLoader.LOG.severe("File could not be closed");
        }
    }

}