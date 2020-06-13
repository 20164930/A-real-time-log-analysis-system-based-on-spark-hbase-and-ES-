package com.neu.analysis.service;

import com.neu.analysis.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
public class UserService {
    private final UserDao userDao;

    @Autowired
    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    @Transactional
    public Map<String,Long> getUserFavSong(){
        Map<String,Long> result=userDao.getUserFavoriteSong();
        return result;
    }

    @Transactional
    public Map<String,Long> getUserFavSinger(){
        Map<String,Long> result=userDao.getUserFavoriteSinger();
        return result;
    }

    @Transactional
    public Map<String,Long> getUserFavRMSong(){
        Map<String,Long> result=userDao.getHisUserFavoriteRMSong();
        return result;
    }
    @Transactional
    public Map<String,Long> getUserFavRMSinger(){
        Map<String,Long> result=userDao.getUserFavoriteRMSinger();
        return result;
    }

    @Transactional
    public Map<String,Long> getUserLocal(){
        Map<String,Long> result=userDao.getUserLocal();
        return result;
    }

    @Transactional
    public Map<String,Long> getUserSearch(){
        Map<String,Long> result=userDao.getUserSearch();
        return result;
    }
}
