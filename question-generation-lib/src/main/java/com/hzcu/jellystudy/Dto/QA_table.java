package com.hzcu.jellystudy.Dto;


import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "qa_table")
public class QA_table {
    @Id
    private String _id;
    private String question;
    private String answer;
    private String explanation;
}
