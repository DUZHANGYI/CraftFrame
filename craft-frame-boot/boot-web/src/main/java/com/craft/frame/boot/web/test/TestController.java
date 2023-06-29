package com.craft.frame.boot.web.test;

import com.craft.frame.boot.web.annotation.FrequencyControl;
import com.craft.frame.boot.web.annotation.RepeatSubmit;
import com.craft.frame.boot.web.response.Operation;
import com.craft.frame.boot.web.response.SingleData;
import com.craft.frame.boot.web.security.SecurityTool;
import com.craft.frame.boot.web.security.annotation.Anonymous;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.UUID;

/**
 * @author DURR
 * @desc 描述
 * @date 2023/6/26 9:58
 */

@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private SecurityTool securityTool;


    //测试频控注解  1秒1次
    @Anonymous //匿名访问
    @FrequencyControl(time = 1, count = 1, target = FrequencyControl.Target.UID)
    @GetMapping("frequencyControl")
    public SingleData<?> frequencyControl() {
        return SingleData.succ(UUID.randomUUID().toString());
    }

    // 间隔时间 三秒内表单数据一样则为重复提交
    @RepeatSubmit(interval = 3000)
    @GetMapping("repeatSubmit")
    public SingleData<?> repeatSubmit(@RequestParam("test") String test) {
        return SingleData.succ(UUID.randomUUID().toString());
    }

    @Anonymous
    @GetMapping("/login")
    public SingleData<?> login() {
        String token = securityTool.generateToken("DURR", "1", Arrays.asList("ROLE_admin", "add"), false);
        return SingleData.succ(token);
    }

    @GetMapping("/exit")
    public Operation<?> exit() {
        securityTool.lostCurrUserToken();
        return Operation.succ();
    }

    @PreAuthorize("hasAnyRole('ROLE_admin')")
    @GetMapping("/auth")
    public SingleData<?> auth() {
        return SingleData.succ(UUID.randomUUID().toString());
    }

}
