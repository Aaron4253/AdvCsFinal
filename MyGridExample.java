//d oberle, 2020
//Graphics demo with mouse and sound where there is a board and pieces on the board that can be picked up and moved
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class MyGridExample extends JPanel implements MouseListener, MouseMotionListener
{
   private ImageIcon black = new ImageIcon("graphics/blackPiece.gif");//change these to plants.
   private ImageIcon white = new ImageIcon("graphics/whitePiece.gif");
   private ImageIcon blueTile = new ImageIcon("graphics/images_1.jpg");
   private ImageIcon peaShooter = new ImageIcon("graphics/peaShooter.jpg");
   private ImageIcon peaShooter1 = new ImageIcon("graphics/peaShooter2.gif");
   private ImageIcon wallNut = new ImageIcon("graphics/wallNut.jpg");
   private ImageIcon wallNut1 = new ImageIcon("graphics/wallNut1.gif");
   private ImageIcon redTile = new ImageIcon("graphics/images_1.jpg");      //JPG images can not have transparency
   private ImageIcon crossHair = new ImageIcon("graphics/crossHair.GIF");	//GIF immages can have transparency

   private static final int SIZE=60;	//size of cell being drawn
 
   //This array will be represented graphically on the screen
   private static int[][] board,       //the background 0-blueTile, 1-redTile 
                          pieces;	   //the pieces 0-none, 1-black chip, 2-white chip
   private static Timer timer;
   private static plant[][] plantBoard;  
   private static plant[] buyMenu;  
   private static ArrayList<projectile> projectiles;
   private static int playerR;			//start row for the player selection tool
   private static int playerC;			//start col for the player selection tool
   private static int selected;        //value of the piece selected (0-none, 1-black chip, 2-white chip)
   private static plant selectedPlant;
   protected static int mouseX;			//locations for the mouse pointer
   protected static int mouseY;
   private int money;
   public MyGridExample()
   {
      addMouseListener( this );
      addMouseMotionListener( this );
      mouseX = 0;
      mouseY = 0;
      money = 1000;
      board = new int[5][10];      //background
      pieces = new int[5][10];     //pieces that go on top of the background
      //timer 
      timer = new Timer(0, new Listener());
      
      //
      //plant logic starts here
      plantBoard = new plant[5][10];//new plant board to control the backend
      buyMenu = new plant[3]; // can change this value as more types of plants are added
      buyMenu[2] = new plant(0, 0, 50, 20, 300);//peashooter is 2
      buyMenu[1] = new plant(0, 0, 350, 0, 0);//wallnut is 1
      //plant logic ends here
      //projectile logic begins here
      projectiles = new ArrayList<projectile>();
      //projectile logic ends here
      selected = 0;
      int nextValue = 0;            //to assign alternating values to the board (0,1)
      for(int r=0;r<board.length;r++)	
      {
         for(int c=0;c<board[0].length;c++)
         {
            board[r][c] = nextValue;
            nextValue = (nextValue+1)%2;           //alternate between 0 and 1
           /* if(Math.random() < 0.25)               //put some random pieces on the board
               pieces[r][c] = (int)(Math.random()*3);
               */
         }
         nextValue = (nextValue+1)%2;
      }
     
      playerR = board.length/2;							//start player position in the middle
      playerC = board[0].length/2;	
      Sound.initialize();
      timer.start();
   }


	//post:  shows different pictures on the screen in grid format depending on the values stored in the array board
	//			0-blueTile, 1-redTile
   //       shows pieces on the board and the crosshair
   //       0-no piece, 1-black chip, 2-white chip
   public void showBoard(Graphics g)	
   {
      g.drawImage(peaShooter.getImage(), 20, 20, SIZE, SIZE, null);  // go to line  if(mouseC == 0 && mouseR == 0){ around 173
      g.drawImage(wallNut.getImage(), 100, 20, SIZE, SIZE, null);
      int x =0, y = 100;	//if y is ever changed, go to int mouseR = ((mouseY-100)/SIZE); and change its value  //upper left corner location of where image will be drawn
      for(int r=0;r<board.length;r++)
      {
         x = 0;						            //reset the row distance
         for(int c=0;c<board[0].length;c++)
         {
            if(board[r][c]==0)               //draw the tile background
               g.drawImage(blueTile.getImage(), x, y, SIZE, SIZE, null);  
            else //if(board[r][c]==1)
               g.drawImage(redTile.getImage(), x, y, SIZE, SIZE, null);  
               
            if(pieces[r][c]==1)              //draw the player chips
               g.drawImage(wallNut1.getImage(), x, y, SIZE, SIZE, null);  
            else if(pieces[r][c]==2)
               g.drawImage(peaShooter1.getImage(), x, y, SIZE, SIZE, null);  
             
            /*if(r==playerR && c==playerC)	   //draw the crosshair on the board after the cell has been drawn
            {
               if(selected == 0)             //no piece has been selected
                  g.drawImage(crossHair.getImage(), x, y, SIZE, SIZE, null);  
               else if(selected==1)          //black chip selected
                  g.drawImage(black.getImage(), x, y, SIZE, SIZE, null);  
               else //if(selected==2)        //white chip selected
                  g.drawImage(white.getImage(), x, y, SIZE, SIZE, null);  
             }*/

            x+=SIZE;
         }
         y+=SIZE;
      }
      for(projectile proj : projectiles){
         g.setColor(Color.black);
         if(proj != null){
            g.fillOval((int)(proj.getX()*SIZE + SIZE), proj.getY()*SIZE + SIZE  + 50, 15, 15);
         }
      }
      updateMouse(g);
      updateMoney(g);
   }
   //updating methods
   public void updateMouse(Graphics g){
      if(selected == 2){
         g.drawImage(peaShooter1.getImage(), mouseX-SIZE/2, mouseY-SIZE/2, SIZE, SIZE, null);
      } 
      if(selected == 1){
         g.drawImage(wallNut1.getImage(), mouseX-SIZE/2, mouseY-SIZE/2, SIZE, SIZE, null);
      }else{

      }
   }

   public void updateMoney(Graphics g){
      String moneyString = Integer.toString(money);
      
      g.setFont(new Font("TimesRoman", Font.PLAIN, 40)); 
      g.setColor(Color.white);
      g.drawString("Money:" +moneyString, 350, 50);
      g.setFont(new Font("TimesRoman", Font.PLAIN, 20)); 
      g.drawString("$100", 20, 30);
      g.drawString("$50", 100, 30);

   }

   

	//THIS METHOD IS ONLY CALLED THE MOMENT A KEY IS HIT - NOT AT ANY OTHER TIME
	//pre:   k is a valid keyCode
	//post:  changes the players position depending on the key that was pressed (sent from the driver)
	//			keeps the player in the bounds of the size of the array board
   public void processUserInput(int k)
   {
      if(k==KeyEvent.VK_Q || k==KeyEvent.VK_ESCAPE)					//End the program	
         System.exit(1);
      if(k==KeyEvent.VK_UP && playerR>0)
         playerR--;
      else 
         if(k==KeyEvent.VK_DOWN && playerR<board.length-1)
            playerR++;
         else
            if(k==KeyEvent.VK_LEFT && playerC>0)
               playerC--;
            else 
               if(k==KeyEvent.VK_RIGHT && playerC<board[0].length-1)
                  playerC++;
               else 
                  if(k==KeyEvent.VK_SPACE)	//try to pick up or place a piece
                  {
                     if (selected == 0 && pieces[playerR][playerC] != 0)
                     {                       //pick up a piece that is there
                        selected = pieces[playerR][playerC];
                        pieces[playerR][playerC] = 0;
                        Sound.click();
                     }
                     else if (selected != 0 && pieces[playerR][playerC] == 0)
                     {                       //put down a piece you selected
                        pieces[playerR][playerC] = selected;
                        selected = 0;
                        Sound.click();
                     }
                     else                    //illegal move
                     {
                        Sound.randomNote();
                     }
                  }
      repaint();			//refresh the screen
   }

   public void paintComponent(Graphics g)
   {
      super.paintComponent(g); 
      g.setColor(Color.gray);		//draw a gray boarder around the board
      g.fillRect(0, 0, (board[0].length*SIZE), (board.length*SIZE));
      showBoard(g);					//draw the contents of the array board on the screen
   }
   
	 //***BEGIN MOUSE STUFF***
   private class Listener implements ActionListener
   {
      public void actionPerformed(ActionEvent e)	//this is called for each timer iteration
      {
         for(int i = 0; i < projectiles.size(); i++){
            projectiles.get(i).incrementX();
            if(projectiles.get(i).getX() > 10.0){
               projectiles.remove(i);
            }
         }

         for(int i = 0; i < plantBoard.length; i++){
            for(int j = 0; j < plantBoard[0].length; j++){
               if(plantBoard[i][j] != null){
                  plantBoard[i][j].incrementFrame();//increments all plant's frams in plantBoard by 1
                  projectile temp = plantBoard[i][j].shoot(); 
                  if(temp != null && temp.getDamage() != 0){
                     projectiles.add(temp);
                     //System.out.println("added a projectile");
                  }
               }
            }
         }
         repaint();
      }
   }

   public void mouseClicked( MouseEvent e )
   {
      int button = e.getButton();
      if(button == MouseEvent.BUTTON1 || button == MouseEvent.BUTTON3)
      {
         if(mouseX < board[0].length*SIZE && mouseY < 100){
            int mouseC = (mouseX/SIZE);
            int mouseR = (mouseY/SIZE);
            if(mouseC == 0 && mouseR == 0){//for peashooter
               if(money >= 100){//cost
                  selected = 2;
                  selectedPlant = buyMenu[2].copy(); 
                  money -= 100;
                  Sound.click();                  
               }else{
                  System.out.println("Not enough money");                  
               }
               repaint();
            }
            else if(mouseC == 2 && mouseR == 0){//for wallnut
               if(money >= 50){//cost
                  selected = 1;
                  selectedPlant = buyMenu[1].copy(); 
                  money -= 50;
                  Sound.click();
               }else{
                  System.out.println("Not enough money");
               }
               repaint();
            }
            //mouseC is the plant index
            //make an array of plants
            //each index of the array of plants is represented by a tile, if a mouse is inbetween a certain bound and pressed, select that plant and convert mouse crosshair to plant image.
            //
         }else{
         int mouseR = ((mouseY-100)/SIZE);
         int mouseC = (mouseX/SIZE);
         
         if(mouseR >=0 && mouseC >= 0 && mouseR < board.length && mouseC < board[0].length)
         {     //if the mouse is in bounds of the board
            playerR = mouseR;
            playerC = mouseC;
            if (selected == 0 && pieces[playerR][playerC] == 0)//this probably doesnt matter
            {  //pick up a piece that is there               
               selected = pieces[playerR][playerC];
               if(selected == 2 && money >= 100){
                  money -= 100;
                  pieces[playerR][playerC] = 0;
                  Sound.click();
                  
               } else if(selected == 1 && money >= 50){
                  money -= 50;
                  pieces[playerR][playerC] = 0;
                  Sound.click();
               }
               
            }//the above probably doesnt matter idk.
            else if (selected != 0 && pieces[playerR][playerC] == 0 && selectedPlant != null)//to put stuff down
            {  //put down a piece you selected
               pieces[playerR][playerC] = selected;
               selectedPlant.setX(playerC);
               selectedPlant.setY(playerR);
               plantBoard[playerR][playerC] = selectedPlant;
               selected = 0;
               selectedPlant = null;
               Sound.click();
            }
            else//illegal move
            {
               Sound.randomNote();
            }
         }
         else
         {     //reset the player into the center
            playerR = board.length/2;
            playerC = board[0].length/2;
         }
      
      } 
   }
      repaint();
   }

   public void mousePressed( MouseEvent e )
   {}

   public void mouseReleased( MouseEvent e )
   {}

   public void mouseEntered( MouseEvent e )
   {}

   public void mouseMoved( MouseEvent e)
   {
      mouseX = e.getX();
      mouseY = e.getY();
      
      int mouseR = (mouseY/SIZE);
      int mouseC = (mouseX/SIZE);
     // System.out.println(mouseR+":"+mouseC);
      if(mouseR >=0 && mouseC >= 0 && mouseR < board.length && mouseC < board[0].length)
      {
         playerR = mouseR;
         playerC = mouseC;
      }
      else
      {
         playerR = board.length/2;
         playerC = board[0].length/2;
      
      }
      repaint();			//refresh the screen
   }

   public void mouseDragged( MouseEvent e)
   {}

   public void mouseExited( MouseEvent e )
   {}

}