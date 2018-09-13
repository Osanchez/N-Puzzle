import java.util.ArrayList;
import java.util.Collections;

public class Main {
    public static void main(String[] args) {

    IOHandler io = new IOHandler();
    io.readFile("Files/boards.txt");
    ArrayList<int[]> boards = io.finalizedBoards;

    for(int[] board: boards) {
        Node rootNode = new Node(board);
        Uninformed ui = new Uninformed();

        ArrayList<Node> solution = ui.breadthFirstSeach(rootNode);

        if(solution.size() > 0) {
            Collections.reverse(solution);
            for(int i = 0 ; i < solution.size(); i++) {
                solution.get(i).printPuzzle();
            }
        } else {
            System.out.println("No path to the solution was found.");
        }
    }


    }
}
