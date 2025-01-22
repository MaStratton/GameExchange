package org.GameExchange.ExchangeAPI.Controller;

import java.util.Base64;
import java.util.Map;

import org.GameExchange.ExchangeAPI.Model.Condition;
import org.GameExchange.ExchangeAPI.Model.GameJpaRepository;
import org.GameExchange.ExchangeAPI.Model.GameOwnerRecord;
import org.GameExchange.ExchangeAPI.Model.GameOwnerRecordJpaRepository;
import org.GameExchange.ExchangeAPI.Model.GameSystem;
import org.GameExchange.ExchangeAPI.Model.GameSystemJpaRepository;
import org.GameExchange.ExchangeAPI.Model.Person;
import org.GameExchange.ExchangeAPI.Model.PublisherJpaRepository;
import org.GameExchange.ExchangeAPI.Model.Game;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
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
    
    @RequestMapping(method=RequestMethod.POST)
    public ResponseEntity<Map<String, String>> addGameToOwner(@RequestHeader("Authorization") String authorization, @RequestBody Map<String,String> input){
        
        String[] creds = decriptCreds(authorization);

        String[] userInfo = new String[4];
        userInfo[0] = input.get("gameTitle");
        userInfo[1] = input.get("gameSystem");
        userInfo[2] = input.get("condition");

        for (int i = 0; i < userInfo.length - 1; i++){
            if (userInfo[i] == null || userInfo[i].isBlank()){
                mapMessage.put("MissingGameInfo", "Missing Important Game Information");
                return ResponseEntity.status(400).body(getReturnMap());
            }
        }
        
        
        Game game = getGame(userInfo[0]);
        GameSystem gameSystem = getGameSystem(userInfo[1]);
        Condition condition = getCondition(userInfo[2].toLowerCase());
        Person owner = new Person();


        
        try{
            owner = personJpaRepository.findByCreds(creds[0], creds[1]).get(0);
        } catch (IndexOutOfBoundsException e){
            mapMessage.put("OwnershipError", "System Encountered Unexpected Error When Establishing Owner");
            return ResponseEntity.status(400).body(getReturnMap());
        }

        if (game == null || gameSystem == null || condition == null || owner == null){
            mapMessage.put("RecordsNotFound", "One or More Records Not Found, See Other Error Returns");
            return ResponseEntity.status(400).body(getReturnMap());
        }

        try{
            GameOwnerRecord record = new GameOwnerRecord(owner, game, gameSystem, condition);
            gameOwnerRecordJpaRepository.save(record);
            mapMessage.put("InputSuccessFul","Record Inputted Successfully");
            return ResponseEntity.status(201).body(getReturnMap());
        } catch (Exception e){
            mapMessage.put("UnexpectedException", "System Has Encountered an Unexpexted Issue");
            return ResponseEntity.status(500).body(getReturnMap());
        }
        
    }
    
    
    @RequestMapping(path="/{Id}", method=RequestMethod.PUT)
    public ResponseEntity<Map<String, String>> updateGameOwnerRecord(@PathVariable int Id, @RequestHeader("Authorization") String auth, @RequestBody Map<String, String> input){
        String[] creds = decriptCreds(auth);
        String[] userInput = new String[2];
        System.out.println(input);
        userInput[0] = input.get("condition");
        userInput[1] = input.get("system");

        for (int i = 0; i < userInput.length; i++){
            if (userInput[i] == null){
                mapMessage.put("MissingInformation", "Missing Game Information. Required: condition, system");
                return ResponseEntity.status(400).body(getReturnMap());
            }
        }

        Condition condition = getCondition(userInput[0].toLowerCase());
        GameSystem system = getGameSystem(userInput[1]);

        if (condition == null || system == null){
            mapMessage.put("RecordsNotFound","Records Not Found. See Error Output");
            return ResponseEntity.status(400).body(getReturnMap());
        }


        Person owner = personJpaRepository.findByCreds(creds[0], creds[1]).get(0);

        GameOwnerRecord record = getByIdAndOwner(Id, owner.getPersonId());
        if (record == null){
            return ResponseEntity.status(400).body(getReturnMap());
        }

        System.out.println(record.toFullString());

        record.setGameSystem(system);
        record.setCondition(condition);

        try {
            gameOwnerRecordJpaRepository.save(record);
            mapMessage.put("UpdateSuccessful", "Record Update Successfull");
            return ResponseEntity.status(200).body(getReturnMap());

        } catch (Exception e){
            System.out.println(e.getCause());
            mapMessage.put("UnexpectedException", "System Has Encountered an Unexpexted Issue");
            return ResponseEntity.status(500).body(getReturnMap());
        }
    }

    @RequestMapping(path="", method=RequestMethod.GET)
    public String getAvailableGames(){
        return gameOwnerRecordJpaRepository.findAllAvailable().toString();
    }

    public Condition getCondition(String conditionLable){
        try {
            switch (conditionLable.toLowerCase()) {
                case "mint":
                    return new Condition(1, "MINT");
                case "good":
                    return new Condition(2, "GOOD");
                case "fair":
                    return new Condition(3, "FAIR");
                case "poor":
                    return new Condition(4, "POOR");
                default:
                    mapMessage.put("ConditionError","Invalid Condition Inputted");
                    return null;
            }
    } catch (Exception e){
        mapMessage.put("ConditionError","Invalid Condition Inputted");
        System.out.println(e.getMessage());
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

    public GameOwnerRecord getByIdAndOwner(int Id, int ownerId){
        try{
            GameOwnerRecord record = gameOwnerRecordJpaRepository.findByGameOwnerRecordIdAndOwnerPersonId(Id, ownerId).get(0);
            return record;
        } catch (IndexOutOfBoundsException e){
            mapMessage.put("GameOwnerRecordNotFound", "No Game Owner Record Exists By That Id for Current User");
            return null;
        }
    }

}
