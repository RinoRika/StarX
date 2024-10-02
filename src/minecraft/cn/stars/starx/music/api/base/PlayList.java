/*
 * StarX Client - A PVP Client with hack visual.
 * Copyright 2024 Starlight, All rights reserved.
 */
package cn.stars.starx.music.api.base;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.util.ArrayList;

@Getter
@Setter
public class PlayList {
    String name;
    String description;
    long id;
    File file;
    public ArrayList<Music> musicList = new ArrayList<>();
    public File coverImage;

    public PlayList(String name, String description, long id, File file) {
        this.name = name;
        this.description = description;
        this.id = id;
        this.file = file;
    }

    public PlayList(String name, String description) {
        this.name = name;
        this.description = description;
        this.id = -1;
        this.file = null;
    }
}
