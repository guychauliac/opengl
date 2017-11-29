package chabernac.opengl;

import com.jogamp.opengl.GL3;

public interface IOpengGLObject {
    public void bind( GL3 gl );

    public void use( GL3 gl );

    public void unbind( GL3 gl );
}
