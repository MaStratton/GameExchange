package org.GameExchange.ExchangeAPI.Controller;

import org.GameExchange.ExchangeAPI.Model.Person;
import org.GameExchange.ExchangeAPI.Model.PersonJpaRepository;

import java.util.HashMap;
import java.util.List;
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
    private PersonJpaRepository jpaRepository;

    @RequestMapping(path="/Register", method=RequestMethod.POST)
    public Map<String, String> registerUser(@RequestBody Person person){
        if (!jpaRepository.checkUserExist(person.getEmailAddr())){
            System.out.println("Does Not exist");
            List<Person> people = jpaRepository.findAll();
            for (Person human: people){
                System.out.println(human.toString());
            }
        }
        Map<String, String> mapReturn = new HashMap<>();

        return mapReturn;
    }
    
}
