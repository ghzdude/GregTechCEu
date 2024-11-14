package gregtech.api.pattern;

import gregtech.api.GregTechAPI;
import gregtech.api.block.IHeatingCoilBlockStats;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.interfaces.IGregTechTileEntity;
import gregtech.api.metatileentity.multiblock.MultiblockControllerBase;
import gregtech.api.util.BlockInfo;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.include.com.google.common.collect.ImmutableList;

import java.util.*;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class TraceabilityPredicate implements Predicate<BlockWorldState> {

    // Allow any block.
    public static TraceabilityPredicate ANY = new TraceabilityPredicate((state) -> true);

    // Allow the air block.
    public static TraceabilityPredicate AIR = new TraceabilityPredicate(BlockWorldState::isAir);

    // Allow all heating coils, and require them to have the same type.
    public static TraceabilityPredicate HEATING_COILS = new TraceabilityPredicate(blockWorldState -> {
        IBlockState blockState = blockWorldState.getBlockState();
        if (GregTechAPI.HEATING_COILS.containsKey(blockState)) {
            IHeatingCoilBlockStats stats = GregTechAPI.HEATING_COILS.get(blockState);
            Object currentCoil = blockWorldState.getMatchContext().getOrPut("CoilType", stats);
            if (!currentCoil.equals(stats)) {
                blockWorldState.setError(new PatternStringError("gregtech.multiblock.pattern.error.coils"));
                return false;
            }
            blockWorldState.getMatchContext().getOrPut("VABlock", new LinkedList<>()).add(blockWorldState.getPos());
            return true;
        }
        return false;
    }, () -> {
        var coils = new ArrayList<>(GregTechAPI.HEATING_COILS.entrySet());
        coils.sort(Comparator.comparingInt(entry -> entry.getValue().getTier()));
        BlockInfo[] infos = new BlockInfo[coils.size()];
        int i = 0;
        for (var entry : coils)
            infos[i++] = new BlockInfo(entry.getKey(), null);
        return infos;
    }).addTooltips("gregtech.multiblock.pattern.error.coils");

    public final List<SimplePredicate> common = new ArrayList<>();
    public final List<SimplePredicate> limited = new ArrayList<>();
    protected boolean isCenter;
    protected boolean hasAir = false;
    protected boolean isSingle = true;

    public TraceabilityPredicate() {}

    public TraceabilityPredicate(TraceabilityPredicate predicate) {
        common.addAll(predicate.common);
        limited.addAll(predicate.limited);
        isCenter = predicate.isCenter;
        hasAir = predicate.hasAir;
        isSingle = predicate.isSingle;
    }

    public TraceabilityPredicate(Predicate<BlockWorldState> predicate, Supplier<BlockInfo[]> candidates) {
        common.add(new SimplePredicate(predicate, candidates));
    }

    public TraceabilityPredicate(Predicate<BlockWorldState> predicate) {
        this(predicate, null);
    }

    public boolean isHasAir() {
        return hasAir;
    }

    public boolean isSingle() {
        return isSingle;
    }

    /**
     * Mark it as the controller of this multi. Normally you won't call it yourself. Use
     * {@link MultiblockControllerBase#selfPredicate()} plz.
     */
    public TraceabilityPredicate setCenter() {
        isCenter = true;
        return this;
    }

    public TraceabilityPredicate sort() {
        limited.sort(Comparator.comparingInt(a -> ((a.minLayerCount + 1) * 100 + a.minGlobalCount)));
        return this;
    }

    /**
     * Add tooltips for candidates. They are shown in JEI Pages.
     * Do NOT pass {@link I18n#format(String, Object...)} calls here! Everything is will be translated when it's needed.
     * If you need parameters, use {@link #addTooltip(String, Object...)} instead.
     */
    public TraceabilityPredicate addTooltips(String... tips) {
        if (FMLCommonHandler.instance().getSide() == Side.CLIENT && tips.length > 0) {
            List<String> tooltips = Arrays.stream(tips).collect(Collectors.toList());
            common.forEach(predicate -> {
                if (predicate.candidates == null) return;
                if (predicate.toolTips == null) {
                    predicate.toolTips = new ArrayList<>();
                }
                predicate.toolTips.addAll(tooltips);
            });
            limited.forEach(predicate -> {
                if (predicate.candidates == null) return;
                if (predicate.toolTips == null) {
                    predicate.toolTips = new ArrayList<>();
                }
                predicate.toolTips.addAll(tooltips);
            });
        }
        return this;
    }

    /**
     * Note: This method does not translate dynamically!! Parameters can not be updated once set.
     */
    public TraceabilityPredicate addTooltip(String langKey, Object... data) {
        if (FMLCommonHandler.instance().getSide() == Side.CLIENT) {
            addTooltips(I18n.format(langKey, data));
        }
        return this;
    }

    /**
     * Set the minimum number of candidate blocks.
     */
    public TraceabilityPredicate setMinGlobalLimited(int min) {
        limited.addAll(common);
        common.clear();
        for (SimplePredicate predicate : limited) {
            predicate.minGlobalCount = min;
        }
        return this;
    }

    public TraceabilityPredicate setMinGlobalLimited(int min, int previewCount) {
        return this.setMinGlobalLimited(min).setPreviewCount(previewCount);
    }

    /**
     * Set the maximum number of candidate blocks.
     */
    public TraceabilityPredicate setMaxGlobalLimited(int max) {
        limited.addAll(common);
        common.clear();
        for (SimplePredicate predicate : limited) {
            predicate.maxGlobalCount = max;
        }
        return this;
    }

    public TraceabilityPredicate setMaxGlobalLimited(int max, int previewCount) {
        return this.setMaxGlobalLimited(max).setPreviewCount(previewCount);
    }

    /**
     * Set the minimum number of candidate blocks for each aisle layer.
     */
    public TraceabilityPredicate setMinLayerLimited(int min) {
        limited.addAll(common);
        common.clear();
        for (SimplePredicate predicate : limited) {
            predicate.minLayerCount = min;
        }
        return this;
    }

    public TraceabilityPredicate setMinLayerLimited(int min, int previewCount) {
        return this.setMinLayerLimited(min).setPreviewCount(previewCount);
    }

    /**
     * Set the maximum number of candidate blocks for each aisle layer.
     */
    public TraceabilityPredicate setMaxLayerLimited(int max) {
        limited.addAll(common);
        common.clear();
        for (SimplePredicate predicate : limited) {
            predicate.maxLayerCount = max;
        }
        return this;
    }

    public TraceabilityPredicate setMaxLayerLimited(int max, int previewCount) {
        return this.setMaxLayerLimited(max).setPreviewCount(previewCount);
    }

    /**
     * Sets the Minimum and Maximum limit to the passed value
     *
     * @param limit The Maximum and Minimum limit
     */
    public TraceabilityPredicate setExactLimit(int limit) {
        return this.setMinGlobalLimited(limit).setMaxGlobalLimited(limit);
    }

    /**
     * Set the number of it appears in JEI pages. It only affects JEI preview. (The specific number)
     */
    public TraceabilityPredicate setPreviewCount(int count) {
        common.forEach(predicate -> predicate.previewCount = count);
        limited.forEach(predicate -> predicate.previewCount = count);
        return this;
    }

    @Override
    public boolean test(BlockWorldState blockWorldState) {
        for (SimplePredicate p : limited) {
            if (p.testLimited(blockWorldState)) {
                return true;
            }
        }

        for (SimplePredicate p : common) {
            if (p.test(blockWorldState)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public @NotNull TraceabilityPredicate negate() {
        return TraceabilityPredicate.negate(this);
    }

    @Override
    public @NotNull TraceabilityPredicate or(@NotNull Predicate<? super BlockWorldState> other) {
        if (other instanceof TraceabilityPredicate traceabilityPredicate)
            return TraceabilityPredicate.or(this, traceabilityPredicate);
        return this;
    }

    @Override
    public @NotNull TraceabilityPredicate and(@NotNull Predicate<? super BlockWorldState> other) {
        if (other instanceof TraceabilityPredicate traceabilityPredicate)
            return TraceabilityPredicate.and(this, traceabilityPredicate);
        return this;
    }

    static TraceabilityPredicate and(TraceabilityPredicate a, TraceabilityPredicate b) {
        TraceabilityPredicate newPredicate = new TraceabilityPredicate(a);
        if (a != AIR && b != AIR) {
            newPredicate.isSingle = false;
        } else {
            newPredicate.isSingle = a.isSingle && b.isSingle;
        }
        newPredicate.hasAir = newPredicate.hasAir || a == AIR || b == AIR;
        newPredicate.common.addAll(b.common);
        newPredicate.limited.addAll(b.limited);
        return newPredicate;
    }

    static TraceabilityPredicate or(TraceabilityPredicate a, TraceabilityPredicate b) {
        TraceabilityPredicate newPredicate = new TraceabilityPredicate(a);
        if (a != AIR && b != AIR) {
            newPredicate.isSingle = false;
        } else {
            newPredicate.isSingle = a.isSingle && b.isSingle;
        }
        newPredicate.hasAir = newPredicate.hasAir || a == AIR || b == AIR;
        newPredicate.common.addAll(b.common);
        newPredicate.limited.addAll(b.limited);
        return newPredicate;
    }

    static TraceabilityPredicate negate(TraceabilityPredicate a) {
        Predicate<BlockWorldState> negate = blockWorldState -> !a.test(blockWorldState);
        TraceabilityPredicate newPredicate = new TraceabilityPredicate(negate);
        newPredicate.isSingle = a.isSingle;
        newPredicate.hasAir = newPredicate.hasAir || a == AIR;
        return newPredicate;
    }

    static TraceabilityPredicate not(TraceabilityPredicate a) {
        return a.negate();
    }

    public static class SimplePredicate {

        public final Supplier<BlockInfo[]> candidates;
        private BlockInfo[] cache = null;

        public final Predicate<BlockWorldState> predicate;

        @SideOnly(Side.CLIENT)
        private List<String> toolTips;

        public int minGlobalCount = -1;
        public int maxGlobalCount = -1;
        public int minLayerCount = -1;
        public int maxLayerCount = -1;

        public int previewCount = -1;

        public SimplePredicate(Predicate<BlockWorldState> predicate, Supplier<BlockInfo[]> candidates) {
            this.predicate = predicate;
            this.candidates = candidates;
        }

        @SideOnly(Side.CLIENT)
        public List<String> getToolTips(TraceabilityPredicate predicates) {
            List<String> result = new ArrayList<>();
            if (toolTips != null) {
                toolTips.forEach(tip -> result.add(I18n.format(tip)));
            }
            if (minGlobalCount == maxGlobalCount && maxGlobalCount != -1) {
                result.add(I18n.format("gregtech.multiblock.pattern.error.limited_exact", minGlobalCount));
            } else if (minGlobalCount != maxGlobalCount && minGlobalCount != -1 && maxGlobalCount != -1) {
                result.add(I18n.format("gregtech.multiblock.pattern.error.limited_within", minGlobalCount,
                        maxGlobalCount));
            } else {
                if (minGlobalCount != -1) {
                    result.add(I18n.format("gregtech.multiblock.pattern.error.limited.1", minGlobalCount));
                }
                if (maxGlobalCount != -1) {
                    result.add(I18n.format("gregtech.multiblock.pattern.error.limited.0", maxGlobalCount));
                }
            }
            if (minLayerCount != -1) {
                result.add(I18n.format("gregtech.multiblock.pattern.error.limited.3", minLayerCount));
            }
            if (maxLayerCount != -1) {
                result.add(I18n.format("gregtech.multiblock.pattern.error.limited.2", maxLayerCount));
            }
            if (predicates == null) return result;
            if (predicates.isSingle) {
                result.add(I18n.format("gregtech.multiblock.pattern.single"));
            }
            if (predicates.hasAir) {
                result.add(I18n.format("gregtech.multiblock.pattern.replaceable_air"));
            }
            return result;
        }

        public boolean test(BlockWorldState blockWorldState) {
            return predicate.test(blockWorldState);
        }

        public boolean testLimited(BlockWorldState blockWorldState) {
            return testGlobal(blockWorldState) && testLayer(blockWorldState);
        }

        public boolean testGlobal(BlockWorldState blockWorldState) {
            if (minGlobalCount == -1 && maxGlobalCount == -1) return true;
            Integer count = blockWorldState.globalCount.get(this);
            boolean base = predicate.test(blockWorldState);
            count = (count == null ? 0 : count) + (base ? 1 : 0);
            blockWorldState.globalCount.put(this, count);
            if (maxGlobalCount == -1 || count <= maxGlobalCount) return base;
            blockWorldState.setError(new SinglePredicateError(this, 0));
            return false;
        }

        public boolean testLayer(BlockWorldState blockWorldState) {
            if (minLayerCount == -1 && maxLayerCount == -1) return true;
            Integer count = blockWorldState.layerCount.get(this);
            boolean base = predicate.test(blockWorldState);
            count = (count == null ? 0 : count) + (base ? 1 : 0);
            blockWorldState.layerCount.put(this, count);
            if (maxLayerCount == -1 || count <= maxLayerCount) return base;
            blockWorldState.setError(new SinglePredicateError(this, 2));
            return false;
        }

        public List<ItemStack> getCandidates() {
            if (candidates == null) return Collections.emptyList();
            if (cache == null) cache = this.candidates.get();

            ImmutableList.Builder<ItemStack> stacks = ImmutableList.builder();
            for (var info : this.cache) {
                if (info.isAir()) continue;
                IBlockState blockState = info.getBlockState();
                MetaTileEntity metaTileEntity = info.getTileEntity() instanceof IGregTechTileEntity ?
                        ((IGregTechTileEntity) info.getTileEntity()).getMetaTileEntity() : null;
                if (metaTileEntity != null) {
                    stacks.add(metaTileEntity.getStackForm());
                } else {
                    stacks.add(new ItemStack(blockState.getBlock(), 1,
                            blockState.getBlock().damageDropped(blockState)));
                }
            }
            return stacks.build();
        }
    }

    public static class SinglePredicateError extends PatternError {

        public final SimplePredicate predicate;
        public final int type;

        public SinglePredicateError(SimplePredicate predicate, int type) {
            this.predicate = predicate;
            this.type = type;
        }

        @Override
        public List<List<ItemStack>> getCandidates() {
            return Collections.singletonList(predicate.getCandidates());
        }

        @SideOnly(Side.CLIENT)
        @Override
        public String getErrorInfo() {
            int number = -1;
            if (type == 0) number = predicate.maxGlobalCount;
            if (type == 1) number = predicate.minGlobalCount;
            if (type == 2) number = predicate.maxLayerCount;
            if (type == 3) number = predicate.minLayerCount;
            return I18n.format("gregtech.multiblock.pattern.error.limited." + type, number);
        }
    }
}
