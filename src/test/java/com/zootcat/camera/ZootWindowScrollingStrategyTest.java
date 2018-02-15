package com.zootcat.camera;

import static org.junit.Assert.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.zootcat.scene.ZootActor;

public class ZootWindowScrollingStrategyTest
{
	private static final float LEFT = 40.0f;
	private static final float RIGHT = 60.0f;
	private static final float TOP = 20.0f;
	private static final float BOTTOM = 30.0f;
	private static final float CAMERA_START_X = 0;
	private static final float CAMERA_START_Y = 0;
		
	@Mock private ZootActor actor;
	@Mock private ZootCamera camera;	
	private Vector3 cameraPosition;
	private ZootWindowScrollingStrategy strategy;
		
	@Before
	public void setup()
	{
		MockitoAnnotations.initMocks(this);
		cameraPosition = new Vector3(CAMERA_START_X, CAMERA_START_Y, 0.0f);		
		strategy = new ZootWindowScrollingStrategy(LEFT, RIGHT, TOP, BOTTOM);
		strategy.setScrollingSpeed(1.0f);
		
		when(camera.getPosition()).thenReturn(cameraPosition);
		when(camera.getTarget()).thenReturn(actor);
	}
	
	@Test
	public void scrollCameraOnNullTargetShouldNotChangeCameraPositionTest()
	{		
		//when
		when(camera.getTarget()).thenReturn(null);	
		strategy.scrollCamera(camera, 1.0f);
		
		//then
		verify(camera, times(1)).getTarget();
		verifyNoMoreInteractions(camera);
	}
	
	@Test
	public void scrollCameraActorInCenterOfWindowBoundaryShouldNotChangePositionTest()
	{
		//given		
		when(actor.getX()).thenReturn(CAMERA_START_X);
		when(actor.getY()).thenReturn(CAMERA_START_Y);
		
		//when
		strategy.scrollCamera(camera, 1.0f);
		
		//then
		verify(camera, times(1)).setPosition(0.0f, 0.0f);
	}
	
	@Test
	public void scrollCameraActorOutsideOfLeftBoundaryTest()
	{
		//given		
		when(actor.getX()).thenReturn(-LEFT * 2);
		when(actor.getY()).thenReturn(CAMERA_START_Y);
		
		//when
		strategy.scrollCamera(camera, 1.0f);		
		
		//then
		verify(camera, times(1)).setPosition(-LEFT, CAMERA_START_Y);
	}
	
	@Test
	public void scrollCameraActorOutsideOfRightBoundaryTest()
	{
		//given		
		when(actor.getX()).thenReturn(RIGHT * 2);
		when(actor.getY()).thenReturn(CAMERA_START_Y);
		
		//when
		strategy.scrollCamera(camera, 1.0f);		
		
		//then
		verify(camera, times(1)).setPosition(RIGHT, CAMERA_START_Y);
	}
	
	@Test
	public void scrollCameraActorOutsideOfTopBoundaryTest()
	{
		//given		
		when(actor.getX()).thenReturn(CAMERA_START_X);
		when(actor.getY()).thenReturn(TOP * 2);
		
		//when
		strategy.scrollCamera(camera, 1.0f);		
		
		//then
		verify(camera, times(1)).setPosition(CAMERA_START_X, TOP);
	}
	
	@Test
	public void scrollCameraActorOutsideOfBottomBoundaryTest()
	{
		//given		
		when(actor.getX()).thenReturn(CAMERA_START_X);
		when(actor.getY()).thenReturn(-BOTTOM * 2);
		
		//when
		strategy.scrollCamera(camera, 1.0f);		
		
		//then
		verify(camera, times(1)).setPosition(CAMERA_START_X, -BOTTOM);
	}
	
	@Test
	public void scrollCameraShouldIncludeXOffsetTest()
	{
		//given		
		when(actor.getX()).thenReturn(-LEFT * 2);
		when(actor.getY()).thenReturn(CAMERA_START_Y);
		
		//when
		strategy.setOffset(-LEFT, 0.0f);
		strategy.scrollCamera(camera, 1.0f);		
		
		//then
		verify(camera, times(1)).setPosition(0.0f, CAMERA_START_Y);	
	}
	
	@Test
	public void scrollCameraShouldIncludeYOffsetTest()
	{
		//given		
		when(actor.getX()).thenReturn(CAMERA_START_X);
		when(actor.getY()).thenReturn(-BOTTOM * 2);
		
		//when
		strategy.setOffset(0.0f, -BOTTOM);
		strategy.scrollCamera(camera, 1.0f);		
		
		//then
		verify(camera, times(1)).setPosition(CAMERA_START_X, 0.0f);
	}
	
	@Test
	public void setOffsetTest()
	{
		strategy.setOffset(0.0f, 0.0f);
		assertEquals(0.0f, strategy.getOffsetCopy().x, 0.0f);
		assertEquals(0.0f, strategy.getOffsetCopy().y, 0.0f);
		
		strategy.setOffset(1.0f, 2.0f);
		assertEquals(1.0f, strategy.getOffsetCopy().x, 0.0f);
		assertEquals(2.0f, strategy.getOffsetCopy().y, 0.0f);
		
		strategy.setOffset(-1.0f, -2.0f);
		assertEquals(-1.0f, strategy.getOffsetCopy().x, 0.0f);
		assertEquals(-2.0f, strategy.getOffsetCopy().y, 0.0f);
	}
	
	@Test
	public void getOffsetCopyShouldReturnDefensiveCopyTest()
	{
		//given
		assertEquals(0.0f, strategy.getOffsetCopy().x, 0.0f);
		assertEquals(0.0f, strategy.getOffsetCopy().y, 0.0f);
		
		//when
		Vector2 offset = strategy.getOffsetCopy();
		offset.x = 1.0f;
		offset.y = 2.0f;
		
		//then
		assertEquals(0.0f, strategy.getOffsetCopy().x, 0.0f);
		assertEquals(0.0f, strategy.getOffsetCopy().y, 0.0f);
	}
	
	@Test
	public void setScrollingSpeedTest()
	{
		strategy.setScrollingSpeed(0.0f);
		assertEquals(0.0f, strategy.getScrollingSpeed(), 0.0f);
		
		strategy.setScrollingSpeed(1.0f);
		assertEquals(1.0f, strategy.getScrollingSpeed(), 0.0f);
		
		strategy.setScrollingSpeed(-1.0f);
		assertEquals(-1.0f, strategy.getScrollingSpeed(), 0.0f);
		
		strategy.setScrollingSpeed(5.0f);
		assertEquals(5.0f, strategy.getScrollingSpeed(), 0.0f);
		
		strategy.setScrollingSpeed(-5.0f);
		assertEquals(-5.0f, strategy.getScrollingSpeed(), 0.0f);
	}
}
