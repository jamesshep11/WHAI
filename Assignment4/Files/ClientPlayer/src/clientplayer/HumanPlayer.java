/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientplayer;


import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;
import javax.imageio.ImageIO;
import java.net.*;
import javax.swing.JOptionPane;

/**
 *
 * @author MC
 */
public class HumanPlayer extends Frame implements Player{
    
    Socket connectToServer;
    
    BufferedReader isFromServer; 
    PrintWriter osToServer; 
    
    int BoardSize;
    int Side;
    int ResponseTime;
    
    String imname = "HexBoard.jpg";
    
    int RHCornerX = 642;
    int RHCornerY = 433;
    float HexHeight = 26.7f;
    float HexWidth = 30.7f;
    
    BoardDataStructure displayBDS;
    
    Image source1;    
    PicCanvas pc;
    
    int winner = BoardDataStructure.Empty;
    
    boolean thismove = false;
    String TheMove;
    
    int Port = 2001;
    String Server = "Local";
    String PlayerName = "HumanPlayer";
    
    /** Creates a new instance of DisplayFrame */
    public HumanPlayer()
    {
        this("Local", 2001, "HumanPlayer");
    }
    
    public HumanPlayer(String name)
    {
        this("Local", 2001, name);
    }
    
    public HumanPlayer(String server, int port, String name)
    {
        PlayerName = name;
        Server= server;
        Port = port;   
        
        GetConnection();
        displayBDS = new BoardDataStructure(BoardSize);
        source1 = Toolkit.getDefaultToolkit().getImage(imname);        
        
        
        
        while(source1.getWidth(null) < 0){System.out.println("Image Loading");}   
        setLayout(new BorderLayout());       
        pc = new PicCanvas();
        add("Center", pc);        

        pack();
        setTitle("Tybalt's Hex.....Waiting for opponent");
        setVisible(true);
        
        addWindowListener(new WindowAdapter()
			{       
                                
				public void windowClosing(WindowEvent e)
				{
                                    
					dispose();
				}
			});  
        
        boolean GameOver = false;       
        try
        {
            while(!GameOver)
            {
                String nextL = isFromServer.readLine();
                if(nextL.length() > 1)
                {
                    java.awt.Toolkit.getDefaultToolkit().beep();
                    osToServer.println(MakeMove(nextL));
                    osToServer.flush();
                }
                else
                {
                    System.out.println("Winner: " + nextL);
                    
                    winner = Integer.parseInt(nextL);
                    nextL = isFromServer.readLine();
                    MakeMove(nextL);
                    GameOver = true;
                }
            }
        }
        catch(Exception ioe)
        {
            ioe.printStackTrace();
        }     
    }

    @Override
    public void GetConnection() {
        try {
            if(Server.equals("Local"))
            {
                connectToServer = new Socket(InetAddress.getLocalHost(),Port);
            }
            else connectToServer = new Socket(Server,Port);
            isFromServer = new BufferedReader(new InputStreamReader(connectToServer.getInputStream()));
            osToServer =  new PrintWriter(connectToServer.getOutputStream());
            System.out.println("Connected");
            osToServer.println(PlayerName);
            osToServer.flush();
            String setup = isFromServer.readLine();
            System.out.println(setup);
            StringTokenizer st = new StringTokenizer(setup, ",");
            BoardSize = Integer.parseInt(st.nextToken());
            Side = Integer.parseInt(st.nextToken());
            ResponseTime = Integer.parseInt(st.nextToken());  
            
        } catch (IOException ex) {
            
        }        
    }

    @Override
    public String MakeMove(String board) 
    {
        
        TheMove = BoardSize+","+BoardSize;
        
        long starttime = new GregorianCalendar().getTimeInMillis();
        BoardDataStructure temp = new BoardDataStructure(BoardSize);
        StringTokenizer st = new StringTokenizer(board, ",");
        for(int r =0; r < BoardSize; r++)
        {
            for(int c = 0; c < BoardSize; c++)
            {
                temp.Board[c][r] = Integer.parseInt(st.nextToken());
            }
        }
        
        displayBDS = temp;
        
        pc.repaint();
        thismove = true;
        if(winner == BoardDataStructure.Empty)
        {
            long endtime = starttime;
            while((endtime - starttime < ResponseTime*1000)&&(TheMove.equals(BoardSize+","+BoardSize)))
            {
                System.out.println("Game Running");
                try
                {
                    long secondsleft = ((starttime + ResponseTime*1000)-endtime)/1000;
                    if(secondsleft <= 5) java.awt.Toolkit.getDefaultToolkit().beep();
                    
                    setTitle("Your turn. Seconds left: " + secondsleft);
                    Thread.sleep(300);
                }
                catch(InterruptedException ie)
                {
                }            
                endtime = new GregorianCalendar().getTimeInMillis();            
            }

            if(endtime - starttime < ResponseTime*1000)
            {   
                setTitle("Waiting for opponent to play");
                return TheMove;
            }
            else
            {
                setTitle("Your time ran out. You loose.");
                if(Side == BoardDataStructure.RedMove)
                {
                    winner = BoardDataStructure.BlueMove;
                }
                else winner = BoardDataStructure.RedMove;
                pc.repaint();
                return TheMove;
            }
        }
        else
        {
            setTitle("Game Over");
            pc.repaint();
            return TheMove;
        }
        
    }
    
    public class PicCanvas extends Canvas implements MouseListener, MouseMotionListener
    {
        Image buffer;
        Graphics bg;



