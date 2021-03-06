package com.lemon.player.model;

import com.lemon.player.LemonApplication;
import com.lemon.player.interfaces.FileType;
import com.lemon.player.utils.FileHelper;
import com.lemon.player.utils.StringUtils;

public class FileObject extends BaseFileObject {

    public String extension;

    public TagInfo tagInfo;

    private long duration = 0;

    public FileObject() {
        this.fileType = FileType.FILE;
    }

    public String getTimeString() {
        if (duration == 0) {
            duration = FileHelper.getDuration(LemonApplication.getInstance(), this);
        }
        return StringUtils.makeTimeString(LemonApplication.getInstance(), duration / 1000);
    }

    @Override
    public String toString() {
        return "FileObject{" +
                "extension='" + extension + '\'' +
                ", size='" + size + '\'' +
                "} " + super.toString();
    }
}
