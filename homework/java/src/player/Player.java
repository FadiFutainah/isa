package player;


import game.Board;
import models.Cell;
import models.Piece;
import models.State;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.abs;

public abstract class Player {
    public Piece piece;
    public Piece target;
    public Board board;
    public int steps = 0;

    public Player() {
    }

    public abstract void initialize(Board board);

    public abstract List<Piece> play();

    public abstract void search(State current);

    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    public void setTarget(Piece target) {
        this.target = target;
    }

    public boolean hasReached() {
        return piece.equals(target);
    }

    public int howHarmful(int y, int x) {
        return (int) Math.pow(2, abs(y - lookDown(y, x)));
    }

    public int lookDown(int y, int x) {
        while (board.hasDown(x, y)) {
            ++y;
        }
        return y;
    }

    public State getState(State parent, int x, int y) {
        List<Piece> newPath = new ArrayList<>(parent.path);
        newPath.add(new Piece(x, lookDown(y, x)));
        return new State(lookDown(y, x), x, newPath, howHarmful(y, x));
    }

    public List<State> lookAround(State state) {
        List<State> nextStates = new ArrayList<>();
        int x = state.x;
        int y = state.y;
        Cell[][] grid = board.getGrid();
        for (int left = x - 1; left >= 0 && grid[y][left].isNotBrick(); --left) {
            nextStates.add(getState(state, left, y));
        }
        for (int right = x + 1; right < grid[0].length && grid[y][right].isNotBrick(); ++right) {
            nextStates.add(getState(state, right, y));
        }
        return nextStates;
    }

    public void report() {
        System.out.println("========================================");
        System.out.println("\t\tNumber of calls : " + steps);
        System.out.println("========================================");
    }
}
