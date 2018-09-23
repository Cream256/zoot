package com.zootcat.scene;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyFloat;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.InOrder;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.zootcat.controllers.ChangeListenerController;
import com.zootcat.controllers.Controller;
import com.zootcat.controllers.ControllerPriority;
import com.zootcat.controllers.factory.mocks.CountingController;
import com.zootcat.controllers.factory.mocks.MockBaseController;
import com.zootcat.controllers.factory.mocks.MockDerivedController;
import com.zootcat.controllers.factory.mocks.SimpleController;
import com.zootcat.controllers.gfx.RenderController;
import com.zootcat.exceptions.RuntimeZootException;

public class ZootActorTest 
{	
	private Controller mockCtrl1;
	private Controller mockCtrl2;
	private Controller mockCtrl3;
	
	@BeforeClass
	public static void setupClass()
	{		
		Gdx.graphics = mock(Graphics.class);		
	}
	
	@AfterClass
	public static void tearDownClass()
	{
		Gdx.graphics = null;
	}
	
	@Before
	public void setup()
	{
		mockCtrl1 = mock(Controller.class);
		mockCtrl2 = mock(Controller.class);
		mockCtrl3 = mock(Controller.class);	
		when(mockCtrl1.getPriority()).thenReturn(ControllerPriority.Normal);
		when(mockCtrl2.getPriority()).thenReturn(ControllerPriority.Normal);
		when(mockCtrl3.getPriority()).thenReturn(ControllerPriority.Normal);
	}
	
	@Test
	public void shouldCreateProperDefaultValuesAfterConstruction()
	{
		ZootActor actor = new ZootActor();
		assertEquals("Should have default name", ZootActor.DEFAULT_NAME, actor.getName());
		assertNotNull("Should have state machine", actor.getStateMachine());
		assertEquals("State machine should listen to events", 1, actor.getListeners().size);
		assertTrue("State machine should listen to events", actor.getListeners().contains(actor.getStateMachine(), true));
	}
	
	@Test
	public void shouldUpdateControllersAndStateMachineTest()
	{
		//given
		Controller ctrl1 = mock(Controller.class);
		Controller ctrl2 = mock(Controller.class);		
		ZootActor actor = new ZootActor();
		
		//when
		actor.addController(ctrl1);
		actor.addController(ctrl2);		
		actor.act(0.5f);
		
		//then
		verify(ctrl1, times(1)).onUpdate(0.5f, actor);
		verify(ctrl2, times(1)).onUpdate(0.5f, actor);
	}
	
    @Test
    public void shouldProperlyAddAndRemoveActorTypes()
    {
        //given
        ZootActor actor = new ZootActor();
        
        //then
        assertTrue("New actor should have empty types", actor.getTypes().isEmpty());
        
        //when
        actor.addType("newType");
        
        //then
        assertEquals(1, actor.getTypes().size());
        assertTrue(actor.isType("newType"));
        assertTrue(actor.isType("newtype"));
        assertTrue(actor.isType("NEWTYPE"));
        assertFalse(actor.isType(" newType "));
        assertFalse(actor.isType("xyz"));
        
        //when
        actor.removeType("newType");
        
        //then
        assertEquals(0, actor.getTypes().size());
        assertFalse(actor.isType("newType"));
        assertFalse(actor.isType("newtype"));
        assertFalse(actor.isType("NEWTYPE"));
        assertFalse(actor.isType(" newType "));
        assertFalse(actor.isType("xyz"));
    }
	
	@Test
	public void shouldProperlyAddController()
	{
		ZootActor actor = new ZootActor();
		assertEquals("After creation controller list should be empty", 0, actor.getAllControllers().size());
		
		actor.addController(mockCtrl1);
		assertEquals("Controller should be added immediatelly", 1, actor.getAllControllers().size());
				
		actor.addController(mockCtrl2);
		assertEquals("Controllers should  be added immediatelly", 2, actor.getAllControllers().size());
	}
	
