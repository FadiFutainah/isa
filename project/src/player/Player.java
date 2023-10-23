package player;

import structure.Edge;
import structure.Node;
import structure.State;
import structure.Vehicle;

import java.util.*;

public class Player {
    private final PriorityQueue<State> stateSpace = new PriorityQueue<>();
    private final Map<State, Boolean> visited = new HashMap<>();
    public ArrayList<String> lineTable;
    public ArrayList<Node> stations;
    public double walkingSpeed;
    public double taxiCost;
    public double health;
    public double pocket;
    public double busCost;
    private Node home;
    private int priorityCost;

    public Player(double walkingSpeed, double health, double pocket, double busCost, double taxiCost) {
        this.health = health;
        this.pocket = pocket;
        this.taxiCost = taxiCost;
        this.busCost = busCost;
        this.walkingSpeed = walkingSpeed;
        this.stations = new ArrayList<>();
    }

    public void setStations(ArrayList<Node> stations) {
        this.stations = stations;
    }

    public void setLineTable(ArrayList<String> lineTable) {
        this.lineTable = lineTable;
    }

    public void setHome(Node home) {
        this.home = home;
    }

    public void scanPriorityCost() {
        Scanner input = new Scanner(System.in);
        System.out.println("choose the priority cost : ");
        System.out.println("1. minimum time.");
        System.out.println("2. minimum cost.");
        System.out.println("3. maximum health.");
        System.out.println("4. go all out!!.");
        priorityCost = input.nextInt();
        input.close();
    }

    private double theGreediestCost(State state) {
        double costWeight = 1;
        double timeWeight = 1;
        double healthWeight = -1;
        return costWeight * state.cost + timeWeight * state.time + healthWeight * state.health;
    }

    public void setPriorityCost(State state) {
        switch (priorityCost) {
            case 1 -> state.priorityCost = state.time;
            case 2 -> state.priorityCost = state.cost;
            case 3 -> state.priorityCost = -state.health;
            case 4 -> state.priorityCost = theGreediestCost(state);
        }
    }

    private List<State> takeABus(State current, Edge edge) {
        List<State> nexts = new ArrayList<>();
        int index = stations.indexOf(current.node);
        double health = current.health - 5 * edge.length;
        double distance = current.distance + edge.length;
        double time = current.time + edge.length / edge.busSpeed;
        double cost = current.cost;
        if (health < 0) return null;
        if (current.parent != null && current.parent.busPath.contains(String.valueOf(edge.destination))) {
            State next = new State(0, stations.get(edge.destination), current.busPath, current, cost, distance, time, health, Vehicle.BUS);
            nexts.add(next);
        }
        cost += 400;
        if (cost > pocket) return nexts;
        for (String path : lineTable) {
            if (path.contains(String.valueOf(index))) {
                State next = new State(0, stations.get(edge.destination), path, current, cost, distance, time, health, Vehicle.BUS);
                nexts.add(next);
            }
        }
        return nexts;
    }

    private State takeATaxi(State current, Edge edge) {
        String path = "";
        double health = current.health + 5 * edge.length;
        double distance = current.distance + edge.length;
        double cost = current.cost + taxiCost * edge.length;
        double time = current.time + edge.length / edge.taxiSpeed;
        State next = new State(0, stations.get(edge.destination), path, current, cost, distance, time, health, Vehicle.TAXI);
        if (cost > pocket) return null;
        return next;
    }

    private State takeAWalk(State current, Edge edge) {
        double cost = current.cost;
        String path = "";
        double distance = current.distance + edge.length;
        double health = current.health - 10 * edge.length;
        double time = current.time + edge.length / walkingSpeed;
        State next = new State(0, stations.get(edge.destination), path, current, cost, distance, time, health, Vehicle.WALK);
        if (health < 0) return null;
        return next;
    }

    public List<State> lookAround(State current) {
        List<State> nextStates = new ArrayList<>();
        for (Edge edge : current.node.edges) {
            List<State> busWays = null;
            State taxiWay = null;
            State walkWay = null;
            if (!edge.impossibleToDrive) {
                busWays = takeABus(current, edge);
                taxiWay = takeATaxi(current, edge);
            }
            walkWay = takeAWalk(current, edge);
            if (busWays != null) nextStates.addAll(busWays);
            if (taxiWay != null) nextStates.add(taxiWay);
            if (walkWay != null) nextStates.add(walkWay);
        }
        return nextStates;
    }

