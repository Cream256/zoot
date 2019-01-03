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
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
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
import com.zootcat.controllers.ControllerAdapter;
import com.zootcat.controllers.ControllerPriority;
import com.zootcat.controllers.factory.mocks.MockBaseController;
import com.zootcat.controllers.factory.mocks.MockDerivedController;
import com.zootcat.controllers.factory.mocks.MockEmptyController;
import com.zootcat.controllers.factory.mocks.RenderControllerMock1;
import com.zootcat.controllers.factory.mocks.RenderControllerMock2;
import com.zootcat.controllers.factory.mocks.SimpleController;
import com.zootcat.controllers.gfx.RenderController;
import com.zootcat.exceptions.ZootControllerNotFoundException;
import com.zootcat.exceptions.ZootDuplicatedControllerException;

public class ZootActorTest 
{	
	private Controller ctrl1;
	private Controller ctrl2;
	private Controller ctrl3;
	
	private ZootActor actor;
	
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
		ctrl1 = mock(Controller.class);
		ctrl2 = mock(RenderController.class);
		ctrl3 = mock(MockEmptyController.class);
		when(ctrl1.getPriority()).thenReturn(ControllerPriority.Normal);
		when(ctrl2.getPriority()).thenReturn(ControllerPriority.Normal);
		when(ctrl3.getPriority()).thenReturn(ControllerPriority.Normal);
		
