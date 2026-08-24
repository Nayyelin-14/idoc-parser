package com.nayyelin.idocparser.core;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * Output result returned by your service.
 * <p>
 * This class represents the output data structure from the service execution.
 * Customize this class by adding your specific result properties.
 * </p>
 * <p>
 * Usage Example:
 * </p>
 * 
 * <pre>{@code
 * ParserServiceResult result = ParserServiceResult.builder()
 *         .output("processed output")
 *         .success(true)
 *         .build();
 * }</pre>
 *
 * @see ParserServiceRequest
 * @see ParserService
 */
public class ParserServiceResult {

    /** Service output (text, JSON, etc.) */
    private String output;

    /** Success flag */
    private boolean success;

    /** Parsed JSON schema */
    private JsonNode schema;

    /** Optional schema name used when saving to file */
    private String schemaName;

    /** Default constructor */
    public ParserServiceResult() {
    }

    /**
     * Full constructor.
     *
     * @param output     output string
     * @param schema     JSON schema
     * @param schemaName schema name
     * @param success    success flag
     */
    public ParserServiceResult(String output, JsonNode schema, String schemaName, boolean success) {
        this.output = output;
        this.schema = schema;
        this.schemaName = schemaName;
        this.success = success;
    }

    /*
     * =====================================================
     * Standard getters & setters
     * =====================================================
     */

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    /**
     * Internal canonical getter.
     */
    @JsonIgnore
    public JsonNode getSchema() {
        return schema;
    }

    public void setSchema(JsonNode schema) {
        this.schema = schema;
    }

    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
    }

    /*
     * =====================================================
     * Pipeline compatibility getters (IMPORTANT)
     * =====================================================
     */

    /**
     * Getter expected by pipeline reflection.
     * Alias of {@link #getSchema()}.
     */
    @JsonProperty("schema")
    public JsonNode getJsonSchema() {
        return schema;
    }

    /**
     * Getter expected by pipeline reflection.
     */
    public String getSchemaName() {
        return schemaName;
    }

    /*
     * =====================================================
     * Builder
     * =====================================================
     */

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private final ParserServiceResult result = new ParserServiceResult();

        public Builder output(String output) {
            result.output = output;
            return this;
        }

        public Builder success(boolean success) {
            result.success = success;
            return this;
        }

        public Builder schema(JsonNode schema) {
            result.schema = schema;
            return this;
        }

        public Builder schemaName(String schemaName) {
            result.schemaName = schemaName;
            return this;
        }

        public ParserServiceResult build() {
            return result;
        }
    }
}