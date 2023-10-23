package ui;

import structure.Edge;
import structure.Node;
import structure.State;
import structure.Vehicle;
import utils.Colors;

import java.util.*;

public class Presenter {
    private final String padding = " ".repeat(45);

    private final Map<String, String> colors = new HashMap<>();

    public String[][] map;

    public Presenter(int w, int h) {
        initColors();
        map = new String[w][h];
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                map[j][i] = "   ";
            }
        }
    }

    private void initColors() {
        colors.put("   ", Colors.GREEN);
        colors.put("    ", Colors.GREEN);
        colors.put(" H ", Colors.PURPLE);
        colors.put(" E ", Colors.RED);
        colors.put(" - ", Colors.BLACK);
        colors.put(" T ", Colors.YELLOW);
        colors.put(" B ", Colors.YELLOW);
        colors.put(" W ", Colors.YELLOW);
        colors.put(" | ", Colors.BLACK);
        colors.put(" * ", Colors.BLACK);
        for (int i = 0; i < 50; i++) {
            colors.put(" " + i + " ", Colors.BLUE);
            colors.put(" " + i, Colors.BLUE);
        }
    }

    private String getVehicleLetter(Vehicle v) {
        switch (v) {
            case BUS -> {
                return "B";
            }
            case TAXI -> {
                return "T";
            }
            default -> {
                return "W";
            }
        }
    }

    public void initMap(ArrayList<Node> nodes) {
        for (int k = 0; k < nodes.size(); ++k) {
            Node n = nodes.get(k);
            map[n.y][n.x] = k < 10 ? " " + k + " " : " " + k;
            for (Edge e : n.edges) {
                Node d = nodes.get(e.destination);
                if (d.x == n.x) {
                    for (int i = Math.min(d.y, n.y) + 1; i < Math.max(d.y, n.y); ++i) {
                        if (Objects.equals(map[i][n.x], "   ")) {
                            if (e.impossibleToDrive) {
                                map[i][n.x] = " * ";
                            } else {
                                map[i][n.x] = " - ";
                            }
                        }
                    }
                } else {
                    for (int i = Math.min(d.x, n.x) + 1; i < Math.max(d.x, n.x); ++i) {
                        if (Objects.equals(map[n.y][i], "   ")) {
                            if (e.impossibleToDrive) {
                                map[n.y][i] = " * ";
                            } else {
                                map[n.y][i] = " | ";
                            }
                        }

                    }
                }
            }
        }
        for (int i = 0; i < map[0].length; i++) {
            for (int j = 0; j < map.length; ++j) {
                if ((j == 0 || j == map.length - 1) && i % 2 == 0) map[j][i] += " ";
            }
        }
        map[nodes.get(0).y][nodes.get(0).x] = " H ";
        map[nodes.get(nodes.size() - 1).y][nodes.get(nodes.size() - 1).x] = " E ";
    }

    private void printCell(String cell) {
        System.out.print(colors.get(cell));
        System.out.print(cell);
        System.out.print(Colors.RESET);
    }

    public void printMap(State state) {
        if (state.parent != null) {
            Node d = state.node;
            Node n = state.parent.node;
            String v = getVehicleLetter(state.vehicle);
            if (state.node.x == state.parent.node.x) {
                for (int i = Math.min(d.y, n.y) + 1; i < Math.max(d.y, n.y); ++i) {
                    map[i][n.x] = " " + v + " ";
                }
            } else {
                for (int i = Math.min(d.x, n.x) + 1; i < Math.max(d.x, n.x); ++i) {
                    map[n.y][i] = " " + v + " ";
                }
            }
        }
        System.out.println();
        for (int i = 0; i < map[0].length; i++) {
            if (i % 2 == 0) {
                System.out.print(padding.substring(1));
            } else {
                System.out.print(padding);
            }
            for (String[] strings : map) {
                printCell(strings[i]);
            }
            System.out.println();
        }
    }

    public void printSolution(List<State> solution, List<Node> nodes, boolean uiEnabled) {
        if (uiEnabled) {
            for (State state : solution) {
                printMap(state);
            }
        }
        for (State s : solution) {
            System.out.println("The node number " + nodes.indexOf(s.node) + " : ");
            System.out.println(s);
        }
    }
}
