package lib;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

public class DateGenerator extends BaseTestCase{
    public static String getRandomEmail(){
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date());
        return "learnqa" + timeStamp +"@example.com";

    }
    public static Map<String,String> getRegistrationData(){
        Map<String,String>data = new HashMap<>();
        data.put("email",DateGenerator.getRandomEmail());
        data.put("password","123");
        data.put("username","learnqa");
        data.put("firstName","learnqa");
        data.put("lastName","learnqa");
        return data;
    }
    public static Map<String,String> getRegistrationData(Map<String,String>notDefaultValues){
        Map<String,String>defaultValues = DateGenerator.getRegistrationData();
        Map<String,String> userData = new HashMap<>();
     //   String[]keys = {"email","password","username","firstName","lastName"}; // moved it into BaseTestCase
        for(String key :keys){
            if(notDefaultValues.containsKey(key)){
                userData.put(key,notDefaultValues.get(key));
            }else{
                userData.put(key,defaultValues.get(key));
            }
        }

        return userData;
    }
}