		actor = new ZootActor();
	}
	
	@Test
	public void shouldCreateProperDefaultValuesAfterConstruction()
	{
		assertEquals("Should have default name", ZootActor.DEFAULT_NAME, actor.getName());
		assertNotNull("Should have state machine", actor.getStateMachine());
		assertEquals("State machine should listen to events", 1, actor.getListeners().size);
		assertTrue("State machine should listen to events", actor.getListeners().contains(actor.getStateMachine(), true));
	}
	
	@Test
	public void shouldUpdateControllersAndStateMachineTest()
	{		
		//when
		when(ctrl1.isEnabled()).thenReturn(true);
		when(ctrl2.isEnabled()).thenReturn(true);
		actor.addController(ctrl1);
		actor.addController(ctrl2);		
		actor.act(0.5f);
		
		//then
		verify(ctrl1, times(1)).onUpdate(0.5f, actor);
		verify(ctrl2, times(1)).onUpdate(0.5f, actor);
	}
	
	@Test
	public void shouldOnlyUpdateEnabledControllers()
	{		
		//when
		when(ctrl1.isEnabled()).thenReturn(true);
		when(ctrl2.isEnabled()).thenReturn(false);
		actor.addController(ctrl1);
		actor.addController(ctrl2);		
		actor.act(0.5f);
		
		//then
		verify(ctrl1, times(1)).onUpdate(0.5f, actor);
		verify(ctrl2, never()).onUpdate(0.5f, actor);		
	}
	
    @Test
    public void shouldProperlyAddAndRemoveActorTypes()
    {        
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
    public void shouldHaveEmptyControllersListAfterCreation()
    {
    	assertEquals("After creation controller list should be empty", 0, actor.getAllControllers().size());
    }
    
	@Test
	public void shouldProperlyAddControllers()
	{		
		//when
		actor.addController(ctrl1);
		
		//then
		assertEquals("Controller should be added immediatelly", 1, actor.getAllControllers().size());
		assertEquals(ctrl1, actor.getAllControllers().get(0));
		
		//when
		actor.addController(ctrl2);
		
		//then
		assertEquals("Controller should be added immediatelly", 2, actor.getAllControllers().size());
		assertEquals(ctrl2, actor.getAllControllers().get(1));
	}
	
	@Test
	public void shouldSortControllersAfterAdding()
	{
		//given
		Controller ctrl1 = mock(Controller.class);
		when(ctrl1.getPriority()).thenReturn(ControllerPriority.Low);
		Controller ctrl2 = mock(Controller.class);
		when(ctrl2.getPriority()).thenReturn(ControllerPriority.Normal);
		Controller ctrl3 = mock(Controller.class);
		when(ctrl3.getPriority()).thenReturn(ControllerPriority.High);
		
		//when
		actor.addController(ctrl1);
		actor.addController(ctrl3);
		
		//then
		assertEquals(ControllerPriority.High, actor.getControllers(Controller.class).get(0).getPriority());
		assertEquals(ControllerPriority.Low, actor.getControllers(Controller.class).get(1).getPriority());
		
		//when
		actor.addController(ctrl2);
		
		//then
		assertEquals(ControllerPriority.High, actor.getControllers(Controller.class).get(0).getPriority());
		assertEquals(ControllerPriority.Normal, actor.getControllers(Controller.class).get(1).getPriority());
		assertEquals(ControllerPriority.Low, actor.getControllers(Controller.class).get(2).getPriority());
	}
		
	@Test
	public void shouldProperlyRemoveControllers()
	{		
		//when
		actor.addController(ctrl1);
		actor.addController(ctrl2);
		
		//then
		assertEquals(2, actor.getAllControllers().size());
		
		//when, then
		actor.removeController(ctrl1);
		assertEquals(1, actor.getAllControllers().size());

		//when, then
		assertEquals(1, actor.getAllControllers().size());
		assertEquals(ctrl2, actor.getAllControllers().get(0));
		
		//when, then		
		actor.removeController(ctrl2);
		assertEquals(0, actor.getAllControllers().size());
	}	
	
	@Test
	public void shouldReturnSingleController()
	{
		//given
		Controller ctrl = mock(Controller.class);
		
		//when
		actor.addController(ctrl);
		
		//then
		assertEquals(ctrl, actor.getSingleController(Controller.class));		
	}
	
	@Test(expected = ZootControllerNotFoundException.class)
	public void shouldThrowIfSingleControllerIsNotFound()
	{
		actor.getSingleController(Controller.class);
		//throw
	}
	
	@Test(expected = ZootDuplicatedControllerException.class)
	public void shouldThrowIfThereAreMoreThanOneSingleControllersFound()
	{
		//given
		Controller ctrl1 = mock(Controller.class);
		Controller ctrl2 = mock(Controller.class);
		
		//when
		actor.addController(ctrl1);
		actor.addController(ctrl2);
		
		//then throw
		actor.getSingleController(Controller.class);		
	}
	
	@Test
	public void shouldReturnSingleControllerWhenTrying()
	{
		//given
		Controller ctrl = mock(Controller.class);
		
		//when
		actor.addController(ctrl);
		
		//then
		assertEquals(ctrl, actor.tryGetSingleController(Controller.class));		
	}
	
	@Test
	public void shouldNotThrowIfSingleControllerIsNotFoundWhenTrying()
	{
		assertNull(actor.tryGetSingleController(Controller.class));
	}
	
	@Test(expected = ZootDuplicatedControllerException.class)
	public void shouldThrowIfThereAreMoreThanOneSingleControllersFoundWhenTryingToGetController()
	{
		//given
		Controller ctrl1 = mock(Controller.class);
		Controller ctrl2 = mock(Controller.class);
		
		//when
		actor.addController(ctrl1);
		actor.addController(ctrl2);
		
		//then throw
		actor.tryGetSingleController(Controller.class);		
	}
	
	@Test
	public void shouldDoNothingWhenRemovingControllersFromActorWithNoControllers()
	{
		assertEquals(0, actor.getAllControllers().size());
		
		actor.removeController(ctrl1);
		actor.removeController(ctrl2);
		assertEquals(0, actor.getAllControllers().size());
		
		actor.act(0.0f);
		assertEquals(0, actor.getAllControllers().size());
	}
		
	@Test
	public void shouldInvokeControllerOnAddMethodWhenAddingControllerToActor()
	{		
		//when
		actor.addController(ctrl1);
		actor.addController(ctrl2);	
				
		//then
		verify(ctrl1, times(1)).onAdd(actor);
		verify(ctrl2, times(1)).onAdd(actor);
	}
	
	@Test
	public void shouldInvokeControllerOnRemoveMethodWhenRemovingControllersFromActor()
	{
		//given
		actor.addController(ctrl1);
		actor.addController(ctrl2);
		
		//then
		verify(ctrl1, times(1)).onAdd(actor);
		verify(ctrl2, times(1)).onAdd(actor);
		
		//when
		actor.removeController(ctrl1);
		actor.removeController(ctrl2);	
				
		//then
		verify(ctrl1, times(1)).onRemove(actor);
		verify(ctrl2, times(1)).onRemove(actor);
	}
	
	@Test	
	public void shouldNotifyChangeListenerWhenActorChanges()
	{
		//given
		ChangeListenerController controller = mock(ChangeListenerController.class);
				
		//when
		when(controller.isEnabled()).thenReturn(true);
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
		RenderController renderCtrl1 = spy(new RenderControllerMock1());
		RenderController renderCtrl2 = spy(new RenderControllerMock2());
		ChangeListenerController changeListenerController = mock(ChangeListenerController.class);
		when(changeListenerController.getPriority()).thenReturn(ControllerPriority.Normal);
		
		//when
		when(renderCtrl1.isEnabled()).thenReturn(true);
		when(renderCtrl2.isEnabled()).thenReturn(true);
		when(changeListenerController.isEnabled()).thenReturn(true);
		actor.addController(renderCtrl1);
		actor.addController(changeListenerController);
		actor.addController(renderCtrl2);
		actor.act(0.0f);
		
		//then
		verify(renderCtrl1, times(0)).onRender(anyObject(), anyFloat(), anyObject(), anyFloat());
		verify(renderCtrl2, times(0)).onRender(anyObject(), anyFloat(), anyObject(), anyFloat());
		
		//when
		actor.draw(batch, 1.0f);
				
		//then
		verify(renderCtrl1, times(1)).onRender(eq(batch), eq(parentAlpha), anyObject(), anyFloat());
		verify(renderCtrl2, times(1)).onRender(eq(batch), eq(parentAlpha), anyObject(), anyFloat());
		
		//when
		actor.draw(batch, 1.0f);
		
		//then
		verify(renderCtrl1, times(2)).onRender(eq(batch), eq(parentAlpha), anyObject(), anyFloat());
		verify(renderCtrl2, times(2)).onRender(eq(batch), eq(parentAlpha), anyObject(), anyFloat());
	}
	
	@Test
	public void shouldOnlyExecuteEnabledRenderControllersOnDraw()
	{
		//given
		final float parentAlpha = 1.0f;
		final Batch batch = mock(Batch.class);
		
		RenderController renderCtrl1 = spy(new RenderControllerMock1());
		RenderController renderCtrl2 = spy(new RenderControllerMock2());
		
		//when
		when(renderCtrl1.isEnabled()).thenReturn(true);
		when(renderCtrl2.isEnabled()).thenReturn(false);
		actor.addController(renderCtrl1);
		actor.addController(renderCtrl2);
		
		//when
		actor.draw(batch, 1.0f);
				
		//then
		verify(renderCtrl1, times(1)).onRender(eq(batch), eq(parentAlpha), anyObject(), anyFloat());
		verify(renderCtrl2, never()).onRender(eq(batch), eq(parentAlpha), anyObject(), anyFloat());
	}
	
	@Test
	public void shouldHaveDefaultStateMachine()
	{
		assertNotNull(actor.getStateMachine());
		assertEquals(actor, actor.getStateMachine().getOwner());
	}
		
	@Test
	public void shouldNotThrowIfBaseAndDerivedControllersAreAdded()
	{
		//given
		Controller ctrl1 = new MockBaseController();
		Controller ctrl2 = new MockDerivedController();
		
		//when
		actor.addController(ctrl1);
		actor.addController(ctrl2);
		
		//then
		assertEquals(ctrl1, actor.getControllers(MockBaseController.class).get(0));
		assertEquals(ctrl2, actor.getControllers(MockDerivedController.class).get(0));
	}
	
	@Test	
	public void shuoldNotThrowIfControllersAreNotFound()
	{
		assertTrue(actor.getControllers(SimpleController.class).isEmpty());
	}
					
	@Test
	public void shouldNotThrowOnControllerActionIfControllerIsNotFoundTest()
	{
		actor.controllersAction(SimpleController.class, (ctrl) -> {});
	}
	
	@Test
	public void shouldExecuteControllersAction()
	{
		//given
		SimpleController ctrl = new SimpleController();
		
		//when
		actor.addController(ctrl);
		actor.controllersAction(SimpleController.class, (c) -> c.set(100));
		
		//then
		assertEquals(100, ctrl.get());
	}
		
	@Test
	public void shouldReturnProperValueWhenEvaluatingAllControllers()
	{
		//given
		SimpleController ctrl1 = new SimpleController();
		SimpleController ctrl2 = new SimpleController();
		
		//when
		ctrl1.set(100);
		ctrl2.set(50);
		actor.addController(ctrl1);
		actor.addController(ctrl2);
		
		//then
		assertTrue("Should be true if all controllers qualify", actor.controllersAllMatch(SimpleController.class, (c) -> c.get() >= 50));
		assertFalse("Should be false if any controller does not qualify", actor.controllersAllMatch(SimpleController.class, (c) -> c.get() <= 50));
	}
	
	@Test
	public void shouldEvaluateToFalseWhenNoControllersAreFoundForAllMatch()
	{
		assertFalse(actor.controllersAllMatch(SimpleController.class, (c) -> true));
	}
	
	@Test
	public void shouldReturnProperValueWhenEvaluatingAnyController()
	{
		//given
		SimpleController ctrl1 = new SimpleController();
		SimpleController ctrl2 = new SimpleController();
		
		//when
		ctrl1.set(100);
		ctrl2.set(50);
		actor.addController(ctrl1);
		actor.addController(ctrl2);
		
		//then
		assertTrue("Should be true if any controller qualify", actor.controllersAnyMatch(SimpleController.class, (c) -> c.get() >= 50));
		assertTrue("Should be true if any controller qualify", actor.controllersAnyMatch(SimpleController.class, (c) -> c.get() <= 50));
		assertFalse("Should be false if none controllers qualify", actor.controllersAnyMatch(SimpleController.class, (c) -> c.get() == 00));		
	}
	
	@Test
	public void shouldEvaluateToFalseWhenNoControllersAreFoundForAnyMatch()
	{
		assertFalse(actor.controllersAnyMatch(SimpleController.class, (c) -> true));
	}
	
	@Test
	public void shouldSortControllersAfterAddingThemToActor()
	{
		//given
		Controller ctrl1 = mock(Controller.class);
		Controller ctrl2 = mock(Controller.class);
		Controller ctrl3 = mock(Controller.class);
		when(ctrl1.getPriority()).thenReturn(ControllerPriority.Normal);
		when(ctrl2.getPriority()).thenReturn(ControllerPriority.Low);
		when(ctrl3.getPriority()).thenReturn(ControllerPriority.High);		
				
		//when
		actor.addControllers(Arrays.asList(ctrl1, ctrl2, ctrl3));
		
		//then
		List<Controller> controllers = actor.getAllControllers();
		assertEquals(3, controllers.size());
		assertTrue(ctrl3 == controllers.get(0));
		assertTrue(ctrl1 == controllers.get(1));
		assertTrue(ctrl2 == controllers.get(2));
	}
	
	@Test
	public void shouldAddControllersInPriorityOrder()
	{
		//given
		Controller ctrl1 = mock(Controller.class);
		Controller ctrl2 = mock(Controller.class);
		Controller ctrl3 = mock(Controller.class);
		when(ctrl1.getPriority()).thenReturn(ControllerPriority.Normal);
		when(ctrl2.getPriority()).thenReturn(ControllerPriority.Low);
		when(ctrl3.getPriority()).thenReturn(ControllerPriority.High);		
		
		//when
		InOrder inOrder = inOrder(ctrl1, ctrl2, ctrl3);
		actor.addControllers(Arrays.asList(ctrl2, ctrl3, ctrl1));
		
		//then
		inOrder.verify(ctrl3).onAdd(actor);
		inOrder.verify(ctrl1).onAdd(actor);
		inOrder.verify(ctrl2).onAdd(actor);
	}
		
	@Test
	public void shouldNotThrowWhenAddingDuplicatedControllers()
	{
		//given
		Controller ctrl1 = new ControllerAdapter();
		Controller ctrl2 = new ControllerAdapter();
		
		//when
		actor.addControllers(Arrays.asList(ctrl1, ctrl2));
		
		//ok
	}
	
	@Test(expected = ZootDuplicatedControllerException.class)
	public void shouldThrowWhenAddingDuplicatedSingletonController()
	{
		//given
		Controller ctrl1 = mock(Controller.class);
		when(ctrl1.isSingleton()).thenReturn(true);
		Controller ctrl2 = mock(Controller.class);
		when(ctrl2.isSingleton()).thenReturn(true);
				
		//when
		actor.addController(ctrl1);
		actor.addController(ctrl2);
		
		//throw
	}
	
	@Test(expected = ZootDuplicatedControllerException.class)
	public void shouldThrowWhenAddingDuplicatedSingletonControllers()
	{
		//given
		Controller ctrl1 = mock(Controller.class);
		when(ctrl1.isSingleton()).thenReturn(true);
		Controller ctrl2 = mock(Controller.class);
		when(ctrl2.isSingleton()).thenReturn(true);
				
		//when
		actor.addController(ctrl1);
		actor.addControllers(Arrays.asList(ctrl2));
		
		//throw		
	}
	
	@Test(expected = ZootDuplicatedControllerException.class)
	public void shouldThrowWhenAddingDuplicatedSingletonControllersFromTheSameCollection()
	{
		//given
		Controller ctrl1 = mock(Controller.class);
		when(ctrl1.isSingleton()).thenReturn(true);
		Controller ctrl2 = mock(Controller.class);
		when(ctrl2.isSingleton()).thenReturn(true);
				
		//when
		actor.addControllers(Arrays.asList(ctrl1, ctrl2));
		
		//throw		
	}
	
	@Test
	public void shouldNotThrowWhenAddingDuplicatedControllerType()
	{
		//given
		Controller ctrl1 = new ControllerAdapter();
		Controller ctrl2 = new ControllerAdapter();
		
		//when
		actor.addController(ctrl1);
		actor.addController(ctrl2);
		
		//ok
	}
	
	@Test
	public void shouldRemoveAllControllersWhenRemovingActor()
	{
		//given
		actor.addControllers(Arrays.asList(ctrl1, ctrl2, ctrl3));
		
		//when
		actor.remove();
		
		//then
		verify(ctrl1, times(1)).onRemove(actor);
		verify(ctrl2, times(1)).onRemove(actor);
		verify(ctrl3, times(1)).onRemove(actor);
		assertEquals(0, actor.getAllControllers().size());
	}
	
	@Test
	public void shouldRemoveAllControllersInDescendingPriorityOrder()
	{
		//given
		when(ctrl1.getPriority()).thenReturn(ControllerPriority.Normal);
		when(ctrl2.getPriority()).thenReturn(ControllerPriority.High);
		when(ctrl3.getPriority()).thenReturn(ControllerPriority.Low);		
		InOrder inOrder = inOrder(ctrl1, ctrl2, ctrl3);
		
		//when
		actor.addControllers(Arrays.asList(ctrl1, ctrl2, ctrl3));
		actor.remove();
		
		//then
		inOrder.verify(ctrl3).onRemove(actor);
		inOrder.verify(ctrl1).onRemove(actor);
		inOrder.verify(ctrl2).onRemove(actor);
	}
	
	@Test
	public void shouldRemoveAllControllers()
	{
		//given
		actor.addControllers(Arrays.asList(ctrl1, ctrl2, ctrl3));
		
		//when
		actor.removeAllControllers();
		
		//then
		verify(ctrl1, times(1)).onRemove(actor);
		verify(ctrl2, times(1)).onRemove(actor);
		verify(ctrl3, times(1)).onRemove(actor);
		assertEquals(0, actor.getAllControllers().size());
	}
	
	@Test
	public void shouldSetId()
	{
		assertEquals(0, actor.getId());
		
		actor.setId(1);
		assertEquals(1, actor.getId());
	}
	
	@Test
	public void shouldSetGid()
	{
		assertEquals(-1, actor.getGid());
		
		actor.setGid(1);
		assertEquals(1, actor.getGid());		
	}
	
	@Test
	public void shuouldSetOpacity()
	{
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
		MockBaseController baseCtrl = new MockBaseController();
		MockDerivedController derivedCtrl = new MockDerivedController();
		SimpleController simpleCtrl = new SimpleController();
				
		//when
		actor.addController(baseCtrl);
		actor.addController(derivedCtrl);
		actor.addController(simpleCtrl);
		
		//then
		List<MockBaseController> ctrls = actor.getControllers(MockBaseController.class);
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
		MockBaseController ctrl1 = mock(MockBaseController.class);
		MockDerivedController ctrl2 = mock(MockDerivedController.class);		
		SimpleController ctrl3 = mock(SimpleController.class);
		
		when(ctrl1.getPriority()).thenReturn(ControllerPriority.Normal);
		when(ctrl2.getPriority()).thenReturn(ControllerPriority.Normal);
		when(ctrl3.getPriority()).thenReturn(ControllerPriority.Normal);
		
		//when
		actor.addController(ctrl1);
		actor.addController(ctrl2);
		actor.addController(ctrl3);
		actor.controllersAction(MockBaseController.class, ctrl -> ctrl.getBaseParam());
		
		//then
		verify(ctrl1).getBaseParam();
		verify(ctrl2).getBaseParam();
		verify(ctrl3).onAdd(actor);
	}
		
	@Test
	public void shouldSetScene()
	{
		//given
		ZootScene scene = mock(ZootScene.class);
		
		//when
		actor.setScene(scene);
		
		//then
		assertEquals(scene, actor.getScene());
	}
	
	@Test
	public void shouldReturnValidString()
	{		
		//when
		actor.setName("Name");
		
		//then		
		assertEquals("Name", actor.toString());
	}
}
