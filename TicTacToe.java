import java.util.stream.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;
import javax.sound.midi.SysexMessage;

public class TicTacToe {
    static int winner = -1;
    static char playerArrays[] = new char[] {
        'X',
        'O',
        'A',
        'B',
        'C',
        'D',
        'E',
        'F',
        'G',
        'H',
        'I',
        'J',
        'K',
        'L',
        'M',
        'N',
        'P',
        'Q',
        'R',
        'S',
        'T',
        'U',
        'V',
        'W',
        'Y',
        'Z'
    };
    static int row;
    static int column;
    static int boardsize;
    static int noOfPlayers;
    static int player = -1;
    static int winning_seq;
    int placed = 0;
    static TicTacToe t;
    static int[] remaining_chances;
    static int[][] matrix;
    public boolean fileUpdated = false;
    public String fileNameRetrieved = "";
    public boolean resumed = false;
    public static final int ERROR_ALREADY_OCCUPIED = -2;
    public static final int ITS_A_TIE = -3;
    public static final int ERROR_INPUT_ERROR = -1;

    public static void main(String args[]) {
        int i = 1;
        char ch,choice;
        t = new TicTacToe();
       do{

        System.out.println("Start a new game or resume, R for resume, N for newgame");
        Scanner startNewGameOrResume = new Scanner(System.in);
        choice = startNewGameOrResume.next().charAt(0);
        if (choice == 'R') {
            t.saveResumeFromFileModule();
            remaining_chances = t.remainingChances();
            t.gameStart(noOfPlayers, winner, playerArrays);
        }
        if (choice == 'N') {
            t.gameConstraints();
            remaining_chances = t.remainingChances();
            t.gameStart(noOfPlayers, winner, playerArrays);
        }
        else{
            System.out.println("ENTER A VALID VALUE");
            System.out.println("TRY AGAIN");

        }
    }while(choice!='R'||choice!='N');
    }

    int saveResumeFromFileModule() {
        System.out.println("Enter the filename");
        Scanner filename = new Scanner(System.in);
        t.fileNameRetrieved = filename.next();
        int[][] matrixFromGame = t.resumeAgame(t.fileNameRetrieved);
        if (matrixFromGame != null) {
            matrix = matrixFromGame;
            boardsize = matrixFromGame.length;
            t.resumed = true;
            printBoard();
        }
        return 0;
    }

    void gameConstraints() {
        // saveResumeFromFileModule();
        try {
            System.out.println("Starting a new game");
            System.out.println("Enter size of the board");
            Scanner sizeOfBoard = new Scanner(System.in);
            while (!sizeOfBoard.hasNextInt()) {
                System.out.println("integer, please!");
                sizeOfBoard.nextLine();
            }
            boardsize = sizeOfBoard.nextInt();
            while (boardsize > 999) {
                if (boardsize > 999) {
                    System.out.println("Boardsize is greater than 999, Try again");
                    sizeOfBoard = new Scanner(System.in);
                    boardsize = sizeOfBoard.nextInt();
                }
            }
            matrix = new int[boardsize][boardsize];
            for (int i = 0; i < boardsize; i++) {
                for (int j = 0; j < boardsize; j++) {
                    matrix[i][j] = -1;
                }
            }

            System.out.println("Enter number of Players");
            Scanner NumberOfPlayers = new Scanner(System.in);
            while (!NumberOfPlayers.hasNextInt()) {
                System.out.println("integer, please!");
                NumberOfPlayers.nextLine(); // discard!
            }
            noOfPlayers = NumberOfPlayers.nextInt();
            while (noOfPlayers > 26 || noOfPlayers > boardsize * boardsize) {
                if (noOfPlayers > 26) {
                    System.out.println("Game is limited to 26 members only, Try again");
                    while (!NumberOfPlayers.hasNextInt()) {
                        System.out.println("integer, please!");
                        NumberOfPlayers.nextLine(); // discard!
                    }
                    noOfPlayers = NumberOfPlayers.nextInt();
                }
                if (noOfPlayers > boardsize * boardsize) {
                    System.out.println("Number of players is greater than the boardsize, try again");
                    while (!NumberOfPlayers.hasNextInt()) {
                        System.out.println("integer, please!");
                        NumberOfPlayers.nextLine(); // discard!
                    }
                    noOfPlayers = NumberOfPlayers.nextInt();
                }
            }
            System.out.println("Enter winning sequence");
            Scanner winning_sequence = new Scanner(System.in);
            while (!winning_sequence.hasNextInt()) {
                System.out.println("integer, please!");
                winning_sequence.nextLine(); // discard!
            }
            winning_seq = winning_sequence.nextInt();
            if (winning_seq > boardsize) {
                System.out.println("Winning Sequence is greater than the boardSize ");
                do {
                    System.out.println("TRY AGAIN");
                    System.out.println("Enter winning sequence");
                    winning_sequence = new Scanner(System.in);
                    while (!winning_sequence.hasNextInt()) {
                        System.out.println("integer, please!");
                        winning_sequence.nextLine(); // discard!
                    }
                    winning_seq = winning_sequence.nextInt();
                } while (winning_seq > boardsize);
            }

        } catch (InputMismatchException e) {
            System.out.println("Input is not valid");
        }
    }

