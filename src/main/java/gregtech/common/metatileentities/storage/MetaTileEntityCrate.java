package gregtech.common.metatileentities.storage;

import codechicken.lib.colour.ColourRGBA;
import codechicken.lib.render.CCRenderState;
import codechicken.lib.render.pipeline.IVertexOperation;
import codechicken.lib.vec.Matrix4;
import gregtech.api.gui.GuiTextures;
import gregtech.api.gui.ModularUI;
import gregtech.api.gui.ModularUI.Builder;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.interfaces.IGregTechTileEntity;
import gregtech.api.metatileentity.interfaces.IMetaTileEntity;
import gregtech.api.metatileentity.interfaces.IMetaTileEntity.IMTEGetComparatorInputOverride;
import gregtech.api.recipes.ModHandler;
import gregtech.api.unification.material.Material;
import gregtech.api.util.GTUtility;
import gregtech.api.util.InventoryUtils;
import gregtech.client.renderer.texture.Textures;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import java.util.List;

public class MetaTileEntityCrate extends MetaTileEntity implements IMTEGetComparatorInputOverride {

    private final Material material;
    private final int inventorySize;
    private ItemStackHandler inventory;

    public MetaTileEntityCrate(ResourceLocation metaTileEntityId, Material material, int inventorySize) {
        super(metaTileEntityId);
        this.material = material;
        this.inventorySize = inventorySize;
        initializeInventory();
    }

    @Override
    public IMetaTileEntity createMetaTileEntity(IGregTechTileEntity tileEntity) {
        return new MetaTileEntityCrate(metaTileEntityId, material, inventorySize);
    }

    @Override
    public boolean hasFrontFacing() {
        return false;
    }

    @Override
    public String getHarvestTool() {
        return material.toString().contains("wood") ? "axe" : "pickaxe";
    }

    @Override
    protected void initializeInventory() {
        super.initializeInventory();
        this.inventory = new ItemStackHandler(inventorySize) {
            @Override
            protected void onContentsChanged(int slot) {
                super.onContentsChanged(slot);
            }
        };
        this.itemInventory = inventory;
    }

    @Override
    public int getComparatorInputOverride() {
        return ItemHandlerHelper.calcRedstoneFromInventory(inventory);
    }

    @Override
    public void clearMachineInventory(NonNullList<ItemStack> itemBuffer) {
        InventoryUtils.clearInventory(itemBuffer, inventory);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Pair<TextureAtlasSprite, Integer> getParticleTexture() {
        if (ModHandler.isMaterialWood(material)) {
            return Pair.of(Textures.WOODEN_CRATE.getParticleTexture(), getPaintingColorForRendering());
        } else {
            int color = ColourRGBA.multiply(
                    GTUtility.convertRGBtoOpaqueRGBA_CL(material.getMaterialRGB()),
                    GTUtility.convertRGBtoOpaqueRGBA_CL(getPaintingColorForRendering()));
            color = GTUtility.convertOpaqueRGBA_CLtoRGB(color);
            return Pair.of(Textures.METAL_CRATE.getParticleTexture(), color);
        }
    }

    @Override
    public void renderTileEntity(CCRenderState renderState, Matrix4 translation, IVertexOperation[] pipeline) {
        if (material.toString().contains("wood")) {
            Textures.WOODEN_CRATE.render(renderState, translation, GTUtility.convertRGBtoOpaqueRGBA_CL(getPaintingColorForRendering()), pipeline);
        } else {
            int baseColor = ColourRGBA.multiply(GTUtility.convertRGBtoOpaqueRGBA_CL(material.getMaterialRGB()), GTUtility.convertRGBtoOpaqueRGBA_CL(getPaintingColorForRendering()));
            Textures.METAL_CRATE.render(renderState, translation, baseColor, pipeline);
        }
    }

    @Override
    public int getDefaultPaintingColor() {
        return 0xFFFFFF;
    }

    @Override
    public ModularUI createUI(EntityPlayer entityPlayer) {
        int factor = inventorySize / 9 > 8 ? 18 : 9;
        Builder builder = ModularUI.builder(GuiTextures.BACKGROUND, 176 + (factor == 18 ? 176 : 0), 8 + inventorySize / factor * 18 + 104).label(5, 5, getMetaFullName());
        for (int i = 0; i < inventorySize; i++) {
            builder.slot(inventory, i, 7 * (factor == 18 ? 2 : 1) + i % factor * 18, 18 + i / factor * 18, GuiTextures.SLOT);
        }
        builder.bindPlayerInventory(entityPlayer.inventory, GuiTextures.SLOT, 7 + (factor == 18 ? 88 : 0), 18 + inventorySize / factor * 18 + 11);
        return builder.build(getTileEntity(), entityPlayer);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound data) {
        super.writeToNBT(data);
        data.setTag("Inventory", inventory.serializeNBT());
        return data;
    }

    @Override
    public void readFromNBT(NBTTagCompound data) {
        super.readFromNBT(data);
        this.inventory.deserializeNBT(data.getCompoundTag("Inventory"));
    }

    @Override
    protected boolean shouldSerializeInventories() {
        return false;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, boolean advanced) {
        tooltip.add(I18n.format("gregtech.universal.tooltip.item_storage_capacity", inventorySize));
    }
}
