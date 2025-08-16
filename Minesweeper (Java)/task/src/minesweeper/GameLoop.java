package minesweeper;
import java.util.Scanner;

// Notes / Testing info @: C:\Users\crowe\AppData\Roaming\JetBrains\IdeaIC2025.1\scratches

public class GameLoop {

    public static boolean isGameWon = false;

    public static void main(String[] args) {

        Player newPlayer = new Player();
        int numMines = newPlayer.getInitialInput();
        GameBoard game = new GameBoard();
        game.createMineField(9,9,numMines);
        game.displayField();
        int gameOver = 0;

        // main game loop after first guess
        while (!isGameWon) {
            gameOver = newPlayer.handleGuessCoordinates(game);
            if (gameOver == 1) {
                isGameWon = true;
                game.displayField("Epic Fail");
                continue;
            }
            game.displayField();
        }

    }
}
