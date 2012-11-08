package pacman;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;


import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

@SuppressWarnings("serial")
public class Board extends JPanel implements ActionListener{

	Dimension d;
	Font smallfont = new Font("Helvetica", Font.BOLD, 14);
	
	FontMetrics fmsmall, fmlarge;
	Image ii;
	Color dotColor = new Color(192, 192, 0);
	Color mazeColor;
	
	boolean ingame = false;
	boolean dying = false;
	
	final int blockSize = 24;
	final int nrOfBlocks = 26;
	final int scrSize = nrOfBlocks * blockSize;
	final int pacAnimDelay = 2;
	final int pacmanAnimCount = 4;
	final int maxGhosts = 12;
	final int pacmanSpeed = 6;
	
	int pacAnimCount = pacAnimDelay;
	int pacAnimDir = 1;
	int pacmanAnimPos = 0;
	int nrOfGhosts = 5;
	int pacsLeft, score;
	int deathCounter =0;
	int currentLevel =1;
	int nrLevels = 2;
	int [] dx, dy;
	int [] ghostX, ghostY, ghostDx, ghostDy, ghostSpeed;
	
	Image ghost, pacman1MIR;
	Image pacman1, pacman2up,pacman2left,pacman2right,pacman2down;
	Image 		   pacman3up,pacman3left,pacman3right,pacman3down;
	Image		   pacman4up,pacman4left,pacman4right,pacman4down;
	
	int pacmanX, pacmanY, pacmanDx, pacmanDy;
	int reqDx, reqDy, viewDx, viewDy;
	
