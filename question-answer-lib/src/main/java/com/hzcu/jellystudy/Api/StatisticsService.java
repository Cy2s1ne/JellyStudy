package com.hzcu.jellystudy.Api;

import com.hzcu.jellystudy.Entity.Answer;
import com.hzcu.jellystudy.Entity.Question;

import java.util.List;
import java.util.Map;

public interface StatisticsService {

    // 获取问题总数
    long getTotalQuestionCount();

    // 获取最热问题
    List<Question> getHotQuestions(int limit);

    // 获取高赞回答
    List<Answer> getTopVotedAnswers(int limit);

    // 获取总体统计信息
    Map<String, Object> getOverallStatistics();
}