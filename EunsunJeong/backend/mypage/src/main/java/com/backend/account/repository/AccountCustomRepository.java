package com.backend.account.repository;

public interface AccountCustomRepository {
    void save(String email, String loginType);
    void saveAdmin(String email, String loginType);
    boolean deleteAccount(Long accountId);
}
