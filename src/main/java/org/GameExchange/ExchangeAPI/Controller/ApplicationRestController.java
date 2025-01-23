package org.GameExchange.ExchangeAPI.Controller;

import java.util.Base64;
import java.util.LinkedHashMap;

import org.GameExchange.ExchangeAPI.Model.GameJpaRepository;
import org.GameExchange.ExchangeAPI.Model.GameOwnerRecordJpaRepository;
import org.GameExchange.ExchangeAPI.Model.PersonJpaRepository;

import org.springframework.beans.factory.annotation.Autowired;

public class ApplicationRestController {

        @Autowired
        protected PersonJpaRepository personJpaRepository;

        @Autowired
        protected GameOwnerRecordJpaRepository gameOwnerRecordJpaRepository;

        @Autowired
        protected GameJpaRepository gameJpaRepository;


    public LinkedHashMap<String, String> mapMessage = new LinkedHashMap<String, String>();

    public LinkedHashMap<String, String> getReturnMap(){
        LinkedHashMap<String, String> mapReturn = new LinkedHashMap<String, String>();
        mapReturn.putAll(mapMessage);
        mapMessage.clear();
        return mapReturn;
    }
    
        public String[] decriptCreds(String creds){
        creds = creds.substring(6);
        creds = new String(Base64.getDecoder().decode(creds));
        String[] credsReturn = creds.split(":");
        credsReturn[1] = ProtectionController.hash(credsReturn[1]);
        return credsReturn;
    }
}
