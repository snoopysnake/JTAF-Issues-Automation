import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * Created by K26151 on 6/8/2016.
 */
public class JavaWriteTest {
    public static void main(String[] args) {
        Path editedFile = Paths.get("C:\\Users\\K26151\\Documents\\test.txt");
        String s = "blah";

        try (InputStream in = Files.newInputStream(editedFile);
             BufferedReader reader =
                     new BufferedReader(new InputStreamReader(in))) {
            String line = null;
            while ((line = reader.readLine()) != null) {
                byte data[] = s.getBytes();
                try (OutputStream out = new BufferedOutputStream(
                        Files.newOutputStream(editedFile,StandardOpenOption.WRITE))) {
                        out.write(data, 0, data.length);
                } catch (IOException x) {
                    System.err.println(x);
                }
            }
        } catch (IOException x) {
            System.err.println(x);
        }


    }
}
