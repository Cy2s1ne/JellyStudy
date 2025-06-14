package com.hzcu.jellystudy.Controller;

import com.hzcu.jellystudy.Entity.QA_table;
import com.hzcu.jellystudy.Service.QAtableServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/qa-table")
public class QAtableController {

    private static final Logger logger = LoggerFactory.getLogger(QAtableController.class);
    
    @Autowired
    private QAtableServiceImpl qAtableService;
    
    @Autowired
    private RestTemplate restTemplate;
    
    @Autowired
    private ObjectMapper objectMapper;

    @PostMapping
    public ResponseEntity<QA_table> createQAtable(@RequestBody QA_table qa_table) {
        return new ResponseEntity<>(qAtableService.createQAtable(qa_table), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<QA_table> updateQAtable(@PathVariable String id, @RequestBody QA_table qa_table) {
        return ResponseEntity.ok(qAtableService.updateQAtable(id, qa_table));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQAtable(@PathVariable String id) {
        qAtableService.deleteQAtable(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<QA_table> getQAtableById(@PathVariable String id) {
        return ResponseEntity.ok(qAtableService.getQAtableById(id));
    }

    @GetMapping("/question")
    public ResponseEntity<List<QA_table>> generateQuestionsFromPopular(
            @RequestParam(value = "limit", defaultValue = "5") int limit,
            @RequestParam(value = "questionCount", defaultValue = "3") int questionCount,
            @RequestParam(value = "questionType", defaultValue = "选择题") String questionType,
            @RequestParam(value = "difficulty", defaultValue = "中等") String difficulty) {
        
        logger.info("Received request to generate questions from popular questions with limit: {}, questionCount: {}, questionType: {}, difficulty: {}", 
                   limit, questionCount, questionType, difficulty);
        
        try {
            // 1. 调用question-answer-app获取高人气问题
            String highPopularityUrl = "http://localhost:8080/api/questions/high-popularity?limit=" + limit;
            ResponseEntity<String> popularQuestionsResponse = restTemplate.getForEntity(highPopularityUrl, String.class);
            
            if (!popularQuestionsResponse.getStatusCode().is2xxSuccessful()) {
                logger.error("Failed to get high popularity questions. Status code: {}", popularQuestionsResponse.getStatusCode());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
            
            // 2. 解析高人气问题，提取知识点
            JsonNode questionsArray = objectMapper.readTree(popularQuestionsResponse.getBody());
            List<QA_table> savedQuestions = new ArrayList<>();
            
            // 3. 为每个高人气问题生成新问题
            for (JsonNode questionNode : questionsArray) {
                // 提取知识点
                JsonNode knowledgePointsNode = questionNode.get("knowledgePoints");
                if (knowledgePointsNode == null || knowledgePointsNode.isEmpty()) {
                    continue; // 跳过没有知识点的问题
                }
                
                // 从第一个知识点开始生成问题
                for (JsonNode knowledgePointNode : knowledgePointsNode) {
                    String knowledgePoint = knowledgePointNode.asText();
                    
                    // 4. 调用ai-app生成问题
                    String generateUrl = String.format(
                        "http://localhost:8082/generate/question?knowledgePoint=%s&questionType=%s&difficulty=%s&count=%d",
                        knowledgePoint, questionType, difficulty, questionCount
                    );
                    
                    ResponseEntity<String> generatedQuestionsResponse = restTemplate.getForEntity(generateUrl, String.class);
                    
                    if (!generatedQuestionsResponse.getStatusCode().is2xxSuccessful()) {
                        logger.error("Failed to generate questions for knowledge point: {}. Status code: {}", 
                                    knowledgePoint, generatedQuestionsResponse.getStatusCode());
                        continue;
                    }
                    
                    // 5. 解析生成的问题并保存到数据库
                    JsonNode generatedQuestions = objectMapper.readTree(generatedQuestionsResponse.getBody());
                    for (JsonNode generatedQuestion : generatedQuestions) {
                        QA_table qaTable = new QA_table();
                        qaTable.setQuestion(generatedQuestion.get("question").asText());
                        qaTable.setAnswer(generatedQuestion.get("answer").asText());
                        qaTable.setExplanation(generatedQuestion.get("explanation").asText());
                        
                        // 保存到数据库
                        QA_table savedQA = qAtableService.createQAtable(qaTable);
                        savedQuestions.add(savedQA);
                        
                        logger.info("Saved generated question to database with ID: {}", savedQA.get_id());
                    }
                    
                    // 只处理第一个知识点，避免生成太多问题
                    break;
                }
            }
            
            return ResponseEntity.ok(savedQuestions);
            
        } catch (Exception e) {
            logger.error("Error generating questions from popular questions", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
} 