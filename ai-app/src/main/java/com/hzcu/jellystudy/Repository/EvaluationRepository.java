package com.hzcu.jellystudy.Repository;

import com.hzcu.jellystudy.Entity.Evaluation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EvaluationRepository extends MongoRepository<Evaluation, String> {
    // Additional query methods can be added here if needed
    List<Evaluation> findByQuestionId(String questionId);

    List<Evaluation> findByAnswerId(String answerId);
} 