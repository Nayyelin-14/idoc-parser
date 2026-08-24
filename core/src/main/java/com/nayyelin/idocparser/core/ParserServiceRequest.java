package com.nayyelin.idocparser.core;

import java.io.InputStream;

import com.nayyelin.idocparser.core.model.SchemaMetadata;

/**
 * Input request for your service.
 * <p>
 * This class represents the input data structure for the service execution.
 * Customize this class by adding your specific properties and validation logic.
 * </p>
 * <p>
 * Usage Example:
 * </p>
 * 
 * <pre>{@code
 * ParserServiceRequest request = ParserServiceRequest.builder()
 *         .input("example input")
 *         .build();
 * }</pre>
 *
 * @see ParserServiceResult
 * @see ParserService
 */
public class ParserServiceRequest {

    private String input;
    private InputStream inputFile;
    private SchemaMetadata schemaMetadata;

    /**
     * Default constructor.
     */
    public ParserServiceRequest() {
    }

    /**
     * Constructor with input parameter.
     *
     * @param input the input string
     */
    public ParserServiceRequest(String input) {
        this.input = input;
    }

    /**
     * Gets the input string.
     *
     * @return the input string
     */
    public String getInput() {
        return input;
    }

    /**
     * Sets the input string.
     *
     * @param input the input string to set
     */
    public void setInput(String input) {
        this.input = input;
    }

    public SchemaMetadata getSchemaMetadata() {
        return schemaMetadata;
    }

    public void setSchemaMetadata(SchemaMetadata schemaMetadata) {
        this.schemaMetadata = schemaMetadata;
    }

    /**
     * Creates a new builder instance for constructing ParserServiceRequest objects.
     * <p>
     * The builder pattern provides a fluent interface for object construction.
     * </p>
     *
     * @return a new Builder instance
     */
    public static Builder builder() {
        return new Builder();
    }

    public InputStream getInputFile() {
        return inputFile;
    }

    public void setInputFile(InputStream inputFile) {
        this.inputFile = inputFile;
    }

    /**
     * Builder class for ParserServiceRequest.
     * <p>
     * Provides a fluent interface for constructing ParserServiceRequest objects
     * with validation and default values.
     * </p>
     */
    public static class Builder {
        /** The request being built. */
        private final ParserServiceRequest request = new ParserServiceRequest();

        /**
         * Creates a new Builder instance.
         */
        public Builder() {
        }

        /**
         * Sets the input string.
         *
         * @param input the input string
         * @return this builder instance for method chaining
         */
        public Builder input(String input) {
            request.input = input;
            return this;
        }

        public Builder schemaMetadata(SchemaMetadata schemaMetadata) {
            request.schemaMetadata = schemaMetadata;
            return this;
        }

        /**
         * Builds and returns the ParserServiceRequest instance.
         * <p>
         * Validates required fields before returning the instance.
         * </p>
         *
         * @return the constructed ParserServiceRequest instance
         * @throws IllegalStateException if required fields are missing
         */
        public ParserServiceRequest build() {
            // TODO: Add validation for required fields
            // if (request.input == null) {
            // throw new IllegalStateException("Input is required");
            // }
            return request;
        }

        public Builder inputStream(InputStream is) {
            request.inputFile = is;
            return this;
        }
    }
}
