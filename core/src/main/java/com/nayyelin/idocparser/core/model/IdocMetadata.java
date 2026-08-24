package com.nayyelin.idocparser.core.model;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({
        "idocType",
        "messageType",
        "release",
        "description"
})
public class IdocMetadata {
    private String idocType;
    private String messageType;
    private String release;
    private String description;

    public IdocMetadata(String idocType, String messageType, String release, String description) {
        this.idocType = idocType;
        this.messageType = messageType;
        this.release = release;
        this.description = description;
    }

    public String getIdocType() {
        return idocType;
    }

    public void setIdocType(String idocType) {
        this.idocType = idocType;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getRelease() {
        return release;
    }

    public void setRelease(String release) {
        this.release = release;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}