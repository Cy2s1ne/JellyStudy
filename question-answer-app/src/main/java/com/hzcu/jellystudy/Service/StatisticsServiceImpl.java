package com.hzcu.jellystudy.Service;

import com.hzcu.jellystudy.Api.StatisticsService;
import com.hzcu.jellystudy.Entity.Answer;
import com.hzcu.jellystudy.Entity.Question;
import com.hzcu.jellystudy.Repository.AnswerRepository;
import com.hzcu.jellystudy.Repository.QuestionRepository;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@DubboService(version = "1.0.0")
public class StatisticsServiceImpl implements StatisticsService {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerRepository answerRepository;

    // 获取问题总数
    @Override
    public long getTotalQuestionCount() {
        return questionRepository.countByStatus("active");
    }

    // 获取最热问题
    @Override
    public List<Question> getHotQuestions(int limit) {
        return questionRepository.findByStatusOrderByViewCountDescAnswerCountDesc(
                "active", PageRequest.of(0, limit)).getContent();
    }    // 获取高赞回答
    @Override
    public List<Answer> getTopVotedAnswers(int limit) {
        return answerRepository.findByVoteCountGreaterThanOrderByVoteCountDesc(
                -1, PageRequest.of(0, limit)).getContent();
    }

    // 获取总体统计信息
    @Override
    public Map<String, Object> getOverallStatistics() {
        Map<String, Object> statistics = new HashMap<>();

        statistics.put("totalQuestions", questionRepository.countByStatus("active"));
        statistics.put("totalAnswers", answerRepository.count());

        List<Question> recentQuestions = questionRepository.findAll(
                PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "createdAt"))).getContent();
        statistics.put("recentQuestions", recentQuestions);

        List<Answer> topAnswers = answerRepository.findAll(
                PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "voteCount"))).getContent();
        statistics.put("topAnswers", topAnswers);

        return statistics;
    }
}