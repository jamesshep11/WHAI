/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientplayer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.*;


/**
 *
 * @author MC
 */
public class MonteCarloPlayer implements Player
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
    String PlayerName = "MonteCarloPlayer";

    public MonteCarloPlayer()
    {
        this("Local", 2001, "MonteCarloPlayer");
    }

    public MonteCarloPlayer(String name)
    {
        this("Local", 2001, name);
    }

    public MonteCarloPlayer(String server, int port, String name)
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

    private long startTime;
    @Override
    public String MakeMove(String board)
    {
        startTime = System.currentTimeMillis();
        ArrayList<Position> availableSlots = new ArrayList<>();

        // Convert board string to board
        BoardDataStructure temp = new BoardDataStructure(BoardSize);
        StringTokenizer st = new StringTokenizer(board, ",");
        for(int r =0; r < BoardSize; r++)
            for(int c = 0; c < BoardSize; c++) {
                int x = Integer.parseInt(st.nextToken());
                temp.Board[c][r] = x;
                if (x == BoardDataStructure.Empty)
                    availableSlots.add(new Position(c, r, 0));
            }

        Position pos = monteCarlo(temp, availableSlots);

        return pos.toString();
    }

    private Position monteCarlo(BoardDataStructure board, ArrayList<Position> slots){
        while ((System.currentTimeMillis() - startTime) < ResponseTime*1000*0.99){
            for (Position pos : slots){
                board.Board[pos.getCol()][pos.getRow()] = Side;
                int value = playRandomGame(board, slots);
                pos.setValue(pos.getValue() + value);
                board.Board[pos.getCol()][pos.getRow()] = BoardDataStructure.Empty;
            }
        }

        Collections.shuffle(slots);
        slots.sort(Comparator.comparingInt(Position::getValue));

        return slots.get(slots.size()-1);
    }

    private int playRandomGame(BoardDataStructure board, ArrayList<Position> slots){
        ArrayList<Position> temp = (ArrayList<Position>) slots.clone();

        int winner = BoardDataStructure.Empty;
        int turn = Side;
        int count = 0;
        while(winner == BoardDataStructure.Empty && !temp.isEmpty()){
            // next player's turn
            turn = turn == BoardDataStructure.BlueMove ? BoardDataStructure.RedMove : BoardDataStructure.BlueMove;
            // make a random move
            int rand = r.nextInt(temp.size());
            Position rando = temp.remove(rand);
            board.Board[rando.getCol()][rando.getRow()] = turn;
            // check if there's a winner
            winner = board.CheckWinner();
            // count number of moves
            count++;
        }

        return winner == Side ? 1/count : -1/count;
    }
    
}
