package bldg5.jj.pgnhelper;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import bldg5.jj.pgnhelper.common.Piece;

public class Snapshot {
    public static final String xAxis = "abcdefgh";
    public static final String yAxis = "12345678";
    public static final String nonPawns = "RNBQK";

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

    public static String[][] PGN2Board(JSONObject jsonPGN) throws JSONException {
        String[][] board = InitBoard();

        int nNumberMoves = jsonPGN.length();
        // for (int i = 1; i < nNumberMoves + 1; i++) {
            JSONObject move = (JSONObject) jsonPGN.get(String.valueOf(1));
            String movePGN = move.get("S").toString();
            String white = movePGN.split(" ")[0];
            String black = movePGN.split(" ")[1];


            board = transform("w", white, board);
            board = transform("b", black, board);
        // }

        return board;
    }

    public static String[][] transform(String wb, String move, String[][] currentBoard) {
        String xPawn = intersect(xAxis, move);
        String yPawn = intersect(yAxis, move);
        String xOther = intersect(nonPawns, move);

        int xSource = 0;
        int ySource = 0;
        int xDest = 0;
        int yDest = 0;

        if (xOther.equals("")) {
            xDest = xAxis.indexOf(xPawn);
            yDest = yAxis.indexOf(yPawn);

            // x and y are the destination, but what's the source? find the pawn
            for (ySource = 0; ySource < 8; ySource++) {
                if (currentBoard[ySource][xDest].contains(wb + "p")) {
                    break;
                }
            }

            // so the new board doesn't have a pawn at xDest, ySource
            // but does have a pawn at xDest, yDest
            currentBoard[yDest][xDest] = currentBoard[ySource][xDest];
            currentBoard[ySource][xDest] = "";
        } else {

            try {
                xDest = xAxis.indexOf(intersect(xAxis, move));
                yDest = yAxis.indexOf(intersect(yAxis, move));
                boolean bFound = false;

                // in the array the y is the rows, the x is the columns
                for (ySource = 0; ySource < 8; ySource++) {
                    for (xSource = 0; xSource < 8; xSource++) {
                        String strPiece = currentBoard[ySource][xSource];

                        if (strPiece == null)
                            continue;

                        Piece other = new Piece(xOther, xSource, ySource, xDest, yDest);
                        bFound = strPiece.equals(wb + xOther.toLowerCase()) && other.isLegal();

                        if (bFound)
                            break;
                    }

                    if (bFound)
                        break;
                }

                // so the piece is at xSource, ySource
                // and must move to xDest, yDest
                currentBoard[yDest][xDest] = currentBoard[ySource][xSource];
                currentBoard[ySource][xSource] = "";
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }

        }

        return currentBoard;
    }

    public static String intersect(String one, String two) {
        String strResult = "";
        String[] oneAry = one.split("");
        String[] twoAry = two.split("");

        int i = 1;
        int j = 1;

        // only one character is expected
        for (i = 1; i < oneAry.length; i++) {
            for (j = 1; j < twoAry.length; j++ ) {
                if (oneAry[i].equals(twoAry[j])) {
                    strResult = oneAry[i];
                    break;
                }
            }
        }

        return strResult;
    }
}
