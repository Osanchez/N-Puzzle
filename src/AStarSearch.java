import java.text.DecimalFormat;
import java.util.*;

public class AStarSearch {
    public int addedToFrontier = 0;
    public int expandedFromFrontier = 0;
    public int[] maximumSize = {0,0};

    public AStarSearch() {

    }

    public ArrayList<Node> AStarSearch(Node root) {

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

            for(Node node : openList.values()) {
                node.calculatef(root.puzzle);
                if(node.f <= minInt) {
                    minInt = node.f;
                    currentNode = node;
                }
                else {
                    minInt = Integer.MAX_VALUE;
                }
            }

            openList.remove(Arrays.toString(currentNode.puzzle));
            closedList.put(Arrays.toString(currentNode.puzzle), currentNode);

            //expand currentNode
            currentNode.expandMove();
            expandedFromFrontier++;


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
        System.out.println("A* Search Path");
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
            AStarSearch as = new AStarSearch();

            long startTime = System.nanoTime();
            ArrayList<Node> solution = as.AStarSearch(rootNode);
            long endTime   = System.nanoTime();
            double totalTime = (double) (endTime - startTime) / 1000000000.0;
            DecimalFormat df = new DecimalFormat("###.###");
            //System.out.println(rootNode.h);

            if(solution.size() > 0) {
                Collections.reverse(solution);
                for (int i = 0; i < solution.size(); i++) {
                    solution.get(i).printPuzzle();
                    //System.out.println("Path Cost: " + solution.get(i).f);

                }
            } else {
                System.out.println();
                System.out.println("No Solution.");
            }
            //print metric data
            System.out.println();
            System.out.println("total nodes added to frontier: " + as.addedToFrontier);
            System.out.println("total nodes expanded from frontier: " + as.expandedFromFrontier);
            System.out.println("Maximum size of the search queue at any given time: " + as.maximumSize[1] + " at time step " + as.maximumSize[0]);
            System.out.println("total time: " + df.format(totalTime) + " Seconds");

            //reset metric data
            as.addedToFrontier = 0;
            as.expandedFromFrontier = 0;
        }

    }
}
