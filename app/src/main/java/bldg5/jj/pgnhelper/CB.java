package bldg5.jj.pgnhelper;

import android.content.Context;
import android.content.res.TypedArray;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TableLayout;

import java.util.HashMap;
import java.util.Map;


public class CB
        extends TableLayout {

    private String pgn;
    private boolean color;

    public CB(Context context) {
        super(context);
        initializeViews(context);
    }

    public CB(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeViews(context);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ChessBoard);

        try
        {
            int attr = typedArray.getIndex(0);
            pgn = typedArray.getString(attr);

            String[][] board = Snapshot.InitBoard();

            for (int i=0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    String strPiece = board[i][j];
                    strPiece = (strPiece == null) ? "": strPiece;

                    if (strPiece != ""){
                        ImageView imageView = (ImageView) findViewById(Snapshot.boardRIDs[i][j]);
                        imageView.setImageResource(Snapshot.mapStringsToResources.get(board[i][j]));
                    }
                }
            }
        }

        // the recycle() will be executed obligatorily
        finally {
            // for reuse
            typedArray.recycle();
        }
    }

    private void initializeViews(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.board, this);
    }
}
