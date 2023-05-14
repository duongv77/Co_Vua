package Utils;

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
    public static String PATH_IMAGE = "D:\\Liên thông\\java\\demo1\\src\\Utils\\image\\";

    public static void printLog(String message){
        System.setProperty("file.encoding", "UTF-8");
        PrintWriter writer = new PrintWriter(System.out, true, StandardCharsets.UTF_8);
        writer.println(message);
    }

    public static void showPopupNotify(String message){
        JOptionPane.showMessageDialog(null, message);
    }

    public static void showPopupMultipleOption(String title, String message, Object[] options ){
        JOptionPane.showOptionDialog(null, message, title,
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
    }

    public static List<ChessPiece> initDefaultChessMan(){
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
//        chessPieces.add(new ChessPiece("TOT", Color.WHITE, 1, 0));
//        chessPieces.add(new ChessPiece("TOT", Color.WHITE, 1, 1));
//        chessPieces.add(new ChessPiece("TOT", Color.WHITE, 1, 2));
//        chessPieces.add(new ChessPiece("TOT", Color.WHITE, 1, 3));
//        chessPieces.add(new ChessPiece("TOT", Color.WHITE, 1, 4));
//        chessPieces.add(new ChessPiece("TOT", Color.WHITE, 1, 5));
//        chessPieces.add(new ChessPiece("TOT", Color.WHITE, 1, 6));
//        chessPieces.add(new ChessPiece("TOT", Color.WHITE, 1, 7));
        //Hết bên trắng

        //Bên đen
//        chessPieces.add(new ChessPiece("TOT", Color.BLACK, 6, 0));
//        chessPieces.add(new ChessPiece("TOT", Color.BLACK, 6, 1));
//        chessPieces.add(new ChessPiece("TOT", Color.BLACK, 6, 2));
//        chessPieces.add(new ChessPiece("TOT", Color.BLACK, 6, 3));
//        chessPieces.add(new ChessPiece("TOT", Color.BLACK, 6, 4));
//        chessPieces.add(new ChessPiece("TOT", Color.BLACK, 6, 5));
//        chessPieces.add(new ChessPiece("TOT", Color.BLACK, 6, 6));
//        chessPieces.add(new ChessPiece("TOT", Color.BLACK, 6, 7));
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
        return result;
    }

}
