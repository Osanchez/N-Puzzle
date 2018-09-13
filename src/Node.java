import java.util.ArrayList;

/*
Goal Node:
1 2 3
4 5 6
7 8 -/0
 */

public class Node {
    public ArrayList<Node> children = new ArrayList<>();
    public Node parent;
    public int[] puzzle = new int[9];
    public int emptySpot;

    public int columnSize = 3;

    public Node(int[] inputPuzzle) {
        puzzle = inputPuzzle;
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

    //Expansion calls - **Body of Program**

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
    //int i = puzzle index

    public void moveLeft(int[] puzzle, int i){
        if(i % columnSize > 0) {
            int[] puzzleCopy = new int[9];
            copyPuzzle(puzzleCopy, puzzle);

            int temp = puzzleCopy[i - 1];
            puzzleCopy[i - 1] = puzzleCopy[i];
            puzzleCopy[i] = temp;

            Node child = new Node(puzzleCopy);
            children.add(child);
            child.parent = this;
        }

    }

    public void moveRight(int[] puzzle, int i){
        if(i % columnSize < columnSize - 1) {
            int[] puzzleCopy = new int[9];
            copyPuzzle(puzzleCopy, puzzle);

            int temp = puzzleCopy[i + 1];
            puzzleCopy[i + 1] = puzzleCopy[i];
            puzzleCopy[i] = temp;

            Node child = new Node(puzzleCopy);
            children.add(child);
            child.parent = this;
        }

    }

    public void moveUp(int[] puzzle, int i){
        if(i - columnSize >= 0) {
            int[] puzzleCopy = new int[9];
            copyPuzzle(puzzleCopy, puzzle);

            int temp = puzzleCopy[i - 3];
            puzzleCopy[i - 3] = puzzleCopy[i];
            puzzleCopy[i] = temp;

            Node child = new Node(puzzleCopy);
            children.add(child);
            child.parent = this;
        }
    }

    public void moveDown(int[] puzzle, int i){
        if(i + columnSize < puzzle.length) {
            int[] puzzleCopy = new int[9];
            copyPuzzle(puzzleCopy, puzzle);

            int temp = puzzleCopy[i + 3];
            puzzleCopy[i + 3] = puzzleCopy[i];
            puzzleCopy[i] = temp;

            Node child = new Node(puzzleCopy);
            children.add(child);
            child.parent = this;
        }
    }

    //Debugging
    public void printPuzzle() {
        System.out.println();
        int m = 0;
        for(int i = 0; i < columnSize; i++) { //column
            for(int j = 0; j < columnSize; j++) { //row
                System.out.print(puzzle[m] + " ");
                m++;
            }
            System.out.println();
        }
    }
}