import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by K26151 on 6/6/2016.
 */
public class IssuesList {
    public Map<String, ArrayList<String>> map;

    public IssuesList() {
        map = new HashMap<>();
    }
    public IssuesList(String errorName, String errorPosition) {
        map = new HashMap<>();
        add(errorName, errorPosition);
    }

    //NOTE: position will have to be converted into int
    public void add(String errorName, String errorPosition) {
        if(map.containsKey(errorName)) {
            if(!map.get(errorName).contains(errorPosition)) {
                map.get(errorName).add(errorPosition);
            }
        } else {
            map.put(errorName, new ArrayList<>());
            map.get(errorName).add(errorPosition);
        }
    }

    public Set<String> getErrors() {
        if(map.keySet().isEmpty()) {
            System.out.println("No errors found");
        }
        return map.keySet();
    }

    public ArrayList<String> getPositions(String errorName) {
        if(map.containsKey(errorName)) {
            return map.get(errorName);
        } else {
            System.out.println("Error position(s) not found");
            return null;
        }
    }
}
