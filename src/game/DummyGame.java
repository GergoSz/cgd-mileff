package game;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_DOWN;
import static org.lwjgl.glfw.GLFW.glfwGetCursorPos;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_UP;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_1;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_2;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_3;
import java.nio.DoubleBuffer;
import java.util.ArrayList;
import java.util.List;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.lwjgl.BufferUtils;

import common.Unit;
import common.Unit.Animation;
import common.Unit.CurrentAction;
import common.Enemy;
import common.Goblin;
import common.Golem;
import common.Map;
import common.Ogre;
import common.Orc;
import common.PlaceOfTower;
import common.Projectile;
//import engine.CSprite;
import engine.IGameLogic;
import engine.Renderer;
import engine.Texture2D;
import engine.Timer;
import engine.Vector2D;
import engine.MapHandler;
//import engine.Vector2D;
import engine.Window;

public class DummyGame implements IGameLogic {

	//private static final int GLFW_MOUSE_BUTTON_1 = 0;

	//private static final int GLFW_MOUSE_BUTTON_2 = 0;

	private final Renderer renderer;

	// 2D Texture items
	//private CSprite sprite;



	public static List<Golem> friendlyUnits = new ArrayList<Golem>();
	public static List<Enemy> enemyUnits = new ArrayList<Enemy>();
	public static List<Unit> dyingUnits = new ArrayList<Unit>();
	public static List<Projectile> projectiles = new ArrayList<Projectile>();
	public static PlaceOfTower tempPot = new PlaceOfTower();

	//private Map map;
	//private Texture2D[][] mapTextures = new Texture2D[Map.getNumberofRows()][Map.getNumberofColumns()];
	

	Timer timer = new Timer();
	float elapsedSeconds = 0;

	public DummyGame() {
		renderer = new Renderer();
	}

	@Override
	public void init(Window window) throws Exception {
		renderer.init(window);
		Map.loadMap();
		Map.loadDecorations();

		//valami�rt nem fut le a dekor�ci�, ha a loadTowerPlaces() itt van
		Map.loadTowerPlaces();
		//initCurrentMousePosition(200);
		Golem.LoadAllSprites();
		Goblin.LoadAllTextures();
		Ogre.LoadAllTextures();
		Orc.LoadAllTextures();
		Projectile.LoadAllTextures();
		
		

		Golem golem = new Golem(300,500);
		golem.setScale(0.33f);
		
		friendlyUnits.add(golem);
		
		enemyUnits.add(new Orc(800,50,0.2f));
		enemyUnits.get(0).addGoalLocation(new Vector2D(0,0));
		enemyUnits.get(0).addGoalLocation(new Vector2D(300,500));
				
		enemyUnits.add(new Ogre(0,0,0.2f));
		enemyUnits.get(1).addGoalLocation(new Vector2D(600,0));
		enemyUnits.get(1).addGoalLocation(new Vector2D(300,500));
		
		enemyUnits.add(new Goblin(800,400,0.2f));
		enemyUnits.get(2).addGoalLocation(new Vector2D(0,600));
		enemyUnits.get(2).addGoalLocation(new Vector2D(300,500));
		
		
		timer.init();
	}

	@Override
	public void input(Window window) {
		if (window.isKeyPressed('A')) {
			projectiles.add(new Projectile(1000,0,enemyUnits.get(0)));

		}
		//ez �gy valami�rt lefut t�bbsz�r (valszeg amiatt, hogy a lent t�lt�tt id�t �rz�keli)
		//lehet ink�bb a m�sik met�dus kellett volna, deh�t na, most ez m�kszik 
		 else if (window.mouseButtonDown(GLFW_MOUSE_BUTTON_1)) {
			 addTowerMouseClick(window, "textures/MapComponents/epulet/bastya/bastya_001.png");
				

		}else if (window.mouseButtonDown(GLFW_MOUSE_BUTTON_2)) {
			addTowerMouseClick(window, "textures/MapComponents/epulet/bastya/bastya_003.png");
			Golem golem = tempPot.getTower().putGolemOnMap();
			friendlyUnits.add(golem);

		}
		else if (window.mouseButtonDown(GLFW_MOUSE_BUTTON_3)) {
			addTowerMouseClick(window, "textures/MapComponents/epulet/bastya/bastya_005.png");
				

		}/* else if (window.isKeyPressed(GLFW_KEY_DOWN)) {
			Vector2D pos = sprite.GetPosition();
			pos.y += 10;
			sprite.SetPosition(pos);

		} else if (window.isKeyPressed(GLFW_KEY_LEFT)) {
			Vector2D pos = sprite.GetPosition();
			pos.x -= 10;
			sprite.SetPosition(pos);

		} else if (window.isKeyPressed(GLFW_KEY_RIGHT)) {
			Vector2D pos = sprite.GetPosition();
			pos.x += 10;
			sprite.SetPosition(pos);
		}*/
	}

