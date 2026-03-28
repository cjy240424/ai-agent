package com.cjy.service;

import com.cjy.record.FileIngestDTO;
import com.cjy.record.KnowledgeIngestDTO;


public interface KnowledgeService {

    boolean ingestKnowledge(KnowledgeIngestDTO dto);

    boolean ingestFile(FileIngestDTO dto);

}
