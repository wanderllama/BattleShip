import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

public class Game {

    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);

        //create 8x8 grid
        String[][]grid = new String[8][8];

        //create 2D for ship positions -> each array element is a ship and has three elements for its positions
        String[][] ships = new String[3][3];

        //first array in grid is [0, 1, 2, 3, 4, 5, 6, 7]
        for (int i = 0; i < grid[0].length; i++) {
            grid[0][i] = i + "";
        }

        //add capital letter to first index of each grid starting with 'A'
        char letter = 'A';
        for (int i = 1; i < grid.length; i++) {
            grid[i][0] = "" + letter++;
        }

        //count variable to terminate while loop -> row and column variables
        int count = 0;
        int row;
        int column;

        while (count < 3) {

            //generate random number to determine axis of ship
            //axis = 0 -> horizontal
            //axis = 1 -> vertical
            int axis = ThreadLocalRandom.current().nextInt(0, 2);

            //if axis is horizontal ship can not have middle coordinate exist on left/right most edge
            //the index for horizontal ships middle coordinate must be between 2-6
            if (axis == 0) {
                row = ThreadLocalRandom.current().nextInt(0, 8);
                column = ThreadLocalRandom.current().nextInt(2, 7);

                //if the coordinates for the ship are free then add them to the board
                if (grid[row][column] == null && grid[row][column - 1] == null && grid[row][column + 1] == null) {
                    grid[row][column] = "1";
                    grid[row][column + 1] = "1";
                    grid[row][column - 1] = "1";

                    //convert the row value to letter, add the column value and create/store string in ships 2D array
                    //use count variable to choose which element in 2D array
                    ships[count][0] = "" + (char)(row + 64) + (column - 1);
                    ships[count][1] = "" + (char)(row + 64) + column;
                    ships[count][2] = "" + (char)(row + 64) + (column + 1);
                } else {//restart loop if one coordinate for ship is not free
                    continue;
                }

                //if axis is vertical ship can not have middle coordinate exist on top/bottom edge
                //the row for vertical ships middle coordinate must be between 2-6
            } else {
                row = ThreadLocalRandom.current().nextInt(2, 7);
                column = ThreadLocalRandom.current().nextInt(0, 8);

                //if coordinates are for the ship are free add them to the board
                if (grid[row][column] == null && grid[row - 1][column] == null && grid[row + 1][column] == null) {
                    grid[row][column] = "1";
                    grid[row + 1][column] = "1";
                    grid[row - 1][column] = "1";

                    //convert the row value to letter, add the column value and create/store string in ships 2D array
                    //use count variable to choose which element in 2D array
                    ships[count][0] = "" + (char)(row + 63) + column;
                    ships[count][1] = "" + (char)(row + 64) + column;
                    ships[count][2] = "" + (char)(row + 65) + column;
                } else {//restart loop if one coordinate for ship is not free
                    continue;
                }
            }
            count++;
        }

        //change null values to ' ' character for nicer print
        for (int i = 1; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j] == null) {
                    grid[i][j] = " ";
                } else if (grid[i][j].equals("1")) {
                    grid[i][j] = "" + '\u2693';
                }
            }
        }
        //print grid with each array on its own line
        for (String[] arr : grid) {
            System.out.println(Arrays.toString(arr));
        }
        //print ship positions stored in 2D array
        System.out.println(Arrays.deepToString(ships));
//end of board print/creation

        String[][] shipGuess = new String[3][3];
        String[] displayHit = new String[9];
        int[] shipHit = {0, 0, 0};
        String attempts = "";
        int hit = 0;

        while (hit < 9) {
            //accept user input for coordinate to fire shot at -> A1, B3, C5, G7
            System.out.println("enter position");
            String position = input.nextLine().trim().toUpperCase(); //65 ASCII value for 'A' -> 49 ASCII value for 1

            //convert the input so the first character is the row number and the second character is index number of entered coordinate
            int rowGuess = position.charAt(0) - 64;//store ASCII value for number that corresponds to row number
            int columnGuess = position.charAt(position.length() - 1) - 48;//store ASCII value for number that corresponds to column(index)

            switch (grid[rowGuess][columnGuess]) {
                case "" + '\u2693':
                    System.out.println("Hit");
                    grid[rowGuess][columnGuess] = "2";
                    for (int i = 0; i < ships.length; i++) {
                        for (int j = 0; j < ships[i].length; j++) {
                            if (ships[i][j].equals(position)) {
                                shipGuess[i][j] = position;
                                shipHit[i]++;
                            }
                        }
                    }
                    for (int num : shipHit) {
                        if (num == 3) {
                            System.out.println("sunk ship");
                        }
                    }
                    displayHit[hit] = position;
                    hit++;
                    break;
                case " ":
                    System.out.println("Miss");
                    grid[rowGuess][columnGuess] = "0";
                    break;
                case "2":
                    System.out.println("already hit boat in that position");
                    continue;
                case "0":
                    System.out.println("already guessed that position");
                    continue;
            }
            System.out.println(Arrays.toString(displayHit));
            attempts += position + " ";//store the previous guesses made by user
        }

        System.out.println("Game Over");
    }
}
