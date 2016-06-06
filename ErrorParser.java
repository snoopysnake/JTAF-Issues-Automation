import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Admin on 6/3/2016.
 *
 * Error should be in following format "Line [#]: [file path]:[line #]:[column #]: error: [error message]
 */
public class ErrorParser {
    ArrayList<String> issues;
    ArrayList<String> paths;
    ArrayList<String> positions;
    ArrayList<String> errors;

    public ErrorParser() {
        issues = new ArrayList<>();
        paths = new ArrayList<>();
        positions = new ArrayList<>();
        errors = new ArrayList<>();
    }

    public ErrorParser(String url) throws Exception {
        issues = new ArrayList<>();
        paths = new ArrayList<>();
        positions = new ArrayList<>();
        errors = new ArrayList<>();
        read(url);
    }

    private void read(String url) throws Exception {
        URL issueURL = new URL(url);
        BufferedReader in = new BufferedReader(
        new InputStreamReader(issueURL.openStream()));

        String inputLine;

        while ((inputLine = in.readLine()) != null) {
            if (inputLine.contains("<div class=\"comment-body markdown-body markdown-format js-comment-body\">")) {
                while ((inputLine = in.readLine()) != null) {
                    if (inputLine.contains("</div>")) {
                        break;
                    }
                    //adds issues to list, removes unnecessary tags
                    inputLine = inputLine.replaceAll("<.*?>", "");
                    issues.add(inputLine);
                    //parse error message
                    String[] parsed = inputLine.split(" error: ");
                    if(parsed.length ==  2) {
                        String firstHalf = parsed[0];
                        String secondHalf = parsed[1];
                        String parseFirstHalf[] = firstHalf.split(":",2);
                        String path = parseFirstHalf[0];
                        String position = parseFirstHalf[1];
                        paths.add(path);
                        positions.add(position);
                        errors.add(secondHalf);
                    }
                }
            }
        }
        in.close();

        if(paths.size() != positions.size() || paths.size() != errors.size()) {
            System.out.println("Something went wrong.");
        }
    }

    public ArrayList<String> getIssues() {
        return issues;
    }

    public ArrayList<String> getPaths() {
        return paths;
    }
    public ArrayList<String> getPositions() {
        return positions;
    }
    public ArrayList<String> getErrors() {
        return errors;
    }

    public static int getRow(String pos) {
        String parse[] = pos.split(":");
        return Integer.parseInt(parse[0]);
    }

    public static int getCol(String pos) {
        String parse[] = pos.split(":");
        System.out.println("length" + parse.length);
        if(parse.length > 1) {
            return Integer.parseInt(parse[1]);
        }
        return -1;
    }
}
