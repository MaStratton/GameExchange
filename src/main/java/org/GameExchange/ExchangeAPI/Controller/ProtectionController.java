package org.iamliterallyhim.OSWars.Controller;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


import java.math.BigInteger; 
import java.security.MessageDigest; 
import java.security.NoSuchAlgorithmException; 


public class ProtectionController {
    final static String emailRegex = "^((?!\\.)[\\w\\-_.]*[^.])(@\\w+)(\\.\\w+(\\.\\w+)?[^.\\W])$";



    public static Boolean chkEmail(String email){
        Pattern p = Pattern.compile(emailRegex);
        Matcher m = p.matcher(email);

        return m.find();

    }

    public static String hash(String passwd){
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            byte[] messageDigest = md.digest(passwd.getBytes()); 
            BigInteger no = new BigInteger(1, messageDigest); 
            String hashtext = no.toString(16); 
            while (hashtext.length() < 32) { 
                hashtext = "0" + hashtext; 
            } 
            return hashtext; 
        } catch (NoSuchAlgorithmException e){
            throw new RuntimeException(e);
        }

    }
    
}
