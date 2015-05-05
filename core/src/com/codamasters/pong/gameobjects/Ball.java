package com.codamasters.pong.gameobjects;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactFilter;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class Ball implements ContactFilter, ContactListener{
	
	private Body body;
	private Fixture fixture;
	private World world;
	public final float RADIUS = .2f;
	
	public Ball(World world, float x, float y) {
		
		BodyDef bodyDef = new BodyDef();
		FixtureDef fixtureDef = new FixtureDef();
		this.world = world;
		
		// body definition
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.set(x, y);

		// ball shape
		CircleShape ballShape = new CircleShape();
		ballShape.setRadius(RADIUS);
	
		// fixture definition
		fixtureDef.shape = ballShape;
		fixtureDef.friction = 0;
		fixtureDef.restitution = 1;
		fixtureDef.density = 0;
		
		body = world.createBody(bodyDef);
		fixture = body.createFixture(fixtureDef);
		
		ballShape.dispose();
	}
	
	public Body getBody(){
		return body;
	}
	
	public Fixture getFixture(){
		return fixture;
	}

	@Override
	public void beginContact(Contact contact) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void endContact(Contact contact) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean shouldCollide(Fixture fixtureA, Fixture fixtureB) {
		// TODO Auto-generated method stub
		return false;
	}

}
