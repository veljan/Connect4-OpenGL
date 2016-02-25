
public class Board
{
	private static final int COLS = 9; 
	private static final int ROWS = 7;
	
	private Field[][] board = new Field[7][9];
	private int br; //broj istobojnih susednih polja
	
	public Board()
	{
		this.br = 1;
		for (int i = 0; i < ROWS; i++){
			for (int j = 0; j < COLS; j++){				
					board[i][j] = new Field(i, j);				
			}
		}
	}

	public final Field getField(int row, int col){
		return board[row][col];
	}

	public final int lowestEmptyRowIndex(int col){
		int i;
		for (i = ROWS - 1; i >= 0; --i){
			if (board[i][col].getState().notEqualsTo(Field.FieldState.Empty))
				break;
		}
		return i + 1;
	}

	public final void setRedField(int row, int col){
		board[row][col].setRedBall();
	}
	
	public final void setBlueField(int row, int col){
		board[row][col].setBlueBall();
	}

	public final boolean victory(){
		return checkRows() || checkCols() || checkDiag1() || checkDiag2();
	}

	private boolean checkRows()	{
		br = 1;
		for (int j = 0; j < COLS; ++j)	{
			for (int i = 1; i < ROWS; ++i)	{
				if (checkField(i, j, i - 1, j))	{
					return true;
				}
			}
		}	
		return false;
	}
	
	private boolean checkCols(){
		br = 1;		
		for (int i = 0; i < ROWS; ++i){
			for (int j = 1; j < COLS; ++j)	{
				if (checkField(i, j, i, j - 1)){
					return true;
				}				
			}
		}
		return false;
	}
	
	private boolean checkDiag1(){
		for (int i = 3; i < ROWS; ++i){
			if (checkLowerDiag1(i)){
				return true;
			}			
		}		
		for (int j = 1; j < COLS - 3; ++j)	{
			if (checkUpperDiag1(j)){
				return true;
			}
		}
		return false;
	}
	
	private boolean checkLowerDiag1(int row){
		br = 1;
		row--;
		for (int col = 1; row >= 0; ++col,--row){
			if (checkField(row, col, row + 1, col - 1))	{
				return true;
			}
		}
		return false;
	}
	
	private boolean checkUpperDiag1(int col){
		br = 1;
		col++;
		for (int row = ROWS - 2; (row >= 0) && (col < COLS); --row, ++col){
			if (checkField(row, col, row + 1, col - 1)){
				return true;
			}
		}
		return false;
	}
	
	private boolean checkDiag2(){
		
		for (int i = ROWS - 4; i >= 0; --i){
			if (checkUpperDiag2(i)){
					return true;
			}			
		}
		
		for (int j = 1; j < COLS - 3; ++j)	{
			if (checkLowerDiag2(j)){
					return true;
			}
		}
		return false;
	}
	
	private boolean checkUpperDiag2(int row){
		row++;
		for (int col = 1; row < ROWS; col++, row++){
			if (checkField(row, col, row - 1, col - 1)){
				return true;
			}
		}
		return false;
	}
	
	private boolean checkLowerDiag2(int col){
		col++;
		for (int row = 1; (row < ROWS) && (col < COLS); ++row, ++col){
			if (checkField(row, col, row - 1, col - 1)){
				return true;
			}
		}
		return false;
	}
	
	private boolean checkField(int row, int col, int row1, int col1){
		if (board[row][col].getState().equalsTo(Field.FieldState.Empty)){
			br = 1;
		}else if (board[row][col].getState().equalsTo(board[row1][col1].getState())){
			br++;
			if (br == 4){
				return true;
			}
		}else{
			br = 1;
		}
		return false;
	}
}