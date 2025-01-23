package org.GameExchange.ExchangeAPI.Controller;

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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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
        requestee = personJpaRepository.findById(offer.getRequesteeId()).get();

        Person requester = personJpaRepository.findByCreds(creds[0], creds[1]).get(0);



        if (requestee == null){
            mapMessage.put("UserDoesNotExist", "No User Exists by that Id");
            return ResponseEntity.status(404).body(getReturnMap());
        }

        if (requestee.getPersonId() == requester.getPersonId()){
            mapMessage.put("UserRequestingOwnGames", "Requestee and User are the same person");
            return ResponseEntity.status(400).body(getReturnMap());
        }
        
        GameOwnerRecord[] requested = new GameOwnerRecord[offer.getRequested().size()];
        GameOwnerRecord[] offered = new GameOwnerRecord[offer.getOffered().size()];

        for (int i = 0; i < offer.getOffered().size(); i++){
            try{ 
                offered[i] = gameOwnerRecordJpaRepository.findByGameOwnerRecordIdAndOwnerPersonId(offer.getOffered().get(i),requester.getPersonId()).get(0);
                if (offered[i].isInOffer()){
                    mapMessage.put("RecordAlreadyPartOfOffer", "Game Owned By You Is Already Part of Another Offer. Game:" + offered[i].getUri());
                    return ResponseEntity.status(400).body(getReturnMap());
                }
            } catch (IndexOutOfBoundsException e){
                mapMessage.put("OfferedGameNotOwnedByUser", "One or More Games Offered Do Not Belong to You");
                return ResponseEntity.status(400).body(getReturnMap());
            }
        }

        for (int i = 0; i < offer.getRequested().size(); i++){
            try {
                requested[i] = gameOwnerRecordJpaRepository.findByGameOwnerRecordIdAndOwnerPersonId(offer.getRequested().get(i), offer.getRequesteeId()).get(0);
                if (requested[i].isInOffer()){
                    mapMessage.put("RecordAlreadyPartOfOffer", "Game Owned Requestee You Is Already Part of Another Offer. Game:" + requested[i].getUri());
                    return ResponseEntity.status(400).body(getReturnMap());
                }
            } catch (IndexOutOfBoundsException e){
                System.out.println(e.toString());
                mapMessage.put("RequestedGamesNotOwnedBySameUser", "One or More Games Requested Do Not Belong to Specified User");
                return ResponseEntity.status(400).body(getReturnMap());
            }
        }

        OfferRecord record = new OfferRecord();

        try{
        record = offerRecordJpaRepository.save(record);
        mapMessage.put("OfferRecordMade", "Offer Record Made");
        } catch (Exception e){
            System.out.println(e.getCause());
            mapMessage.put("UnexpectedException", "System Has Encountered an Unexpexted Issue");
            return ResponseEntity.status(500).body(getReturnMap());
        }

        try {
            for (GameOwnerRecord GOR: requested){
                System.out.println("Requested" + GOR.toFullString());
                GOR.setInOffer(true);
                GOR.setOfferRecord(record);
                GOR.setOfferSender(requester);
                gameOwnerRecordJpaRepository.save(GOR);
            }
            for (GameOwnerRecord GOR: offered){
                System.out.println("Offered" + GOR.toFullString());
                GOR.setInOffer(true);
                GOR.setOfferRecord(record);
                GOR.setOfferSender(requester);
                gameOwnerRecordJpaRepository.save(GOR);
            }
            return ResponseEntity.status(201).body(getReturnMap());
        } catch (Exception e){
            System.out.println(e.getCause());
            mapMessage.put("UnexpectedException", "System Has Encountered an Unexpexted Issue");
            return ResponseEntity.status(500).body(getReturnMap());
        }
    

    }


    
}
