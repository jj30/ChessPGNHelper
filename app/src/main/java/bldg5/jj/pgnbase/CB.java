package bldg5.jj.pgnbase;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TableLayout;

import org.json.JSONException;

import java.util.regex.Pattern;

import bldg5.jj.pgnbase.common.Error;
import bldg5.jj.pgnbase.common.Game;

public class CB
        extends TableLayout {

    private String pgn;
    private int nMoveNumber;
    private String pgns;

    // private JSONObject pgnJSON;
    private String[] aryPGNs;
    private String[][] currentBoard = Snapshot.initBoard();
    private boolean bIsFlipped = false;
    private int numMovesInGame;
    private String black = "";
    private String white = "";
    private String date = "";
    private String eco = "";
    private String event = "";
    private String result = "";
    private String round = "";
    private Game game;
    private static final String tag = "CB.java";

    public Integer getMoveNumber() {
        return this.nMoveNumber;
    }

    public void setMoveNumber(int n) {
        nMoveNumber = n;
    }

    public Integer getNumMoves() {
        return this.numMovesInGame;
    }

    public String getMove() {
        String strReturn = "";
        // nMoveNumber is the UI move number, ie, 10 corresponds to 5 in the PGNs
        int nPGNMoveNumber = (int) (nMoveNumber + 1) / 2;

        try {
            String strWhite = "";
            String strBlack = "";

            String movePGN = aryPGNs[nPGNMoveNumber].trim();
            String movePGNMinusComments = splitCommentsMove(movePGN)[0];

            // \\s+ splits any number of whitespace, two spaces or one
            strWhite = movePGNMinusComments.toString().split("\\s+")[0];
            strBlack = movePGNMinusComments.toString().split("\\s+")[1];

            if (nMoveNumber % 2 == 1)
                strReturn = String.valueOf(nPGNMoveNumber) + ". " + strWhite;
            else
                strReturn = String.valueOf(nPGNMoveNumber) + ". ..." +  strBlack;

            // if there are comments, even one, it's in an array, ie ["just this one comment"]
            if (movePGN.contains("{") && movePGN.contains("}")) {
                String moveComment = splitCommentsMove(movePGN)[1];
                strReturn += "\n" + moveComment;
            }

        } catch(IndexOutOfBoundsException ex) {
            strReturn = (nPGNMoveNumber == 0) ? getInfo() : "End of game.";
        }

        return strReturn;
    }

    private String[] splitCommentsMove(String strIn) {
        String moveWOComments = "";
        String allComments = "";
        String str_regex = "[{}]";
        String[] ary = strIn.split(str_regex);

        for (String elem : ary){
            allComments += (elem.contains("[") && elem.contains("]")) ? elem: "";
            moveWOComments += (elem.contains("[") && elem.contains("]")) ? "": elem;
        }

        return new String[] { moveWOComments, allComments };
    }

    public CB(Context context, Game game) {
        super(context);
        initializeViews(context);

        setGame(game);
    }

    public CB(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeViews(context);

        try{
            String[][] board = Snapshot.PGN2Board(nMoveNumber, aryPGNs);
            Drawboard(board);
        } catch(Exception ex) {
            Error.sendError(ex.getMessage());
        }
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game thisGame) {
        try {
            game = thisGame;
            pgns = thisGame.getPgn();
            black = thisGame.getBlack();
            white = thisGame.getWhite();
            date = thisGame.getDate();
            result = thisGame.getResult();
            event = thisGame.getEvent();
            eco = thisGame.getEco();
            round = thisGame.getRound();

            String str_regex = "\\d+\\.";
            Pattern regex = Pattern.compile(str_regex, Pattern.DOTALL);

            /* Matcher regexMatcher = regex.matcher(pgns);
            int i = 0;
            while (regexMatcher.find()) {
                String move = regexMatcher.group(0);
                aryPgns.add(i, move);
                i++;
            }
            int numMoves = aryPgns.size();*/

            aryPGNs = pgns.split(regex.toString());

            // store the number of moves in this game
            numMovesInGame = aryPGNs.length - 1;
        } catch(Exception ex) {
            Error.sendError(ex.getMessage());
        }
    }

    public String getInfo() {
        // to prevent null ref exceptions:
        date = date == null ? "": date;
        event = event == null ? "": event;
        eco = eco == null ? "": eco;
        result = result == null ? "": result;
        white = white == null ? "": white;
        black = black == null ? "": black;

        String strReturn = "";
        date = date.trim() == "" ? "": String.format(" on %s", date);
        event = event.trim() == "" ? "": String.format("at %s", event);
        eco = eco.trim() == "" ? "": String.format("ECO %s.", eco);

        date += event != "" ? " ": "";
        event += eco != "" ? ". ": ".";
        String strWhenWhereECO = date + event + eco;

        try {
            String whiteResult = result.split("-")[0];
            String blackResult = result.split("-")[1];
            strReturn = String.format("%s (white, %s) vs %s (black, %s)%s", white, whiteResult, black, blackResult, strWhenWhereECO);
        } catch (IndexOutOfBoundsException ex) {
            strReturn = String.format("%s (white) vs %s (black)%s", white, black, strWhenWhereECO);
        }

        return strReturn.trim();
    }

    public void initBoard() {
        try {
            currentBoard = Snapshot.initBoard();
            Drawboard(currentBoard);
        } catch(Exception ex) {
            Error.sendError(ex.getMessage());
        }
    }

    public void toTheEnd() {
        try {
            currentBoard = Snapshot.toTheEnd(aryPGNs);
            Drawboard(currentBoard);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void toMoveNumber(int n) {
        try {
            currentBoard = Snapshot.PGN2Board(n, aryPGNs);
            Drawboard(currentBoard);
        } catch(Exception ex) {
            Error.sendError(ex.getMessage());
        }
    }

    public void switchSides() {
        try {
            bIsFlipped = !bIsFlipped;
            Drawboard(this.currentBoard);
        } catch(Exception ex) {
            Error.sendError(ex.getMessage());
        }
    }

    public void halfMove() {

        try {
            String[][] board = currentBoard;
            board = Snapshot.oneMove(nMoveNumber, aryPGNs, currentBoard);

            currentBoard = board;
            Drawboard(board);
        } catch(Exception ex) {
            Error.sendError(ex.getMessage());
            // Log.e(tag, ex.getMessage());
        }
    }

    public void halfMoveBackwards() {
        try {
            String[][] board = Snapshot.PGN2Board(nMoveNumber, aryPGNs);
            currentBoard = board;
            Drawboard(board);
        } catch(Exception ex) {
            Error.sendError(ex.getMessage());
        }
    }

    private void Drawboard(String[][] thisBoard) {
        try {
            for (int i=0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    String strPiece = thisBoard[i][j];
                    strPiece = (strPiece == null) ? "": strPiece;

                    // if the view is flipped, flip it
                    int newY = bIsFlipped ? 7 - i : i;
                    ImageView imageView = (ImageView) findViewById(Snapshot.boardRIDs[newY][j]);

                    if (!strPiece.equals("")){
                        imageView.setImageResource(Snapshot.mapStringsToResources.get(thisBoard[i][j]));
                    } else {
                        imageView.setImageDrawable(null);
                    }
                }
            }
        } catch (Exception ex) {
            Error.sendError(ex.getMessage());
            // Log.e(tag, ex.getMessage());
        }
    }

    private void initializeViews(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.board, this);
    }
}
