package minesweeper;
import java.util.Random;

public class GameBoard {

    private Cell[][] minefield;

    public GameBoard () {}

    private int cellsFreed = 0;

    private int maxFreeCells;

    public void setMinefield (Cell[][] field) {
        this.minefield = field;
    }

    public Cell[][] getMinefield () {
        return this.minefield;
    }

    public int getCellsFreed() {
        return cellsFreed;
    }

    public int getMaxFreeCells() {
        return maxFreeCells;
    }

    // Method to display the minefield in the terminal
    public void displayField() {
        if (minefield == null) {
            return;
        }
        //display board header (first two rows)
        this.displayBoardHeader();

        // display board contents
        for (int i = 0; i < minefield.length; i++) {
            System.out.printf("%d|", i+1);
            for (int j = 0; j < minefield[0].length; j++) {
                System.out.print(minefield[i][j].getDisplayValue());
            }
            System.out.print("|\n");
        }

        // display board footer (last row)
        this.displayBoardFooter();
    }

    /*
    Overloaded version of displayField called when player frees a mine
     */
    public void displayField(String bombed) {
        if (minefield == null) {
            return;
        }
        //display board header (first two rows)
        this.displayBoardHeader();

        // display board contents
        for (int i = 0; i < minefield.length; i++) {
            System.out.printf("%d|", i+1);
            for (int j = 0; j < minefield[0].length; j++) {
                if (minefield[i][j].getType() == 1) {
                    System.out.print("X");
                }
                else {
                    System.out.print(minefield[i][j].getDisplayValue());
                }
            }
            System.out.print("|\n");
        }

        // display board footer (last row)
        this.displayBoardFooter();
    }

    private void displayBoardHeader() {
        int numCols = minefield[0].length;

        System.out.print(" |");
        for (int i = 1; i < numCols + 1; i++) {
            System.out.print(i);
        }
        System.out.print("|\n");

        System.out.print("-|");
        for (int i = 0; i < numCols; i++) {
            System.out.print("-");
        }
        System.out.print("|\n");
    }

    private void displayBoardFooter() {
        int numCols = minefield[0].length;

        System.out.print("-|");
        for (int i = 0; i < numCols; i++) {
            System.out.print("-");
        }
        System.out.print("|\n");
    }

    //Method to set display values when the players guess type is "free"
    private void freeCells(int i, int j) {
        if (((0 <= i && i <= 8) && (0 <= j && j <= 8)) && !minefield[i][j].getIsExplored()) {
            try {
                minefield[i][j].setIsExplored(true);
                //displayField();
                int surroundingBombs = minefield[i][j].getSurroundingBombs();
                //System.out.println("surroundingBombs: " + surroundingBombs);
                //System.out.println("Current coordinates being freed: " + (j + 1) + "," + (i + 1));
                if (surroundingBombs != 0) {
                    minefield[i][j].setDisplayValue(Integer.toString(surroundingBombs));
                }
                else {
                    minefield[i][j].setDisplayValue("/");
                    for (int x = (i-1); x < (i+2); x++) {
                        for (int y = (j - 1); y < (j + 2); y++) {
                            freeCells(x,y);
                        }
                    }
                }
                cellsFreed++;
            } catch (Exception e) {
                //do nothing
            }
        }
    }

