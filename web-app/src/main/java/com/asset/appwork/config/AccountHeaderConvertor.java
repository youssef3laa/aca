package com.asset.appwork.config;

import com.asset.appwork.dto.Account;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * Created by karim on 11/5/20.
 */
@Component
public class AccountHeaderConvertor implements Converter<String, Account> {
    @Override
    public Account convert(String s) {
        ObjectMapper mapper = new ObjectMapper();
        Account account = null;
        try {
            account = mapper.readValue(s, Account.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return account;
    }
}
