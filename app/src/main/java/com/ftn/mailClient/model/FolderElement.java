package com.ftn.mailClient.model;

import java.io.Serializable;

public abstract class FolderElement extends Identifiable implements Serializable {
    private Folder parentFolder;

    public  FolderElement(){
        this(null, null);
    }

    public FolderElement(Integer id, Folder parentFolder) {
        super(id);
        this.parentFolder = parentFolder;
    }

    public Folder getParentFolder() {
        return parentFolder;
    }

    public void setParentFolder(Folder parentFolder) {
        this.parentFolder = parentFolder;
    }
}
