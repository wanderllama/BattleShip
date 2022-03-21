import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

public class Grid {

    public static void main(String[] args) {

        //create 8x8 grid
        String[][]grid = new String[8][8];

        //first array in grid is [0, 1, 2, 3, 4, 5, 6, 7]
        for (int i = 0; i < grid[0].length; i++) {
            grid[0][i] = i + "";
        }

        //add capital letter to first index of each grid starting with 'A'
        char letter = 'A';
        for (int i = 1; i < grid.length; i++) {
            grid[i][0] = "" + letter++;
        }

        //print grid with each array on its own line
        for (String[] arr : grid) {
            System.out.println(Arrays.toString(arr));
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
    }
}
