package chabernac.opengl;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL3;
import com.jogamp.opengl.util.GLBuffers;

public class Shape implements IOpenGLShape {
	private final int	stride;
	private float[]		data;
	private short[]		elements;
	private int			vertexBufferIndex;
	private int			elementBufferIndex;
	private int			vertexArrayIndex;
	private int[]		attributeLengths;

	public Shape(int stride, int rows) {
		super();
		this.stride = stride;
		this.data = new float[stride * rows];
		this.attributeLengths = new int[stride];
	}

	public Shape(int stride) {
		super();
		this.stride = stride;
		this.attributeLengths = new int[stride];
	}

	public void setRow(int row, float[] rowData) {
		for (int i = 0; i < rowData.length; i++) {
			data[row * stride + i] = rowData[i];
		}
	}

	public int[] getAttributeLengths() {
		return attributeLengths;
	}

	public Shape setAttributeLengths(int... attributeLengths) {
		this.attributeLengths = attributeLengths;
		return this;
	}

	public Shape bind(GL3 gl) {
		// VAO
		gl.glBindVertexArray(vertexArrayIndex);

		// VBO
		FloatBuffer vertexBuffer = GLBuffers.newDirectFloatBuffer(data);
		gl.glBindBuffer(GL.GL_ARRAY_BUFFER, vertexBufferIndex);
		gl.glBufferData(GL.GL_ARRAY_BUFFER, vertexBuffer.capacity() * Float.BYTES, vertexBuffer, GL.GL_STATIC_DRAW);
		long offset = 0;
		for (int i = 0; i < attributeLengths.length; i++) {
			gl.glVertexAttribPointer(i, attributeLengths[i], GL.GL_FLOAT, false, stride * Float.BYTES, offset * Float.BYTES);
			gl.glEnableVertexAttribArray(i);
			offset += attributeLengths[i];
		}
		gl.glBindBuffer(GL.GL_ARRAY_BUFFER, 0);
		
		// EBO
		ShortBuffer elementBuffer = GLBuffers.newDirectShortBuffer(elements);
		gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, elementBufferIndex);
		gl.glBufferData(
		        GL.GL_ELEMENT_ARRAY_BUFFER,
		        elementBuffer.capacity() * Short.BYTES,
		        elementBuffer,
		        GL.GL_STATIC_DRAW);
		gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, 0);
		return this;
	}

	public Shape unbind(GL3 gl) {
		return this;
	}

	public Shape use(GL3 gl) {
		gl.glBindVertexArray(vertexArrayIndex);
		gl.glDrawArrays(GL.GL_TRIANGLES, 0, stride);
		// gl.glBindVertexArray( 0 );
		return this;
	}

	public int getBufferIndex() {
		return vertexBufferIndex;
	}

	public void setVertexBufferIndex(int bufferIndex) {
		this.vertexBufferIndex = bufferIndex;
	}

	public int getVertexArrayIndex() {
		return vertexArrayIndex;
	}

	public void setVertexArrayIndex(int vertexArrayIndex) {
		this.vertexArrayIndex = vertexArrayIndex;
	}

	public float[] getData() {
		return data;
	}

	public Shape setData(float[] data) {
		this.data = data;
		return this;
	}

	public short[] getElements() {
		return elements;
	}

	public Shape setElements(short[] elements) {
		this.elements = elements;
		return this;
	}

	public int getElementBufferIndex() {
		return elementBufferIndex;
	}

	@Override
	public void setElementBufferIndex(int elementBufferIndex) {
		this.elementBufferIndex = elementBufferIndex;
	}
}
