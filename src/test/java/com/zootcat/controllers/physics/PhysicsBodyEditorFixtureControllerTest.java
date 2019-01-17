package com.zootcat.controllers.physics;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Matchers.*;
import static org.mockito.Matchers.anyFloat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.zootcat.assets.ZootAssetManager;
import com.zootcat.controllers.factory.ControllerAnnotations;
import com.zootcat.scene.ZootScene;
import com.zootcat.testing.ZootActorStub;
import com.zootcat.testing.ZootTestUtils;
import com.zootcat.tools.physicsbodyeditor.PhysicsBodyEditorModel;

public class PhysicsBodyEditorFixtureControllerTest
{	
	private static final float EXPECTED_FIXTURE_SCALE = 0.5f;
	private static final String EXPECTED_FIXTURE_NAME = "test01";
	
	@Mock private ZootScene scene;
	@Mock private Body expectedBody;
	@Mock private ZootAssetManager assetManager;	
	@Mock private PhysicsBodyController physicsBodyCtrl;
	@Mock private PhysicsBodyEditorModel expectedFixtureModel;		
	private ZootActorStub actor;
	private String expectedFilePath;
	private PhysicsBodyEditorFixtureController ctrl;
				
	@BeforeClass
	public static void init()
	{
		Box2D.init();
	}
	
	@Before
	public void setup()
	{
		MockitoAnnotations.initMocks(this);
		when(physicsBodyCtrl.getBody()).thenReturn(expectedBody);
		
		expectedFilePath = ZootTestUtils.getResourcePath("physics/PhysicsBodyEditorModel.json", this);
		when(assetManager.get(expectedFilePath, PhysicsBodyEditorModel.class)).thenReturn(expectedFixtureModel);
				
		actor = new ZootActorStub();
		actor.addController(physicsBodyCtrl);
				
		ctrl = new PhysicsBodyEditorFixtureController();
		ControllerAnnotations.setControllerParameter(ctrl, "fileName", expectedFilePath);
		ControllerAnnotations.setControllerParameter(ctrl, "fixtureName", EXPECTED_FIXTURE_NAME);
		ControllerAnnotations.setControllerParameter(ctrl, "fixtureScale", EXPECTED_FIXTURE_SCALE);
		ControllerAnnotations.setControllerParameter(ctrl, "scene", scene);
		ControllerAnnotations.setControllerParameter(ctrl, "assetManager", assetManager);
	}
	
	@Test
	public void shouldHaveProperDefaultValues()
	{
		assertEquals(0.0f, ctrl.density, 1.0f);
		assertEquals(0.0f, ctrl.friction, 0.2f);
		assertEquals(0.0f, ctrl.restitution, 0.0f);
		assertEquals(0.0f, ctrl.offsetX, 0.0f);
		assertEquals(0.0f, ctrl.offsetY, 0.0f);
		assertEquals(0.0f, ctrl.width, 0.0f);
		assertEquals(0.0f, ctrl.height, 0.0f);
		assertFalse(ctrl.sensor);
		assertEquals("", ctrl.category);
		assertEquals("", ctrl.mask);	
	}
		
	@Test
	public void shouldLoadModelFromAssetManagerOnInit()
	{
		ctrl.init(actor);
		verify(assetManager).get(expectedFilePath, PhysicsBodyEditorModel.class);
	}
	
	@Test
	public void shouldAttachLoadedModelToActorsPhysicsBody()
	{
		ctrl.init(actor);
		ctrl.onAdd(actor);
		
		verify(expectedFixtureModel).attachFixture(eq(actor), anyString(), any(), anyFloat());
	}
	
	@Test
	public void shouldUseProvidedFixtureScale()
	{
		ctrl.init(actor);
		ctrl.onAdd(actor);
		
		verify(expectedFixtureModel).attachFixture(any(), anyString(), any(), eq(EXPECTED_FIXTURE_SCALE));
	}
	
	@Test
	public void shouldUseProvidedFixtureName()
	{
		ctrl.init(actor);
		ctrl.onAdd(actor);
		
		verify(expectedFixtureModel).attachFixture(any(), eq(EXPECTED_FIXTURE_NAME), any(), anyFloat());		
	}
	
}
