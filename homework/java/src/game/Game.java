package game;

import data.GameRepository;
import models.Level;
import models.Piece;
import player.Player;
import ui.Presenter;

import java.util.List;
import java.util.Scanner;

public class Game {
    private final Presenter presenter;
    public Player player;
    public Board board;

    public Game(Player player) {
        this.player = player;
        presenter = new Presenter();
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public void initLvl() {
        board.placePlayer(player.piece.x, player.piece.y);
        board.placeTarget(player.target.x, player.target.y);
        board.togglePossibleMovesVisibility(player.piece.x, player.piece.y, true);
    }

    public void loadFromFile() {
        GameRepository repository = new GameRepository();
        Level level = repository.loadLevel();
        setBoard(level.getBoard());
        player.setPiece(level.getPiece());
        player.setTarget(level.getTarget());
        player.initialize(board);
        initLvl();
    }

    private void movePlayer() {
        board.placePlayer(player.piece.x, player.piece.y);
    }

    private void paintGame() {
        Piece piece = player.piece;
        board.togglePossibleMovesVisibility(piece.x, piece.y, true);
        presenter.viewBoard(board.getGrid());
        board.togglePossibleMovesVisibility(piece.x, piece.y, false);
    }

    private void pickUpPlayer() {
        if (player.piece.x == player.target.x && player.piece.y == player.target.y) return;
        board.removePlayer(player.piece.x, player.piece.y);
    }

    public void run() {
        paintGame();
        start();
    }

    public void move(List<Piece> path) {
        for (Piece step : path) {
            pickUpPlayer();
            player.piece.move(step.x, step.y);
            movePlayer();
            paintGame();
        }
    }

    public void start() {
        do {
            pickUpPlayer();
            List<Piece> solution = player.play();
            move(solution);
        } while (!gameOver());
        player.report();
    }

    private boolean gameOver() {
        return player.hasReached();
    }
}
