package com.hzcu.jellystudy.Service;

import com.hzcu.jellystudy.Api.AnswerService;
import com.hzcu.jellystudy.Entity.Answer;
import com.hzcu.jellystudy.Entity.Question;
import com.hzcu.jellystudy.Repository.AnswerRepository;
import com.hzcu.jellystudy.Repository.QuestionRepository;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;


import java.util.Date;
import java.util.List;

@DubboService(version = "1.0.0")
public class AnswerServiceImpl implements AnswerService {

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private QuestionRepository questionRepository;

    // 创建回答
    @Override
    public Answer createAnswer(Answer answer) {
        Question question = questionRepository.findById(answer.getQuestionId())
                .orElseThrow(() -> new RuntimeException("问题不存在"));

        Date now = new Date();
        answer.setCreatedAt(now);
        answer.setUpdatedAt(now);
        answer.setVoteCount(0);
        answer.setCommentCount(0);
        answer.setAccepted(false);

        Answer savedAnswer = answerRepository.save(answer);

        // 设置路径
        savedAnswer.setPath("Q-" + answer.getQuestionId() + ".A-" + savedAnswer.getId());
        savedAnswer = answerRepository.save(savedAnswer);

        // 更新问题的回答数
        question.setAnswerCount(question.getAnswerCount() + 1);
        questionRepository.save(question);

        return savedAnswer;
    }

    // 更新回答
    @Override
    public Answer updateAnswer(String id, Answer answerUpdate) {
        Answer existingAnswer = answerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("回答不存在"));

        // 只允许更新内容
        existingAnswer.setContent(answerUpdate.getContent());
        existingAnswer.setUpdatedAt(new Date());

        return answerRepository.save(existingAnswer);
    }

    // 删除回答
    @Override
    public void deleteAnswer(String id) {
        Answer answer = answerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("回答不存在"));

        answerRepository.delete(answer);

        // 更新问题的回答数
        Question question = questionRepository.findById(answer.getQuestionId())
                .orElseThrow(() -> new RuntimeException("问题不存在"));
        question.setAnswerCount(question.getAnswerCount() - 1);
        questionRepository.save(question);
    }

    // 接受回答
    @Override
    public Answer acceptAnswer(String id) {
        Answer answer = answerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("回答不存在"));

        answer.setAccepted(true);
        answer.setUpdatedAt(new Date());

        return answerRepository.save(answer);
    }

    // 点赞回答
    @Override
    public Answer voteAnswer(String id, boolean isUpvote) {
        Answer answer = answerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("回答不存在"));

        if (isUpvote) {
            answer.setVoteCount(answer.getVoteCount() + 1);
        } else {
            answer.setVoteCount(answer.getVoteCount() - 1);
        }

        return answerRepository.save(answer);
    }

    // 获取问题的所有回答
    @Override
    public List<Answer> getAnswersByQuestionId(String questionId) {
        return answerRepository.findByQuestionId(questionId);
    }    // 获取高赞回答
    @Override
    public List<Answer> getTopVotedAnswers(int limit) {
        // 修改条件，获取所有回答并按照点赞数降序排序
        Page<Answer> topAnswers = answerRepository.findByVoteCountGreaterThanOrderByVoteCountDesc(
                -1, PageRequest.of(0, limit));
        return topAnswers.getContent();
    }
}