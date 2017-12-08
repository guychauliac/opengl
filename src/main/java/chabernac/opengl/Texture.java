package chabernac.opengl;

import java.io.IOException;

import com.jogamp.opengl.GL3;
import com.jogamp.opengl.util.texture.TextureIO;

import chabernac.io.IResource;

public class Texture implements IOpengGLObject {
	private final IResource							textureResource;
	private com.jogamp.opengl.util.texture.Texture	texture;

	public Texture(IResource textureResource) {
		this.textureResource = textureResource;
	}

	@Override
	public IOpengGLObject bind(GL3 gl) {
		try {
			texture = TextureIO.newTexture(textureResource.getInputStream(), false, null);
			texture.setTexParameteri(gl, GL3.GL_TEXTURE_MIN_FILTER, GL3.GL_LINEAR);
			texture.setTexParameteri(gl, GL3.GL_TEXTURE_MAG_FILTER, GL3.GL_LINEAR);
			texture.setTexParameteri(gl, GL3.GL_TEXTURE_WRAP_S, GL3.GL_CLAMP_TO_EDGE);
			texture.setTexParameteri(gl, GL3.GL_TEXTURE_WRAP_T, GL3.GL_CLAMP_TO_EDGE);
			return this;
		} catch (IOException e) {
			throw new RuntimeException("Could not load texture", e);
		}
	}

	@Override
	public IOpengGLObject use(GL3 gl) {
		texture.enable(gl);
		return this;
	}

	@Override
	public IOpengGLObject unbind(GL3 gl) {
		texture.disable(gl);
		return this;
	}

}
