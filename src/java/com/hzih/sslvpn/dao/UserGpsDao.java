package com.hzih.sslvpn.dao;

import com.hzih.sslvpn.domain.UserGps;

/**
 * Created by hhm on 2014/12/17.
 */
public interface UserGpsDao {

    public boolean add(UserGps userGps)throws Exception;

    public boolean modify(UserGps userGps)throws Exception;

    public boolean delete(UserGps userGps)throws Exception;

    public UserGps findById(int id)throws Exception;
}
