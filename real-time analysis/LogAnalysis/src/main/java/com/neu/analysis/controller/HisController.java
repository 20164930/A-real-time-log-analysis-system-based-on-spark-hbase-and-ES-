package com.neu.analysis.controller;

import com.neu.analysis.service.HisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@CrossOrigin//跨域问题
public class HisController {

    private final HisService hisService;
    @Autowired
    public HisController(HisService hisService) {
        this.hisService = hisService;
    }

    @RequestMapping("/hisTotal")
    public Map<String,Long[]> getHisTotal(){
        long start=System.currentTimeMillis();
        Map<String,Long[]> re=hisService.getHisTotal();
        long end=System.currentTimeMillis();
        System.out.println("start: "+start+" end: "+end+" used: "+(end-start));
        return re;
    }

    @RequestMapping("/hisFlow")
    public Map<String,Long[]> getHisFlow(){
        Map<String,Long[]> re=hisService.getHisFlow();
        return re;
    }

    @RequestMapping("/hisSearch")
    public Map<String,String>[] getHisSearch(@RequestBody String keyword){
        Map<String,String>[] re=hisService.getHisSearch(keyword);
        //System.out.println(re.length);
        return re;
    }



}
