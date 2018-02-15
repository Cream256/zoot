package com.zootcat.hud;

import java.util.function.Supplier;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class ZootDynamicLabel extends Label
{
	public Supplier<Object> supplier;
	
	public ZootDynamicLabel(Skin skin, Supplier<Object> supplier)
	{
		super("", skin);
		this.supplier = supplier;
		addAction(new Action() {

			@Override
			public boolean act(float delta)
			{
				setText(supplier.get().toString());
				return false;
			}});
	}
}
