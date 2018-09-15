import java.util.ArrayList;

public class Node {
    public ArrayList<Node> children = new ArrayList<>();
    public Node parent;
    public int[] puzzle;
    public int emptySpot;
    public int columnSize;
    public boolean solvable;

    //greedy best first search
    //f(n) = h(n)
    //A* best first search
    //f(n) = g(n) + h(n)
    public int f;
    public int h; //h = heuristics
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

        solvable = isSolvable();
        h = calculateManhattanDistance();
    }

    //calculates cost from start node to current node
    public int calculatePathCost(int[] startPuzzle) {
        int cost = 0;
        int boardPiece = 0;
        int[] pCords = new int[2];
        int[] cCords = new int[2];

        int[][] startCopy = new int[columnSize][columnSize];//this.parent.puzzle;
        int[][] childCopy = new int[columnSize][columnSize];//this.puzzle;

        if(isSamePuzzle(startPuzzle)) {
            return cost;
        }

        //populates 2d boards with values of each puzzle
        for(int i = 0; i < columnSize; i++) {
            for(int j = 0; j < columnSize; j++) {
                startCopy[i][j] = startPuzzle[boardPiece];
                childCopy[i][j] = this.puzzle[boardPiece];
                boardPiece++;
            }
        }

        //cost calculation
        while(boardPiece < this.puzzle.length) {
         for(int x = 0; x < columnSize; x++) {
             for(int y = 0; y < columnSize; y++) {
                if(startCopy[x][y] == boardPiece) {
                    pCords[0] = x;
                    pCords[1] = y;
                }
                 if(childCopy[x][y] == boardPiece) {
                     cCords[0] = x;
                     cCords[1] = y;
                 }
             }
         }
            cost += Math.abs(pCords[0] - cCords[0]);
            cost += Math.abs(pCords[1] - cCords[1]);

            boardPiece++;
        }

        return cost;
    }

    //calculates manhattan Distance of current node
    public int calculateManhattanDistance() {
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
        return mDistance;
    }

    //checks to see if node is solvable
    //checks for duplicate values and skipped values
    public boolean isSolvable() {
        solvable = true;
        //creates an array and initializes all value to zero (values are 0 by default)
        int[] puzzleCopy = new int[puzzle.length];

        //traverses puzzle and sets puzzleCopy[value returned] to a value of 1.
        for(int i = 0; i < puzzle.length; i++) {
           puzzleCopy[puzzle[i]] = 1;
        }

        //if there are any 0's in the copy array than there are missing or duplicate values in the puzzle and it
        //is not solvable
        for(int i = 0; i < puzzle.length; i++) {
            if(puzzleCopy[i] == 0) {
                solvable = false;
            }
        }

        //puzzle already solved because all indexes are set to 1
        if(isGoalNode()) {
            solvable = false;
        }

        return solvable;
    }

    //Checks if goal node has been reached.
    public boolean isGoalNode() {
        boolean isGoal = true;
        int m = puzzle[0];

        for(int i = 1; i < puzzle.length; i++) {
            if(m > puzzle[i]) {
                isGoal = false;
            }
            m = puzzle[i];
        }
        return isGoal;
    }

    public boolean isSamePuzzle(int[] currentPuzzle) {
        boolean samePuzzle = true;
        for(int i = 0; i < puzzle.length; i++) {
            if(puzzle[i] != currentPuzzle[i]) {
                samePuzzle = false;
            }
        }
        return samePuzzle;
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