    void gameStart(int noOfPlayers, int winner, char playerArrays[]) {
        try {
            int i = 0;
            while (true) {
                // starting from the position which is saved in the file already and retrieving
                // the player from the file

                if (t.resumed && player != -1) {
                    i = player;
                    t.resumed = false;
                } else {
                    i = 0;
                }
                mainloop: for (; i < noOfPlayers; i++) {
                    System.out.println();
                    int[] rowColumnArray = new int[3];
                    player = i;
                    System.out.println("Player " + playerArrays[i]);
                    System.out.println("Enter row and column number or type Q to quit");
                    BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
                    String row_and_column = input.readLine();
                    String[] strings = row_and_column.trim().split("\\s+");
                    if (strings.length == 1) {
                        if (strings[0].length() == 1) {
                            if (strings[0].charAt(0) == 'Q') {
                                int x = t.quitGame();
                                if (x != -1) {
                                    --i;
                                    continue mainloop;
                                }

                            } else {
                                System.out.println("Try again with the inputs");
                                --i;
                                continue mainloop;
                            }
                        } else {
                            System.out.println("Try again with the inputs");
                            --i;
                            continue mainloop;
                        }
                    }
                    if (strings.length == 2) {
                        for (int k = 0; k < strings.length; k++) {
                            rowColumnArray[k] = Integer.parseInt(strings[k]);
                        }
                        if (rowColumnArray.length != 2) {
                            row = (rowColumnArray[0] - 1);
                            column = (rowColumnArray[1] - 1);
                        } else {
                            System.out.println("Try again with better inputs");
                        }
                        if (row < boardsize && column < boardsize) {
                            winner = t.move(row, column, i, winning_seq);
                            --remaining_chances[player];

                            t.predictATie(boardsize, noOfPlayers);


                            if (winner == ERROR_ALREADY_OCCUPIED) {
                                --i;
                                System.out.println("At Wrong place");
                                continue mainloop;
                            }
                            printBoard();
                        } else {
                            System.out.println("TRY AGAIN");
                            --i;
                            continue mainloop;
                        }
                        if (winner != -1) {
                            break;
                        }
                    }
                    if (winner != -1) {
                        break;
                    }
                }
                if (winner == ITS_A_TIE && t.placed == boardsize * boardsize) {
                    System.out.println("It's a tie");
                    System.exit(0);
                }
                if (winner != -1) {
                    break;
                }
            }
            System.out.println("The winner is Player " + playerArrays[i]);
        } catch (IOException | ArrayIndexOutOfBoundsException | InputMismatchException e) {
            e.printStackTrace();
            System.out.println("Wrong Input type");
        }
    }