	//make up the maze
	//provide information on where corners are and points
	// 19 = top, left, and a point
	//	  = 16   + 2 	+ 1
	
	
	//1 left border
	//2 top border
	//4 right border
	//8 bottom border
	//16 point
    final short levelData[][] =
    {
	    { 19, 26, 26, 26, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 22,
	      21, 0,  0,  0,  17, 16, 16, 16, 16, 16, 16, 16, 16, 16, 20,
	      21, 0,  0,  0,  17, 16, 16, 16, 16, 16, 16, 16, 16, 16, 20, 
	      21, 0,  0,  0,  17, 16, 16, 24, 16, 16, 16, 16, 16, 16, 20, 
	      17, 18, 18, 18, 16, 16, 20, 0,  17, 16, 16, 16, 16, 16, 20,
	      17, 16, 16, 16, 16, 16, 20, 0,  17, 16, 16, 16, 16, 24, 20, 
	      25, 16, 16, 16, 24, 24, 28, 0,  25, 24, 24, 16, 20, 0,  21, 
	      1,  17, 16, 20, 0,  0,  0,  0,  0,  0,  0,  17, 20, 0,  21,
	      1,  17, 16, 16, 18, 18, 22, 0,  19, 18, 18, 16, 20, 0,  21,
	      1,  17, 16, 16, 16, 16, 20, 0,  17, 16, 16, 16, 20, 0,  21, 
	      1,  17, 16, 16, 16, 16, 20, 0,  17, 16, 16, 16, 20, 0,  21,
	      1,  17, 16, 16, 16, 16, 16, 18, 16, 16, 16, 16, 20, 0,  21,
	      1,  17, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 20, 0,  21,
	      1,  25, 24, 24, 24, 24, 24, 24, 24, 24, 16, 16, 16, 18, 20,
	      9,  8,  8,  8,  8,  8,  8,  8,  8,  8,  25, 24, 24, 24, 28 },
	    
	      {
	    	  19, 26, 26, 26, 26, 18, 26, 26, 26, 26, 26, 22, 1,  4,  19, 26, 26, 26, 26, 26, 18, 26, 26, 26, 26, 22, 
	    	  21, 3,  2,  2,  6,  21, 3,  2,  2,  2,  6,  21, 1,  4,  21, 3,  2,  2,  2,  6,  21, 3,  2,  2,  6,  21, 
	    	  21, 1,  0,  0,  4,  21, 1,  0,  0,  0,  4,  21, 1,  4,  21, 1,  0,  0,  0,  4,  21, 1,  0,  0,  4,  21, 
	    	  21, 9,  8,  8,  12, 21, 9,  8,  8,  8,  12, 21, 1,  4,  21, 9,  8,  8,  8,  12, 21, 9,  8,  8,  12, 21, 
	    	  17, 26, 26, 26, 26, 16, 26, 26, 18, 26, 26, 24, 26, 26, 24, 26, 26, 18, 26, 26, 16, 26, 26, 26, 26, 20, 
	    	  21, 3,  2,  2,  6,  21, 3,  6,  21, 3,  2,  2,  2,  2,  2,  2,  6,  21, 3,  6,  21, 3,  2,  2,  6,  21, 
	    	  21, 9,  8,  8,  12, 21, 1,  4,  21, 9,  8,  8,  0,  0,  8,  8,  12, 21, 1,  4,  21, 9,  8,  8,  12, 21, 
	    	  25, 26, 26, 26, 26, 20, 1,  4,  25, 26, 26, 22, 1,  4,  19, 26, 26, 28, 1,  4,  17, 26, 26, 26, 26, 28, 
	    	  2,  2,  2,  2,  6,  21, 1,  0,  2,  2,  6,  21, 1,  4,  21, 3,  2,  2,  0,  4,  21, 3,  2,  2,  2,  2,  
	    	  0,  0,  0,  0,  4,  21, 1,  8,  8,  8,  12, 21, 1,  4,  21, 9,  8,  8,  8,  4,  21, 1,  0,  0,  0,  0,  
	    	  0,  0,  0,  0,  4,  21, 5,  19, 26, 26, 26, 24, 26, 26, 24, 26, 26, 26, 22, 5,  21, 1,  0,  0,  0,  0,  
	    	  0,  0,  0,  0,  4,  21, 5,  21, 3,  2,  2,  2,  2,  2,  2,  2,  2,  6,  21, 5,  21, 1,  0,  0,  0,  0,  
	    	  0,  0,  0,  0,  4,  21, 5,  21, 1,  0,  0,  0,  0,  0,  0,  0,  0,  4,  21, 5,  21, 1,  0,  0,  0,  0,  
	    	  0,  0,  0,  0,  4,  21, 5,  21, 1,  0,  0,  0,  0,  0,  0,  0,  0,  4,  21, 5,  21, 1,  0,  0,  0,  0,  
	    	  0,  0,  0,  0,  4,  21, 5,  21, 9,  8,  8,  8,  8,  8,  8,  8,  8,  12, 21, 5,  21, 1,  0,  0,  0,  0,  
	    	  0,  0,  0,  0,  4,  21, 5,  25, 26, 26, 26, 18, 26, 26, 18, 26, 26, 26, 28, 5,  21, 1,  0,  0,  0,  0,  
	    	  0,  0,  0,  0,  4,  21, 1,  2,  2,  2,  6,  21, 1,  4,  21, 3,  2,  2,  2,  4,  21, 1,  0,  0,  0,  0,  
	    	  8,  8,  8,  8,  12, 21, 1,  0,  8,  8,  12, 21, 1,  4,  21, 9,  8,  8,  0,  4,  21, 9,  8,  8,  8,  8,  
	    	  19, 26, 26, 26, 26, 20, 1,  4,  19, 26, 26, 28, 1,  4,  25, 26, 26, 22, 1,  4,  17, 26, 26, 26, 26, 22, 
	    	  21, 3,  2,  2,  6,  21, 1,  4,  21, 3,  2,  2,  0,  0,  2,  2,  6,  21, 1,  4,  21, 3,  2,  2,  6,  21, 
	    	  21, 9,  8,  8,  12, 21, 9,  12, 21, 9,  8,  8,  8,  8,  8,  8,  12, 21, 9,  12, 21, 9,  8,  8,  12, 21, 
	    	  17, 26, 26, 26, 26, 16, 26, 26, 24, 26, 26, 18, 26, 26, 18, 26, 26, 24, 26, 26, 16, 26, 26, 26, 26, 20, 
	    	  21, 3,  2,  2,  6,  21, 3,  2,  2,  2,  6,  21, 1,  4,  21, 3,  2,  2,  2,  6,  21, 3,  2,  2,  6,  21, 
	    	  21, 1,  0,  0,  4,  21, 1,  0,  0,  0,  4,  21, 1,  4,  21, 1,  0,  0,  0,  4,  21, 1,  0,  0,  4,  21, 
	    	  21, 9,  8,  8,  12, 21, 9,  8,  8,  8,  12, 21, 1,  4,  21, 9,  8,  8,  8,  12, 21, 9,  8,  8,  12, 21, 
	    	  25, 26, 26, 26, 26, 24, 26, 26, 26, 26, 26, 28, 1,  4,  25, 26, 26, 26, 26, 26, 24, 26, 26, 26, 26, 28,   
	    	}
    };
    
    
    final int validSpeeds [] = { 1, 2, 3, 4, 6, 8 };
    final int maxSpeed = 6;
    
