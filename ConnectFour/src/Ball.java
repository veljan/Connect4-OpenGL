
public class Ball
{
	public static class BallColor
	{
		private float r;
		private float g;
		private float b;
		
		BallColor(float r, float g, float b){
			this.r = r;
			this.g = g;
			this.b = b;
		}

		public final float getR(){
			return r;
		}

		public final float getG(){
			return g;
		}

		public final float getB(){
			return b;
		}


		public boolean equalsTo(BallColor bc){
			return r == bc.getR() && g == bc.getG() && b == bc.getB();
		}		
		
	}
	
	public final BallColor Red = new BallColor(1,0,0);
	public final BallColor Blue = new BallColor(0,0,1);
		
	private BallColor color;
	public static final float BALL_SIZE = 1.0F;
	
	public Ball(BallColor color)
	{
		this.color = color;

	}	

	public final BallColor getColor()	{
		return color;
	}

	public final void setColor(BallColor color)	{

		this.color = color;
	}
}
