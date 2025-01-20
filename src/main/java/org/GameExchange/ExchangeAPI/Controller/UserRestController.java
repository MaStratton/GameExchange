package org.GameExchange.ExchangeAPI.Controller;

import org.GameExchange.ExchangeAPI.Model.Address;
import org.GameExchange.ExchangeAPI.Model.AddressJpaRepository;
import org.GameExchange.ExchangeAPI.Model.City;
import org.GameExchange.ExchangeAPI.Model.CityJpaRepository;
import org.GameExchange.ExchangeAPI.Model.Person;
import org.GameExchange.ExchangeAPI.Model.PersonJpaRepository;
import org.GameExchange.ExchangeAPI.Model.State;
import org.GameExchange.ExchangeAPI.Model.StateJpaRepository;
import org.GameExchange.ExchangeAPI.Model.Zip;
import org.GameExchange.ExchangeAPI.Model.ZipJpaRepository;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/User")
public class UserRestController {

    Map<String, String> mapMessage = new HashMap<String, String>();

    @Autowired
    private AddressJpaRepository addressJpaRepository;

    @Autowired
    private PersonJpaRepository personJpaRepository;

    @Autowired
    private CityJpaRepository cityJpaRepository;

    @Autowired
    private StateJpaRepository stateJpaRepository;

    @Autowired
    private ZipJpaRepository zipJpaRepository;

    @RequestMapping(path="/Register", method=RequestMethod.POST)
    public Map<String, String> registerUser(@RequestBody Map<String,String> input){
        System.out.println(input);

        do{
            String[] userInfo = {input.get("firstName"), input.get("lastName"), input.get("emailAddr"), input.get("password")};

            boolean containsNullOrEmpty = false;
            for (int i = 0; i < userInfo.length; i++){
                if ((userInfo[i] == null || userInfo[i].equals(""))&& i != 1){
                    mapMessage.put("MissingUserInformation", "Missing User Information");    
                    containsNullOrEmpty = true;
                    break;
                }
            }

            if (containsNullOrEmpty)
                break;

            if (personJpaRepository.checkUserExist(input.get("emailAddr"))){
                mapMessage.put("UserExists", "User already exists with indicated email");
                break;
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
                    //mapMessage.put("AddressError", "MissingMissingAddressParts");
                    break;
                }
            }

            try {
                System.out.println(userInfo[3]);
                Person person = new Person(userInfo[0], userInfo[1], userInfo[2], ProtectionController.hash(userInfo[3]), address);
                personJpaRepository.save(person);
                mapMessage.put("Succes", "User Successfully added");
            } catch (Exception e){
                System.out.println(e.getMessage());

            }


        } while (false);

        Map<String, String> mapReturn = new HashMap<String, String>();
        mapReturn.putAll(mapMessage);
        mapMessage.clear();

        System.out.println(mapReturn);
        return mapReturn;
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
                System.out.println(i + " " + addressInfo[i]);
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
