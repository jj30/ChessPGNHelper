package bldg5.jj.pgnhelper;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.Arrays;

import bldg5.jj.pgnhelper.common.OnSwipeTouchListener;
import bldg5.jj.pgnhelper.common.Utils;

public class CB
        extends TableLayout {

    private String pgn;
    private int nMoveNumber;
    private JSONObject pgnJSON;
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

    public CB(Context context) {
        super(context);
        initializeViews(context);
    }

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
        int nPGNMoveNumber = (int) (nMoveNumber + 1) / 2;
        String strReturn = "";
        String strWhite = "";
        String strBlack = "";

        JSONObject move = null;
        try {
            move = (JSONObject) pgnJSON.get(String.valueOf(nPGNMoveNumber));
            String movePGN = move.get("S").toString();

            strWhite = movePGN.toString().split(" ")[1];
            strBlack = movePGN.toString().split(" ")[0];

            if (nMoveNumber % 2 == 0)
                strReturn = strWhite;
            else
                strReturn = strBlack;

            // if there are comments, even one, it's in an array, ie ["just this one comment"]
            if (move.has("C")) {
                String moveComment = move.getJSONArray("C").join("\n");
                strReturn += "\n" + moveComment;
            }
        } catch (JSONException | ArrayIndexOutOfBoundsException e) {
            strReturn = strWhite;
            e.printStackTrace();
            Log.i("PGNHelper", "This game ends on a white move, or user is back to 0th move (the beginning of the game).");
        }

        return strReturn;
    }

    public CB(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeViews(context);

        try
        {
            String strJSON = Utils.getJSONFromRaw(context, R.raw.sample_game);
            JSONObject json = new JSONObject(strJSON);

            pgnJSON = (JSONObject) json.get("PGN");

            if (json.has("black"))
                black = json.getString("black");
            if (json.has("player"))
                white = json.getString("player");
            if (json.has("date"))
                date = json.getString("date");
            if (json.has("result"))
                result = json.getString("result");
            if (json.has("event"))
                event = json.getString("event");
            if (json.has("ECO"))
                eco = json.getString("ECO");
            if (json.has("round"))
                round = json.getString("round");

            // store the number of moves in this game
            numMovesInGame = pgnJSON.length();

            String[][] board = Snapshot.PGN2Board(nMoveNumber, pgnJSON);
            Drawboard(board);
        }

        // the recycle() will be executed obligatorily
        catch (IOException | JSONException e) {
            e.printStackTrace();
        } finally {
            // for reuse
            // typedArray.recycle();
        }
    }

    public String getInfo() {
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
        currentBoard = Snapshot.initBoard();
        Drawboard(currentBoard);
    }

    public void toTheEnd() {
        try {
            currentBoard = Snapshot.toTheEnd(pgnJSON);
            Drawboard(currentBoard);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void toMoveNumber(int n) {
        try {
            currentBoard = Snapshot.PGN2Board(n, pgnJSON);
            Drawboard(currentBoard);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void switchSides() {
        bIsFlipped = !bIsFlipped;
        Drawboard(this.currentBoard);
    }

    public void halfMove() {
        try {
            // String[][] board = Snapshot.PGN2Board(nMoveNumber, pgnJSON);

            String[][] board = currentBoard;
            board = Snapshot.oneMove(nMoveNumber, pgnJSON, currentBoard);

            currentBoard = board;
            Drawboard(board);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void halfMoveBackwards() {
        try {
            String[][] board = Snapshot.PGN2Board(nMoveNumber, pgnJSON);
            currentBoard = board;
            Drawboard(board);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void Drawboard(String[][] thisBoard) {
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
    }

    private void initializeViews(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.board, this);
    }
}
