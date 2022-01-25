package org.totogames.infoengine.rendering.opengl;

import org.jetbrains.annotations.NotNull;
import org.totogames.infoengine.DisposedException;
import org.totogames.infoengine.rendering.opengl.enums.ShaderParameters;
import org.totogames.infoengine.rendering.opengl.enums.ShaderTypes;
import org.totogames.infoengine.util.logging.LogSeverity;
import org.totogames.infoengine.util.logging.Logger;

import java.util.HashMap;

import static org.lwjgl.opengl.GL11C.GL_FALSE;
import static org.lwjgl.opengl.GL20C.*;
import static org.lwjgl.opengl.GL46C.glDeleteShader;

public class Shader implements IOglObject {
    private final static HashMap<ShaderTypes, String> typeNames;

    static {
        HashMap<ShaderTypes, String> temp = new HashMap<>();
        temp.put(ShaderTypes.VERTEX_SHADER, "VertexShader");
        temp.put(ShaderTypes.FRAGMENT_SHADER, "FragmentShader");
        temp.put(ShaderTypes.GEOMETRY_SHADER, "GeometryShader");
        temp.put(ShaderTypes.TESS_CONTROL_SHADER, "TessControlShader");
        temp.put(ShaderTypes.TESS_EVALUATION_SHADER, "TessEvaluationShader");
        typeNames = temp;
    }

    private final int id;
    private final String source;
    private final ShaderTypes shaderType;
    private boolean isDisposed = false;

    public Shader(@NotNull String source, @NotNull ShaderTypes type) {
        this.source = source;
        this.shaderType = type;

        id = glCreateShader(type.getValue());
        Logger.log(LogSeverity.Debug, "Shader", typeNames.get(type) + " created in slot " + id);
        glShaderSource(id, source);
        glCompileShader(id);

        if (glGetShaderi(id, GL_COMPILE_STATUS) == GL_FALSE) {
            Logger.log(LogSeverity.Critical, "Shader", typeNames.get(type) + " " + id + " could not be compiled");
            dispose();
            return;
        }

        Logger.log(LogSeverity.Debug, "Shader", typeNames.get(type) + " compiled in slot " + id);
    }

    public @NotNull ShaderTypes getShaderType() {
        return shaderType;
    }

    public @NotNull String getSource() {
        if (isDisposed) throw new DisposedException("Shader was already disposed");
        return source;
    }

    public int getShaderParameter(@NotNull ShaderParameters param) {
        if (isDisposed) throw new DisposedException("Shader was already disposed");
        return glGetShaderi(id, param.getValue());
    }

    public int getId() {
        if (isDisposed) throw new DisposedException("Shader was already disposed");
        return id;
    }

    public void dispose() {
        if (isDisposed) throw new DisposedException("Shader was already disposed");
        glDeleteShader(id);
        isDisposed = true;
        Logger.log(LogSeverity.Debug, "Shader", typeNames.get(shaderType) + " deleted from slot " + id);
    }
    public boolean isDisposed() {
        return isDisposed;
    }
}
