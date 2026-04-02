package com.cjy.tools;

import dev.langchain4j.agent.tool.Tool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DevOpsTools {

    @Tool
    public boolean TerminalTool(){
        return true;
    }
}