    /* 
    The function prints the board
     */
    static void printBoard() {
        //  Printing a board greater than 23 automatically distorts the complete board 
        System.out.print(" ");
        for (int i = 1; i <= boardsize; i++) {
            if (i < 10) {
                System.out.print("  " + i + " ");
            }
            if (i >= 10) {
                System.out.print(" " + i + " ");
            }
        }
        System.out.println();
        for (int i = 1; i <= boardsize; i++) {
            if (i < 10) {
                System.out.print(i + " ");
            }
            if (i >= 10 && i <= 99) {
                System.out.print(i + "");
            }
            for (int iq = 1; iq <= boardsize; iq++) {
                if (iq != boardsize) {
                    System.out.print(
                        " " + (matrix[i - 1][iq - 1] == -1 ? "  |" : playerArrays[matrix[i - 1][iq - 1]] + " |"));
                }
                if (iq == boardsize) {
                    System.out.print(" " + (matrix[i - 1][iq - 1] == -1 ? "  " : playerArrays[matrix[i - 1][iq - 1]]));
                }
            }
            System.out.println();
            if (i != boardsize) {
                for (int iw = 1; iw < boardsize; iw++) {
                    if (iw == 1) {
                        System.out.print("  ---+");
                    }
                    if (iw != boardsize - 1) {
                        System.out.print("---+");
                    }
                    if (iw == boardsize - 1) {
                        System.out.print("---");
                    }
                }
            }
            System.out.println();

        }
    }
    /* 
    The function which calls searchNeighbours function and act as an intermediate */
    public int move(int row, int col, int player, int winning_sequence) {
        if (matrix[row][col] != -1) {
            return ERROR_ALREADY_OCCUPIED;
        }
        //placing the value in the matrix
        matrix[row][col] = player;
        t.placed++;
        int result = t.searchNeighbours(row, col, player, winning_sequence);
        if (t.placed >= t.boardsize * t.boardsize && result == -1) {
            return ITS_A_TIE;
        }
        return result;
    }
    /* 
    //to check for win condition, look at each neighbour
    These are the eight direction methods being called recursively and checking if there is a wining position */
    public int searchNeighbours(int row, int col, int player, int winning_sequence) {
        int right_count = checkright(row, col, player, 0);
        int left_count = checkleft(row, col, player, 0);
        int top_count = checktop(row, col, player, 0);
        int down_count = checkdown(row, col, player, 0);
        int top_left = top_left(row, col, player, 0);
        int top_right = top_right(row, col, player, 0);
        int down_left = down_left(row, col, player, 0);
        int down_right = down_right(row, col, player, 0);
        if ((left_count + right_count - 1) == winning_sequence) {
            return player;
        }
        if ((top_count + down_count - 1) == winning_sequence) {
            return player;
        }
        if ((top_right + down_left - 1) == winning_sequence) {
            return player;
        }
        if ((top_left + down_right - 1) == winning_sequence) {
            return player;
        }
        return -1;
    }
    public int checkleft(int row, int column, int player, int count) {
        if (row >= 0 && row < boardsize && column >= 0 && column < boardsize) {
            if (matrix[row][column] == player) {
                count = checkleft(row, --column, player, ++count);
            }
        }
        return count;
    }

    public int checkright(int row, int column, int player, int count) {
        if (row >= 0 && row < boardsize && column >= 0 && column < boardsize) {
            if (matrix[row][column] == player) {
                count = checkright(row, ++column, player, ++count);
            }
        }
        return count;
    }

    public int checktop(int row, int column, int player, int count) {
        if (row >= 0 && row < boardsize && column >= 0 && column < boardsize) {
            if (matrix[row][column] == player) {
                count = checktop(--row, column, player, ++count);
            }
        }
        return count;
    }

    public int checkdown(int row, int column, int player, int count) {
        if (row >= 0 && row < boardsize && column >= 0 && column < boardsize) {
            if (matrix[row][column] == player) {
                count = checkdown(++row, column, player, ++count);
            }
        }
        return count;
    }

