package com.hzcu.jellystudy.Service;

import com.hzcu.jellystudy.Api.QuestionService;
import com.hzcu.jellystudy.Entity.Question;
import com.hzcu.jellystudy.Repository.QuestionRepository;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import com.hzcu.jellystudy.Entity.Answer;
import com.hzcu.jellystudy.Entity.User;
import com.hzcu.jellystudy.Repository.AnswerRepository;
import com.hzcu.jellystudy.Repository.UserRepository;

@DubboService(version = "1.0.0")
public class QuestionServiceImpl implements QuestionService {

    @Autowired
    private QuestionRepository questionRepository;
    
    @Autowired
    private AnswerRepository answerRepository;
    
    @Autowired
    private UserRepository userRepository;


    // 创建新问题
    @Override
    public Question createQuestion(Question question) {
        Date now = new Date();
        question.setCreatedAt(now);
        question.setUpdatedAt(now);
        question.setViewCount(0);
        question.setAnswerCount(0);
        question.setLikeCount(0);
        question.setStatus("active");
        
        // 设置默认悬赏金额为0
        if (question.getMoney() > 0) {
            // 如果设置了悬赏金额，检查用户余额是否足够
            User user = userRepository.findById(question.getAuthor().getUserId())
                .orElseThrow(() -> new RuntimeException("用户不存在"));
                
            if (user.getMoney() < question.getMoney()) {
                throw new RuntimeException("用户余额不足");
            }
            
            // 扣除用户悬赏金额
            user.setMoney(user.getMoney() - question.getMoney());
            userRepository.save(user);
        }

        Question savedQuestion = questionRepository.save(question);

        // 设置路径
        savedQuestion.setPath("Q-" + savedQuestion.getId());
        return questionRepository.save(savedQuestion);
    }

    // 更新问题
    @Override
    public Question updateQuestion(String id, Question questionUpdate) {
        Question existingQuestion = questionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("问题不存在"));

        // 只允许更新特定字段
        existingQuestion.setTitle(questionUpdate.getTitle());
        existingQuestion.setContent(questionUpdate.getContent());
        existingQuestion.setTags(questionUpdate.getTags());
        existingQuestion.setUpdatedAt(new Date());

        return questionRepository.save(existingQuestion);
    }

    // 获取问题详情
    @Override
    public Question getQuestionById(String id) {
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("问题不存在"));

        // 增加浏览量
        question.setViewCount(question.getViewCount() + 1);
        return questionRepository.save(question);
    }

    // 删除问题
    @Override
    public void deleteQuestion(String id) {
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("问题不存在"));

        // 软删除，将状态改为deleted
        question.setStatus("deleted");
        question.setUpdatedAt(new Date());
        questionRepository.save(question);
    }

    // 获取热门问题
    @Override
    public List<Question> getHotQuestions(int limit) {
        Page<Question> hotQuestions = questionRepository.findByStatusOrderByViewCountDescAnswerCountDesc(
                "active", PageRequest.of(0, limit));
        return hotQuestions.getContent();
    }
    
    // 获取高人气问题（根据点赞数、评论数、浏览量计算）
    @Override
    public List<Question> getHighPopularityQuestions(int limit) {
        // 获取所有活跃问题
        List<Question> activeQuestions = questionRepository.findByStatus("active");
        
        // 计算问题的人气得分并排序
        // 人气得分 = 点赞数 * 3 + 评论数 * 2 + 浏览量 * 1
        List<Question> highPopularityQuestions = activeQuestions.stream()
                .sorted((q1, q2) -> {
                    int score1 = q1.getLikeCount() * 3 + q1.getAnswerCount() * 2 + q1.getViewCount();
                    int score2 = q2.getLikeCount() * 3 + q2.getAnswerCount() * 2 + q2.getViewCount();
                    return Integer.compare(score2, score1); // 降序排列
                })
                .limit(limit)
                .collect(Collectors.toList());
                
        return highPopularityQuestions;
    }

    // 获取推荐问题列表
    @Override
    public List<Question> getRecommendedQuestions(int limit) {
        // 这里简化为获取最新问题，实际应用中可能需要更复杂的推荐算法
        Page<Question> recentQuestions = questionRepository.findAll(
                PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "createdAt")));
        return recentQuestions.getContent();
    }

    // 获取知识点相关问题
    @Override
    public List<Question> getQuestionsByKnowledgePoint(String knowledgePointId) {
        return questionRepository.findByKnowledgePointsContaining(knowledgePointId);
    }

    // 搜索问题
    @Override
    public List<Question> searchQuestions(String searchText) {
        return questionRepository.searchQuestions(searchText);
    }
    
    // 设置问题悬赏金额
    @Override
    public Question setQuestionBounty(String questionId, int money) {
        if (money <= 0) {
            throw new RuntimeException("悬赏金额必须大于0");
        }
        
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException("问题不存在"));
        
        // 检查问题状态
        if (!"active".equals(question.getStatus())) {
            throw new RuntimeException("问题已关闭，无法设置悬赏");
        }
        
        // 获取提问者信息
        User user = userRepository.findById(question.getAuthor().getUserId())
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        
        // 检查用户余额
        if (user.getMoney() < money) {
            throw new RuntimeException("用户余额不足");
        }
        
        // 扣除用户余额
        user.setMoney(user.getMoney() - money);
        userRepository.save(user);
        
        // 如果之前已有悬赏，累加悬赏金额
        question.setMoney(question.getMoney() + money);
        question.setUpdatedAt(new Date());
        
        return questionRepository.save(question);
    }
    
    // 接受回答并发放悬赏
    @Override
    public Question acceptAnswerWithBounty(String questionId, String answerId) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException("问题不存在"));
        
        // 检查问题状态
        if (!"active".equals(question.getStatus())) {
            throw new RuntimeException("问题已关闭，无法接受回答");
        }
        
        // 检查悬赏金额
        if (question.getMoney() <= 0) {
            throw new RuntimeException("该问题没有设置悬赏");
        }
        
        // 获取回答
        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new RuntimeException("回答不存在"));
        
        // 检查回答是否属于该问题
        if (!answer.getQuestionId().equals(questionId)) {
            throw new RuntimeException("该回答不属于此问题");
        }
        
        // 检查回答是否已被接受
        if (answer.isAccepted()) {
            throw new RuntimeException("该回答已被接受");
        }
        
        // 获取回答者信息
        User answerer = userRepository.findById(answer.getAuthor().getUserId())
                .orElseThrow(() -> new RuntimeException("回答者不存在"));
        
        // 将悬赏金额转给回答者
        answerer.setMoney(answerer.getMoney() + question.getMoney());
        userRepository.save(answerer);
        
        // 标记回答为已接受
        answer.setAccepted(true);
        answer.setUpdatedAt(new Date());
        answerRepository.save(answer);
        
        // 更新问题状态，清空悬赏金额（已发放）
        int bountyAmount = question.getMoney();
        question.setMoney(0);
        question.setStatus("resolved");
        question.setUpdatedAt(new Date());
        
        return questionRepository.save(question);
    }
}