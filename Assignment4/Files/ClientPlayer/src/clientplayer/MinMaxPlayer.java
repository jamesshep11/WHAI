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
public class MinMaxPlayer implements Player
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
    String PlayerName = "MinMaxPlayer";

    public MinMaxPlayer()
    {
        this("Local", 2001, "MinMaxPlayer");
    }

    public MinMaxPlayer(String name)
    {
        this("Local", 2001, name);
    }

    public MinMaxPlayer(String server, int port, String name)
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
        // Convert board string to board
        ArrayList<Position> emptySpots = new ArrayList<>();
        BoardDataStructure temp = new BoardDataStructure(BoardSize);
        StringTokenizer st = new StringTokenizer(board, ",");
        for(int r =0; r < BoardSize; r++)
            for(int c = 0; c < BoardSize; c++) {
                int x = Integer.parseInt(st.nextToken());
                temp.Board[c][r] = x;
                // Find empty positions
                if (x == BoardDataStructure.Empty)
                    emptySpots.add(new Position(c, r));
            }

        Position pos = MinMax(temp, emptySpots);
        return pos.toString();
    }

    private Position MinMax(BoardDataStructure board, ArrayList<Position> emptySpots){
        // Perform MinMax algorithm
        int val = Integer.MIN_VALUE;
        int count = 0;
        while (count < emptySpots.size()){
            Position pos = emptySpots.remove(0);
            board.Board[pos.getCol()][pos.getRow()] = Side;
            val = Math.max(val, MinVal(board, emptySpots));
            board.Board[pos.getCol()][pos.getRow()] = BoardDataStructure.Empty;
            pos.setValue(val);
            emptySpots.add(pos);
            count++;
        }

        // find Position that leads to the max result
        Collections.shuffle(emptySpots);
        emptySpots.sort(Comparator.comparingInt(Position::getValue));
        for(Position pos : emptySpots)
            System.out.print(pos.getValue() + ", ");
        System.out.println();

        return emptySpots.get(emptySpots.size()-1);
    }

    private int MaxVal(BoardDataStructure board, ArrayList<Position> slots){
        int val = board.CheckWinner();
        if (val != BoardDataStructure.Empty)
            return val == Side ? 1 : -1;

        val = Integer.MIN_VALUE;
        int count = 0;
        while (count < slots.size()){
            Position pos = slots.remove(0);
            board.Board[pos.getCol()][pos.getRow()] = Side;
            val = Math.max(val, MinVal(board, slots));
            board.Board[pos.getCol()][pos.getRow()] = BoardDataStructure.Empty;
            slots.add(pos);
            count++;
        }
        return val;
    }

    private int MinVal(BoardDataStructure board, ArrayList<Position> slots){
        int val = board.CheckWinner();
        if (val != BoardDataStructure.Empty)
            return val == Side ? 1 : -1;

        val = Integer.MAX_VALUE;
        int count = 0;
        while (count < slots.size()){
            Position pos = slots.remove(0);
            board.Board[pos.getCol()][pos.getRow()] = Side == 1 ? 2 : 1;
            val = Math.min(val, MaxVal(board, slots));
            board.Board[pos.getCol()][pos.getRow()] = BoardDataStructure.Empty;
            slots.add(pos);
            count++;
        }
        return val;
    }

}
