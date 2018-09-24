/*
I cannot figure out why this puzzle is taking a long time to solve
7 2 4
5 . 6
8 3 1
 */

import java.util.ArrayList;

public class Node {
    public ArrayList<Node> children = new ArrayList<>();
    public Node parent;
    public int[] puzzle;
    public int emptySpot;
    public int columnSize;

    //greedy best first search
    //f(n) = h(n)
    //A* best first search
    //f(n) = g(n) + h(n)
    public int f;
    public int h; //h = heuristics - manhattan distance;
    public int g; //g = cost from root node to current node

    public Node(int[] inputPuzzle) {
        puzzle = inputPuzzle;

        if(inputPuzzle.length == 9) {
            columnSize = 3;
        }
        else if(inputPuzzle.length == 16) {
            columnSize = 4;
        }
        else if(inputPuzzle.length == 25) {
            columnSize = 5;
        } else {
            throw new IllegalArgumentException("unrecognized number of puzzle pieces detected.");
        }

        calculateManhattanDistance();
    }

    public void calculatef(int[] startPuzzle) {
        calculatePathCost(startPuzzle);
        f = g + h;
    }

    //calculates cost from start node to current node
    public void calculatePathCost(int[] startPuzzle) {
        //calculate g
        int cost = 0;
        int boardPiece = 0;
        int[] pCords = new int[2];
        int[] cCords = new int[2];

        int[][] startCopy = new int[columnSize][columnSize];//this.parent.puzzle;
        int[][] childCopy = new int[columnSize][columnSize];//this.puzzle;

        if(isSamePuzzle(startPuzzle)) {
            //do nothing
        } else {

            //populates 2d boards with values of each puzzle
            for (int i = 0; i < columnSize; i++) {
                for (int j = 0; j < columnSize; j++) {
                    startCopy[i][j] = startPuzzle[boardPiece];
                    childCopy[i][j] = this.puzzle[boardPiece];
                    boardPiece++;
                }
            }

            //cost calculation
            while (boardPiece < this.puzzle.length) {
                for (int x = 0; x < columnSize; x++) {
                    for (int y = 0; y < columnSize; y++) {
                        if (startCopy[x][y] == boardPiece) {
                            pCords[0] = x;
                            pCords[1] = y;
                        }
                        if (childCopy[x][y] == boardPiece) {
                            cCords[0] = x;
                            cCords[1] = y;
                        }
                    }
                }
                cost += Math.abs(pCords[0] - cCords[0]);
                cost += Math.abs(pCords[1] - cCords[1]);

                boardPiece++;
            }
        }

        g = cost;
    }

    //calculates manhattan Distance of current node
    public void calculateManhattanDistance() {
        //calculate h
        int[][] copyBoard = new int[columnSize][columnSize];
        int[] goalState = new int[2];

        int mDistance = 0;
        int boardPiece = 0;

        //populates 2d board with values of puzzle for use in manhattan distance calc.
        for(int i = 0; i < columnSize; i++) {
            for(int j = 0; j < columnSize; j++) {
                copyBoard[i][j] = puzzle[boardPiece];
                boardPiece++;
            }
        }

        for(int i = 0; i < columnSize; i++) {
            for(int j = 0; j < columnSize; j++) {
                if(copyBoard[i][j] != 0) {
                    int puzzlePiece = copyBoard[i][j];
                    goalState[0] = puzzlePiece % columnSize; //row
                    goalState[1] = (int) Math.floor((puzzlePiece-1)/columnSize); //column
                    mDistance += (Math.abs(goalState[0]- j) + Math.abs(goalState[1]-i));
                }
            }
        }

        h = mDistance; //assign mDistance to current object
    }

    //checks to see if node is solvable
    //checks for duplicate values and skipped values
    public boolean isSolvable() {
        int inversions = 0;

        for(int i = 0; i < puzzle.length; i++) {
            for(int j = i + 1; j < puzzle.length; j++) {
                if(puzzle[i] != 0 && puzzle[j] != 0 && puzzle[i] > puzzle[j]) { //do not include the inversions for puzzle piece 0
                    inversions++;
                }
            }
        }

        if(inversions % 2 == 1){
            return false;
        }else{
            return true;
        }
    }

