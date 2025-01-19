package org.GameExchange.ExchangeAPI.Controller;

import org.GameExchange.ExchangeAPI.Model.Address;
import org.GameExchange.ExchangeAPI.Model.AddressJpaRepository;
import org.GameExchange.ExchangeAPI.Model.Person;
import org.GameExchange.ExchangeAPI.Model.PersonJpaRepository;

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

    @Autowired
    private AddressJpaRepository addressJpaRepository;

    @Autowired
    private PersonJpaRepository personJpaRepository;

    @RequestMapping(path="/Register", method=RequestMethod.POST)
    public Map<String, String> registerUser(@RequestBody Person person){
        Map<String, String> mapReturn = new HashMap<>();
        if(!personJpaRepository.checkUserExist(person.getEmailAddr())){

        } else {
            mapReturn.put("Messege", "Email already used");
        }
        return mapReturn;

    }

    @RequestMapping(path="/AddAddress", method=RequestMethod.POST)
    public Address addAddress(@RequestBody Address address){
        //addressJpaRepository.save(address);
        System.out.println(address);
        //System.out.println(addressJpaRepository.findById(1).get());
        return addressJpaRepository.findById(1).get();

    }
    
}
