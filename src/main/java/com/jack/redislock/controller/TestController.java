package com.jack.redislock.controller;

import com.jack.redislock.service.GoodService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api("测试Con")
@RequestMapping("/api")
public class TestController {
    @Autowired
    private GoodService goodService;

    @ApiOperation("测试")
    @RequestMapping(value="/a/{id}", method = RequestMethod.GET)
    public boolean a(@PathVariable int id){
        boolean miaosha = goodService.miaosha(id);
        return miaosha;
    }

}
