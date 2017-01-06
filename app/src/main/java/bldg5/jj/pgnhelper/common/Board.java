package bldg5.jj.pgnhelper.common;

public class Board {
    // private static final int nWidth = 8;
    // private static final int nLength = 8;

    // what color is the square x, y? returns "w" or "b"
    public static String squareColor(int x, int y) {
        return (x + y) % 2 == 0 ? "b" : "w";
    }
}
