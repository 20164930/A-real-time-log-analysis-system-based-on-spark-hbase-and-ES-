package com.neu.analysis.controller;

import com.neu.analysis.service.RealTimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@CrossOrigin//跨域问题
public class RealTimeController {

    private final RealTimeService realTimeService;
    @Autowired
    public RealTimeController(RealTimeService realTimeService) {
        this.realTimeService = realTimeService;
    }

    @RequestMapping("/realTotal")
    public Map<Long,Long> getRealTotal(){
        Map<Long,Long> re=realTimeService.getRealTotal();
        return re;
    }

    @RequestMapping("/realReal")
    public Map<Long,Long> getRealReal(){
        Map<Long,Long> re=realTimeService.getRealReal();
        return re;
    }
}
