package service;

import static Utils.Helper.*;

import entity.ChessCheckmate;
import entity.ChessPiece;

import java.util.HashMap;
import java.util.Map;

public class CheckmateService {

    /*
     * - Comment luồng xử lí function checkmateKing():
     * - Thực hiện xử lí check xem khi thay đổi 1 nước cờ có xảy ra trường hợp chiếu tướng hay không
     * - Vòng for chessNormal sử dụng để duyệt các quân cờ có trên bàn cờ
     * - Vòng for i thực hiện duyệt row và vòng for j thực hiện để duyệt các column
     * - Tại vòng for j thực hiện kiểm tra tại row i và column j có phải bước đi hợp lệ đối với quân chessNormal đang duyệt hay koong
     *   +   Nếu hợp lệ thực hiện tìm kiếm xem có quân cờ nào đang ở vị trí row i và column j.
     *       nếu tồn tại thì thực hiện kiểm tra thêm 2 điều kiện là khác màu với quân cờ chessNormal đang xét và có type == VUA
     * - Nếu không có quân cờ Vua nào bị chiếu trả về null, ngược lại trả về object chứa quân cờ vua, quân cờ chiếu vua và row i, column j
     */
    public static ChessCheckmate checkmateKing() {
        for (ChessPiece chessNormal : chessPieces) {
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if (isValidStep(chessNormal, i, j)) {
                        ChessPiece chessCheckmate = getChessPieceAt(i, j);
                        if (chessCheckmate != null && chessCheckmate.getColor() != chessNormal.getColor() && chessCheckmate.getType().equals("VUA")) {
                            return new ChessCheckmate(chessCheckmate, chessNormal, i, j);
                        }
                    }
                }
            }
        }
        return null;
    }

    /*
     * - Tham số truyền vào là quân cờ chiếu vua
     * - For duyệt list quân cờ phía đối thủ bị chiếu nếu có quân cờ nào có thể ăn quân cờ đang chiếu vua thì trả về true
     * - Ngược lại không có thì trả về false
     * */
    public static boolean killCheckmateKing(ChessPiece chessMate) {
        boolean result = false;
        for (ChessPiece chessPiece : chessPieces) {
            if (!chessPiece.getColor().equals(chessMate.getColor())) {
                result = isValidStep(chessPiece, chessMate.getRow(), chessMate.getColumn());
            }
            if (result) break;
        }
        return result;
    }

    /*
     * - Tham số truyền vào là quân vua và quân chiếu quân vua
     * - Nếu có thể có 1 quân cờ có thể bảo vệ quân vua bằng cách phù hợp với 1 trong những ô ở giữa thì trả về true, ngược lại trả về false
     * */
    public static boolean shieldChessKing(ChessPiece chessMateKing, ChessPiece chessKing) {
        Map<Integer, Integer> listLocationShield = new HashMap<>();
        switch (chessMateKing.getType()) {
            case "XE":
                listLocationShield = listLocationShieldXeAndVua(chessMateKing, chessKing);
                break;
            case "TUONG":
                listLocationShield = listLocationShieldTuongAndVua(chessMateKing, chessKing);
                break;
            case "HAU":
                if (chessMateKing.getColumn() != chessKing.getColumn() && chessMateKing.getRow() != chessKing.getRow()) {
                    listLocationShield = listLocationShieldTuongAndVua(chessMateKing, chessKing);
                } else {
                    listLocationShield = listLocationShieldXeAndVua(chessMateKing, chessKing);
                }
        }
        boolean result = false;
        for (ChessPiece chess : chessPieces ) {
            if(chess.getColor().equals(chessKing.getColor()) && !chess.getType().equals("VUA") && listLocationShield.size()>0){
                for (Integer row: listLocationShield.keySet()) {
                    result = isValidStep(chess, row, listLocationShield.get(row));
                    if (result) break;
                }
                if (result) break;
            }
        }
        return result;
    }

    /*
     * - Dùng để trả về các vị trí có thể chắn trước mặt quân chiếu vua (quân xe) và quân vua
     * - Tham số truyền vào là quân chiếu vua và quân vua
     * - Xảy ra 2 trường hơp là cùng 1 row hoặc cùng 1 column đối với quân chiếu và quân xe
     * */
    private static Map<Integer, Integer> listLocationShieldXeAndVua(ChessPiece chessMateKing, ChessPiece chessKing) {
        Map<Integer, Integer> result = new HashMap<>();
        if (chessKing.getRow() == chessMateKing.getRow()) {
            int min = Math.min(chessMateKing.getColumn(), chessKing.getColumn()) + 1;
            int max = Math.max(chessMateKing.getColumn(), chessKing.getColumn());
            for (int i = min; i < max; i++) {
                result.put(chessKing.getRow(), i);
            }
        } else {
            int min = Math.min(chessMateKing.getRow(), chessKing.getRow()) + 1;
            int max = Math.max(chessMateKing.getRow(), chessKing.getRow());
            for (int i = min; i < max; i++) {
                result.put(i, chessKing.getColumn());
            }
        }
        return result;
    }

    /*
     * - Dùng để trả về các vị trí có thể chắn trước mặt quân chiếu vua (quân tượng) và quân vua
     * - Tham số truyền vào là quân chiếu vua và quân vua
     * */
    private static Map<Integer, Integer> listLocationShieldTuongAndVua(ChessPiece chessMateKing, ChessPiece chessKing) {
        Map<Integer, Integer> result = new HashMap<>();
        int deltaX = chessMateKing.getRow() - chessKing.getRow();
        int deltaY = chessMateKing.getColumn() - chessKing.getColumn();
        for (int i = 1; i < Math.abs(deltaX); i++) {
            int row = chessKing.getRow() + (deltaX < 0 ? -i : i);
            int column = chessKing.getColumn() + (deltaY < 0 ? -i : i);
            result.put(row, column);
        }
        return result;
    }
}
