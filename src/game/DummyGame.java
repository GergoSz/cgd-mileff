package game;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_DOWN;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_UP;

import java.util.ArrayList;
import java.util.List;

import org.joml.Matrix4f;

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
//import engine.CSprite;
import engine.IGameLogic;
import engine.Renderer;
import engine.Texture2D;
import engine.Timer;
import engine.MapHandler;
//import engine.Vector2D;
import engine.Window;

public class DummyGame implements IGameLogic {

	private final Renderer renderer;

	// 2D Texture items
	//private CSprite sprite;
<<<<<<< HEAD
	private List<Golem> friendlyUnits = new ArrayList<Golem>();
	private List<Enemy> enemyUnits = new ArrayList<Enemy>();
	private List<PlaceOfTower> towers = new ArrayList<PlaceOfTower>();
=======
	public static List<Golem> friendlyUnits = new ArrayList<Golem>();
	public static List<Enemy> enemyUnits = new ArrayList<Enemy>();
	public static List<Unit> dyingUnits = new ArrayList<Unit>();
>>>>>>> branch 'master' of https://github.com/lKomrad/cgd-mileff
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
<<<<<<< HEAD
		//valami�rt nem fut le a dekor�ci�, ha a loadTowerPlaces() itt van
		Map.loadTowerPlaces();
		
		
		
		Golem golem = new Golem(300,450);
=======

		Golem golem = new Golem(300,500);
>>>>>>> branch 'master' of https://github.com/lKomrad/cgd-mileff
		golem.setScale(0.33f);
		
		friendlyUnits.add(golem);
		
		enemyUnits.add(new Orc(800,50,0.2f));
		//enemyUnits.add(new Ogre(800,250,0.2f));
		enemyUnits.add(new Ogre(0,0,0.2f));
		enemyUnits.add(new Goblin(800,400,0.2f));
		timer.init();
	}

	@Override
	public void input(Window window) {
		if (window.isKeyPressed('A')) {
			friendlyUnits.get(0).Reset();
			friendlyUnits.get(0).Attack();

		}
		 else if (window.isKeyPressed(GLFW_KEY_DOWN)) {
			 int i = 0;
			 for (PlaceOfTower pot : Map.getPlaceOfTowers()) {
				 
				 pot.placeTower("textures/MapComponents/epulet/bastya/bastya_001.png");
				 System.out.println("EZLEFUT?!?!?!" + i);
				 i++;
				}
				

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
		checkUnitActions(friendlyUnits, enemyUnits, dyingUnits);
		
		collectGarbage();
		//System.gc();
	}
	
	public void checkUnitActions(List<Golem> friendlyUnits, List<Enemy> enemyUnits, List<Unit> dyingUnits) {
		//sorrend fontos
		checkEnemyWalking(enemyUnits);
		checkForFights(friendlyUnits, enemyUnits);
		walkToGoal(enemyUnits);
		setCorrectAnimations(friendlyUnits, enemyUnits, dyingUnits);
	}
	
	public void checkForFights(List<Golem> friendlyUnits, List<Enemy> enemyUnits) {
		for (Enemy enemy : enemyUnits) {
			for (Golem golem : friendlyUnits) {
				if (GameLogic.calculateDistance(enemy, golem) < 100) {
					enemy.setCurrentAction(CurrentAction.Attacking);
					enemy.setTargetUnit(golem);
					golem.setCurrentAction(CurrentAction.Attacking);
					golem.setTargetUnit(enemy);
<<<<<<< HEAD
					//System.out.println(golem.getTargetUnit());
=======
>>>>>>> branch 'master' of https://github.com/lKomrad/cgd-mileff
				}
			}
		}
	}
	
	public void checkEnemyWalking(List<Enemy> enemyUnits) {
		for (Enemy enemy : enemyUnits) {
			if(enemy.NotReachedGoal()) {
				enemy.setCurrentAction(CurrentAction.Walking);
			}
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
	
	public void checkDyingUnits(List<Unit> dyingUnits) {
		
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
	
}
