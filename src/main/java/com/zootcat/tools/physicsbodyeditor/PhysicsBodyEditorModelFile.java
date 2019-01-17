package com.zootcat.tools.physicsbodyeditor;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.zootcat.tools.physicsbodyeditor.PhysicsBodyEditorModel.CircleModel;
import com.zootcat.tools.physicsbodyeditor.PhysicsBodyEditorModel.PolygonModel;
import com.zootcat.tools.physicsbodyeditor.PhysicsBodyEditorModel.RigidBodyModel;

/**
 * Based on Physics Body Editor loader by Rasmus Praestholm (Cervator) <br/>
 * @see <a href="https://github.com/MovingBlocks/box2d-editor/blob/develop/loader-libgdx/src/aurelienribon/bodyeditor/BodyEditorLoader.java">GitHub Link</a>
 * @author Aurelien Ribon | http://www.aurelienribon.com
 * @author Cream
 */
//TODO BETA refactor to use tags: https://github.com/Cream256/zoot/issues/6
public class PhysicsBodyEditorModelFile
{
	private PhysicsBodyEditorModel model;
	
	public PhysicsBodyEditorModelFile(FileHandle fileHandle)
	{
		model = readJson(fileHandle.readString());	
	}
	
	public PhysicsBodyEditorModel getModel()
	{
		return model;
	}
	
	private PhysicsBodyEditorModel readJson(String str) {
		PhysicsBodyEditorModel model = new PhysicsBodyEditorModel();
		
		JsonValue map = new JsonReader().parse(str);
		JsonValue bodyElem = map.getChild("rigidBodies");
		for (; bodyElem != null; bodyElem = bodyElem.next()) {
			RigidBodyModel rbModel = readRigidBody(bodyElem);
			model.addRigidBodyModel(rbModel);
		}

		return model;
	}
	
	private RigidBodyModel readRigidBody(JsonValue bodyElem) {
		RigidBodyModel rbModel = new RigidBodyModel();
		rbModel.name = bodyElem.getString("name");
		rbModel.imagePath = bodyElem.getString("imagePath");

		JsonValue originElem = bodyElem.get("origin");
		rbModel.origin.x = originElem.getFloat("x");
		rbModel.origin.y = originElem.getFloat("y");

		// polygons
		JsonValue polygonsElem = bodyElem.getChild("polygons");	
		for (; polygonsElem != null ;polygonsElem = polygonsElem.next()){

			PolygonModel polygon = new PolygonModel();
			rbModel.polygons.add(polygon);

			JsonValue vertexElem = polygonsElem.child();
			for (; vertexElem != null; vertexElem = vertexElem.next()) {
				float x = vertexElem.getFloat("x");
				float y = vertexElem.getFloat("y");
				polygon.vertices.add(new Vector2(x, y));
			}

			polygon.buffer = new Vector2[polygon.vertices.size()];
		}

		// circles
		JsonValue circleElem = bodyElem.getChild("circles");

		for (; circleElem != null; circleElem = circleElem.next()) {
			CircleModel circle = new CircleModel();
			rbModel.circles.add(circle);

			circle.center.x = circleElem.getFloat("cx");
			circle.center.y = circleElem.getFloat("cy");
			circle.radius = circleElem.getFloat("r");
		}
		return rbModel;
	}
}
