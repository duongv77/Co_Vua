package entity;

public class ChessCheckmate {
    private ChessPiece king;
    private ChessPiece chessCheckmate;
    private Integer row;
    private Integer column;

    public ChessCheckmate() {
    }

    public ChessCheckmate(ChessPiece king, ChessPiece chessCheckmate, Integer row, Integer column) {
        this.king = king;
        this.chessCheckmate = chessCheckmate;
        this.row = row;
        this.column = column;
    }

    public ChessPiece getKing() {
        return king;
    }

    public void setKing(ChessPiece king) {
        this.king = king;
    }

    public ChessPiece getChessCheckmate() {
        return chessCheckmate;
    }

    public void setChessCheckmate(ChessPiece chessCheckmate) {
        this.chessCheckmate = chessCheckmate;
    }

    public Integer getRow() {
        return row;
    }

    public void setRow(Integer row) {
        this.row = row;
    }

    public Integer getColumn() {
        return column;
    }

    public void setColumn(Integer column) {
        this.column = column;
    }
}
