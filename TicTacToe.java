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
			System.out.println("Player "+playerArrays[i]);
			System.out.println("Enter row number");
			userInput = new Scanner(System.in);
			row   = userInput.nextInt();
			System.out.println("Enter column number");
			userInputcolumn = new Scanner(System.in);
			column = userInputcolumn.nextInt();
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
		int front_row_count= 0,back_row_count=0,front_column_count=0,back_column_count=0,front_forward_diagonal_count=0,back_forward_diagonal_count=0,front_backward_diagonal_cout=0,back_backward_diagonal_count=0;
		if(matrix[row][col]==-1){
			System.out.println("Wrong place");
			return -1;
		}		 
		   //check row traversing forward
		   boolean win=true;
		   for(int i=0; i<matrix.length; i++){
			//loop should traverse when going over the row by checking that it's not empty or is not equal to desired player   
			if(matrix[row][i]!=player&&matrix[row][i]!=-1){
					win=false;
					front_row_count=0;
				System.out.println("Everthong is not fine"+front_row_count);
					break;
			   }
			   if(matrix[row][i]==player){
				System.out.println("Everything is fine"+front_row_count);
				front_row_count++;
			   }   
			   if(win&&front_row_count==winning_sequence) return player;
		   }
		   //row traversing backward
		   win=true;
		   for(int i=matrix.length-1; i>=0; i--){
			   if(matrix[row][i]!=player&&matrix[row][i]!=-1){
					win=false;
					back_row_count=0;
				System.out.println("Everthong is not fine"+back_row_count);
					break;
			   }
			   if(matrix[row][i]==player){
				System.out.println("Everything is fine"+back_row_count);
				back_row_count++;
			   }   
			   if(win&&back_row_count==winning_sequence) return player;
		   }		
		   //checking column traversing forward
		   win=true;
		   for(int i=0; i<matrix.length; i++){
			   if(matrix[i][col]!=player&&matrix[i][col]!=-1){
				   win=false;
				   front_column_count=0;
				   break;
			   }
			    if(matrix[i][col]==player){
					front_column_count++;
			}
			if(win&&front_column_count==winning_sequence) return player;
		   }

		   //checking column by traversing backward
		   win=true;
		   for(int i=matrix.length-1; i>=0; i--){
			   if(matrix[i][col]!=player&&matrix[i][col]!=-1){
				   win=false;
				   back_column_count=0;
				   break;
			   }
			    if(matrix[i][col]==player){
					back_column_count++;
			}
			if(win&&back_column_count==winning_sequence) return player;
		   }

		   //checking diagonal while traversing forward
		   win=true;
		   for(int i=0; i<matrix.length; i++){
			   if(matrix[i][i]!=player&&matrix[i][i]!=-1){
				win=false;
				front_forward_diagonal_count=0;
				   break;
			   } 
			   if(matrix[i][i]==player){
				front_forward_diagonal_count++;
			}
			if(win&&front_forward_diagonal_count==winning_sequence) return player;
		}

		// checking diagonal while traversing backward
		win=true;
		for(int i=matrix.length-1; i>=0; i--){
			if(matrix[i][i]!=player&&matrix[i][i]!=-1){
			 win=false;
			 back_forward_diagonal_count=0;
				break;
			} 
			if(matrix[i][i]==player){
				back_forward_diagonal_count++;
		 }
		 if(win&&back_forward_diagonal_count==winning_sequence) return player;
	 }
	 
		
	//  win =true;
	// int row_for_traversing = row;
	//  for(int i=row;i<matrix.length;i++){
	// 	 for(int j=col;j<matrix.length;j++){
	// 		if(matrix[i][j]!=player){
	// 			win=false;
	// 			count=0;
	// 			break;
	// 		} 
	// 		if(matrix[i][j]==player){
	// 			 count++;
	// 		 }
	// 			break;
	// 	 }
	// 	 if(!win){
	// 		 break;
	// 	 }
	// 	 if(win&&count==winning_sequence) return player;
	//  }
	 //check backward diagonal while traversing forward
		   win=true;
		   for(int i=0; i<matrix.length; i++){
			   if(matrix[i][matrix.length-i-1]!=player&&matrix[i][matrix.length-i-1]!=-1){
				win=false;
				front_backward_diagonal_cout=0;
				   break;
			   } 
			   if(matrix[i][matrix.length-i-1]==player){
				front_backward_diagonal_cout++;
			}
			if(win&&front_backward_diagonal_cout==winning_sequence) return player;
		}	
//checking backward diagonal by traversing backward
		win=true;
		for(int i=matrix.length-1; i>=0; i--){
			if(matrix[i][matrix.length-i-1]!=player&&matrix[i][matrix.length-i-1]!=-1){
			 win=false;
			 back_backward_diagonal_count=0;
				break;
			} 
			if(matrix[i][matrix.length-i-1]==player){
				back_backward_diagonal_count++;
		 }
		 if(win&&back_backward_diagonal_count==winning_sequence) return player;
	 }	
		   return -1;
   }
}