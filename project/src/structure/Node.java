package structure;

import java.util.ArrayList;
import java.util.Objects;

public class Node {
    public int x;
    public int y;
    public double busCallingTime;
    public double taxiCallingTime;
    public ArrayList<Edge> edges;

    public Node(int x, int y, double busCallingTime, double taxiCallingTime) {
        this.x = x;
        this.y = y;
        this.edges = new ArrayList<>();
        this.taxiCallingTime = taxiCallingTime;
        this.busCallingTime = busCallingTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Node node)) return false;
        return x == node.x && y == node.y && Double.compare(node.busCallingTime, busCallingTime) == 0 && Double.compare(node.taxiCallingTime, taxiCallingTime) == 0 && Objects.equals(edges, node.edges);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, busCallingTime, taxiCallingTime, edges);
    }
}
