package service;

import entity.ChessPiece;

import static Utils.Helper.*;

public class StepService {
    public static boolean isValidTuong(int startRow, int startColumn, int endRow, int endColumn) {
        int deltaX = endRow - startRow;
        int deltaY = endColumn - startColumn;

        for (int i = 1; i < Math.abs(deltaX); i++) {
            int row = startRow + (deltaX < 0 ? -i : i);
            int column = startColumn + (deltaY < 0 ? -i : i);
            ChessPiece chessSelect = getChessPieceAt(row, column);
            if (chessSelect != null) return false;
        }

        if (Math.abs(deltaX) == Math.abs(deltaY)) {
            return true;
        }

        return false;
    }

    public static boolean validHau(int startRow, int startColumn, int endRow, int endColumn) {
        return isValidQuanXe(startRow, startColumn, endRow, endColumn) || isValidTuong(startRow, startColumn, endRow, endColumn);
    }


    public static boolean isValidQuanMa(int startRow, int startColumn, int endRow, int endColumn) {
        int deltaX = Math.abs(endRow - startRow);
        int deltaY = Math.abs(endColumn - startColumn);

        // Kiểm tra xem nước đi có di chuyển theo quy tắc của quân Mã hay không
        if ((deltaX == 2 && deltaY == 1) || (deltaX == 1 && deltaY == 2)) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isValidQuanXe(int startX, int startY, int endX, int endY) {
        int deltaX = endX - startX;
        int deltaY = endY - startY;
        if (deltaX == 0 && deltaY != 0) {
            for (int i = 1; i < Math.abs(deltaY); i++) {
                int index = deltaY < 0 ? i * -1 : i;
                ChessPiece chessBetween = getChessPieceAt(startX, startY + index);
                if (null != chessBetween) {
                    //Tồn tại 1 quân cờ giữa đường đi
                    printLog("Tồn tại 1 quân cờ giữa đường đi!");
                    return false;
                }
            }
            return true;
        }
        if (deltaY == 0 && deltaX != 0) {
            for (int i = 1; i < Math.abs(deltaX); i++) {
                int index = deltaX < 0 ? i * -1 : i;
                ChessPiece chessBetween = getChessPieceAt(startX + index, startY);
                if (null != chessBetween) {
                    //Tồn tại 1 quân cờ giữa đường đi
                    printLog("Tồn tại 1 quân cờ giữa đường đi!");
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public static boolean validVua(int startRow, int startColumn, int endRow, int endColumn) {
        int deltaX = endRow - startRow;
        int deltaY = endColumn - startColumn;

        if (Math.abs(deltaX) == 1 && deltaY == 0) return true;
        if (Math.abs(deltaY) == 1 && deltaX == 0) return true;
        return Math.abs(deltaY) == 1 && Math.abs(deltaX) == 1;
    }

    public static boolean isValidQuanTot(int startX, int startY, int endX, int endY, boolean isFirstMove,
                                         boolean isBlackPiece) {
        int deltaX = endX - startX;
        int deltaY = endY - startY;
        /*Kiểm tra hướng di chuyển của Quân Tốt dựa trên màu sắc của nó
        1 đi lên
        -1 đi xuống*/
        int direction = isBlackPiece ? 1 : -1;

        // Kiểm tra nước đi lên trên một ô
        if (deltaX == -direction && deltaY == 0) {
            ChessPiece chessSelect = getChessPieceAt(endX, endY);
            return chessSelect == null;
        }

        // Kiểm tra nước đi lên trên hai ô (lần di chuyển đầu tiên)
        if (isFirstMove && deltaX == -2 * direction && deltaY == 0) {
            return true;
        }

        //Kiểm tra đi chéo ăn quân đối thủ
        if (Math.abs(deltaX) == 1) {
            ChessPiece chessSelect = getChessPieceAt(endX, endY);
            return chessSelect != null;
        }

        return false;
    }

}
