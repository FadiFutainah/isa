package models;


import java.util.List;
import java.util.Objects;

public class State implements Comparable<State> {
    public int y;
    public int x;
    public List<Piece> path;
    public int cost;

    public State(int y, int x, List<Piece> path, int cost) {
        this.y = y;
        this.x = x;
        this.path = path;
        this.cost = cost;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof State state)) return false;
        return y == state.y && x == state.x && cost == state.cost && Objects.equals(path, state.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(y, x, path, cost);
    }

    @Override
    public int compareTo(State o) {
        return (this.cost < o.cost) ? -1 : 1;
    }
}
