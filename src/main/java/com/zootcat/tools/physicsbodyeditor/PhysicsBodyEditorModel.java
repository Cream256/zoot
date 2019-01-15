package com.zootcat.tools.physicsbodyeditor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.zootcat.exceptions.RuntimeZootException;

/**
 * Based on Physics Body Editor loader by Rasmus Praestholm (Cervator) <br/>
 * @see <a href="https://github.com/MovingBlocks/box2d-editor/blob/develop/loader-libgdx/src/aurelienribon/bodyeditor/BodyEditorLoader.java">GitHub Link</a>
 * @author Aurelien Ribon | http://www.aurelienribon.com
 * @author Cream
 */
public class PhysicsBodyEditorModel
{	
	public final Map<String, RigidBodyModel> rigidBodies = new HashMap<String, RigidBodyModel>();	
	public final List<Vector2> vectorPool = new ArrayList<Vector2>();
	public final PolygonShape polygonShape = new PolygonShape();
	public final CircleShape circleShape = new CircleShape();
	public final Vector2 vec = new Vector2();	
	
	public static class RigidBodyModel
	{
		public String name;
		public String imagePath;
		public final Vector2 origin = new Vector2();
		public final List<PolygonModel> polygons = new ArrayList<PolygonModel>();
		public final List<CircleModel> circles = new ArrayList<CircleModel>();
	}
	
	public static class PolygonModel {
		public final List<Vector2> vertices = new ArrayList<Vector2>();
		public Vector2[] buffer; // used to avoid allocation in attachFixture()
	}

	public static class CircleModel {
		public final Vector2 center = new Vector2();
		public float radius;
	}	
	
	public void attachFixture(Body body, String name, FixtureDef fd, float scale) {
		RigidBodyModel rbModel = rigidBodies.get(name);
		if (rbModel == null) throw new RuntimeZootException("Name '" + name + "' was not found.");

		Vector2 origin = vec.set(rbModel.origin).scl(scale);

		for (int i=0, n=rbModel.polygons.size(); i<n; i++) {
			PolygonModel polygon = rbModel.polygons.get(i);
			Vector2[] vertices = polygon.buffer;

			for (int ii=0, nn=vertices.length; ii<nn; ii++) {
				vertices[ii] = newVector().set(polygon.vertices.get(ii)).scl(scale);
				vertices[ii].sub(origin);
			}

			polygonShape.set(vertices);
			fd.shape = polygonShape;
			body.createFixture(fd);

			for (int ii=0, nn=vertices.length; ii<nn; ii++) {
				freeVector(vertices[ii]);
			}
		}

		for (int i=0, n=rbModel.circles.size(); i<n; i++) {
			CircleModel circle = rbModel.circles.get(i);
			Vector2 center = newVector().set(circle.center).scl(scale);
			float radius = circle.radius * scale;

			circleShape.setPosition(center);
			circleShape.setRadius(radius);
			fd.shape = circleShape;
			body.createFixture(fd);

			freeVector(center);
		}
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
