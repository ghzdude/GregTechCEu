package gregtech.common.metatileentities.storage;

import codechicken.lib.raytracer.CuboidRayTraceResult;
import codechicken.lib.render.CCRenderState;
import codechicken.lib.render.pipeline.ColourMultiplier;
import codechicken.lib.render.pipeline.IVertexOperation;
import codechicken.lib.vec.Matrix4;
import gregtech.api.capability.IActiveOutputSide;
import gregtech.api.capability.impl.FilteredFluidHandler;
import gregtech.api.capability.impl.FluidTankList;
import gregtech.api.gui.GuiTextures;
import gregtech.api.gui.ModularUI;
import gregtech.api.gui.widgets.TankWidget;
import gregtech.api.gui.widgets.ToggleButtonWidget;
import gregtech.api.metatileentity.ITieredMetaTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.interfaces.IGregTechTileEntity;
import gregtech.client.renderer.texture.Textures;
import gregtech.api.util.GTUtility;
import gregtech.client.utils.RenderUtil;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import java.util.List;

import static gregtech.api.capability.GregtechDataCodes.*;

public class MetaTileEntityBuffer extends MetaTileEntity implements ITieredMetaTileEntity, IActiveOutputSide {

    private static final int TANK_SIZE = 64000;
    private final int tier;

    private FluidTankList fluidTankList;
    private ItemStackHandler itemStackHandler;
    private boolean autoOutputFluids = false;
    private boolean autoOutputItems = false;
    private boolean allowInOnFluidOut = false;
    private boolean allowInOnItemOout = false;
    private EnumFacing itemOutputFacing = getFrontFacing().getOpposite();
    private EnumFacing fluidOutputFacing = itemOutputFacing;

    public MetaTileEntityBuffer(ResourceLocation metaTileEntityId, int tier) {
        super(metaTileEntityId);
        this.tier = tier;
        initializeInventory();
    }

    @Override
    protected void initializeInventory() {
        super.initializeInventory();
        FilteredFluidHandler[] fluidHandlers = new FilteredFluidHandler[tier + 2];
        for (int i = 0; i < tier + 2; i++) {
            fluidHandlers[i] = new FilteredFluidHandler(TANK_SIZE);
        }
        fluidInventory = fluidTankList = new FluidTankList(false, fluidHandlers);
        itemInventory = itemStackHandler = new ItemStackHandler((int)Math.pow(tier + 2, 2));
    }

    @Override
    public MetaTileEntity createMetaTileEntity(IGregTechTileEntity tileEntity) {
        return new MetaTileEntityBuffer(metaTileEntityId, tier);
    }

    @Override
    public Pair<TextureAtlasSprite, Integer> getParticleTexture() {
        return Pair.of(Textures.VOLTAGE_CASINGS[tier].getParticleSprite(), this.getPaintingColorForRendering());
    }

    @Override
    protected ModularUI createUI(EntityPlayer entityPlayer) {
        int invTier = tier + 2;
        ModularUI.Builder builder = ModularUI.builder(GuiTextures.BACKGROUND,
                176, Math.max(166, 18 + 18 * invTier + 94));//176, 166
        for (int i = 0; i < this.fluidTankList.getTanks(); i++) {
            builder.widget(new TankWidget(this.fluidTankList.getTankAt(i), 176 - 8 - 18, 18 + 18 * i, 18, 18)
                    .setAlwaysShowFull(true)
                    .setBackgroundTexture(GuiTextures.FLUID_SLOT)
                    .setContainerClicking(true, true));
        }
        for (int y = 0; y < invTier; y++) {
            for (int x = 0; x < invTier; x++) {
                int index = y * invTier + x;
                builder.slot(itemStackHandler, index, 8 + x * 18, 18 + y * 18, GuiTextures.SLOT);
            }
        }
        return builder.label(6, 6, getMetaFullName())
                .widget(new ToggleButtonWidget(7, 53, 18, 18,
                        GuiTextures.BUTTON_ITEM_OUTPUT, this::isAutoOutputItems, this::setAutoOutputItems)
                        .shouldUseBaseBackground()
                        .setTooltipText("gregtech.gui.item_auto_output.tooltip"))
                .widget(new ToggleButtonWidget(7 + 18, 53, 18, 18,
                        GuiTextures.BUTTON_FLUID_OUTPUT, this::isAutoOutputFluids, this::setAutoOutputFluids)
                        .shouldUseBaseBackground()
                        .setTooltipText("gregtech.gui.fluid_auto_output.tooltip"))
                .bindPlayerInventory(entityPlayer.inventory, GuiTextures.SLOT, 8, 18 + 18 * invTier + 12)
                .build(getHolder(), entityPlayer);
    }

    @Override
    public int getTier() {
        return tier;
    }

