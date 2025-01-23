package org.GameExchange.ExchangeAPI.Controller;

import java.util.LinkedHashMap;

import org.GameExchange.ExchangeAPI.Model.GameOwnerRecordJpaRepository;
import org.GameExchange.ExchangeAPI.Model.PersonJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class ApplicationRestController {

        @Autowired
        protected PersonJpaRepository personJpaRepository;

        @Autowired
        protected GameOwnerRecordJpaRepository gameOwnerRecordJpaRepository;


    public LinkedHashMap<String, String> mapMessage = new LinkedHashMap<String, String>();

    public LinkedHashMap<String, String> getReturnMap(){
        LinkedHashMap<String, String> mapReturn = new LinkedHashMap<String, String>();
        mapReturn.putAll(mapMessage);
        mapMessage.clear();
        return mapReturn;
    }
    
}
