package org.csu.mypetstore_springboot.service;

import org.csu.mypetstore_springboot.domain.Account;

import java.util.List;

public interface AccountService {

    public Account getAccount(String username);

    public Account getAccount(String username, String password);

    public void insertAccount(Account account);

    public void updateAccount(Account account) ;

    public List<String> getUsernames();
}
