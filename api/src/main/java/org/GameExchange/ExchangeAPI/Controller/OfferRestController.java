package org.GameExchange.ExchangeAPI.Controller;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.GameExchange.ExchangeAPI.Model.GameOwnerRecord;
import org.GameExchange.ExchangeAPI.Model.OfferDTO;
import org.GameExchange.ExchangeAPI.Model.OfferRecord;
import org.GameExchange.ExchangeAPI.Model.OfferRecordJpaRepository;
import org.GameExchange.ExchangeAPI.Model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.method.P;
import org.springframework.web.bind.annotation.PathVariable;
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
        try{
            requestee = personJpaRepository.findById(offer.getRequesteeId()).get();
        } catch (Exception e){
            System.out.println(e.getCause());
            mapMessage.put("UserDoesNotExist", "No User Exists by that Id");
            return ResponseEntity.status(404).body(getReturnMap());
        }

        Person requester = personJpaRepository.findByCreds(creds[0], creds[1]).get(0);
        System.out.println(requester);

        if (requestee == null){
            mapMessage.put("UserDoesNotExist", "No User Exists by that Id");
            return ResponseEntity.status(404).body(getReturnMap());
        }

        if (requestee.getPersonId() == requester.getPersonId()){
            mapMessage.put("UserRequestingOwnGames", "Requestee and User are the same person");
            return ResponseEntity.status(400).body(getReturnMap());
        }
        System.out.println("HOW");
        System.out.println("Requested: " + offer.getRequested());
        System.out.println("Offered: " + offer.getOffered());
        System.out.println("RequesteeId: " + offer.getRequesteeId());
        GameOwnerRecord[] requested = checkOwnership(offer.getRequested(), requestee.getPersonId());
        System.out.println("why");
        GameOwnerRecord[] offered = checkOwnership(offer.getOffered(), requester.getPersonId());
        System.out.println("TF");

        if (requested == null || offered == null){
            return ResponseEntity.status(400).body(getReturnMap());
        }

        OfferRecord record = new OfferRecord("pending");

        try{
            record = offerRecordJpaRepository.save(record);
            applicationKafkaProducer.sendOfferCreated(requestee.getPersonId(), requestee.getPersonId());
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
            applicationKafkaProducer.sendOfferCreated(requester.getPersonId(), requestee.getPersonId());
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
        String by = input.get("by");
        if (by == null) by = "all";
        status = status.toLowerCase();

        if (!(status.equalsIgnoreCase("pending") || status.equalsIgnoreCase("accepted") || status.equalsIgnoreCase("rejected"))){
            status = null;
        }

        List<GameOwnerRecord> recordsInOffers = null;

        if (status != null) {
            switch (by){
                case "myself":
                    recordsInOffers = gameOwnerRecordJpaRepository.findOffersByGameOwnerSent(owner.getPersonId(), status);
                    break;
                case "other":
                    recordsInOffers = gameOwnerRecordJpaRepository.findOffersByGameOwnerReceived(owner.getPersonId(), status);
                    break;
                default:
                    recordsInOffers = gameOwnerRecordJpaRepository.findOffersByGameOwner(owner.getPersonId(), status);
            }
        } else {
            switch (by){
            case "myself":
                    recordsInOffers = gameOwnerRecordJpaRepository.findOffersByGameOwnerSent(owner.getPersonId());
                    break;
                case "other":
                    recordsInOffers = gameOwnerRecordJpaRepository.findOffersByGameOwnerReceived(owner.getPersonId());
                    break;
                default:
                    recordsInOffers = gameOwnerRecordJpaRepository.findOffersByGameOwner(owner.getPersonId());
            }
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
        System.out.println(offer);
        System.out.println(ownerId);
        for (int i = 0; i < offer.size(); i++){
            System.out.println(i);
            try{ 
                System.out.println("About to check ownership if " + offer.get(i) + "If owned by " + ownerId);
                records[i] = gameOwnerRecordJpaRepository.findByRecordIdAndOwnerId(offer.get(i), ownerId);
                System.out.println(i + "=" + records[i]);
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

    @RequestMapping(path="/{id}", method=RequestMethod.DELETE)
    public ResponseEntity<Object> deleteOffer(@RequestHeader("Authorization") String auth, @PathVariable("id") int id){
        String[] creds = decriptCreds(auth);
        Person sender = personJpaRepository.findByCreds(creds[0], creds[1]).get(0);

        List<GameOwnerRecord> recordsInOffers = gameOwnerRecordJpaRepository.findOfferBySender(sender.getPersonId());
    

        if (recordsInOffers == null){
            mapMessage.put("NoOfferFound", " No Offer Found By That Id");
            return ResponseEntity.status(404).body(getReturnMap());
        }

        Person requestee = gameOwnerRecordJpaRepository.findPersonByOfferIdAndOtherPersonId(id, sender.getPersonId());
        System.out.println(requestee);

        for (GameOwnerRecord GOR: recordsInOffers){
            try {
                GOR.setInOffer(false);
                GOR.setOfferRecord(null);
                GOR.setOfferSender(null);
                gameOwnerRecordJpaRepository.save(GOR);
            } catch (Exception e){
                System.out.println("Error On updating all recoreds: " + e.getMessage());
                mapMessage.put("Unexpexted Error", "Server Has Enocuntered an unexpected error");
                return ResponseEntity.status(500).body(getReturnMap());
            }
        }
        applicationKafkaProducer.sendOfferUpdated(sender.getPersonId(), requestee.getPersonId(), "deleted");
        mapMessage.put("DeleteSuccessful","Offer Record Deleted");
        return ResponseEntity.status(200).body(getReturnMap());
    }

    @RequestMapping(path="/{id}/{decision}", method=RequestMethod.PATCH)
    public ResponseEntity<Object> decideOffer(@RequestHeader("Authorization") String auth, @PathVariable("id") int id, @PathVariable("decision") String decision){
        String[] creds = decriptCreds(auth);
        Person owner = personJpaRepository.findByCreds(creds[0], creds[1]).get(0);

        switch (decision) {
            case "accept":
            case "reject":
            break;
            default:{
                mapMessage.put("InvalidDecision", "Invalid Decision. Valid: accept, reject");
                return ResponseEntity.status(400).body(getReturnMap());
            }
        }
        List<GameOwnerRecord> recordsInOffers = gameOwnerRecordJpaRepository.findOfferByOwnerAndOfferId(owner.getPersonId(), id);

        if (recordsInOffers == null){
            mapMessage.put("NoOfferFound", " No Offer Found By That Id");
            return ResponseEntity.status(404).body(getReturnMap());
        }

        Person sender = gameOwnerRecordJpaRepository.findPersonByOfferIdAndOtherPersonId(id, owner.getPersonId());

        try {
            OfferRecord offerRecord = recordsInOffers.get(0).getOfferRecord();
            offerRecord.setOfferStatus(decision);
            offerRecordJpaRepository.save(offerRecord);
            applicationKafkaProducer.sendOfferUpdated(sender.getPersonId(), owner.getPersonId(), "deleted");
            mapMessage.put("Updated", "Record Updated");
            return ResponseEntity.status(204).body(getReturnMap());
        } catch (Exception e){
            System.out.println("Exception updating Offer Record: " + e.getMessage());
            mapMessage.put("Unexpexted Error", "Server Has Enocuntered an unexpected error");
            return ResponseEntity.status(500).body(getReturnMap());
        }

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
