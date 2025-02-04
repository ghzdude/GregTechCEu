package gregtech.api.storagedrawers;

import it.unimi.dsi.fastutil.ints.Int2IntArrayMap;
import it.unimi.dsi.fastutil.ints.Int2IntMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface IAuxData {

    String KEY = "insertion_data";

    void gregTech$setData(String key, Object value);

    @Nullable
    Object gregTech$getData(String key);

    @NotNull
    default Int2IntMap getOrCreateData() {
        Int2IntMap v = (Int2IntMap) gregTech$getData(KEY);
        if (v == null) {
            v = new Int2IntArrayMap();
            gregTech$setData(KEY, v);
        }
        return v;
    }
}
