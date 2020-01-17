package com.demo.design.controller;

import com.demo.design.annotation.CustomAutowire;
import com.demo.design.service.RankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class RankController {

    @Resource(name = "dlRankService")
    RankService dlRankService;

    @Resource(name = "syRankService")
    RankService syRankService;

    @Resource(name = "hfRankService")
    RankService hfRankService;

    @CustomAutowire
    RankService rankService;

    @GetMapping("/edit")
    public void edit(@RequestParam(value = "syscode", required = false) String syscode){

        rankService.edit();
        hfRankService.edit();
        dlRankService.edit();
        syRankService.edit();

    }
}
