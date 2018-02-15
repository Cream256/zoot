package com.zootcat.hud;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Tree;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

public class ZootPropertiesTree extends Tree
{
	private static final int TEXT_ALIGN = Align.left;
	private static final int INDENT = -60;
	private static final int ROW_HEIGHT = 20;
	
	private Skin skin;
	private Table table;
		
	public ZootPropertiesTree(String treeName, Skin skin, ZootDebugWindow window)
	{
		super(skin);
		this.skin = skin;
		
		//tree - root node
		Label treeLabel = new Label(treeName, skin);
		treeLabel.setColor(Color.RED);		
		Node rootNode = new Node(treeLabel);
				
		//table - child node		
		table = new Table(skin);
		table.setDebug(false);
		table.align(Align.topLeft);
		table.padLeft(INDENT);
		table.defaults().height(ROW_HEIGHT).minHeight(ROW_HEIGHT).maxHeight(ROW_HEIGHT * 2); 

		Node childNode = new Node(table);
		
		//building tree
		add(rootNode);
		rootNode.add(childNode);
		
		//when tree is expanded / collapsed, update parent window
		addListener(new ClickListener() 
		{
			@Override
			public void clicked (InputEvent event, float x, float y) 
			{				
				window.pack();
				window.setDefaultPos();
			}			
		});
	}
	
	public void addProperty(String name, String value)
	{
		Label valueLabel = new Label(value, skin);
		valueLabel.setAlignment(TEXT_ALIGN);
		valueLabel.setWrap(true);
		
		Label nameLabel = new Label(name, skin);
		nameLabel.setAlignment(TEXT_ALIGN);
		nameLabel.setWrap(true);
		
		addProperty(nameLabel, valueLabel);
	}
	
	public void addProperty(String name, Label value)
	{
		Label nameLabel = new Label(name, skin);
		nameLabel.setAlignment(TEXT_ALIGN);
		nameLabel.setWrap(true);
		addProperty(nameLabel, value);
	}
	
	public void addProperty(Label name, Label value)
	{		
		table.add(name).minWidth(100).maxWidth(300).prefWidth(200);
		table.add(value).minWidth(200).maxWidth(350).prefWidth(300);
		table.row();
	}
}
