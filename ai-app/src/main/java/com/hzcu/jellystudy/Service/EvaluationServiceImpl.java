package com.hzcu.jellystudy.Service;

import com.hzcu.jellystudy.Api.EvaluationService;
import com.hzcu.jellystudy.Entity.Evaluation;
import com.hzcu.jellystudy.Repository.EvaluationRepository;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@DubboService(version = "1.0.0")
public class EvaluationServiceImpl implements EvaluationService {

    private final EvaluationRepository evaluationRepository;

    @Autowired
    public EvaluationServiceImpl(EvaluationRepository evaluationRepository) {
        this.evaluationRepository = evaluationRepository;
    }
    public List<Evaluation> getEvaluationByQuestionId(String questionId) {
        return evaluationRepository.findByQuestionId(questionId);
    }
    public List<Evaluation> getEvaluationByAnswerId(String answerId) {
        return evaluationRepository.findByAnswerId(answerId);
    }
    public Evaluation getEvaluationById(String id) {
        return evaluationRepository.findById(id).orElse(null);
    }
    public Evaluation saveEvaluation(Evaluation evaluation) {
        return evaluationRepository.save(evaluation);
    }

} 