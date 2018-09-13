import java.util.ArrayList;

//Breadth-First Search
public class Uninformed {

    public Uninformed() {

    }

    public ArrayList<Node> breadthFirstSeach(Node root) {
        ArrayList pathToSolution = new ArrayList();
        ArrayList<Node> openList = new ArrayList();
        ArrayList<Node> closedList = new ArrayList();

        openList.add(root);
        boolean goal = false;

        while(!openList.isEmpty() && !goal) {
            Node currentNode = openList.get(0);
            closedList.add(currentNode);
            openList.remove(0);

            currentNode.expandMove();
            //TODO: Debugging
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
}
