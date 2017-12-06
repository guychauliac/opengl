package chabernac.opengl;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import com.jogamp.opengl.GL3;
import com.jogamp.opengl.util.GLBuffers;

public class Buffers implements IOpengGLObject {
    private final List<IOpenGLBuffer> buffers = new ArrayList<>();

    public Buffers addBuffer( IOpenGLBuffer aBuffer ) {
        buffers.add( aBuffer );
        return this;
    }

    @Override
    public Buffers bind( GL3 gl ) {
        IntBuffer bufferReferences = GLBuffers.newDirectIntBuffer( buffers.size() * 3 );
        gl.glGenBuffers( buffers.size() * 3, bufferReferences );

        // IntBuffer vertexArrayReferences = GLBuffers.newDirectIntBuffer( buffers.size() );
        // gl.glGenVertexArrays( buffers.size(), vertexArrayReferences );

        for ( int i = 0; i < buffers.size(); i++ ) {
            IOpenGLBuffer buffer = buffers.get( i );
            buffer.setVertexBufferIndex( bufferReferences.get( i * 3 + 0 ) );
            buffer.setVertexArrayIndex( bufferReferences.get( i * 3 + 1 ) );
            buffer.setElementBufferIndex( bufferReferences.get( i * 3 + 2 ) );
            buffer.bind( gl );
        }
        return this;
    }

    @Override
    public Buffers use( GL3 gl ) {
        buffers.stream().forEach( buffer -> buffer.use( gl ) );
        return this;
    }

    @Override
    public Buffers unbind( GL3 gl ) {
        buffers.stream().forEach( buffer -> buffer.unbind( gl ) );
        return this;
    }
}