    // Method to set the minefield
    public void createMineField(int numColumns, int numRows, int numMines) {
        // validate input params, uncomment later if getting board size from player
        if (numRows <= 0 || numColumns <= 0 || numMines <= 0 || numMines > (numRows * numColumns)) {
            return;
        }

        Random random = new Random();
        int randomRow;
        int randomColumn;
        minefield = new Cell[numRows][numColumns];
        int surroundingMines;
        Integer surrMines = 0;
        maxFreeCells = 81 - numMines;

        while (numMines > 0) {
            randomRow = random.nextInt(numRows);
            randomColumn = random.nextInt(numColumns);
            if (minefield[randomRow][randomColumn] == null) {
                minefield[randomRow][randomColumn] = new Cell(1, ".");
                numMines--;
            }
        }


        for (int i = 0; i < minefield.length; i++) {
            for (int j = 0; j < minefield[0].length; j++) {
                if (minefield[i][j] == null) {
                    minefield[i][j] = new Cell(0, ".");
                    // check surrounding cells for bombs, record num in cell
                    surroundingMines = checkSurroundingCells(minefield, i, j);
                    minefield[i][j].setDisplayValue(".");
                    // Comment out 2 lines below if not debugging
                    //surrMines = surroundingMines;
                    //minefield[i][j].setDisplayValue(surrMines.toString());
                    minefield[i][j].setSurroundingBombs(surroundingMines);
                    /*
                    if (surroundingMines != 0) {
                        minefield[i][j].setSurroundingBombs(surroundingMines);
                    }
                    else {
                        minefield[i][j].setDisplayValue("" + surroundingMines + "");
                    }
                    */
                }
            }
        }
    }

    // Return number of mines around passed in cell
    private static int checkSurroundingCells (Cell[][] minefield, int i, int j) {
        int currAdjCell;
        int numAdjacentBombs = 0;
        int[][] adjacentCells = {
                {i - 1,j - 1},{i, j - 1},{i + 1, j - 1},
                {i - 1,j},{i + 1, j},
                {i - 1,j + 1},{i, j + 1},{i +1, j + 1},
        };

        for (int k = 0; k < 8; k++) {
            try {
                currAdjCell = minefield[adjacentCells[k][0]][adjacentCells[k][1]].getType();
                if (currAdjCell == 1) {
                    numAdjacentBombs++;
                }
            } catch (Exception e) {
                // Do Nothing
            }
        }

        return numAdjacentBombs;
    }

    // Pretty computationally expensive, only called when first free is a mine
    public void moveMine(int i, int j) {
        Random random = new Random();
        int randomRow;
        int randomColumn;
        int surroundingMines;
        boolean mineMoved = false;

        //swap out mine with a non-mine cell
        while (!mineMoved) {
            randomRow = random.nextInt(9);
            randomColumn = random.nextInt(9);
            if (minefield[randomRow][randomColumn].getType() == 0) {
                minefield[randomRow][randomColumn].setType(1);
                mineMoved = true;
            }
        }

        // Set passed in mine to be a non-bomb
        minefield[i][j].setType(0);

        // re-calculate surrounding bombs amount for all cells
        for (int x = 0; x < minefield.length; x++) {
            for (int y = 0; y < minefield[0].length; y++) {
                if (minefield[x][y].getType() == 0) {
                    surroundingMines = checkSurroundingCells(minefield, x, y);
                    //minefield[i][j].setDisplayValue(".");
                    minefield[x][y].setSurroundingBombs(surroundingMines);
                }
            }
        }
    }

    public String handleGuess(int x, int y, String guessType) {
        //guess coordinates are validated before this method is called
        String boardValue = minefield[x][y].getDisplayValue();
        String cellContains;

        if (guessType.equals("mine")) {
            switch (boardValue) {
                case ".":
                    cellContains = "Add Mark";
                    minefield[x][y].setDisplayValue("*");
                    if (minefield[x][y].getType() == 0) {
                        cellContains = "Add Mark";
                    } else {
                        cellContains = "Bomb";
                    }
                    break;
                case "*":
                    minefield[x][y].setDisplayValue(".");
                    if (minefield[x][y].getType() == 1) {
                        cellContains = "Delete Mark From Bomb";
                    } else {
                        cellContains = "Delete Mark Non-Bomb";
                    }
                    break;
                default:
                    cellContains = "Number";
            }
        }
        else if (guessType.equals("free")) {
            if (minefield[x][y].getType() == 1) {
                cellContains = "BombFreed";
            }
            else{
                freeCells(x, y);
                cellContains = "CellsFreed";
            }
        }
        else {
            cellContains = "Invalid";
        }

        return cellContains;

    }

}

