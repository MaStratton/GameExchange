package org.GameExchange.ExchangeAPI.Controller;

import java.util.Base64;
import java.util.Map;

import org.GameExchange.ExchangeAPI.Model.GameOwnerRecord;
import org.GameExchange.ExchangeAPI.Model.PersonJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/Game")
public class GameRestController {

    @Autowired
    private PersonJpaRepository personJpaRepository;
    
    @RequestMapping(path="", method=RequestMethod.POST)
    public GameOwnerRecord addGameToOwner(Map<String, String> input,@RequestHeader("Authorization") String authorization){
        String[] creds = decriptCreds(authorization);
        try{
        int ownerId = personJpaRepository.getPersonId(creds[0], creds[1]);
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
        return new GameOwnerRecord();
    }

    public String[] decriptCreds(String creds){
        creds = creds.substring(6);
        creds = new String(Base64.getDecoder().decode(creds));
        String[] credsReturn = creds.split(":");
        credsReturn[1] = ProtectionController.hash(credsReturn[1]);
        return credsReturn;
    }
}
