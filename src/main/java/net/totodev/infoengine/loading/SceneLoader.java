package net.totodev.infoengine.loading;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import net.totodev.infoengine.ecs.IComponent;
import net.totodev.infoengine.ecs.Scene;
import net.totodev.infoengine.util.IO;
import net.totodev.infoengine.util.logging.LogLevel;
import net.totodev.infoengine.util.logging.Logger;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;

public class SceneLoader {
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public @NotNull Scene loadSceneFromFile(@NotNull Path path) {
        return loadScene(IO.getTextFromFile(new File(path.toUri())));
    }

    @SuppressWarnings("unchecked")
    public @NotNull Scene loadScene(@NotNull String sceneJson) {
        SceneModel sceneModel;
        Scene scene = new Scene();

        if (sceneJson.equals("")) {
            Logger.log(LogLevel.Critical, "SceneLoader", "File not found or is empty");
            return scene;
        }

        try {
            sceneModel = gson.fromJson(sceneJson, SceneModel.class);
        } catch (JsonSyntaxException e) {
            Logger.log(LogLevel.Critical, "SceneLoader", "Invalid scene file");
            return scene;
        }

        // Create entities
        for (int i = 0; i < sceneModel.entityCount(); i++)
            scene.createEntity();

        // Register and initialize components
        for (ComponentModel componentModel : sceneModel.components()) {
            try {
                Class<? extends IComponent> type = (Class<? extends IComponent>) Class.forName(componentModel.type());
                IComponent component = type.getDeclaredConstructor().newInstance();
                scene.registerComponentUnsafe(type, component);
                component.deserializeState(componentModel.data());
            } catch (ClassNotFoundException e) {
                Logger.log(LogLevel.Error, "SceneLoader", "Class " + componentModel.type() + " could not be found. This component will not be added.");
            } catch (NoSuchMethodException e) {
                Logger.log(LogLevel.Error, "SceneLoader", "Empty constructor could not found on class " + componentModel.type() + ". This component will not be added.");
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                Logger.log(LogLevel.Error, "SceneLoader", "Error while instantiating class " + componentModel.type() + ". This component will not be added.");
            }
        }

        //TODO: Systems

        Logger.log(LogLevel.Critical, "SceneLoader", "Scene <" + sceneModel.name() + "> loaded");
        return scene;
    }
}
