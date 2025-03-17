package com.example.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.entity.Account;
import com.example.exception.DuplicateUsernameException;
import com.example.repository.AccountRepository;

@Service
public class AccountService {
    AccountRepository accountRepository;
    public AccountService (AccountRepository accountRepository){
        this.accountRepository=accountRepository;
    }

    public Optional<Account> register (Account account){
        if (account.getUsername().isBlank() || account.getPassword().length() < 4) {
            return Optional.empty();
        }
        if (accountRepository.findByUsername(account.getUsername()).isPresent()) {
            throw new DuplicateUsernameException("Username already exists");
        }
        return Optional.of(accountRepository.save(account));
    }
    public Optional<Account> login(String username, String password) {
        return accountRepository.findByUsername(username)
                .filter(acc -> acc.getPassword().equals(password));
    }
}