    private double heuristic(State current) {
        double startX = stations.get(0).x;
        double startY = stations.get(0).y;
        double xTargetDistance = home.x - current.node.x;
        double yTargetDistance = home.y - current.node.y;
        double xStartDistance = current.node.x - startX;
        double yStartDistance = current.node.y - startY;
        xTargetDistance *= xTargetDistance;
        yTargetDistance *= yTargetDistance;
        xStartDistance *= xStartDistance;
        yStartDistance *= yStartDistance;
        double distanceFromStart = Math.sqrt(xStartDistance + yStartDistance);
        double distanceToEnd = Math.sqrt(xTargetDistance + yTargetDistance);
        double distance = distanceFromStart + distanceToEnd;
        double heuristicValue = 0;
        double wellTestedValue = 10;
        switch (priorityCost) {
            case 1 -> {
                double healthToReach = distanceToEnd * 10;
                double timeToReach = distanceToEnd / walkingSpeed;
                double timeToReachByCurrentHealth = (current.health / 10) / walkingSpeed;
                double estimatedTime = (healthToReach > current.health) ?
                                            timeToReach
                                : timeToReachByCurrentHealth + (distanceToEnd - current.health / 10) * taxiCost;
                heuristicValue = estimatedTime;
            }
            case 2 -> {
                double costToReach = distanceToEnd * taxiCost;
                double distanceToReachByCurrentMoney = current.cost / taxiCost;
                double healthAfterTaxi = current.health + distanceToReachByCurrentMoney * 5;
                double distanceLeft = distanceToEnd - distanceToReachByCurrentMoney;
                double canWalkInDistanceLeft = Math.min(distanceLeft, healthAfterTaxi / 10);
                double noName = (distanceLeft - canWalkInDistanceLeft) * wellTestedValue;
                heuristicValue = (costToReach <= current.cost) ? costToReach : costToReach + noName;
            }
            case 3 -> {
                double canWalk = current.health / 10;
                double distanceLeftToWalk = distanceToEnd - canWalk;
                heuristicValue = (canWalk <= distanceToEnd) ? canWalk : canWalk + distanceLeftToWalk * wellTestedValue;
            }
            default -> heuristicValue = distance / walkingSpeed + distance * taxiCost - distance * 10;
        }
        return heuristicValue;
    }

    private double oldHeuristic(State current) {
        double startX = stations.get(0).x;
        double startY = stations.get(0).y;
        double xTargetDistance = home.x - current.node.x;
        double yTargetDistance = home.y - current.node.y;
        double xStartDistance = current.node.x - startX;
        double yStartDistance = current.node.y - startY;
        xTargetDistance *= xTargetDistance;
        yTargetDistance *= yTargetDistance;
        xStartDistance *= xStartDistance;
        yStartDistance *= yStartDistance;
        double distanceFromStart = Math.sqrt(xStartDistance + yStartDistance);
        double distanceToEnd = Math.sqrt(xTargetDistance + yTargetDistance);
        double distance = distanceFromStart + distanceToEnd;
        return switch (priorityCost) {
            case 1 -> distance / walkingSpeed;
            case 2 -> distance * taxiCost;
            case 3 -> distance * 10;
            default -> distance / walkingSpeed + distance * taxiCost - distance * 10;
        };
    }


    public double testHeuristic() {
        return 100;
    }

    public State pickMinimum() {
        List<State> temp = new ArrayList<>();
        State state = stateSpace.poll();
        temp.add(state);
        while (!stateSpace.isEmpty() && Objects.requireNonNull(stateSpace.peek()).priorityCost == Objects.requireNonNull(state).priorityCost) {
            temp.add(stateSpace.poll());
        }
        for (State s : temp) {
            assert state != null;
            if (s.priorityCost - heuristic(s) < state.priorityCost - heuristic(state)) {
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

    private List<State> getPath(State state) {
        List<State> path = new ArrayList<>();
        path.add(state);
        while (state.parent != null) {
            state = state.parent;
            path.add(state);
        }
        Collections.reverse(path);
        return path;
    }

    public void search(State current) {
        List<State> nextStates = lookAround(current);
        for (State state : nextStates) {
            if (visited.containsKey(state)) continue;
            setPriorityCost(state);
            visited.put(state, true);
            state.priorityCost += heuristic(state);
            stateSpace.add(state);
        }
    }

    public List<List<State>> play(State state, boolean goAllOut) {
        scanPriorityCost();
        long startTime = System.currentTimeMillis();
        int visitedNodes = 0;
        List<List<State>> paths = new ArrayList<>();
        List<State> minimumPath = null;
        stateSpace.add(state);
        visited.put(state, true);
        while (!stateSpace.isEmpty()) {
            State current = pickMinimum();
            ++visitedNodes;
            if (current.node.equals(home)) {
                List<State> path = getPath(current);
                if (!goAllOut) {
                    paths.add(path);
                    break;
                }
                if (minimumPath == null) {
                    minimumPath = path;
                    paths.add(path);
                    continue;
                }
                if (path.size() == minimumPath.size()) {
                    paths.add(path);
                    continue;
                }
                if (path.size() < minimumPath.size()) {
                    paths.clear();
                    paths.add(path);
                    minimumPath = path;
                    continue;
                }
            }
            search(current);
        }
        long endTime = System.currentTimeMillis();
        report(endTime - startTime, visitedNodes, paths.size());
        return paths;
    }

    public void report(long duration, int visitedNodes, int numberOfSolutions) {
        System.out.println("Report : ");
        System.out.println("the solution took : " + duration + " millisecond");
        System.out.println("the number of visited nodes : " + visitedNodes);
        System.out.println("the number of solutions : " + numberOfSolutions);
        System.out.println("----------------------");
    }
}
