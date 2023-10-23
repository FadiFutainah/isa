package player;

import game.Board;
import models.Piece;
import models.State;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class DFSPlayer extends Player {
    private final Stack<State> stateSpace = new Stack<>();
    public State initialState;
    private boolean[][] visited;

    public DFSPlayer(){}

    @Override
    public void initialize(Board board) {
        super.board = board;
        visited = new boolean[board.width][board.height];
        initialState = new State(piece.y, piece.x, new ArrayList<>(), 0);
    }

    @Override
    public List<Piece> play() {
        List<Piece> path = new ArrayList<>();
        stateSpace.add(initialState);
        while (!stateSpace.isEmpty()) {
            ++steps;
            State current = stateSpace.pop();
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
        if (!visited[current.x][current.y]) {
            visited[current.x][current.y] = true;
            List<State> nextStates = lookAround(current);
            for (State state : nextStates) {
                if(board.isDead(state.x, state.y)) continue;
                if (!visited[state.x][state.y]) {
                    stateSpace.add(state);
                }
            }
        }
    }
}
