package bldg5.jj.pgnhelper.common;

public class Piece {
    private int x;
    private int y;
    private int xDestination;
    private int yDestination;
    private String type; // poss types R, N, B, Q, K, P
    private String color; // poss types w, b
    private boolean bDoesCapture = false;
    private String[][] board = new String[8][8];

    public Piece(String strType) throws Exception {
        setType(strType);
    }

    public Piece(String strType, int x, int y, int xDestination, int yDestination) throws Exception {
        setType(strType);
        setLocation(x, y);
        setDestination(xDestination, yDestination);
    }

    public int getX() {
        return this.x;
    }

    public void setX(int xLocation) {
        this.x = xLocation;
    }

    public int getY() {
        return this.x;
    }

    public void setY(int yLocation) {
        this.x = yLocation;
    }

    public int[] getLocation() {
        return new int[] { this.x, this.y };
    }

    public void setLocation(int xLocation, int yLocation) {
        this.x = xLocation;
        this.y = yLocation;
    }

    public int[] getDestination() {
        return new int[] { this.x, this.y };
    }

    public void setDestination(int xLocation, int yLocation) {
        this.xDestination = xLocation;
        this.yDestination = yLocation;
    }

    public String getType() {
        return this.type.toUpperCase();
    }

    public void setType(String strType) throws Exception {
        strType = strType.toUpperCase();
        boolean bIsValid = strType.equals("R") || strType.equals("N") || strType.equals("B") || strType.equals("Q") || strType.equals("K") || strType.equals("P");

        if (bIsValid) {
            this.type = strType;
        } else {
            throw new Exception("Invalid chess piece type: " + strType);
        }
    }

    public String getColor() {
        return this.color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public boolean getDoesCapture() {
        return this.bDoesCapture;
    }

    public void setbDoesCapture(boolean bDoesCapture) {
        this.bDoesCapture = bDoesCapture;
    }

    public String[][] getBoard() {
        return this.board;
    }

    public void setBoard(String[][] board) {
        this.board = board;
    }

    public boolean isLegal() throws Exception {
        boolean bResult = false;
        boolean bCorrectSlope = false;
        boolean bCorrectDisplacement = false;

        if (this.type.equals("R")) {
            // slope is infinity or 0
            bResult = xDestination == x || yDestination == y;

            return bResult;
        }

        if (this.type.equals("N")) {
            // knight never moves to the same color
            bResult = !Board.squareColor(this.x, this.y).equals(Board.squareColor(this.xDestination, this.yDestination));

            try {
                // slope is 2 or .5
                bCorrectSlope = (Math.abs((yDestination - y) / (xDestination - x)) == 2) ||
                        (Math.abs(((yDestination - y) * 1.0) / (xDestination - x)) == 0.5d);
            } catch (ArithmeticException ex) {
                // Divide by zero error.
                bCorrectSlope = false;
            }

            // max displacement in either horizontal or vertical is 3.
            bCorrectDisplacement = Math.abs(yDestination - y) <= 3 && Math.abs(xDestination - x) <= 3;

            bResult = bResult && bCorrectSlope && bCorrectDisplacement;
            return  bResult;
        }

        if (this.type.equals("B")) {
            // bishop stays on his color
            bResult = Board.squareColor(this.x, this.y).equals(Board.squareColor(this.xDestination, this.yDestination));

            try {
                bCorrectSlope = Math.abs((yDestination - y) / (xDestination - x)) == 1;
            } catch (ArithmeticException ex) {
                // bishops don't move up and down like this. Divide by zero error.
                bCorrectSlope = false;
            }

            bResult = bResult && bCorrectSlope;
            return  bResult;
        }

        if (this.type.equals("Q")) {
            // queen can move like a rook or a bishop
            Piece bishop = new Piece("B");
            Piece rook = new Piece("R");

            bishop.setLocation(this.x, this.y);
            bishop.setDestination(this.xDestination, this.yDestination);

            rook.setLocation(this.x, this.y);
            rook.setDestination(this.xDestination, this.yDestination);

            bResult = bishop.isLegal() || rook.isLegal();
            return bResult;
        }

        if (this.type.equals("K")) {
            // King ... one space in any direction
            // King two spaces to either g8 or g1
            boolean bCastle = xDestination == 6 && (yDestination == 0 || yDestination == 7);
            bResult = yDestination == y + 1 ||
                    yDestination == y - 1 ||
                    xDestination == x + 1 ||
                    xDestination == x - 1;

            bResult = (!bResult && bCastle) || bResult;

            return bResult;
        }

        if (this.type.equals("P")) {
            // pawn. minus en passant.
            // a pawn can move horizontally one space either to or fro (if capturing)
            boolean bHorizontalOk = (bDoesCapture && (xDestination - 1 == x || xDestination == x - 1)) ||
                    (!bDoesCapture && x == xDestination);

            if (this.color == "w") {
                bResult = (yDestination == y + 1 && bHorizontalOk) ||
                        (x == xDestination && y == 1 && yDestination == 3);
            } else {
                bResult = (yDestination == y - 1 && bHorizontalOk) ||
                        (x == xDestination && y == 6 && yDestination == 4);
            }
        }

        return  bResult;
    }
}
