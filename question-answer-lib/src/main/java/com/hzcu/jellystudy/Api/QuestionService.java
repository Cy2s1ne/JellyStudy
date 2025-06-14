package com.hzcu.jellystudy.Api;

import com.hzcu.jellystudy.Entity.Question;

import java.util.List;

public interface QuestionService {

    // 创建新问题
    Question createQuestion(Question question);

    // 更新问题
    Question updateQuestion(String id, Question questionDtoUpdate);

    // 获取问题详情
    Question getQuestionById(String id);

    // 删除问题
    void deleteQuestion(String id);

    // 获取热门问题
    List<Question> getHotQuestions(int limit);


    // 获取高人气问题（根据点赞数、评论数、浏览量计算）
    List<Question> getHighPopularityQuestions(int limit);

    // 获取推荐问题列表
    List<Question> getRecommendedQuestions(int limit);

    // 获取知识点相关问题
    List<Question> getQuestionsByKnowledgePoint(String knowledgePointId);

    // 搜索问题
    List<Question> searchQuestions(String searchText);
    
    // 设置问题悬赏金额
    Question setQuestionBounty(String questionId, int money);
    
    // 接受回答并发放悬赏
    Question acceptAnswerWithBounty(String questionId, String answerId);
}