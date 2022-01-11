package utils;

import org.apache.maven.model.Dependency;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListUtil {

    public static Map<String,Integer> frequencyOfListElements(List<Dependency> items) {
        if (items == null || items.size() == 0) return null;
        Map<String, Integer> map = new HashMap<String, Integer>();
        for (Dependency temp : items) {
            String tempID = temp.getGroupId() + "-" + temp.getArtifactId();
            Integer count = map.get(tempID);
            map.put(tempID, (count == null) ? 1 : count + 1);
        }
        return map;
    }
}
