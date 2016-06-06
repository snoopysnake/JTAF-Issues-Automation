import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Set;

/**
 * Created by K26151 on 6/3/2016.
 */
public class NewLineChecker {
    public static void main(String[] args) throws Exception {
//        String fileName = "JTAF-XCore\\src\\main\\java\\org\\finra\\jtaf\\core\\parallel\\MasterSuiteRunnersBuilder.java";

        String errorName = "'{' at column [number] should be on the previous line.";
        String location = "C:\\Users\\K26151\\Documents\\";
        ErrorParser parser = new ErrorParser("https://github.com/FINRAOS/JTAF-XCore/issues/67");
        for (String f: parser.getPaths()) {
            for (String e: parser.getErrors(f)) {
                ArrayList<String> positions = parser.getErrorPositions(f,e);
                System.out.print(f + ": " + "Fixing error \"" + e + "\" at ");
                ArrayList<Integer> listOfRows = new ArrayList<>();
                ArrayList<Integer> listOfCols = new ArrayList<>();
                ArrayList<Integer> listOfPreviousRows = new ArrayList<>();
                ArrayList<Integer> listOfPreviousCols = new ArrayList<>();
                for (String p: positions) {
                    int row = ErrorParser.getRow(p);
                    int col = ErrorParser.getCol(p);
                    listOfRows.add(row);
                    listOfPreviousRows.add(row-1);
                    System.out.print("[Line " + row + ", Col " + col + "], ");
                }
                System.out.println();
                File file = new File(location + f);
                Scanner fileScanner = new Scanner(file);

                int counter = 1; //line number
                int linesChanged = 0; //# of errors
                while(fileScanner.hasNext()) {
                    String line = fileScanner.nextLine();
                    //previous row
                    if(listOfPreviousRows.contains(counter)) {
                        line = line.trim();
                        line += " {";
                        linesChanged++;
                    }
                    if(!listOfRows.contains(counter)) {
                        System.out.println(line);
                    }
                    counter++;
                }
                System.out.println("Lines changed in " + f + ": " + linesChanged);
            }
        }

//        for (String error: parser.getErrors(fileName)) {
//            ArrayList<String> vals = parser.getErrorPositions(fileName, error);
//            System.out.println(error + " " + vals);
//        }

    }
}
