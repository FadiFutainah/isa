package ui;

import models.Cell;
import utils.Colors;

public class Presenter {
    private final String padding = " ".repeat(60);

    public Presenter() {
    }

    private void drawCell(Cell cell) {
        String brick = " ";
        int index = cell.getIndex();
        if(cell.isMovable()){
            brick += index <= 9 ? index + " " : index;
        }else{
            brick += "  ";
        }
        System.out.print(cell.getColor());
        System.out.print(brick);
    }

    private void clearScreen(){
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public void viewBoard(Cell[][] board)  {
        try{
            clearScreen();
            System.out.print("\n\n");
            for (Cell[] cells : board) {
                System.out.print(padding);
                for (Cell cell : cells) {
                    drawCell(cell);
                }
                System.out.println(Colors.RESET);
            }
            System.out.print("\n\n");
            Thread.sleep(350);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
