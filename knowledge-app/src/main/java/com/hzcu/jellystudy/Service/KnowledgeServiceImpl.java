package com.hzcu.jellystudy.Service;

import com.hzcu.jellystudy.Api.KnowledgeService;
import org.apache.dubbo.config.annotation.DubboService;

@DubboService(version = "1.0.0")
public class KnowledgeServiceImpl implements KnowledgeService {
    @Override
    public String getKnowledgeById(String id) {
        return "Knowledge with ID: " + id;
    }
}
