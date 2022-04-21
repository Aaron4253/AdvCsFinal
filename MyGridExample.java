//d oberle, 2020
//Graphics demo with mouse and sound where there is a board and pieces on the board that can be picked up and moved
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.TimeUnit;
//the color for slowed zombies is #107ad1
public class MyGridExample extends JPanel implements MouseListener, MouseMotionListener
{
   //boolean t = true;
   private ImageIcon black = new ImageIcon("graphics/blackPiece.gif");//change these to plants.
   private ImageIcon white = new ImageIcon("graphics/whitePiece.gif");
   private ImageIcon blueTile = new ImageIcon("graphics/images_1.jpg");
   private ImageIcon peaShooter = new ImageIcon("graphics/peaShooter.jpg");
   private ImageIcon peaShooter1 = new ImageIcon("graphics/peaShooter2.gif");
   private ImageIcon iceShooter = new ImageIcon("graphics/iceShooter.gif");
   private ImageIcon iceShooter1 = new ImageIcon("graphics/iceShooter1.gif");
   private ImageIcon wallNut = new ImageIcon("graphics/wallNut.jpg");
   private ImageIcon wallNut1 = new ImageIcon("graphics/wallNut1.gif");
   private ImageIcon redTile = new ImageIcon("graphics/images_1.jpg");      //JPG images can not have transparency
   private ImageIcon crossHair = new ImageIcon("graphics/crossHair.GIF");	//GIF immages can have transparency
   private ImageIcon zombie = new ImageIcon("graphics/zombie2.gif");	
   private ImageIcon iceZombie = new ImageIcon("graphics/iceZombie.gif");	
   private ImageIcon zombieEating = new ImageIcon("graphics/zombieEating.gif");	
   private ImageIcon iceZombieEating = new ImageIcon("graphics/iceZombieEating.gif");	


   private static final int SIZE=60;	//size of cell being drawn
 
