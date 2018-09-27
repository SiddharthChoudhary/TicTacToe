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
	static int winner=-1;
	static char playerArrays[] = new char[]{'X','O','A','B','C','D','E','F','G','H','I','J','K','L','M','N','P','Q','R','S','T','U','V','W','Y','Z'};
	static int row;
	static int column;
    static int boardsize;
    static int noOfPlayers;
    static int player=-1;
    static int winning_seq;
    static TicTacToe t;
    static int[][] matrix;
    public boolean resumed=false;
    public static final int ERROR_ALREADY_OCCUPIED=-2;
    public static final int ERROR_INPUT_ERROR          =-1;
	public static void main(String args[]){
        int i=1;	
        char ch;
        t=new TicTacToe();
        System.out.println("Start a new game or resume, r for resume, n for newgame");
        Scanner startNewGameOrResume = new Scanner(System.in);
        char choice = startNewGameOrResume.next().charAt(0);
        if(choice=='r'){
          t.saveResumeFromFileModule();
          t.gameStart(noOfPlayers,winner,playerArrays);
        }
        if(choice=='n'){
            t.gameConstraints();
            t.gameStart(noOfPlayers,winner,playerArrays);
        }
        
    }
        int saveResumeFromFileModule(){
            System.out.println("Enter the filename");
            Scanner filename = new Scanner(System.in);
            int[][] matrixFromGame = t.resumeAgame(filename.next());
            if(matrixFromGame!=null){
                matrix=matrixFromGame; 
                boardsize=matrixFromGame.length;
                t.resumed=true;
                printBoard();
            }
            return 0;
        }
        void gameConstraints(){
            //saveResumeFromFileModule();
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
            matrix = new int[boardsize][boardsize];
            for(int i=0;i<boardsize;i++){
                for(int j=0;j<boardsize;j++){
                    matrix[i][j]=-1;
                }
            }
        
            System.out.println("Enter number of Players");
            Scanner NumberOfPlayers = new Scanner(System.in);
            noOfPlayers 		= NumberOfPlayers.nextInt();	 
            while(noOfPlayers>26){
                if(noOfPlayers>26){
                System.out.println("Game is limited to 26 members only, Try again");
                NumberOfPlayers =  new Scanner(System.in);
                noOfPlayers = NumberOfPlayers.nextInt();	
            }
        }
            System.out.println("Enter winning sequence");
            Scanner winning_sequence = new Scanner(System.in);
            winning_seq 		= winning_sequence.nextInt(); 
        }

        void gameStart(int noOfPlayers,int winner,char playerArrays[]){
         try{
             int i=0; 
            while(true){
                 //starting from the position which is saved in the file already and retrieving the player from the file
                
                if(t.resumed&&player!=-1){
                    i = player;
                    t.resumed=false;
                }else{
                    i=0;
                }
                mainloop:
                 for(;i<noOfPlayers;i++){
                    System.out.println();
                    int[] rowColumnArray = new int[3];
                    player = i; 
                    System.out.println("Player "+playerArrays[i]);
                    System.out.println("Enter row and column number");
                    BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
                    String  row_and_column = input.readLine();        
                    String[] strings = row_and_column.trim().split("\\s+");
                    if(strings.length==1){
                        if(strings[0].length()==1){
                            if(strings[0].charAt(0)=='q'){
                                int x= t.quitGame();
                                if(x!=-1){
                                    t.resumed=true;
                                    continue mainloop;
                                };
                            }
                        }
                            else{
                                System.out.println("Try again with the inputs");
                                --i;
                                continue mainloop;
                            }
                    }
                    if(strings.length==2){
                    for (int k = 0; k < strings.length; k++) {
                    rowColumnArray[k] = Integer.parseInt(strings[k]);
                    }
                    if(rowColumnArray.length!=2){
                        row   = (rowColumnArray[0]-1);
                        column= (rowColumnArray[1]-1);
                    }
                    else{
                        System.out.println("Try again with better inputs");
                    }
                
                    if(row<boardsize&&column<boardsize){
                        winner = t.move(row,column,i,winning_seq);
                        if(winner==ERROR_ALREADY_OCCUPIED){
                        --i;
                        System.out.println("At Wrong place");
                        continue mainloop;    
                        }
                        printBoard();
                    }else{
                        System.out.println("TRY AGAIN");
                        --i;
                        continue mainloop;	
                        }
                        if(winner!=-1){
                            break;
                        }
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
        //print a space
        System.out.print(" ");
		for(int i=1;i<=boardsize;i++){
		System.out.print("  "+i+" ");
		}
		System.out.println();
		for	(int i=1;i<=boardsize;i++){
		System.out.print(i+" ");
		for(int iq=1;iq<=boardsize;iq++){
        if(iq!=boardsize){
            System.out.print(" "+(matrix[i-1][iq-1] == -1?"  |":playerArrays[matrix[i-1][iq-1]]+" |"));
        }
        if(iq==boardsize){
            System.out.print(" "+(matrix[i-1][iq-1] == -1?"  ":playerArrays[matrix[i-1][iq-1]]));
        }
    }
    	System.out.println();
        if(i!=boardsize){
        for(int iw=1;iw<boardsize;iw++){
        if(iw==1){
            System.out.print("  ---+");
        }
        if(iw!=boardsize-1){
			System.out.print("---+");
        }
        if(iw==boardsize-1){
			System.out.print("---");
        }
    }
    }
		System.out.println();
		
	}
}

	   public int move(int row, int col, int player,int winning_sequence) {	
        if(matrix[row][col]!=-1){
            return ERROR_ALREADY_OCCUPIED;
        }
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
            builder.append("noOfPlayers:"+noOfPlayers+"\n");
            builder.append("savedByPlayer:"+player+"\n");
            builder.append("boardSize:"+boardsize+"\n");
            builder.append("winning_sequence:"+winning_seq+"\n");
            builder.append("Matrix"+"\n");
            
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
        public int quitGame(){
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
                        return player;
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
               return player;
            }
            return player;
        }
        public int[][] resumeAgame(String filename){
        try{
            BufferedReader reader = new BufferedReader(new FileReader(filename+".txt"));
            String line = "";
            int row = 0,column=0,count=0;
            boolean matrix_starting=false;
            while((line = reader.readLine()) != null)
            {   
                count++;
                if(count==1){
                    String[] noOfPlayer = line.split(":",3);
                    noOfPlayers         = Integer.parseInt(noOfPlayer[1]);
                }      
                if(count==2){
                    String[] playerChance = line.split(":",2);
                    player          = Integer.parseInt(playerChance[1]);
                } 
                if(count==3){
                    String[] boardSize = line.split(":",2);
                    boardsize          = Integer.parseInt(boardSize[1]);
                }      
    
                if(count==4){
                    String[] winning_sequence = line.split(":",2);
                    winning_seq               = Integer.parseInt(winning_sequence[1]);
                }      
                if(line.matches("^Matrix")){
                   matrix_starting = true;
                   continue;
                }   
                if(matrix_starting){
                    String[] cols = line.split(","); 
                    column        = cols.length;
                    row++;
                }
            }
            
            int[][] board = new int[row][column];            
            BufferedReader readeragain = new BufferedReader(new FileReader(filename+".txt"));
            line = "";
            row=0;   
            count=0;
            while((line = readeragain.readLine()) != null)
                {   count++;           
                    if(count>5){ 
                    String[] cols = line.split(","); 
                    int col = 0;
                    for(String  c : cols)
                    {
                    board[row][col] = Integer.parseInt(c);
                    col++;
                    }
                    row++;
                }
            }
                    reader.close();
                    return board;
        }catch(IOException e){
            System.out.println("Can't resume, Couldn't find the file");
            return null;
        }
    }
}