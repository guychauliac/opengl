package chabernac.opengl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Map;

import com.jogamp.opengl.GL3;
import com.jogamp.opengl.util.GLBuffers;

public class ShaderProgram {
    public static enum ShaderType {
            VERTEX( GL3.GL_VERTEX_SHADER ),
            FRAGMENT( GL3.GL_FRAGMENT_SHADER );

        private final int glType;

        private ShaderType( int glType ) {
            this.glType = glType;
        }

        public int getGlType() {
            return glType;
        }
    }

    private final GL3                  gl;
    private final Map<String, Integer> shaders = new HashMap<>();
    private final int                  shaderProgram;

    public ShaderProgram( GL3 gl ) {
        super();
        this.gl = gl;
        this.shaderProgram = gl.glCreateProgram();
    }

    public ShaderProgram addShader( InputStream aShaderStream, ShaderType aShaderType, String aShaderName ) {
        if ( aShaderStream == null ) {
            throw new NullPointerException( "Shader stream must not be null" );
        }
        int shaderReference = gl.glCreateShader( aShaderType.getGlType() );

        try (BufferedReader bufferedReader = new BufferedReader( new InputStreamReader( aShaderStream ) )) {
            String shaderSource = "";
            String line;
            while ( ( line = bufferedReader.readLine() ) != null ) {
                shaderSource += line + "\n";
            }
            gl.glShaderSource( shaderReference, 1, new String[] { shaderSource }, (int[]) null, 0 );
            gl.glCompileShader( shaderReference );
        } catch ( IOException e ) {
            throw new RuntimeException( "Could not load shader", e );
        }

        shaders.put( aShaderName, shaderReference );
        return this;
    }

    public ShaderProgram attachAllShaders() {
        shaders.values().stream().forEach( shaderReference -> attachShader( shaderReference ) );

        gl.glLinkProgram( shaderProgram );

        IntBuffer status = GLBuffers.newDirectIntBuffer( 1 );
        gl.glGetProgramiv( shaderProgram, GL3.GL_LINK_STATUS, status );
        if ( status.get( 0 ) == GL3.GL_FALSE ) {

            IntBuffer infoLogLength = GLBuffers.newDirectIntBuffer( 1 );
            gl.glGetProgramiv( shaderProgram, GL3.GL_INFO_LOG_LENGTH, infoLogLength );

            ByteBuffer bufferInfoLog = GLBuffers.newDirectByteBuffer( infoLogLength.get( 0 ) );
            gl.glGetProgramInfoLog( shaderProgram, infoLogLength.get( 0 ), null, bufferInfoLog );
            byte[] bytes = new byte[ infoLogLength.get( 0 ) ];
            bufferInfoLog.get( bytes );
            String strInfoLog = new String( bytes );

            System.err.println( "Linker failure: " + strInfoLog );

            // destroyBuffers(infoLogLength, bufferInfoLog);
        }

        gl.glValidateProgram( shaderProgram );
        shaders.values().stream().forEach( shaderReference -> gl.glDetachShader( shaderProgram, shaderReference ) );

        // destroyBuffer(status);

        return this;
    }

    private void attachShader( Integer shaderReference ) {
        gl.glAttachShader( shaderProgram, shaderReference );
    }

    public ShaderProgram use() {
        gl.glUseProgram( shaderProgram );
        return this;
    }

    public ShaderProgram uniform( String variable, float... floats ) {
        int index = gl.glGetUniformLocation( shaderProgram, variable );
        if ( floats.length == 4 ) {
            gl.glUniform4f( index, floats[ 0 ], floats[ 1 ], floats[ 2 ], floats[ 3 ] );
        }
        return this;
    }

    public void unUse() {
        gl.glUseProgram( 0 );
    }
}
