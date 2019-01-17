package com.zootcat.tools.physicsbodyeditor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Disposable;
import com.zootcat.controllers.physics.PhysicsBodyController;
import com.zootcat.exceptions.RuntimeZootException;
import com.zootcat.scene.ZootActor;

/**
 * Based on Physics Body Editor loader by Rasmus Praestholm (Cervator) <br/>
 * @see <a href="https://github.com/MovingBlocks/box2d-editor/blob/develop/loader-libgdx/src/aurelienribon/bodyeditor/BodyEditorLoader.java">GitHub Link</a>
 * @author Aurelien Ribon | http://www.aurelienribon.com
 * @author Cream
 */
public class PhysicsBodyEditorModel implements Disposable
{		
	public static class RigidBodyModel
	{
		public String name;
		public String imagePath;
		public final Vector2 origin = new Vector2();
		public final List<PolygonModel> polygons = new ArrayList<PolygonModel>();
		public final List<CircleModel> circles = new ArrayList<CircleModel>();
		public final List<Fixture> attachedFixtures = new ArrayList<Fixture>();
	}
	
	public static class PolygonModel {
		public final List<Vector2> vertices = new ArrayList<Vector2>();
		public Vector2[] buffer; // used to avoid allocation in attachFixture()
	}

	public static class CircleModel {
		public final Vector2 center = new Vector2();
		public float radius;
	}	
	
	private Map<String, RigidBodyModel> rigidBodies = new HashMap<String, RigidBodyModel>();	
	private List<Vector2> vectorPool = new ArrayList<Vector2>();
	private PolygonShape polygonShape = new PolygonShape();
	private CircleShape circleShape = new CircleShape();
	private Vector2 vec = new Vector2();
	
	public void addRigidBodyModel(RigidBodyModel model)
	{
		rigidBodies.put(model.name, model);
	}
	
	public RigidBodyModel getRigidBodyModel(String modelName)
	{
		return rigidBodies.get(modelName);
	}
		
	public void attachFixture(ZootActor actor, String name, FixtureDef fd, float scaleX, float scaleY) {
		PhysicsBodyController physicsBodyCtrl = actor.getSingleController(PhysicsBodyController.class);
		
		RigidBodyModel rbModel = rigidBodies.get(name);
		if (rbModel == null) throw new RuntimeZootException("Name '" + name + "' was not found.");
				
		Vector2 origin = vec.set(rbModel.origin).scl(scaleX, scaleY);

		rbModel.attachedFixtures.clear();
		for (int i=0, n=rbModel.polygons.size(); i<n; i++) {
			PolygonModel polygon = rbModel.polygons.get(i);
			Vector2[] vertices = polygon.buffer;

			for (int ii=0, nn=vertices.length; ii<nn; ii++) {
				vertices[ii] = newVector().set(polygon.vertices.get(ii)).scl(scaleX, scaleY);
				vertices[ii].sub(origin);
			}

			polygonShape.set(vertices);
			fd.shape = polygonShape;
			
			Fixture newPolygonFixture = physicsBodyCtrl.addFixture(fd, actor);
			rbModel.attachedFixtures.add(newPolygonFixture);
			
			for (int ii=0, nn=vertices.length; ii<nn; ii++) {
				freeVector(vertices[ii]);
			}
		}

		for (int i=0, n=rbModel.circles.size(); i<n; i++) {
			CircleModel circle = rbModel.circles.get(i);
			Vector2 center = newVector().set(circle.center).scl(scaleX, scaleY);
			float radius = circle.radius * scaleX;

			circleShape.setPosition(center);
			circleShape.setRadius(radius);
			fd.shape = circleShape;
			
			Fixture newCircleFixture = physicsBodyCtrl.addFixture(fd, actor);
			rbModel.attachedFixtures.add(newCircleFixture);

			freeVector(center);
		}
	}
	
	@Override
	public void dispose()
	{
		this.rigidBodies.clear();
		this.rigidBodies = null;
		this.circleShape.dispose();
		this.circleShape = null;
		this.polygonShape.dispose();
		this.polygonShape = null;
		this.vec = null;
		this.vectorPool.clear();
		this.vectorPool = null;
	}
	
	/**
	 * Get the image path attached to the given name.
	 */
	public String getImagePath(String name) {
		RigidBodyModel rbModel = rigidBodies.get(name);
		if (rbModel == null) throw new RuntimeZootException("Name '" + name + "' was not found.");
		return rbModel.imagePath;
	}

	/**
	 * Gets the origin point attached to the given name. Since the point is
	 * normalized in [0,1] coordinates, it needs to be scaled to your body
	 * size. Warning: this method returns the same Vector2 object each time, so
	 * copy it if you need it for later use.
	 */
	public Vector2 getOrigin(String name, float scale) {
		RigidBodyModel rbModel = rigidBodies.get(name);
		if (rbModel == null) throw new RuntimeZootException("Name '" + name + "' was not found.");

		return vec.set(rbModel.origin).scl(scale);
	}
	
	private Vector2 newVector() {
		return vectorPool.isEmpty() ? new Vector2() : vectorPool.remove(0);
	}

	private void freeVector(Vector2 v) {
		vectorPool.add(v);
	}
}
