package org.GameExchange.ExchangeAPI.Controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

import org.GameExchange.ExchangeAPI.Model.GameOwnerRecord;
import org.GameExchange.ExchangeAPI.Model.OfferDTO;
import org.GameExchange.ExchangeAPI.Model.OfferRecord;
import org.GameExchange.ExchangeAPI.Model.OfferRecordJpaRepository;
import org.GameExchange.ExchangeAPI.Model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(path="/Offer")
public class OfferRestController extends ApplicationRestController{

    @Autowired
    private OfferRecordJpaRepository offerRecordJpaRepository;

    @RequestMapping(path="", method=RequestMethod.POST)
    private ResponseEntity<Object> makeOffer(@RequestBody OfferDTO offer, @RequestHeader("Authorization") String auth){
        String[] creds = decriptCreds(auth);

        if (offer.getRequested() == null){
            mapMessage.put("NoGamesRequested", "Please Input Games Requested");
            return ResponseEntity.status(400).body(getReturnMap());
        }

        if (offer.getOffered() == null){
            mapMessage.put("NoGamesOffered", "Please Input Games Offered");
            return ResponseEntity.status(400).body(getReturnMap());
        }

        Person requestee = null;
        System.out.println(offer.getRequesteeId());
        try{
            requestee = personJpaRepository.findById(offer.getRequesteeId()).get();
        } catch (Exception e){
            System.out.println(e.getCause());
            mapMessage.put("UserDoesNotExist", "No User Exists by that Id");
            return ResponseEntity.status(404).body(getReturnMap());
        }

        Person requester = personJpaRepository.findByCreds(creds[0], creds[1]).get(0);

        if (requestee == null){
            mapMessage.put("UserDoesNotExist", "No User Exists by that Id");
            return ResponseEntity.status(404).body(getReturnMap());
        }

        if (requestee.getPersonId() == requester.getPersonId()){
            mapMessage.put("UserRequestingOwnGames", "Requestee and User are the same person");
            return ResponseEntity.status(400).body(getReturnMap());
        }
        
        GameOwnerRecord[] requested = checkOwnership(offer.getRequested(), requestee.getPersonId());
        GameOwnerRecord[] offered = checkOwnership(offer.getOffered(), requester.getPersonId());

        if (requested == null || offered == null){
            return ResponseEntity.status(400).body(getReturnMap());
        }

        OfferRecord record = new OfferRecord("pending");

        try{
            record = offerRecordJpaRepository.save(record);
            mapMessage.put("OfferRecordMade", "Offer Record Made");
        } catch (Exception e){
            System.out.println("Making New Offer Record Error: " +e.getMessage());
            mapMessage.put("UnexpectedException", "System Has Encountered an Unexpexted Issue");
            return ResponseEntity.status(500).body(getReturnMap());
        }

        try {
            for (GameOwnerRecord GOR: requested){
                GOR.setInOffer(true);
                GOR.setOfferRecord(record);
                GOR.setOfferSender(requester);
                gameOwnerRecordJpaRepository.save(GOR);
            }
            for (GameOwnerRecord GOR: offered){
                GOR.setInOffer(true);
                GOR.setOfferRecord(record);
                GOR.setOfferSender(requester);
                gameOwnerRecordJpaRepository.save(GOR);
            }
            return ResponseEntity.status(201).body(getReturnMap());
        } catch (Exception e){
            System.out.println("Updateing Games Error: " + e.getMessage());
            mapMessage.put("UnexpectedException", "System Has Encountered an Unexpexted Issue");
            return ResponseEntity.status(500).body(getReturnMap());
        }
    

    }

    @RequestMapping(path="", method=RequestMethod.GET)
    public ResponseEntity<Object> getRecords(@RequestHeader("Authorization") String auth, @RequestBody LinkedHashMap<String,String> input){
        String[] creds = decriptCreds(auth);
        Person owner = personJpaRepository.findByCreds(creds[0], creds[1]).get(0);

        String status = input.get("status");

        List<GameOwnerRecord> recordsInOffers = null;

        if (status != null) {
            recordsInOffers = gameOwnerRecordJpaRepository.findOffersByGameOwner(owner.getPersonId(), status);
        } else {
            recordsInOffers = gameOwnerRecordJpaRepository.findOffersByGameOwner(owner.getPersonId());;
        }

        if (recordsInOffers.size() == 0){
            mapMessage.put("NoOffers", "No Games In Offers For Current User");
            return ResponseEntity.status(200).body(getReturnMap());
        }

        LinkedHashMap<LinkedHashMap<String, Integer>, ArrayList<LinkedHashMap<String, String>>> offers = new LinkedHashMap<LinkedHashMap<String, Integer>, ArrayList<LinkedHashMap<String, String>>>();
        try{
            for (GameOwnerRecord GOR: recordsInOffers){
                LinkedHashMap<String, Integer> idMap = getOfferLinkedMap(GOR.getOfferRecord().getOfferRecordId());
                if (offers.containsKey(idMap)){
                    offers.get(idMap).add(getFullOfferMap(GOR));
                } else {
                    offers.put(getOfferLinkedMap(GOR.getOfferRecord().getOfferRecordId()), new ArrayList<LinkedHashMap<String, String>>());
                    offers.get(idMap).add(getFullOfferMap(GOR));
                }
            }
        } catch (Exception e){
            System.out.println("Faled Turning intop LHM of I ArrLst of LikedHashMap of Str Str" + e.getMessage());
            mapMessage.put("Unexpexted Error", "Server Has Enocuntered an unexpected error");
            return ResponseEntity.status(500).body(getReturnMap());
        }


        return ResponseEntity.status(200).body(offers);

    }

    public GameOwnerRecord[] checkOwnership(List<Integer> offer, int ownerId){
        GameOwnerRecord[] records = new GameOwnerRecord[offer.size()];
        for (int i = 0; i < offer.size(); i++){
            try{ 
                records[i] = gameOwnerRecordJpaRepository.findByGameOwnerRecordIdAndOwnerPersonId(offer.get(i), ownerId).get(0);
                if (records[i].isInOffer()){
                    mapMessage.put("RecordAlreadyPartOfOffer", "Game Owned By {localhost:8080/User/" + ownerId +"} Is Already Part of Another Offer. Game:" + records[i].getUri());
                    return null;
                }
            } catch (IndexOutOfBoundsException e){
                mapMessage.put("OfferedGameNotOwnedByUser", "One or More Games Offered Do Not Belong to You");
                return null;
            }
        }
        return records;
    }

    public LinkedHashMap<String, String> getFullOfferMap(GameOwnerRecord GOR){
        LinkedHashMap<String, String> mapReturn = GOR.toMap();
        mapReturn.put("Status", GOR.getOfferRecord().getOfferStatus());
        mapReturn.put("Creation Time", GOR.getOfferRecord().getCreationTime().toString());
        mapReturn.put("Offer Creator", GOR.getOfferSender().getUri());
        return mapReturn;

    }

    public LinkedHashMap<String, Integer> getOfferLinkedMap(int offerRecordId){
        LinkedHashMap<String, Integer> mapReturn = new LinkedHashMap<String, Integer>();
        mapReturn.put("Offer Id", offerRecordId);
        return mapReturn;
    }


    
}