    public int top_left(int row, int column, int player, int count) {
        if (row >= 0 && row < boardsize && column >= 0 && column < boardsize) {
            if (matrix[row][column] == player) {
                count = top_left(--row, --column, player, ++count);
            }
        }
        return count;
    }

    public int top_right(int row, int column, int player, int count) {
        if (row >= 0 && row < boardsize && column >= 0 && column < boardsize) {
            if (matrix[row][column] == player) {
                count = top_right(--row, ++column, player, ++count);
            }
        }
        return count;
    }

    public int down_left(int row, int column, int player, int count) {
        if (row >= 0 && row < boardsize && column >= 0 && column < boardsize) {
            if (matrix[row][column] == player) {
                count = down_left(++row, --column, player, ++count);
            }
        }
        return count;
    }

    public int down_right(int row, int column, int player, int count) {
        if (row >= 0 && row < boardsize && column >= 0 && column < boardsize) {
            if (matrix[row][column] == player) {
                count = down_right(++row, ++column, player, ++count);
            }
        }
        return count;
    }

    public static boolean saveInfile(int[][] matrix, String path, String filename) {
        try {
            StringBuilder builder = new StringBuilder();
            builder.append("noOfPlayers:" + noOfPlayers + "\n");
            builder.append("savedByPlayer:" + player + "\n");
            builder.append("boardSize:" + boardsize + "\n");
            builder.append("winning_sequence:" + winning_seq + "\n");
            builder.append("Matrix" + "\n");

            for (int i = 0; i < matrix.length; i++) // for each row
            {
                for (int j = 0; j < matrix.length; j++) // for each column
                {
                    builder.append(matrix[i][j] + ""); // append to the output string
                    if (j < matrix.length - 1) // if this is not the last row element
                        builder.append(","); // then add comma (if you don't like commas you can use spaces)
                }
                builder.append("\n"); // append new line at the end of the row
            }
            BufferedWriter writer = new BufferedWriter(new FileWriter(new File(filename + ".txt")));
            writer.write(builder.toString()); // save the string representation of the board
            writer.close();
            return true;
        } catch (Exception io) {
            io.printStackTrace();
            System.out.println("Input Output Exception caught");
            return false;
        }
    }

    public int quitGame() {
        System.out.println("Do you want to quit the game? Press Q for quit, N for Don't quit and continue");
        Scanner quitOrNot = new Scanner(System.in);
        char ch = quitOrNot.next().charAt(0);
        if (ch == 'Q') {
            System.out.println("Do you want to save it (S for save, N for don't save and just quit, C for cancel and back to previous question)?");
            Scanner saveOrNot = new Scanner(System.in);
            char saveOrnot = saveOrNot.next().charAt(0);
            String filename = "";
            if (saveOrnot == 'S') {
                if (t.fileNameRetrieved != "") {
                    filename = t.fileNameRetrieved;
                } else {
                    System.out.println("Enter filename for the saving the file (WITHOUT EXTENSION, WE WILL TAKE CARE OF THAT :) ");
                    Scanner filenameInput = new Scanner(System.in);
                    filename = filenameInput.next();
                }
                if (saveInfile(matrix, "/", filename)) {
                    System.out.println("Successfully saved");
                    System.exit(0);
                } else {
                    System.out.println("Couldn't save successfully");
                    return player;
                }
            }
            if (saveOrnot == 'N') {
                System.out.println("Now exiting...");
                System.exit(0);
            }
            if (saveOrnot == 'C') {
                t.quitGame();
            }
        }
        if (ch == 'N') {
            return player;
        }
        return player;
    }

