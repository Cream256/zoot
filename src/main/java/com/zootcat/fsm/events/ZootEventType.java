package com.zootcat.fsm.events;

public enum ZootEventType implements ZootEventTypeEnum
{
	None, 
	InitEvent, 
	Update, 
	WalkLeft, 
	WalkRight, 
	RunLeft, 
	RunRight,
	FlyLeft,
	FlyRight,
	JumpUp, 
	JumpForward, 
	Up, 
	Down, 
	Stop, 
	Hurt,
	Fall, 
	Attack, 
	Collide, 
	Dead, 
	Ground, 
	NoGroundAhead,
	InAir, 
	TriggerOn, 
	TriggerOff, 
	Grab, 
	GrabSide, 
	Obstacle;
}