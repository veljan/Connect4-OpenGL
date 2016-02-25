
import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.nio.ByteBuffer;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.glu.GLUquadric;
import com.jogamp.opengl.util.Animator;
import com.jogamp.opengl.util.gl2.GLUT;


public class C4 extends Frame implements KeyListener, GLEventListener, MouseListener, MouseMotionListener {
	
	private static final long serialVersionUID = 1L;
	
	private static final float BALL_SIZE = 1.0f;
	private static final int BALL_SLICES = 20;
	private static final int BALL_STACKS = 16;
	private static final float F_SIZE = (float) (2*BALL_SIZE + 0.1);
	private static final int NOT_VALID = -1;

	private static final int COLS = 9;
	private static final int ROWS = 7;
	private int col = NOT_VALID;	
	
	final static Ball.BallColor Red = new Ball.BallColor(1, 0, 0);
	final static Ball.BallColor Blue = new Ball.BallColor(0, 0, 1);
        
    private GLCapabilities capabilities;
    private Animator animator;
    private GLProfile profile;
	private GLCanvas canvas;
	
	private float angle = 0.0f;
	private float [] axis = new float[3]; 
	private float [] trans = new float[3];

	private boolean trackingMouse = false;
	private boolean redrawContinue = false;
	private boolean trackballMove = false;

	private float [] lastPos = {0.0f, 0.0f, 0.0f};
	private int curx, cury;
	private int startX, startY;
	private int	winWidth = 640, winHeight = 480;
	
	private GL2 gl;
	private GLU glu = new GLU();	
	private GLUT glut = new GLUT();	
	
	private float modelViewMatrix[] = {1.0f, 0.0f, 0.0f, 0.0f,
			 						   0.0f, 1.0f, 0.0f, 0.0f,
			 						   0.0f, 0.0f, 1.0f, 0.0f,
			 						   0.0f, 0.0f, 0.0f, 1.0f};

	private boolean aDrop = false;
	private float dbHeight;
	private int row;
	
	byte space[] ={0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};

