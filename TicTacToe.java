import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.InputMismatchException;
import java.util.Scanner;



public class TicTacToe {
	static int winner;
	static char playerArrays[] = new char[]{'X','O','A','B','C','D','E','F','G','H','I','J','K','L','M','N','P','Q','R','S','T','U','V','W','Y','Z'};
	static int row;
	static int column;
	static int boardsize;
	static int[][] matrix;
	public static void main(String args[]){
		int i=1;	
		try{
		Scanner userInput = new Scanner(System.in);
		Scanner userInputcolumn = new Scanner(System.in);
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
		
			 
		TicTacToe t = new TicTacToe(boardsize	);
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
            int[] rowColumnArray = new int[3];
			System.out.println("Player "+playerArrays[i]);
            try{
            System.out.println("Enter row and column number");
            BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
            String  row_and_column = input.readLine();        
            String[] strings = row_and_column.trim().split("\\s+");
            for (int k = 0; k < strings.length; k++) {
            rowColumnArray[k] = Integer.parseInt(strings[i]);
            }
            row   = (rowColumnArray[0]-1);
            column= (rowColumnArray[1]-1);
            System.out.println("Row and COlum"+rowColumnArray[0]+""+rowColumnArray[1]);
        }catch(IOException e){
            System.out.println("Try again, you have input something wrong!!");
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
		userInput.close();
		userInputcolumn.close();		
		}catch(InputMismatchException e){
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
	
	   /** Player {player} makes a move at ({row}, {col}).
		   @param row The row of the board.
		   @param col The column of the board.
		   @param player The player, can be either 1 or 2.
		   @return The current winning condition, can be either:
				   0: No one wins.
				   1: Player 1 wins.
				   2: Player 2 wins. */
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
        System.out.println("COunt is"+left_count);
        if((left_count+right_count-1)==winning_sequence){
         System.out.println("COunt is"+(left_count+right_count));
            return player;
        }
        if((top_count+down_count-1)==winning_sequence){
            System.out.println("COunt is"+(top_count+down_count));
               return player;
        }
        if((top_right+down_left-1)==winning_sequence){
            System.out.println("COunt is"+(top_right+down_left));
               return player;
        }
        if((top_left+down_right-1)==winning_sequence){
            System.out.println("COunt is"+(top_left+down_right));
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
}