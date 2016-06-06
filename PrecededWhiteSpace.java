import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by K26151 on 6/6/2016.
 */
public class PrecededWhiteSpace {
    public static void main(String[] args) throws Exception {
        String errorName = "'[keyword]' is not preceded with whitespace.";
        String location = "C:\\Users\\K26151\\Documents\\";
        ErrorParser parser = new ErrorParser("https://github.com/FINRAOS/JTAF-XCore/issues/66");
        for(String f:parser.getPaths()) {
            String keyword;
            for (String e : parser.getErrors(f)) {
                keyword = e.split("'")[1]; //gets keyword from error msg
                //prints locations
                ArrayList<String> positions = parser.getErrorPositions(f, e);
                System.out.print(f + ": " + "Fixing error \"" + e + "\" at ");
                ArrayList<Integer> listOfRows = new ArrayList<>();
                for (String p : positions) {
                    int row = ErrorParser.getRow(p);
                    int col = ErrorParser.getCol(p);
                    listOfRows.add(row);
                    System.out.print("[Line " + row + ", Col " + col + "], ");
                }
                System.out.println();
                File file = new File(location + f);
                Scanner fileScanner = new Scanner(file);

                int counter = 1; //line number
                int linesChanged = 0; //# of errors
                while (fileScanner.hasNext()) {
                    String line = fileScanner.nextLine();
                    //previous row
                    if (listOfRows.contains(counter)) {
                        line = line.replaceAll(keyword," " + keyword);
                        System.out.println(line);
                        linesChanged++;
                    }
                    counter++;
                }
                System.out.println("Lines changed in " + f + ": " + linesChanged);
            }
        }
    }
}
