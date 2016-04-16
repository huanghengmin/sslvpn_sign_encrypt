package com.hzih.sslvpn.service;

import cn.collin.commons.domain.PageResult;
import com.hzih.sslvpn.domain.BackUp;


public interface BackUpService {
    public boolean add(BackUp backUp)throws Exception;

    public boolean delete(BackUp backUp)throws Exception;

    public PageResult findByPages(int start, int limit)throws Exception;

    public BackUp findById(int id) throws Exception;
}