	@Test
	public void shouldAddDuplicateControllerInstances()
	{
		ZootActor actor = new ZootActor();
		actor.addController(mockCtrl1);
		actor.addController(mockCtrl2);
		actor.addController(mockCtrl1);
		actor.addController(mockCtrl2);
		actor.act(0.0f);
		
		List<Controller> actual = actor.getAllControllers();
		List<Controller> expected = Arrays.asList(mockCtrl1, mockCtrl2, mockCtrl1, mockCtrl2);
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void shouldProperlyRemoveControllers()
	{
		//given
		ZootActor actor = new ZootActor();
		
		//when
		actor.addController(mockCtrl1);
		actor.addController(mockCtrl2);
		
		//then
		assertEquals(2, actor.getAllControllers().size());
		
		//when, then
		actor.removeController(mockCtrl1);
		assertEquals(1, actor.getAllControllers().size());

		//when, then
		assertEquals(1, actor.getAllControllers().size());
		assertEquals(mockCtrl2, actor.getAllControllers().get(0));
		
		//when, then		
		actor.removeController(mockCtrl2);
		assertEquals(0, actor.getAllControllers().size());
	}	
	
	@Test
	public void shouldDoNothingWhenRemovingControllersFromActorWithNoControllers()
	{
		ZootActor actor = new ZootActor();
		assertEquals(0, actor.getAllControllers().size());
		
		actor.removeController(mockCtrl1);
		actor.removeController(mockCtrl2);
		assertEquals(0, actor.getAllControllers().size());
		
		actor.act(0.0f);
		assertEquals(0, actor.getAllControllers().size());
	}
	
	@Test
	public void shouldRemoveAllControllersWithGivenType()
	{
		//given
		ZootActor actor = new ZootActor();
		
		//when
		actor.addController(mockCtrl1);
		actor.addController(mockCtrl2);
		actor.addController(mockCtrl3);
		actor.addController(mockCtrl1);
		actor.addController(mockCtrl2);
		actor.addController(mockCtrl3);
		
		//then
		assertEquals(6, actor.getAllControllers().size());
		
		//when
		actor.removeController(mockCtrl1);
		
		//then
		List<Controller> expected = Arrays.asList(mockCtrl2, mockCtrl3, mockCtrl2, mockCtrl3);
		assertEquals(4, actor.getAllControllers().size());
		assertEquals(expected, actor.getAllControllers());
	}
	
	@Test
	public void shouldInvokeControllerOnAddMethodWhenAddingControllerToActor()
	{
		//given
		ZootActor actor = new ZootActor();
		
		//then
		verify(mockCtrl1, times(0)).onAdd(actor);
		verify(mockCtrl2, times(0)).onAdd(actor);
		
		//when
		actor.addController(mockCtrl1);
		actor.addController(mockCtrl2);	
				
		//then
		verify(mockCtrl1, times(1)).onAdd(actor);
		verify(mockCtrl2, times(1)).onAdd(actor);
	}
	
	@Test
	public void shouldInvokeControllerOnRemoveMethodWhenRemovingControllersFromActor()
	{
		//given
		ZootActor actor = new ZootActor();
		actor.addController(mockCtrl1);
		actor.addController(mockCtrl2);
		
		//then
		verify(mockCtrl1, times(1)).onAdd(actor);
		verify(mockCtrl2, times(1)).onAdd(actor);
		
		//when
		actor.removeController(mockCtrl1);
		actor.removeController(mockCtrl2);	
				
		//then
		verify(mockCtrl1, times(1)).onRemove(actor);
		verify(mockCtrl2, times(1)).onRemove(actor);
	}
	
	@Test	
	public void shouldNotifyChangeListenerWhenActorChanges()
	{
		//given
		ZootActor actor = new ZootActor();
		ChangeListenerController controller = mock(ChangeListenerController.class);
		
		//when
		actor.addController(controller);
		actor.act(0.0f);
		actor.setPosition(5, 5);
		
		//then
		verify(controller, times(1)).onPositionChange(actor);
		
		//when
		actor.setSize(1.0f, 1.0f);
		
		//then
		verify(controller, times(1)).onSizeChange(actor);
		
		//when
		actor.setRotation(256.0f);
		
		//then
		verify(controller, times(1)).onRotationChange(actor);	
	}
	
	@Test
	public void shouldExecuteAllRenderControllersOnDraw()
	{
		//given
		final float parentAlpha = 1.0f;
		final Batch batch = mock(Batch.class);
		
		ZootActor actor = new ZootActor();
		RenderController renderCtrl1 = mock(RenderController.class);
		RenderController renderCtrl2 = mock(RenderController.class);
		ChangeListenerController changeListenerController = mock(ChangeListenerController.class);
		
		//when
		actor.addController(renderCtrl1);
		actor.addController(changeListenerController);
		actor.addController(renderCtrl2);
		actor.act(0.0f);
		
		//then
		verify(renderCtrl1, times(0)).onRender(anyObject(), anyFloat(), anyObject(), anyFloat());;
		verify(renderCtrl2, times(0)).onRender(anyObject(), anyFloat(), anyObject(), anyFloat());;
		
		//when
		actor.draw(batch, 1.0f);
				
		//then
		verify(renderCtrl1, times(1)).onRender(eq(batch), eq(parentAlpha), anyObject(), anyFloat());;
		verify(renderCtrl2, times(1)).onRender(eq(batch), eq(parentAlpha), anyObject(), anyFloat());;
		
		//when
		actor.draw(batch, 1.0f);
		
		//then
		verify(renderCtrl1, times(2)).onRender(eq(batch), eq(parentAlpha), anyObject(), anyFloat());;
		verify(renderCtrl2, times(2)).onRender(eq(batch), eq(parentAlpha), anyObject(), anyFloat());;
	}
	
	@Test
	public void shouldReturnStateMachine()
	{
		//when
		ZootActor actor = new ZootActor();
		
		//then
		assertNotNull(actor.getStateMachine());
		assertEquals(actor, actor.getStateMachine().getOwner());
	}
	
	@Test
	public void shouldReturnController()
	{
		//given
		Controller ctrl = new SimpleController();
		ZootActor actor = new ZootActor();

		//when
		actor.addController(ctrl);
		
		//then
		assertEquals(ctrl, actor.getController(SimpleController.class));		
	}
	
	@Test(expected = RuntimeZootException.class)
	public void shouldThrowIfMoreThanOneInstanceOfControllerIsAvaiable()
	{
		//given
		Controller ctrl1 = new MockBaseController();
		Controller ctrl2 = new MockDerivedController();
		
		ZootActor actor = new ZootActor();

		//when
		actor.addController(ctrl1);
		actor.addController(ctrl2);
		
		//then
		actor.getController(MockBaseController.class);		
	}
		
	@Test(expected = RuntimeZootException.class)
	public void shuoldThrowIfControllerIsNotFound()
	{
		ZootActor actor = new ZootActor();
		actor.getController(SimpleController.class);
	}
	
	@Test
	public void shouldReturnControllerAndNotThrow()
	{
		//given
		Controller ctrl = new SimpleController();
		ZootActor actor = new ZootActor();

		//when
		actor.addController(ctrl);
		
		//then
		assertEquals(ctrl, actor.tryGetController(SimpleController.class));
	}
	
	@Test
	public void shouldReturnNullAndNotThrow()
	{
		ZootActor actor = new ZootActor();
		assertNull(actor.tryGetController(SimpleController.class));
	}
		
	@Test
	public void shouldNotThrowOnControllerActionIfControllerIsNotFoundTest()
	{
		ZootActor actor = new ZootActor();
		actor.controllerAction(SimpleController.class, (ctrl) -> {});
	}
	
	@Test
	public void shouldExecuteControllerAction()
	{
		//given
		ZootActor actor = new ZootActor();		
		SimpleController ctrl = new SimpleController();
		
		//when
		actor.addController(ctrl);
		actor.controllerAction(SimpleController.class, (c) -> c.set(100));
		
		//then
		assertEquals(100, ctrl.get());
	}
	
	@Test
	public void shouldReturnProperControllerCondition()
	{
		//given
		ZootActor actor = new ZootActor();		
		SimpleController ctrl = new SimpleController();		
		
		//when
		ctrl.set(100);
		actor.addController(ctrl);
		
		//then
		assertTrue(actor.controllerCondition(SimpleController.class, (c) -> c.get() == 100));
		assertFalse(actor.controllerCondition(SimpleController.class, (c) -> c.get() == 0));
	}
	
	@Test
	public void shouldReturnFalseIfControllerIsNotFoundForControllerCondition()
	{
		ZootActor actor = new ZootActor();
		assertFalse(actor.controllerCondition(SimpleController.class, (c) -> true));
	}
	
	@Test
	public void shouldAddControllersFirstAndThenInvokeOnAddMethods()
	{
		//given
		CountingController ctrl1 = new CountingController();
		CountingController ctrl2 = new CountingController();
		CountingController ctrl3 = new CountingController();
		ZootActor actor = new ZootActor();
		
		//when
		actor.addControllers(Arrays.asList(ctrl1, ctrl2, ctrl3));
		
		//then
		assertEquals(3, ctrl1.getControllersCountOnAdd());
		assertEquals(3, ctrl2.getControllersCountOnAdd());
		assertEquals(3, ctrl3.getControllersCountOnAdd());
	}
	
	@Test
	public void shouldAddControllersInPriorityOrder()
	{
		//given
		Controller ctrl1 = mock(Controller.class);
		Controller ctrl2 = mock(Controller.class);
		Controller ctrl3 = mock(Controller.class);
		when(ctrl1.getPriority()).thenReturn(ControllerPriority.High);
		when(ctrl2.getPriority()).thenReturn(ControllerPriority.Normal);
		when(ctrl3.getPriority()).thenReturn(ControllerPriority.Low);		
		ZootActor actor = new ZootActor();
		
		//when
		InOrder inOrder = inOrder(ctrl1, ctrl2, ctrl3);
		actor.addControllers(Arrays.asList(ctrl2, ctrl3, ctrl1));
		
		//then
		inOrder.verify(ctrl1).onAdd(actor);
		inOrder.verify(ctrl2).onAdd(actor);
		inOrder.verify(ctrl3).onAdd(actor);
	}
	
	@Test
	public void shouldRemoveAllControllersWhenRemovingActor()
	{
		//given
		ZootActor actor = new ZootActor();
		actor.addControllers(Arrays.asList(mockCtrl1, mockCtrl2, mockCtrl3));
		
		//when
		actor.remove();
		
		//then
		verify(mockCtrl1, times(1)).onRemove(actor);
		verify(mockCtrl2, times(1)).onRemove(actor);
		verify(mockCtrl3, times(1)).onRemove(actor);
		assertEquals(0, actor.getAllControllers().size());
	}
	
	@Test
	public void shouldRemoveAllControllersInDescendingPriorityOrder()
	{
		//given
		when(mockCtrl1.getPriority()).thenReturn(ControllerPriority.Normal);
		when(mockCtrl2.getPriority()).thenReturn(ControllerPriority.High);
		when(mockCtrl3.getPriority()).thenReturn(ControllerPriority.Low);
		
		InOrder inOrder = inOrder(mockCtrl1, mockCtrl2, mockCtrl3);
		
		ZootActor actor = new ZootActor();
		actor.addControllers(Arrays.asList(mockCtrl1, mockCtrl2, mockCtrl3));
		
		//when
		actor.remove();
		
		//then
		inOrder.verify(mockCtrl3).onRemove(actor);
		inOrder.verify(mockCtrl1).onRemove(actor);
		inOrder.verify(mockCtrl2).onRemove(actor);
	}
	
	@Test
	public void shouldRemoveAllControllers()
	{
		//given
		ZootActor actor = new ZootActor();
		actor.addControllers(Arrays.asList(mockCtrl1, mockCtrl2, mockCtrl3));
		
		//when
		actor.removeAllControllers();
		
		//then
		verify(mockCtrl1, times(1)).onRemove(actor);
		verify(mockCtrl2, times(1)).onRemove(actor);
		verify(mockCtrl3, times(1)).onRemove(actor);
		assertEquals(0, actor.getAllControllers().size());
	}
	
	@Test
	public void shouldSetId()
	{
		ZootActor actor = new ZootActor();
		assertEquals(0, actor.getId());
		
		actor.setId(1);
		assertEquals(1, actor.getId());
	}
	
	@Test
	public void shouldSetGid()
	{
		ZootActor actor = new ZootActor();
		assertEquals(-1, actor.getGid());
		
		actor.setGid(1);
		assertEquals(1, actor.getGid());		
	}
	
	@Test
	public void shuouldSetOpacity()
	{
		ZootActor actor = new ZootActor();
		assertEquals(1.0f, actor.getOpacity(), 0.0f);
		
		actor.setOpacity(0.5f);
		assertEquals(0.5f, actor.getOpacity(), 0.0f);
		
		actor.setOpacity(0.0f);
		assertEquals(0.0f, actor.getOpacity(), 0.0f);
		
		actor.setOpacity(-1.0f);
		assertEquals(0.0f, actor.getOpacity(), 0.0f);
		
		actor.setOpacity(1.1f);
		assertEquals(1.0f, actor.getOpacity(), 0.0f);
	}
	
	@Test
	public void shouldReturnAllControllersOfGivenType()
	{
		//given
		ZootActor actor = new ZootActor();
		MockBaseController baseCtrl = new MockBaseController();
		MockDerivedController derivedCtrl = new MockDerivedController();
		SimpleController simpleCtrl = new SimpleController();
				
		//when
		actor.addController(baseCtrl);
		actor.addController(derivedCtrl);
		actor.addController(simpleCtrl);
		
		//then
		List<MockBaseController> ctrls = actor.getControllersOfType(MockBaseController.class);
		assertNotNull(ctrls);
		assertEquals(2, ctrls.size());
		assertTrue(ctrls.contains(baseCtrl));
		assertTrue(ctrls.contains(derivedCtrl));
		assertFalse(ctrls.contains(simpleCtrl));
	}
	
	@Test
	public void shouldInvokeActionOnAllControllersOfGivenType()
	{
		//given
		ZootActor actor = new ZootActor();
		MockBaseController ctrl1 = mock(MockBaseController.class);
		MockDerivedController ctrl2 = mock(MockDerivedController.class);		
		SimpleController ctrl3 = mock(SimpleController.class);
		
		//when
		actor.addController(ctrl1);
		actor.addController(ctrl2);
		actor.addController(ctrl3);
		actor.controllersOfTypeAction(MockBaseController.class, ctrl -> ctrl.getBaseParam());
		
		//then
		verify(ctrl1).getBaseParam();
		verify(ctrl2).getBaseParam();
		verify(ctrl3).onAdd(actor);
		verifyNoMoreInteractions(ctrl3);
	}
	
	@Test
	public void shouldSetScene()
	{
		//given
		ZootActor actor = new ZootActor();
		ZootScene scene = mock(ZootScene.class);
		
		//when
		actor.setScene(scene);
		
		//then
		assertEquals(scene, actor.getScene());
	}
}
