package minesweeper;

import java.util.Scanner;

public class Player {
    private int minesToGuess = 0;
    private int minesHit = 0;
    private int currentGuesses = 0;
    private int currentFrees = 0; // records amount of 'frees' used by player
    Scanner scan = new Scanner(System.in);

    public int getInitialInput() {
        int numMines = 0;
        while (minesToGuess == 0) {
            System.out.println("How many mines do you want on the field?");
            numMines = scan.nextInt();
            if (numMines <= 0 || numMines > 81) {
                System.out.println("Invalid amount of mines, please enter a valid amount");
            } else {
                minesToGuess = numMines;
            }
        }
        return numMines;
    }

    // Return 0 if game is not over , Return 1 if game is over
    public int handleGuessCoordinates(GameBoard gameboard) {
        System.out.println("Set/unset mines marks or claim a cell as free ");
        int gameOver = 0;

        int x = scan.nextInt();
        int y = scan.nextInt();
        String guessType = scan.next().toLowerCase();
        String guessResult;

        if ((1 <= x && x <= 9) && (1 <= y && y <= 9)) {
            // Valid guess, proceed with guess logic
            guessResult = gameboard.handleGuess(y - 1, x - 1, guessType);

            switch (guessResult) {
                case "Number":
                    System.out.println("There is a number here!");
                    //this.handleGuessCoordinates(gameboard);
                    break;
                case "Bomb":
                    minesHit++;
                    currentGuesses++;
                    if (minesHit == minesToGuess && currentGuesses == minesToGuess) {
                        System.out.println("Congratulations! You found all the mines!");
                        scan.close();
                        gameOver = 1;
                        break;
                    }
                    break;
                case "Delete Mark From Bomb":
                    minesHit--;
                    currentGuesses--;
                    break;
                case "Delete Mark Non-Bomb":
                    currentGuesses--;
                    break;
                case "Add Mark":
                    currentGuesses++;
                    break;
                case "BombFreed":
                    if (currentFrees == 0) {
                        // first guess is a mine, so swap the guessed mine with a non-mine cell
                        gameboard.moveMine(y - 1, x - 1);
                        // free the passed in cell now that it's not a bomb
                        gameboard.handleGuess(y - 1, x - 1, guessType);
                    }
                    else {
                        System.out.println("You stepped on a mine and failed!");
                        scan.close();
                        gameOver = 1;
                        break;
                    }
                case "CellsFreed":
                    if (gameboard.getCellsFreed() == gameboard.getMaxFreeCells()) {
                        System.out.println("Congratulations! You found all the mines!");
                        scan.close();
                        gameOver = 1;
                    }
                    break;
                default:
                    break;
            }
            if (guessType.equals("free")) {
                currentFrees++;
            }
        }
        return gameOver;
    }
}
