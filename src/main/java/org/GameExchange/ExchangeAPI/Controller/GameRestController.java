package org.GameExchange.ExchangeAPI.Controller;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import org.GameExchange.ExchangeAPI.Model.Condition;
import org.GameExchange.ExchangeAPI.Model.GameJpaRepository;
import org.GameExchange.ExchangeAPI.Model.GameOwnerRecord;
import org.GameExchange.ExchangeAPI.Model.PersonJpaRepository;
import org.GameExchange.ExchangeAPI.Model.PublisherJpaRepository;
import org.GameExchange.ExchangeAPI.Model.Condition.ConditionName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/Game")
public class GameRestController extends ApplicationRestController{

    @Autowired
    private GameJpaRepository gameJpaRepository;

    @Autowired 
    private PublisherJpaRepository publisherJpaRepository;
    
    @RequestMapping(path="", method=RequestMethod.POST)
    public Map<String, String> addGameToOwner(@RequestBody Map<String, String> input,@RequestHeader("Authorization") String authorization){
        String[] userInfo = new String[3];
        userInfo[0] = input.get("gameTitle");
        userInfo[1] = input.get("system");
        userInfo[2] = input.get("condition");
        
        String[] creds = decriptCreds(authorization);

        for (int i = 0; i < userInfo.length; i++){
            if (userInfo[i] == null || userInfo[i].isBlank()){
                
            }
        }
        
        try{
        int ownerId = personJpaRepository.getPersonId(creds[0], creds[1]);
        } catch (Exception e){
            System.out.println(e.getMessage());
        }

        
        return mapMessage;
    }

    public Condition getCondition(String conditionName){
        switch (conditionName.toLowerCase()) {
            case "mint":
                return new Condition(1, ConditionName.MINT);
            case "good":
                return new Condition(2, ConditionName.GOOD);
            case "fair":
                return new Condition(3, ConditionName.FAIR);
            case "poor":
                return new Condition(4, ConditionName.POOR);
            default:
                return null;
        }
    }

    public String[] decriptCreds(String creds){
        creds = creds.substring(6);
        creds = new String(Base64.getDecoder().decode(creds));
        String[] credsReturn = creds.split(":");
        credsReturn[1] = ProtectionController.hash(credsReturn[1]);
        return credsReturn;
    }
}