   //This array will be represented graphically on the screen
   private static int[][] board,       //the background 0-blueTile, 1-redTile 
                          pieces;	   //the pieces 0-none, 1-black chip, 2-white chip
   private static Timer timer;
   private static plant[][] plantBoard;  
   private static plant[] buyMenu;  
   private static ArrayList<projectile> projectiles;
   private static ArrayList<zombie> zombies;   
   private static int playerR;			//start row for the player selection tool
   private static int playerC;			//start col for the player selection tool
   private static int selected;        //value of the piece selected (0-none, 1-black chip, 2-white chip)
   private static plant selectedPlant; //holds onto the plant Object that is selected (backend)
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
      buyMenu = new plant[4]; // can change this value as more types of plants are added
      buyMenu[1] = new plant(0, 0, 3000, 0, 0);//wallnut is 1
      buyMenu[2] = new plant(0, 0, 50, 20, 300);//peashooter is 2
      buyMenu[3] = new plant(0, 0, 50, 25, 300);//iceShooter is 3
      //plant logic ends here
      //projectile logic begins here
      projectiles = new ArrayList<projectile>();
      //projectile logic ends here
      //zombie logic begins here
      zombies = new ArrayList<zombie>();
      //zombie logic ends here
      selected = 0; //holds onto the image of the plant selected
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
      g.drawImage(peaShooter.getImage(), 20, 20, SIZE, SIZE, null); // go to line  if(mouseC == 0 && mouseR == 0){ around 173
      g.drawImage(wallNut.getImage(), 100, 20, SIZE, SIZE, null);
      g.drawImage(iceShooter.getImage(), 180, 20, SIZE, SIZE, null);

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
            else if(pieces[r][c]==3)
               g.drawImage(iceShooter1.getImage(), x, y, SIZE, SIZE, null);            
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
         if(proj.getColor().equals("blue")){//checks if a projectile is shot from the iceShooter
               g.setColor(Color.blue);//projectile color
            if(proj != null){
               g.fillOval((int)(proj.getX()*SIZE + SIZE), proj.getY()*SIZE + SIZE + 50, 15, 15);
            }
         }else{
               g.setColor(Color.green);//projectile color
            if(proj != null){
               g.fillOval((int)(proj.getX()*SIZE + SIZE), proj.getY()*SIZE + SIZE + 50, 15, 15);
            }
         }
         
      }

      for(zombie z : zombies){
         if(z != null){
            if(z.getMovementSpeed() == 0.0002 && z.isEating()){//checks if the zombie is considered slowed
               g.drawImage(iceZombieEating.getImage(), (int)(z.getX()*SIZE + SIZE) - 40, z.getY()*SIZE + SIZE +20, SIZE, SIZE, null); 
               //creates the image of a slowed zombie
            }else if(z.getMovementSpeed() == 0.0002){
               g.drawImage(iceZombie.getImage(), (int)(z.getX()*SIZE + SIZE) - 40, z.getY()*SIZE + SIZE +20, SIZE, SIZE, null); 
            }else if(z.isEating()){
               g.drawImage(zombieEating.getImage(), (int)(z.getX()*SIZE + SIZE) - 40, z.getY()*SIZE + SIZE +20, SIZE, SIZE, null);  
            }else{
               g.drawImage(zombie.getImage(), (int)(z.getX()*SIZE + SIZE) - 40, z.getY()*SIZE + SIZE +20, SIZE, SIZE, null);  
               //draw zombie here
            }
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
      }
      if(selected == 3){
         g.drawImage(iceShooter1.getImage(), mouseX-SIZE/2, mouseY-SIZE/2, SIZE, SIZE, null);

      }
   }

   public void updateMoney(Graphics g){
      String moneyString = Integer.toString(money);      
      g.setFont(new Font("TimesRoman", Font.PLAIN, 25)); 
      g.setColor(Color.white);
      g.drawString("Money:" + moneyString, 450, 25);
      g.setFont(new Font("TimesRoman", Font.PLAIN, 20)); 
      g.drawString("$100", 20, 20);
      g.drawString("$50", 100, 20);
      g.drawString("$150", 180, 20);

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
         //for zombies
         boolean onePercentChance = (Math.random() < 0.005);
         if(onePercentChance){
            int random = (int)(Math. random()*(4-0+1))+0;
            int randomHealth = (int)(Math.random()*(500-1+1)) + 1;
            zombies.add(new zombie(9.2, random, randomHealth, 1));         
         }
         /*if(t){
            zombies.add(new zombie(10.0, 3, 50, 10));         
            t = false;
         }*/

         for(int index = 0; index < zombies.size(); index++){
            zombies.get(index).incrementX();
            //System.out.println("zombie x:" + zombies.get(index).getX() + "zombie y:" + zombies.get(index).getY());
            if(zombies.get(index).getX() < 0){
               System.out.println("The Zombies ate your brains");
               zombies.remove(index);
            }
         }

         //for projectiles
         for(int i = 0; i < projectiles.size(); i++){
            projectiles.get(i).incrementX();
            //System.out.println("projectile X:" + projectiles.get(i).getX() + "projectile Y:" + projectiles.get(i).getY());
            if(projectiles.get(i).getX() > 10.0){
               projectiles.remove(i);
            }
         }
         //for plants
         for(int i = 0; i < plantBoard.length; i++){
            for(int j = 0; j < plantBoard[0].length; j++){
               if(plantBoard[i][j] != null){
                  plantBoard[i][j].incrementFrame();//increments all plant's frames in plantBoard by 1
                  projectile temp = plantBoard[i][j].shoot(); 
                  if(temp != null && temp.getDamage() != 0){//wall nuts or plants that do not do damage are not given a projectile.
                     for(zombie z : zombies){//checks if a zombie is in the same row as plant, then the plant will shoot.
                        if(z.getY() == i){
                           if(plantBoard[i][j].getDamage() == 25){//this damage is unqiue to iceShooter
                              temp.setColor("blue");
                              //the projectile color is set to blue, all zombies hit by it will have their ms slowed
                           }
                           projectiles.add(temp);
                        }
                     }
                     //System.out.println("added a projectile");
                  }
               }
            }
         }
         //to check if a projectile collides with a zombie
         for(int i = 0; i < zombies.size(); i++){            
            for(int j = 0; j < projectiles.size(); j++){
               if(Math.abs(zombies.get(i).getX() - projectiles.get(j).getX()) < 0.1 && zombies.get(i).getY() == projectiles.get(j).getY()){
                  zombies.get(i).deductHp(projectiles.get(j).getDamage());
                  if(projectiles.get(j).getColor().equals("blue")){
                     zombies.get(i).setMovementSpeed(0.0002);
                  }
                  if(zombies.get(i).getHealth() <= 0){//check if a zombie died. If so, zombies is removed 
                     zombies.remove(i);
                     money += 50;
                     //System.out.println("zombie has been removed");
                  }
                  projectiles.remove(j);//projectile is removed regardless.
                  //System.out.println("projectile has been removed");
               }
            }
         }
         //check if zombie is ontop of a plant
         for(int i = 0; i < plantBoard.length; i++){
            for(int j = 0; j < plantBoard[0].length; j++){
               for(int k = 0; k < zombies.size(); k++){
                  if(plantBoard[i][j] == null && zombies.get(k).isEating() && zombies.get(k).getY() == i && Math.abs(zombies.get(k).getX() - j) < 0.1) zombies.get(k).setIsEating(false);
                  if(plantBoard[i][j] != null){
                     if(zombies.get(k).getY() == i && Math.abs(zombies.get(k).getX() - j) < 0.1){//if a zombie overlaps a plant
                        zombies.get(k).setIsEating(true);//zombie is then halted to eat the plant
                        plantBoard[i][j].deductHp(zombies.get(k).getDamage());//zombie inflicts damage each frame
                        if(plantBoard[i][j].getHealth() <= 0){//remove the plant if its HP is reduced under 0
                           zombies.get(k).setIsEating(false);
                           plantBoard[i][j] = null;
                           pieces[i][j] = 0;
                        }
                     }
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
            //System.out.println(mouseC + " " + mouseR);
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
            else if(mouseC == 3 && mouseR == 0){//for iceShooter
               if(money >= 150){
                  selected = 3;
                  selectedPlant = buyMenu[3].copy();
                  money -= 150;
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
