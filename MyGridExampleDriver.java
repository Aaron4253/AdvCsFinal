//d oberle, 2020
//Graphics demo with mouse and sound where there is a board and pieces on the board that can be picked up and moved
   import javax.swing.JFrame;
   import java.awt.event.KeyListener;
   import java.awt.event.KeyEvent;

   public class MyGridExampleDriver							//Driver Program
   {
      public static MyGridExample screen;					//Game window
   
   
      public static void main(String[]args)
      {
         screen = new MyGridExample();
         JFrame frame = new JFrame("Vegetation vs undead");	//window title
         frame.setSize(616, 439);					//Size of game window
         frame.setLocation(100, 50);				//location of game window on the screen
         frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         frame.setContentPane(screen);		
         frame.setVisible(true);
         frame.addKeyListener(new listen());		//Get input from the keyboard
      
      }
   
      public static class listen implements KeyListener 
      { 
         public void keyTyped(KeyEvent e)
         { }
      
         public void keyPressed(KeyEvent e)
         { }
      
         public void keyReleased(KeyEvent e)
         {
            screen.processUserInput(e.getKeyCode());
         }
      }
   
   }
