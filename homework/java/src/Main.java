import game.Game;
import player.*;


public class Main {

    public static void main(String[] args) {
        Player player = new AstarPlayer();
        Game game = new Game(player);
        game.loadFromFile();
        game.run();
    }
}