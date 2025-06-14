package com.hzcu.jellystudy.Entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
@Document(collection = "questions")
@CompoundIndexes({
        @CompoundIndex(name = "view_answer_idx", def = "{'view_count': -1, 'answer_count': -1}")
})
public class Question {
    @Id
    private String id;

    @TextIndexed
    private String title;

    @TextIndexed
    private String content;

    private Author author;

    private int money;

    private List<String> knowledgePoints;

    @Indexed
    private List<String> tags;

    // 修复: 使用IndexDirection.DESC代替-1
    @Indexed(direction = IndexDirection.DESCENDING)
    private Date createdAt;

    private Date updatedAt;

    private int viewCount;

    private int likeCount;

    private int answerCount;

    private String status;

    private String path;

    private Map<String, Object> metadata;

    @Data
    public static class Author {
        private String userId;
        private String username;
        private String role;
    }
}
