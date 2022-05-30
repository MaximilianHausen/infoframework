package net.totodev.infoengine.ecs;

import net.totodev.infoengine.loading.ComponentDataModel;
import org.eclipse.collections.api.set.primitive.MutableIntSet;
import org.eclipse.collections.impl.factory.primitive.IntSets;
import org.jetbrains.annotations.*;

public class FlagComponent implements IComponent {
    private final MutableIntSet set = IntSets.mutable.empty();
    public void addOnEntity(int entityId) {
        set.add(entityId);
    }
    public void removeFromEntity(int entityId) {
        set.remove(entityId);
    }

    public void deserializeState(@NotNull ComponentDataModel data) {
        addOnEntity(data.entity);
    }
    public @Nullable String serializeState(int entityId) {
        return set.contains(entityId) ? "" : null;
    }

    public boolean isPresentOn(int entityId) {
        return set.contains(entityId);
    }
}
