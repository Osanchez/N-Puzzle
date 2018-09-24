import java.text.DecimalFormat;
import java.util.*;

public class GreedySearch {

    public int addedToFrontier = 0;
    public int expandedFromFrontier = 0;
    public int[] maximumSize = {0,0};

    public GreedySearch() {

    }

    public ArrayList<Node> GreedySearch(Node root) {

        ArrayList pathToSolution = new ArrayList();
        LinkedHashMap<String, Node> openList = new LinkedHashMap<>();
        LinkedHashMap<String, Node> closedList = new LinkedHashMap<>();

        int minInt = Integer.MAX_VALUE;

        boolean goal = false;
        boolean solvable = root.isSolvable();

        if(!solvable) {
            return pathToSolution;
        }

        openList.put(Arrays.toString(root.puzzle), root);
        addedToFrontier++;

        Node currentNode = null;
        int timeStep = 0;
        while(!openList.isEmpty() && !goal) {

            if(expandedFromFrontier > 100000) {
                System.out.println("100k limit reached");
                return pathToSolution;
            }

            timeStep++;

            if(maximumSize[1] < openList.size()) {
                maximumSize[0] = timeStep;
                maximumSize[1] = openList.size();
            }

            //gets the lowest costing node from the frontier
            for(Node node : openList.values()) {
                if(node.h <= minInt) {
                    minInt = node.h;
                    currentNode = node;
                }
                else {
                    minInt = Integer.MAX_VALUE;
                }
            }

            closedList.put(Arrays.toString(currentNode.puzzle), currentNode);
            openList.remove(Arrays.toString(currentNode.puzzle));

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
                if (!openList.containsKey(Arrays.toString(currentChild.puzzle)) && !closedList.containsKey(Arrays.toString(currentChild.puzzle))) {
                    openList.put(Arrays.toString(currentChild.puzzle), currentChild);
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

    public static void main(String[] args) {

        IOHandler io = new IOHandler();
        io.readFile(args[0]); //Command Line Input
        //io.readFile("Files/boards.txt");
        ArrayList<int[]> boards = io.finalizedBoards;

        for (int[] board : boards) {
            Node rootNode = new Node(board);
            GreedySearch gs = new GreedySearch();

            long startTime = System.nanoTime();
            ArrayList<Node> solution = gs.GreedySearch(rootNode);
            long endTime   = System.nanoTime();
            double totalTime = (double) (endTime - startTime) / 1000000000.0;
            DecimalFormat df = new DecimalFormat("###.###");
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
            System.out.println("Maximum size of the search queue at any given time: " + gs.maximumSize[1] + " at time step " + gs.maximumSize[0]);
            System.out.println("total time: " + df.format(totalTime) + " Seconds");

            //reset metric data
            gs.addedToFrontier = 0;
            gs.expandedFromFrontier = 0;
        }

    }
}
