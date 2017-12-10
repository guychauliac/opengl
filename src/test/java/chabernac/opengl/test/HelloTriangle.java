package chabernac.opengl.test;

import com.jogamp.newt.event.WindowAdapter;
import com.jogamp.newt.event.WindowEvent;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.GL3;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.util.Animator;

import chabernac.io.ClassPathResource;
import chabernac.opengl.Buffers;
import chabernac.opengl.IOpengGLObject;
import chabernac.opengl.ShaderProgram;
import chabernac.opengl.ShaderProgram.ShaderType;
import chabernac.opengl.Shape;
import chabernac.opengl.Texture;
import glm.mat._4.Mat4;
import glm.vec._3.Vec3;

public class HelloTriangle implements GLEventListener {
	private ShaderProgram	shaderProgram;
	private Buffers			buffers;
	private IOpengGLObject	texture;

	@Override
	public void display(GLAutoDrawable drawable) {
		draw(drawable);
	}

	private void draw(GLAutoDrawable drawable) {
		GL3 gl = drawable.getGL().getGL3();

		gl.glClear(GL3.GL_COLOR_BUFFER_BIT | GL3.GL_DEPTH_BUFFER_BIT);
		shaderProgram.use();
		texture.use(gl);
		buffers.use(gl);
		shaderProgram.unUse();
	}

	@Override
	public void dispose(GLAutoDrawable arg0) {
		// method body
	}

	@Override
	public void init(GLAutoDrawable arg0) {
		GL4 gl = arg0.getGL().getGL4();

		Mat4 model = new Mat4(1.0f).rotate(90, new Vec3(0,0,1));

		gl.glClearColor(0.0f, 0.0f, 0.0f, 1f);
		gl.glClear(GL3.GL_COLOR_BUFFER_BIT | GL3.GL_DEPTH_BUFFER_BIT);
		gl.glEnable(GL3.GL_DEPTH_TEST);

		buffers = new Buffers()
		        .addBuffer(new Shape(8)
		                .setAttributeLengths(3, 3, 2) // 3 floats for position,
		                                              // 3 float for color, 2
		                                              // for vertex position
		                .setData(new float[] {
		                        -0.5f, -0.5f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f,
		                        0.5f, -0.5f, 0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f,
		                        0.0f, 0.5f, 0.0f, 0.0f, 0.0f, 1.0f, 0.5f, 1f })
		                .setElements(new short[] { 0, 1, 2 }))
		        .bind(gl);

		shaderProgram = new ShaderProgram(gl)
		        .addShader(
		                getClass().getResourceAsStream("/shaders/hello-triangle.vert"),
		                ShaderType.VERTEX,
		                "vertexShader")
		        .addShader(
		                getClass().getResourceAsStream("/shaders/hello-triangle.frag"),
		                ShaderType.FRAGMENT,
		                "fragmentShader")
		        .attachAllShaders()
		        .use()
		        .uniform("model", model)
		        ;

		texture = new Texture(new ClassPathResource("/wall.jpg"))
		        .bind(gl);
	}

	@Override
	public void reshape(GLAutoDrawable arg0, int arg1, int arg2, int arg3, int arg4) {
		// method body
	}

	public static void main(String[] args) {
		// getting the capabilities object of GL2 profile
		final GLProfile profile = GLProfile.get(GLProfile.GL3);
		GLCapabilities capabilities = new GLCapabilities(profile);
		capabilities.setDoubleBuffered(true);
		// The canvas
		GLWindow window = GLWindow.create(capabilities);
		window.setAutoSwapBufferMode(true);
		Animator animator = new Animator(window);
		animator.start();
		HelloTriangle l = new HelloTriangle();
		window.addGLEventListener(l);
		window.setSize(400, 400);
		window.setVisible(true);

		window.addWindowListener(new WindowAdapter() {
			@Override
			public void windowDestroyed(WindowEvent e) {
				animator.stop();
				System.exit(1);
			}
		});
	}
}
