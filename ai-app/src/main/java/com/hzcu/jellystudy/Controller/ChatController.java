package com.hzcu.jellystudy.Controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hzcu.jellystudy.Entity.Evaluation;
import com.hzcu.jellystudy.Service.EvaluationServiceImpl;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@RestController
public class ChatController {

    private static final Logger logger = LoggerFactory.getLogger(ChatController.class);
    
    private final ChatClient chatClient;
    private final EvaluationServiceImpl evaluationServiceImpl;
    private final ObjectMapper objectMapper;

    @Autowired
    public ChatController(ChatClient.Builder builder, EvaluationServiceImpl evaluationServiceImpl, ObjectMapper objectMapper) {
        this.chatClient = builder.build();
        this.evaluationServiceImpl = evaluationServiceImpl;
        this.objectMapper = objectMapper;
        logger.info("ChatController initialized with dependencies");
    }

    @GetMapping("/evaluation/question")
    @ResponseBody
    public String evaluationQuestion(@RequestParam("questionId") String questionId, @RequestParam("input") String input) {
        logger.info("Received /evaluation/question request with questionId: {}", questionId);
        
        // 修改prompt，让大模型知道需要返回特定格式的JSON
        String systemPrompt = "请评估以下问题并以JSON格式返回结果，格式为：{\"content\": \"评估内容\", \"level\": \"难度级别\", \"point\": \"得分\"}";

        String response = this.chatClient.prompt()
                .system(systemPrompt)  // 添加系统提示
                .user(input)
                .call()
                .content();
        
        logger.info("AI response received: {}", response);

        // 如果大模型返回的不是有效JSON，你可能需要进行处理
        try {
            // 验证JSON是否有效
            JsonNode jsonNode = objectMapper.readTree(response);
            logger.info("Successfully parsed JSON response");

            // 创建Evaluation对象并保存到数据库
            Evaluation evaluation = new Evaluation();
            evaluation.setQuestionId(questionId);
            evaluation.setContent(jsonNode.get("content").asText());
            evaluation.setLevel(jsonNode.get("level").asText());
            evaluation.setPoint(jsonNode.get("point").asText());
            
            logger.info("Created Evaluation object: {}", evaluation);

            // 保存到数据库
            Evaluation savedEvaluation = evaluationServiceImpl.saveEvaluation(evaluation);
            logger.info("Saved evaluation to database with ID: {}", savedEvaluation.getId());

            return response;
        } catch (Exception e) {
            // 返回错误格式的JSON
            logger.error("Error processing evaluation question: ", e);
            return "{\"content\": \"无法解析响应\", \"level\": \"unknown\", \"point\": \"0\"}";
        }
    }
    @GetMapping("/evaluation/answer")
    public String EvaluationAnswer(@RequestParam("questionId") String answerId, @RequestParam("input") String input) {
        logger.info("Received /evaluation/answer request with answerId: {}", answerId);
        
        // 修改prompt，让大模型知道需要返回特定格式的JSON
        String systemPrompt = "请评估以下回答并以JSON格式返回结果，格式为：{\"content\": \"评估内容\", \"level\": \"难度级别\", \"point\": \"得分\"}";

        String response = this.chatClient.prompt()
                .system(systemPrompt)  // 添加系统提示
                .user(input)
                .call()
                .content();
        
        logger.info("AI response received: {}", response);

        // 如果大模型返回的不是有效JSON，你可能需要进行处理
        try {
            // 验证JSON是否有效
            JsonNode jsonNode = objectMapper.readTree(response);
            logger.info("Successfully parsed JSON response");

            // 创建Evaluation对象并保存到数据库
            Evaluation evaluation = new Evaluation();
            evaluation.setAnswerId(answerId);
            evaluation.setContent(jsonNode.get("content").asText());
            evaluation.setLevel(jsonNode.get("level").asText());
            evaluation.setPoint(jsonNode.get("point").asText());
            
            logger.info("Created Evaluation object: {}", evaluation);

            // 保存到数据库
            Evaluation savedEvaluation = evaluationServiceImpl.saveEvaluation(evaluation);
            logger.info("Saved evaluation to database with ID: {}", savedEvaluation.getId());

            return response;
        } catch (Exception e) {
            // 返回错误格式的JSON
            logger.error("Error processing evaluation answer: ", e);
            return "{\"content\": \"无法解析响应\", \"level\": \"unknown\", \"point\": \"0\"}";
        }
    }
    @GetMapping("/evaluation")
    public Evaluation Evaluation(@RequestParam String evaluationId){
        logger.info("Retrieving evaluation with ID: {}", evaluationId);
        Evaluation evaluation = evaluationServiceImpl.getEvaluationById(evaluationId);
        if (evaluation != null) {
            logger.info("Found evaluation: {}", evaluation);
        } else {
            logger.warn("No evaluation found with ID: {}", evaluationId);
        }
        return evaluation;
    }
    
