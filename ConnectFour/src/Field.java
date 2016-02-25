
public class Field
{
	public static class FieldState{
		public final static FieldState Empty = new FieldState(0);
		public final static FieldState Red = new FieldState(1);
		public final static FieldState Blue = new FieldState(2);
		
		private int state;
		private FieldState(int state){
			this.state = state;
		}

		public final int getState()	{
			return state;
		}

		public boolean equalsTo(FieldState st){
			return state == st.getState();
		}
		
		public boolean notEqualsTo(FieldState st){
			return state != st.getState();
		}	
		
	}

	public Field(int row, int col){
		this.row = row;
		this.col = col;	
		this.state = FieldState.Empty;

	}

	private int row;
	private int col;
	private FieldState state;

	public final int getCol(){
		return col;
	}

	public final void setCol(int col){
		this.col = col;
	}

	public final int getRow(){
		return row;
	}

	public final void setRow(int row){
		this.row = row;
	}

	public final void setRedBall()	{
		if (state.equalsTo(FieldState.Empty)){
			state = FieldState.Red;
		}
	}

	public final void setBlueBall(){
		if (state.equalsTo(FieldState.Empty)){
			state = FieldState.Blue;
		}
	}

	public final boolean isEmpty(){
		return state.equalsTo(FieldState.Empty);
	}

	public final FieldState getState(){
		return state;
	}
}