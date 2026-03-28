package com.cjy.controller;

import com.cjy.record.KnowledgeIngestDTO;
import com.cjy.record.FileIngestDTO;
import com.cjy.record.R;
import com.cjy.service.KnowledgeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/knowledge")
@RequiredArgsConstructor
public class KnowledgeController {

    private final KnowledgeService knowledgeService;

    @RequestMapping("/ingest") //Post
    public R<String> ingestKnowledge(@RequestBody KnowledgeIngestDTO dto){
       if (!knowledgeService.ingestKnowledge(dto)){
            return R.error("存入数据库过程中出现异常，请稍后重试！");
        }
        // 提示：你甚至可以通过 splitter.split(document).size() 来获取到底切了多少块
        return R.success("String 灌库成功！数据已落盘 Zilliz Cloud");
    }

    @PostMapping("/fileIngest")
    public R<String> ingestPDF(@RequestBody FileIngestDTO dto){
        if (!knowledgeService.ingestFile(dto))
            return R.error("存入数据库过程中出现异常，请稍后重试！");

        return R.success("File 灌库成功！数据已落盘 Zilliz Cloud");
    }
}
