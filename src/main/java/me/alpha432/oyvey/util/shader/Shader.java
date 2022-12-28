/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.GlStateManager
 *  org.apache.commons.io.IOUtils
 *  org.lwjgl.opengl.ARBShaderObjects
 *  org.lwjgl.opengl.GL20
 */
package me.alpha432.oyvey.util.shader;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.renderer.GlStateManager;
import org.apache.commons.io.IOUtils;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.GL20;

public abstract class Shader {
    public int program;
    public Map<String, Integer> uniformsMap;
    protected String fragmentShader;

    public Shader(String fragmentShader) {
        int fragmentShaderID;
        int vertexShaderID;
        this.fragmentShader = fragmentShader;
        try {
            InputStream vertexStream = this.getClass().getResourceAsStream("/shader/vertex.vert");
            vertexShaderID = this.createShader(IOUtils.toString((InputStream)vertexStream, (Charset)Charset.defaultCharset()), 35633);
            IOUtils.closeQuietly((InputStream)vertexStream);
            InputStream fragmentStream = this.getClass().getResourceAsStream("/shader/" + fragmentShader);
            fragmentShaderID = this.createShader(IOUtils.toString((InputStream)fragmentStream, (Charset)Charset.defaultCharset()), 35632);
            IOUtils.closeQuietly((InputStream)fragmentStream);
        }
        catch (Exception e) {
            e.printStackTrace();
            return;
        }
        if (vertexShaderID == 0 || fragmentShaderID == 0) {
            return;
        }
        this.program = ARBShaderObjects.glCreateProgramObjectARB();
        if (this.program == 0) {
            return;
        }
        ARBShaderObjects.glAttachObjectARB((int)this.program, (int)vertexShaderID);
        ARBShaderObjects.glAttachObjectARB((int)this.program, (int)fragmentShaderID);
        ARBShaderObjects.glLinkProgramARB((int)this.program);
        ARBShaderObjects.glValidateProgramARB((int)this.program);
    }

    public void startShader() {
        GlStateManager.func_179094_E();
        GL20.glUseProgram((int)this.program);
        if (this.uniformsMap == null) {
            this.uniformsMap = new HashMap<String, Integer>();
            this.setupUniforms();
        }
        this.updateUniforms();
    }

    public void stopShader() {
        GL20.glUseProgram((int)0);
        GlStateManager.func_179121_F();
    }

    public abstract void setupUniforms();

    public abstract void updateUniforms();

    public int createShader(String shaderSource, int shaderType) {
        int shader = 0;
        try {
            shader = ARBShaderObjects.glCreateShaderObjectARB((int)shaderType);
            if (shader == 0) {
                return 0;
            }
            ARBShaderObjects.glShaderSourceARB((int)shader, (CharSequence)shaderSource);
            ARBShaderObjects.glCompileShaderARB((int)shader);
            if (ARBShaderObjects.glGetObjectParameteriARB((int)shader, (int)35713) == 0) {
                throw new RuntimeException("Error creating shader: " + this.getLogInfo(shader));
            }
            return shader;
        }
        catch (Exception e) {
            ARBShaderObjects.glDeleteObjectARB((int)shader);
            throw e;
        }
    }

    public String getLogInfo(int i) {
        return ARBShaderObjects.glGetInfoLogARB((int)i, (int)ARBShaderObjects.glGetObjectParameteriARB((int)i, (int)35716));
    }

    public void setUniform(String uniformName, int location) {
        this.uniformsMap.put(uniformName, location);
    }

    public void setupUniform(String uniformName) {
        this.setUniform(uniformName, GL20.glGetUniformLocation((int)this.program, (CharSequence)uniformName));
    }

    public int getUniform(String uniformName) {
        return this.uniformsMap.get(uniformName);
    }

    public int getProgramId() {
        return this.program;
    }
}

