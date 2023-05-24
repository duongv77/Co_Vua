package entity;

import java.awt.*;

public class ChessPiece {
    private String type;
    private Color color;
    private int row;
    private int column;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public ChessPiece(String type, Color color, int row, int column) {
        this.type = type;
        this.color = color;
        this.row = row;
        this.column = column;
    }

    public ChessPiece cloneChess(){
        return new ChessPiece(this.type, this.color, this.row, this.column);
    }
}
