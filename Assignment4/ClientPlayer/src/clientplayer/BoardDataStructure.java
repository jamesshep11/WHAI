/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientplayer;

/**
 *
 * @author MC
 */
public class BoardDataStructure {
    
    public static int Empty = 0;
    public static int RedMove = 1;
    public static int BlueMove = 2;
    public int BoardSize = 7;
    
    public int[][] Board; 
    
    public BoardDataStructure()
    {
        Board = new int[BoardSize][BoardSize]; 
    }
    
    public BoardDataStructure(int BS)
    {   
        BoardSize = BS;
        Board = new int[BS][BS]; 
    }
    
    public BoardDataStructure clone()
    {
        BoardDataStructure temp = new BoardDataStructure(BoardSize);
        
        for(int r =0; r < BoardSize; r++)
        {
            for(int c = 0; c < BoardSize; c++)
            {
                temp.Board[c][r] = this.Board[c][r];
            }
        }
        return temp;
    }
    
    public String GetBoardString()
    {
        String s = "";
        for(int r =0; r < BoardSize; r++)
        {
            for(int c = 0; c < BoardSize; c++)
            {
                s= s+  this.Board[c][r] +",";
            }
        }
        return s;
        
    }
    
    public void DisplayBoard()
    {
        String s = "";
        for(int r =0; r < BoardSize; r++)
        {
            for(int c = 0; c < BoardSize; c++)
            {
                s= s + this.Board[c][r] +",";
            }
            System.out.println(s);
            s = "";
        }
        
        System.out.println("");
        
    }
    
    public int CheckWinner()
    {
        BoardDataStructure temp = this.clone();
        //Check Red
        for(int c =0; c < BoardSize; c++)
        {
            if(CheckRecursion(temp, 0, c, RedMove)) return RedMove;
        }
        
        //CheckBlue
        for(int r = 0; r < BoardSize; r++)            
        {
            if(CheckRecursion(temp, r, 0, BlueMove)) return BlueMove;
        }
        
        return Empty;
    }
    
    public boolean CheckRecursion(BoardDataStructure bds, int row, int col, int Move)
    {
        //System.out.println(col + " " + row + " " + Move + " " + bds.Board[col][row]);
             
        
        if(bds.Board[col][row] == Move)
        {
            if((Move == RedMove)&&(row == BoardSize-1)) return true;
        
            if((Move == BlueMove)&&(col == BoardSize-1)) return true;  
            
            bds.Board[col][row] = Empty;
            
            if(row-1 >=0)
            {
                if(CheckRecursion(bds, row-1, col, Move))
                {
                    return true;
                }
                else if(col+1 < BoardSize)
                {
                    if(CheckRecursion(bds, row-1, col+1, Move))
                    {
                        return true;
                    }
                }
            }
            
            if(row+1 < BoardSize)
            {
                if(CheckRecursion(bds, row+1, col, Move))
                {
                    return true;
                }
                else if(col-1 >= 0)
                {
                    if(CheckRecursion(bds, row+1, col-1, Move))
                    {
                        return true;
                    }
                }
            }
            
            if(col+1 < BoardSize)
            {
                if(CheckRecursion(bds, row, col+1, Move))
                {
                    return true;
                }
            }
            
            if(col-1 >= 0)
            {
                if(CheckRecursion(bds, row, col-1, Move))
                {
                    return true;
                }
            }              
        }
        
        return false;
    }
    
}
