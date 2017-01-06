package bldg5.jj.pgnhelper.common;

public class Piece {
    private int x;
    private int y;
    private int xDestination;
    private int yDestination;
    private String type; // poss types R, N, B, Q, K, P

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
        boolean bIsValid = (strType == "R") || (strType == "N") || (strType == "B") || (strType == "Q") || (strType == "K") || (strType == "P");

        if (bIsValid) {
            this.type = strType;
        } else {
            throw new Exception("Invalid chess piece type: " + strType);
        }
    }

    public boolean isLegal() throws Exception {
        boolean bResult = false;
        boolean bCorrectSlope = false;

        if (this.type == "R") {
            // slope is infinity or 0
            bResult = xDestination == x || yDestination == y;
            return bResult;
        }

        if (this.type == "N") {
            // knight never moves to the same color
            bResult = (Board.squareColor(this.x, this.y) == "w" && Board.squareColor(this.xDestination, this.yDestination) == "b") ||
                    (Board.squareColor(this.x, this.y) == "b" && Board.squareColor(this.xDestination, this.yDestination) == "w");

            // slope is 2 or .5
            bCorrectSlope = (Math.abs((yDestination - y) / (xDestination - x)) == 2) ||
                    (Math.abs(((yDestination - y) * 1.0) / (xDestination - x)) == 0.5d);

            bResult = bResult && bCorrectSlope;
            return  bResult;
        }

        if (this.type == "B") {
            // bishop stays on his color
            bResult = (Board.squareColor(this.x, this.y) == "w" && Board.squareColor(this.xDestination, this.yDestination) == "w") ||
                    (Board.squareColor(this.x, this.y) == "b" && Board.squareColor(this.xDestination, this.yDestination) == "b");

            bCorrectSlope = Math.abs((yDestination - y) / (xDestination - x)) == 1;

            bResult = bResult && bCorrectSlope;
            return  bResult;
        }

        if (this.type == "Q") {
            // queen can move like a rook or a bishop
            Piece bishop = new Piece("B");
            Piece rook = new Piece("R");

            bishop.setLocation(this.x, this.y);
            bishop.setDestination(this.xDestination, this.yDestination);

            rook.setLocation(this.x, this.y);
            rook.setDestination(this.xDestination, this.yDestination);

            bResult = bishop.isLegal() && rook.isLegal();
            return bResult;
        }

        if (this.type == "K") {
            // King ... one space in any direction
            // castle not implemented.
            bResult = yDestination == y + 1 ||
                    yDestination == y - 1 ||
                    xDestination == x + 1 ||
                    xDestination == x - 1;

            return bResult;
        }

        if (this.type == "P") {
            // pawn. minus en passant.
            bResult = yDestination == y + 1 ||
                    yDestination == y - 1 ||
                    (y == 1 && yDestination == 3);
        }

        return  bResult;
    }
}
