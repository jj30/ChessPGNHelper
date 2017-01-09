package bldg5.jj.pgnhelper;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
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

    public CB(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeViews(context);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ChessBoard);

        try
        {
            String test = typedArray.getString(R.styleable.ChessBoard_pgn);
            setMoveNumber(typedArray.getInteger(R.styleable.ChessBoard_moveNumber, 0));

            String strSampleJSON = Utils.getJSONFromRaw(context, R.raw.sample_game);

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
            typedArray.recycle();
        }
    }

    public void halfMove() {
        try {
            String[][] board = Snapshot.PGN2Board(nMoveNumber, pgnJSON);
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
                ImageView imageView = (ImageView) findViewById(Snapshot.boardRIDs[i][j]);

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

    /*
    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
    }
    @Override
    public void invalidate() {
        super.invalidate();
    }
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }*/
}