	byte letters[][] = {{0x00, 0x00, (byte)0xc3, (byte)0xc3, (byte)0xc3, (byte)0xc3, (byte)0xff, (byte)0xc3, (byte)0xc3, (byte)0xc3, (byte)0x66, (byte)0x3c, (byte)0x18},
						{0x00, 0x00, (byte)0xfe, (byte)0xc7, (byte)0xc3, (byte)0xc3, (byte)0xc7, (byte)0xfe, (byte)0xc7, (byte)0xc3, (byte)0xc3, (byte)0xc7, (byte)0xfe},
						{0x00, 0x00, (byte)0x7e, (byte)0xe7, (byte)0xc0, (byte)0xc0, (byte)0xc0, (byte)0xc0, (byte)0xc0, (byte)0xc0, (byte)0xc0, (byte)0xe7, (byte)0x7e},
						{0x00, 0x00, (byte)0xfc, (byte)0xce, (byte)0xc7, (byte)0xc3, (byte)0xc3, (byte)0xc3, (byte)0xc3, (byte)0xc3, (byte)0xc7, (byte)0xce, (byte)0xfc},
						{0x00, 0x00, (byte)0xff, (byte)0xc0, (byte)0xc0, (byte)0xc0, (byte)0xc0, (byte)0xfc, (byte)0xc0, (byte)0xc0, (byte)0xc0, (byte)0xc0, (byte)0xff},
						{0x00, 0x00, (byte)0xc0, (byte)0xc0, (byte)0xc0, (byte)0xc0, (byte)0xc0, (byte)0xc0, (byte)0xfc, (byte)0xc0, (byte)0xc0, (byte)0xc0, (byte)0xff},
						{0x00, 0x00, (byte)0x7e, (byte)0xe7, (byte)0xc3, (byte)0xc3, (byte)0xcf, (byte)0xc0, (byte)0xc0, (byte)0xc0, (byte)0xc0, (byte)0xe7, (byte)0x7e},
						{0x00, 0x00, (byte)0xc3, (byte)0xc3, (byte)0xc3, (byte)0xc3, (byte)0xc3, (byte)0xff, (byte)0xc3, (byte)0xc3, (byte)0xc3, (byte)0xc3, (byte)0xc3},
						{0x00, 0x00, (byte)0x7e, (byte)0x18, (byte)0x18, (byte)0x18, (byte)0x18, (byte)0x18, (byte)0x18, (byte)0x18, (byte)0x18, (byte)0x18, (byte)0x7e},
						{0x00, 0x00, (byte)0x7c, (byte)0xee, (byte)0xc6, (byte)0x06, (byte)0x06, (byte)0x06, (byte)0x06, (byte)0x06, (byte)0x06, (byte)0x06, (byte)0x06},
						{0x00, 0x00, (byte)0xc3, (byte)0xc6, (byte)0xcc, (byte)0xd8, (byte)0xf0, (byte)0xe0, (byte)0xf0, (byte)0xd8, (byte)0xcc, (byte)0xc6, (byte)0xc3},
						{0x00, 0x00, (byte)0xff, (byte)0xc0, (byte)0xc0, (byte)0xc0, (byte)0xc0, (byte)0xc0, (byte)0xc0, (byte)0xc0, (byte)0xc0, (byte)0xc0, (byte)0xc0},
						{0x00, 0x00, (byte)0xc3, (byte)0xc3, (byte)0xc3, (byte)0xc3, (byte)0xc3, (byte)0xc3, (byte)0xdb, (byte)0xff, (byte)0xff, (byte)0xe7, (byte)0xc3},
						{0x00, 0x00, (byte)0xc7, (byte)0xc7, (byte)0xcf, (byte)0xcf, (byte)0xdf, (byte)0xdb, (byte)0xfb, (byte)0xf3, (byte)0xf3, (byte)0xe3, (byte)0xe3},
						{0x00, 0x00, (byte)0x7e, (byte)0xe7, (byte)0xc3, (byte)0xc3, (byte)0xc3, (byte)0xc3, (byte)0xc3, (byte)0xc3, (byte)0xc3, (byte)0xe7, (byte)0x7e},
						{0x00, 0x00, (byte)0xc0, (byte)0xc0, (byte)0xc0, (byte)0xc0, (byte)0xc0, (byte)0xfe, (byte)0xc7, (byte)0xc3, (byte)0xc3, (byte)0xc7, (byte)0xfe},
						{0x00, 0x00, (byte)0x3f, (byte)0x6e, (byte)0xdf, (byte)0xdb, (byte)0xc3, (byte)0xc3, (byte)0xc3, (byte)0xc3, (byte)0xc3, (byte)0x66, (byte)0x3c},
						{0x00, 0x00, (byte)0xc3, (byte)0xc6, (byte)0xcc, (byte)0xd8, (byte)0xf0, (byte)0xfe, (byte)0xc7, (byte)0xc3, (byte)0xc3, (byte)0xc7, (byte)0xfe},
						{0x00, 0x00, (byte)0x7e, (byte)0xe7, (byte)0x03, (byte)0x03, (byte)0x07, (byte)0x7e, (byte)0xe0, (byte)0xc0, (byte)0xc0, (byte)0xe7, (byte)0x7e},
						{0x00, 0x00, (byte)0x18, (byte)0x18, (byte)0x18, (byte)0x18, (byte)0x18, (byte)0x18, (byte)0x18, (byte)0x18, (byte)0x18, (byte)0x18, (byte)0xff},
						{0x00, 0x00, (byte)0x7e, (byte)0xe7, (byte)0xc3, (byte)0xc3, (byte)0xc3, (byte)0xc3, (byte)0xc3, (byte)0xc3, (byte)0xc3, (byte)0xc3, (byte)0xc3},
						{0x00, 0x00, (byte)0x18, (byte)0x3c, (byte)0x3c, (byte)0x66, (byte)0x66, (byte)0xc3, (byte)0xc3, (byte)0xc3, (byte)0xc3, (byte)0xc3, (byte)0xc3},
						{0x00, 0x00, (byte)0xc3, (byte)0xe7, (byte)0xff, (byte)0xff, (byte)0xdb, (byte)0xdb, (byte)0xc3, (byte)0xc3, (byte)0xc3, (byte)0xc3, (byte)0xc3},
						{0x00, 0x00, (byte)0xc3, (byte)0x66, (byte)0x66, (byte)0x3c, (byte)0x3c, (byte)0x18, (byte)0x3c, (byte)0x3c, (byte)0x66, (byte)0x66, (byte)0xc3},
						{0x00, 0x00, (byte)0x18, (byte)0x18, (byte)0x18, (byte)0x18, (byte)0x18, (byte)0x18, (byte)0x3c, (byte)0x3c, (byte)0x66, (byte)0x66, (byte)0xc3},
						{0x00, 0x00, (byte)0xff, (byte)0xc0, (byte)0xc0, (byte)0x60, (byte)0x30, (byte)0x7e, (byte)0x0c, (byte)0x06, (byte)0x03, (byte)0x03, (byte)0xff}};

