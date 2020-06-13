package com.neu.analysis.service;

import com.neu.analysis.dao.HisDao;
import com.neu.analysis.dao.SearchDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class HisService {

    private final HisDao hisDao;
    private final SearchDao searchDao;

    @Autowired
    public HisService(HisDao hisDao,SearchDao searchDao) {
        this.hisDao = hisDao;
        this.searchDao = searchDao;
    }

    @Transactional
    public Map<String,Long[]> getHisTotal(){
        Map<String,Long[]> result=hisDao.getHisTotal();
        return result;
    }

    @Transactional
    public Map<String,Long[]> getHisFlow(){
        Map<String,Long[]> result=hisDao.getHisFlow("1000000000000","3000000000000");
        return result;
    }

    @Transactional
    public Map<String,String>[] getHisSearch(String keyword){
        String[] params=keyword.substring(12,keyword.length()-2).split("&");
        Map<String,List<String>> map=new HashMap<>();
        for(String param:params){
            String[] all=param.split("=");
            map.put(all[0], Arrays.asList(all[1].split(",")));
        }
        List<String> ids=searchDao.getHisSearch(map);
        Map<String,String>[] re=searchDao.getData(ids);
        //System.out.println(re.length);
        return re;
    }
}
