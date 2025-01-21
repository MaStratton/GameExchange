package org.GameExchange.ExchangeAPI.Controller;

import java.util.HashMap;
import java.util.Map;

import org.GameExchange.ExchangeAPI.Model.GameOwnerRecordJpaRepository;
import org.GameExchange.ExchangeAPI.Model.PersonJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class ApplicationRestController {

        @Autowired
        protected PersonJpaRepository personJpaRepository;

        @Autowired
        protected GameOwnerRecordJpaRepository gameOwnerRecordJpaRepository;


    public Map<String, String> mapMessage = new HashMap<String, String>();

    public Map<String, String> getReturnMap(){
        Map<String, String> mapReturn = new HashMap<String, String>();
        mapReturn.putAll(mapMessage);
        mapMessage.clear();
        return mapReturn;
    }
    
}
