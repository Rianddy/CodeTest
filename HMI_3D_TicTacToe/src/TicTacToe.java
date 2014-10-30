package task.third;

import java.io.BufferedInputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.TreeMap;

public class TicTacToe {
	private int size = 4;
	// first, second, third, fourth
	// row1, row2, row3, row4, col1, col2, col3, col4, dia1, dia2
	private int[][] horizontalSolutionScore;
	private int[][][] cube;
	// row1, row2, row3, row4
	// col1, col2, col3, col4
	private int[][] verticalSolutionScore;
	// row1, row2, row3, row4, col1, col2, col3, col4, dia1, dia2
	// dia1, dia2
	private int[][] diaSolutionScore;
	private final int PLAYER = 1;
	private final int COMPUTER = 2;

	public TicTacToe(int[][][] cube) {
		this.cube = cube;
		this.horizontalSolutionScore = new int[size][2 * size + 2];
		this.verticalSolutionScore = new int[size][size];
		this.diaSolutionScore = new int[2 * size + 2][2];
	}
	/**
	 * 
	 * @param flat
	 * @param row
	 * @param column
	 * @param user
	 * @param changeCube
	 * 
	 * Function: placeXorO
	   -------------
	   This method is to help add score of the three arrays that stores 76 solutions. When a step
	   is made by player of computer, the effect of the step will automatically calculated and 
	   has a effect on related solutions.
	   
	   For example, horizontalSolutionScore[flat][row] += addScore(user); the row of the layer of flat
	   will be affected because its step in this row. Then call method addScore to add +1 for player or
	   -1 for computer. 
	   	 
	 * @return nothing 
	 */
	public void placeXorO(int flat, int row, int column, int user, boolean changeCube) {
		if(changeCube)
			cube[flat][row][column] = user;

		// add horizontal solution score
		horizontalSolutionScore[flat][row] += addScore(user);
		horizontalSolutionScore[flat][size + column] += addScore(user);
		if (row == column)
			horizontalSolutionScore[flat][2 * size] += addScore(user);
		if (row == (size - column - 1))
			horizontalSolutionScore[flat][2 * size + 1] += addScore(user);

		// add vertical solution score
		verticalSolutionScore[row][column] += addScore(user);

		// diagonal of latitude vertical flat
		if (flat == (size - 1 - column))
			diaSolutionScore[row][0] += addScore(user);
		else if (flat == column)
			diaSolutionScore[row][1] += addScore(user);

		// diagonal of longitude vertical flat
		if (flat == (size - 1 - row))
			diaSolutionScore[size + column][0] += addScore(user);
		else if (flat == row)
			diaSolutionScore[size + column][1] += addScore(user);

		// diagonal of cube
		if (flat == row && row == column)
			diaSolutionScore[2 * size][1] += addScore(user);
		else if (flat == (size - 1 - row) && row == column)
			diaSolutionScore[2 * size][0] += addScore(user);
		else if (flat == row && row == (size - 1 - column))
			diaSolutionScore[2 * size + 1][1] += addScore(user);
		else if (flat == column && column == (size - 1 - row))
			diaSolutionScore[2 * size + 1][0] += addScore(user);
		System.out.print("");
	}
	/**
	 * 
	 * @param user
	 * 
	 * Function: addScore
	   -------------
	   If the user is PLAYER which is 1, then return 1 otherwise return -1
	   	 
	 * @return int 
	 */
	public int addScore(int user) {
		if (user == this.PLAYER)
			return 1;
		else
			return -1;
	}
	
	public static int[][] deepCopy(int[][] original) {
	    final int[][] result = new int[original.length][];
	    for (int i = 0; i < original.length; i++) {
	        result[i] = Arrays.copyOf(original[i], original[i].length);
	    }
	    return result;
	}
	
	/**
	 * 
	 * Function: judgeWinOrLose
	   -------------
	   To judge whether player or computer already win this game by check whether there is any
	   solution that scores 4 or -4.
	   
	   For example, if(horizontalSolutionScore[i][j]==4) return 1; shows if any solution in this 
	   array it will return 1 to show that player already win this game
	   	 
	 * @return int 
	 */
	public int judgeWinOrLose(){
		for(int i=0;i<horizontalSolutionScore.length;i++){
			for(int j=0;j<horizontalSolutionScore[i].length;j++)
			if(horizontalSolutionScore[i][j]==4)
				return 1;
			else
				if(horizontalSolutionScore[i][j]==-4)
					return -1;
		}
		for(int i=0;i<diaSolutionScore.length;i++){
			for(int j=0;j<diaSolutionScore[i].length;j++)
			if(diaSolutionScore[i][j]==4)
				return 1;
			else
				if(diaSolutionScore[i][j]==-4)
					return -1;
		}
		for(int i=0;i<verticalSolutionScore.length;i++){
			for(int j=0;j<verticalSolutionScore[i].length;j++)
			if(verticalSolutionScore[i][j]==4)
				return 1;
			else
				if(verticalSolutionScore[i][j]==-4)
					return -1;
		}
		return 0;
	}
	
