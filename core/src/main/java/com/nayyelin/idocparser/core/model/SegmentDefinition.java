package com.nayyelin.idocparser.core.model;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({
        "type",
        "description",
        "properties",
        "required"
})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SegmentDefinition {

    private String type;
    private String description;
    private Map<String, Object> properties;  // Changed from FieldDefinition to Object
    private List<String> required;  // Added for mandatory fields/segments
    private Integer level;  // Keep for metadata if needed

    public SegmentDefinition(String type, String description,
            Map<String, Object> properties, List<String> required) {
        this.type = type;
        this.description = description;
        this.properties = properties;
        this.required = required;
    }

    // Legacy constructor for backward compatibility
    public SegmentDefinition(String type, String description,
            Map<String, Object> properties, Integer level) {
        this.type = type;
        this.description = description;
        this.properties = properties;
        this.level = level;
        this.required = null;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getRequired() {
        return required;
    }

    public void setRequired(List<String> required) {
        this.required = required;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }
}