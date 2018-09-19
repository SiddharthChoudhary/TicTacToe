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
	static int winner;
	static char playerArrays[] = new char[]{'X','O','A','B','C','D','E','F','G','H','I','J','K','L','M','N','P','Q','R','S','T','U','V','W','Y','Z'};
	static int row;
	static int column;
    static int boardsize;
    static int noOfPlayers;
    static int winning_seq;
    static TicTacToe t;
	static int[][] matrix;
	public static void main(String args[]){
        int i=1;	
		try{
        System.out.println("Start a new game or resume, r for resume, n for newgame");
        Scanner startNewGameOrResume = new Scanner(System.in);
        char choice = startNewGameOrResume.next().charAt(0);
        if(choice=='r'){
        System.out.println("Enter the filename");
        Scanner filename = new Scanner(System.in);
        int[][] matrixFromGame = resumeAgame(filename.next());
        if(matrixFromGame!=null){
            matrix=matrixFromGame; 
            boardsize=matrixFromGame.length;
            printBoard();
        }
        }
        System.out.println("Starting a new game"); 
		System.out.println("Enter size of the board");
		Scanner sizeOfBoard = new Scanner(System.in);
		boardsize     = sizeOfBoard.nextInt();
        while(boardsize>999){
			if(boardsize>999){
		System.out.println("Boardsize is greater than 999, Try again");			
		sizeOfBoard = new Scanner(System.in);
		boardsize     = sizeOfBoard.nextInt();		
			}
		}
		t = new TicTacToe(boardsize);
		System.out.println("Enter number of Players");
		Scanner NumberOfPlayers = new Scanner(System.in);
		int noOfPlayers 		= NumberOfPlayers.nextInt();	 
		while(noOfPlayers>26){
			if(noOfPlayers>26){
			System.out.println("Game is limited to 26 members only, Try again");
			NumberOfPlayers =  new Scanner(System.in);
			noOfPlayers = NumberOfPlayers.nextInt();	
		}
	}
		System.out.println("Enter winning sequence");
		Scanner winning_sequence = new Scanner(System.in);
		int winning_seq 		= winning_sequence.nextInt();	 
        while(true){
		for(i=0;i<noOfPlayers;i++){
            System.out.println();
            quitGame();
            int[] rowColumnArray = new int[3];
            
            System.out.println("Player "+playerArrays[i]);
            System.out.println("Enter row and column number");
            BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
            String  row_and_column = input.readLine();        
            String[] strings = row_and_column.trim().split("\\s+");
            for (int k = 0; k < strings.length; k++) {
            rowColumnArray[k] = Integer.parseInt(strings[k]);
            }
            if(rowColumnArray.length!=2){
                row   = (rowColumnArray[0]-1);
                column= (rowColumnArray[1]-1);
            }else{
                System.out.println("Try again with better inputs");
            }
            if(row<boardsize&&column<boardsize){
				winner = t.move(row,column,i,winning_seq);
				printBoard();
			}else{
				System.out.println("TRY AGAIN");
				--i;	
				}
				if(winner!=-1){
					break;
				}
			}		
		
			if(winner!=-1){				
			break;
			}
	}

		System.out.println("The winner is Player "+playerArrays[i]);		
		}catch(IOException|ArrayIndexOutOfBoundsException|InputMismatchException e){
            e.printStackTrace();
            System.out.println("Wrong Input type");

		}
		}
	static void printBoard() {
		for(int i=1;i<=boardsize;i++){
		System.out.print("  "+i+" | ");
		}
		System.out.println();
		for	(int i=1;i<=boardsize;i++){
		System.out.print(" "+i+"  ");
		for(int iq=1;iq<=boardsize;iq++){
		System.out.print("  "+(matrix[i-1][iq-1] == -1?" |":playerArrays[matrix[i-1][iq-1]]+" |"));
		}
		System.out.println();
		for(int iw=1;iw<=boardsize;iw++){
		if(iw!=boardsize){
			System.out.print(" _ _ +");
		}else{
			System.out.print(" _ _ ");
		}}
		System.out.println();
		
	}
}
	   /** Initialize your data structure here. */
	   public TicTacToe(int n) {
		   matrix = new int[n][n];
		for(int i=0;i<n;i++){
			for(int j=0;j<n;j++){
				matrix[i][j]=-1;
			}
		}
        }

	   public int move(int row, int col, int player,int winning_sequence) {	
        matrix[row][col]=player;
        int right_count = checkright(row,col,player,0);
        int left_count  = checkleft(row,col,player,0);
        int top_count   = checktop(row,col,player,0);
        int down_count= checkdown(row,col,player,0);
        int top_left = top_left(row,col,player,0);
        int top_right = top_right(row,col,player,0);
        int down_left= down_left(row, col, player, 0);
        int down_right=down_right(row, col, player, 0);
        if((left_count+right_count-1)==winning_sequence){
            return player;
        }
        if((top_count+down_count-1)==winning_sequence){
               return player;
        }
        if((top_right+down_left-1)==winning_sequence){
               return player;
        }
        if((top_left+down_right-1)==winning_sequence){
               return player;
        }
        return -1;
    }
    public int checkleft(int row, int column,int player,int count){
            if(row>=0&&row<boardsize&&column>=0&&column<boardsize){
            if(matrix[row][column]==player){
                count = checkleft(row,--column,player,++count);
            }
        }
            return count;
        }    
        public int checkright(int row, int column,int player,int count){
            if(row>=0&&row<boardsize&&column>=0&&column<boardsize){
                if(matrix[row][column]==player){
                    count = checkright(row,++column,player,++count);
                }
            }
                return count;        
        }
        public int checktop(int row, int column,int player,int count){
            if(row>=0&&row<boardsize&&column>=0&&column<boardsize){
                if(matrix[row][column]==player){
                    count = checktop(--row,column,player,++count);
                }
            }
                return count;
        }
        public int checkdown(int row, int column,int player,int count){
            if(row>=0&&row<boardsize&&column>=0&&column<boardsize){
                if(matrix[row][column]==player){
                    count = checkdown(++row,column,player,++count);
                }
            }
                return count;
        }
        public int top_left(int row, int column,int player,int count){
            if(row>=0&&row<boardsize&&column>=0&&column<boardsize){
                if(matrix[row][column]==player){
                    count = top_left(--row,--column,player,++count);
                }
            }
                return count;
        }
        public int top_right(int row, int column,int player,int count){
            if(row>=0&&row<boardsize&&column>=0&&column<boardsize){
                if(matrix[row][column]==player){
                    count = top_right(--row,++column,player,++count);
                }
            }
                return count;
        }
        public int down_left(int row, int column,int player,int count){
            if(row>=0&&row<boardsize&&column>=0&&column<boardsize){
                if(matrix[row][column]==player){
                    count = down_left(++row,--column,player,++count);
                }
            }
                return count;
        }
        public int down_right(int row, int column,int player,int count){
            if(row>=0&&row<boardsize&&column>=0&&column<boardsize){
                if(matrix[row][column]==player){
                    count = down_right(++row,++column,player,++count);
                }
            }
                return count;
            }
        public static boolean saveInfile(int[][] matrix,String path, String filename){
           try{
            StringBuilder builder = new StringBuilder();
            for(int i = 0; i < matrix.length; i++)//for each row
            {
               for(int j = 0; j < matrix.length; j++)//for each column
               {
                  builder.append(matrix[i][j]+"");//append to the output string
                  if(j < matrix.length - 1)//if this is not the last row element
                     builder.append(",");//then add comma (if you don't like commas you can use spaces)
               }
               builder.append("\n");//append new line at the end of the row
            }
            BufferedWriter writer = new BufferedWriter(new FileWriter(new File(filename + ".txt")));
            writer.write(builder.toString());//save the string representation of the board
            writer.close();
            return true;
        }catch(Exception io){
            io.printStackTrace();
            System.out.println("Input Output Exception caught");
            return false;
        }
        }
        public static int quitGame(){
            System.out.println("Do you want to quit the game? Press q for quit, n for Don't quit and continue");
            Scanner quitOrNot = new Scanner(System.in);
            char ch = quitOrNot.next().charAt(0);
            if(ch=='q'){
                System.out.println("Do you want to save it (s for save, n for don't save and just quit, c for cancel and back to previous question)?"); 
                Scanner saveOrNot = new Scanner(System.in);
                char saveOrnot = saveOrNot.next().charAt(0);
                if(saveOrnot=='s'){
                    System.out.println("Enter filename for the saving the file");
                    Scanner filenameInput = new Scanner(System.in);
                    String filename    =   filenameInput.next();
                    if(saveInfile(matrix,"/",filename)){
                        System.out.println("Successfully saved");
                        System.exit(0);
                    }else{
                        System.out.println("Couldn't save successfully");
                        return -1;
                    }
                }
                if(saveOrnot=='n'){
                    System.out.println("Now exiting...");    
                    System.exit(0);
                }
                if(saveOrnot=='c'){
                    quitGame();
                }
            }
            if(ch=='n'){
               return -1;
            }
            return -1;
        }
        public static int[][] resumeAgame(String filename){
        try{
            BufferedReader reader = new BufferedReader(new FileReader(filename+".txt"));
            String line = "";
            int row = 0,column=0;
            while((line = reader.readLine()) != null)
            {   
               String[] cols = line.split(","); //note that if you have used space as separator you have to split on " "
               column = cols.length;
               row++;
            }
            int[][] board = new int[row][column];            
            BufferedReader readeragain = new BufferedReader(new FileReader(filename+".txt"));
            line = "";
            row=0;   
            while((line = readeragain.readLine()) != null)
                {               
                    String[] cols = line.split(","); //note that if you have used space as separator you have to split on " "
                    int col = 0;
                    for(String  c : cols)
                    {
                    board[row][col] = Integer.parseInt(c);
                    col++;
                    }
                    row++;
                }
                    reader.close();
                    return board;
        }catch(IOException e){
            System.out.println("Can't resume, Couldn't find the file");
            return null;
        }
    }
}