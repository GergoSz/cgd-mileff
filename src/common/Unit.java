package common;

import java.util.ArrayList;

import org.joml.Vector2f;

import engine.Texture2D;
import engine.Timer;
import engine.Vector2D;

public abstract class Unit {
	
	protected int health;
	protected int attack;
	protected float detectionRange;
	protected float speed;
	protected boolean facingRight;
	
	public Timer attackTimer;
	protected float attackSpeed;
	protected int framesPassed;
	protected float elapsedTime;
	protected boolean attacked;
	
	protected int dyingFrames;
	
	protected Unit targetUnit;

	// Actual frame
	protected int m_iActualFrame;

	// Position of the sprite
	protected Vector2D m_vSpritePosition;

	// The last time the animation was update
	protected long m_iLastUpdate;

	// The number of frames per second
	protected int m_iFps;

	protected float scale;

	public enum CurrentAction {
		Idle, Walking, Attacking, Dying
	}

	protected CurrentAction currentAction;

	public enum Animation {
		Idle, Walk, Attack, Oof, Dying
	}

	protected Animation animation;

	public Unit() {
		m_iActualFrame = 1;
		m_iLastUpdate = System.currentTimeMillis();
		m_iFps = 15; // default FPS animation rate
		animation = Animation.Idle;
		currentAction = CurrentAction.Idle;
		facingRight = true;
		detectionRange = 100;
		speed = 1.2f;
		scale = 1;
		
		attackTimer = new Timer();
		attackTimer.init();
		attackSpeed = 2;
		framesPassed = 0;
		elapsedTime = 0;
		targetUnit = null;
		attacked = true;
		
		dyingFrames = 0;
	}

	public Unit(float x, float y) {
		m_iActualFrame = 1;
		m_iLastUpdate = System.currentTimeMillis();
		m_iFps = 15; // default FPS animation rate
		animation = Animation.Idle;
		currentAction = CurrentAction.Idle;
		facingRight = true;
		detectionRange = 100;
		speed = 1.2f;
		scale = 1;
		
		attackTimer = new Timer();
		attackTimer.init();
		attackSpeed = 2;
		framesPassed = 0;
		elapsedTime = 0;
		targetUnit = null;
		attacked = true;
		
		dyingFrames = 0;

		m_vSpritePosition = new Vector2D(x, y);
	}

	public Unit(float x, float y, float scale) {
		m_iActualFrame = 1;
		m_iLastUpdate = System.currentTimeMillis();
		m_iFps = 15; // default FPS animation rate
		animation = Animation.Idle;
		currentAction = CurrentAction.Idle;
		facingRight = true;
		detectionRange = 100;
		speed = 1.2f;
		this.scale = scale;
		
		attackTimer = new Timer();
		attackTimer.init();
		attackSpeed = 2;
		framesPassed = 0;
		elapsedTime = 0;
		targetUnit = null;
		attacked = true;
		
		dyingFrames = 0;

		m_vSpritePosition = new Vector2D(x, y);
	}
	
	/*public static void LoadDefaultTextures() {
		// idleAnimation
		String filenames = "textures/Golems/PNG/Golem_01/PNG Sequences/Idle/Golem_01_Idle_0";
		int numOfFrames = 12;
		LoadTextureGroup(filenames, numOfFrames, idle_vFrames, idle_vFramesFlipped);
		filenames = "textures/Golems/PNG/Golem_01/PNG Sequences/Walking/Golem_01_Walking_0";
		numOfFrames = 18;
		LoadTextureGroup(filenames, numOfFrames, walk_vFrames, walk_vFramesFlipped);
		filenames = "textures/Golems/PNG/Golem_01/PNG Sequences/Attacking/Golem_01_Attacking_0";
		numOfFrames = 12;
		LoadTextureGroup(filenames, numOfFrames, attack_vFrames, attack_vFramesFlipped);
		filenames = "textures/Golems/PNG/Golem_01/PNG Sequences/Hurt/Golem_01_Hurt_0";
		numOfFrames = 12;
		LoadTextureGroup(filenames, numOfFrames, oof_vFrames, oof_vFramesFlipped);
		filenames = "textures/Golems/PNG/Golem_01/PNG Sequences/Dying/Golem_01_Dying_0";
		numOfFrames = 15;
		LoadTextureGroup(filenames, numOfFrames, dying_vFrames, dying_vFramesFlipped);
	}*/