    int currentSpeed = 3;
    short screenData[];
    Timer timer;
    
    
    
    public Board(){
    	
    	getImages();
    	
    	addKeyListener(new TAdapter());
    	
    	screenData = new short [nrOfBlocks * nrOfBlocks];
    	//mazeColor = new Color(5, 100, 5);
    	mazeColor = new Color (85, 85, 255);
    	setFocusable(true);
    	
    	ghostX = new int[maxGhosts];
    	ghostDx = new int[maxGhosts];
    	ghostY = new int[maxGhosts];
    	ghostDy = new int [maxGhosts];
    	ghostSpeed = new int[maxGhosts];
    	dx = new int [4];
    	dy = new int [4];
    	timer = new Timer(40, this);
    	timer.start();
    }
    
    
    public void addNotify(){
    	super.addNotify();
    	gameInit();
    }
    
    
    // count pacmananimpos variable, which determines what pacman
    // image is drawn (there are 4 images in a direction)
    //
    // pacmandelay makes animation slower, otherwise pacman opens
    // mouth too quickly
    public void doAnim(){
    	pacAnimCount--;
    	if (pacAnimCount <= 0){
    		pacAnimCount = pacAnimDelay;
    		pacmanAnimPos = pacmanAnimPos + pacAnimDir;
    		if (pacmanAnimPos == (pacmanAnimCount - 1) || pacmanAnimPos ==0)
    			pacAnimDir = -pacAnimDir;
    	}
    }
    
    
    public void playGame(Graphics2D g2d){
    	if(dying){
    		death();
    	}else{
    		movePacMan();
    		drawPacMan(g2d);
    		moveGhosts(g2d);
    		checkMaze();
    	}
    		
    }
    
    
    public void showIntroScreen(Graphics2D g2d){
    	
    	g2d.setColor(new Color(0, 32, 48));
    	g2d.fillRect(50, scrSize/2 -30, scrSize -100, 50);
    	g2d.setColor(Color.white);
    	g2d.drawRect(50, scrSize/2 -30, scrSize -100, 50);
    	
    	String s = "Press s to start";
    	Font small = new Font("Helvetica", Font.BOLD, 14);
    	FontMetrics metr = this.getFontMetrics(small);
    	
    	g2d.setColor(Color.white);
    	g2d.setFont(small);
    	g2d.drawString(s, (scrSize - metr.stringWidth(s)) /2, scrSize/2);
    	
    }
    
    
    public void drawScore(Graphics2D g){
    	int i;
    	String s;
    	
    	g.setFont(smallfont);
    	g.setColor(new Color(96, 128, 255));
    	s = "Score: " + score;
    	g.drawString(s, scrSize/2 +96, scrSize + 16);
    	for(i=0; i<pacsLeft; i++){
    		g.drawImage(pacman4left, i*28 + 8, scrSize +1, this);
    	}
    }
    
    
    public void checkMaze(){
    	short i = 0;
    	boolean finished = true;
    	
    	//check if any points are left for pacman to eat. 16 = point
    	while (i< nrOfBlocks*nrOfBlocks && finished){
    		if ((screenData[i] & 16) != 0) finished = false;
    		i++;
    	}
    	
    	//if all points eaten, we finish level and go to next
    	if (finished){
    		score+=50;
    		
    		if(nrOfGhosts < maxGhosts) nrOfGhosts++;
    		if(currentSpeed < maxSpeed) currentSpeed++;
    		if(currentLevel < nrLevels-1) currentLevel++;
    		levelInit();    		
    	}
    	
    }
    
    
    public void death(){    	
    	pacsLeft--;
    	if(pacsLeft==0) {
    		ingame = false;
    		nrOfGhosts =5;
    	}
    	levelContinue();
    }
    
