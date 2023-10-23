package data;

import game.Game;
import player.Player;
import structure.Edge;
import structure.Node;
import ui.Presenter;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

public class Repository {
    Scanner loader;
    ArrayList<String> lineTable = new ArrayList<>();

    public Repository() {
    }

    private Scanner getLoader(String level) {
        String file = "src/resources/" + level + ".txt";
        try {
            return new Scanner(new File(file));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public Game loadGame(String level) {
        loader = getLoader(level);
        Game game = new Game();
        ArrayList<Node> nodes = new ArrayList<>();
        while (loader.hasNext()) {
            String ch = loader.next();
            switch (ch) {
                case "M" -> game.setPresenter(scanPresenter());
                case "N" -> game.setPlayer(scanPlayer());
                case "E" -> nodes.get(nodes.size() - 1).edges.add(scanEdge());
                case "D", "S", "P" -> nodes.add(scanNode());
            }
        }
        game.player.setStations(nodes);
        game.player.setLineTable(lineTable);
        loader.close();
        return game;
    }

    private Presenter scanPresenter() {
        int w = loader.nextInt();
        int h = loader.nextInt();
        return new Presenter(w, h);
    }

    private Player scanPlayer() {
        double walkingSpeed = loader.nextDouble();
        double health = loader.nextDouble();
        double money = loader.nextDouble();
        double busCost = loader.nextDouble();
        double taxiCost = loader.nextDouble();
        return new Player(walkingSpeed, health, money, busCost, taxiCost);
    }

    private Edge scanEdge() {
        int destination = loader.nextInt();
        double length = loader.nextDouble();
        boolean impossibleToDrive = loader.next().equals("1");
        String busPath = loader.next();
        double busSpeed = loader.nextDouble();
        double taxiSpeed = loader.nextDouble();
        if (!Objects.equals(busPath, "-1")) lineTable.add(busPath);
        return new Edge(destination, length, impossibleToDrive, busPath, busSpeed, taxiSpeed);
    }

    private Node scanNode() {
        int x = loader.nextInt();
        int y = loader.nextInt();
        double busCallingTime = loader.nextDouble();
        double taxiCallingTime = loader.nextDouble();
        return new Node(x, y, busCallingTime, taxiCallingTime);
    }
}
