package com.example.igorcouto.openglteste;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class Render implements GLSurfaceView.Renderer {

    int m_program;

    @Override
    // Metodo chamado quando o rendere é iniciado
    public void onSurfaceCreated(GL10 gl10, EGLConfig config) {

        // Cria, compila e atrela os shaders ao Opengl

        String vertexShaderSource = "attribute vec4 position;void main(){gl_Position = position;}";
        String fragmentShaderSource = "void main(){gl_FragColor = vec4(0.4, 1.0, 0.5, 1.0);}";

        int vertexShader = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER);
        GLES20.glShaderSource(vertexShader,vertexShaderSource);
        GLES20.glCompileShader(vertexShader);
        String vertexShaderCompileLog = GLES20.glGetShaderInfoLog(vertexShader);

        int fragmentShader = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER);
        GLES20.glShaderSource(fragmentShader,fragmentShaderSource);
        GLES20.glCompileShader(fragmentShader);
        String fragmentShaderCompileLog = GLES20.glGetShaderInfoLog(fragmentShader);

        m_program = GLES20.glCreateProgram();
        GLES20.glAttachShader(m_program, vertexShader);
        GLES20.glAttachShader(m_program, fragmentShader);
        // Diz que haverá uma variável (attribute) no shader que passaremos atravez do codigo
        // Recebe o programa, o indice deste atributo no shader e o nome dele
        GLES20.glBindAttribLocation(m_program, 0, "position");
        // Linka o programa com o OpenGL
        GLES20.glLinkProgram(m_program);
        String programLinkLog = GLES20.glGetProgramInfoLog(m_program);

        GLES20.glUseProgram( m_program );

        // Seta a cor que ira preencher o frame. Mesma coisa do OpenGL para desktop
        GLES20.glClearColor( 0.3f, 0.3f, 0.3f, 1.0f );
    }

    @Override
    // Metodo iniciado sempre que a view é redimencionada. Executa pelo menos uma vez, no inicio
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        // Quanto da view o render deve ocupar? Irá desenhar onde for atribuido
        GLES20.glViewport(0, 0, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        // Limpa a tela e preenche com a cor definida no glClearColor
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        // Array que representa os pontos do que será renderizado. O valor 1.0f no fim deve existir.
        float[] geometry =
        {
            -1.0f, 0.0f, 0.0f, 1.0f,
             1.0f, 0.0f, 0.0f, 1.0f,
             0.0f, 1.0f, 0.0f, 1.0f
        };

        /*
        *	O OpenGL espera receber os pontos da geometria em uma estrutura chamada Float Buffer. Acima, criamos um array
        *	de floats que contem os pontos. Agora vamos criar o FloatBuffer. Para isso precisamos alocar memória
        */

        // Aloca espaço para a geometria. Quantidade de elementos no array * tamanho do float em bytes
        ByteBuffer geometryByteBuffer = ByteBuffer.allocateDirect(geometry.length * 4);
        // Diz em que ordem o sistema deve tratar estes dados (Big ou Little Indian)
        geometryByteBuffer.order(ByteOrder.nativeOrder());
        // Transforma o byte buffer em float buffer
        FloatBuffer geometryBuffer = geometryByteBuffer.asFloatBuffer();
        // Coloca o conteudo (a geometria em si) dentro do buffer criado. Como acabamos de inserir informação, o ponteiro esta na ultima posição
        geometryBuffer.put(geometry);
        // Coloca o ponteiro no primeiro elemento
        geometryBuffer.rewind();

        // Define um array de vertices(pontos) que será passado para o OpenGL
        // Atributos:
        // Specifies the index of the generic vertex attribute to be modified. (?)
        // Numero de elementos por unidade,ou seja 4 valores por ponto
        // Tipo de dado
        // Se os dados devem ser normalizados quando acessados (de 0 a 1) ou acessados com seu valor de forma direta
        // Tamanho dos elementos em bytes. No caso, 16 bytes para ir do primeiro elemento ao ultimo de um vetice.
        // O buffer que contem a geometria
        GLES20.glVertexAttribPointer(0, 4, GLES20.GL_FLOAT, false, 4 * 4, geometryBuffer);

        // Habilita o primeiro atributo passado, no caso, o position, definido em glBindAttribLocation
        GLES20.glEnableVertexAttribArray(0);

        // Draw Call
        // Atributos:
        // Primitiva a ser desenhada
        // Por qual elemento começar a desenhar
        // Quantos elementos (pontos) deve desenhar? Note que isso é o elemento, ele tem a quantidade de de unidades definida no segundo argumento da glVertexAttribPointer
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 3);
    }
}
