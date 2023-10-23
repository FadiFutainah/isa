package player;

import game.Board;
import models.Piece;
import models.State;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;

public class UCSPlayer extends Player {
    private final PriorityQueue<State> stateSpace = new PriorityQueue<>();
    public State initialState;
    private int[][] costs;

    public UCSPlayer() {
    }

    @Override
    public void initialize(Board board) {
        super.board = board;
        costs = new int[board.width][board.height];
        for (int[] row : costs) {
            Arrays.fill(row, 100000000);
        }
        initialState = new State(piece.y, piece.x, new ArrayList<>(), 0);
    }

    @Override
    public List<Piece> play() {
        List<Piece> path = new ArrayList<>();
        stateSpace.add(initialState);
        costs[initialState.x][initialState.y] = 0;
        while (!stateSpace.isEmpty()) {
            ++steps;
            State current = stateSpace.poll();
            piece.move(current.x, current.y);
            if (hasReached()) {
                path = current.path;
                break;
            }
            search(current);
        }
        return path;
    }

    @Override
    public void search(State current) {
        List<State> nextStates = lookAround(current);
        for (State state : nextStates) {
            if(board.isDead(state.x, state.y)) continue;
            int totalCost = costs[current.x][current.y] + state.cost;
            if (totalCost < costs[state.x][state.y]) {
                costs[state.x][state.y] = totalCost;
                stateSpace.add(state);
            }
        }
    }
}
