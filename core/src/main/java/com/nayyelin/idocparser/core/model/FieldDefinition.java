package com.nayyelin.idocparser.core.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "type", "maxLength", "description" })
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FieldDefinition {

    private final String jsonType;
    private final int maxLength;

    private final String description;

    public FieldDefinition(String sapType, int length, String description) {
        this.jsonType = mapSapType(sapType);
        this.maxLength = length;
        this.description = description;
    }

    private String mapSapType(String sapType) {
        if (sapType == null)
            return "string";
        return switch (sapType.toUpperCase()) {
            case "CHARACTER", "CHAR", "STRING" -> "string";
            case "NUMERIC", "NUM", "DEC", "INTEGER" -> "number";
            case "DATE", "DATS" -> "string";
            default -> "string";
        };
    }

    @JsonProperty("type")
    public String getType() {
        return jsonType;
    }

    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    public int getMaxLength() {
        return maxLength;
    }
}
