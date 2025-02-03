package gregtech.api.storagedrawers;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public interface IAuxData {

    String KEY = "insertion_data";

    void gregTech$setData(String key, Object value);

    @Nullable
    Object gregTech$getData(String key);

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
}
