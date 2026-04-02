package com.cjy.service;

import com.cjy.record.FileIngestDTO;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.DocumentParser;
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.data.document.parser.TextDocumentParser;
import dev.langchain4j.data.document.parser.apache.pdfbox.ApachePdfBoxDocumentParser;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DevOpsKnowledgeServiceImpl implements DevOpsKnowledgeService{

    private final EmbeddingStoreIngestor embeddingStoreIngestor;

    @Override
    public boolean ingestFile(FileIngestDTO dto) {
        Path path = Paths.get(Optional.ofNullable(dto.filePath()).orElse( null));
        if(path == null){
            throw new RuntimeException("请上传有效的文件地址");
        }

        String fileName = path.getFileName().toString().toLowerCase();
        // FileSystemDocumentLoader 充当物理钥匙，结合 pdfParser 将字节流转化为结构化的 Document 对象
        DocumentParser parser;

        if (fileName.endsWith(".pdf")) {
            // 遇到 PDF，出动重型装甲车
            parser = new ApachePdfBoxDocumentParser();
            System.out.println("📄 探测到 PDF 文件，已启动 PDFBox 解析引擎...");
        } else if (fileName.endsWith(".txt") || fileName.endsWith(".md")) {
            // 遇到纯文本，出动轻量级步兵
            parser = new TextDocumentParser();
            System.out.println("📝 探测到纯文本文件，已启动基础文本解析引擎...");
        } else {
            throw new IllegalArgumentException("❌ 暂不支持的文件格式: " + fileName);
        }

        // 使用对应的解析器加载文档
        Document document = FileSystemDocumentLoader.loadDocument(path, parser);

        // 👑 2. 强行追加业务标签！(极其关键)
        document.metadata().put("category", dto.category());

        // 存入数据库
        try {
            embeddingStoreIngestor.ingest(document);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
