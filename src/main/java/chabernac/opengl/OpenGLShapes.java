package chabernac.opengl;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import com.jogamp.opengl.GL3;
import com.jogamp.opengl.util.GLBuffers;

public class OpenGLShapes implements IOpengGLObject {
    private final List<IOpenGLShape> buffers = new ArrayList<>();

    public OpenGLShapes addBuffer( IOpenGLShape aBuffer ) {
        buffers.add( aBuffer );
        return this;
    }

    @Override
    public OpenGLShapes bind( GL3 gl ) {
        IntBuffer bufferReferences = GLBuffers.newDirectIntBuffer( buffers.size() * 3 );
        gl.glGenBuffers( buffers.size() * 3, bufferReferences );

        // IntBuffer vertexArrayReferences = GLBuffers.newDirectIntBuffer( buffers.size() );
        // gl.glGenVertexArrays( buffers.size(), vertexArrayReferences );

        for ( int i = 0; i < buffers.size(); i++ ) {
            IOpenGLShape buffer = buffers.get( i );
            buffer.setVertexBufferIndex( bufferReferences.get( i * 3 + 0 ) );
            buffer.setVertexArrayIndex( bufferReferences.get( i * 3 + 1 ) );
            buffer.setElementBufferIndex( bufferReferences.get( i * 3 + 2 ) );
            buffer.bind( gl );
        }
        return this;
    }

    @Override
    public OpenGLShapes use( GL3 gl ) {
        buffers.stream().forEach( buffer -> buffer.use( gl ) );
        return this;
    }

    @Override
    public OpenGLShapes unbind( GL3 gl ) {
        buffers.stream().forEach( buffer -> buffer.unbind( gl ) );
        return this;
    }
}
