public class Main {
    public static void main(String[] args) {
/*      int[] puzzle = {
                1,2,4,
                3,0,5,
                7,6,8
        };

        Node rootNode = new Node(puzzle);
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
*/

    IOHandler io = new IOHandler();
    io.readFile("Files/boards.txt");
    }
}
