package gregtech.api.storagedrawers;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public interface IAuxData {

    String KEY = "insertion_data";

    default boolean gregTech$hasData(String key) {
        return gregTech$getData(key) != null;
    }

    void gregTech$setData(String key, Object value);

    @Nullable
    Object gregTech$getData(String key);

    @Nullable
    default <T> T getDataAncCast(String key, Class<T> clazz) {
        Object v = gregTech$getData(key);
        if (v != null && clazz.isAssignableFrom(v.getClass())) {
            return clazz.cast(v);
        }
        return null;
    }

    @NotNull
    @SuppressWarnings("unchecked")
    default <T> T getOrCreateData(String key, Supplier<T> generator) {
        Object v = gregTech$getData(key);
        if (v == null) {
            v = generator.get();
            gregTech$setData(key, v);
        }
        return (T) v;
    }

    default boolean getDataAsBoolean(String key) {
        Boolean b = getDataAncCast(key, Boolean.TYPE);
        if (b == null) throw new IllegalArgumentException();
        return b;
    }
}
