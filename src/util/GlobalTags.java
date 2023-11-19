/**
 * @author Marc Rizzolo
 */

package util;

import java.util.HashSet;
import java.util.Set;

public class GlobalTags {
    private static GlobalTags instance;
    private Set<String> tagTypes;

    private GlobalTags() {
        tagTypes = new HashSet<>(); // Initialize the tagTypes set
    }

    public static synchronized GlobalTags getInstance() {
        if (instance == null) {
            instance = new GlobalTags(); // Create a new instance if it doesn't exist
        }
        return instance; // Return the instance
    }

    public Set<String> getTagTypes() {
        return tagTypes; // Return the tagTypes set
    }

    public void setTagTypes(Set<String> tagTypes) {
        this.tagTypes = tagTypes; // Set the tagTypes set
    }

    public void addTagType(String tagType) {
        tagTypes.add(tagType); // Add a new tagType to the tagTypes set
    }
}