    //Checks if goal node has been reached.
    public boolean isGoalNode() {
        int m = puzzle[0];

        for(int i = 1; i < puzzle.length; i++) {
            if(m > puzzle[i]) {
                return false;
            }
            m = puzzle[i];
        }
        return true;
    }

    public boolean isSamePuzzle(int[] currentPuzzle) {
        for(int i = 0; i < puzzle.length; i++) {
            if(puzzle[i] != currentPuzzle[i]) {
                return false;
            }
        }
        return true;
    }

    //copy puzzle - crates a copy of the current puzzle state
    public void copyPuzzle(int[] puzzleCopy, int[] puzzle) {
        for(int i = 0; i < puzzle.length; i++) {
            puzzleCopy[i] = puzzle[i];
        }
    }

    //Expansion calls - **AI of Program... but not really**

    public void expandMove() {
        for(int i = 0; i < puzzle.length; i++) {
            if(puzzle[i] == 0) {
                emptySpot = i;
            }
        }
        moveRight(puzzle, emptySpot);
        moveLeft(puzzle, emptySpot);
        moveUp(puzzle, emptySpot);
        moveDown(puzzle, emptySpot);
    }

    //Node Expansion - Up, Down, Left, Right.
    //int[] puzzle = current puzzle state
    //int i = "." puzzle piece index

    public void moveLeft(int[] puzzle, int i){
        if(i % columnSize > 0) {
            int[] puzzleCopy = new int[puzzle.length];
            copyPuzzle(puzzleCopy, puzzle);

            int temp = puzzleCopy[i - 1];
            puzzleCopy[i - 1] = puzzleCopy[i];
            puzzleCopy[i] = temp;

            Node child = new Node(puzzleCopy);

            child.parent = this;
            children.add(child);
            //A* Search Heuristic Function

        }

    }

    public void moveRight(int[] puzzle, int i){
        if(i % columnSize < columnSize - 1) {
            int[] puzzleCopy = new int[puzzle.length];
            copyPuzzle(puzzleCopy, puzzle);

            int temp = puzzleCopy[i + 1];
            puzzleCopy[i + 1] = puzzleCopy[i];
            puzzleCopy[i] = temp;

            Node child = new Node(puzzleCopy);

            child.parent = this;
            children.add(child);
            //A* Search Heuristic Function

        }

    }

    public void moveUp(int[] puzzle, int i){
        if(i - columnSize >= 0) {
            int[] puzzleCopy = new int[puzzle.length];
            copyPuzzle(puzzleCopy, puzzle);

            int temp = puzzleCopy[i - 3];
            puzzleCopy[i - 3] = puzzleCopy[i];
            puzzleCopy[i] = temp;

            Node child = new Node(puzzleCopy);

            child.parent = this;
            children.add(child);
            //A* Search Heuristic Function

        }
    }

    public void moveDown(int[] puzzle, int i){
        if(i + columnSize < puzzle.length) {
            int[] puzzleCopy = new int[puzzle.length];
            copyPuzzle(puzzleCopy, puzzle);

            int temp = puzzleCopy[i + 3];
            puzzleCopy[i + 3] = puzzleCopy[i];
            puzzleCopy[i] = temp;

            Node child = new Node(puzzleCopy);

            child.parent = this;
            children.add(child);
            //A* Search Heuristic Function

        }
    }

    //Debugging
    public void printPuzzle() {
        System.out.println();
        int m = 0;
        for(int i = 0; i < columnSize; i++) { //column
            for(int j = 0; j < columnSize; j++) { //row
                if(puzzle[m] == 0) {
                    System.out.print("." + " ");
                } else {
                    System.out.print(puzzle[m] + " ");
                }
                m++;
            }
            System.out.println();
        }
    }
}
