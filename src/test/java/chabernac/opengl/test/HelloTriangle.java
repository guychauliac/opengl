package chabernac.opengl.test;

import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.KeyListener;
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
import chabernac.opengl.IOpengGLObject;
import chabernac.opengl.OpenGLShapes;
import chabernac.opengl.ShaderProgram;
import chabernac.opengl.ShaderProgram.ShaderType;
import chabernac.opengl.Shape;
import chabernac.opengl.Texture;
import glm.Glm;
import glm.mat._4.Mat4;
import glm.vec._3.Vec3;

public class HelloTriangle implements GLEventListener, KeyListener {
  private ShaderProgram  shaderProgram;
  private OpenGLShapes   shapes;
  private IOpengGLObject texture;
  private final long     startTime  = System.currentTimeMillis();
  private Mat4           projection = Glm.perspective_((float) Math.PI / 4, 1.0f, 0.1f, 5000f);
  private Mat4           view       = new Mat4(1.0f).translate(0, 0, -400.0f);

  @Override
  public void display(GLAutoDrawable drawable) {
    draw(drawable);
  }

  private void draw(GLAutoDrawable drawable) {

    GL3 gl = drawable.getGL().getGL3();
    gl.glClear(GL3.GL_COLOR_BUFFER_BIT | GL3.GL_DEPTH_BUFFER_BIT);

    shaderProgram.use();
    Mat4 model = new Mat4(1.0f).rotate(((float) (System.currentTimeMillis() - startTime)) / 1000f, new Vec3(0, 0, 1));
    shaderProgram.uniform("model", model);
    shaderProgram.uniform("projection", projection);
    shaderProgram.uniform("view", view);
    texture.use(gl);
    shapes.use(gl);
    shaderProgram.unUse();
  }

  @Override
  public void dispose(GLAutoDrawable arg0) {
    // method body
  }

  @Override
  public void init(GLAutoDrawable arg0) {
    GL4 gl = arg0.getGL().getGL4();

    gl.glClearColor(0.0f, 0.0f, 0.0f, 1f);
    gl.glClear(GL3.GL_COLOR_BUFFER_BIT | GL3.GL_DEPTH_BUFFER_BIT);
    gl.glEnable(GL3.GL_DEPTH_TEST);

    shapes = new OpenGLShapes()
        .addBuffer(
            new Shape(8)
                .setAttributeLengths(3, 3, 2) // 3 floats for position,
                                              // 3 float for color, 2
                                              // for vertex position
                .setData(new float[] {
                    -200f, -200f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f,
                    200f, -200f, 0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f,
                    0.0f, 200f, 0.0f, 0.0f, 0.0f, 1.0f, 0.5f, 1f })
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
        .uniform("view", view)
        .uniform("projection", projection);

    texture = new Texture(new ClassPathResource("/textures/wall.jpg"))
        .bind(gl);
  }

  @Override
  public void reshape(GLAutoDrawable arg0, int arg1, int arg2, int width, int height) {
    projection = Glm.perspective_((float) Math.PI / 4, (float) width / (float) height, 0.1f, 1000f);
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
    HelloTriangle helloTriangle = new HelloTriangle();
    window.addGLEventListener(helloTriangle);
    window.setSize(400, 400);
    window.setResizable(true);
    window.setVisible(true);
    window.addKeyListener(helloTriangle);

    window.addWindowListener(new WindowAdapter() {
      @Override
      public void windowDestroyed(WindowEvent e) {
        animator.stop();
        System.exit(1);
      }
    });
  }

  @Override
  public void keyPressed(KeyEvent e) {

  }

  @Override
  public void keyReleased(KeyEvent e) {
    if (e.getKeyCode() == KeyEvent.VK_DOWN) {
      view = view.rotate((float)(Math.PI / 180), 1f, 0f, 0f);
    } else if (e.getKeyCode() == KeyEvent.VK_UP) {
      view = view.rotate((float)(-Math.PI / 180), 1f, 0f, 0f);
    }
  }
}
