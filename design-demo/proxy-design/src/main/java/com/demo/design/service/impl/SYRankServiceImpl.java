package com.demo.design.service.impl;

import com.demo.design.service.RankService;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service("syRankService")
@Primary
public class SYRankServiceImpl implements RankService {
    @Override
    public void edit() {
        System.out.println("SY");
    }
}
