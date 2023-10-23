package player;

import game.Board;
import models.Piece;
import models.State;

import java.util.*;

public class AstarPlayer extends Player {
    private final PriorityQueue<State> stateSpace = new PriorityQueue<>();
    public State initialState;
    private int[][] costs;

    public AstarPlayer() {
    }

    public int costEstimation(int y1, int y2) {
        if(target.y == y2) return 0;
        return (y2 > y1) ? target.y - y2 : 1000000001;
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

    public State pickMinimum() {
        List<State> temp = new ArrayList<>();
        State state = stateSpace.poll();
        temp.add(state);
        while (!stateSpace.isEmpty() && Objects.requireNonNull(stateSpace.peek()).cost == Objects.requireNonNull(state).cost) {
            temp.add(stateSpace.poll());
        }
        for (State s : temp) {
            assert state != null;
            if (s.cost - costEstimation(state.y + 1, state.y) < state.cost - costEstimation(s.y, state.y)) {
                state = s;
            }
        }
        for (State s : temp) {
            if (!s.equals(state)) {
                stateSpace.add(s);
            }
        }
        return state;
    }

    @Override
    public List<Piece> play() {
        List<Piece> path = new ArrayList<>();
        stateSpace.add(initialState);
        costs[initialState.x][initialState.y] = 0;
        while (!stateSpace.isEmpty()) {
            ++steps;
            State current = pickMinimum();
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
            int totalCost = costs[current.x][current.y] + state.cost + costEstimation(current.y, state.y);
            if (totalCost < costs[state.x][state.y]) {
                costs[state.x][state.y] = totalCost;
                state.cost += costEstimation(current.y, state.y);
                stateSpace.add(state);
            }
        }
    }
}
