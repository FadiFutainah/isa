import data.Repository;
import game.Game;

public class Main {
    public static void main(String[] args) {
        Repository gameRepository = new Repository();
        Game game = gameRepository.loadGame("easy");
        game.start();
    }
}