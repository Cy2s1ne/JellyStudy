package com.hzcu.jellystudy.Repository;

import com.hzcu.jellystudy.Entity.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface QuestionRepository extends MongoRepository<Question, String> {

    // 根据知识点查询问题
    List<Question> findByKnowledgePointsContaining(String knowledgePointId);

    // 根据标签查询问题
    List<Question> findByTagsContaining(String tag);

    // 获取热门问题（按照浏览量和回答数排序）
    Page<Question> findByStatusOrderByViewCountDescAnswerCountDesc(String status, Pageable pageable);

    // 根据状态查询问题
    List<Question> findByStatus(String status);

    // 全文搜索问题
    @Query("{'$text': {'$search': ?0}}")
    List<Question> searchQuestions(String searchText);

    // 统计问题总数
    long countByStatus(String status);
}
