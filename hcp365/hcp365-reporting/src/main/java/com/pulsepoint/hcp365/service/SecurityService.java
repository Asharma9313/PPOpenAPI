package com.pulsepoint.hcp365.service;

import com.pulsepoint.hcp365.modal.User;
import com.pulsepoint.hcp365.repository.SecurityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SecurityService {
    @Autowired
    SecurityRepository securityRepository;

    public List<Long> getUserAccounts(String userName){
        List<Object[]> accountIds = securityRepository.getUserAccounts(userName);
        if(CollectionUtils.isEmpty(accountIds) == false){
            return accountIds.stream().map(accountId -> Long.parseLong(accountId[0].toString())).collect(Collectors.toList());
        }
        return null;
    }

    public boolean isUserAdmin(String userName){
        List<Object[]> count = securityRepository.checkIsAdmin(userName);
        if(CollectionUtils.isEmpty(count) == false){
            return Integer.parseInt(count.get(0)[0].toString()) > 0;
        }
        return false;
    }

    public User getByUserName(String userName){
        return securityRepository.getByUserName(userName);
    }
}
