package com.zootcat.dialogs;

import java.io.File;
import java.util.List;

import com.badlogic.gdx.graphics.Texture;
import com.zootcat.assets.ZootAssetManager;
import com.zootcat.exceptions.ZootException;
import com.zootcat.scene.ZootDirection;
import com.zootcat.textdata.TextDataFile;
import com.zootcat.textdata.TextDataSection;
import com.zootcat.utils.ZootUtils;

public class ZootDialog 
{
	private static final int MAX_LINES = 5;
	private static final float CHARS_PER_SECOND = 50.0f;
        
    private int currentFrameIndex = 0;    
    private String currentText = new String();
    private List<TextDataSection> dialogFrames;
    private int visibleTextLength = 0;
    private float time = 0.0f;
    private ZootDirection facePosition = ZootDirection.None;
    private String currentName = "";
    private Texture currentFace = null;
    private String currentFaceFileName = "";
    private boolean realTime = true;
    private boolean forceFinished = false;
    private Texture currentImage = null;
    private ZootAssetManager assetManager;
        
    public ZootDialog(String path, String startToken, String endToken, ZootAssetManager assetManager) throws ZootException
    {
        this(new TextDataFile(new File(path)), startToken, endToken, assetManager);
    }
    
    public ZootDialog(TextDataFile dialogFile, String startToken, String endToken, ZootAssetManager assetManager) throws ZootException
    {        
        List<TextDataSection> settingFrames = dialogFile.readSections(":Settings", ":EndSettings");
        if(settingFrames.size() > 0)
        {
            TextDataSection settingSection = settingFrames.get(0);
            realTime = Boolean.valueOf(settingSection.getString("realTime", "true"));
        }
        
        dialogFrames = dialogFile.readSections(startToken, endToken); 
        if(dialogFrames.isEmpty())
        {
        	throw new ZootException("No dialog tokens found: " + startToken + ", " + endToken);
        }
        
        this.assetManager = assetManager;
        
        updateCurrentFrame();
        setVisibleText(0);
    }
        
    public Texture getCurrentImage()
    {
    	return currentImage;
    }
    
    public boolean isRealTime()
    {
        return realTime;
    }
    
    protected void updateCurrentFrame()
    {
        currentFace = null;
        TextDataSection currentFrame = dialogFrames.get(currentFrameIndex);
        currentName = currentFrame.getString("actorName", "");
        facePosition = ZootDirection.fromString(currentFrame.getString("facePosition", "left"));
        try
        {
            currentFaceFileName = ZootUtils.trimLeadingSlashes(currentFrame.getString("faceImage", ""));            
            currentFace = assetManager.getOrLoad(currentFaceFileName, Texture.class);
        }
        catch(Exception e)
        {
            currentFace = null;
        }
        try
        {
        	String currentImageFileName = ZootUtils.trimLeadingSlashes(currentFrame.getString("dialogImage", ""));
        	currentImage = assetManager.getOrLoad(currentImageFileName, Texture.class);
        }
        catch(Exception e)
        {
        	currentImage = null;
        }
    }
        
    public Texture getCurrentFace()
    {
        return currentFace;
    }
    
    public String getCurrentName()
    {
        return currentName;
    }

    public void nextFrame()
    {
        if(++currentFrameIndex < dialogFrames.size())
        {        
            setVisibleText(currentFrameIndex);            
            updateCurrentFrame();
        }
        else
        {
            currentText = "";
            visibleTextLength = 0;
            facePosition = ZootDirection.None;
        }
        time = 0.0f;
    }
    
    public void rewind()
    {
        time = 0.0f;
        currentFrameIndex = 0;
        forceFinished = false;
        updateCurrentFrame();
        setVisibleText(0);
    }

    public ZootDirection getFacePosition()
    {
        return facePosition;
    }
    
    public String getVisibleText()
    {
        return currentText.substring(0, visibleTextLength);
    }
    
    protected void setVisibleText(int frameIndex)
    {
        currentText = "";
        visibleTextLength = 0;
        TextDataSection frame = dialogFrames.get(frameIndex);        
        for(int i = 1; i <= MAX_LINES; ++i)
        {            
            String key = "line" + i;            
            String line = frame.getString(key, "");
            currentText += line + "\n"; 
        }
    }
    
    public List<TextDataSection> getFrames()
    {
        return dialogFrames;
    }
    
    public boolean currentFrameFinished()
    {
        return visibleTextLength == currentText.length();
    }
    
    public void update(float dt)
    {
        final float characterTimeout = 1.0f / CHARS_PER_SECOND; 
        time += dt;
        if(time >= characterTimeout)
        {
            int numOfChars = Math.round(time / characterTimeout);
            visibleTextLength = Math.min(visibleTextLength + numOfChars, currentText.length());
            time -= numOfChars * characterTimeout;
        }
    }

    protected boolean showEnter()
    {
        return true;
    }

    public boolean finished()
    {
        return forceFinished || currentFrameIndex >= dialogFrames.size();
    }

    public void showWholeFrame()
    {        
        this.visibleTextLength = currentText.length();
    }

    public void forceFinish()
    {
        forceFinished = true;
        showWholeFrame();
    }
    
    public String getCurrentFaceFileName()
    {
    	return currentFaceFileName;
    }           
}