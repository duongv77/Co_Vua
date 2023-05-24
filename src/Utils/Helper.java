package Utils;

import entity.ChessCheckmate;
import entity.ChessPiece;
import service.StepService;

import javax.swing.*;
import java.awt.*;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Helper {
    public static List<ChessPiece> chessPieces;
    public static String PATH_IMAGE = "src\\Utils\\image\\";

    public static void printLog(String message) {
        System.setProperty("file.encoding", "UTF-8");
        PrintWriter writer = new PrintWriter(System.out, true, StandardCharsets.UTF_8);
        writer.println(message);
    }

    public static void showPopupNotify(String message) {
        JOptionPane.showMessageDialog(null, message);
    }

    public static void showPopupMultipleOption(String title, String message, Object[] options) {
        JOptionPane.showOptionDialog(null, message, title,
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
    }

    public static List<ChessPiece> initDefaultChessMan() {
        /*
        - Khởi tạo các quân cờ bên trắng và đen.
        - Trắng hàng thứ 6 và 7
        - Đen hàng thứ 0 và 1
        */
        List<ChessPiece> chessPieces = new ArrayList<>();
        //Bên trắng
        chessPieces.add(new ChessPiece("XE", Color.WHITE, 0, 0));
        chessPieces.add(new ChessPiece("MA", Color.WHITE, 0, 1));
        chessPieces.add(new ChessPiece("TUONG", Color.WHITE, 0, 2));
        chessPieces.add(new ChessPiece("HAU", Color.WHITE, 0, 3));
        chessPieces.add(new ChessPiece("VUA", Color.WHITE, 0, 4));
        chessPieces.add(new ChessPiece("TUONG", Color.WHITE, 0, 5));
        chessPieces.add(new ChessPiece("MA", Color.WHITE, 0, 6));
        chessPieces.add(new ChessPiece("XE", Color.WHITE, 0, 7));
        chessPieces.add(new ChessPiece("TOT", Color.WHITE, 1, 0));
        chessPieces.add(new ChessPiece("TOT", Color.WHITE, 1, 1));
        chessPieces.add(new ChessPiece("TOT", Color.WHITE, 1, 2));
        chessPieces.add(new ChessPiece("TOT", Color.WHITE, 1, 3));
        chessPieces.add(new ChessPiece("TOT", Color.WHITE, 1, 4));
        chessPieces.add(new ChessPiece("TOT", Color.WHITE, 1, 5));
        chessPieces.add(new ChessPiece("TOT", Color.WHITE, 1, 6));
        chessPieces.add(new ChessPiece("TOT", Color.WHITE, 1, 7));
        //Hết bên trắng

        //Bên đen
        chessPieces.add(new ChessPiece("TOT", Color.BLACK, 6, 0));
        chessPieces.add(new ChessPiece("TOT", Color.BLACK, 6, 1));
        chessPieces.add(new ChessPiece("TOT", Color.BLACK, 6, 2));
        chessPieces.add(new ChessPiece("TOT", Color.BLACK, 6, 3));
        chessPieces.add(new ChessPiece("TOT", Color.BLACK, 6, 4));
        chessPieces.add(new ChessPiece("TOT", Color.BLACK, 6, 5));
        chessPieces.add(new ChessPiece("TOT", Color.BLACK, 6, 6));
        chessPieces.add(new ChessPiece("TOT", Color.BLACK, 6, 7));
        chessPieces.add(new ChessPiece("XE", Color.BLACK, 7, 0));
        chessPieces.add(new ChessPiece("MA", Color.BLACK, 7, 1));
        chessPieces.add(new ChessPiece("TUONG", Color.BLACK, 7, 2));
        chessPieces.add(new ChessPiece("HAU", Color.BLACK, 7, 3));
        chessPieces.add(new ChessPiece("VUA", Color.BLACK, 7, 4));
        chessPieces.add(new ChessPiece("TUONG", Color.BLACK, 7, 5));
        chessPieces.add(new ChessPiece("MA", Color.BLACK, 7, 6));
        chessPieces.add(new ChessPiece("XE", Color.BLACK, 7, 7));
        //Hết bên đen
        return chessPieces;
    }

    public static ChessPiece getChessPieceAt(int row, int column) {
        //Trả về quân cờ vị trí row và column
        for (ChessPiece piece : chessPieces) {
            if (piece.getRow() == row && piece.getColumn() == column) {
                return piece;
            }
        }
        return null;
    }


    /*
     * - Truyền vào row và column để xác định hướng đi của quân cờ
     * - Nước đi hợp lệ trả về true - ngược lại false
     * - Nếu true kiểm tra thêm ô đó có quân cờ không. nếu có thì phải khác màu mới trả về true. ngược lại trả về false
     * */
    public static boolean isValidStep(ChessPiece piece, int row, int column) {
        boolean result = false;
        switch (piece.getType()) {
            case "MA" -> result = StepService.isValidQuanMa(piece.getRow(), piece.getColumn(), row, column);
            case "TOT" -> {
                boolean isFirstMove;
                if (piece.getColor() == Color.WHITE) {
                    isFirstMove = piece.getRow() == 1;
                } else {
                    isFirstMove = piece.getRow() == 6;
                }
                result = StepService.isValidQuanTot(piece.getRow(), piece.getColumn(), row, column, isFirstMove, piece.getColor() == Color.BLACK);
            }
            case "XE" -> result = StepService.isValidQuanXe(piece.getRow(), piece.getColumn(), row, column);
            case "TUONG" -> result = StepService.isValidTuong(piece.getRow(), piece.getColumn(), row, column);
            case "HAU" -> result = StepService.validHau(piece.getRow(), piece.getColumn(), row, column);
            case "VUA" -> result = StepService.validVua(piece.getRow(), piece.getColumn(), row, column);
        }
        if (result) {
            return checkColor(piece, row, column);
        } else return false;
    }

    /*
     * - Kiểm tra hết cờ hay chưa
     * - Lần lượt kiểm tra các vị trí đi xung quanh của quân vua có hợp lệ không
     *   + Nếu hợp lệ kiểm tra nước đó đi xong có bị chiếu tiếp không
     * - Mô hình nước đi của quân vua:
     * 1    2   3
     * 4   Vua  6
     * 7    8   9
     *  - Nếu tồn tại nước đi không bị chiếu tướng trả về true
     *  - Ngược lại trả về false
     * */
    public static boolean checkChessDie(ChessCheckmate chessCheckmate) {
        int startRow = chessCheckmate.getKing().getRow();
        int startColumn = chessCheckmate.getKing().getColumn();
        boolean water1 = checkKing(chessCheckmate.getKing(), Math.max(startRow - 1, 0), Math.max(startColumn - 1, 0));
        boolean water2 = checkKing(chessCheckmate.getKing(), Math.max(startRow - 1, 0), startColumn);
        boolean water3 = checkKing(chessCheckmate.getKing(), Math.max(startRow - 1, 0), Math.max(startColumn + 1, 0));
        boolean water4 = checkKing(chessCheckmate.getKing(), startRow, Math.max(startColumn - 1, 0));
        boolean water6 = checkKing(chessCheckmate.getKing(), startRow, Math.max(startColumn + 1, 0));
        boolean water7 = checkKing(chessCheckmate.getKing(), Math.max(startRow + 1, 0), Math.max(startColumn - 1, 0));
        boolean water8 = checkKing(chessCheckmate.getKing(), Math.max(startRow + 1, 0), startColumn);
        boolean water9 = checkKing(chessCheckmate.getKing(), Math.max(startRow + 1, 0), Math.max(startColumn + 1, 0));

        return !water1 && !water2 && !water3 && !water4 && !water6 && !water7 && !water8 && !water9;
    }

    /*
     * - Kiểm tra xem nước đi sắp tới của quân vua có bị bất kỳ quân nào trên bàn cờ chiếu không
     * - Tất cả không chiếu vua thì trả về true: đây là nước đi an toàn
     * */
    public static boolean checkKing(ChessPiece chessKing, int row, int column) {
        if (row == chessKing.getRow() && column == chessKing.getColumn()) return false;
        ChessPiece chessPieceAt = getChessPieceAt(row, column);
        if (chessPieceAt != null && chessKing.getColor() == chessPieceAt.getColor()) {
            return false;
        }
        return checkKingCompetitor(chessKing, row, column);
    }

    /*
     * - Kiểm tra các quân còn lại trên bàn cờ có màu khác với màu của quân vua đang kiểm tra
     * - Return true nếu không có quân nào có thể giết quân vua, false nếu có 1 quân có thể ăn quân vua
     * */
    public static boolean checkKingCompetitor(ChessPiece chessKing, int row, int column) {
        for (ChessPiece chessPiece : chessPieces) {
            if (chessKing.getColor() != chessPiece.getColor()) {
                if (isValidStepCustomCheckKing(chessKing, chessPiece, row, column)) return false;
            }
        }
        return true;
    }


    /*
     * - Truyền vào tham số quân cờ và hàng cột muốn di chuyển tới
     * - Kiểm tra xem hàng cột đó đã có tồn tại quân nào chưa
     * - Nếu tồn tại kiểm tra nếu khác màu trả về true ngược lại trả về false
     * */
    public static boolean checkColor(ChessPiece chessPiece, int row, int column) {
        ChessPiece chess = getChessPieceAt(row, column);
        if (chess == null) return true;
        return chessPiece.getColor() != chess.getColor();
    }

    public static boolean isValidStepCustomCheckKing(ChessPiece chessKing, ChessPiece piece, int row, int column) {
        boolean result = false;
        switch (piece.getType()) {
            case "MA" -> result = StepService.isValidQuanMa(piece.getRow(), piece.getColumn(), row, column);
            case "TOT" -> {
                boolean isFirstMove;
                if (piece.getColor() == Color.WHITE) {
                    isFirstMove = piece.getRow() == 1;
                } else {
                    isFirstMove = piece.getRow() == 6;
                }
                result = StepService.isValidQuanTot(piece.getRow(), piece.getColumn(), row, column, isFirstMove, piece.getColor() == Color.BLACK);
            }
            case "XE" -> result = StepService.isValidQuanXeCustomCheckKing(chessKing, piece.getRow(), piece.getColumn(), row, column);
            case "TUONG" -> result = StepService.isValidTuongCustomCheckKing(chessKing, piece.getRow(), piece.getColumn(), row, column);
            case "HAU" -> result = StepService.validHauCustomCheckKing(chessKing, piece.getRow(), piece.getColumn(), row, column);
            case "VUA" -> result = StepService.validVua(piece.getRow(), piece.getColumn(), row, column);
        }
         return result;
    }
}
