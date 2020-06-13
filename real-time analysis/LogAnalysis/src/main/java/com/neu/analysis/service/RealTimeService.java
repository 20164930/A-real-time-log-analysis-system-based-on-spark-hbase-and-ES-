package com.neu.analysis.service;

import com.neu.analysis.configuration.ScheduleTask;
import com.neu.analysis.dao.RealTimeDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
public class RealTimeService {
    private final RealTimeDao realTimeDao;

    @Autowired
    public RealTimeService(RealTimeDao realTimeDao) {
        this.realTimeDao = realTimeDao;
    }

    @Transactional
    public  Map<Long,Long> getRealTotal(){
        return ScheduleTask.getTotalMap();
    }

    @Transactional
    public  Map<Long,Long> getRealReal(){
        return ScheduleTask.getRealMap();
    }
}
