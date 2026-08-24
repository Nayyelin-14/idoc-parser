package com.nayyelin.idocparser.core.model;

public class GroupDef {

    private String parent;
    private int level;
    private int minOccurs;
    private int maxOccurs;

    public GroupDef() {
    }

    public GroupDef(String parent, int level, int minOccurs, int maxOccurs) {
        this.parent = parent;
        this.level = level;
        this.minOccurs = minOccurs;
        this.maxOccurs = maxOccurs;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getMinOccurs() {
        return minOccurs;
    }

    public void setMinOccurs(int minOccurs) {
        this.minOccurs = minOccurs;
    }

    public int getMaxOccurs() {
        return maxOccurs;
    }

    public void setMaxOccurs(int maxOccurs) {
        this.maxOccurs = maxOccurs;
    }
}
