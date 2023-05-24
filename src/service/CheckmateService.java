package service;

import static Utils.Helper.*;

import entity.ChessCheckmate;
import entity.ChessPiece;

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
    * - Kiểm tra xem nước đi của quân vua có bị chiếu không
    * */
    public static boolean checkmateKingCustom(int row, int column) {
        for (ChessPiece chessNormal : chessPieces) {
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if (isValidStep(chessNormal, i, j)) {
                        if(i == row && j == column){
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
}
