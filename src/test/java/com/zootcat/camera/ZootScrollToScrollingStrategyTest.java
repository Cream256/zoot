package com.zootcat.camera;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.badlogic.gdx.math.Vector3;
import com.zootcat.scene.ZootActor;

public class ZootScrollToScrollingStrategyTest
{
	private static final float ACTOR_X = 10.0f;
	private static final float ACTOR_Y = 20.0f;
	private static final float ACTOR_WIDTH = 100.0f;
	private static final float ACTOR_HEIGHT = 200.0f;
	
	@Mock private ZootActor actor;
	@Mock private ZootCamera camera;
	
	@Before
	public void setup()
	{
		MockitoAnnotations.initMocks(this);
		when(actor.getX()).thenReturn(ACTOR_X);
		when(actor.getY()).thenReturn(ACTOR_Y);
		when(actor.getWidth()).thenReturn(ACTOR_WIDTH);
		when(actor.getHeight()).thenReturn(ACTOR_HEIGHT);
		when(camera.getPosition()).thenReturn(Vector3.Zero);
	}
	
	@Test
	public void shouldScrollToActorCenter()
	{
		//given
		float expectedDuration = 2.0f;
		
		//when
		ZootScrollToScrollingStrategy strategy = new ZootScrollToScrollingStrategy(actor, expectedDuration);
		strategy.scrollCamera(camera, 1.0f);
		
		//then
		verify(camera).setPosition((ACTOR_X + ACTOR_WIDTH * 0.5f) * 0.5f, (ACTOR_Y + ACTOR_HEIGHT * 0.5f) * 0.5f);
		
		//when
		strategy.scrollCamera(camera, 1.0f);

		//then
		verify(camera).setPosition(ACTOR_X + ACTOR_WIDTH * 0.5f, ACTOR_Y + ACTOR_HEIGHT * 0.5f);
	}
	
	@Test
	public void shouldScrollToPoint()
	{
		//given
		float expectedX = 100.0f;
		float expectedY = 200.0f;
		float expectedDuration = 2.0f;
		
		//when
		ZootScrollToScrollingStrategy strategy = new ZootScrollToScrollingStrategy(expectedX, expectedY, expectedDuration);
		strategy.scrollCamera(camera, 1.0f);
		
		//then
		verify(camera).setPosition(expectedX / 2.0f, expectedY / 2.0f);
		
		//when
		strategy.scrollCamera(camera, 1.0f);

		//then
		verify(camera).setPosition(expectedX, expectedY);
	}
	
}
