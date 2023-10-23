package data;

import game.Board;
import models.Level;
import models.Piece;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Objects;
import java.util.Scanner;

/**
 * input files should start with the commands:
 * P : player position.
 * T : target position.
 * V : vertical wall.
 * H : horizontal wall.
 * B : board dimensions.
 * **/

public class GameRepository {
    private Scanner loader;

    public GameRepository() {
    }

    public Scanner getLoader()  {
        String file = "src/resources/board_lvl3.txt";
        try {
            return new Scanner(new File(file));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public Level loadLevel(){
        Level level = new Level();
        loader = getLoader();
        while (loader.hasNext()) {
            String choice = loader.next();
            switch (choice) {
                case "B" -> level.setBoard(scanBoard());
                case "P" -> level.setPiece(scanPiece());
                case "T" -> level.setTarget(scanPiece());
                case "H", "V" -> scanWall(level.getBoard(), choice);
            }
        }
        loader.close();
        return level;
    }

    private Board scanBoard() {
        int width = loader.nextInt();
        int height = loader.nextInt();
        return new Board(height, width);
    }

    private Piece scanPiece() {
        int x = loader.nextInt();
        int y = loader.nextInt();
        return new Piece(x, y);
    }

    private void scanWall(Board board, String orientation) {
        int position = loader.nextInt();
        int start = loader.nextInt();
        int end = loader.nextInt();
        if (Objects.equals(orientation, "H")) {
            board.createHorizontalWall(position, start, end);
        } else if (Objects.equals(orientation, "V")) {
            board.createVerticalWall(position, start, end);
        }
    }
}