   // The following function is used for resuming a game from a filename given
    public int[][] resumeAgame(String filename) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename + ".txt"));
            String line = "";
            int row = 0, column = 0, count = 0;
            //the logic here is to help break the file in lines and then retrieve values one by one
            boolean matrix_starting = false;
            while ((line = reader.readLine()) != null) {
                count++;
                if (count == 1) {
                    String[] noOfPlayer = line.split(":", 3);
                    noOfPlayers = Integer.parseInt(noOfPlayer[1]);
                }
                if (count == 2) {
                    String[] playerChance = line.split(":", 2);
                    player = Integer.parseInt(playerChance[1]);
                }
                if (count == 3) {
                    String[] boardSize = line.split(":", 2);
                    boardsize = Integer.parseInt(boardSize[1]);
                }

                if (count == 4) {
                    String[] winning_sequence = line.split(":", 2);
                    winning_seq = Integer.parseInt(winning_sequence[1]);
                }
                if (line.matches("^Matrix")) {
                    matrix_starting = true;
                    continue;
                }
                if (matrix_starting) {
                    String[] cols = line.split(",");
                    column = cols.length;
                    row++;
                }
            }
            //As soon as all the values are retrieved, the matrix is retrieved too by going again over a file
            int[][] board = new int[row][column];
            BufferedReader readeragain = new BufferedReader(new FileReader(filename + ".txt"));
            line = "";
            row = 0;
            count = 0;
            while ((line = readeragain.readLine()) != null) {
                count++;
                if (count > 5) {
                    String[] cols = line.split(",");
                    int col = 0;
                    for (String c: cols) {
                        board[row][col] = Integer.parseInt(c);
                        col++;
                    }
                    row++;
                }
            }
            reader.close();
            System.out.println("THE RESUMED GAME HAS PLAYER " + playerArrays[player] + "'s turn.");
            System.out.println("WINNING SEQUENCE IS: " + winning_seq);
            System.out.println("NUMBER OF PLAYERS ARE: " + noOfPlayers);

            return board;
        } catch (IOException e) {
            System.out.println("Can't resume, Couldn't find the file");
            return null;
        }
    }

    /* //Predictor Algorithm Of mine starts from here. I am going through all null places in the matrix for each
    player and for each null values Recursively calling the 8 methods to check in each direction whether there
    is a tie or not */
    public int predictATie(int row, int noOfPlayer) {
        boolean resultOfLosers = false;
        for (int k = 0; k < noOfPlayer; k++) {
            for (int i = 0; i < row; i++) {
                for (int j = 0; j < row; j++) {
                    if (matrix[i][j] == -1) {
                        //if anything is positive and means is winning, then resultOfLosers will have a true value and ORing it to new incoming value
                        resultOfLosers |= (t.searchNeighboursForTie(i, j, k, winning_seq) >= 0);
                        //as soon as somebody wins I break the loop and doesn't run the algo anymore for that phase
                        if (resultOfLosers) {
                            break;
                        }
                    }
                }
                if (resultOfLosers) {
                    break;
                }

            }
            if (resultOfLosers) {
                break;
            }
        }
        if (!resultOfLosers) {
            System.out.println("PREDICTION of our ALGORITHM: It's gonna be a Tie");
        }
        return 0;
    }
/* 
    //This function solves the remaining chances problem and assigns the remaining chances to an array by 
    calculating the simple mathematics and thus assigning the chances which each player has */
    public int[] remainingChances() {
        int noOfPlaces, quotient, remainder, i;
        int[] remaining_chance = new int[noOfPlayers];
        if (boardsize != 0 && boardsize < 999) {
            noOfPlaces = boardsize * boardsize;
            quotient = noOfPlaces / noOfPlayers;
            remainder = noOfPlaces % noOfPlayers;
            for (i = 0; i < remainder; i++) {
                remaining_chance[i] = quotient + 1;
            }
            for (int j = i; j < noOfPlayers; j++) {
                remaining_chance[j] = quotient;
            }

        }
        return remaining_chance;
    }
