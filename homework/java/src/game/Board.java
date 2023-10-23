package game;


import models.Cell;
import utils.Colors;


public class Board {
    public final int height;
    public final int width;
    private final Cell[][] grid;

    public Board(int height, int width) {
        this.height = height;
        this.width = width;
        grid = new Cell[height][width];
        for (int i = 0; i < grid.length; ++i) {
            for (int j = 0; j < grid[i].length; ++j) {
                grid[i][j] = new Cell();
            }
        }
    }

    public Cell[][] getGrid() {
        return grid;
    }

    public void placePlayer(int x, int y) {
        grid[y][x].setColor(Colors.PURPLE);
    }

    public void removePlayer(int x, int y) {
        grid[y][x].setColor(Colors.BLACK);
    }

    public void placeTarget(int x, int y) {
        grid[y][x].setColor(Colors.RED);
    }

    public void createVerticalWall(int x, int startY, int endY) {
        for (int i = startY; i <= endY; ++i) {
            grid[i][x].setColor(Colors.GREEN);
        }
    }

    public void createHorizontalWall(int y, int startX, int endX) {
        for (int i = startX; i <= endX; ++i) {
            grid[y][i].setColor(Colors.GREEN);
        }
    }

    public void togglePossibleMovesVisibility(int x, int y, boolean show) {
        for (int left = x - 1; left >= 0 && grid[y][left].isNotBrick(); --left) {
            grid[y][left].setIndex(show ? left : -1);
        }
        for (int up = y - 1; up >= 0 && grid[up][x].isNotBrick(); --up) {
            grid[up][x].setIndex(show ? up : -1);
        }
        for (int right = x + 1; right < width && grid[y][right].isNotBrick(); ++right) {
            grid[y][right].setIndex(show ? right : -1);
        }
    }

    public boolean hasDown(int x, int y) {
        if (y + 1 == height) return false;
        return grid[y + 1][x].isNotBrick();
    }

    public boolean isDead(int x, int y) {
        return false;
    }
}
