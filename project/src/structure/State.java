package structure;

import java.util.Objects;

public class State implements Comparable<State> {
    public Node node;
    public double priorityCost;
    public String busPath;
    public State parent;
    public double cost;
    public double distance;
    public double time;
    public double health;
    public Vehicle vehicle;

    public State(double priorityCost, Node node, String busPath, State parent, double cost, double distance, double time, double health, Vehicle vehicle) {
        this.priorityCost = priorityCost;
        this.node = node;
        this.busPath = busPath;
        this.parent = parent;
        this.cost = cost;
        this.distance = distance;
        this.time = time;
        this.health = health;
        this.vehicle = vehicle;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof State state)) return false;
        return Double.compare(state.priorityCost, priorityCost) == 0 && Double.compare(state.cost, cost) == 0 && Double.compare(state.distance, distance) == 0 && Double.compare(state.time, time) == 0 && Double.compare(state.health, health) == 0 && Objects.equals(node, state.node) && Objects.equals(busPath, state.busPath) && Objects.equals(parent, state.parent) && vehicle == state.vehicle;
    }

    @Override
    public int hashCode() {
        return Objects.hash(node, priorityCost, busPath, parent, cost, distance, time, health, vehicle);
    }

    @Override
    public int compareTo(State o) {
        return Double.compare(priorityCost, o.priorityCost);
    }

    @Override
    public String toString() {
        return " priorityCost=" + String.format("%.2f", priorityCost) +
                "\n busPath='" + busPath + '\'' +
                "\n cost=" + String.format("%.2f", cost) +
                "\n distance=" + String.format("%.2f", distance) +
                "\n time=" + String.format("%.2f", time) +
                "\n health=" + String.format("%.2f", health) +
                "\n vehicle=" + vehicle +
                "\n ========================== \n";
    }
}
