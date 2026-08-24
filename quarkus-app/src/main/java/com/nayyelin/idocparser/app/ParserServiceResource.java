package com.nayyelin.idocparser.app;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.jboss.resteasy.reactive.RestForm;
import org.jboss.resteasy.reactive.multipart.FileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nayyelin.idocparser.core.ParserService;
import com.nayyelin.idocparser.core.ParserServiceException;
import com.nayyelin.idocparser.core.ParserServiceRequest;
import com.nayyelin.idocparser.core.ParserServiceResult;
import com.nayyelin.idocparser.core.model.SchemaMetadata;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * REST API for the service (development/testing).
 *
 * Available Endpoints:
 * <ul>
 * <li><b>POST /api/parser-service/execute</b> - Execute the service</li>
 * <li><b>POST /api/parser-service/parse</b> - Parse an IDoc file upload</li>
 * <li><b>GET /api/parser-service/health</b> - Health check</li>
 * <li><b>GET /api/parser-service/config-schema</b> - Get configuration schema</li>
 * </ul>
 */
@Path("/api/parser-service")
@Produces(MediaType.APPLICATION_JSON)
public class ParserServiceResource {

    private static final Logger log = LoggerFactory.getLogger(ParserServiceResource.class);

    private static final String SERVICE_NAME = "idoc-parser";
    private static final String SERVICE_VERSION = "1.0.0";
    private static final String SERVICE_DESCRIPTION = "IDOC to JSON Schema Parser - Converts SAP IDOC fixed-length format files into JSON schema definitions";

    @Inject
    ParserService parserService;

    public ParserServiceResource() {
    }

    /**
     * Main endpoint: executes the service.
     * <p>
     * POST /api/parser-service/execute
     */
    @POST
    @Path("/execute")
    public Response execute(ParserServiceRequest request) {
        log.info("Received execute request: {}", request.getInput());

        try {
            if (request.getInputFile() == null && request.getInput() != null) {
                request.setInputFile(
                        new ByteArrayInputStream(request.getInput().getBytes(StandardCharsets.UTF_8)));
            }

            ParserServiceResult result;
            if (request.getInputFile() != null) {
                result = parserService.parseSchema(request);
            } else {
                result = parserService.execute(request);
            }

            return Response.ok(result).build();

        } catch (ParserServiceException e) {
            log.error("Service execution failed", e);
            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of("error", e.getMessage()))
                    .build();

        } catch (IllegalArgumentException e) {
            log.error("Invalid request", e);
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", e.getMessage()))
                    .build();
        }
    }

    @POST
    @Path("/parse")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public ParserServiceResult parseFile(
            @RestForm("file") FileUpload file,
            @RestForm("id") String metaId,
            @RestForm("version") String metaVersion,
            @RestForm("createdAt") String metaCreatedAt,
            @RestForm("representation") String metaRepresentation,
            @RestForm("specification") String metaSpecification,
            @RestForm("specVersion") String metaSpecVersion,
            @RestForm("code") String metaCode,
            @RestForm("documentType") String metaDocumentType,
            @RestForm("modelName") String metaModelName,
            @RestForm("category") String metaCategory,
            @RestForm("baseStandardId") String metaBaseStandardId,
            @RestForm("projectionId") String metaProjectionId,
            @RestForm("customerName") String metaCustomerName)
            throws IOException, ParserServiceException {

        if (file == null || file.size() == 0) {
            throw new ParserServiceException(
                    "INVALID_FILE",
                    "Uploaded file is empty or missing");
        }

        SchemaMetadata metadata = buildMetadataFromForm(
                metaId, metaVersion, metaCreatedAt, metaRepresentation,
                metaSpecification, metaSpecVersion, metaCode,
                metaDocumentType, metaModelName, metaCategory,
                metaBaseStandardId, metaProjectionId, metaCustomerName);

        try (InputStream is = Files.newInputStream(file.uploadedFile())) {

            ParserServiceRequest request = ParserServiceRequest.builder()
                    .inputStream(is)
                    .schemaMetadata(metadata)
                    .build();

            return parserService.parseSchema(request);
        }
    }

    private SchemaMetadata buildMetadataFromForm(
            String id, String version, String createdAt, String representation,
            String specification, String specVersion, String code,
            String documentType, String modelName, String category,
            String baseStandardId, String projectionId, String customerName) {

        boolean hasAny = id != null || version != null || createdAt != null
                || representation != null || specification != null || specVersion != null
                || code != null || documentType != null || modelName != null
                || category != null || baseStandardId != null || projectionId != null
                || customerName != null;

        if (!hasAny) {
            return null;
        }

        return SchemaMetadata.builder()
                .id(id)
                .version(version)
                .createdAt(createdAt)
                .representation(representation)
                .specification(specification)
                .specVersion(specVersion)
                .code(code)
                .documentType(documentType)
                .modelName(modelName)
                .category(category)
                .baseStandardId(baseStandardId)
                .projectionId(projectionId)
                .customerName(customerName)
                .build();
    }

    /**
     * Health check endpoint.
     * <p>
     * GET /api/parser-service/health
     */
    @GET
    @Path("/health")
    public Response health() {
        Map<String, Object> health = new HashMap<>();
        health.put("status", "UP");
        health.put("service", SERVICE_NAME);
        health.put("version", SERVICE_VERSION);
        health.put("description", SERVICE_DESCRIPTION);

        return Response.ok(health).build();
    }

    /**
     * Configuration schema endpoint.
     * <p>
     * GET /api/parser-service/config-schema
     */
    @GET
    @Path("/config-schema")
    public Response getConfigSchema() {
        Map<String, String> schema = new LinkedHashMap<>();
        schema.put("schemaMetadata.id", "Schema ID slug, auto-derived if blank (string)");
        schema.put("schemaMetadata.version", "Schema version (string, e.g. 1.0)");
        schema.put("schemaMetadata.createdAt", "Creation date in yyyyMMdd format (string)");
        schema.put("schemaMetadata.representation", "Physical format: FLAT, XML, JSON (string)");
        schema.put("schemaMetadata.specification", "Standard family: IDoc, UBL, EDIFACT (string)");
        schema.put("schemaMetadata.specVersion", "Spec version / Basic Type: ORDERS05, DELVRY03 (string)");
        schema.put("schemaMetadata.code", "Document code / Message Type: ORDERS, DESADV (string)");
        schema.put("schemaMetadata.documentType", "Business classification: Business, Financial, Logistics (string)");
        schema.put("schemaMetadata.modelName", "Model name (string, optional)");
        schema.put("schemaMetadata.category", "Category: standard, projection (string)");
        schema.put("schemaMetadata.baseStandardId", "Base standard ID (string, required for projection)");
        schema.put("schemaMetadata.projectionId", "Projection ID (string, required for projection)");
        schema.put("schemaMetadata.customerName", "Customer name (string, optional)");

        return Response.ok(schema).build();
    }
}
