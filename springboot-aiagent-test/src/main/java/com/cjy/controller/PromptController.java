package com.cjy.controller;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.cjy.domain.dto.PromptCreatDTO;
import com.cjy.domain.dto.PromptUpdateDTO;
import com.cjy.domain.po.Result;
import com.cjy.domain.vo.PromptVO;
import com.cjy.query.QueryPrompt;
import com.cjy.service.PromptService;
import lombok.RequiredArgsConstructor;
import com.cjy.domain.po.Prompt;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/prompt")
@RequiredArgsConstructor
public class PromptController {

    //0.注入Service
    private final PromptService promptService;

    //1. 新增提示词

    /**
     * @param promptCreatDto
     * @return
     */
    @PostMapping("/add")
    public Result addPrompt(@RequestBody PromptCreatDTO promptCreatDto){
        //判断提示词名称是否已存在
        if (promptService.isNameExist(promptCreatDto.getPromptName())){
            return Result.error("提示词名称已存在");
        }
        //添加数据
        if (promptService.addPrompt(promptCreatDto)) {
            return Result.success();
        }
        return Result.error("添加数据失败，请重新尝试");
    }

    //2. 根据id查找提示词
    @GetMapping("/{id}")
    public Result getPromptById(@PathVariable Long id){
        PromptVO vo = promptService.getPromptById(id);
        if(vo == null){
            return Result.error("提示词不存在");
        }
        return Result.success(vo);
    }

    //3. 根据条件分页查询提示词
    @GetMapping("/page")
    public Result pageQuery(QueryPrompt queryPrompt){
        return Result.success(promptService.queryPromptPage(queryPrompt));
    }

    //4. 修改提示词信息
    @PutMapping("/update")
    public Result updatePrompt(@RequestBody PromptUpdateDTO promptUpdateDto){
        //1.判断传入的信息是否同时为空？
        if(!StringUtils.isNotBlank(promptUpdateDto.getPromptName())
                && !StringUtils.isNotBlank(promptUpdateDto.getContent())){
            return Result.error("没有有效信息，请输入修改信息");
        }
        //2. 判断提示词名称是否已存在
        if (promptService.isNameExist(promptUpdateDto.getPromptName())){
            return Result.error("提示词名称已存在");
        }

        //3. 修改
        if (promptService.updatePromptById(promptUpdateDto)) {
            return Result.success();
        }
        return Result.error("修改数据失败，请重新尝试");
    }

    //5. 修改提示词状态
    @PutMapping("/status/{id}")
    public Result updatePromptStatusById(@PathVariable("id") Long id,
                                         @RequestParam("status") Boolean status){

        if (promptService.lambdaUpdate()
                .eq(Prompt::getId, id)
                .set(Prompt::getStatus, status)
                .update()) {
            return Result.success();
        }
        return Result.error("修改状态失败，请重新尝试");
    }
}
