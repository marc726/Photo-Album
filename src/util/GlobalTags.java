package util;

import java.util.HashSet;
import java.util.Set;

public class GlobalTags {
    private static GlobalTags instance;
    private Set<String> tagTypes;

    private GlobalTags() {
        tagTypes = new HashSet<>();
    }

    public static synchronized GlobalTags getInstance() {
        if (instance == null) {
            instance = new GlobalTags();
        }
        return instance;
    }

    public Set<String> getTagTypes() {
        return tagTypes;
    }

    public void setTagTypes(Set<String> tagTypes) {
        this.tagTypes = tagTypes;
    }

    public void addTagType(String tagType) {
        tagTypes.add(tagType);
    }
}
