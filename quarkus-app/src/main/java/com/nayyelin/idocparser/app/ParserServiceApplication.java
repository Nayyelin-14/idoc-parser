package com.nayyelin.idocparser.app;

import com.nayyelin.idocparser.core.ParserService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Quarkus Application - CDI Configuration.
 * <p>
 * This class provides CDI producers for the parser service.
 * It uses Quarkus CDI only for bean exposure in the Quarkus runtime environment.
 * </p>
 */
@ApplicationScoped
public class ParserServiceApplication {

    private static final Logger log = LoggerFactory.getLogger(ParserServiceApplication.class);

    public ParserServiceApplication() {
    }

    /**
     * Produces a singleton instance of ParserService for CDI.
     *
     * @return a singleton ParserService instance
     */
    @Produces
    @Singleton
    public ParserService parserService() {
        log.info("Initializing ParserService...");
        ParserService parserService = new ParserService();
        log.info("ParserService initialized");
        return parserService;
    }
}
