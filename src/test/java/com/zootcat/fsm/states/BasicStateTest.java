package com.zootcat.fsm.states;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import org.junit.Test;

import com.zootcat.controllers.gfx.AnimatedSpriteController;
import com.zootcat.fsm.events.ZootEvent;
import com.zootcat.gfx.ZootAnimation;
import com.zootcat.scene.ZootActor;

public class BasicStateTest
{
	@Test
	public void shouldReturnHashCode()
	{
		BasicState state1 = new BasicState("test");
		BasicState state2 = new BasicState("test");
		BasicState state3 = new BasicState("TEST");
		
		assertEquals(state1.hashCode(), state2.hashCode());
		assertFalse(state2.hashCode() == state3.hashCode());
	}
		
	@Test
	public void shouldReturnEquals()
	{
		BasicState state1 = new BasicState("test");
		BasicState state2 = new BasicState("test");
		BasicState state3 = new BasicState("TEST");
		
		assertTrue(state1.equals(state2));
		assertTrue(state1.equals(state1));
		assertTrue(state2.equals(state1));
		assertTrue(state2.equals(state2));
		assertFalse(state1.equals(state3));
		assertFalse(state2.equals(state3));
		assertFalse(state3.equals(state1));
		assertFalse(state3.equals(state2));
		
		assertFalse(state1.equals(1));
		assertFalse(state1.equals(null));
		assertFalse(state1.equals("this is a string"));		
	}
	
	@Test
	public void shouldReturnId()
	{
		BasicState state1 = new BasicState("test");
		BasicState state2 = new BasicState("test");
		BasicState state3 = new BasicState("TEST");
		
		assertEquals("test".hashCode(), state1.getId());
		assertEquals("test".hashCode(), state2.getId());
		assertEquals("TEST".hashCode(), state3.getId());
		assertEquals(state1.getId(), state2.getId());
		assertFalse(state2.getId() == state3.getId());
	}
	
	@Test
	public void shouldReturnName()
	{
		assertEquals("ABC", new BasicState("ABC").getName());
		assertEquals("test", new BasicState("test").getName());
		assertEquals("", new BasicState("").getName());
	}
	
	@Test
	public void shouldSetName()
	{
		BasicState state = new BasicState("oldName");
		state.setName("newName");
		assertEquals("newName", state.getName());		
	}
	
	@Test
	public void shouldConvertToString()
	{
		assertEquals("ABC", new BasicState("ABC").toString());
		assertEquals("test", new BasicState("test").toString());
		assertEquals("", new BasicState("").toString());		
	}
	
	@Test
	public void shouldAlwaysReturnFalseOnHandle()
	{
		assertFalse("NamedState should always return false", new BasicState("").handle(new ZootEvent()));
	}
	
	@Test
	public void actionsShouldNotCauseAnySideEffects()
	{
		//given
		BasicState state = new BasicState("test");
		ZootActor actor = mock(ZootActor.class);
		
		//when
		state.onEnter(actor, null);
		state.onUpdate(actor, 0.0f);
		state.onLeave(actor, null);
		
		//then
		verifyZeroInteractions(actor);		
	}
	
	@Test
	public void shouldReturnNullActorAnimation()
	{
		//given
		BasicState state = new BasicState("test");
		ZootActor actor = mock(ZootActor.class);
		
		//when
		when(actor.getSingleController(AnimatedSpriteController.class)).thenReturn(null);

		//then
		assertNull(state.getActorAnimation(actor));
	}
	
	@Test
	public void shouldReturnActorAnimation()
	{
		//given
		BasicState state = new BasicState("test");
		ZootActor actor = mock(ZootActor.class);
		AnimatedSpriteController spriteCtrl = mock(AnimatedSpriteController.class);
		ZootAnimation animation = mock(ZootAnimation.class);
		
		//when
		when(actor.getSingleController(AnimatedSpriteController.class)).thenReturn(spriteCtrl);
		when(spriteCtrl.getCurrentAnimation()).thenReturn(animation);
		
		//then
		assertEquals(animation, state.getActorAnimation(actor));
	}
	
	@Test
	public void shouldSetActorAnimation()
	{
		//given
		BasicState state = new BasicState("test");
		ZootActor actor = new ZootActor();
		AnimatedSpriteController spriteCtrl = mock(AnimatedSpriteController.class);
		
		//when
		actor.addController(spriteCtrl);
		state.setActorAnimation(actor, "Hurt");
		
		//then
		verify(spriteCtrl).setAnimation("Hurt");		
	}
	
	@Test
	public void shouldSetActorAnimationBasedOnStateName()
	{
		//given
		BasicState state = new BasicState("test");
		ZootActor actor = new ZootActor();
		AnimatedSpriteController spriteCtrl = mock(AnimatedSpriteController.class);
		
		//when
		actor.addController(spriteCtrl);
		state.setAnimationBasedOnStateName(actor);
		
		//then
		verify(spriteCtrl).setAnimation("test");		
	}
	
	@Test
	public void shouldNotSetActorAnimationIfPlayingTheSameOne()
	{
		//given
		BasicState state = new BasicState("test");
		ZootActor actor = new ZootActor();
		AnimatedSpriteController spriteCtrl = mock(AnimatedSpriteController.class);
		ZootAnimation currentAnimation = mock(ZootAnimation.class);
		
		//when
		actor.addController(spriteCtrl);
		when(spriteCtrl.getCurrentAnimation()).thenReturn(currentAnimation);
		when(currentAnimation.getName()).thenReturn("Run");
		
		//then
		state.setActorAnimationIfNotSet(actor, "Run");
		verify(spriteCtrl, never()).setAnimation("Run");
	}
	
	@Test
	public void shouldSetActorAnimationIfPlayingDifferentOne()
	{
		//given
		BasicState state = new BasicState("test");
		ZootActor actor = new ZootActor();
		AnimatedSpriteController spriteCtrl = mock(AnimatedSpriteController.class);
		ZootAnimation currentAnimation = mock(ZootAnimation.class);
		
		//when
		actor.addController(spriteCtrl);
		when(spriteCtrl.getCurrentAnimation()).thenReturn(currentAnimation);
		when(currentAnimation.getName()).thenReturn("Walk");
		
		//then
		state.setActorAnimationIfNotSet(actor, "Run");
		verify(spriteCtrl).setAnimation("Run");		
	}
}
