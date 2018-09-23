import java.util.ArrayList;
import java.util.Collections;

//Breadth-First Search
public class uninformedSearch {

    public int addedToFrontier = 0;
    public int expandedFromFrontier = 0;

    public uninformedSearch() {

    }

    public ArrayList<Node> breadthFirstSearch(Node root) {

        ArrayList pathToSolution = new ArrayList();
        ArrayList<Node> openList = new ArrayList();
        ArrayList closedList = new ArrayList();

        boolean goal = false;
        boolean solvable = root.isSolvable();

        if(!solvable) {
            return pathToSolution;
        }

        openList.add(root);
        addedToFrontier++;

        while(!openList.isEmpty() && !goal) {
            Node currentNode = openList.get(0);
            closedList.add(currentNode);
            openList.remove(0);

            currentNode.expandMove();
            expandedFromFrontier++;

            //TODO: Debug
            //currentNode.printPuzzle();

            for(int i = 0; i < currentNode.children.size(); i++) {
                Node currentChild = currentNode.children.get(i);
                if(currentChild.isGoalNode()) {
                    System.out.println();
                    System.out.println("Goal Found!");
                    goal = true;
                    pathTrace(pathToSolution, currentChild);
                }
                if(!Contains(openList, currentChild) && !Contains(closedList, currentChild)) {
                    openList.add(currentChild);
                    addedToFrontier++;
                }

            }
        }
        return pathToSolution;
    }

    public void pathTrace(ArrayList<Node> path, Node n) {
        System.out.println("Breadth-First Search Path");
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
            uninformedSearch ui = new uninformedSearch();

            ArrayList<Node> solution = ui.breadthFirstSearch(rootNode);

            if (solution.size() > 0) {
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
            System.out.println("total nodes added to frontier: " + ui.addedToFrontier);
            System.out.println("total nodes expanded from frontier: " + ui.expandedFromFrontier);

            //reset metric data
            ui.addedToFrontier = 0;
            ui.expandedFromFrontier = 0;
        }


    }
}
