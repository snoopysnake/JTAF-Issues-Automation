import java.io.File;
import java.util.Scanner;

/**
 * Created by K26151 on 6/3/2016.
 */
public class NewLineChecker {
    public static void main(String[] args) throws Exception {
        File file = new File("C:\\Users\\K26151\\Documents\\JTAF-XCore\\src\\main\\java\\org\\finra\\jtaf\\core\\parsing\\TestDataPlugin.java");

        Scanner fileScanner = new Scanner(file);
        int i = 1; //line number

        while(fileScanner.hasNextLine()) {
            String line = fileScanner.nextLine();
            line = line.replaceAll("\\s+",""); //removes whitespaces
            if(line.equals("{")) {
                System.out.println("Line " + i);
            }
            i++;
        }
    }
}
