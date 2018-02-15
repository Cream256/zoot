package com.zootcat.textdata;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.zootcat.exceptions.ZootException;
import com.zootcat.testing.ZootTestUtils;

public class TextDataFileTest
{
    private File testFile;
	
	@Before
    public void setup()
    {
    	String textDataFilePath = ZootTestUtils.getResourcePath("testResources/textdata/TextDataFile.txt", this);
    	testFile = new File(  textDataFilePath);
    }
	
	@Test(expected=ZootException.class)
    public void shouldThrowErrorWhenFileDoesNotExist() throws ZootException
    {
        File file = new File("");    	
    	new TextDataFile(file);
    }
        
    @Test
    public void readSectionsTest() throws ZootException
    {
        TextDataFile file = new TextDataFile(testFile);
        
        List<TextDataSection> result = file.readSections(":SectionStart", ":SectionEnd");
        
        assertEquals(2, result.size());
        assertEquals(":SectionStart", result.get(0).getStartToken());
        assertEquals(":SectionEnd", result.get(0).getEndToken());
        assertEquals("CatName", result.get(0).getString("cat", "error"));
        assertEquals("DogName", result.get(0).getString("dog", "error"));
        
        assertEquals(":SectionStart", result.get(1).getStartToken());
        assertEquals(":SectionEnd", result.get(1).getEndToken());
        assertEquals("Tom", result.get(1).getString("cat", "error"));
        assertEquals("Jerry", result.get(1).getString("mouse", "error"));
        
        result = file.readSections(":OtherSectionStart", ":OtherSectionEnd");
        assertEquals(1, result.size());
    }    
}
