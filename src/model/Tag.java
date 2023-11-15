package model;
import java.io.Serializable;

/**
 * Represents a Tag with attributes.
 * 
 * @author Marc Rizzolo
 */

 
public class Tag implements Serializable{
    
    private static final long serialVersionUID = 1L;

    private String tagName;
    private String tagValue;

    public Tag(String tagName, String tagValue) {
        this.tagName = tagName;
        this.tagValue = tagValue;
    }

    public String getTagName() {
        return tagName;
    }

    public String getTagValue() {
        return tagValue;
    }

    @Override
    public String toString() {
        return tagName + "=" + tagValue;
    }
}
