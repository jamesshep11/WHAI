using System;
using System.Collections;
using System.Collections.Generic;
using System.Diagnostics;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Assignment2
{
    class Program
    {
        static ArrayList maze = new ArrayList();
        static ArrayList paths;
        static ArrayList solPath;
        static HashSet<Position> explored;

        static Stopwatch timer = new Stopwatch();
        static Solution sol = new Solution();
        static int memory = 1;

        static void Main(string[] args) {
            // Choose a maze
            Console.WriteLine("Please enter the file name:");
            string fileName = Console.ReadLine();
            readFromFile(fileName);

            // Exectute Algorithm
            bool found = false;
            found = UCS();

            sol.memory = memory;
            sol.nodesExplored = explored.Count;
            sol.pathLength = solPath.Count;
            sol.executionTime = timer.ElapsedTicks;
            sol.cost = 0;
            foreach (Position pos in solPath) {
                sol.cost += pos.cost;
            }

            // show path in map
            for (int x = 0; x < maze.Count - 1; x++)
                for (int y = 0; y < ((ArrayList)maze[x]).Count; y++)
                    if ((int)((ArrayList)maze[x])[y] != 0 && !arrayListContains(new Position(x, y), solPath))
                        ((ArrayList)maze[x])[y] = null;

            // Display stats
            Console.WriteLine("Memory: " + sol.memory + "\n" +
                "Nodes Explored: " + sol.nodesExplored + "\n" +
                "Path Length: " + sol.pathLength + "\n" +
                "Execution Time: " + sol.executionTime + "\n" +
                "Cost: " + sol.cost + "\n");

            // Display the final path
            if (found) {
                foreach (ArrayList list in maze) {
                    for (int i = 0; i < list.Count; i++)
                        if (list[i] != null)
                            Console.Write(list[i]);
                        else
                            Console.Write(" ");
                    Console.WriteLine();
                }
            } else
                Console.WriteLine("ALGORITHM FAILED: A path could not be found.");

            Console.ReadLine();
        }

        static void readFromFile(string fileName) {
            try {
                StreamReader reader = new StreamReader(fileName);

                while (!reader.EndOfStream) {
                    string line = reader.ReadLine();

                    ArrayList list = new ArrayList();
                    foreach (char x in line)
                        list.Add(int.Parse(x.ToString()));

                    maze.Add(list);
                }
            } catch (Exception e) {
                Console.WriteLine("Exception: " + e.Message);
            }
        }

        static bool UCS() {
            timer.Restart();
            PriorityQueue frontier = new PriorityQueue();
            explored = new HashSet<Position>();
            paths = new ArrayList();

            // Add initial State to frontier
            foreach (int x in (ArrayList)maze[0])
                if (x > 0) {
                    int pos = ((ArrayList)maze[0]).IndexOf(x);
                    frontier.Enqueue(new Position(0, pos, (int)((ArrayList)maze[0])[pos]));
                    break;
                }

            // Find Goal
            Position goal = new Position(0, 0);
            foreach (int x in (ArrayList)maze[maze.Count - 1])
                if (x > 0) {
                    int pos = ((ArrayList)maze[maze.Count - 1]).IndexOf(x);
                    goal = new Position(maze.Count - 1, pos, (int)((ArrayList)maze[maze.Count - 1])[pos]);
                    break;
                }

            bool found = false;
            while (frontier.Count > 0) {
                // explore a new position
                Position curPos = frontier.Dequeue();

                // Have we found the goal?
                if (curPos.x == goal.x && curPos.y == goal.y) {
                    found = true;
                    break;
                }

                explored.Add(curPos);
                addToPath(curPos);

                // Find new states for the frontier
                ArrayList tempFront = new ArrayList();
                if (curPos.y - 1 < ((ArrayList)maze[0]).Count) {
                    Position newPos = new Position(curPos.x, curPos.y - 1);
                    newPos.cost = curPos.cost + (int)((ArrayList)maze[curPos.x])[curPos.y - 1] +heuristic(newPos, goal);
                    tempFront.Add(newPos);
                }
                if (curPos.x + 1 < maze.Count) {
                    Position newPos = new Position(curPos.x + 1, curPos.y);
                    newPos.cost = curPos.cost + (int)((ArrayList)maze[curPos.x + 1])[curPos.y] + heuristic(newPos, goal);
                    tempFront.Add(newPos);
                }
                if (curPos.y + 1 >= 0) {
                    Position newPos = new Position(curPos.x, curPos.y + 1);
                    newPos.cost = curPos.cost + (int)((ArrayList)maze[curPos.x])[curPos.y + 1] + heuristic(newPos, goal);
                    tempFront.Add(newPos);
                }
                if (curPos.x - 1 >= 0) {
                    Position newPos = new Position(curPos.x - 1, curPos.y);
                    newPos.cost = curPos.cost + (int)((ArrayList)maze[curPos.x - 1])[curPos.y] + heuristic(newPos, goal);
                    tempFront.Add(newPos);
                }

                // Add new states to the frontier
                foreach (Position pos in tempFront)
                    if ((int)((ArrayList)maze[pos.x])[pos.y] > 0 && !hashContains(pos, explored)) {
                        if (!priorityQueueContains(pos, frontier))
                            frontier.Enqueue(pos);
                        else {
                            foreach (Position pos2 in frontier)
                                if (pos.x == pos2.x && pos.y == pos2.y)
                                    if (pos.cost < pos2.cost)
                                        pos2.cost = pos.cost;
                        }
                    }
                if (frontier.Count > memory)
                    memory = frontier.Count;
            }
            timer.Stop();

            // Found the goal
            if (found) {
                // Find the final path
                solPath = new ArrayList();
                foreach (ArrayList list in paths)
                    if (canReach(goal, (Position)list[list.Count - 1])) {
                        solPath = list;
                        break;
                    }

                return true;
            }

            // Didn't find the goal
            return false;
        }

        static int heuristic(Position pos, Position goal) {
            // Change in x + change in y
            return Math.Abs(goal.x - pos.x) + Math.Abs(goal.y - pos.y);
        }

        static void addToPath(Position newPos) {
            bool placed = false;

            if (paths.Count == 0) {
                ArrayList newList = new ArrayList();
                newList.Add(newPos);
                paths.Add(newList);
                return;
            }

            // Check the paths and add
            int i = 0;
            while (!placed) {
                ArrayList list = (ArrayList)paths[i];
                if (canReach(newPos, (Position)list[list.Count - 1])) {
                    paths.Add((ArrayList)list.Clone());
                    list.Add(newPos);
                    placed = true;
                }
                i++;
            }
        }

        static bool canReach(Position startPos, Position endPos) {
            if (startPos.x == endPos.x) {
                if (startPos.y - 1 == endPos.y || startPos.y + 1 == endPos.y)
                    return true;
            } else if (startPos.y == endPos.y) {
                if (startPos.x - 1 == endPos.x || startPos.x + 1 == endPos.x)
                    return true;
            }

            return false;
        }

        static bool arrayListContains(Position position, ArrayList list) {
            foreach (Position pos in list)
                if (pos.x == position.x && pos.y == position.y)
                    return true;

            return false;
        }

        static bool priorityQueueContains(Position position, PriorityQueue queue) {
            foreach (Position pos in queue)
                if (pos.x == position.x && pos.y == position.y)
                    return true;

            return false;
        }

        static bool hashContains(Position position, HashSet<Position> set) {
            foreach (Position pos in set)
                if (pos.x == position.x && pos.y == position.y)
                    return true;

            return false;
        }
    }

    class Position
    {
        public int x;
        public int y;
        public int cost;

        public Position(int x, int y) {
            this.x = x;
            this.y = y;
            this.cost = 1;
        }

        public Position(int x, int y, int cost) {
            this.x = x;
            this.y = y;
            this.cost = cost;
        }
    }

    class PriorityQueue : Queue
    {
        public PriorityQueue() {

        }

        public Position Dequeue() {
            Position min = (Position)base.Dequeue();
            for (int i = 0; i < Count; i++) {
                Position pos = (Position)base.Dequeue();
                if (pos.cost - min.cost < 0) {
                    Enqueue(min);
                    min = pos;
                } else
                    Enqueue(pos);
            }

            return min;
        }

        public Position Peek() {
            Position min = (Position)base.Dequeue();
            foreach (Position pos in this)
                if (pos.cost - min.cost < 0)
                    min = pos;

            return min;
        }
    }

    class Solution
    {
        public int memory;
        public int nodesExplored;
        public long executionTime;
        public int pathLength;
        public int cost;

        public Solution() {
        }

        public Solution(int memory, int nodesExplored, long executionTime, int pathLength, int cost) {
            this.memory = memory;
            this.nodesExplored = nodesExplored;
            this.executionTime = executionTime;
            this.pathLength = pathLength;
            this.cost = cost;
        }
    }
}
