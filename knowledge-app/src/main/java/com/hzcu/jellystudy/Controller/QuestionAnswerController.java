package com.hzcu.jellystudy.Controller;

import com.hzcu.jellystudy.Api.KnowledgeService;
import com.hzcu.jellystudy.Service.KnowledgeServiceImpl;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class QuestionAnswerController {

    @DubboReference(version = "1.0.0")
    private KnowledgeService knowledgeService;

    @GetMapping("/question/{id}")
    public String getQuestionAnswer(@PathVariable String id) {
        return "Answer based on: " + knowledgeService.getKnowledgeById(id);
    }
}