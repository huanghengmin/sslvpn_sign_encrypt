package com.hzih.sslvpn.service;

import com.hzih.sslvpn.domain.Account;

public interface LoginService {

	Account getAccountByNameAndPwd(String name, String pwd) ;

    Account getAccountByName(String name) ;
}
