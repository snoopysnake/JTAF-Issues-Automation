import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by Admin on 6/3/2016.
 *
 * Error should be in following format "Line [#]: [file path]:[line #]:[column #]: error: [error message]
 */
public class ErrorParser {
    ArrayList<String> issues;
    HashMap<String, IssuesList> map;

    public ErrorParser() {
        issues = new ArrayList<>();
        map = new HashMap<>();
    }

    public ErrorParser(String url) throws Exception {
        issues = new ArrayList<>();
        map = new HashMap<>();
        read(url);
    }

    private void read(String url) throws Exception {
        //reads from Github, replace this later
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("proxye1.finra.org", 8080));
        URL issueURL = new URL(url);
        HttpURLConnection uc = (HttpURLConnection)issueURL.openConnection(proxy);
        uc.connect();

        BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream()));

        String inputLine;

        while ((inputLine = in.readLine()) != null) {
            if (inputLine.contains("<div class=\"comment-body markdown-body markdown-format js-comment-body\">")) {
                while ((inputLine = in.readLine()) != null) {
                    if (inputLine.contains("</div>")) {
                        break;
                    }
                    //adds issues to list, removes unnecessary tags
                    inputLine = inputLine.replaceAll("<.*?>", "");
                    inputLine = inputLine.replaceAll("^\\s+", "");
                    inputLine = inputLine.replaceAll("^Line (\\d+): ","");
                    issues.add(inputLine);
                    //parse error message
                    String[] parsed = inputLine.split(" error: ");
                    if(parsed.length ==  2) {
                        String filePos = parsed[0];
                        String errorMessage = parsed[1];
                        //replace numbers in error message
                        errorMessage = errorMessage.replaceAll("\\d+","[number]");
                        String parse[] = filePos.split(":",2);
                        String path = parse[0];
                        String position = parse[1];
                        //checks if it is duplicate file
                        if(map.containsKey(path)) {
                            map.get(path).add(errorMessage, position);
                        } else {
                            map.put(path, new IssuesList(errorMessage, position));
                        }
                    }
                }
            }
        }
        in.close();
    }

    public ArrayList<String> getIssues() {
        return issues;
    }

    public Set<String> getPaths() {
        return map.keySet();
    }

    public Set<String> getErrors(String fileName) {
        if(map.containsKey(fileName)) {
            return map.get(fileName).getErrors();
        }
        System.out.println("Cannot find errors");
        return null;
    }

    public ArrayList<String> getErrorPositions(String fileName, String errorName) {
        if(map.containsKey(fileName)) {
            if(map.get(fileName).getErrors().contains(errorName)) {
                return map.get(fileName).getPositions(errorName);
            }
        }
        return null;
    }

    public static int getRow(String pos) {
        String parse[] = pos.split(":");
        return Integer.parseInt(parse[0]);
    }

    public static int getCol(String pos) {
        String parse[] = pos.split(":");
        if(parse.length > 1) {
            return Integer.parseInt(parse[1]);
        }
        return -1;
    }
}