    @GetMapping("/generate/question")
    @ResponseBody
    public String generateQuestion(@RequestParam("knowledgePoint") String knowledgePoint, 
                                   @RequestParam("questionType") String questionType,
                                   @RequestParam(value = "difficulty", required = false, defaultValue = "中等") String difficulty,
                                   @RequestParam(value = "count", required = false, defaultValue = "3") int count) {
        logger.info("Received /generate/question request with knowledgePoint: {}, questionType: {}, difficulty: {}, count: {}", 
                    knowledgePoint, questionType, difficulty, count);
        
        // 构建提示词，让大模型生成特定格式的JSON题目
        String systemPrompt = "你是一个专业的题目生成器。请根据以下知识点和题型生成多道题目。" +
                             "你必须严格按照以下JSON数组格式返回结果，不要包含任何其他文本、代码块或反引号：" +
                             "[{\"question\": \"题目内容\", \"answer\": \"参考答案\", \"explanation\": \"解题思路和分析\"}]";
        
        String userPrompt = String.format("请生成%d道关于%s的%s题，难度为%s。请直接返回JSON数组，不要包含任何其他文本、代码块或反引号。", 
                                         count, knowledgePoint, questionType, difficulty);

        String response = this.chatClient.prompt()
                .system(systemPrompt)
                .user(userPrompt)
                .call()
                .content();
        
        logger.info("AI generated questions received");

        // 清理响应内容，移除可能导致JSON解析错误的字符
        String cleanedResponse = cleanJsonResponse(response);
        logger.info("Cleaned response: {}", cleanedResponse);

        // 尝试解析返回的JSON，确保格式正确
        try {
            JsonNode jsonNode = objectMapper.readTree(cleanedResponse);
            logger.info("Successfully parsed JSON response for generated questions");
            return cleanedResponse;
        } catch (Exception e) {
            // 如果解析失败，返回错误信息
            logger.error("Error parsing generated questions: ", e);
            return "[{\"question\": \"生成题目失败，请重试\", \"answer\": \"无\", \"explanation\": \"系统错误\"}]";
        }
    }
    
    /**
     * 清理AI返回的响应，确保它是有效的JSON格式
     * @param response AI返回的原始响应
     * @return 清理后的JSON字符串
     */
    private String cleanJsonResponse(String response) {
        if (response == null || response.isEmpty()) {
            return "[]";
        }
        
        // 移除可能包含的代码块标记
        String cleaned = response;
        
        // 移除开头的```json、```、```javascript等标记
        cleaned = cleaned.replaceAll("^\\s*```(?:json|javascript|js)?\\s*", "");
        
        // 移除结尾的```标记
        cleaned = cleaned.replaceAll("\\s*```\\s*$", "");
        
        // 如果清理后的字符串不是以[开头，则可能不是JSON数组
        if (!cleaned.trim().startsWith("[")) {
            logger.warn("Response does not start with '[', might not be a JSON array");
            // 尝试找到JSON数组的开始位置
            int startIndex = cleaned.indexOf("[");
            if (startIndex >= 0) {
                cleaned = cleaned.substring(startIndex);
            } else {
                // 如果找不到JSON数组，返回空数组
                return "[]";
            }
        }
        
        // 如果不是以]结尾，尝试找到结束位置
        if (!cleaned.trim().endsWith("]")) {
            int endIndex = cleaned.lastIndexOf("]");
            if (endIndex >= 0) {
                cleaned = cleaned.substring(0, endIndex + 1);
            }
        }
        
        return cleaned;
    }
}