	@Override
	public void update(float interval) {
		checkUnitActions(friendlyUnits, enemyUnits, dyingUnits, projectiles);
		
		collectGarbage();
		//System.gc();
	}
	
	public void checkUnitActions(List<Golem> friendlyUnits, List<Enemy> enemyUnits, List<Unit> dyingUnits, List<Projectile> projectiles) {
		//sorrend fontos
		checkEnemyWalking(enemyUnits);
		checkForFights(friendlyUnits, enemyUnits);
		walkToGoal(enemyUnits);
		setCorrectAnimations(friendlyUnits, enemyUnits, dyingUnits);
		handleProjectiles(projectiles);
		checkDyingUnits(dyingUnits);
	}
	
	public void checkForFights(List<Golem> friendlyUnits, List<Enemy> enemyUnits) {
		for (Enemy enemy : enemyUnits) {
			for (Golem golem : friendlyUnits) {
				if (GameLogic.calculateDistance(enemy, golem) < 100) {
					enemy.setCurrentAction(CurrentAction.Attacking);
					enemy.setTargetUnit(golem);
					golem.setCurrentAction(CurrentAction.Attacking);
					golem.setTargetUnit(enemy);

					//System.out.println(golem.getTargetUnit());
				}
			}
		}
	}
	
	public void checkEnemyWalking(List<Enemy> enemyUnits) {
		for (Enemy enemy : enemyUnits) {
			if(enemy.NotReachedGoal()) {
				enemy.setCurrentAction(CurrentAction.Walking);
			} else enemy.setCurrentAction(CurrentAction.Idle);
		}
	}
	
	public void walkToGoal(List<Enemy> enemyUnits) {
		for (Enemy enemy : enemyUnits) {
			if(enemy.getCuttentAction().equals(CurrentAction.Walking)) {
				enemy.Walk();
				
			}
		}
	}
	
	public void setCorrectAnimations(List<Golem> friendlyUnits, List<Enemy> enemyUnits, List<Unit> dyingUnits) {
		for (Golem golem : friendlyUnits) {
			golem.setCorrectAnimation();
		}
		for (Enemy enemy : enemyUnits) {
			enemy.setCorrectAnimation();
		}
		for (Unit unit : dyingUnits) {
			unit.setCorrectAnimation();
		}
	}
	
	public void handleProjectiles(List<Projectile> projectiles) {
		for (Projectile projectile : projectiles) {
			projectile.FlyToTarget();
		}
		for (int i = 0; i < projectiles.size(); i++) {
			if (projectiles.get(i).getToBeDeleted()) {
				projectiles.remove(i);
				i--;
			}
		}
		
	}
	
	public void checkDyingUnits(List<Unit> dyingUnits) {
		for (Unit unit : dyingUnits) {
			unit.setElapsedTime(unit.attackTimer.getElapsedTime() + unit.getElapsedTime());
			System.out.println(unit.getElapsedTime());
		}
		for(int i = 0; i < dyingUnits.size(); i++) {
			if(dyingUnits.get(i).getElapsedTime() > 10) {
				System.out.println("bruh");
				dyingUnits.set(i, null);
				dyingUnits.remove(i);
			}
		}
	}
	
	public void collectGarbage() {
		elapsedSeconds += timer.getElapsedTime();
		if(elapsedSeconds > 1) {
			elapsedSeconds = 0;
			System.gc();
		}
	}

	@Override
	public void render(Window window) {
		renderer.render(window);
	}

	@Override
	public void cleanup() {
		renderer.cleanup();
		//gameItems.cleanUp();
	}
	
	public void addTowerMouseClick(Window window, String filename) {
		 System.out.println(window.getCursorXPosition() + "," + window.getCursorYPosition());
			
		 for (PlaceOfTower pot : Map.getPlaceOfTowers()) {
			 //bonk big box kell majd ide
			 if(window.getCursorXPosition() > pot.getX() && window.getCursorXPosition() < pot.getX() + 100) {
				 if(window.getCursorYPosition() > pot.getY() && window.getCursorYPosition() < pot.getY() + 40) {
					 pot.placeTower(filename);
					 tempPot = pot;
				 }
		 	  }			 
		 }
	}
	
}
