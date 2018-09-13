import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class IOHandler {
    public ArrayList<int[]> finalizedBoards = new ArrayList<>();

    public void readFile(String filePath) {
        try {
            File file = new File(filePath); //Pathname
            BufferedReader br = new BufferedReader(new FileReader(file));

            String st;
            ArrayList<String[]> boardLines = new ArrayList<>();

            //read all lines of file
            while((st = br.readLine()) != null) {
               boardLines.add(st.replace(".", "0").split(" "));
            }

            //create finalized boards
            ArrayList<Integer> board = new ArrayList<>();

            for(int x = 0; x < boardLines.size(); x++) {
                if(boardLines.get(x).length > 1) {
                    for(String boardPiece: boardLines.get(x)) {
                        board.add(Integer.parseInt(boardPiece));
                    }
                }
                if (boardLines.get(x).length == 1 || x == boardLines.size() - 1){ //ensures last board of file is read
                    int[] finalBoard = new int[board.size()];
                    for(int y = 0; y < board.size(); y++) {
                        finalBoard[y] = board.get(y);
                    }
                    finalizedBoards.add(finalBoard);
                    board = new ArrayList<>();
                }
            }

            br.close();

            for(int[] aBoard: finalizedBoards) {
                System.out.println(Arrays.toString(aBoard));
            }

            System.out.println();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