	/** Loads sprite textures */
	/*public void LoadTextures(String filenames, int numOfFrames) {
		if (numOfFrames == 1) {
			Texture2D tex = new Texture2D();
			tex.CreateTexture(filenames + ".png");
			idle_vFrames.add(tex);
		} else {
			// Loading single textures 
			for (int i = 0; i < numOfFrames; i++) {
				Texture2D tex = new Texture2D();

				if (0 <= i && i < 10) {
					tex.CreateTexture(filenames + "0" + (i) + ".png");
				} else {
					tex.CreateTexture(filenames + (i) + ".png");
				}

				idle_vFrames.add(tex);
			}
		}

	}*/

	/*public static void LoadTextureGroup(String filenames, int numOfFrames, ArrayList<Texture2D> list, ArrayList<Texture2D> flipped) {

		if (numOfFrames == 1) {
			Texture2D tex = new Texture2D();
			tex.CreateTexture(filenames + ".png");
			list.add(tex);
			
			Texture2D texFlip = new Texture2D();
			tex.CreateTextureFlipped(filenames + ".png");
			flipped.add(texFlip);
		} else {
			//Loading single textures
			for (int i = 0; i < numOfFrames; i++) {
				Texture2D tex = new Texture2D();

				if (0 <= i && i < 10) {
					tex.CreateTexture(filenames + "0" + (i) + ".png");
				} else {
					tex.CreateTexture(filenames + (i) + ".png");
				}

				list.add(tex);
			}
			
			for (int i = 0; i < numOfFrames; i++) {
				Texture2D tex = new Texture2D();

				if (0 <= i && i < 10) {
					tex.CreateTextureFlipped(filenames + "0" + (i) + ".png");
				} else {
					tex.CreateTextureFlipped(filenames + (i) + ".png");
				}

				flipped.add(tex);
			}
		}
	}*/

	public float getScale() {
		return this.scale;
	}

	public void setScale(float scale) {
		this.scale = scale;
	}

	public Animation getAnimation() {
		return animation;
	}

	public void setAnimation(Animation animation) {
		if (!this.animation.equals(animation)) {
			this.animation = animation;
			Reset();
		}
	}

	public CurrentAction getCuttentAction() {
		return currentAction;
	}

	public void setCurrentAction(CurrentAction action) {
		this.currentAction = action;
	}

	public abstract void setCorrectAnimation();
	
	//also handles attacking
	/*public void setCorrectAnimation() {
		switch (this.currentAction) {
		case Idle:
			this.setAnimation(Animation.Idle);
			break;
		case Walking:
			this.setAnimation(Animation.Walk);
			break;
		case Attacking:
			elapsedTime += attackTimer.getElapsedTime();
			if (elapsedTime > 2 && targetUnit != null) {
				turnToTarget();
				this.setAnimation(Animation.Attack);
				framesPassed = 0;
				elapsedTime = 0;
				attacked = false;
			} else {
				framesPassed ++;
				if(framesPassed > attack_vFrames.size() + 1 && !attacked) {
					this.setAnimation(Animation.Idle);
					try {
						attackTarget();
						//targetUnit.setAnimation(Animation.Oof);
					} catch (Exception e) {
						this.setCurrentAction(CurrentAction.Idle);
					}
					framesPassed = 0;
					attacked = true;
				}
			}
			break;
		case Dying:
			this.setAnimation(Animation.Dying);
			break;
		default:
			break;
		}
	}*/
	
	public Unit getTargetUnit() {
		return this.targetUnit;
	}
	
	public void setTargetUnit(Unit target) {
		if (targetUnit == null) targetUnit = target;
	}

	public void attackTarget() {
		
	}
	
	public void turnToTarget() {
		if(this.GetSpritePosX() > targetUnit.GetSpritePosX()) {
			facingRight = false;
		} else facingRight = true;
	}