	int fontOffset;
	
	Game g = new Game(Game.Player.Blue);	
    
    public C4() {
	    profile = GLProfile.getDefault();
	    capabilities = new GLCapabilities(profile);
    	canvas = new GLCanvas(capabilities);
		animator = new Animator(canvas);
		
		canvas.addGLEventListener(this);
		canvas.addKeyListener(this);
		canvas.addMouseListener(this);		
		canvas.addMouseMotionListener(this);	
		
		add(canvas, BorderLayout.CENTER);
		setResizable(false);

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				animator.stop();
				dispose();
				System.exit(0);
			}
		});
		
		//
    	
    }	

    public void play() {
    				
    	setTitle("Connect Four");
		setSize(640, 640);
		setVisible(true);
		animator.start();
		canvas.requestFocus();		
	}
    
    public void makeRasterFont(){
       int i, j;
       gl.glPixelStorei(GL2.GL_UNPACK_ALIGNMENT, 1);

       fontOffset = gl.glGenLists (128);
       for (i = 0, j = 'A'; i < 26; i++,j++) {
          gl.glNewList(fontOffset + j, GL2.GL_COMPILE);
          gl.glBitmap(8, 13, 0.0f, 2.0f, 10.0f, 0.0f, letters[i], fontOffset);          	
          gl.glEndList();
       }
       gl.glNewList(fontOffset + ' ', GL2.GL_COMPILE);
       	gl.glBitmap(8, 13, 0.0f, 2.0f, 10.0f, 0.0f, space, fontOffset);
       gl.glEndList();
    }
    	
    public void printString(String string) {
       gl.glPushAttrib (GL2.GL_LIST_BIT);
       gl.glListBase(fontOffset);
       gl.glCallList(string.length());
       //gl.glCallLists(s.length, GL2.GL_UNSIGNED_BYTE, (byte)s[]);
       gl.glPopAttrib ();
    }
    
		
    @Override
    public void display(GLAutoDrawable drawable) {
    	// TODO Auto-generated method stub
    	gl = drawable.getGL().getGL2();		   	

    	gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);	
    	
    	gl.glPushMatrix(); // 1
    		displayText();
    	gl.glPopMatrix(); // -1			   

    	if (trackballMove){
    		gl.glRotatef(angle, axis[0], axis[1], axis[2]);
    	}
    	
    	gl.glPushMatrix(); //2

    	gl.glLoadMatrixf(modelViewMatrix, 0);
    	
    	if(trackingMouse)
    		gl.glRotatef(angle, axis[0], axis[1], axis[2]);

    	gl.glPushMatrix(); //3
    	gl.glTranslatef(-4 * F_SIZE, -3 * F_SIZE, 0 * F_SIZE);
    	gl.glRotatef(20.0f, 1.0f, 0.0f, 0.0f);

    	if (aDrop) {
    		gl.glPushMatrix(); // 4
    		if (g.getCurPlayer().equals(Game.Player.Red)){   			
    			 gl.glColor3f(1.0f, 0.0f, 1.0f);
    		}else{
    			gl.glColor3f(0.0f, 0.0f, 1.0f);
    		}
    		gl.glTranslatef(col * F_SIZE, dbHeight * F_SIZE, 0);
    		glut.glutSolidSphere(BALL_SIZE, BALL_SLICES, BALL_STACKS);
    		gl.glPopMatrix(); //-4
    	}
    	
    	displayStand();

    	displayBalls();

    	gl.glColor4f(0.0f, 1.0f, 0.0f, 0.3f);

    	gl.glEnable(GL2.GL_BLEND);
    	gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);
    	gl.glDepthMask(false);
    	displayCylinders();
    	gl.glDepthMask(true);
    	gl.glDisable(GL2.GL_BLEND);

    	gl.glPopMatrix(); // -3
    	gl.glGetFloatv(GL2.GL_MODELVIEW_MATRIX, modelViewMatrix, 0);
    	gl.glPopMatrix(); // -2

    	gl.glFlush();
    }

    private void displayText() {
    	// TODO Auto-generated method stub
    	gl.glColor3f(0.0f, 1.0f, 0.0f);

    	gl.glRasterPos2f(8*F_SIZE, -8*F_SIZE);
    	if(g.isGameOver()) {
    		if(g.getCurPlayer() == Game.Player.Blue) {
    			printString("CRVENI IGRAC POBEDIO");
    		}else {
    			printString("PLAVI IGRAC POBEDIO");
    		}
    	} else if(g.getCurPlayer() == Game.Player.Blue) {
    		printString("PLAVI IGRAC NA POTEZU");
    	}else {
    		printString("CRVENI IGRAC NA POTEZU");
    	}
    }

    private void displayCylinders() {
    	// TODO Auto-generated method stub
    	GLUquadric obj = glu.gluNewQuadric();
    	for (int j = 0; j < COLS; ++j) {				
    		gl.glPushMatrix();
    		gl.glColor4f(1.0f, 1.0f, 1.0f, 0.3f);
    		gl.glTranslatef(j * F_SIZE, ROWS * F_SIZE - F_SIZE / 2,	0);
    		gl.glRotatef(90.0f, 1.0f, 0.0f, 0.0f);
    		glu.gluCylinder(obj, F_SIZE / 2, F_SIZE / 2, ROWS * F_SIZE, 30, 30);
    		gl.glPopMatrix();
    	}		
    }
	
	private void displayStand() {
		// TODO Auto-generated method stub
		gl.glPushMatrix();
		gl.glColor3f(0.0f, 1.0f, 1.0f);
		gl.glBegin(GL2.GL_QUADS);
			gl.glVertex3f(-0.5f * F_SIZE, -1f, -0.5f * F_SIZE);
			gl.glVertex3f( 8.5f * F_SIZE, -1f, -0.5f * F_SIZE);
			gl.glVertex3f( 8.5f * F_SIZE, -1f,  0.5f * F_SIZE);
			gl.glVertex3f( -0.5f * F_SIZE, -1f,  0.5f * F_SIZE);
			
			gl.glVertex3f(-0.5f * F_SIZE, -1f, -0.5f * F_SIZE);
			gl.glVertex3f(-0.5f * F_SIZE, -1.5f, -0.5f * F_SIZE);
			gl.glVertex3f( -0.5f * F_SIZE, -1.5f,  0.5f * F_SIZE);
			gl.glVertex3f( -0.5f * F_SIZE, -1.0f,  0.5f * F_SIZE);
			
			gl.glVertex3f(8.5f * F_SIZE, -1f, -0.5f * F_SIZE);
			gl.glVertex3f(8.5f * F_SIZE, -1.5f, -0.5f * F_SIZE);
			gl.glVertex3f( 8.5f * F_SIZE, -1.5f,  0.5f * F_SIZE);
			gl.glVertex3f( 8.5f * F_SIZE, -1.0f,  0.5f * F_SIZE);
		
			gl.glVertex3f(-0.5f * F_SIZE, -1.5f, -0.5f * F_SIZE);
			gl.glVertex3f( 8.5f * F_SIZE, -1.5f, -0.5f * F_SIZE);
			gl.glVertex3f( 8.5f * F_SIZE, -1.5f, 0.5f * F_SIZE);
			gl.glVertex3f( -0.5f * F_SIZE, -1.5f, 0.5f * F_SIZE);
			
			gl.glVertex3f(-0.5f * F_SIZE, -1f, -0.5f * F_SIZE);
			gl.glVertex3f( 8.5f * F_SIZE, -1f, -0.5f * F_SIZE);
			gl.glVertex3f( 8.5f * F_SIZE, -1.5f, -0.5f * F_SIZE);
			gl.glVertex3f( -0.5f * F_SIZE, -1.5f, -0.5f * F_SIZE);
			
			gl.glVertex3f(-0.5f * F_SIZE, -1f, 0.5f * F_SIZE);
			gl.glVertex3f( 8.5f * F_SIZE, -1f, 0.5f * F_SIZE);
			gl.glVertex3f( 8.5f * F_SIZE, -1.5f, 0.5f * F_SIZE);
			gl.glVertex3f( -0.5f * F_SIZE, -1.5f, 0.5f * F_SIZE);
		gl.glEnd();
		gl.glPopMatrix();
		
	}

	public void displayBalls(){
		for (int i = 0; i < ROWS; ++i){
			for (int j = 0; j < COLS; ++j){
				Field f = (Field)(g.getBoard().getField(i, j));
				if (f.isEmpty())
					continue;
				gl.glPushMatrix();
				if (f.getState().equalsTo(Field.FieldState.Red)){
					gl.glColor3f(1.0f, 0.0f, 0.0f);
				}
				else{
					gl.glColor3f(0.0f, 0.0f, 1.0f);
				}
				gl.glTranslatef(f.getCol()* F_SIZE, f.getRow()* F_SIZE, 0.0f);
				glut.glutSolidSphere(Ball.BALL_SIZE, BALL_SLICES, BALL_STACKS);
				gl.glPopMatrix();
			}
		}
	}
	
	@Override
	public void dispose(GLAutoDrawable drawable) {
		// TODO Auto-generated method stub
	}
	
	@Override	
	public void init(GLAutoDrawable drawable) {
		// TODO Auto-generated method stub
		
		
        GL2 gl = drawable.getGL().getGL2();
        
        float mat_specular[] = { 1.0f, 1.0f, 1.0f, 1.0f };
    	float mat_shininess[] = { 20.0f };
    	float light_position1[] = { -5.0f, -1.0f, 2.0f, +50.0f };
    	float light_position2[] = { 5.0f, 2.0f, 2.0f, -50.0f };
    	float white_light[] = { 1.0f, 1.0f, 1.0f, 1.0f };
    	
    	
        gl.glShadeModel(GL2.GL_SMOOTH);              // Enable Smooth Shading        
        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.5f);    // Black Background
        gl.glClearDepth(1.0f);                      // Depth Buffer Setup
        gl.glEnable(GL2.GL_DEPTH_TEST);							// Enables Depth Testing
        gl.glDepthFunc(GL2.GL_LEQUAL);								// The Type Of Depth Testing To Do
        gl.glHint(GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL2.GL_NICEST);	// Really Nice Perspective Calculations       

    	gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
    	gl.glClearAccum(0.0f, 0.0f, 0.0f, 1.0f);
    	gl.glShadeModel(GL2.GL_SMOOTH);

    	gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, mat_specular, 0);
    	gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SHININESS, mat_shininess, 0);
    	
    	gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, light_position1, 0);
    	gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, white_light, 0);
    	gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPECULAR,  white_light, 0); 
    	
    	gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_POSITION, light_position2, 0);
    	gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_DIFFUSE, white_light, 0);
    	gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_SPECULAR,  white_light, 0);

    	gl.glEnable(GL2.GL_LIGHTING);
    	gl.glEnable(GL2.GL_LIGHT0);
    	gl.glEnable(GL2.GL_LIGHT1);
    	gl.glEnable(GL2.GL_DEPTH_TEST);
    	gl.glEnable(GL2.GL_COLOR_MATERIAL);
    	
    	//makeRasterFont();
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		// TODO Auto-generated method stub		
		GL2 gl = drawable.getGL().getGL2();
	    if (height <= 0) // avoid a divide by zero error!
	    	height = 1;
	    final float h = (float)width / (float)height;
	    gl.glViewport(0, 0, width, height);
	    gl.glMatrixMode(GL2.GL_PROJECTION);
	    gl.glLoadIdentity();
	    //glu.gluPerspective(45.0f, h, 1.0, 20.0);
	    gl.glOrtho(-16.0, 16.0, -16.0, 16.0, -16.0, 16.0);
	    winWidth = width;
	    winHeight = height;
	    gl.glMatrixMode(GL2.GL_MODELVIEW);
	    gl.glLoadIdentity();
	}	

	@Override
	public void keyPressed(KeyEvent e){
		switch (e.getKeyCode()){
			case KeyEvent.VK_R:
				g = new Game(Game.Player.Blue);				
				//animator.start();
				break;
			case KeyEvent.VK_1:
				aDrop = true;
				drop(1);
				break;
			case KeyEvent.VK_2:
				aDrop = true;
				drop(2);
				break;
			case KeyEvent.VK_3:
				aDrop = true;
				drop(3);
				break;
			case KeyEvent.VK_4:
				aDrop = true;
				drop(4);
				break;
			case KeyEvent.VK_5:
				aDrop = true;
				drop(5);
				break;
			case KeyEvent.VK_6:
				aDrop = true;
				drop(6);
				break;
			case KeyEvent.VK_7:
				aDrop = true;
				drop(7);
				break;
			case KeyEvent.VK_8:
				aDrop = true;
				drop(8);
				break;
			case KeyEvent.VK_9:
				aDrop = true;
				drop(9);
				break;
		}
	}	
		
	public void drop (int c){
		if (g.isGameOver())
			return;
		if (col == NOT_VALID) {
			col = c-1;
			animator.start();
			animateDrop();			
			return;
		}	
		row = g.getBoard().lowestEmptyRowIndex(col);

		if (row == ROWS) {
			col = NOT_VALID;
			return;
		}

		aDrop = true;
		dbHeight = ROWS;
	}

	public void animateDrop(){
		if (!aDrop){
			animator.pause();		
			return;
		}			
		if (dbHeight <= row){
			aDrop = false;
			g.playerMove(col);			
			col = NOT_VALID;
		}
		dbHeight -= 0.5;
		
		if(animator.isPaused())
			animator.resume();
		else
			animator.start();		
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub		
	}
	
	private void trackball_ptov(int x, int y, int width, int height, float [] v)
    {
        double d, a;    	
    	x = x - (width / 2);    	
    	v[0] = (x * 2.0F) / width;   
    	y = y - (height / 2);
    	v[1] = (2.0F * y) / height;    
        
    	d = Math.sqrt(v[0]*v[0] + v[1]*v[1]);

    	v[2] = (float) Math.cos((Math.PI/2.0) * ((d < 1.0) ? d 	: 1.0));
    	
    	a = 1.0 / Math.sqrt(v[0]*v[0] + v[1]*v[1] + v[2]*v[2]);
        v[0] *= a;    v[1] *= a;    v[2] *= a;
    }

	private void startMotion(int x, int y)
	{
		trackingMouse = true;
		redrawContinue = false;
		startX = x;
		startY = y;
		curx = x;
		cury = y;
		trackball_ptov(x, y, winWidth, winHeight, lastPos);
		trackballMove=true;	
		if(animator.isPaused())
			animator.resume();
		else
			animator.start();
	}
	
	private void stopMotion(int x, int y)
	{
		trackingMouse = false;
		/* check if position has changed */
	    if (startX != x || startY != y)
		    redrawContinue = true;
	    else 
	    {
			angle = 0.0f;
		    redrawContinue = false;
		    trackballMove = false;
	    }	
	    animator.stop();
	}
		
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		if(e.getButton() == MouseEvent.BUTTON1)
		{
			int y = winHeight-e.getY();
			int x = e.getX();
			startMotion(x,y);
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		   stopMotion(e.getX(),e.getY());
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		float [] curPos = new float[3];
		double dx, dy, dz;
		/* compute position on hemisphere */			

		trackball_ptov(e.getX(), winHeight - e.getY(), winWidth, winHeight, curPos);

		if(trackingMouse){
			/* compute the change in position on the hemisphere */
			dx = curPos[0] - lastPos[0];
			dy = curPos[1] - lastPos[1];
			dz = curPos[2] - lastPos[2];
			if (dx != 0.0f  || dy != 0.0f || dz != 0.0f) {

				angle = (float) (45.0 * Math.sqrt(dx*dx + dy*dy + dz*dz));

				axis[0] = lastPos[1]*curPos[2] - lastPos[2]*curPos[1];
				axis[1] = lastPos[2]*curPos[0] - lastPos[0]*curPos[2];
				axis[2] = lastPos[0]*curPos[1] - lastPos[1]*curPos[0];

				/* update position */
				lastPos[0] = curPos[0];
				lastPos[1] = curPos[1];
				lastPos[2] = curPos[2];
			}
		}   			
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub		
	}	
}

