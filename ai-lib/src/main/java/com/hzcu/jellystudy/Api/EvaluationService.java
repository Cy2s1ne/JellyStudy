package com.hzcu.jellystudy.Api;

import com.hzcu.jellystudy.Entity.Evaluation;

import java.util.List;

public interface EvaluationService {

    Evaluation getEvaluationById(String id);

    List<Evaluation> getEvaluationByQuestionId(String questionId);

    List<Evaluation> getEvaluationByAnswerId(String answerId);

}
