package com.hzcu.jellystudy.Dto;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.Map;

@Data
@Document(collection = "answers")
public class AnswerDto {
    @Id
    private String id;

    @Indexed
    private String questionId;

    private String content;

    private QuestionDto.Author author;

    private Date createdAt;

    private Date updatedAt;

    private int voteCount;

    private int commentCount;

    private boolean isAccepted;

    @Indexed
    private String path;

    private Map<String, Object> metadata;
}