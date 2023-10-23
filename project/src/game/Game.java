package game;

import player.Player;
import structure.Node;
import structure.State;
import ui.Presenter;

import java.util.List;

public class Game {
    public Player player;
    public Presenter presenter;

    public Game() {
    }

    public void setPresenter(Presenter presenter) {
        this.presenter = presenter;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    private void report(State last){
        System.out.println("the solution costs " + String.format("%.2f", last.cost) + " s.p");
        System.out.println("the solution takes " + String.format("%.2f", last.time) + " hr");
        System.out.println("the solution costs " + String.format("%.2f", player.health - last.health) + " health unit");
        System.out.println("the solution path length is " + String.format("%.2f", last.distance) + " km");
    }

    public void start() {


        Node home = player.stations.get(player.stations.size() - 1);

        State initialState = new State(0, player.stations.get(0), "",
                null, 0, 0, 0, player.health, null);


        boolean goAllOut = false;
        boolean uiEnabled = true;

        presenter.initMap(player.stations);
        presenter.printMap(initialState);

        player.setHome(home);
        List<List<State>> solutions = player.play(initialState, goAllOut);

        if (solutions.size() == 0) {
            System.out.println("NO POSSIBLE SOLUTION FOR THE CURRENT INPUT");
        } else {
            for (int i = 0; i < solutions.size(); ++i) {
                if (solutions.size() > 1) System.out.println("the solution number " + (i + 1) + " : ");
                report(solutions.get(i).get(solutions.get(i).size() - 1));
                presenter.printSolution(solutions.get(i), player.stations, uiEnabled);
            }
        }
    }
}
