package com.zootcat.scene;

import com.badlogic.gdx.math.MathUtils;

public enum ZootDirection
{
    None(0,0), Up(1,0), Down(-1,0), Left(0,-1), Right(0,1), UpLeft(1,-1), UpRight(1,1), DownLeft(-1,-1), DownRight(-1,1);

	private int verticalValue;
	private int horizontalValue;
	
	private ZootDirection(int vertical, int horizontal)
	{
		this.verticalValue = vertical;
		this.horizontalValue = horizontal;
	}
	
    public boolean isVertical()
    {
    	return verticalValue != 0;
    }
    
    public boolean isHorizontal()
    {
    	return horizontalValue != 0;
    }
    
    public int getHorizontalValue()
    {
    	return horizontalValue;
    }
    
    public int getVerticalValue()
    {
    	return verticalValue;
    }
	
    public ZootDirection invert()
    {
        switch(this) 
        {
        case Up:
            return Down;
            
        case Down:
            return Up;
            
        case Left:
            return Right;
            
        case Right:
            return Left;
            
        case UpLeft:
        	return DownRight;
        	
        case UpRight:
        	return DownLeft;
        	
        case DownLeft:
        	return UpRight;
        	
        case DownRight:
        	return UpLeft;
            
        default:
            return None;
        }
    }
        
    public static ZootDirection fromString(String value)
    {
        String trimmedValue = value.trim();
        
        if(value.equalsIgnoreCase("random"))
        {
        	return getRandomLeftRight();
        }
        
        for(ZootDirection dir : ZootDirection.values())
        {
            if(dir.name().equalsIgnoreCase(trimmedValue))
            {
                return dir;
            }
        }
        
        return None;
    }
    
    public boolean isOpposite(ZootDirection other)
    {
        return this.invert().equals(other);
    }
    
    private static ZootDirection getRandomLeftRight()
    {
    	int randomNumber = MathUtils.random(1, 100);
    	if(randomNumber <= 50) return ZootDirection.Left;
    	else return ZootDirection.Right;
    }
}