	/**
	 * 
	 * Function: judgeNextStep
	   -------------
	   This method is to help computer make a decision about how to place a next step by check 
	   every available place in three-dimensional array. Then calculate all solutions' score and 
	   map it to that specific place. As a result, sort the score and help computer make a step
	   that has the best score.
	   
	   For example, score = judgeSolutionScore(horizontalSolutionScore)+ judgeSolutionScore(verticalSolutionScore)
	   + judgeSolutionScore(diaSolutionScore); is to calculate sum of all solutions' score
	   	 
	 * @return nothing 
	 */
	public void judgeNextStep() {
		TreeMap<Integer,String> scoreOfEachPlace=new TreeMap<Integer,String>();
		for (int flat = 0; flat < size; flat++) {
			for (int i = 0; i < size; i++) {
				for (int j = 0; j < size; j++) {
					if (cube[flat][i][j] == 0) {
						int[][] tempHorizentalSolutionScore =deepCopy(horizontalSolutionScore);
						int[][] tempVerticalSolutionScore = deepCopy(verticalSolutionScore);
						int[][] tempDiaSolutionScore = deepCopy(diaSolutionScore);
						placeXorO(flat, i, j, this.COMPUTER,false);
						int score = 0;
						score = judgeSolutionScore(horizontalSolutionScore)
								+ judgeSolutionScore(verticalSolutionScore)
								+ judgeSolutionScore(diaSolutionScore);
						String cordinator=flat+" "+i+" "+j+" ";
						scoreOfEachPlace.put(score,cordinator);
						horizontalSolutionScore = tempHorizentalSolutionScore;
						verticalSolutionScore = tempVerticalSolutionScore;
						diaSolutionScore = tempDiaSolutionScore;
					}
				}
			}
		}
		String cordinator[]=scoreOfEachPlace.get(scoreOfEachPlace.firstKey()).split(" ");
		int flat=Integer.valueOf(cordinator[0]);
		int row=Integer.valueOf(cordinator[1]);
		int column=Integer.valueOf(cordinator[2]);
		System.out.println("Computer's place:");
		System.out.println("flat:"+(flat+1)+"    row:"+(row+1)+"    column"+(column+1));
		placeXorO(flat,row,column,this.COMPUTER,true);
	}

	/**
	 * 
	 * @param solutionScore
	 * 
	 * Function: judgeSolutionScore
	   -------------
	   According to the solutionScore to calculate the weight of every solution. The player
	   solution score with 1 weights 1, score with 2 weights 10, score with 3 weights 200. However
	   when computer score with -3 weights -100 because it needs block players 3 in one line rather
	   to generate its own 3 in one line situation.
	   
	   For example,if (solutionScore[i][j] == -4) {total = total - 4000;}  means computer has 
	   huge weight when it has 4 in one line situation
	   	 
	 * @return nothing 
	 */
	public int judgeSolutionScore(int[][] solutionScore) {
		int total = 0;
		for (int i = 0; i < solutionScore.length; i++)
			for (int j = 0; j < solutionScore[i].length; j++)
				if (solutionScore[i][j] == -4) {
					total = total - 4000;
				} else {
					if (solutionScore[i][j] == 4) {
						total = total + 4000;
					} else {
						if (solutionScore[i][j] == -3) {
							total = total - 100;
						} else {
							if (solutionScore[i][j] == 3) {
								total = total + 200;
							} else {
								if (solutionScore[i][j] == -2) {
									total = total - 10;
								} else {
									if (solutionScore[i][j] == 2) {
										total = total + 10;
									} else {
										if (solutionScore[i][j] == -1) {
											total = total - 1;
										} else {
											if (solutionScore[i][j] == 1) {
												total = total + 1;
											}
										}
									}
								}
							}
						}
					}
				}
		return total;
	}


	public static void main(String args[]) {
		int size = 4;
		int[][][] cube = new int[size][size][size];
		TicTacToe tictactoe = new TicTacToe(cube);
		boolean gameOver = false;
		int PLAYER=1;
		List<Long> runTime = new LinkedList<Long>();
		while (!gameOver) {
			System.out
					.println("Instruction:Layer1(bot)->Layer4(top), 1 means player and 2 means computer");
			for (int i = 0; i < size; i++) {
				System.out.println("Layer" + (i+1) + ":");
				for (int j = 0; j < size; j++) {
					for (int q = 0; q < size; q++) {
						System.out.print(cube[i][j][q] + " ");
					}
					System.out.println();
				}
			}

			System.out.println("Please input the layer first:");
			Scanner scan = new Scanner(new BufferedInputStream(System.in));
			int layer = scan.nextInt();
			System.out.println("Please input the row:");
			int row = scan.nextInt();
			System.out.println("Please input the column:");
			int column = scan.nextInt();
			System.out.println("---------------------------------------------");
			tictactoe.placeXorO(layer-1, row-1, column-1, PLAYER, true);
			if(tictactoe.judgeWinOrLose()==1){
				System.out.println("Player wins!");
				break;
			}else{
				if(tictactoe.judgeWinOrLose()==-1){
					System.out.println("Computer wins!");
					break;
				}
			}
			long startTime = System.nanoTime();
			tictactoe.judgeNextStep();
			long endTime = System.nanoTime();
			runTime.add(endTime - startTime);
			if(tictactoe.judgeWinOrLose()==1){
				System.out.println("Player wins!");
				break;
			}else{
				if(tictactoe.judgeWinOrLose()==-1){
					System.out.println("Computer wins!");
					break;
				}
			}
		}
		for(int i=0;i<runTime.size();i++){
			System.out.println(runTime.get(i));
		}
	}
}
