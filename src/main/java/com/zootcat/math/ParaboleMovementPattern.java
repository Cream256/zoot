package com.zootcat.math;

import com.badlogic.gdx.math.Vector2;

public class ParaboleMovementPattern
{
    private float a;
    private float p;
    private float q;

    //from: http://www.matmana6.pl/tablice_matematyczne/liceum/funkcja_kwadratowa/75-wyznaczanie_wzoru_funkcji_kwadratowej
    //parabolic equation used: f(x) = a(x - p)^2 + q => a(x - p)^2 = y - q
    public ParaboleMovementPattern(Vector2 peak, Vector2 pointOnParabole)
    {
        p = peak.x;
        q = peak.y;        
        
        double px = pointOnParabole.x;
        double py = pointOnParabole.y;        
        double eqLeft = Math.pow(px - p, 2.0); 
        double eqRight = py - q;      
        
        a = (float)(eqRight / eqLeft);
    }
        
	public Vector2 at(float time) 
	{
		float x = time;
		float y = (float)(a * Math.pow(x - p, 2.0) + q);		
		return new Vector2(x, y);
	}
}