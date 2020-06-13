package com.neu.analysis.configuration;

import com.neu.analysis.dao.RealTimeDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class ScheduleTask {
    private static long startTime=0;
    private static Map<Long,Long> realMap=new LinkedHashMap<>();
    private static Map<Long,Long> totalMap=new LinkedHashMap<>();
    private static Long tail=0L;
    private final RealTimeDao realTimeDao;

    @Autowired
    public ScheduleTask(RealTimeDao realTimeDao) {
        this.realTimeDao = realTimeDao;
    }

    public static void setStartTime(Long time){
        startTime=time;
    }


    public static long getTimeStamp() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTimeInMillis();
    }

    @Scheduled(fixedRate = 3000)
    public  void setRealTimeMap(){
        long time=new Date().getTime();
        long gap=time-startTime;
        if(gap>86400000L){//已经过了24小时
            setStartTime(getTimeStamp());
            realMap.clear();
            totalMap.clear();
        }
        Map<Long,Long> tmp=realTimeDao.getRealTotal(startTime,time);
        for(Map.Entry<Long,Long> entry:tmp.entrySet()){
            //System.out.println(entry.getKey()+" "+entry.getValue());
            realMap.put(entry.getKey(),entry.getValue());
            totalMap.put(entry.getKey(),entry.getValue()+tail);
            tail+=entry.getValue();
        }
        startTime=time;
        //System.out.println(time+" "+realMap.size());
    }

    public static Map<Long,Long> getRealMap(){
        return realMap;
    }
    public static Map<Long,Long> getTotalMap(){return totalMap;}
}
