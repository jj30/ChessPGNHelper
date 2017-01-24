package bldg5.jj.pgnbase;

import android.util.Log;
import org.json.JSONException;
import java.util.HashMap;
import bldg5.jj.pgnbase.common.Piece;

public class Snapshot {
    public static final String xAxis = "abcdefgh";
    public static final String yAxis = "12345678";
    public static final String nonPawns = "RNBQK";
    private static final String tag = "Snapshot.java";

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

    public static String[][] initBoard() {
        String[][] board = new String[8][8];

        // pieces at the start of the game
        board[0] = new String[] { "wr", "wn", "wb", "wq", "wk", "wb", "wn", "wr" };
        board[1] = new String[] { "wp", "wp", "wp", "wp", "wp", "wp", "wp", "wp" };
        board[2] = new String[] { "", "", "", "", "", "", "", "" };
        board[3] = new String[] { "", "", "", "", "", "", "", "" };
        board[4] = new String[] { "", "", "", "", "", "", "", "" };
        board[5] = new String[] { "", "", "", "", "", "", "", "" };
        board[6] = new String[] { "bp", "bp", "bp", "bp", "bp", "bp", "bp", "bp" };
        board[7] = new String[] { "br", "bn", "bb", "bq", "bk", "bb", "bn", "br" };

        return board;
    }

    public static String[][] toTheEnd(String[] aryPGNs) throws JSONException {
        int nLength = (aryPGNs.length - 1) * 2;
        // the UI moves are half-moves
        return PGN2Board(nLength, aryPGNs);
    }

    public static String[][] PGN2Board(int toMoveNumber, String[] aryPgns) {
        String[][] board = initBoard();
        int nLoop = (int) (toMoveNumber + 1) / 2;

        for (int i = 1; i <= nLoop; i++) {
            String movePGN = aryPgns[i].trim();
            String white = movePGN.split(" ")[0];
            String black = "";

            try {
                black = movePGN.split(" ")[1];
            }
            catch(ArrayIndexOutOfBoundsException ex) {
                Log.i(tag, "Game ends on white move.");
            } finally {
                board = transform("w", white, board);

                if (i < nLoop)
                    board = transform("b", black, board);
                else
                    // i == nLoop, last iteration
                    if (toMoveNumber % 2 == 0)
                        board = transform("b", black, board);
            }
        }

        return board;
    }

    public static String[][] oneMove(int toUIMoveNumber, String[] aryPGNs, String[][] board) {

        try {
            int nPGNMoveNumber = (int) (toUIMoveNumber + 1) / 2;
            String movePGN = aryPGNs[nPGNMoveNumber].trim();
            String white = movePGN.split(" ")[0];
            String black = "";

            try {
                black = movePGN.split(" ")[1];
            } catch (ArrayIndexOutOfBoundsException ex) {
                Log.i(tag, "Game ends on white move.");
            } finally {
                // which half of the PGN move is this? the 1st or the 2nd half?
                if (toUIMoveNumber % 2 == 1)
                    board = transform("w", white, board);
                else
                    board = transform("b", black, board);
            }
        } catch (ArrayIndexOutOfBoundsException ex) {
            // passed the last move in the game.
            Log.e(tag, ex.getMessage());
        }

        return board;
    }

