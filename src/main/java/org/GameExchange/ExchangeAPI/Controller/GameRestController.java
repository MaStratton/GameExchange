package org.GameExchange.ExchangeAPI.Controllers;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.GameExchange.ExchangeAPI.Models.Game;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
public class GameRestController {

    @RequestMapping(path="/game", method=RequestMethod.POST)
    String[] addGame(@RequestBody Game game){
        String[] strReturn =  {"Hello"};
        return strReturn;
    }
    

    // @RequestMapping(path="/game", method=RequestMethod.GET)
    // Map<String, String> getGame(){
    //     Map mapReturn = new Map<String, String>();
    // }
    
}
