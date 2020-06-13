package com.neu.analysis.controller;

import com.neu.analysis.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@CrossOrigin//跨域问题
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping("/favSong")
    public Map<String,Long> getFavSong(){
        Map<String,Long> re=userService.getUserFavSong();
        for(Map.Entry<String,Long> entry:re.entrySet()){
        System.out.println(re.size()+" "+entry.getKey()+" "+entry.getValue());
        }
        return re;
    }

    @RequestMapping("/favSinger")
    public Map<String,Long> getFavSinger(){
        Map<String,Long> re=userService.getUserFavSinger();
        return re;
    }

    @RequestMapping("/favRMSong")
    public Map<String,Long> getFavRMSong(){
        Map<String,Long> re=userService.getUserFavRMSong();
        return re;
    }

    @RequestMapping("/favRMSinger")
    public Map<String,Long> getFavRMSinger(){
        Map<String,Long> re=userService.getUserFavRMSinger();
        for(Map.Entry<String,Long> entry:re.entrySet()){
            System.out.println(re.size()+" "+entry.getKey()+" "+entry.getValue());
        }
        return re;
    }

    @RequestMapping("/userLocal")
    public Map<String,Long> getUserLocal(){
        Map<String,Long> re=userService.getUserLocal();
        //for(Map.Entry<String,Long> entry:re.entrySet()){
            //System.out.println(re.size()+" "+entry.getKey()+" "+entry.getValue());
        //}
        return re;
    }

    @RequestMapping("/userSearch")
    public Map<String,Long> getUserSearch(){
        Map<String,Long> re=userService.getUserSearch();
        return re;
    }

}
