package com.ftn.mailClient.model;

import java.util.List;

public class Folder extends FolderElement {
    private String name;
    private List<FolderElement> folderContents;

    public Folder() {
        this(null, null, null, null);
    }

    public Folder(Integer id, Folder parentFolder, String name, List<FolderElement> folderContents) {
        super(id, parentFolder);
        this.name = name;
        this.folderContents = folderContents;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<FolderElement> getFolderContents() {
        return folderContents;
    }

    public void setFolderContents(List<FolderElement> folderContents) {
        this.folderContents = folderContents;
    }
}
