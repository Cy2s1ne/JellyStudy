package com.hzcu.jellystudy.Entity;


import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;

@Data
@Document(collection = "qa_table")
public class QA_table {
    @Id
    private String _id;
    private String question;
    private String answer;
    private String explanation;
}