/* 
    The mega function which runs through eight direction calling recursively for prediction algorithm */
    public int searchNeighboursForTie(int row, int col, int player_for_tie, int winning_seq) {

        int[] left_count_and_remaining_chance_array = checkleft(row, col, player_for_tie, 0, remaining_chances[player_for_tie]);
        int left_count = left_count_and_remaining_chance_array[0];
        int left_count_remaining = left_count_and_remaining_chance_array[1];

        int[] right_count_and_remaining_chance_array = checkright(row, col, player_for_tie, 0, left_count_remaining);
        int right_count = right_count_and_remaining_chance_array[0];
        int right_count_remaining = right_count_and_remaining_chance_array[1];

        if ((left_count + right_count - 1) >= winning_seq) {
            return player_for_tie;
        }

        int[] top_count_and_remaining_chance_array = checktop(row, col, player_for_tie, 0, remaining_chances[player_for_tie]);
        int top_count = top_count_and_remaining_chance_array[0];
        int top_count_remaining = top_count_and_remaining_chance_array[1];

        int[] bottom_count_and_remaining_chance_array = checkbottom(row, col, player_for_tie, 0, top_count_remaining);
        int bottom_count = bottom_count_and_remaining_chance_array[0];
        int bottom_count_remaining = bottom_count_and_remaining_chance_array[1];

        if ((top_count + bottom_count - 1) >= winning_seq) {
            return player_for_tie;
        }

        int[] top_left_count_and_remaining_chance_array = checkTopLeft(row, col, player_for_tie, 0, remaining_chances[player_for_tie]);
        int top_left_count = top_left_count_and_remaining_chance_array[0];
        int top_left_count_remaining = top_left_count_and_remaining_chance_array[1];

        int[] bottom_right_count_and_remaining_chance_array = checkRightBottom(row, col, player_for_tie, 0, top_left_count_remaining);
        int bottom_right_count = bottom_right_count_and_remaining_chance_array[0];
        int bottom_right_count_remaining = bottom_right_count_and_remaining_chance_array[1];

        if ((top_left_count + bottom_right_count - 1) >= winning_seq) {
            return player_for_tie;
        }

        int[] top_right_count_and_remaining_chance_array = checkTopRight(row, col, player_for_tie, 0, remaining_chances[player_for_tie]);
        int top_right_count = top_right_count_and_remaining_chance_array[0];
        int top_right_count_remaining = top_right_count_and_remaining_chance_array[1];

        int[] bottom_left_count_and_remaining_chance_array = checkBottomLeft(row, col, player_for_tie, 0, top_right_count_remaining);
        int bottom_left_count = bottom_left_count_and_remaining_chance_array[0];
        int bottom_left_count_remaining = bottom_left_count_and_remaining_chance_array[1];

        if ((top_right_count + bottom_left_count - 1) >= winning_seq) {
            return player_for_tie;
        }
        return -1;
    }