    @Override
    public void renderMetaTileEntity(CCRenderState renderState, Matrix4 translation, IVertexOperation[] pipeline) {
        Textures.VOLTAGE_CASINGS[tier].render(renderState, translation, ArrayUtils.add(pipeline,
                new ColourMultiplier(GTUtility.convertRGBtoOpaqueRGBA_CL(getPaintingColorForRendering()))));
        for (EnumFacing facing : EnumFacing.VALUES) {
            Textures.BUFFER_OVERLAY.renderSided(facing, renderState, translation, pipeline);
        }
        Textures.PIPE_OUT_OVERLAY.renderSided(itemOutputFacing, renderState, translation, pipeline);
        if (fluidOutputFacing != null) {
            Textures.PIPE_OUT_OVERLAY.renderSided(fluidOutputFacing, renderState, RenderUtil.adjustTrans(translation, fluidOutputFacing, 2), pipeline);
        }
        if (itemOutputFacing != null) {
            Textures.PIPE_OUT_OVERLAY.renderSided(itemOutputFacing, renderState, RenderUtil.adjustTrans(translation, itemOutputFacing, 2), pipeline);
        }
        if (isAutoOutputItems() && itemOutputFacing != null) {
            Textures.ITEM_OUTPUT_OVERLAY.renderSided(itemOutputFacing, renderState, RenderUtil.adjustTrans(translation, itemOutputFacing, 2), pipeline);
        }
        if (isAutoOutputFluids() && fluidOutputFacing != null) {
            Textures.FLUID_OUTPUT_OVERLAY.renderSided(fluidOutputFacing, renderState, RenderUtil.adjustTrans(translation, fluidOutputFacing, 2), pipeline);
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
        tag.setTag("Inventory", itemStackHandler.serializeNBT());
        tag.setTag("FluidInventory", fluidTankList.serializeNBT());
        return tag;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        this.itemStackHandler.deserializeNBT(tag.getCompoundTag("Inventory"));
        this.fluidTankList.deserializeNBT(tag.getCompoundTag("FluidInventory"));
    }

    @Override
    protected boolean shouldSerializeInventories() {
        return false;
    }

    @Override
    public boolean hasFrontFacing() {
        return false;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, boolean advanced) {
        super.addInformation(stack, player, tooltip, advanced);
        tooltip.add(I18n.format("gregtech.machine.buffer.tooltip"));
        tooltip.add(I18n.format("gregtech.universal.tooltip.item_storage_capacity", (int) Math.pow(tier + 2, 2)));
        tooltip.add(I18n.format("gregtech.universal.tooltip.fluid_storage_capacity_mult", tier + 2, TANK_SIZE));
    }

    @Override
    public void addToolUsages(ItemStack stack, @Nullable World world, List<String> tooltip, boolean advanced) {
        tooltip.add(I18n.format("gregtech.tool_action.screwdriver.access_covers"));
        // TODO Add this when the Buffer gets an auto-output side, and change the above to
        // "gregtech.tool_action.screwdriver.auto_output_covers"
        //tooltip.add(I18n.format("gregtech.tool_action.wrench.set_facing"));
        super.addToolUsages(stack, world, tooltip, advanced);
    }

    @Override
    public void clearMachineInventory(NonNullList<ItemStack> itemBuffer) {
        clearInventory(itemBuffer, itemStackHandler);
    }

    public void setOutputFacing(EnumFacing outputFacing) {
        this.itemOutputFacing = outputFacing;
        this.fluidOutputFacing = outputFacing;
        if (!getWorld().isRemote) {
            notifyBlockUpdate();
            writeCustomData(UPDATE_OUTPUT_FACING, buf -> buf.writeByte(outputFacing.getIndex()));
            markDirty();
        }
    }

    @Override
    public boolean onWrenchClick(EntityPlayer playerIn, EnumHand hand, EnumFacing wrenchSide, CuboidRayTraceResult hitResult) {
        if (!playerIn.isSneaking()) {
            if (getFrontFacing().getOpposite() == wrenchSide) {
                return false;
            }
            if (!getWorld().isRemote) {
                setOutputFacing(wrenchSide);
            }
            return true;
        }
        return super.onWrenchClick(playerIn, hand, wrenchSide, hitResult);
    }

    @Override
    public boolean isAutoOutputItems() {
        return this.autoOutputItems;
    }

    public void setAutoOutputItems(boolean autoOutputItems) {
        this.autoOutputItems = autoOutputItems;
        if (!getWorld().isRemote) {
            writeCustomData(UPDATE_AUTO_OUTPUT_ITEMS, buf -> buf.writeBoolean(autoOutputItems));
            markDirty();
        }
    }

    @Override
    public boolean isAutoOutputFluids() {
        return this.autoOutputFluids;
    }

    public void setAutoOutputFluids(boolean autoOutputFluids) {
        this.autoOutputFluids = autoOutputFluids;
        if (!getWorld().isRemote) {
            writeCustomData(UPDATE_AUTO_OUTPUT_FLUIDS, buf -> buf.writeBoolean(autoOutputFluids));
            markDirty();
        }
    }

    @Override
    public boolean isAllowInputFromOutputSideItems() {
        return this.allowInOnItemOout;
    }

    @Override
    public boolean isAllowInputFromOutputSideFluids() {
        return this.allowInOnFluidOut;
    }
}
