package gregtech.common.covers;

import codechicken.lib.raytracer.CuboidRayTraceResult;
import codechicken.lib.render.CCRenderState;
import codechicken.lib.render.pipeline.IVertexOperation;
import codechicken.lib.vec.Cuboid6;
import codechicken.lib.vec.Matrix4;
import gregtech.api.capability.impl.ItemHandlerDelegate;
import gregtech.api.cover.CoverBehavior;
import gregtech.api.cover.CoverWithUI;
import gregtech.api.cover.ICoverable;
import gregtech.api.gui.GuiTextures;
import gregtech.api.gui.ModularUI;
import gregtech.api.gui.widgets.CycleButtonWidget;
import gregtech.api.gui.widgets.LabelWidget;
import gregtech.api.gui.widgets.WidgetGroup;
import gregtech.api.util.GTUtility;
import gregtech.client.renderer.texture.cube.SimpleOverlayRenderer;
import gregtech.common.covers.filter.BigItemFilterWrapper;
import gregtech.common.covers.filter.ItemFilter;
import gregtech.common.covers.filter.ItemFilterWrapper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;

public class CoverBigItemFilter extends CoverItemFilter {

    protected final BigItemFilterWrapper itemFilter;

    public CoverBigItemFilter(ICoverable coverHolder, EnumFacing attachedSide, String titleLocale, SimpleOverlayRenderer texture, ItemFilter itemFilter) {
        super(coverHolder, attachedSide, titleLocale, texture, itemFilter);
        this.itemFilter = new BigItemFilterWrapper(this);
    }

    @Override
    public ModularUI createUI(EntityPlayer player) {
        WidgetGroup filterGroup = new WidgetGroup();
        filterGroup.addWidget(new LabelWidget(10, 5, titleLocale));
        filterGroup.addWidget(new CycleButtonWidget(10, 105 - 3, 110, 20,
                GTUtility.mapToString(ItemFilterMode.values(), it -> it.localeName),
                () -> filterMode.ordinal(), (newMode) -> setFilterMode(ItemFilterMode.values()[newMode])));
        this.itemFilter.initUI(45, filterGroup::addWidget);
        this.itemFilter.blacklistUI(45, filterGroup::addWidget, () -> true);
        return ModularUI.builder(GuiTextures.BACKGROUND, 176, 105 + 82 + 18)
                .widget(filterGroup)
                .bindPlayerInventory(player.inventory, GuiTextures.SLOT, 7, 105 + 18)
                .build(this, player);
    }
}
