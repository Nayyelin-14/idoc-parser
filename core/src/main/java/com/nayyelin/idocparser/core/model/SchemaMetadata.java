package com.nayyelin.idocparser.core.model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({
        "id",
        "projectionId",
        "organisationCode",
        "version",
        "createdAt",
        "representation",
        "specification",
        "specVersion",
        "code",
        "documentType",
        "modelName",
        "category",
        "baseStandardId",
        "customerName",
        "aliases",
        "tags",
        "summary"
})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SchemaMetadata {

    // --- 13 base fields (all from API) ---

    private String id;
    private String version;
    private String createdAt;
    private String representation;
    private String specification;
    private String specVersion;
    private String code;
    private String documentType;
    private String modelName;
    private String category;
    private String baseStandardId;
    private String projectionId;
    private String customerName;
    private String organisationCode;

    // --- 3 RAG fields (auto-generated) ---

    private Map<String, String> aliases;
    private List<String> tags;
    private String summary;

    public SchemaMetadata() {
    }

    private SchemaMetadata(Builder builder) {
        this.id = builder.id;
        this.version = builder.version;
        this.createdAt = builder.createdAt;
        this.representation = builder.representation;
        this.specification = builder.specification;
        this.specVersion = builder.specVersion;
        this.code = builder.code;
        this.documentType = builder.documentType != null ? builder.documentType : "";
        this.modelName = builder.modelName;
        this.category = builder.category;
        this.baseStandardId = builder.baseStandardId;
        this.projectionId = builder.projectionId;
        this.customerName = builder.customerName;
        this.organisationCode = builder.organisationCode;
    }

    /**
     * Populates the RAG fields (aliases, tags, summary) from the 13 base fields.
     * Called after base fields are set. Does not overwrite if already populated.
     */
    public void populateRagFields() {
        if (aliases == null) {
            Map<String, String> a = new LinkedHashMap<>();
            if (code != null && !code.isBlank()) {
                a.put("messageType", code);
            }
            if (specVersion != null && !specVersion.isBlank()) {
                a.put("idocType", specVersion);
            }
            if (!a.isEmpty()) {
                aliases = a;
            }
        }

        if (tags == null) {
            List<String> t = new ArrayList<>();
            t.add("sap");
            t.add("idoc");
            if (specVersion != null && !specVersion.isBlank()) {
                t.add(specVersion.toLowerCase());
            }
            if (code != null && !code.isBlank()) {
                t.add(code.toLowerCase());
            }
            tags = t;
        }

        if (summary == null) {
            StringBuilder sb = new StringBuilder("SAP IDOC");
            if (specVersion != null && !specVersion.isBlank()) {
                sb.append(" ").append(specVersion);
            }
            if (code != null && !code.isBlank()) {
                sb.append(" - ").append(code);
            }
            if (documentType != null && !documentType.isBlank()) {
                sb.append(" (").append(documentType).append(")");
            }
            summary = sb.toString();
        }
    }

    // --- Getters & Setters ---

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getVersion() { return version; }
    public void setVersion(String version) { this.version = version; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    public String getRepresentation() { return representation; }
    public void setRepresentation(String representation) { this.representation = representation; }

    public String getSpecification() { return specification; }
    public void setSpecification(String specification) { this.specification = specification; }

    public String getSpecVersion() { return specVersion; }
    public void setSpecVersion(String specVersion) { this.specVersion = specVersion; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getDocumentType() { return documentType; }
    public void setDocumentType(String documentType) { this.documentType = documentType; }

    public String getModelName() { return modelName; }
    public void setModelName(String modelName) { this.modelName = modelName; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getBaseStandardId() { return baseStandardId; }
    public void setBaseStandardId(String baseStandardId) { this.baseStandardId = baseStandardId; }

    public String getProjectionId() { return projectionId; }
    public void setProjectionId(String projectionId) { this.projectionId = projectionId; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getOrganisationCode() { return organisationCode; }
    public void setOrganisationCode(String organisationCode) { this.organisationCode = organisationCode; }

    public Map<String, String> getAliases() { return aliases; }
    public void setAliases(Map<String, String> aliases) { this.aliases = aliases; }

    public List<String> getTags() { return tags; }
    public void setTags(List<String> tags) { this.tags = tags; }

    public String getSummary() { return summary; }
    public void setSummary(String summary) { this.summary = summary; }

    // --- Builder ---

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String id;
        private String version;
        private String createdAt;
        private String representation;
        private String specification;
        private String specVersion;
        private String code;
        private String documentType;
        private String modelName;
        private String category;
        private String baseStandardId;
        private String projectionId;
        private String customerName;
        private String organisationCode;

        private Builder() {
        }

        public Builder id(String id) { this.id = id; return this; }
        public Builder version(String version) { this.version = version; return this; }
        public Builder createdAt(String createdAt) { this.createdAt = createdAt; return this; }
        public Builder representation(String representation) { this.representation = representation; return this; }
        public Builder specification(String specification) { this.specification = specification; return this; }
        public Builder specVersion(String specVersion) { this.specVersion = specVersion; return this; }
        public Builder code(String code) { this.code = code; return this; }
        public Builder documentType(String documentType) { this.documentType = documentType; return this; }
        public Builder modelName(String modelName) { this.modelName = modelName; return this; }
        public Builder category(String category) { this.category = category; return this; }
        public Builder baseStandardId(String baseStandardId) { this.baseStandardId = baseStandardId; return this; }
        public Builder projectionId(String projectionId) { this.projectionId = projectionId; return this; }
        public Builder customerName(String customerName) { this.customerName = customerName; return this; }
        public Builder organisationCode(String organisationCode) { this.organisationCode = organisationCode; return this; }

        public SchemaMetadata build() {
            return new SchemaMetadata(this);
        }
    }

    @Override
    public String toString() {
        return "SchemaMetadata{id='" + id + "', category='" + category + "', specVersion='" + specVersion + "'}";
    }
}
