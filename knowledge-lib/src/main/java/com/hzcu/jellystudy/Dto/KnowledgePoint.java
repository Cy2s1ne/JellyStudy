package com.hzcu.jellystudy.Dto;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Document(collection = "knowledge_points")
public class KnowledgePoint {
    @Id
    private String id;

    private String title;

    private String description;

    private String category;

    private String createdBy;

    private Date createdAt;
}
