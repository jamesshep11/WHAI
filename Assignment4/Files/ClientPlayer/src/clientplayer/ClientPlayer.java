/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientplayer;

import java.util.GregorianCalendar;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
/**
 *
 * @author MC
 */
public class ClientPlayer {

    /**
     * @param args the command line arguments
     */
    
    public static void main(String[] args) {
        
    /*  int bs = 7;
     BanditPlayer mc = new BanditPlayer(bs);
     GregorianCalendar now = new GregorianCalendar();
     
        long timenow = now.getTimeInMillis();
        System.out.println(mc.MakeMove(new BoardDataStructure(bs).GetBoardString()));
        
     now = new GregorianCalendar();
     long timeafter = now.getTimeInMillis(); 
     
        System.out.println(timeafter-timenow);
     
     
    } 
     */
      
        Random r = new Random();
        String server = "";
        int port = 0;
        
        GregorianCalendar now = new GregorianCalendar();
        String timenow = now.getTime().toString();
        
        String[] options = {"Local", "Other"};
        //Integer[] options = {1, 3, 5, 7, 9, 11};
        //Double[] options = {3.141, 1.618};
        //Character[] options = {'a', 'b', 'c', 'd'};
        int x = JOptionPane.showOptionDialog(null, "Local or Other Server",
                "Select Server",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
        if(x == 1)
        {
            server = JOptionPane.showInputDialog("Enter server name");
            port = Integer.parseInt(JOptionPane.showInputDialog("Enter port number"));
        }
        
        options = new String[] {"Single", "Tournament"};
        //Integer[] options = {1, 3, 5, 7, 9, 11};
        //Double[] options = {3.141, 1.618};
        //Character[] options = {'a', 'b', 'c', 'd'};
        int y = JOptionPane.showOptionDialog(null, "Single Game or Tournament Match",
                "Single of Tournament",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
        
        if(y == 0)
        {
            y = 1;
        }
        else y = 8;
        
        options = new String[] {"Random", "Human", "MonteCarlo", "MiniMax", "Bandit"};
        //Integer[] options = {1, 3, 5, 7, 9, 11};
        //Double[] options = {3.141, 1.618};
        //Character[] options = {'a', 'b', 'c', 'd'};
         x = JOptionPane.showOptionDialog(null, "Please select a player",
                "Select Player",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
        String playername = "";
        
        
       
        for(int c = 0; c<y; c++)
        {
            if(x == 0)
            {   
                
                if(server.equals(""))
                {
                    new RandomPlayer("RandomPlayer " + timenow);
                }
                else new RandomPlayer(server, port, "RandomPlayer " + timenow);


            }
            else if(x==1)
            {
                if(playername.equals(""))
                {
                    playername = JOptionPane.showInputDialog("Enter player name");
                }
                if(server.equals(""))
                {
                    new HumanPlayer(playername);
                }
                else new HumanPlayer(server, port, playername);           
            }
            else if(x==2)
            {
               if(server.equals(""))
                {
                    new MonteCarloPlayer("MonteCarloPlayer " + timenow);
                }
                else new MonteCarloPlayer(server, port, "MonteCarloPlayer " + timenow);
            }
            else if(x==3)
            {
                if(server.equals(""))
                {
                    new MinMaxPlayer("MinMaxPlayer " + timenow);
                }
                else new MinMaxPlayer(server, port, "MinMaxPlayer " + timenow);
            }           
            else if(x==4)
            {
                if(server.equals(""))
                {
                    new RandomPlayer("RandomPlayer " + timenow);
                }
                else new RandomPlayer(server, port, "RandomPlayer " + timenow);          
            }
            
            
        }
    } 
    
}