	public abstract void Draw();
	
	/** Draw Animated Sprite */
	/*public void Draw() {
		Texture2D tex;
		if(this.facingRight) {
			switch (this.animation) {
			case Idle:
				tex = idle_vFrames.get(m_iActualFrame - 1);
				break;
			case Walk:
				tex = walk_vFrames.get(m_iActualFrame - 1);
				break;
			case Attack:
				tex = attack_vFrames.get(m_iActualFrame - 1);
				break;
			case Oof:
				tex = oof_vFrames.get(m_iActualFrame - 1);
				break;
			case Dying:
				tex = dying_vFrames.get(m_iActualFrame - 1);
				break;
			default:
				tex = idle_vFrames.get(m_iActualFrame - 1);
				break;
			}			
		}else {
			switch (this.animation) {
			case Idle:
				tex = idle_vFramesFlipped.get(m_iActualFrame - 1);
				break;
			case Walk:
				tex = walk_vFramesFlipped.get(m_iActualFrame - 1);
				break;
			case Attack:
				tex = attack_vFramesFlipped.get(m_iActualFrame - 1);
				break;
			case Oof:
				tex = oof_vFramesFlipped.get(m_iActualFrame - 1);
				break;
			case Dying:
				tex = dying_vFramesFlipped.get(m_iActualFrame - 1);
				break;
			default:
				tex = idle_vFramesFlipped.get(m_iActualFrame - 1);
				break;
			}	
		}

		tex.Draw(m_vSpritePosition);
		Update();
	}*/

	/** Draw Animated Sprite */
	/*public void Draw(Vector2D pos) {
		Texture2D tex = idle_vFrames.get(m_iActualFrame - 1);
		tex.Draw(pos);
		Update();
	}*/

	public abstract void Update();
	
	/** Update frames */
	/*public void Update() {
		if (500.0f / m_iFps < (System.currentTimeMillis() - m_iLastUpdate)) {
			m_iLastUpdate = System.currentTimeMillis();
			switch (this.animation) {
			case Idle:
				if (++m_iActualFrame > idle_vFrames.size()) {
					m_iActualFrame = 1;
				}
				break;
			case Walk:
				if (++m_iActualFrame > walk_vFrames.size()) {
					m_iActualFrame = 1;
				}
				break;
			case Attack:
				if (++m_iActualFrame > attack_vFrames.size()) {
					m_iActualFrame = 1;
				}
				break;
			case Oof:
				if (++m_iActualFrame > oof_vFrames.size()) {
					setAnimation(Animation.Idle);
				}
				break;
			case Dying:
				if (m_iActualFrame < dying_vFrames.size()) {
					m_iActualFrame++;
				}
				break;
			default:
				break;
			}

		}
	}*/

	/** Draw Animated Sprite */
	/*public void DrawOne() {
		Texture2D tex = idle_vFrames.get(m_iActualFrame - 1);
		tex.Draw(m_vSpritePosition);
		if (1000.0f / m_iFps < (System.currentTimeMillis() - m_iLastUpdate)) {
			m_iLastUpdate = System.currentTimeMillis();
			if (m_iActualFrame < idle_vFrames.size()) {
				m_iActualFrame++;
			}
		}
	}*/

	/** Draw Animated Sprite */
	/*public void DrawOne(Vector2D pos) {
		Texture2D tex = idle_vFrames.get(m_iActualFrame - 1);
		tex.Draw(pos);
		if (1000.0f / m_iFps < (System.currentTimeMillis() - m_iLastUpdate)) {
			m_iLastUpdate = System.currentTimeMillis();
			if (m_iActualFrame < idle_vFrames.size()) {
				m_iActualFrame++;
			}
		}
	}*/

	/** Draw Animated Sprite */
	/*public void DrawOne(float posX, float posY) {
		Texture2D tex = idle_vFrames.get(m_iActualFrame - 1);

		tex.Draw(posX, posY);

		if (1000.0f / m_iFps < (System.currentTimeMillis() - m_iLastUpdate)) {
			m_iLastUpdate = System.currentTimeMillis();
			if (m_iActualFrame < idle_vFrames.size()) {
				m_iActualFrame++;
			}
		}
	}*/