    /**
     * ghosts move one square and decide if to change direction
     * @param g2d
     */    
    public void moveGhosts(Graphics2D g2d){
    	short i;
    	int pos;
    	int count;
    	
    	for(i=0; i<nrOfGhosts; i++){
    		//continue only if you have finished moving one square
    		if(ghostX[i] % blockSize == 0 && ghostY[i] % blockSize ==0){
    			//determine position/square of the ghost. 
    			pos = ghostX[i] / blockSize + nrOfBlocks * (int)(ghostY[i]/blockSize);
    			    			
    			count =0;
    			
    			//entering a tunnel, ghost continues in same direction
    			//until he is out
    			
    			//no obstacle on left, ghost not moving to right
    			//ghost continue moving left
    			if((screenData[pos] & 1) == 0 && ghostDx[i] !=1){
    				dx[count] = -1;
    				dy[count] = 0;
    				count++;
    			}
    			//top
    			if((screenData[pos] & 2) == 0 && ghostDy[i] !=1){
    				dx[count] = 0;
    				dy[count] = -1;
    				count++;
    			}
    			//right
    			if((screenData[pos] & 4) == 0 && ghostDx[i] !=-1){
    				dx[count] = 1;
    				dy[count] = 0;
    				count++;
    			}
    			//bottom
    			if((screenData[pos] & 8) == 0 && ghostDy[i] !=-1){
    				dx[count] = 0;
    				dy[count] = 1;
    				count++;
    			}
    			
    			if (count==0){
    				if ((screenData[pos] & 15) == 15){
    					ghostDx[i] =0;
    					ghostDy[i] =0;
    				}else{
    					ghostDx[i] =-ghostDx[i];
    					ghostDy[i] =-ghostDy[i];
    				}
    			}else{
    				count = (int)(Math.random()*count);
    				if (count>3) count=3;
    				ghostDx[i] = dx[count];
    				ghostDy[i] = dy[count];
    			}    			
    		}
    		
    		ghostX[i] = ghostX[i] + (ghostDx[i] * ghostSpeed[i]);
    		ghostY[i] = ghostY[i] + (ghostDy[i] * ghostSpeed[i]);
    		drawGhost(g2d,ghostX[i] +1, ghostY[i] +1);
    		
    		//collision between pacman and ghost means pacman dies
    		if (pacmanX > (ghostX[i] -12) && pacmanX < (ghostX[i] +12) &&
    			pacmanY > (ghostY[i] -12) && pacmanY < (ghostY[i] +12) &&
    			ingame){
    			
    			dying = true;
    			deathCounter++;
    		}
    	}
    }

   
    
    public void drawGhost(Graphics2D g2d, int x, int y){
    	g2d.drawImage(ghost,x,y,this);
    }
    
    public void movePacMan(){
    	int pos;
    	short ch;
    	//reqDx and reqDy determined by keystrokes in TAdapter class
    	
    	if (reqDx == -pacmanDx && reqDy == -pacmanDy){
    		pacmanDx = reqDx;
    		pacmanDy = reqDy;
    		viewDx = pacmanDx;
    		viewDy = pacmanDy;
    	}
    	if (pacmanX % blockSize ==0 && pacmanY % blockSize ==0){
    		pos = pacmanX/blockSize + nrOfBlocks * (int)(pacmanY/blockSize);
    		ch = screenData[pos];    		
    		
    		//if pacman moves to a position with a point, remove it from maze
    		//increase score
    		if ((ch & 16) != 0){
    			screenData[pos] = (short)(ch & 15);
    			score++;
    		}
    		
    		if (reqDx !=0 || reqDy != 0){
    			if(!((reqDx == -1 && reqDy ==  0 && (ch & 1) !=0) ||
    				(reqDx ==  1 && reqDy ==  0 && (ch & 4) !=0) ||
    				(reqDx ==  0 && reqDy == -1 && (ch & 2) !=0) ||
    				(reqDx ==  0 && reqDy ==  1 && (ch & 8) !=0))){
    				pacmanDx = reqDx;
    				pacmanDy = reqDy;
    				viewDx = pacmanDx;
    				viewDy = pacmanDy;
    			}
    		}
    		
    		//Check for standstill
    		if ((pacmanDx == -1 && pacmanDy ==  0 && (ch & 1) !=0) ||
    			(pacmanDx ==  1 && pacmanDy ==  0 && (ch & 4) !=0) ||
    			(pacmanDx ==  0 && pacmanDy == -1 && (ch & 2) !=0) ||
    			(pacmanDx ==  0 && pacmanDy ==  1 && (ch & 8) !=0)){
				pacmanDx = 0;
				pacmanDy = 0;
			}
    	}
    	
    	pacmanX = pacmanX + pacmanSpeed*pacmanDx;
    	pacmanY = pacmanY + pacmanSpeed*pacmanDy;
    }
    
    
    /**
     * four possible directions for pacman to be drawn in
     * @param g2d
     */
    public void drawPacMan(Graphics2D g2d){
    	if (viewDx == -1) drawPacManLeft(g2d);
    	else if (viewDx == 1) drawPacManRight(g2d);
    	else if (viewDy == -1) drawPacManUp(g2d);
    	else drawPacManDown(g2d);
    }
        
