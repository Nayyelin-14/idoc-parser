package com.nayyelin.idocparser.core.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({
    "name",
    "level",
    "minOccurs",
    "maxOccurs",
    "status",
    "isGroup",
    "children"
})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HierarchyNode {
    private String name;
    private int level;
    private int minOccurs;
    private int maxOccurs;
    private String status;
    private Boolean isGroup;
    private List<HierarchyNode> children;

    public HierarchyNode(String name, int level, int minOccurs, int maxOccurs, 
                        String status, Boolean isGroup, List<HierarchyNode> children) {
        this.name = name;
        this.level = level;
        this.minOccurs = minOccurs;
        this.maxOccurs = maxOccurs;
        this.status = status;
        this.isGroup = isGroup;
        this.children = children;
    }

    public HierarchyNode(String name, int level, int minOccurs, int maxOccurs, String status) {
        this.name = name;
        this.level = level;
        this.minOccurs = minOccurs;
        this.maxOccurs = maxOccurs;
        this.status = status;
        this.isGroup = null;  
        this.children = null; 
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public int getLevel() { return level; }
    public void setLevel(int level) { this.level = level; }
    
    public int getMinOccurs() { return minOccurs; }
    public void setMinOccurs(int minOccurs) { this.minOccurs = minOccurs; }
    
    public int getMaxOccurs() { return maxOccurs; }
    public void setMaxOccurs(int maxOccurs) { this.maxOccurs = maxOccurs; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public Boolean getIsGroup() { return isGroup; }
    public void setIsGroup(Boolean isGroup) { this.isGroup = isGroup; }
    
    public List<HierarchyNode> getChildren() { return children; }
    public void setChildren(List<HierarchyNode> children) { this.children = children; }
}