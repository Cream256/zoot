package com.zootcat.screen;

import org.junit.Before;
import org.junit.Test;

public class ZootScreenAdapterTest
{
	private ZootScreenAdapter screenAdapter;
		
	@Before
	public void setup()
	{
		screenAdapter = new ZootScreenAdapter();
	}
	
	@Test
	public void shouldNotThrowOnRender()
	{
		screenAdapter.onRender(1.0f);
		//ok
	}
	
	@Test
	public void shouldNotThrowOnUpdate()
	{
		screenAdapter.onUpdate(1.0f);
		//ok
	}	
}
