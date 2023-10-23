package models;

import game.Board;

public class Level {
    private Board board;
    private Piece piece;
    private Piece target;

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public Piece getPiece() {
        return piece;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    public Piece getTarget() {
        return target;
    }

    public void setTarget(Piece target) {
        this.target = target;
    }
}
