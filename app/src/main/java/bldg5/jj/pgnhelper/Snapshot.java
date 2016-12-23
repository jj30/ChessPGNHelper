package bldg5.jj.pgnhelper;


import java.util.HashMap;

public class Snapshot {
    // board resource ID's. This is just the board wo the pieces
    public static final int[][] boardRIDs = {
            { R.id.row0col0, R.id.row0col1, R.id.row0col2, R.id.row0col3, R.id.row0col4, R.id.row0col5, R.id.row0col6, R.id.row0col7 },
            { R.id.row1col0, R.id.row1col1, R.id.row1col2, R.id.row1col3, R.id.row1col4, R.id.row1col5, R.id.row1col6, R.id.row1col7 },
            { R.id.row2col0, R.id.row2col1, R.id.row2col2, R.id.row2col3, R.id.row2col4, R.id.row2col5, R.id.row2col6, R.id.row2col7 },
            { R.id.row3col0, R.id.row3col1, R.id.row3col2, R.id.row3col3, R.id.row3col4, R.id.row3col5, R.id.row3col6, R.id.row3col7 },
            { R.id.row4col0, R.id.row4col1, R.id.row4col2, R.id.row4col3, R.id.row4col4, R.id.row4col5, R.id.row4col6, R.id.row4col7 },
            { R.id.row5col0, R.id.row5col1, R.id.row5col2, R.id.row5col3, R.id.row5col4, R.id.row5col5, R.id.row5col6, R.id.row5col7 },
            { R.id.row6col0, R.id.row6col1, R.id.row6col2, R.id.row6col3, R.id.row6col4, R.id.row6col5, R.id.row6col6, R.id.row6col7 },
            { R.id.row7col0, R.id.row7col1, R.id.row7col2, R.id.row7col3, R.id.row7col4, R.id.row7col5, R.id.row7col6, R.id.row7col7 }
    };

    public static final HashMap<String, Integer> mapStringsToResources;

    // static initializer for HashMap
    static {
        mapStringsToResources = new HashMap<String, Integer>();
        mapStringsToResources.put("wr", R.drawable.wr);
        mapStringsToResources.put("wn", R.drawable.wn);
        mapStringsToResources.put("wb", R.drawable.wb);
        mapStringsToResources.put("wk", R.drawable.wk);
        mapStringsToResources.put("wq", R.drawable.wq);
        mapStringsToResources.put("wp", R.drawable.wp);

        // black pieces
        mapStringsToResources.put("br", R.drawable.br);
        mapStringsToResources.put("bn", R.drawable.bn);
        mapStringsToResources.put("bb", R.drawable.bb);
        mapStringsToResources.put("bk", R.drawable.bk);
        mapStringsToResources.put("bq", R.drawable.bq);
        mapStringsToResources.put("bp", R.drawable.bp);
    }

    public static String[][] InitBoard() {
        String[][] board = new String[8][8];

        // pieces at the start of the game
        board[0] = new String[] { "wr", "wn", "wb", "wq", "wk", "wb", "wn", "wr" };
        board[1] = new String[] { "wp", "wp", "wp", "wp", "wp", "wp", "wp", "wp" };
        board[6] = new String[] { "bp", "bp", "bp", "bp", "bp", "bp", "bp", "bp" };
        board[7] = new String[] { "br", "bn", "bb", "bq", "bk", "bb", "bn", "br" };

        return board;
    }
}
