package org.GameExchange.ExchangeAPI.Controller;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.GameExchange.ExchangeAPI.Model.Condition;
import org.GameExchange.ExchangeAPI.Model.ConditionJpaRepository;
import org.GameExchange.ExchangeAPI.Model.GameOwnerRecord;
import org.GameExchange.ExchangeAPI.Model.GameSystem;
import org.GameExchange.ExchangeAPI.Model.GameSystemJpaRepository;
import org.GameExchange.ExchangeAPI.Model.Person;
import org.GameExchange.ExchangeAPI.Model.Publisher;
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
    private PublisherJpaRepository publisherJpaRepository;

    @Autowired
    private GameSystemJpaRepository gameSystemJpaRepository;

    @Autowired
    private ConditionJpaRepository conditionJpaRepository;
    
    @RequestMapping(path="/Records", method=RequestMethod.POST)
    public ResponseEntity<Object> addGameToOwner(@RequestHeader("Authorization") String authorization, @RequestBody LinkedHashMap<String,String> input){
        
        String[] creds = decriptCreds(authorization);

        String[] userInfo = new String[4];
        userInfo[0] = input.get("gameId");
        userInfo[1] = input.get("systemId");
        userInfo[2] = input.get("conditionId");

        for (int i = 0; i < userInfo.length - 1; i++){
            if (userInfo[i] == null || userInfo[i].isBlank()){
                mapMessage.put("MissingGameInfo", "Missing Important Game Information");
                return ResponseEntity.status(400).body(getReturnMap());
            }
        }
                
        Game game = getGameById(userInfo[0]);
        GameSystem gameSystem = getGameSystemById(userInfo[1]);
        Condition condition = getConditionById(userInfo[2].toLowerCase());
        Person owner = new Person();

        try{
            owner = personJpaRepository.findByCreds(creds[0], creds[1]).get(0);
        } catch (IndexOutOfBoundsException e){
            mapMessage.put("OwnershipError", "System Encountered Unexpected Error When Establishing Owner");
            return ResponseEntity.status(500).body(getReturnMap());
        }

        if (game == null || gameSystem == null || condition == null || owner == null){
            mapMessage.put("RecordsNotFound", "One or More Records Not Found, See Other Error Returns");
            return ResponseEntity.status(404).body(getReturnMap());
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

    @RequestMapping(path="/Records", method=RequestMethod.GET)
    public ResponseEntity<LinkedHashMap<Integer, LinkedHashMap<String, String>>> getAvailableGames(){
        List<GameOwnerRecord> records =  gameOwnerRecordJpaRepository.findAllAvailable();
        LinkedHashMap<Integer, LinkedHashMap<String, String>> lhmReturn = new LinkedHashMap<Integer, LinkedHashMap<String, String>>();
        for (int i = 0; i < records.size(); i++){
            lhmReturn.put(i, records.get(i).toMap());
        }
        return ResponseEntity.status(200).body(lhmReturn);
    }

    @RequestMapping(path="/Records/{id}", method=RequestMethod.GET)
    public  ResponseEntity<Object> getGameById(@PathVariable int id){
        GameOwnerRecord record = gameOwnerRecordJpaRepository.findById(id).get();

        if (record == null){
            mapMessage.put("RecordNotFound", "No Record Found by that Id");
            return ResponseEntity.status(404).body(getReturnMap());
        }
        return ResponseEntity.status(200).body(record.toMap());
        
    }
    
    
    @RequestMapping(path="/Records/{id}", method=RequestMethod.PUT)
    public ResponseEntity<Object> updateGameOwnerRecord(@PathVariable int id, @RequestHeader("Authorization") String auth, @RequestBody LinkedHashMap<String, String> input){
        String[] creds = decriptCreds(auth);
        String[] userInput = new String[2];
        Person owner = personJpaRepository.findByCreds(creds[0], creds[1]).get(0);

        userInput[0] = input.get("condition");
        userInput[1] = input.get("system");

        GameOwnerRecord record = null;

        try {
            record = gameOwnerRecordJpaRepository.findByGameOwnerRecordIdAndOwnerPersonId(id, owner.getPersonId()).get(0);
            }catch  (IndexOutOfBoundsException e){
                mapMessage.put("RecordNotFound", "No Record Found For That Id");
                return ResponseEntity.status(404).body(getReturnMap());
            }

        for (int i = 0; i < userInput.length; i++){
            if (userInput[i] == null){
                mapMessage.put("MissingInformation", "Missing Game Information. Required: condition, system");
                return ResponseEntity.status(400).body(getReturnMap());
            }
        }

        Condition condition = getConditionById(userInput[0].toLowerCase());
        GameSystem system = getGameSystemById(userInput[1]);

        if (condition == null || system == null){
            mapMessage.put("RecordsNotFound","Records Not Found. See Error Output");
            return ResponseEntity.status(404).body(getReturnMap());
        }

        System.out.println(record.toFullString());

        record.setGameSystem(system);
        record.setCondition(condition);

        try {
            gameOwnerRecordJpaRepository.save(record);
            mapMessage.put("UpdateSuccessful", "Record Update Successfull");
            return ResponseEntity.status(204).body(getReturnMap());

        } catch (Exception e){
            System.out.println(e.getCause());
            mapMessage.put("UnexpectedException", "System Has Encountered an Unexpexted Issue");
            return ResponseEntity.status(500).body(getReturnMap());
        }
    }

    @RequestMapping(path="/Records/{id}", method=RequestMethod.PATCH)
    public ResponseEntity<Object> partialRecordsUpdate(@PathVariable int id, @RequestHeader("Authorization") String auth, @RequestBody LinkedHashMap<String, String> input){
        String[] creds = decriptCreds(auth);

        GameOwnerRecord record = null;

        Person owner = personJpaRepository.findByCreds(creds[0], creds[1]).get(0);
        try {
        record = gameOwnerRecordJpaRepository.findByGameOwnerRecordIdAndOwnerPersonId(id, owner.getPersonId()).get(0);
        }catch  (IndexOutOfBoundsException e){
            mapMessage.put("RecordNotFound", "No Record Found For That Id");
            return ResponseEntity.status(404).body(getReturnMap());
        }

        Condition condition = null;
        GameSystem gameSystem = null;

        if (input.get("condition") != null){
            condition = getConditionById(input.get("condition"));
            if (condition == null){
                mapMessage.put("RecordsNotFound","Records Not Found. See Error Output");
                return ResponseEntity.status(404).body(getReturnMap());
            } else {
                record.setCondition(condition);
            }
        }

        if (input.get("system") != null){
            gameSystem = getGameSystemById(input.get("system"));
            if (gameSystem == null){
                mapMessage.put("RecordsNotFound","Records Not Found. See Error Output");
                return ResponseEntity.status(404).body(getReturnMap());
            } else {
                record.setGameSystem(gameSystem);
            }
        }

        if (condition == null && gameSystem == null){
            mapMessage.put("NoInputFound","Please Input System or Condition Update");
            return ResponseEntity.status(400).body(getReturnMap());
        } 
        

        try {
            gameOwnerRecordJpaRepository.save(record);
            mapMessage.put("UpdateSuccessful", "Record Update Successfull");
            return ResponseEntity.status(204).body(getReturnMap());

        } catch (Exception e){
            System.out.println(e.getCause());
            mapMessage.put("UnexpectedException", "System Has Encountered an Unexpexted Issue");
            return ResponseEntity.status(500).body(getReturnMap());
        }
    }
    
    @RequestMapping(path="/Records/{id}", method=RequestMethod.DELETE)
    public ResponseEntity<Object> deleteRecord(@PathVariable int id, @RequestHeader("Authorization") String auth){
        String[] creds = decriptCreds(auth);
        Person owner = personJpaRepository.findByCreds(creds[0], creds[1]).get(0);
        GameOwnerRecord record = null;
        try {
            record = gameOwnerRecordJpaRepository.findByGameOwnerRecordIdAndOwnerPersonId(id, owner.getPersonId()).get(0);
        } catch (IndexOutOfBoundsException e){
            mapMessage.put("RecordNotFound", "No Record Found For That Id");
            return ResponseEntity.status(404).body(getReturnMap());
        }
        gameOwnerRecordJpaRepository.delete(record);
        mapMessage.put("DeleteSuccesful", "Record Deleted");
        return ResponseEntity.status(204).body(getReturnMap());
    }

    //@RequestMapping(path="/{id}", method=RequestMethod.GET)
    //public ResponseEntity<Object> findGameById(@PathVariable int id){
    //    Game game = gameJpaRepository.findById(id).get();
    //    if (game == null){
    //        mapMessage.put("RecordNotFound", "No Record Found by that Id");
    //        return ResponseEntity.status(404).body(getReturnMap());
    //    }
    //    return ResponseEntity.status(200).body(game.toMap());
    //}

    @RequestMapping(path="/Publisher", method=RequestMethod.POST)
    public ResponseEntity<Object> addPublisher(@RequestBody HashMap<String, String> input){
        String publisherName = input.get("publisherName");

        if (publisherName == null){
            mapMessage.put("InvalidInput", "Please Enter A Publisher Name");
            return ResponseEntity.status(400).body(getReturnMap());
        }

        Publisher publisher = new Publisher( publisherName);

        try {
            publisherJpaRepository.save(publisher);
            mapMessage.put("InputSuccessful", "Publisher Successfully Created");
            return ResponseEntity.status(201).body(getReturnMap());
        } catch (Exception e){
            System.out.println("Error Adding Publisher: " + e.getMessage());
            mapMessage.put("Unexpexted Error", "Server Has Enocuntered an unexpected error");
            return ResponseEntity.status(500).body(getReturnMap());
        }
    }

    @RequestMapping(path="/Publishers", method=RequestMethod.GET)
    public ResponseEntity<Object> getPublishers(){
        return ResponseEntity.status(200).body(publisherJpaRepository.findAll());
    }

    @RequestMapping(path="/Publisher/{id}", method=RequestMethod.GET)
    public ResponseEntity<Object> getPublishers(@PathVariable("id") int id){
        try {
            Publisher publisher = publisherJpaRepository.findById(id).get();
            return ResponseEntity.status(200).body(publisher);
        } catch (IndexOutOfBoundsException e){
            mapMessage.put("PublisherNotFound", "No Publisher Found");
            return ResponseEntity.status(404).body(getReturnMap());
        }
    }

    @RequestMapping(path="/System", method=RequestMethod.POST)
    public ResponseEntity<Object> addSystem(@RequestBody HashMap<String, String> input){
        String systemName = input.get("systemName");
        if (systemName == null){
            mapMessage.put("InvalidInput", "Please Enter a System Name");
            return ResponseEntity.status(400).body(getReturnMap());
        }

        GameSystem system = new GameSystem(systemName);

        try {
            gameSystemJpaRepository.save(system);
            mapMessage.put("InputSuccessful", "System Succesfully Created");
            return ResponseEntity.status(201).body(getReturnMap());
        } catch (Exception e) {
            System.out.println("Error Adding System: " + e.getMessage());
            mapMessage.put("Unexpexted Error", "Server Has Enocuntered an unexpected error");
            return ResponseEntity.status(500).body(getReturnMap());
        }
    }

    @RequestMapping(path="/Systems", method=RequestMethod.GET)
    public ResponseEntity<Object> getSmaeSystems(){
        return ResponseEntity.status(200).body(gameSystemJpaRepository.findAll());
    }

    @RequestMapping(path="/System/{id}", method=RequestMethod.GET)
    public ResponseEntity<Object> getGameSystems(@PathVariable("id") int id){
        try {
            GameSystem gameSystems = gameSystemJpaRepository.findById(id).get();
            return ResponseEntity.status(200).body(gameSystems);
        } catch (IndexOutOfBoundsException e){
            mapMessage.put("SystemNotFound", "No Game System Found");
            return ResponseEntity.status(404).body(getReturnMap());
        }
    }

    @RequestMapping(path="/Conditions", method=RequestMethod.GET)
    public ResponseEntity<Object> getCondition(){
        return ResponseEntity.status(200).body(conditionJpaRepository.findAll());
    }

    @RequestMapping(path="/Condition/{id}", method=RequestMethod.GET)
    public ResponseEntity<Object> getCondition(@PathVariable("id") int id){
        try {
            Condition condition = conditionJpaRepository.findById(id).get();
            return ResponseEntity.status(200).body(condition);
        } catch (IndexOutOfBoundsException e){
            mapMessage.put("SystemNotFound", "No Condition Found");
            return ResponseEntity.status(404).body(getReturnMap());
        }
    }


    @RequestMapping(path="/Game", method=RequestMethod.POST)
    public ResponseEntity<Object> addGame(@RequestBody HashMap<String, String> input){
        String title = input.get("title");
        String publisherId = input.get("publisherId");

        if (title == null){
            mapMessage.put("InvalidInput", "Please Enter A Game Title");
            return ResponseEntity.status(400).body(getReturnMap());
        }

        if (publisherId == null){
            mapMessage.put("InvalidInput", "Please Enter a Publisher Id");
            return ResponseEntity.status(400).body(getReturnMap());
        }

    
        Publisher publisher = null;

        try {
            publisher = publisherJpaRepository.findById(Integer.parseInt(publisherId)).get();
        } catch (IndexOutOfBoundsException e){
            mapMessage.put("PublisherNotFound", "Publisher Not Found");
            return ResponseEntity.status(404).body(getReturnMap());
        } catch (NumberFormatException e){
            mapMessage.put("InvalidInput", "Please Enter a Valid Publisher Id");
            return ResponseEntity.status(400).body(getReturnMap());
        }

        try {
            publisherJpaRepository.save(publisher);
            mapMessage.put("InputSuccessful", "Publisher Successfully Created");
            return ResponseEntity.status(201).body(getReturnMap());
        } catch (Exception e){
            System.out.println("Error Adding Publisher: " + e.getMessage());
            mapMessage.put("Unexpexted Error", "Server Has Enocuntered an unexpected error");
            return ResponseEntity.status(500).body(getReturnMap());
        }
    }

    @RequestMapping(path="/Games", method=RequestMethod.GET)
    public ResponseEntity<Object> getGames(){
        return ResponseEntity.status(200).body(gameJpaRepository.findAll());
    }

    @RequestMapping(path="/Games/{id}", method=RequestMethod.GET)
    public ResponseEntity<Object> getGames(@PathVariable("id") int id){
        try {
            Game game = gameJpaRepository.findById(id).get();
            return ResponseEntity.status(200).body(game);
        } catch (IndexOutOfBoundsException e){
            mapMessage.put("GmaNotFound", "No Game Found");
            return ResponseEntity.status(404).body(getReturnMap());
        }
    }



    public Condition getConditionById(String conditionId){
        try {
            switch (Integer.parseInt(conditionId)) {
                case 1:
                    return new Condition(1, "MINT");
                case 2:
                    return new Condition(2, "GOOD");
                case 3:
                    return new Condition(3, "FAIR");
                case 4:
                    return new Condition(4, "POOR");
                default:
                    mapMessage.put("ConditionError","Invalid Condition Inputted");
                    return null;
            }
        } catch (NumberFormatException e){
            mapMessage.put("InvalidInput", "Please Enter a Valid ConditionId Id");
            return null;
        } catch (Exception e){
            mapMessage.put("ConditionError","Invalid Condition Inputted");
            System.out.println(e.getMessage());
            return null;
        }
    }

    public Game getGameById(String gameId){
        try {
            Game game = gameJpaRepository.findById(Integer.parseInt(gameId)).get();
            if (game == null){
                mapMessage.put("GameDoesNotExist", "No Game Exists by That Title");
                return null;
            }
            return game;
        } catch (IndexOutOfBoundsException e){
            mapMessage.put("GameDoesNotExist", "No Game Exists by That Title");
            return null;
        }catch (NumberFormatException e){
            mapMessage.put("InvalidInput", "Please Enter a Valid Game Id");
            return null;
        }
    }

    public GameSystem getGameSystemById(String systemId){
        try{
            GameSystem gameSystem = gameSystemJpaRepository.findById(Integer.parseInt(systemId)).get();
            if (gameSystem == null){
            mapMessage.put("GameSystemDoesNotExist", "System Does Not Exist");
            return null;
            }
            return gameSystem;
        } catch (IndexOutOfBoundsException e){
            mapMessage.put("GameSystemDoesNotExist", "System Does Not Exist");
            return null;
        } catch (NumberFormatException e){
            mapMessage.put("InvalidInput", "Please Enter a Valid Game System Id");
            return null;
        }

    }

    public GameOwnerRecord getGORByIdAndOwner(int Id, int ownerId){
        try{
            GameOwnerRecord record = gameOwnerRecordJpaRepository.findByGameOwnerRecordIdAndOwnerPersonId(Id, ownerId).get(0);
            return record;
        } catch (IndexOutOfBoundsException e){
            mapMessage.put("GameOwnerRecordNotFound", "No Game Owner Record Exists By That Id for Current User");
            return null;
        }
    }



}
