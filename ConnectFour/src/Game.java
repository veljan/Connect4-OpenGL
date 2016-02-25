
public class Game
{
	public static class Player
	{
		private int player;
		
		private Player(int player){
			this.player = player;
		}

		public final int getPlayer(){
			return player;
		}

		public boolean equalsTo(Player p){
			return player == p.getPlayer();
		}
		
		public final static Player Blue = new Player(1);
		public final static Player Red = new Player(2);
	}
	
	private Player currPlayer;
	private Board board = new Board();
	
	public Game(Player firstPlayer)	{
		this.currPlayer = firstPlayer;
		this.board = new Board();
		// TODO Auto-generated constructor stub

	}
	
	public final Field.FieldState getCurFieldColor(){
		if (currPlayer.equalsTo(Player.Blue)){
			return Field.FieldState.Blue;
		}
		else
		{
			return Field.FieldState.Red;
		}
	}

	public final Game.Player getCurPlayer()
	{
		return currPlayer;
	}
	
	private void switchPlayers()
	{
		if (currPlayer.equalsTo(Player.Blue)){
			currPlayer = Player.Red;
		}else{
			currPlayer = Player.Blue;
		}
	}

	public final Board getBoard(){
		return board;
	}

	public final void playerMove(int col)
	{
		int row = board.lowestEmptyRowIndex(col);
		if (row == 7)
			return;
		if (currPlayer.equalsTo(Player.Blue)){
			board.setBlueField(row, col);
		}else{
			board.setRedField(row, col);
		}
		switchPlayers();
	}
	
	public final boolean isGameOver()
	{
		return board.victory();
	}
}








