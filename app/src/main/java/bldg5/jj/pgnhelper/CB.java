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
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import bldg5.jj.pgnhelper.common.Game;
import bldg5.jj.pgnhelper.common.Utils;

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

        try {
            // nMoveNumber is the UI move number, ie, 10 corresponds to 5 in the PGNs
            int nPGNMoveNumber = (int) (nMoveNumber + 1) / 2;
            String strWhite = "";
            String strBlack = "";

            String movePGN = aryPGNs[nPGNMoveNumber].trim();

            strWhite = movePGN.toString().split(" ")[0];
            strBlack = movePGN.toString().split(" ")[1];

            if (nMoveNumber % 2 == 1)
                strReturn = strWhite;
            else
                strReturn = strBlack;

            // if there are comments, even one, it's in an array, ie ["just this one comment"]
            if (movePGN.contains("{") && movePGN.contains("}")) {
                String str_regex = "[{}]";
                String moveComment = movePGN.split(str_regex.toString())[1].trim();
                strReturn += "\n" + moveComment;
            }

        } catch(IndexOutOfBoundsException ex) {
            strReturn = "End of game.";
        }

        return strReturn;
    }

    public CB(Context context, Game game) {
        super(context);
        initializeViews(context);

        setGame(game);
    }

    public CB(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeViews(context);





        String[][] board = Snapshot.PGN2Board(nMoveNumber, aryPGNs);
        Drawboard(board);
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game thisGame) {
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

        pgns = "1.e4 e5 2.Nf3 Nc6 3.Bc4 Bc5 4.c3 Nf6  5.b4 Bb6 6.d3 d6 7.Nbd2 O-O 8.Bb3 a5  9.b5 Ne7 10.O-O c6 11.bxc6 Nxc6 12.Nc4 Bc7  13.Bg5 Qe7 14.Ne3 Be6 15.Bd5 h6 16.Bxf6 Qxf6  17.a4 Rab8 18.Rb1 Bd7 19.Nd2 Nd8 20.c4 Ne6 21.Bxe6 fxe6 22.Qb3 Bc6 23.Rbc1 Qe7 24.Nb1 Qd7  25.Nc3 Bd8  1/2-1/2";
        pgns = "1. e4 c5 2. Nf3 Nc6 3. Bb5 g6 4. Bxc6 dxc6 5. d3 Bg7 6. h3 Nf6 7. Nc3 Nd7 8. O-O e5 9. Be3 Qe7 10. a3 Nf8 11. Na4 Ne6 12. b4 b6 13. bxc5 b5 14. Nb2 Nxc5 15. Qd2 Ne6 16. a4 a6 17. axb5 cxb5 18. c4 Bd7 19. cxb5 Bxb5 20. Nc4 O-O 21. Nb6 Rad8 22. Nd5 Qb7 23. Bh6 Bxh6 24. Qxh6 f6 25. Rfd1 Rxd5 26. exd5 Qxd5 27. Qe3 Rd8 28. Qb6 Kg7 29. Rac1 g5 30. Rd2 Rd7 31. Qb8 Bc6 32. Qb6 Bb7 33. Rb2 Kg6 34. Ne1 Nf4 35. Kh2 Nxd3 36. Rd1 e4 37. Nxd3 exd3 38. f3 Qe5+ 39. Kg1 d2 40. Rbxd2 Rxd2 41. Rxd2 Qe1+ 42. Kh2 Qxd2 43. Qxb7 Qd6+ 44. Kh1 a5 45. Qe4+ Kg7 46. Qb7+ Kh6 47. Qb3 Qd4 48. Qa3 Qb4 49. Qa1 Kg6 50. Qa2 a4 51. Qg8+ Kh6 52. Qd8 Qe1+ 53. Kh2 Qe6 54. Qf8+ Kg6 55. Qd8 Kg7 56. Qc7+ Kh6 57. Qd8 Kg7 58. Qc7+ Qf7 59. Qc5 Qe8 60. Qc7+ Kh6 61. f4 Kg6 62. Qc2+ f5 63. g4 Qe4 64. gxf5+ Kxf5 65. Qc5+ Kxf4 66. Qf2+ Ke5 67. Qc5+ Kf6 68. Qf8+ Kg6 69. Qg8+ Kh6 70. Qf8+ Kg6 { and about 30 more moves} 1/2-1/2";
        aryPGNs = pgns.split(regex.toString());

        // store the number of moves in this game
        numMovesInGame = aryPGNs.length - 1;
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
            currentBoard = Snapshot.toTheEnd(aryPGNs);
            Drawboard(currentBoard);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void toMoveNumber(int n) {
        currentBoard = Snapshot.PGN2Board(n, aryPGNs);
        Drawboard(currentBoard);
    }

    public void switchSides() {
        bIsFlipped = !bIsFlipped;
        Drawboard(this.currentBoard);
    }

    public void halfMove() {
        String[][] board = currentBoard;
        board = Snapshot.oneMove(nMoveNumber, aryPGNs, currentBoard);

        currentBoard = board;
        Drawboard(board);
    }

    public void halfMoveBackwards() {
        String[][] board = Snapshot.PGN2Board(nMoveNumber, aryPGNs);
        currentBoard = board;
        Drawboard(board);
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
