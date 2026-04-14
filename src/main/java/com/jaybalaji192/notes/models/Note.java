package com.jaybalaji192.notes.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "notes")
public class Note {
    @Id
    private String id;

    private String title;
    private String content;

    private String username;

    public Note() {
    }

    public Note(String username, String title, String content) {
        this.username = username;
        this.title = title;
        this.content = content;
    }


    public void updateNote(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getUsername() {
        return username;
    }
}