package org.GameExchange.ExchangeAPI.Controller;

import java.util.Base64;
import java.util.Map;

import org.GameExchange.ExchangeAPI.Model.Condition;
import org.GameExchange.ExchangeAPI.Model.GameJpaRepository;
import org.GameExchange.ExchangeAPI.Model.GameOwnerRecord;
import org.GameExchange.ExchangeAPI.Model.GameSystem;
import org.GameExchange.ExchangeAPI.Model.GameSystemJpaRepository;
import org.GameExchange.ExchangeAPI.Model.Person;
import org.GameExchange.ExchangeAPI.Model.PublisherJpaRepository;
import org.GameExchange.ExchangeAPI.Model.Game;

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

    // @Autowired 
    // private PublisherJpaRepository publisherJpaRepository;

    @Autowired
    private GameSystemJpaRepository gameSystemJpaRepository;
    
    @RequestMapping( method=RequestMethod.POST)
    public Map<String, String> addGameToOwner(@RequestHeader("Authorization") String authorization, @RequestBody Map<String,String> input){
        
        String[] creds = decriptCreds(authorization);

        String[] userInfo = new String[4];
        userInfo[0] = input.get("gameTitle");
        userInfo[1] = input.get("gameSystem");
        userInfo[2] = input.get("condition");

        

        for (int i = 0; i < userInfo.length - 1; i++){
            if (userInfo[i] == null || userInfo[i].isBlank()){
                mapMessage.put("MissingGameInfo", "Missing Important Game Information");
                return getReturnMap();
            }
        }
        
        
        Game game = getGame(userInfo[0]);
        System.out.println("TITS");
        GameSystem gameSystem = getGameSystem(userInfo[1]);
        Condition condition = getCondition(userInfo[2]);
        Person owner = new Person();


        
        try{
            owner = personJpaRepository.getPersonByCreds(creds[0], creds[1]).get(0);
        } catch (IndexOutOfBoundsException e){
            mapMessage.put("OwnershipError", "System Encountered Unexpected Error When Establishing Owner");
            return getReturnMap();
        }

        if (game == null || gameSystem == null || condition == null || owner == null){
            mapMessage.put("RecordsNotFound", "One or More Records Not Found, See Other Error Returns");
            return getReturnMap();
        }

        try{
            GameOwnerRecord record = new GameOwnerRecord(owner, game, gameSystem, condition);
            gameOwnerRecordJpaRepository.save(record);
            mapMessage.put("InputSuccessFul","Record Inputted Successfully");
            return getReturnMap();
        } catch (Exception e){
            mapMessage.put("UnexpectedException", "System Has Encountered an Unexpexted Issue");
            return getReturnMap();
        }

        
    }

    public Condition getCondition(String conditionName){
        switch (conditionName.toLowerCase()) {
            case "mint":
                return new Condition(1, "MINT");
            case "good":
                return new Condition(2, "GOOD");
            case "fair":
                return new Condition(3, "FAIR");
            case "poor":
                return new Condition(4, "POOR");
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

    public Game getGame(String gameTitle){
        try {
            Game game = gameJpaRepository.findByTitle(gameTitle).get(0);
            return game;
        } catch (IndexOutOfBoundsException e){
            mapMessage.put("GameDoesNotExist", "No Game Exists by That Title, Please Add To Records");
            return null;
        }
    }

    public GameSystem getGameSystem(String systemName){
        try{
            GameSystem gameSystem = gameSystemJpaRepository.findByName(systemName).get(0);
            return gameSystem;
        } catch (IndexOutOfBoundsException e){
            mapMessage.put("GameSystemDoesNotExist", "No System Exists by That Name, Please Add To Records");
            return null;
        }
    }

}
