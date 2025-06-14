package com.hzcu.jellystudy.Entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "evaluations")
public class Evaluation {
    @Id
    private String id;

    private String questionId;

    private String answerId;

    private String content;

    private String level;

    private String point;
} 