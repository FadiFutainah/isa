package player;


import game.Board;
import models.Piece;
import models.State;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CommandLinePlayer extends Player {
    private Scanner scanner;

    @Override
    public void initialize(Board board) {
        super.board = board;
        scanner = new Scanner(System.in);
    }

    @Override
    public List<Piece> play() {
        int i = scanner.nextInt();
        List<Piece> path = new ArrayList<>();
        path.add(new Piece(i, lookDown(piece.y, i)));
        return path;
    }

    @Override
    public void search(State current) {

    }
}