    public void drawPacManUp(Graphics2D g2d){
    	switch (pacmanAnimPos){
    	case 1:
    		g2d.drawImage(pacman2up, pacmanX +1, pacmanY +1, this);
    		break;    	
    	case 2:
    		g2d.drawImage(pacman3up, pacmanX +1, pacmanY +1, this);
    		break;
    	case 3:
    		g2d.drawImage(pacman4up, pacmanX +1, pacmanY +1, this);
    		break;
    	default:
    		g2d.drawImage(pacman1, pacmanX +1, pacmanY +1, this);
    		break;
    	}
    }
 
    public void drawPacManDown(Graphics2D g2d){
    	switch (pacmanAnimPos){
    	case 1:
    		g2d.drawImage(pacman2down, pacmanX +1, pacmanY +1, this);
    		break;    	
    	case 2:
    		g2d.drawImage(pacman3down, pacmanX +1, pacmanY +1, this);
    		break;
    	case 3:
    		g2d.drawImage(pacman4down, pacmanX +1, pacmanY +1, this);
    		break;
    	default:
    		g2d.drawImage(pacman1, pacmanX +1, pacmanY +1, this);
    		break;
    	}
    }
        
    public void drawPacManLeft(Graphics2D g2d){
    	switch (pacmanAnimPos){
    	case 1:
    		g2d.drawImage(pacman2left, pacmanX +1, pacmanY +1, this);
    		break;    	
    	case 2:
    		g2d.drawImage(pacman3left, pacmanX +1, pacmanY +1, this);
    		break;
    	case 3:
    		g2d.drawImage(pacman4left, pacmanX +1, pacmanY +1, this);
    		break;
    	default:
    		g2d.drawImage(pacman1MIR, pacmanX +1, pacmanY +1, this);
    		break;
    	}
    }
    
    public void drawPacManRight(Graphics2D g2d){
    	switch (pacmanAnimPos){
    	case 1:
    		g2d.drawImage(pacman2right, pacmanX +1, pacmanY +1, this);
    		break;    	
    	case 2:
    		g2d.drawImage(pacman3right, pacmanX +1, pacmanY +1, this);
    		break;
    	case 3:
    		g2d.drawImage(pacman4right, pacmanX +1, pacmanY +1, this);
    		break;
    	default:
    		g2d.drawImage(pacman1, pacmanX +1, pacmanY +1, this);
    		break;
    	}
    }
    
    
    
    /**
     * draw maze using the numbers in the screenData[] array
     * @param g2d
     */
    public void drawMaze (Graphics2D g2d){
    	short i=0;
    	int x, y;
    	
    	for (y =0; y<scrSize; y+= blockSize){
    		for (x =0; x<scrSize; x+= blockSize){
	    		g2d.setColor(mazeColor);
	    		g2d.setStroke(new BasicStroke(2));
	    		
	    		//draw left
	    		if((screenData[i] & 1) !=0) g2d.drawLine(x, y, x, y + (blockSize -1));
	    		//draws top
	    		if((screenData[i] & 2) !=0) g2d.drawLine(x, y, x + (blockSize -1), y);
	    		//draw right
	    		if((screenData[i] & 4) !=0) g2d.drawLine(x + (blockSize -1), y, x + (blockSize -1), y + (blockSize -1));
	    		//draw bottom
	    		if((screenData[i] & 8) !=0) g2d.drawLine(x, y + (blockSize -1), x + (blockSize -1), y + (blockSize -1));
	    		//draw points
	    		if((screenData[i] & 16)!=0){
	    			g2d.setColor(dotColor);
	    			g2d.fillRect(x+11, y+11, 2, 2);
	    		}
	    		i++;
    		}
    	}
    }
    
    
    public void gameInit(){
    	pacsLeft = 3;
    	score = 0;
    	levelInit();
    	//nrOfGhosts = 6;
    	currentSpeed = 1;
    }
    
    public void levelInit(){
    	for(int i=0; i<nrOfBlocks*nrOfBlocks; i++){
    		screenData[i] = levelData[currentLevel][i];
    	}
    	
    	//Tile level1 [][] = PacmanLevelReader.pacLevelRead();
    	
    	levelContinue();
    }
    
