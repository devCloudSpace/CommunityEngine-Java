package ce.core;

import ce.core.graphics.GameObject;
import ce.core.graphics.Mesh;
import ce.core.input.Input;
import ce.core.input.Key;
import ce.core.input.State;
import ce.core.maths.Transform;
import ce.core.maths.Vector3f;
import ce.core.shader.Default3DShader;
import ce.core.texture.Texture;
import ce.core.texture.TextureLoader;

public class EngineApp {
	
	private Window window;
	private Camera camera;
	private Default3DShader shader;
	private Texture defaultTexture;
	private Mesh mesh;
	private GameObject object;
	
	public EngineApp(int width, int height) {
		System.out.println(Version.getEngineVersion());
		window = Window.createWindow(width, height, "[CE] CommunityEngine");
		init();
		loop();
		dispose();
	}
	
	private void init() {
		window.enableDepthBuffer();
		
		camera = new Camera(window, 70f, 0.1f, 1000f);
		camera.setPosition(new Vector3f(0, 0, 2));
		shader = new Default3DShader();
		shader.bind();
		shader.loadMatrix(shader.getProjectionMatrix(), camera.getProjectionMatrix());
		shader.unbind();
		
		defaultTexture = TextureLoader.loadTexture("res/textures/default.png");
		
		float[] vertices = {
				//
				-0.5f, 0.5f, 0f, //
				-0.5f, -0.5f, 0f, //
				0.5f, -0.5f, 0f, //
				0.5f, 0.5f, 0f //
		};

		float[] texCoords = { 0, 0, //
				0, 1, //
				1, 1, //
				1, 0, //
		};

		int[] indices = {
				//
				0, 1, 2, //
				2, 3, 0//
		};
		
		mesh = new Mesh();
		mesh.add(vertices, texCoords, indices);

		object = new GameObject(new Transform(new Vector3f(0), new Vector3f(0), new Vector3f(1))) {
			public void update() {

			}
		};
		object.setTextureID(defaultTexture.getID());
	}
	
	private void loop()
	{
		while (window.isCloseRequested()) {
			if (Input.getKey(window, Key.KEY_ESCAPE) == State.PRESS) {
				break;
			}

			float SPEED = 0.01f;
			if (Input.getKey(window, Key.KEY_W) == State.PRESS) {
				camera.getPosition().x += Math.sin(camera.getYaw() * Math.PI / 180) * SPEED; // * Time.getDelta();
				camera.getPosition().z += -Math.cos(camera.getYaw() * Math.PI / 180) * SPEED; // * Time.getDelta();
			}

			if (Input.getKey(window, Key.KEY_S) == State.PRESS) {
				camera.getPosition().x -= Math.sin(camera.getYaw() * Math.PI / 180) * SPEED; // * Time.getDelta();
				camera.getPosition().z -= -Math.cos(camera.getYaw() * Math.PI / 180) * SPEED; // * Time.getDelta();
			}

			if (Input.getKey(window, Key.KEY_A) == State.PRESS) {
				camera.getPosition().x += Math.sin((camera.getYaw() - 90) * Math.PI / 180) * SPEED; // * Time.getDelta();
				camera.getPosition().z += -Math.cos((camera.getYaw() - 90) * Math.PI / 180) * SPEED; // * Time.getDelta();
			}

			if (Input.getKey(window, Key.KEY_D) == State.PRESS) {
				camera.getPosition().x += Math.sin((camera.getYaw() + 90) * Math.PI / 180) * SPEED; // * Time.getDelta();
				camera.getPosition().z += -Math.cos((camera.getYaw() + 90) * Math.PI / 180) * SPEED; // * Time.getDelta();
			}

			camera.update();
			shader.bind();
			shader.loadViewMatrix(camera);
			{
				mesh.enable();
				{
					mesh.render(shader, shader.getModelMatrix(), object, camera);
				}
				mesh.disable();
			}
			shader.unbind();
			window.update();
		}
	}
	
	private void dispose()
	{
		defaultTexture.dispose();
		mesh.disable();
		mesh.dispose();
		shader.unbind();
		shader.dispose();
		window.close();
		window.disposeGLFW();
	}

}