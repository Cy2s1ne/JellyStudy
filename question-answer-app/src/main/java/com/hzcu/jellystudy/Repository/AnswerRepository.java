package com.hzcu.jellystudy.Repository;

import com.hzcu.jellystudy.Entity.Answer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface AnswerRepository extends MongoRepository<Answer, String> {

    // 根据问题ID查询所有回答
    List<Answer> findByQuestionId(String questionId);

    // 根据问题ID查询回答并按点赞数排序
    Page<Answer> findByQuestionIdOrderByVoteCountDesc(String questionId, Pageable pageable);

    // 查询用户的所有回答
    List<Answer> findByAuthorUserId(String userId);

    // 查询高赞回答
    Page<Answer> findByVoteCountGreaterThanOrderByVoteCountDesc(int minVotes, Pageable pageable);
}