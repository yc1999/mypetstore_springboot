package org.csu.mypetstore_springboot.persistence;

import org.csu.mypetstore_springboot.domain.Account;

import java.util.List;

public interface AccountMapper {

    Account getAccountByUsername(String username);

    Account getAccountByUsernameAndPassword(Account account);

    List<String> getUsernames();

    void insertAccount(Account account);

    void insertProfile(Account account);

    void insertSignon(Account account);

    void updateAccount(Account account);

    void updateProfile(Account account);

    void updateSignon(Account account);
}
