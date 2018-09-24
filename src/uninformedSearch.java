import java.text.DecimalFormat;
import java.util.*;

//Breadth-First Search
public class uninformedSearch {

    public int addedToFrontier = 0;
    public int expandedFromFrontier = 0;
    public int[] maximumSize = {0,0};

    public uninformedSearch() {

    }

    public ArrayList<Node> breadthFirstSearch(Node root) {

        ArrayList pathToSolution = new ArrayList();
        LinkedHashMap<String, Node> openList = new LinkedHashMap<>();
        LinkedHashMap<String, Node> closedList = new LinkedHashMap<>();

        boolean goal = false;
        boolean solvable = root.isSolvable();

        if(!solvable) {
            return pathToSolution;
        }

        openList.put(Arrays.toString(root.puzzle), root);
        addedToFrontier++;
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

            Map.Entry<String, Node> entry = openList.entrySet().iterator().next();
            String key = entry.getKey();
            Node currentNode = openList.get(key);

            closedList.put(Arrays.toString(currentNode.puzzle), currentNode);
            openList.remove(Arrays.toString(currentNode.puzzle));

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
                if (!openList.containsKey(Arrays.toString(currentChild.puzzle)) && !closedList.containsKey(Arrays.toString(currentChild.puzzle))) {
                    openList.put(Arrays.toString(currentChild.puzzle), currentChild);
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

    public static void main(String[] args) {

        IOHandler io = new IOHandler();
        io.readFile(args[0]); //Command Line Input
        //io.readFile("Files/board.txt");
        ArrayList<int[]> boards = io.finalizedBoards;

        for (int[] board : boards) {
            Node rootNode = new Node(board);
            uninformedSearch ui = new uninformedSearch();

            long startTime = System.nanoTime();
            ArrayList<Node> solution = ui.breadthFirstSearch(rootNode);
            long endTime   = System.nanoTime();
            double totalTime = (double) (endTime - startTime) / 1000000000.0;
            DecimalFormat df = new DecimalFormat("###.###");

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
            System.out.println("Maximum size of the search queue at any given time: " + ui.maximumSize[1] + " at time step " + ui.maximumSize[0]);
            System.out.println("total time: " + df.format(totalTime) + " Seconds");

            //reset metric data
            ui.addedToFrontier = 0;
            ui.expandedFromFrontier = 0;
        }


    }
}