/* 
    Herecomes the eight master functions which check the eight directions for any possibilities */
    public int[] checkleft(int row, int column, int player, int count, int remaining_chance) {
        int[] count_remaining_chance = new int[2];
        // int row = row;
        // int column=column;
        if (row >= 0 && row < boardsize && column >= 0 && column < boardsize) {

            if (matrix[row][column] == -1 && remaining_chance >= 0) {
                return checkleft(row, --column, player, ++count, --remaining_chance);
            } else if (matrix[row][column] == player) {
                return checkleft(row, --column, player, ++count, remaining_chance);
            }

        }
        count_remaining_chance[0] = count;
        count_remaining_chance[1] = remaining_chance;
        return count_remaining_chance;
    }
    public int[] checkright(int row, int column, int player, int count, int remaining_chance) {
        int[] count_remaining_chance = new int[2];
        if (row >= 0 && row < boardsize && column >= 0 && column < boardsize) {

            if (matrix[row][column] == -1 && remaining_chance >= 0) {
                return checkright(row, ++column, player, ++count, --remaining_chance);
            } else if (matrix[row][column] == player) {
                return checkright(row, ++column, player, ++count, remaining_chance);
            }

        }
        count_remaining_chance[0] = count;
        count_remaining_chance[1] = remaining_chance;
        return count_remaining_chance;
    }
    public int[] checktop(int row, int column, int player, int count, int remaining_chance) {
        int[] count_remaining_chance = new int[2];
        // int row = row;
        // int column=column;
        if (row >= 0 && row < boardsize && column >= 0 && column < boardsize) {

            if (matrix[row][column] == -1 && remaining_chance >= 0) {
                return checktop(--row, column, player, ++count, --remaining_chance);
            } else if (matrix[row][column] == player) {
                return checktop(--row, column, player, ++count, remaining_chance);
            }
        }

        count_remaining_chance[0] = count;
        count_remaining_chance[1] = remaining_chance;
        return count_remaining_chance;
    }
    public int[] checkbottom(int row, int column, int player, int count, int remaining_chance) {
        int[] count_remaining_chance = new int[2];
        // int row = row;
        // int column=column;
        if (row >= 0 && row < boardsize && column >= 0 && column < boardsize) {

            if (matrix[row][column] == -1 && remaining_chance >= 0) {
                return checkbottom(++row, column, player, ++count, --remaining_chance);
            } else if (matrix[row][column] == player) {
                return checkbottom(++row, column, player, ++count, remaining_chance);
            }

        }
        count_remaining_chance[0] = count;
        count_remaining_chance[1] = remaining_chance;
        return count_remaining_chance;
    }
    public int[] checkTopLeft(int row, int column, int player, int count, int remaining_chance) {
        int[] count_remaining_chance = new int[2];
        // int row = row;
        // int column=column;
        if (row >= 0 && row < boardsize && column >= 0 && column < boardsize) {

            if (matrix[row][column] == -1 && remaining_chance >= 0) {
                return checkTopLeft(--row, --column, player, ++count, --remaining_chance);
            } else if (matrix[row][column] == player) {
                return checkTopLeft(--row, --column, player, ++count, remaining_chance);
            }

        }
        count_remaining_chance[0] = count;
        count_remaining_chance[1] = remaining_chance;
        return count_remaining_chance;
    }
    public int[] checkRightBottom(int row, int column, int player, int count, int remaining_chance) {
        int[] count_remaining_chance = new int[2];
        // int row = row;
        // int column=column;
        if (row >= 0 && row < boardsize && column >= 0 && column < boardsize) {
            if (matrix[row][column] == -1 && remaining_chance >= 0) {
                return checkRightBottom(++row, ++column, player, ++count, --remaining_chance);
            } else if (matrix[row][column] == player) {
                return checkRightBottom(++row, ++column, player, ++count, remaining_chance);
            }

        }
        count_remaining_chance[0] = count;
        count_remaining_chance[1] = remaining_chance;
        return count_remaining_chance;
    }
    public int[] checkTopRight(int row, int column, int player, int count, int remaining_chance) {
        int[] count_remaining_chance = new int[2];
        // int row = row;
        // int column=column;
        if (row >= 0 && row < boardsize && column >= 0 && column < boardsize) {

            if (matrix[row][column] == -1 && remaining_chance >= 0) {
                return checkTopRight(--row, ++column, player, ++count, --remaining_chance);
            } else if (matrix[row][column] == player) {
                return checkTopRight(--row, ++column, player, ++count, remaining_chance);
            }

        }
        count_remaining_chance[0] = count;
        count_remaining_chance[1] = remaining_chance;
        return count_remaining_chance;
    }

    public int[] checkBottomLeft(int row, int column, int player, int count, int remaining_chance) {
        int[] count_remaining_chance = new int[2];
        // int row = row;
        // int column=column;
        if (row >= 0 && row < boardsize && column >= 0 && column < boardsize) {

            if (matrix[row][column] == -1 && remaining_chance >= 0) {
                return checkBottomLeft(++row, --column, player, ++count, --remaining_chance);
            } else if (matrix[row][column] == player) {
                return checkBottomLeft(++row, --column, player, ++count, remaining_chance);
            }

        }
        count_remaining_chance[0] = count;
        count_remaining_chance[1] = remaining_chance;
        return count_remaining_chance;
    }



}