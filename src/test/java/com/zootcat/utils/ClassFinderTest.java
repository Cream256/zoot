package com.zootcat.utils;

import static org.junit.Assert.assertFalse;

import java.util.List;

import org.junit.Test;

import com.zootcat.utils.ClassFinder;

public class ClassFinderTest
{    
    @Test
    public void findTest()
    {        
        List<Class<?>> result = ClassFinder.find("com.zootcat.utils", true);
        assertFalse(result.isEmpty());
    }   
}
