package chabernac.opengl;

import com.jogamp.opengl.GL3;

public interface IOpengGLObject {
    public IOpengGLObject bind( GL3 gl );

    public IOpengGLObject use( GL3 gl );

    public IOpengGLObject unbind( GL3 gl );
}
