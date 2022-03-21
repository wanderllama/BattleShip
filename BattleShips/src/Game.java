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
        //following commented out code prints game board with ships locations -> for debugging
        /*for (String[] arr : grid) {
            System.out.println(Arrays.toString(arr));
        }*/
        //print ship positions stored in 2D array
        System.out.println(Arrays.deepToString(ships));
//end of board print/creation

        //game board for user to see hits and misses -> not ship location
        String[][] board = new String[8][8];

        //add key for display board
        for (int i = 0; i < board[0].length; i++) {
            board[0][i] = i + "";
        }
        //add capital letter to first index of each row of board starting with 'A'
        char letterBoard = 'A';
        for (int i = 1; i < board.length; i++) {
            board[i][0] = "" + letterBoard++;
        }
        //change null space to sine wave unicode character
        for (int i = 1; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] == null) {
                    board[i][j] = "\u223f";
                }
            }
        }//end of display board setup

        //shipGuess array will hold the corresponding coordinates for each ship
        //starts with all null values and when hit occurs then add coordinates to array
        String[][] shipGuess = new String[3][3];//todo determine if i need this data kept track of if not delete

        //displayHit array starts with null values and used to display correct guesses
        //when hit occurs coordinates are added to first available index
        String[] displayHit = new String[9];

        //shipHit array stores the value for the number of times each ship has been sunk
        //when element = 3 then display "ship sunk"
        int[] shipHit = {0, 0, 0};

        //String of previously made guesses that are valid
        //user can ask for this string to be printed
        String attempts = "";

        //hit value is the number of successful hits
        //game ends when hit = 9
        int hit = 0;

        while (hit < 9) {
            //accept user input for coordinate to fire shot at -> A1, B3, C5, G7
            System.out.println("enter position\nenter 'S' for previous attempted coordinates");
            String position = input.nextLine().trim().toUpperCase(); //65 ASCII value for 'A' -> 49 ASCII value for 1

            if (position.equals("S")) {
                System.out.println(attempts);
            } else if (position.charAt(0) >= 65 && position.charAt(0) <= 71 && position.charAt(1) >= 49 && position.charAt(1) <= 55) {
                //convert the input so the first character is the row number and the second character is index number of entered coordinate
                int rowGuess = position.charAt(0) - 64;//store ASCII value for number that corresponds to row number
                int columnGuess = position.charAt(position.length() - 1) - 48;//store ASCII value for number that corresponds to column(index)

                switch (grid[rowGuess][columnGuess]) {
                    case "" + '\u2693'://coordinates of game board has anchor unicode character as element
                        System.out.println("Hit");
                        grid[rowGuess][columnGuess] = "2";//change element value to 2
                        //determine position of master list of existing ship coordinates and add hit coordinate to shipGuess array
                        for (int i = 0; i < ships.length; i++) {
                            for (int j = 0; j < ships[i].length; j++) {
                                if (ships[i][j].equals(position)) {
                                    shipGuess[i][j] = position;//add to shipGuess array
                                    shipHit[i]++;//increase hit counter to correct ship in shipHit array -> each element represents the number of times a hit has occurred on each ship
                                }
                            }
                        }
                        //check each element in shipHit array
                        for (int num : shipHit) {
                            if (num == 3) {//if element = 3 then print sunk ship
                                System.out.println("sunk ship");
                            }
                        }
                        //add coordinates to leftmost element in displayHit array so user can see what shot have hit ships
                        displayHit[hit] = position;
                        //hit counter increments
                        hit++;
                        //add skull and crossbones unicode character to display board for users hit
                        board[rowGuess][columnGuess] = "\u2620";
                        break;
                    case " ":
                        System.out.println("Miss");
                        grid[rowGuess][columnGuess] = "0";
                        //add miss character to display board
                        board[rowGuess][columnGuess] = "\u2327";
                        break;
                    case "2":
                        System.out.println("already hit boat in that position");
                        continue;
                    case "0":
                        System.out.println("already guessed that position");
                        continue;
                }
            } else {
                System.out.println("invalid input");
                continue;
            }
            //print board after each guess may move this to only occur when user request and end of game-> todo create method for this to print at end of game and when user enters 'X'
            for (String[] arr : board) {
                System.out.println(Arrays.toString(arr));
            }
            System.out.println(Arrays.toString(displayHit));
            attempts += position + " ";//store the previous guesses made by user
        }
        System.out.println("Game Over");
    }
}
