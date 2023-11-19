/**
 * @author Marc Rizzolo
 */

package util;

import java.util.HashSet;
import java.util.Set;

/**
 * The GlobalTags class represents a singleton instance that stores a set of tag types.
 * It provides methods to access and modify the tag types.
 */
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

    /**
     * Returns the set of tag types.
     *
     * @return the set of tag types
     */
    public Set<String> getTagTypes() {
        return tagTypes; // Return the tagTypes set
    }

    /**
     * Sets the tag types for this object.
     *
     * @param tagTypes the set of tag types to be set
     */
    public void setTagTypes(Set<String> tagTypes) {
        this.tagTypes = tagTypes; // Set the tagTypes set
    }

    /**
     * Adds a new tag type to the tagTypes set.
     *
     * @param tagType the tag type to be added
     */
    public void addTagType(String tagType) {
        tagTypes.add(tagType); // Add a new tagType to the tagTypes set
    }
}
