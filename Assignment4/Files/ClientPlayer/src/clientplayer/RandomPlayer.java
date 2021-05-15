/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientplayer;



import java.io.*;
import java.net.*;
import java.util.*;


/**
 *
 * @author MC
 */
public class RandomPlayer implements Player
{
    Socket connectToServer;
    
    BufferedReader isFromServer; 
    PrintWriter osToServer; 
    
    int BoardSize;
    int Side;
    int ResponseTime;
    
    Random r = new Random();
    
    int Port = 2001;
    String Server = "Local";
    String PlayerName = "RandomPlayer";
    
    public RandomPlayer()
    {
        this("Local", 2001, "RandomPlayer");
    }
    
    public RandomPlayer(String name)
    {
        this("Local", 2001, name);
    }
            
    public RandomPlayer(String server, int port, String name)
    {
        PlayerName = name;
        Server= server;
        Port = port;   
        
        GetConnection();
        boolean GameOver = false;       
        try
        {
            System.out.println("Starting Game"); 
            while(!GameOver)
            {
                
                String nextL = isFromServer.readLine();
                
                if(nextL.length() > 1)
                {                   
                    osToServer.println(MakeMove(nextL));
                    osToServer.flush();                    
                }
                else
                {
                    System.out.println("Winner: " + nextL);
                    nextL = isFromServer.readLine();
                    System.out.println("Final Board: " + nextL);
                    GameOver = true;
                }
            }
            System.out.println("Game Over");
        }
        catch(IOException ioe)
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

    public int PlayRandomGame(BoardDataStructure cur, int CurSide)
    {
        if(cur.CheckWinner() != BoardDataStructure.Empty)
        {    
            return CurSide;
        }
        else
        {
            int col = r.nextInt(BoardSize);
            int row = r.nextInt(BoardSize);
            while(cur.Board[col][row]!= BoardDataStructure.Empty)
            {
               // col = r.nextInt(BoardSize);
               // row = r.nextInt(BoardSize);

                col++;
                if(col == BoardSize)
                {
                    col = 0;
                    row++;
                }
                if(row== BoardSize)
                {                
                    row = 0;
                }
            }            
            
            if(CurSide== BoardDataStructure.RedMove)
            {
                CurSide = BoardDataStructure.BlueMove;
            }
            else
            {
                CurSide = BoardDataStructure.RedMove;
            }
            cur.Board[col][row] = CurSide;
            return PlayRandomGame(cur, CurSide);
        }
        
    }

   @Override
    public String MakeMove(String board) 
    {
        ArrayList<Position> opponentHexs = new ArrayList<>();

        BoardDataStructure temp = new BoardDataStructure(BoardSize);
        StringTokenizer st = new StringTokenizer(board, ",");
        for(int r =0; r < BoardSize; r++)
            for(int c = 0; c < BoardSize; c++) {
                int x = Integer.parseInt(st.nextToken());
                temp.Board[c][r] = x;
                if (x != Side && x != BoardDataStructure.Empty)
                    opponentHexs.add(new Position(c, r));
            }

        Position pos;

        if (opponentHexs.size() == 0) {
            int col, row;
            do {
                col = r.nextInt(BoardSize);
                row = r.nextInt(BoardSize);
            } while (temp.Board[col][row] != BoardDataStructure.Empty);
            pos = new Position(col, row);
        }
        else if ((pos = canWin(temp)) != null) { }
        else pos = findPos(opponentHexs, temp);

        return pos.toString();
    }

    private Position findPos(ArrayList<Position> opponentsHexs, BoardDataStructure board){
        Collections.shuffle(opponentsHexs);
        while(true) {
            Position hex = opponentsHexs.remove(0);
            ArrayList<Position> surroundingHexs = getSurroundingHexs(hex, board);
            if (surroundingHexs.size() > 0){
                Collections.shuffle(surroundingHexs);
                return surroundingHexs.get(0);
            }
        }
    }

    private ArrayList<Position> getSurroundingHexs(Position hex, BoardDataStructure board){
        int col = hex.getCol();
        int row = hex.getRow();
        ArrayList<Position> temp = new ArrayList<>(Arrays.asList(
                new Position(col,row+1),
                new Position(col, row-1),
                new Position(col+1, row),
                new Position(col-1, row),
                new Position(col-1, row-1),
                new Position(col+1, row-1)
            ));

        ArrayList<Position> surroundingHexs = new ArrayList<>();
        for (Position pos : temp){
                col = pos.getCol();
                row = pos.getRow();

            if(col >= 0 && col < BoardSize && row >= 0 && row < BoardSize)
                if (board.Board[col][row] == BoardDataStructure.Empty)
                    surroundingHexs.add(pos);
        }

        return surroundingHexs;
    }

    private Position canWin(BoardDataStructure board){
        BoardDataStructure temp = board.clone();

        for(int r =0; r < BoardSize; r++)
            for(int c = 0; c < BoardSize; c++)
                if (temp.Board[c][r] == BoardDataStructure.Empty) {
                    temp.Board[c][r] = Side;
                    if (temp.CheckWinner() == Side)
                        return new Position(c, r);
                    temp.Board[c][r] = BoardDataStructure.Empty;
                }

        return null;
    }
}
