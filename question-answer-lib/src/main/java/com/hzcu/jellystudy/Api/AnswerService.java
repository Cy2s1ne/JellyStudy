package com.hzcu.jellystudy.Api;

import com.hzcu.jellystudy.Entity.Answer;
import java.util.List;

public interface AnswerService {

    // 创建回答
    Answer createAnswer(Answer answer);

    // 更新回答
    Answer updateAnswer(String id, Answer answerUpdate);

    // 删除回答
    void deleteAnswer(String id);

    // 接受回答
    Answer acceptAnswer(String id);

    // 点赞回答
    Answer voteAnswer(String id, boolean isUpvote);

    // 获取问题的所有回答
    List<Answer> getAnswersByQuestionId(String questionId);

    // 获取高赞回答
    List<Answer> getTopVotedAnswers(int limit);
}