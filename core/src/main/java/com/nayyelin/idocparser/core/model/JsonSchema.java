package com.nayyelin.idocparser.core.model;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({
    "$schema",
    "title",
    "description",
    "x-schemaMetadata",
    "type",
    "properties",
    "required",
    "$defs"
})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JsonSchema {

    @JsonProperty("$schema")
    private String schema;

    private String title;
    private String description;

    @JsonProperty("x-schemaMetadata")
    private SchemaMetadata schemaMetadata;

    private String type;
    private Map<String, PropertiesDef> properties;
    private List<String> required;

    @JsonProperty("$defs")
    private Map<String, SegmentDefinition> defs;

    public JsonSchema(String schema, String title, String description,
                     String type, Map<String, PropertiesDef> properties,
                     List<String> required,
                     Map<String, SegmentDefinition> defs) {
        this.schema = schema;
        this.title = title;
        this.description = description;
        this.type = type;
        this.properties = properties;
        this.required = required;
        this.defs = defs;
    }

    public String getSchema() { return schema; }
    public void setSchema(String schema) { this.schema = schema; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public SchemaMetadata getSchemaMetadata() { return schemaMetadata; }
    public void setSchemaMetadata(SchemaMetadata schemaMetadata) { this.schemaMetadata = schemaMetadata; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public Map<String, PropertiesDef> getProperties() { return properties; }
    public void setProperties(Map<String, PropertiesDef> properties) { this.properties = properties; }

    public List<String> getRequired() { return required; }
    public void setRequired(List<String> required) { this.required = required; }

    public Map<String, SegmentDefinition> getDefs() { return defs; }
    public void setDefs(Map<String, SegmentDefinition> defs) { this.defs = defs; }
}
