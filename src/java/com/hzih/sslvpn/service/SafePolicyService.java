package com.hzih.sslvpn.service;

import com.hzih.sslvpn.domain.SafePolicy;

public interface SafePolicyService {

	SafePolicy getData();

    public String select() throws Exception;

    public String update(SafePolicy safePolicy) throws Exception;

    public String selectPasswordRules() throws Exception;
}
