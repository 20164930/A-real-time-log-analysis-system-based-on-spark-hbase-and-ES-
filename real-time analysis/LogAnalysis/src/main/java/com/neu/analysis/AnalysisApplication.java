package com.neu.analysis;

import com.neu.analysis.configuration.MapLocal;
import com.neu.analysis.configuration.ScheduleTask;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Date;

@EnableScheduling
@SpringBootApplication
public class AnalysisApplication {

    public static void main(String[] args) {
        SpringApplication.run(AnalysisApplication.class, args);
        ScheduleTask.setStartTime(ScheduleTask.getTimeStamp());
        MapLocal.setMap();
    }

}
