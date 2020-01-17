package com.demo.design.service.impl;

import com.demo.design.service.RankService;
import org.springframework.stereotype.Service;

@Service("hfRankService")
public class HFRankServiceImpl implements RankService {
    @Override
    public void edit() {
        System.out.println("HF");
    }
}
