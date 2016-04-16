package com.hzih.sslvpn.service.impl;

import com.hzih.sslvpn.dao.SafePolicyDao;
import com.hzih.sslvpn.domain.SafePolicy;
import com.hzih.sslvpn.service.SafePolicyService;
import com.hzih.sslvpn.web.SiteContext;

public class SafePolicyServiceImpl implements SafePolicyService {
	SafePolicyDao safePolicyDao;

	public void setSafePolicyDao(SafePolicyDao safePolicyDao) {
		this.safePolicyDao = safePolicyDao;
	}

	public SafePolicy getData() {
		return safePolicyDao.getData();
	}

	public String update(SafePolicy entry) throws Exception{
		SafePolicy older = (SafePolicy) safePolicyDao.getById(entry.getId());
		older.setErrorLimit(entry.getErrorLimit());
		older.setPasswordLength(entry.getPasswordLength());
		older.setPasswordRules(entry.getPasswordRules());
		older.setRemoteDisabled(entry.getRemoteDisabled());
        older.setMacDisabled(entry.getMacDisabled());
		older.setTimeout(entry.getTimeout());
        older.setLockTime(entry.getLockTime());
		safePolicyDao.update(older);

		SiteContext.getInstance().safePolicy = older;
        return "<font color=\"green\">修改成功,点击[确定]返回页面!</font>";
	}

    @Override
    public String selectPasswordRules() throws Exception {
        SafePolicy safePolicy = safePolicyDao.getData();
        String json = "{success:true,total:1,rows:[{passwordRules:'"+safePolicy.getPasswordRules()+"'}]}";
        return json;
    }

    @Override
    public String select() throws Exception{
        SafePolicy safePolicy = safePolicyDao.getData();
        String json = "{id:"+safePolicy.getId()+",timeout:"+safePolicy.getTimeout()+",lockTime:"+safePolicy.getLockTime()+
                ",passwordRules:'"+safePolicy.getPasswordRules()+"',errorLimit:"+safePolicy.getErrorLimit()+
                ",remoteDisabled:"+safePolicy.getRemoteDisabled()+",macDisabled:"+safePolicy.getMacDisabled()+"}";
        return json;
    }

}
