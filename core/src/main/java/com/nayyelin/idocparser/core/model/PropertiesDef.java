package com.nayyelin.idocparser.core.model;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PropertiesDef {

    private String type;

    private String description;

    @JsonProperty("$ref")
    private String ref;

    private Map<String, PropertiesDef> properties;

    private PropertiesDef items;

    private Integer maxLength;
    private Integer minItems;
    private Integer maxItems;

    private List<String> required;

    public PropertiesDef() {
    }

    public PropertiesDef(String ref, String description) {
        this.ref = ref;
        this.description = description;
    }

    public PropertiesDef(String ref) {
        this.ref = ref;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public Map<String, PropertiesDef> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, PropertiesDef> properties) {
        this.properties = properties;
    }

    public PropertiesDef getItems() {
        return items;
    }

    public void setItems(PropertiesDef items) {
        this.items = items;
    }

    public Integer getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(Integer maxLength) {
        this.maxLength = maxLength;
    }

    public Integer getMinItems() {
        return minItems;
    }

    public void setMinItems(Integer minItems) {
        this.minItems = minItems;
    }

    public Integer getMaxItems() {
        return maxItems;
    }

    public void setMaxItems(Integer maxItems) {
        this.maxItems = maxItems;
    }

    public List<String> getRequired() {
        return required;
    }

    public void setRequired(List<String> required) {
        this.required = required;
    }
}
