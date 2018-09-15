//TODO:Debug Me Please
import java.util.ArrayList;
import java.util.Collections;

public class AStarSearch {

    public int addedToFrontier = 0;
    public int expandedFromFrontier = 0;

    public AStarSearch() {

    }

    public ArrayList<Node> AStarSearch(Node root) {

        ArrayList pathToSolution = new ArrayList();
        ArrayList<Node> openList = new ArrayList();
        ArrayList closedList = new ArrayList();

        int minInt = Integer.MAX_VALUE;

        boolean goal = false;
        boolean solvable = root.solvable;

        openList.add(root);
        addedToFrontier++;

        while(!openList.isEmpty() && !goal && solvable) {

            Node currentNode = null;

            //gets the lowest costing node
            for(Node node : openList) {
                if(node.f <= minInt) {
                    minInt = node.f;
                    currentNode = node;
                }
            }

            if(currentNode.h <= 0) {
                goal = true;
                pathTrace(pathToSolution, currentNode);
                System.out.println();
                System.out.println("Goal Found!");
            }


            closedList.add(currentNode);
            openList.remove(currentNode);

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
                    System.out.println();
                    System.out.println("Goal Found!");
                }

                //add the least cost child to the open list
                if(!Contains(openList, currentChild) && !Contains(closedList, currentChild)) {
                    openList.add(currentChild);
                    addedToFrontier++;
                }
            }

        }

        return pathToSolution;
    }

    public void pathTrace(ArrayList<Node> path, Node n) {
        System.out.println("A* Search Path");
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
            AStarSearch as = new AStarSearch();
            ArrayList<Node> solution = as.AStarSearch(rootNode);
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
        }

    }
}
