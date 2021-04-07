public class Solution {
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
