package com.hzih.sslvpn.service.impl;

import cn.collin.commons.domain.PageResult;
import com.hzih.sslvpn.dao.BackUpDao;
import com.hzih.sslvpn.domain.BackUp;
import com.hzih.sslvpn.service.BackUpService;

public class BackUpServiceImpl implements BackUpService {

    public BackUpDao getBackUpDao() {
        return backUpDao;
    }

    public void setBackUpDao(BackUpDao backUpDao) {
        this.backUpDao = backUpDao;
    }

    private BackUpDao backUpDao;

    @Override
    public boolean add(BackUp backUp) throws Exception {
        return backUpDao.add(backUp);
    }

    @Override
    public boolean delete(BackUp backUp) throws Exception {
        return backUpDao.delete(backUp);
    }

    @Override
    public PageResult findByPages(int start, int limit) throws Exception {
        return backUpDao.findByPages(start,limit);
    }

    @Override
    public BackUp findById(int id) throws Exception {
        return backUpDao.findById(id);
    }
}
