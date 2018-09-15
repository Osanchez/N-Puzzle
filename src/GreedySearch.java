//TODO:Debug Me Please - Issue most likely related to Heuristic function calculations
import java.util.ArrayList;
import java.util.Collections;

public class GreedySearch {

    public int addedToFrontier = 0;
    public int expandedFromFrontier = 0;

    public GreedySearch() {

    }

    public ArrayList<Node> GreedySearch(Node root) {

        ArrayList pathToSolution = new ArrayList();
        ArrayList<Node> openList = new ArrayList();
        ArrayList closedList = new ArrayList();

        int minInt = Integer.MAX_VALUE;

        boolean goal = false;
        boolean solvable = root.solvable;

        openList.add(root);
        addedToFrontier++;

        Node currentNode = null;

        while(!openList.isEmpty() && !goal && solvable) {

            //gets the lowest costing node from the frontier
            for(Node node : openList) {
                if(node.h <= minInt) {
                    minInt = node.h;
                    currentNode = node;
                }
            }

            openList.remove(currentNode);
            closedList.add(currentNode);

            //expand currentNode
            currentNode.expandMove();
            expandedFromFrontier++;

            //TODO: Debug
            //currentNode.printPuzzle();

            for(int i = 0; i < currentNode.children.size(); i++) {
                Node currentChild = currentNode.children.get(i);

                //checks if children contain goal node
                if(currentChild.isGoalNode()) {
                    goal = true;
                    pathTrace(pathToSolution, currentChild);
                    System.out.println("Goal Found!");
                }

                //if not goal node and node has not been previously explored add to open list (frontier)
                if(!Contains(openList, currentChild) && !Contains(closedList, currentChild)) {
                    openList.add(currentChild);
                    addedToFrontier++;
                }
            }

        }

        return pathToSolution;
    }

    public void pathTrace(ArrayList<Node> path, Node n) {
        System.out.println("Greedy Search Path");
        Node current = n;
        path.add(current);

        while(current.parent != null) {
            current = current.parent;
            path.add(current);
        }
    }

    public static boolean Contains(ArrayList<Node> list, Node c) {
        boolean contains = false;
        for(int i = 0; i < list.size(); i++) {
            if(list.get(i).isSamePuzzle(c.puzzle)) {
                contains = true;
            }
        }
        return contains;
    }

    public static void main(String[] args) {

        IOHandler io = new IOHandler();
        //io.readFile(args[0]); //Command Line Input
        io.readFile("Files/boards.txt");
        ArrayList<int[]> boards = io.finalizedBoards;

        for (int[] board : boards) {
            Node rootNode = new Node(board);
            GreedySearch gs = new GreedySearch();
            ArrayList<Node> solution = gs.GreedySearch(rootNode);
            //System.out.println(rootNode.h);

            if(solution.size() > 0) {
                Collections.reverse(solution);
                for (int i = 0; i < solution.size(); i++) {
                    solution.get(i).printPuzzle();
                }
            } else {
                System.out.println();
                System.out.println("No Solution.");
            }
            //print metric data
            System.out.println();
            System.out.println("total nodes added to frontier: " + gs.addedToFrontier);
            System.out.println("total nodes expanded from frontier: " + gs.expandedFromFrontier);

            //reset metric data
            gs.addedToFrontier = 0;
            gs.expandedFromFrontier = 0;
        }

    }
}
