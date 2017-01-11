package bldg5.jj.pgnhelper;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TableLayout;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;

import bldg5.jj.pgnhelper.common.Utils;

public class CB
        extends TableLayout {

    private String pgn;
    private int nMoveNumber;
    private JSONObject pgnJSON;
    private String[][] currentBoard = Snapshot.initBoard();
    private boolean bIsFlipped = false;

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
        } catch (JSONException | ArrayIndexOutOfBoundsException e) {
            strReturn = strWhite;
            e.printStackTrace();
            Log.i("PGNHelper", "Game ends on white move.");
        }

        return strReturn;
    }



    /*public String[][] getCurrentBoard() {
        return this.currentBoard;
    }

    public void setCurrentBoard(String[][] board) {
        this.currentBoard = board;
    }*/

    public CB(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeViews(context);

        // TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ChessBoard);

        try
        {
            /* String test = typedArray.getString(R.styleable.ChessBoard_pgn);
            setMoveNumber(typedArray.getInteger(R.styleable.ChessBoard_moveNumber, 0));*/

            String strSampleJSON = Utils.getJSONFromRaw(context, R.raw.sample_game3);

            JSONObject json = new JSONObject(strSampleJSON);
            JSONObject jsonPGNS =  (JSONObject) json.get("PGN");
            pgnJSON = (JSONObject) jsonPGNS.get("M");

            // String[][] board = Snapshot.InitBoard();
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

    public void initBoard() {
        Drawboard(Snapshot.initBoard());
    }

    public void toTheEnd() {
        try {
            Drawboard(Snapshot.toTheEnd(pgnJSON));
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