    public void levelContinue(){
    	short i;
    	int dx =1;
    	int random;
    	
    	for (i=0; i<nrOfGhosts; i++){
    		ghostX[i] = 4*blockSize;
    		ghostY[i] = 4*blockSize;
    		ghostDx[i] = dx;
    		ghostDy[i] = 0;
    		dx = -dx;
    		random = (int)(Math.random()*(currentSpeed +1));    		
			if (random>currentSpeed) random = currentSpeed;
			ghostSpeed[i] = validSpeeds[random];
    	}
    	
    	pacmanX = 7*blockSize;
    	pacmanY = 11*blockSize;
    	pacmanDx = 0;
    	pacmanDy = 0;
    	reqDx = 0;
    	reqDy = 0;
    	viewDx = -1;
    	viewDy = 0;
    	dying = false;
    }
    
    
    public void getImages(){
    	
    	ghost = new ImageIcon(this.getClass().getResource("ghost.png")).getImage();	    	
    	pacman1 = new ImageIcon(this.getClass().getResource("pacman.png")).getImage();
    	pacman1MIR = new ImageIcon(this.getClass().getResource("pacmanmirror.png")).getImage();
    	pacman2up = new ImageIcon(this.getClass().getResource("up1.png")).getImage();
        pacman3up = new ImageIcon(this.getClass().getResource("up2.png")).getImage();
        pacman4up = new ImageIcon(this.getClass().getResource("up3.png")).getImage();
        pacman2down = new ImageIcon(this.getClass().getResource("down1.png")).getImage();
        pacman3down = new ImageIcon(this.getClass().getResource("down2.png")).getImage(); 
        pacman4down = new ImageIcon(this.getClass().getResource("down3.png")).getImage();
        pacman2left = new ImageIcon(this.getClass().getResource("left1.png")).getImage();
        pacman3left = new ImageIcon(this.getClass().getResource("left2.png")).getImage();
        pacman4left = new ImageIcon(this.getClass().getResource("left3.png")).getImage();
        pacman2right = new ImageIcon(this.getClass().getResource("right1.png")).getImage();
        pacman3right = new ImageIcon(this.getClass().getResource("right2.png")).getImage();
        pacman4right = new ImageIcon(this.getClass().getResource("right3.png")).getImage();
        
    }
    
   
    public void paint (Graphics g){
    	super.paint(g);    	

    	d = getSize();
    	Graphics2D g2d = (Graphics2D) g;

    	g2d.setColor(Color.black);    	
    	g2d.fillRect(0, 0, d.width, d.height);

    	drawMaze(g2d);
    	drawScore(g2d);
    	doAnim();
    	if(ingame) playGame(g2d);
    	else showIntroScreen(g2d);
    	
    	g.drawImage(ii, 5, 5, this);
    	Toolkit.getDefaultToolkit().sync();
    	g.dispose();
    }
			
    
    class TAdapter extends KeyAdapter {
    	public void keyPressed(KeyEvent e){
    		int key = e.getKeyCode();
    		
    		if(ingame){
    			if(key == KeyEvent.VK_LEFT){
    				reqDx=-1;
    				reqDy=0;
    			}
    			else if (key == KeyEvent.VK_RIGHT){
    				reqDx=1;
    				reqDy=0;
    			}
    			else if (key == KeyEvent.VK_UP){
    				reqDx=0;
    				reqDy=-1;
    			}
    			else if (key == KeyEvent.VK_DOWN){
    				reqDx=0;
    				reqDy=1;
    			}
    			else if (key == KeyEvent.VK_ESCAPE && timer.isRunning()){
    				ingame = false;
    			}
    			else if (key == KeyEvent.VK_P || key == KeyEvent.VK_PAUSE){
    				if (timer.isRunning()) timer.stop();
    				else timer.start();
    			}
    		}else{
    			if (key == 's' || key == 'S'){
    				ingame = true;
    				gameInit();
    			}
    		}
    	}
    	
    	public void keyReleased (KeyEvent e){
    		int key = e.getKeyCode();
    		
    		if (key == Event.LEFT || key == Event.RIGHT || key == Event.UP || key == Event.DOWN){
    			reqDx = 0;
    			reqDy = 0;
    		}
    	}
    }
    
   public void actionPerformed(ActionEvent e){
	   repaint();
   }
    
}
