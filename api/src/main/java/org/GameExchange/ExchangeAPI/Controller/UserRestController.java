package org.GameExchange.ExchangeAPI.Controller;

import org.GameExchange.ExchangeAPI.Model.Address;
import org.GameExchange.ExchangeAPI.Model.AddressJpaRepository;
import org.GameExchange.ExchangeAPI.Model.City;
import org.GameExchange.ExchangeAPI.Model.CityJpaRepository;
import org.GameExchange.ExchangeAPI.Model.Person;
import org.GameExchange.ExchangeAPI.Model.State;
import org.GameExchange.ExchangeAPI.Model.StateJpaRepository;
import org.GameExchange.ExchangeAPI.Model.Zip;
import org.GameExchange.ExchangeAPI.Model.ZipJpaRepository;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.RestController;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@RestController
@RequestMapping("/User")
public class UserRestController extends ApplicationRestController{


    @Autowired
    private AddressJpaRepository addressJpaRepository;

    @Autowired
    private CityJpaRepository cityJpaRepository;

    @Autowired
    private StateJpaRepository stateJpaRepository;

    @Autowired
    private ZipJpaRepository zipJpaRepository;

    @RequestMapping(path="/Register", method=RequestMethod.POST)
    public ResponseEntity<Object> registerUser(@RequestBody Map<String,String> input){

        String[] userInfo = new String[4];
        userInfo[0] = input.get("firstName");
        userInfo[1] = input.get("lastName");
        userInfo[2] = input.get("emailAddr");
        userInfo[3] = input.get("password");

        for (int i = 0; i < userInfo.length; i++){
            if ((userInfo[i] == null || userInfo[i].isBlank()) && i != 1){
                mapMessage.put("MissingUserInformation", "Missing User Information");    
                return ResponseEntity.status(400).body(getReturnMap());
            }
        }



        if (personJpaRepository.checkUserExist(input.get("emailAddr"))){
            mapMessage.put("UserExists", "User already exists with indicated email");
            return ResponseEntity.status(400).body(getReturnMap());
        }

        String[] addressInfo = new String[] {
            input.get("addressLine1"),
            input.get("addressLine2"),
            input.get("cityName"),
            input.get("stateAbbr"),
            input.get("zipCode")};
            
        Address address = getAddress(addressInfo);

        if (address == null){
            address = addAddressRecord(addressInfo);
            if (address == null){
                return ResponseEntity.status(500).body(getReturnMap());
            }
        }
        try {
            Person person = new Person(userInfo[0], userInfo[1], userInfo[2], userInfo[3], address);
            personJpaRepository.save(person);
            mapMessage.put("Succes", "User Successfully added");
            return ResponseEntity.status(201).body(getReturnMap());
        } catch (Exception e){
            System.out.println(e.getMessage());
            return ResponseEntity.status(500).body(getReturnMap());
        }
        
    }

    @RequestMapping(path="/ChangePass", method=RequestMethod.PATCH)
    public ResponseEntity<Object> changePassword(@RequestHeader("Authorization") String authorization, @RequestBody LinkedHashMap<String,String> input){
        String creds[] = decriptCreds(authorization);
        Person person = personJpaRepository.findByCreds(creds[0], creds[1]).get(0);

        if (input.get("password") == null){
            mapMessage.put("NoPasswordInput", "Please enter a new Password");
            return ResponseEntity.status(400).body(getReturnMap());
        } else {
            person.setPassword(ProtectionController.hash(input.get("password")));
            personJpaRepository.save(person);
            mapMessage.put("UpdateSuccessful", "Password Successfully Changed");
            applicationKafkaProducer.sendPasswdChange(person.getPersonId());
            return ResponseEntity.status(204).body(getReturnMap());
        }
    
    }

    @RequestMapping(path="", method=RequestMethod.PUT)
    public ResponseEntity<Object> updateFullUser(@RequestHeader("Authorization") String auth, @RequestBody Map<String,String> input){
        String creds[] = decriptCreds(auth);
        String[] userInfo = new String[2];
        userInfo[0] = input.get("firstName");
        userInfo[1] = input.get("lastName");

        for (int i = 0; i < userInfo.length; i++){
            if ((userInfo[i] == null || userInfo[i].isBlank()) && i != 1){
                mapMessage.put("MissingUserInformation", "Missing User Information");    
                return ResponseEntity.status(400).body(getReturnMap());
            }
        }

        String[] addressInfo = new String[] {input.get("addressLine1"),
            input.get("addressLine2"),
            input.get("cityName"),
            input.get("stateAbbr"),
            input.get("zipCode")};
            
        Address address = getAddress(addressInfo);
        if (address == null){
            address = addAddressRecord(addressInfo);
            if (address == null){
                return ResponseEntity.status(500).body(getReturnMap());
            }
        }

        Person person = personJpaRepository.findByCreds(creds[0], creds[1]).get(0);

        person.setAddress(address);
        person.setFirstName(userInfo[0]);
        person.setFirstName(userInfo[1]);

        try{
            mapMessage.put("Succes", "User Updated added");
            return ResponseEntity.status(204).body(getReturnMap());
        } catch (Exception e){
            System.out.println(e.getMessage());
            return ResponseEntity.status(500).body(getReturnMap());
        }
        
    }
    
