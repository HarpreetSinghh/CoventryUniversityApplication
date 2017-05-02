package com.conventry.university.beans;


import com.conventry.university.utils.FileType;

import java.io.Serializable;

public class ModulesFiles implements Serializable {

    private String filename;

    private String dirTitle;

    private FileType type;

    public ModulesFiles(String filename, String dirTitle, FileType type) {
        this.filename = filename;
        this.dirTitle = dirTitle;
        this.type = type;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getDirTitle() {
        return dirTitle;
    }

    public void setDirTitle(String dirTitle) {
        this.dirTitle = dirTitle;
    }

    public FileType getType() {
        return type;
    }

    public void setType(FileType type) {
        this.type = type;
    }
}