    public static String[][] transform(String wb, String move, String[][] currentBoard) {
        if (move.equals(""))
            return currentBoard;

        boolean bCapture = move.contains("x");
        String destOnly  = bCapture ? move.split("x")[1] : move;

        String xDestination = intersect(xAxis, destOnly);
        String yDestination = intersect(yAxis, destOnly);
        String xOther = intersect(nonPawns, move);

        // some moves use this to disambiguate, eg, two horses can move to the same square.
        String hFileRank = move.replace(xOther, "")
                .replace(xDestination + yDestination, "")
                .replace("x", "")
                .replace("+", "");

        int xDest = 0;
        int yDest = 0;

        if (xOther.equals("")) {
            // could be castling
            if (move.equals("O-O") || move.equals("O-O-O")) {
                // no code to test the legality of the move yet
                int nYaxis = wb.equals("w") ? 1: 8;

                if (move.equals("O-O")) {
                    // king to g1 or g8
                    // rook to f1 or f8
                    currentBoard[nYaxis - 1][6] = currentBoard[nYaxis - 1][4];
                    currentBoard[nYaxis - 1][4] = "";

                    currentBoard[nYaxis - 1][5] = currentBoard[nYaxis - 1][7];
                    currentBoard[nYaxis - 1][7] = "";
                } else {
                    // king to c1 or c8
                    // rook to d1 or d8
                    currentBoard[nYaxis - 1][2] = currentBoard[nYaxis - 1][4];
                    currentBoard[nYaxis - 1][4] = "";

                    currentBoard[nYaxis - 1][3] = currentBoard[nYaxis - 1][0];
                    currentBoard[nYaxis - 1][0] = "";
                }
            } else {
                xDest = xAxis.indexOf(xDestination);
                yDest = yAxis.indexOf(yDestination);

                // find pawn that is going to xDest, yDest
                try {
                    int[] location = findPiece(hFileRank, wb, "P", xDest, yDest, bCapture, currentBoard);

                    // so the new board doesn't have a pawn at xDest, ySource
                    // but does have a pawn at xDest, yDest
                    currentBoard[yDest][xDest] = currentBoard[location[1]][location[0]];
                    currentBoard[location[1]][location[0]] = "";

                    // for en passant
                    if (bCapture && (yDest == 5 || yDest == 2)) {
                        // the pawn just moved to column 3 or 4.
                        int yDestroy = wb == "w" ? 4 : 3;
                        currentBoard[yDestroy][xDest] = "";
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        } else {
            try {
                xDest = xAxis.indexOf(intersect(xAxis, move));
                yDest = yAxis.indexOf(intersect(yAxis, move));

                if (move.contains("=")) {
                    // pawn promotion, remove pawn, add queen.
                    // queen goes on xDest, yDest
                    currentBoard[yDest][xDest] = wb == "w" ? "wq" : "bq";
                    int ySource = wb == "w" ? 6 : 1;
                    currentBoard[ySource][xDest] = "";
                } else {
                    int[] location = findPiece(hFileRank, wb, xOther, xDest, yDest, bCapture, currentBoard);

                    try {
                        // so the piece is at xSource, ySource
                        // and must move to xDest, yDest
                        currentBoard[yDest][xDest] = currentBoard[location[1]][location[0]];
                        currentBoard[location[1]][location[0]] = "";
                    } catch (ArrayIndexOutOfBoundsException ex) {
                        Log.i(tag, "Did not find piece.");
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        return currentBoard;
    }

    private static int[] findPiece(String wb, String sType, int x, int y, boolean bCapture, String[][] board) throws Exception {
        int xSource = 0;
        int ySource = 0;
        boolean bFound = false;

        try {
            // x and y are the destination, but what's the source? find the pawn
            outerloop:
            for (ySource = 0; ySource < 8; ySource++) {
                for (xSource = 0; xSource < 8; xSource++) {
                    String strPiece = board[ySource][xSource];

                    if (strPiece.equals(""))
                        continue;

                    Piece move = new Piece(sType, xSource, ySource, x, y);
                    move.setbDoesCapture(bCapture);
                    move.setColor(wb);
                    move.setBoard(board);

                    bFound = strPiece.equals(wb + sType.toLowerCase()) && move.isLegal();

                    if (bFound)
                        break outerloop;
                }
            }
        } catch (Exception ex) {

            ex.printStackTrace();
        }
        return new int[] { xSource, ySource };
    }

    private static int[] findPiece(String hRank, String wb, String sType, int x, int y, boolean bCapture, String[][] board) throws Exception {
        int xSource = xAxis.indexOf(hRank);
        int ySource = 0;
        boolean bFound = false;

        if (hRank.equals("")) {
            return findPiece(wb, sType, x, y, bCapture, board);
        } else {
            // is hRank really vRank?
            try {
                int vRank = Integer.parseInt(hRank);
                return findPiece(vRank, wb, sType, x, y, bCapture, board);
            }
            catch( Exception e ) { }
        }

        try {
            // x and y are the destination, but what's the source? find the piece
            // this overload is for a spec'ed hRank, so we already know the xSource
            for (ySource = 0; ySource < 8; ySource++) {
                String strPiece = board[ySource][xSource];

                if (strPiece.equals(""))
                    continue;

                Piece move = new Piece(sType, xSource, ySource, x, y);
                move.setbDoesCapture(bCapture);
                move.setColor(wb);
                move.setBoard(board);

                bFound = strPiece.equals(wb + sType.toLowerCase()) && move.isLegal();

                if (bFound)
                    break;
            }
        } catch (Exception ex) {

            ex.printStackTrace();
        }

        return new int[] { xSource, ySource };
    }

    private static int[] findPiece(Integer vRank, String wb, String sType, int x, int y, boolean bCapture, String[][] board) throws Exception {
        int xSource = 0;
        int ySource = vRank - 1;
        boolean bFound = false;

        try {
            // x and y are the destination, but what's the source? find the piece
            // this overload is for a spec'ed vRank, so we already know the ySource
            for (xSource = 0; xSource < 8; xSource++) {
                String strPiece = board[ySource][xSource];

                if (strPiece.equals(""))
                    continue;

                Piece move = new Piece(sType, xSource, ySource, x, y);
                move.setbDoesCapture(bCapture);
                move.setColor(wb);
                move.setBoard(board);

                bFound = strPiece.equals(wb + sType.toLowerCase()) && move.isLegal();

                if (bFound)
                    break;
            }
        } catch (Exception ex) {

            ex.printStackTrace();
        }

        return new int[] { xSource, ySource };
    }

    public static String intersect(String one, String two) {
        String strResult = "";
        String[] oneAry = one.split("");
        String[] twoAry = two.split("");

        int i = 0;
        int j = 0;

        // last character is the destination
        // 2nd to last is the source (horizontal rank)
        OL:
        for (j = twoAry.length - 1; j > 0; j--) {
            for (i = oneAry.length - 1; i > 0; i--) {
                if (oneAry[i].equals(twoAry[j])) {
                    strResult = oneAry[i];
                    break OL;
                }
            }
        }

        return strResult;
    }
}
