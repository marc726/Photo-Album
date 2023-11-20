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
    private Set<String> restrictedTagTypes;

    /**
     * The GlobalTags class represents a utility class for managing global tags.
     * It provides methods for initializing and managing tag types.
     */
    private GlobalTags() {
        tagTypes = new HashSet<>(); // Initialize the tagTypes set
        restrictedTagTypes = new HashSet<>();
    }

    /**
     * Returns the singleton instance of the GlobalTags class.
     * If the instance does not exist, it is created.
     *
     * @return the singleton instance of the GlobalTags class
     */
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

        /**
     * Returns the set of restricted tag types.
     *
     * @return the set of restricted tag types
     */
    public Set<String> getRestrictedTagTypes() {
        return restrictedTagTypes;
    }

    /**
     * Adds a new restricted tag type to the restrictedTagTypes set.
     *
     * @param restrictedTagType the tag type to be added as restricted
     */
    public void addRestrictedTagType(String restrictedTagType) {
        restrictedTagTypes.add(restrictedTagType);
    }

    /**
     * Checks if a tag type is restricted.
     *
     * @param tagType the tag type to check
     * @return true if the tag type is restricted, false otherwise
     */
    public boolean isTagTypeRestricted(String tagType) {
        return restrictedTagTypes.contains(tagType);
    }

    /**
     * Sets the restricted tag types for this object.
     * 
     * @param restrictedTagTypes the set of restricted tag types to be set
     * 
     */
    public void setRestrictedTagTypes(Set<String> restrictedTagTypes) {
        this.restrictedTagTypes = restrictedTagTypes;
    }

}