    @RequestMapping(path="", method=RequestMethod.PATCH)
    public ResponseEntity<Object> updatePartialUser(@RequestHeader("Authorization") String auth, @RequestBody Map<String,String> input){
        String creds[] = decriptCreds(auth);
        String[] userInfo = new String[2];
        userInfo[0] = input.get("firstName");
        userInfo[1] = input.get("lastName");


        String[] addressInfo = new String[] {input.get("addressLine1"),
            input.get("addressLine2"),
            input.get("cityName"),
            input.get("stateAbbr"),
            input.get("zipCode")};
            
        Address address = getAddress(addressInfo);
        if (address == null){
            address = addAddressRecord(addressInfo);
            if (address == null){
                return ResponseEntity.status(500).body(getReturnMap());
            }
        }

        Person person = personJpaRepository.findByCreds(creds[0], creds[1]).get(0);

        if (address != null){
            person.setAddress(address);
        }
        if (userInfo[0] != null){
            person.setFirstName(userInfo[0]);
        }
        if (userInfo[1] != null){
            person.setFirstName(userInfo[1]);
        }

        try{
            mapMessage.put("Succes", "User Updated added");
            return ResponseEntity.status(204).body(getReturnMap());
        } catch (Exception e){
            System.out.println(e.getMessage());
            return ResponseEntity.status(500).body(getReturnMap());
        }
        
    }

    Address getAddress(String[] input){
        Address address;
        try {
            if (input[1] == null){
                address = addressJpaRepository.getAddress(
                                input[0],
                                input[2],
                                input[3],
                                input[4]).get(0);
            } else {
                address = addressJpaRepository.getAddress(
                                input[0],
                                input[1],
                                input[2],
                                input[3],
                                input[4]).get(0);
            }
        } catch (IndexOutOfBoundsException e) {
            mapMessage.put("Address Record Not Found", "Creating New Record");
            return null;
        }
        return address;
    }

    public Address addAddressRecord(String[] addressInfo){
        for (int i = 0; i < addressInfo.length; i++){
            if (i != 1 && addressInfo[i] == null){
                mapMessage.put("MissingAddressInfoError", "Could Not Create New Recored: Missing Parts");
                return null;
            }
        } 

        City city;
        State state = null;
        Zip zip;

        try {
            city = cityJpaRepository.findByName(addressInfo[2]).get(0);
        } catch (IndexOutOfBoundsException e){
            mapMessage.put("CityNotFound","Adding Record");
            city = addCityRecord(addressInfo[2]);
        }

        try {
            state = stateJpaRepository.findByAbbr(addressInfo[3]).get(0);
        } catch (IndexOutOfBoundsException e){
            mapMessage.put("StateError", "No Valie State Abbreviation Found");
        }

        try{
            zip = zipJpaRepository.findByCode(addressInfo[4]).get(0);
        } catch (IndexOutOfBoundsException e){
            mapMessage.put("ZipNotFound", "Adding Reccord");
            zip = addZipRecord(addressInfo[4]);
        }

        if (city == null || state == null || zip == null){
            mapMessage.put("CityStateZipError", "Error with City, State, or Zip");
            return null;
        }

        Address address = new Address(addressInfo[0], addressInfo[1], state, city, zip);
        addressJpaRepository.save(address);
        if (addressInfo[1] == null || addressInfo[1].equals("")){
            address = addressJpaRepository.getAddress(addressInfo[0], addressInfo[2], addressInfo[3], addressInfo[4]).get(0);
        } else {
            address = addressJpaRepository.getAddress(addressInfo[0], addressInfo[1], addressInfo[2], addressInfo[3], addressInfo[4]).get(0);

        }
        return address;

    }

    City addCityRecord(String cityName){
        City city = new City(cityName);
        cityJpaRepository.save(city);
        return cityJpaRepository.findByName(cityName).get(0);
    }

    Zip addZipRecord(String zipCode){
        Zip zip = new Zip(zipCode);
        zipJpaRepository.save(zip);
        return zipJpaRepository.findByCode(zipCode).get(0);
    }


}
