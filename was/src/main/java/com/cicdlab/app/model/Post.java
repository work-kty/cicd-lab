package com.cicdlab.app.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Post {
    private final long id;
    private final String title;
    private final String content;
    private final String writer;
    private final String createdAt;

    public Post(long id, String title, String content, String writer) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.writer = writer;
        this.createdAt = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }
    public long getId() { return id; }
    public String getTitle() { return title; }
    public String getContent() { return content; }
    public String getWriter() { return writer; }
    public String getCreatedAt() { return createdAt; }
}