	/** Draw One Frane of the Animated Sprite */
	/*public void DrawFrame(int frame) {
		Texture2D tex = idle_vFrames.get(frame);
		tex.Draw(m_vSpritePosition);
	}*/

	/** Draw One Frane of the Animated Sprite in X, Y position */
	/*public void DrawFrame(int frame, float X, float Y) {
		Texture2D tex = idle_vFrames.get(frame);
		tex.Draw(X, Y);
	}*/

	/** Draw One Frane of the Animated Sprite */
	/*public void DrawFrame(int frame, Vector2D pos) {
		Texture2D tex = idle_vFrames.get(frame);
		tex.Draw(pos);
	}*/

	/** Reset Animation */
	public void Reset() {
		m_iActualFrame = 1;
	}

	public abstract Texture2D GetCurrentFrameTexture();
	
	/** Get current frame of the Animated Sprite */
	/*public Texture2D GetCurrentFrameTexture() {
		if(this.facingRight) {
			switch (this.animation) {
			case Idle:
				return idle_vFrames.get(m_iActualFrame - 1);
			case Walk:
				return walk_vFrames.get(m_iActualFrame - 1);
			case Attack:
				return attack_vFrames.get(m_iActualFrame - 1);
			case Oof:
				return oof_vFrames.get(m_iActualFrame - 1);
			case Dying:
				return dying_vFrames.get(m_iActualFrame - 1);
			default:
				return idle_vFrames.get(m_iActualFrame - 1);
			}			
		}else {
			switch (this.animation) {
			case Idle:
				return idle_vFramesFlipped.get(m_iActualFrame - 1);
			case Walk:
				return walk_vFramesFlipped.get(m_iActualFrame - 1);
			case Attack:
				return attack_vFramesFlipped.get(m_iActualFrame - 1);
			case Oof:
				return oof_vFramesFlipped.get(m_iActualFrame - 1);
			case Dying:
				return dying_vFramesFlipped.get(m_iActualFrame - 1);
			default:
				return idle_vFramesFlipped.get(m_iActualFrame - 1);
			}	
		}

	}*/

	/** Get one frame of the Animated Sprite */
	/*public Texture2D GetTexture(int frame) {
		return idle_vFrames.get(frame);
	}*/

	/** Get the frame number of the Sprite */
	/*public int GetNumberOfFrames() {
		return idle_vFrames.size();
	}*/

	/** Set Animation Speed */
	public void SetAnimationSpeed(int fps) {
		m_iFps = fps;
	}

	/** Set sprite position */
	public void SetPosition(float X, float Y) {
		m_vSpritePosition.x = X;
		m_vSpritePosition.y = Y;
	}

	/** Set sprite position */
	public void SetPosition(Vector2D pos) {
		m_vSpritePosition = pos;
	}

	public Vector2D GetPosition() {
		return m_vSpritePosition;
	}

	public float GetSpritePosX() {
		return m_vSpritePosition.x;
	}

	public float GetSpritePosY() {
		return m_vSpritePosition.y;
	}

	public void SetPositionX(float posX) {
		m_vSpritePosition.x = posX;
	}

	public void SetPositionY(float posY) {
		m_vSpritePosition.y = posY;
	}

	/** Returns the fps of the animation */
	public int GetAnimationSpeed() {
		return m_iFps;
	}

	public void Attack() {
		this.animation = Animation.Attack;
	}

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	public int getAttack() {
		return attack;
	}

	public void setAttack(int attack) {
		this.attack = attack;
	}

	public float getDefectionRange() {
		return detectionRange;
	}

	public void setDefectionRange(float detectionRange) {
		this.detectionRange = detectionRange;
	}
	
	public float getElapsedTime() {
		return this.elapsedTime;
	}
	
	public void setElapsedTime(float time) {
		this.elapsedTime = time;
	}

	/*public void cleanUp() {
		for (int i = 0; i < idle_vFrames.size(); i++) {
			Texture2D tex = idle_vFrames.get(i);
			tex.cleanup();
		}
	}*/
}
