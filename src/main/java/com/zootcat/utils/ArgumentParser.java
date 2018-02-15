package com.zootcat.utils;

import java.util.HashMap;
import java.util.Map;

import com.zootcat.exceptions.RuntimeZootException;

public class ArgumentParser
{        
    public static Map<String, Object> parse(String[] arguments) throws RuntimeZootException
    {
        Map<String, Object> result = new HashMap<String, Object>();
        
        if(arguments.length == 1 && arguments[0].length() == 0)
        {
        	return result;
        }
        
        for(String arg : arguments)
        {
            if(arg.trim().isEmpty())
            {
            	continue;
            }
        	
        	String[] splittedArg = arg.split("="); 
        	if(splittedArg.length != 2)
        	{
        		throw new RuntimeZootException("Invalid argument: " + arg);
        	}
        	
        	String argName = splittedArg[0].trim();
        	String argValue = splittedArg[1].trim();
            
            if(argName.isEmpty())
            {
                throw new RuntimeZootException("Empty argument name!");
            }
            
            if(argValue.isEmpty())
            {
            	throw new RuntimeZootException("Empty argument value for: " + argName);
            }
                      
            //Integer argument
            if(argValue.matches("[-+]?[0-9]+"))
            {
                result.put(argName, Integer.valueOf(argValue));
            }
            
            //Float argument
            else if(argValue.matches("[-+]?[0-9]*\\.[0-9]+f"))
            { 
                result.put(argName, Float.valueOf(argValue));
            }
            
            //Double argument
            else if(argValue.matches("[-+]?[0-9]*\\.[0-9]+d"))
            { 
                result.put(argName, Double.valueOf(argValue));
            }
            
            //Boolean
            else if(argValue.equalsIgnoreCase("true") || argValue.equalsIgnoreCase("false"))
            {
            	result.put(argName, Boolean.valueOf(argValue));
            }
                                    
            //else treat as string argument
            else
            {
                result.put(argName, new String(ZootUtils.unquoteString(argValue)));
            }
        }

        return result;
    }
}
