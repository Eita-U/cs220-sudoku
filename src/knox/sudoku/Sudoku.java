package knox.sudoku;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.InputMismatchException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

/**
 * 
 * This is the MODEL class. This class knows all about the
 * underlying state of the Sudoku game. We can VIEW the data
 * stored in this class in a variety of ways, for example,
 * using a simple toString() method, or using a more complex
 * GUI (Graphical User Interface) such as the SudokuGUI 
 * class that is included.
 * 
 * @author jaimespacco
 *
 */
public class Sudoku {
	int[][] board = new int[9][9];
	
	public int get(int row, int col) {
		// TODO: check for out of bounds
		if(outOfBounds(row, col)) {
			System.out.println("Out of Bounds");
		}
		
		return board[row][col];
	}
	
	public void set(int row, int col, int val) {
		// TODO: make sure val is legal
		if(outOfBounds(row, col)) {
			System.out.println(String.format("Out of bounds"));
			return;
		}else if(!isLegal(row, col, val) || !isBlank(row, col)) {
			System.out.println(String.format("Can't enter %d at row %d and col %d", val, row, col));
			return;
		}
		board[row][col] = val;
	}
	
	public boolean isLegal(int row, int col, int val) {
		// TODO: check if it's legal to put val at row, col
		if(getLegalValues(row, col).contains(val)) {
			return true;
		}else {
			return false;
		}
	}
	
	public Collection<Integer> getLegalValues(int row, int col) {
		// TODO: return only the legal values that can be stored at the given row, col
		if(outOfBounds(row, col)) {
			Set<Integer> legalVals = new HashSet<>(Arrays.asList());
			return legalVals;
		}
		if(!isBlank(row, col)) {
			Set<Integer> legalVals = new HashSet<>(Arrays.asList(board[row][col]));
			return legalVals;
		}
		Set<Integer> legalVals = new HashSet<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9));
		for(int i = 0; i < 9; i++) {
			legalVals.remove(board[row][i]);
			legalVals.remove(board[i][col]);
		}
		int gridRow = row / 3;
		int gridCol = col / 3;
		for(int r = gridRow * 3; r < gridRow * 3 + 3; r++) {
			for(int c = gridCol * 3; c < gridCol * 3 + 3; c++) {
				legalVals.remove(board[r][c]);
			}
		}
		return legalVals;
	}
	
/**

_ _ _ 3 _ 4 _ 8 9
1 _ 3 2 _ _ _ _ _
etc


0 0 0 3 0 4 0 8 9

 */
	public void load(File file) {
		try {
			Scanner scan = new Scanner(file);
			// read the file
			for (int r=0; r<9; r++) {
				for (int c=0; c<9; c++) {
					int val = scan.nextInt();
					board[r][c] = val;
				}
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public void load(String filename) {
		load(new File(filename));
	}
	
	/**
	 * Return which 3x3 grid this row is contained in.
	 * 
	 * @param row
	 * @return
	 */
	public int get3x3row(int row) {
		return row / 3;
	}
	
	/**
	 * Convert this Sudoku board into a String
	 */
	public String toString() {
		String result = "";
		for (int r=0; r<9; r++) {
			for (int c=0; c<9; c++) {
				int val = get(r, c);
				if (val == 0) {
					result += "_ ";
				} else {
					result += val + " ";
				}
			}
			result += "\n";
		}
		return result;
	}
	
	public String toFileString() {
		String result = "";
		for (int r=0; r<9; r++) {
			for (int c=0; c<9; c++) {
				int val = get(r, c);
				result += val + " ";
			}
			result += "\n";
		}
		return result;
	}
	
	public static void main(String[] args) {
		Sudoku sudoku = new Sudoku();
		sudoku.load("easy1.txt");
		System.out.println(sudoku);
		
		Scanner scan = new Scanner(System.in);
		while (!sudoku.gameOver()) {
			try {
				System.out.println("enter value r, c, v :");
				int r = scan.nextInt();
				int c = scan.nextInt();
				int v = scan.nextInt();
				sudoku.set(r, c, v);
			} catch(InputMismatchException e) {
				System.out.println("Enter valid values");
				scan.nextLine();
			}
			System.out.println(sudoku);
		}
		System.out.println("Victory!");
		System.exit(0);;
	}

	public boolean gameOver() {
		// TODO check that there are still open spots
		for(int[] row : board) {
			for(int val : row) {
				if(val == 0) {
					return false;
				}
			}
		}
		return true;
	}
	
	public boolean isVictory() {
		if(!gameOver()) {
			return false;
		}
		for(int r = 0; r < 9; r++) {
			for(int c = 0; c < 9; c++) {
				if(!isLegal(r, c, board[r][c])){
					return false;
				}
			}
		}
		return true;
	}

	public boolean isBlank(int row, int col) {
		if(outOfBounds(row, col)) {
			return false;
		}
		return board[row][col] == 0;
	}
	
	public boolean outOfBounds(int row, int col) {
		if(row < 0 || row > 8 || col < 0 || col > 8) {
			return true;
		}else {
			return false;
		}
	}

}
