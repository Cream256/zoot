package com.zootcat.utils;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class CollisionMaskTest
{	
	@Test
	public void shouldCreateEmptyCollisionMask()
	{
		CollisionMask collisionMask = new CollisionMask();
		assertEquals("", collisionMask.toString());		
	}
	
	@Test
	public void shouldCreateCollisionMaskFromString()
	{
		assertEquals("PLAYER", new CollisionMask("PLAYER").toString());
		assertEquals("PLAYER|ENEMY", new CollisionMask("player|enemy").toString());
		assertEquals("PLAYER|ENEMY|SOLID", new CollisionMask("  plaYER | ENEMY   |solid  ").toString());		
	}
	
	@Test
	public void shouldAddValue()
	{
		//given
		CollisionMask collisionMask = new CollisionMask();
		
		//when
		collisionMask.add("PLAYER");
		
		//then
		assertEquals("PLAYER", collisionMask.toString());
	}

	@Test
	public void shouldAddTheSameValueOnlyOnce()
	{
		//given
		CollisionMask collisionMask = new CollisionMask();
		
		//when
		collisionMask.add("PLAYER");
		collisionMask.add(" PLAYER ");
		collisionMask.add("player");
		
		//then
		assertEquals("PLAYER", collisionMask.toString());
	}
	
	@Test
	public void shuoldNotAddEmptyOrWhitespaceValue()
	{
		//given
		CollisionMask collisionMask = new CollisionMask();
		
		//when
		collisionMask.add("");
		collisionMask.add(" ");
		
		//then
		assertEquals("", collisionMask.toString());
	}
	
	@Test
	public void shouldRemoveValue()
	{
		//given
		CollisionMask collisionMask = new CollisionMask();
		
		//when
		collisionMask.add("PLAYER");
		collisionMask.remove("PLAYER");
		
		//then
		assertEquals("", collisionMask.toString());
	}
	
	@Test
	public void shouldConvertToBitMask()
	{
		//given
		CollisionMask collisionMask = new CollisionMask();
		
		//when
		collisionMask.add("PLAYER");
		
		//then
		assertEquals(BitMaskConverter.Instance.fromString("PLAYER"), collisionMask.toBitMask());
	}
}