        public PicCanvas()
        {
            setSize(source1.getWidth(this), source1.getHeight(this));
            addMouseListener(this);
            addMouseMotionListener(this);
        }     

        public void paint(Graphics g)
        {
            if(bg == null)
             {
                     buffer = createImage(source1.getWidth(null), source1.getHeight(null));
                     bg = buffer.getGraphics();
             }

             bg.drawImage(source1, 0, 0, null);
             Graphics2D g2 = (Graphics2D) bg;
             g2.setStroke(new BasicStroke(3));
             g2.setColor(Color.red);
             g2.drawLine(RHCornerX+5, RHCornerY+3, RHCornerX - Math.round(HexWidth*BoardSize), RHCornerY+3);
             g2.drawLine(RHCornerX-Math.round(HexWidth*BoardSize/2)+6, RHCornerY-Math.round(HexHeight*BoardSize), RHCornerX - Math.round(HexWidth*BoardSize + HexWidth*BoardSize/2), RHCornerY-Math.round(HexHeight*BoardSize));
             g2.setColor(Color.blue);
             g2.drawLine(RHCornerX+6, RHCornerY+3,RHCornerX-Math.round(HexWidth*BoardSize/2)+6, RHCornerY-Math.round(HexHeight*BoardSize));
             g2.drawLine(RHCornerX - Math.round(HexWidth*BoardSize), RHCornerY+3, RHCornerX - Math.round(HexWidth*BoardSize+HexWidth*BoardSize/2), RHCornerY-Math.round(HexHeight*BoardSize));                  

             float LHCornerX = RHCornerX - BoardSize*HexWidth*1.5f;
             float LHCornerY = RHCornerY - BoardSize*HexHeight;

             for(int r =0; r < BoardSize; r++)
             {
                 for(int c = 0; c < BoardSize; c++)
                 {
                     if(displayBDS.Board[c][r] > BoardDataStructure.Empty)
                     {
                         if(displayBDS.Board[c][r] == BoardDataStructure.RedMove)
                         {
                             g2.setColor(Color.red);
                         }
                         else
                         {
                             g2.setColor(Color.blue);
                         }
                         g2.fillOval((int) (LHCornerX + 0.25f*HexWidth + c*HexWidth+(r+1)*0.5f*HexWidth),(int) (LHCornerY + HexHeight/2 + r*HexHeight), (int) (HexWidth/2), (int) (HexHeight/2));
                     }                                
                 }
             }
             
             if(Side==BoardDataStructure.RedMove)
             {
                g2.setColor(Color.red);
                Font stringFont = new Font( "SansSerif", Font.PLAIN, 38 );
                g2.setFont(stringFont);
                g2.drawString("You are Red", 100, 50);
             }
             
             if(Side==BoardDataStructure.BlueMove)
             {
                g2.setColor(Color.blue);
                Font stringFont = new Font( "SansSerif", Font.PLAIN, 38 );
                g2.setFont(stringFont);
                g2.drawString("You are Blue", 100, 50);
             }

             if(winner == BoardDataStructure.RedMove)
             {
                 g2.setColor(Color.red);
                 Font stringFont = new Font( "SansSerif", Font.PLAIN, 58 );
                 g2.setFont(stringFont);
                 g2.drawString("Red Won!", 100, 100);
             }
             else if(winner == BoardDataStructure.BlueMove)
             {
                 g2.setColor(Color.blue);
                 Font stringFont = new Font( "SansSerif", Font.PLAIN, 58 );
                 g2.setFont(stringFont);
                 g2.drawString("Blue Won!", 100, 100);
             }

             g.drawImage(buffer, 0, 0, null);
        }

        public void update(Graphics g)
        {
             paint(g);
        }

        @Override
        public void mouseClicked(MouseEvent me) {
            
            if((thismove)&&(winner == BoardDataStructure.Empty))
            {
                int x = me.getX();
                int y = me.getY();
                if((y < RHCornerY)&&(y > RHCornerY-Math.round(HexHeight*BoardSize)))
                {

                    double yel = y - (RHCornerY-HexHeight*BoardSize);
                    yel = yel/HexHeight;
                    int row = (int) yel;
                    System.out.println(yel);
                    double xel = x - (RHCornerX - HexWidth*1.5*BoardSize);
                    xel = xel- (row+1)*0.5*HexWidth;

                    if(xel < BoardSize * HexWidth)
                    {
                        System.out.println(xel);
                        xel = xel/HexWidth;
                        int col = (int) xel;
                        if(displayBDS.Board[col][row]==0)
                        { 
                            
                            displayBDS.Board[col][row] = Side;
                            TheMove = col+","+row;
                            
                            thismove = false;
                        }
                    }
                }

             /*    for(int r =0; r < BoardSize; r++)
                {
                    for(int c = 0; c < BoardSize; c++)
                    {                               
                        System.out.print(displayBDS.Board[c][r]);
                    }
                    System.out.println("");

                }*/
               // winner = displayBDS.CheckWinner(); 
                repaint();               
            }
        }

        @Override
        public void mousePressed(MouseEvent me) {
           // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void mouseReleased(MouseEvent me) {
           // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void mouseEntered(MouseEvent me) {
           // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void mouseExited(MouseEvent me) {
            //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void mouseDragged(MouseEvent me) {
           // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void mouseMoved(MouseEvent me) {
            //System.out.println(me.getX() + " " + me.getY());
        }

    }
}    
