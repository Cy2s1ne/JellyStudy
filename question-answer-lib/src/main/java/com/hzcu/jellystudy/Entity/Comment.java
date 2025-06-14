package com.hzcu.jellystudy.Entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.Map;

@Data
@Document(collection = "comments")
public class Comment {
    @Id
    private String id;

    private String content;

    private Question.Author author;

    @Indexed
    private String parentId;

    private Date createdAt;

    private Date updatedAt;

    private int voteCount;

    private int depth;

    @Indexed
    private String path;

    private Map<String, Object> metadata;
}
