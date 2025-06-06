package gregtech.loaders;

import gregtech.api.GregTechAPI;
import gregtech.api.unification.material.Materials;
import gregtech.api.unification.stack.MaterialStack;
import gregtech.api.unification.stack.RecyclingData;
import gregtech.common.ConfigHolder;
import gregtech.common.blocks.*;
import gregtech.common.blocks.BlockWireCoil.CoilType;
import gregtech.common.metatileentities.MetaTileEntities;
import gregtech.loaders.recipe.WoodRecipeLoader;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import static gregtech.api.GTValues.M;
import static gregtech.api.GTValues.W;
import static gregtech.api.unification.material.Materials.*;
import static gregtech.common.metatileentities.MetaTileEntities.LONG_DIST_FLUID_ENDPOINT;
import static gregtech.common.metatileentities.MetaTileEntities.LONG_DIST_ITEM_ENDPOINT;

public class MaterialInfoLoader {

    public static void init() {
        GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(MetaBlocks.WIRE_COIL.getItemVariant(CoilType.CUPRONICKEL),
                new RecyclingData(new MaterialStack(Materials.Cupronickel, M * 8), // double wire
                        new MaterialStack(Materials.Bronze, M * 2), // foil
                        new MaterialStack(Materials.TinAlloy, M)) // ingot
        );
        GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(MetaBlocks.WIRE_COIL.getItemVariant(CoilType.KANTHAL),
                new RecyclingData(new MaterialStack(Materials.Kanthal, M * 8), // double wire
                        new MaterialStack(Materials.Aluminium, M * 2), // foil
                        new MaterialStack(Materials.Copper, M)) // ingot
        );
        GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(MetaBlocks.WIRE_COIL.getItemVariant(CoilType.NICHROME),
                new RecyclingData(new MaterialStack(Materials.Nichrome, M * 8), // double wire
                        new MaterialStack(Materials.StainlessSteel, M * 2), // foil
                        new MaterialStack(Materials.Aluminium, M)) // ingot
        );
        GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(MetaBlocks.WIRE_COIL.getItemVariant(CoilType.RTM_ALLOY),
                new RecyclingData(new MaterialStack(Materials.RTMAlloy, M * 8), // double wire
                        new MaterialStack(Materials.VanadiumSteel, M * 2), // foil
                        new MaterialStack(Materials.Nichrome, M)) // ingot
        );
        GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(MetaBlocks.WIRE_COIL.getItemVariant(CoilType.HSS_G),
                new RecyclingData(new MaterialStack(Materials.HSSG, M * 8), // double wire
                        new MaterialStack(Materials.TungstenCarbide, M * 2), // foil
                        new MaterialStack(Materials.Tungsten, M)) // ingot
        );
        GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(MetaBlocks.WIRE_COIL.getItemVariant(CoilType.NAQUADAH),
                new RecyclingData(new MaterialStack(Materials.Naquadah, M * 8), // double wire
                        new MaterialStack(Materials.Osmium, M * 2), // foil
                        new MaterialStack(Materials.TungstenSteel, M)) // ingot
        );
        GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(MetaBlocks.WIRE_COIL.getItemVariant(CoilType.TRINIUM),
                new RecyclingData(new MaterialStack(Materials.Trinium, M * 8), // double wire
                        new MaterialStack(Materials.NaquadahEnriched, M * 2), // foil
                        new MaterialStack(Materials.Naquadah, M)) // ingot
        );
        GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(MetaBlocks.WIRE_COIL.getItemVariant(CoilType.TRITANIUM),
                new RecyclingData(new MaterialStack(Materials.Tritanium, M * 8), // double wire
                        new MaterialStack(Materials.Naquadria, M * 2), // foil
                        new MaterialStack(Materials.Trinium, M)) // ingot
        );

        GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(MetaTileEntities.HULL[0].getStackForm(), new RecyclingData(
                new MaterialStack(Materials.WroughtIron, M * 8), // plate
                new MaterialStack(Materials.RedAlloy, M), // single cable
                new MaterialStack(Materials.Rubber, M * 2))); // plate

        GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(MetaTileEntities.HULL[1].getStackForm(), new RecyclingData(
                new MaterialStack(Materials.Steel, M * 8), // plate
                new MaterialStack(Materials.Tin, M), // single cable
                new MaterialStack(Materials.Rubber, M * 2))); // plate

        GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(MetaTileEntities.HULL[2].getStackForm(), new RecyclingData(
                new MaterialStack(Materials.Aluminium, M * 8), // plate
                new MaterialStack(Materials.Copper, M), // single cable
                new MaterialStack(Materials.Rubber, M * 2))); // plate

        GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(MetaTileEntities.HULL[3].getStackForm(), new RecyclingData(
                new MaterialStack(Materials.StainlessSteel, M * 8), // plate
                new MaterialStack(Materials.Gold, M), // single cable
                new MaterialStack(Materials.Rubber, M * 2))); // plate

        GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(MetaTileEntities.HULL[4].getStackForm(), new RecyclingData(
                new MaterialStack(Materials.Titanium, M * 8), // plate
                new MaterialStack(Materials.Aluminium, M), // single cable
                new MaterialStack(Materials.Rubber, M * 2))); // plate

        GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(MetaTileEntities.HULL[5].getStackForm(), new RecyclingData(
                new MaterialStack(Materials.TungstenSteel, M * 8), // plate
                new MaterialStack(Materials.Platinum, M), // single cable
                new MaterialStack(Materials.Rubber, M * 2))); // plate

        GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(MetaTileEntities.HULL[6].getStackForm(), new RecyclingData(
                new MaterialStack(Materials.RhodiumPlatedPalladium, M * 8), // plate
                new MaterialStack(Materials.NiobiumTitanium, M), // single cable
                new MaterialStack(Materials.Rubber, M * 2))); // plate

        GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(MetaTileEntities.HULL[7].getStackForm(), new RecyclingData(
                new MaterialStack(Materials.NaquadahAlloy, M * 8), // plate
                new MaterialStack(Materials.VanadiumGallium, M), // single cable
                new MaterialStack(Materials.Rubber, M * 2))); // plate

        GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(MetaTileEntities.HULL[8].getStackForm(), new RecyclingData(
                new MaterialStack(Materials.Darmstadtium, M * 8), // plate
                new MaterialStack(Materials.YttriumBariumCuprate, M), // single cable
                new MaterialStack(Materials.Rubber, M * 2))); // plate

        GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(MetaTileEntities.HULL[9].getStackForm(), new RecyclingData(
                new MaterialStack(Materials.Neutronium, M * 8), // plate
                new MaterialStack(Materials.Europium, M), // single cable
                new MaterialStack(Materials.Rubber, M * 2))); // plate

        GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(MetaTileEntities.ENERGY_INPUT_HATCH[3].getStackForm(),
                new RecyclingData(
                        new MaterialStack(Materials.StainlessSteel, M * 8), // plate
                        new MaterialStack(Materials.Gold, M * 2), // single cable
                        new MaterialStack(Materials.Rubber, M * 4), // plate
                        new MaterialStack(Materials.BlackSteel, M * 2), // fine wire
                        new MaterialStack(Materials.SteelMagnetic, M / 2) // rod
                ));

        GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(MetaTileEntities.ENERGY_INPUT_HATCH[4].getStackForm(),
                new RecyclingData(
                        new MaterialStack(Materials.Titanium, M * 8), // plate
                        new MaterialStack(Materials.Aluminium, M * 2), // single cable
                        new MaterialStack(Materials.Rubber, M * 4), // plate
                        new MaterialStack(Materials.TungstenSteel, M * 2), // fine wire
                        new MaterialStack(Materials.NeodymiumMagnetic, M / 2) // rod
                ));

        GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(MetaTileEntities.ENERGY_INPUT_HATCH[5].getStackForm(),
                new RecyclingData(
                        new MaterialStack(Materials.TungstenSteel, M * 8), // plate
                        new MaterialStack(Materials.Platinum, M), // single cable
                        new MaterialStack(Materials.Tungsten, M), // single cable
                        new MaterialStack(Materials.Rubber, M * 4), // plate
                        new MaterialStack(Materials.Iridium, M * 2), // fine wire
                        new MaterialStack(Materials.NeodymiumMagnetic, M / 2) // rod
                ));

        GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(MetaTileEntities.ENERGY_OUTPUT_HATCH[3].getStackForm(),
                new RecyclingData(
                        new MaterialStack(Materials.StainlessSteel, M * 8), // plate
                        new MaterialStack(Materials.Gold, 3 * M), // single cable + spring
                        new MaterialStack(Materials.Rubber, M * 2), // plate
                        new MaterialStack(Materials.BlackSteel, M * 2), // fine wire
                        new MaterialStack(Materials.SteelMagnetic, M / 2) // rod
                ));

        GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(MetaTileEntities.ENERGY_OUTPUT_HATCH[4].getStackForm(),
                new RecyclingData(
                        new MaterialStack(Materials.Titanium, M * 8), // plate
                        new MaterialStack(Materials.Aluminium, 3 * M), // single cable + spring
                        new MaterialStack(Materials.Rubber, M * 2), // plate
                        new MaterialStack(Materials.TungstenSteel, M * 2), // fine wire
                        new MaterialStack(Materials.NeodymiumMagnetic, M / 2) // rod
                ));

        GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(MetaTileEntities.ENERGY_OUTPUT_HATCH[5].getStackForm(),
                new RecyclingData(
                        new MaterialStack(Materials.TungstenSteel, M * 8), // plate
                        new MaterialStack(Materials.Platinum, M), // single cable
                        new MaterialStack(Materials.Tungsten, M * 2), // spring
                        new MaterialStack(Materials.Rubber, M * 2), // plate
                        new MaterialStack(Materials.Iridium, M * 2), // fine wire
                        new MaterialStack(Materials.NeodymiumMagnetic, M / 2) // rod
                ));

        GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(
                MetaBlocks.CLEANROOM_CASING.getItemVariant(BlockCleanroomCasing.CasingType.PLASCRETE),
                new RecyclingData(
                        new MaterialStack(Materials.Steel, (M * 2) / ConfigHolder.recipes.casingsPerCraft), // frame /
                                                                                                            // config
                        new MaterialStack(Materials.Polyethylene, (M * 6) / ConfigHolder.recipes.casingsPerCraft), // 6
                                                                                                                   // sheets
                                                                                                                   // /
                                                                                                                   // config
                        new MaterialStack(Materials.Concrete, M / ConfigHolder.recipes.casingsPerCraft) // 1 block /
                                                                                                        // config
                ));

        GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(
                MetaBlocks.TRANSPARENT_CASING.getItemVariant(BlockGlassCasing.CasingType.CLEANROOM_GLASS),
                new RecyclingData(
                        new MaterialStack(Materials.Steel, (M * 2) / ConfigHolder.recipes.casingsPerCraft), // frame /
                                                                                                            // config
                        new MaterialStack(Materials.Polyethylene, (M * 6) / ConfigHolder.recipes.casingsPerCraft), // 6
                                                                                                                   // sheets
                                                                                                                   // /
                                                                                                                   // config
                        new MaterialStack(Materials.Glass, M / ConfigHolder.recipes.casingsPerCraft) // 1 block / config
                ));

        GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(
                MetaBlocks.METAL_CASING.getItemVariant(BlockMetalCasing.MetalCasingType.PTFE_INERT_CASING),
                new RecyclingData(
                        new MaterialStack(Materials.Steel, (M * 8) / ConfigHolder.recipes.casingsPerCraft), // casing /
                                                                                                            // config
                        new MaterialStack(Materials.Polytetrafluoroethylene, M * 3 / 2) // 1.5 ingots PTFE (fluid in
                                                                                        // recipe)
                ));

        GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(
                MetaBlocks.METAL_CASING.getItemVariant(BlockMetalCasing.MetalCasingType.PRIMITIVE_BRICKS),
                new RecyclingData(new MaterialStack(Materials.Fireclay, M * 4)));

        GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(
                MetaBlocks.BATTERY_BLOCK.getItemVariant(BlockBatteryPart.BatteryPartType.EMPTY_TIER_I),
                new RecyclingData(
                        new MaterialStack(Materials.Ultimet, M * 2 + M * 6 + (M / 9 * 24)))); // frame + 6 plates + 24
                                                                                              // screws
        GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(
                MetaBlocks.BATTERY_BLOCK.getItemVariant(BlockBatteryPart.BatteryPartType.EMPTY_TIER_II),
                new RecyclingData(
                        new MaterialStack(Materials.Ruridit, M * 2 + M * 6 + (M / 9 * 24)))); // frame + 6 plates + 24
                                                                                              // screws
        GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(
                MetaBlocks.BATTERY_BLOCK.getItemVariant(BlockBatteryPart.BatteryPartType.EMPTY_TIER_III),
                new RecyclingData(
                        new MaterialStack(Materials.Neutronium, M * 2 + M * 6 + (M / 9 * 24)))); // frame + 6 plates +
                                                                                                 // 24 screws

        GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(LONG_DIST_ITEM_ENDPOINT.getStackForm(),
                new RecyclingData(new MaterialStack(Tin, M * 6), // large pipe
                        new MaterialStack(Steel, M * 8))); // 4 plates + 1 gear

        GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(LONG_DIST_FLUID_ENDPOINT.getStackForm(),
                new RecyclingData(new MaterialStack(Bronze, M * 6), // large pipe
                        new MaterialStack(Steel, M * 8))); // 4 plates + 1 gear

        GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(new ItemStack(MetaBlocks.LD_ITEM_PIPE),
                new RecyclingData(new MaterialStack(Tin, M * 6 * 2 / 64), // 2 large pipe / 64
                        new MaterialStack(Steel, M * 8 / 64))); // 8 steel plate / 64

        GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(new ItemStack(MetaBlocks.LD_FLUID_PIPE),
                new RecyclingData(new MaterialStack(Bronze, M * 6 * 2 / 64), // 2 large pipe / 64
                        new MaterialStack(Steel, M * 8 / 64))); // 8 steel plate / 64

        if (ConfigHolder.recipes.hardAdvancedIronRecipes) {
            GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(new ItemStack(Items.IRON_DOOR, 1), new RecyclingData(
                    new MaterialStack(Materials.Iron, M * 4 + (M * 3 / 16)), // 4 iron plates + 1 iron bars
                    new MaterialStack(Materials.Steel, M / 9))); // tiny steel dust
        } else {
            GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(new ItemStack(Items.IRON_DOOR, 1),
                    new RecyclingData(new MaterialStack(Materials.Iron, M * 2)));
        }

        GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(new ItemStack(Blocks.STONE_STAIRS, 1),
                new RecyclingData(new MaterialStack(Materials.Stone, (3 * M) / 2))); // dust small
        GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(new ItemStack(Blocks.SANDSTONE_STAIRS, 1),
                new RecyclingData(new MaterialStack(Materials.Stone, (3 * M) / 2))); // dust small
        GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(new ItemStack(Blocks.RED_SANDSTONE_STAIRS, 1),
                new RecyclingData(new MaterialStack(Materials.Stone, (3 * M) / 2))); // dust small
        GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(new ItemStack(Blocks.STONE_BRICK_STAIRS, 1),
                new RecyclingData(new MaterialStack(Materials.Stone, (3 * M) / 2))); // dust small
        GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(new ItemStack(Blocks.QUARTZ_STAIRS, 1),
                new RecyclingData(new MaterialStack(Materials.NetherQuartz, M * 6))); // dust
        GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(new ItemStack(Blocks.BRICK_STAIRS, 1),
                new RecyclingData(new MaterialStack(Materials.Brick, M * 6))); // dust
        GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(new ItemStack(Blocks.NETHER_BRICK_STAIRS, 1),
                new RecyclingData(new MaterialStack(Materials.Netherrack, M * 6))); // dust

        GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(new ItemStack(Blocks.STONE_SLAB, 1, 0),
                new RecyclingData(new MaterialStack(Materials.Stone, M / 2)));
        GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(new ItemStack(Blocks.STONE_SLAB, 1, 1),
                new RecyclingData(new MaterialStack(Materials.Stone, M / 2)));
        GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(new ItemStack(Blocks.STONE_SLAB, 1, 2),
                new RecyclingData(new MaterialStack(Materials.Stone, M / 2)));
        GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(new ItemStack(Blocks.STONE_SLAB, 1, 3),
                new RecyclingData(new MaterialStack(Materials.Stone, M / 2)));
        GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(new ItemStack(Blocks.STONE_SLAB, 1, 4),
                new RecyclingData(new MaterialStack(Materials.Brick, M * 2)));
        GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(new ItemStack(Blocks.STONE_SLAB, 1, 5),
                new RecyclingData(new MaterialStack(Materials.Stone, M / 2)));
        GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(new ItemStack(Blocks.STONE_SLAB, 1, 6),
                new RecyclingData(new MaterialStack(Materials.Netherrack, M * 2)));
        GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(new ItemStack(Blocks.STONE_SLAB, 1, 7),
                new RecyclingData(new MaterialStack(Materials.NetherQuartz, M * 2)));

        GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(new ItemStack(Blocks.LEVER, 1, W), new RecyclingData(
                new MaterialStack(Materials.Stone, M / 9), new MaterialStack(Materials.Wood, 1814400L)));
        GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(new ItemStack(Blocks.WOODEN_BUTTON, 1, W),
                new RecyclingData(new MaterialStack(Materials.Wood, M / 9)));
        GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(new ItemStack(Blocks.STONE_BUTTON, 1, W),
                new RecyclingData(new MaterialStack(Materials.Stone, M / 9)));
        GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(new ItemStack(Blocks.REDSTONE_TORCH, 1, W),
                new RecyclingData(
                        new MaterialStack(Materials.Wood, M / 2), new MaterialStack(Materials.Redstone, M)));

        GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(new ItemStack(Blocks.RAIL, 1),
                new RecyclingData(new MaterialStack(Materials.Iron, M * 3 / 16)));
        GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(new ItemStack(Blocks.GOLDEN_RAIL, 1),
                new RecyclingData(new MaterialStack(Materials.Gold, M / 2)));
        GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(new ItemStack(Blocks.DETECTOR_RAIL, 1),
                new RecyclingData(new MaterialStack(Materials.Iron, M / 2)));
        GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(new ItemStack(Blocks.ACTIVATOR_RAIL, 1),
                new RecyclingData(new MaterialStack(Materials.Iron, M / 2)));

        if (ConfigHolder.recipes.hardRedstoneRecipes) {
            GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(new ItemStack(Blocks.WOODEN_PRESSURE_PLATE, 1, W),
                    new RecyclingData(
                            new MaterialStack(Materials.Wood, M), new MaterialStack(Materials.Iron, M / 2)));
            GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(new ItemStack(Blocks.STONE_PRESSURE_PLATE, 1, W),
                    new RecyclingData(
                            new MaterialStack(Materials.Stone, M), new MaterialStack(Materials.Iron, M * 6 / 8)));
            GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(
                    new ItemStack(Blocks.LIGHT_WEIGHTED_PRESSURE_PLATE, 1, W),
                    new RecyclingData(new MaterialStack(Materials.Gold, M), new MaterialStack(Materials.Steel, M)));
            GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(
                    new ItemStack(Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE, 1, W),
                    new RecyclingData(new MaterialStack(Materials.Iron, M), new MaterialStack(Materials.Steel, M)));
        } else {
            GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(new ItemStack(Blocks.WOODEN_PRESSURE_PLATE, 1, W),
                    new RecyclingData(new MaterialStack(Materials.Wood, M * 2)));
            GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(new ItemStack(Blocks.STONE_PRESSURE_PLATE, 1, W),
                    new RecyclingData(new MaterialStack(Materials.Stone, M * 2)));
            GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(
                    new ItemStack(Blocks.LIGHT_WEIGHTED_PRESSURE_PLATE, 1, W),
                    new RecyclingData(new MaterialStack(Materials.Gold, M * 2)));
            GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(
                    new ItemStack(Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE, 1, W),
                    new RecyclingData(new MaterialStack(Materials.Iron, M * 2)));
        }

        GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(new ItemStack(Items.WHEAT, 1, W),
                new RecyclingData(new MaterialStack(Materials.Wheat, M)));
        GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(new ItemStack(Blocks.HAY_BLOCK, 1, W),
                new RecyclingData(new MaterialStack(Materials.Wheat, M * 9)));

        GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(new ItemStack(Items.SNOWBALL, 1, W),
                new RecyclingData(new MaterialStack(Materials.Water, M / 4)));
        GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(new ItemStack(Blocks.SNOW, 1, W),
                new RecyclingData(new MaterialStack(Materials.Water, M)));

        GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(new ItemStack(Blocks.PACKED_ICE, 1, W),
                new RecyclingData(new MaterialStack(Materials.Ice, M * 2)));

        GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(new ItemStack(Items.BOOK, 1, W),
                new RecyclingData(new MaterialStack(Materials.Paper, M * 3)));
        GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(new ItemStack(Items.WRITABLE_BOOK, 1, W),
                new RecyclingData(new MaterialStack(Materials.Paper, M * 3)));
        GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(new ItemStack(Items.ENCHANTED_BOOK, 1, W),
                new RecyclingData(new MaterialStack(Materials.Paper, M * 3)));
        GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(new ItemStack(Blocks.BOOKSHELF, 1), new RecyclingData(
                new MaterialStack(Materials.Paper, M * 9), new MaterialStack(Materials.Wood, M * 6)));
        GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(new ItemStack(Items.GOLDEN_APPLE, 1, 1),
                new RecyclingData(new MaterialStack(Materials.Gold, M * 72))); // block
        GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(new ItemStack(Items.GOLDEN_APPLE, 1, 0),
                new RecyclingData(new MaterialStack(Materials.Gold, M * 8))); // ingot

        GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(new ItemStack(Items.MINECART, 1),
                new RecyclingData(new MaterialStack(Materials.Iron, M * 5)));
        GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(new ItemStack(Items.CHEST_MINECART, 1), new RecyclingData(
                new MaterialStack(Materials.Iron, M * 5), new MaterialStack(Materials.Wood, M * 8)));
        GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(new ItemStack(Items.FURNACE_MINECART, 1), new RecyclingData(
                new MaterialStack(Materials.Iron, M * 5), new MaterialStack(Materials.Stone, M * 8)));
        GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(new ItemStack(Items.TNT_MINECART, 1),
                new RecyclingData(new MaterialStack(Materials.Iron, M * 5)));
        GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(new ItemStack(Items.HOPPER_MINECART, 1), new RecyclingData(
                new MaterialStack(Materials.Iron, M * 10), new MaterialStack(Materials.Wood, M * 8)));

        GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(new ItemStack(Items.CAULDRON, 1),
                new RecyclingData(new MaterialStack(Materials.Iron, M * 7)));
        GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(new ItemStack(Blocks.IRON_BARS, 8, W),
                new RecyclingData(new MaterialStack(Materials.Iron, M * 3 / 16)));
        GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(new ItemStack(Blocks.IRON_TRAPDOOR, 1),
                new RecyclingData(new MaterialStack(Materials.Iron, M * 4)));
        GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(new ItemStack(Items.BUCKET, 1),
                new RecyclingData(new MaterialStack(Materials.Iron, M * 3)));

        GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(new ItemStack(Blocks.ANVIL, 1, 0),
                new RecyclingData(new MaterialStack(Materials.Iron, M * 31)));
        GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(new ItemStack(Blocks.ANVIL, 1, 1),
                new RecyclingData(new MaterialStack(Materials.Iron, M * 22)));
        GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(new ItemStack(Blocks.ANVIL, 1, 2),
                new RecyclingData(new MaterialStack(Materials.Iron, M * 13)));
        GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(new ItemStack(Blocks.HOPPER, 1, W), new RecyclingData(
                new MaterialStack(Materials.Iron, M * 5), new MaterialStack(Materials.Wood, M * 8)));

        GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(new ItemStack(Items.GLASS_BOTTLE),
                new RecyclingData(new MaterialStack(Materials.Glass, M)));
        GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(new ItemStack(Blocks.STAINED_GLASS, 1, W),
                new RecyclingData(new MaterialStack(Materials.Glass, M)));
        GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(new ItemStack(Blocks.GLASS, 1, W),
                new RecyclingData(new MaterialStack(Materials.Glass, M)));
        GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(new ItemStack(Blocks.STAINED_GLASS_PANE, 1, W),
                new RecyclingData(new MaterialStack(Materials.Glass, M / 3))); // dust tiny
        GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(new ItemStack(Blocks.GLASS_PANE, 1, W),
                new RecyclingData(new MaterialStack(Materials.Glass, M / 3))); // dust tiny

        GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(new ItemStack(Items.FLOWER_POT, 1),
                new RecyclingData(new MaterialStack(Materials.Brick, M * 3)));
        GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(new ItemStack(Items.PAINTING, 1),
                new RecyclingData(new MaterialStack(Materials.Wood, M * 2)));
        GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(new ItemStack(Items.ITEM_FRAME, 1),
                new RecyclingData(new MaterialStack(Materials.Wood, M * 2)));
        GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(new ItemStack(Blocks.COBBLESTONE_WALL, 1),
                new RecyclingData(new MaterialStack(Materials.Stone, M)));
        GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(new ItemStack(Items.END_CRYSTAL, 1), new RecyclingData(
                new MaterialStack(Materials.Glass, M * 7), new MaterialStack(Materials.EnderEye, M)));

        if (ConfigHolder.recipes.hardToolArmorRecipes) {
            GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(new ItemStack(Items.CLOCK, 1, W),
                    new RecyclingData(new MaterialStack(Materials.Gold, (13 * M) / 8), // M + ring + 3 * bolt
                            new MaterialStack(Materials.Redstone, M)));

            GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(new ItemStack(Items.COMPASS, 1, W), new RecyclingData(
                    new MaterialStack(Materials.Iron, (4 * M) / 3), // M + 3*screw
                    new MaterialStack(Materials.RedAlloy, M / 8), // bolt
                    new MaterialStack(Materials.Zinc, M / 4))); // ring
        } else {
            GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(new ItemStack(Items.CLOCK, 1, W), new RecyclingData(
                    new MaterialStack(Materials.Gold, M * 4), new MaterialStack(Materials.Redstone, M)));
            GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(new ItemStack(Items.COMPASS, 1, W), new RecyclingData(
                    new MaterialStack(Materials.Iron, M * 4), new MaterialStack(Materials.Redstone, M)));
        }

        if (ConfigHolder.recipes.hardMiscRecipes) {
            GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(new ItemStack(Blocks.BEACON, 1, W), new RecyclingData(
                    new MaterialStack(Materials.NetherStar, (7 * M) / 4), // M + lens
                    new MaterialStack(Materials.Obsidian, M * 3),
                    new MaterialStack(Materials.Glass, M * 4)));

            GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(new ItemStack(Blocks.ENCHANTING_TABLE, 1, W),
                    new RecyclingData(
                            new MaterialStack(Materials.Diamond, M * 4),
                            new MaterialStack(Materials.Obsidian, M * 3),
                            new MaterialStack(Materials.Paper, M * 9)));

            GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(new ItemStack(Blocks.ENDER_CHEST, 1, W),
                    new RecyclingData(
                            new MaterialStack(Materials.Wood, M * 8), // chest
                            new MaterialStack(Materials.Obsidian, M * 9 * 6), // 6 dense plates
                            new MaterialStack(Materials.EnderEye, M)));
        } else {
            GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(new ItemStack(Blocks.BEACON, 1, W),
                    new RecyclingData(new MaterialStack(Materials.NetherStar, M),
                            new MaterialStack(Materials.Obsidian, M * 3), new MaterialStack(Materials.Glass, M * 5)));
            GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(new ItemStack(Blocks.ENCHANTING_TABLE, 1, W),
                    new RecyclingData(new MaterialStack(Materials.Diamond, M * 2),
                            new MaterialStack(Materials.Obsidian, M * 4), new MaterialStack(Materials.Paper, M * 3)));
            GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(new ItemStack(Blocks.ENDER_CHEST, 1, W),
                    new RecyclingData(
                            new MaterialStack(Materials.EnderEye, M), new MaterialStack(Materials.Obsidian, M * 8)));
        }

        GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(new ItemStack(Blocks.FURNACE, 1, W),
                new RecyclingData(new MaterialStack(Materials.Stone, M * 8)));
        GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(new ItemStack(Blocks.STONEBRICK, 1, W),
                new RecyclingData(new MaterialStack(Materials.Stone, M)));
        GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(new ItemStack(Blocks.COBBLESTONE, 1, W),
                new RecyclingData(new MaterialStack(Materials.Stone, M)));
        GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(new ItemStack(Blocks.MOSSY_COBBLESTONE, 1, W),
                new RecyclingData(new MaterialStack(Materials.Stone, M)));
        GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(new ItemStack(Blocks.LADDER, 1, W),
                new RecyclingData(new MaterialStack(Materials.Wood, M)));

        GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(new ItemStack(Items.BOWL, 1, W),
                new RecyclingData(new MaterialStack(Materials.Wood, M / 4)));
        GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(new ItemStack(Items.SIGN, 1, W),
                new RecyclingData(new MaterialStack(Materials.Wood, M * 2)));
        GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(new ItemStack(Blocks.CHEST, 1, W),
                new RecyclingData(new MaterialStack(Materials.Wood, M * 8)));
        GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(new ItemStack(Blocks.TRAPPED_CHEST, 1, W),
                new RecyclingData(
                        new MaterialStack(Materials.Wood, M * 8), new MaterialStack(Materials.Iron, M / 2))); // ring

        if (ConfigHolder.recipes.hardMiscRecipes) {
            GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(new ItemStack(Blocks.NOTEBLOCK, 1, W),
                    new RecyclingData(
                            new MaterialStack(Materials.Wood, M * 8), new MaterialStack(Materials.RedAlloy, M / 2))); // rod
            GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(new ItemStack(Blocks.JUKEBOX, 1, W), new RecyclingData(
                    new MaterialStack(Materials.Diamond, M / 8), // bolt
                    new MaterialStack(Materials.Iron, (17 * M) / 4), // gear + ring
                    new MaterialStack(Materials.RedAlloy, M)));
        } else {
            GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(new ItemStack(Blocks.NOTEBLOCK, 1, W),
                    new RecyclingData(
                            new MaterialStack(Materials.Wood, M * 8), new MaterialStack(Materials.Redstone, M)));
            GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(new ItemStack(Blocks.JUKEBOX, 1, W), new RecyclingData(
                    new MaterialStack(Materials.Wood, M * 8), new MaterialStack(Materials.Diamond, M)));
        }
        GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(new ItemStack(Blocks.REDSTONE_LAMP, 1, W),
                new RecyclingData(
                        new MaterialStack(Materials.Glowstone, M * 4), new MaterialStack(Materials.Redstone, M * 4))); // dust
        GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(new ItemStack(Blocks.CRAFTING_TABLE, 1, W),
                new RecyclingData(new MaterialStack(Materials.Wood, M * 2)));
        GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(new ItemStack(Blocks.PISTON, 1, W), new RecyclingData(
                new MaterialStack(Materials.Stone, M * 4), new MaterialStack(Materials.Wood, M * 3)));
        GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(new ItemStack(Blocks.STICKY_PISTON, 1, W),
                new RecyclingData(
                        new MaterialStack(Materials.Stone, M * 4), new MaterialStack(Materials.Wood, M * 3)));
        if (ConfigHolder.recipes.hardRedstoneRecipes) {
            GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(new ItemStack(Blocks.DISPENSER, 1, W),
                    new RecyclingData(new MaterialStack(Materials.Stone, M * 2),
                            new MaterialStack(Materials.RedAlloy, M / 2),
                            new MaterialStack(Materials.Iron, M * 4 + M / 4)));
            GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(new ItemStack(Blocks.DROPPER, 1, W),
                    new RecyclingData(new MaterialStack(Materials.Stone, M * 2),
                            new MaterialStack(Materials.RedAlloy, M / 2),
                            new MaterialStack(Materials.Iron, M * 2 + M * 3 / 4)));
        } else {
            GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(new ItemStack(Blocks.DISPENSER, 1, W),
                    new RecyclingData(
                            new MaterialStack(Materials.Stone, M * 2), new MaterialStack(Materials.Redstone, M)));
            GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(new ItemStack(Blocks.DROPPER, 1, W), new RecyclingData(
                    new MaterialStack(Materials.Stone, M * 2), new MaterialStack(Materials.Redstone, M)));
        }

        GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(new ItemStack(Items.IRON_HELMET, 1),
                new RecyclingData(new MaterialStack(Materials.Iron, M * 5)));
        GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(new ItemStack(Items.IRON_CHESTPLATE, 1),
                new RecyclingData(new MaterialStack(Materials.Iron, M * 8)));
        GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(new ItemStack(Items.IRON_LEGGINGS, 1),
                new RecyclingData(new MaterialStack(Materials.Iron, M * 7)));
        GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(new ItemStack(Items.IRON_BOOTS, 1),
                new RecyclingData(new MaterialStack(Materials.Iron, M * 4)));
        GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(new ItemStack(Items.IRON_HORSE_ARMOR, 1),
                new RecyclingData(new MaterialStack(Materials.Iron, M * 8)));
        GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(new ItemStack(Items.IRON_SHOVEL, 1),
                new RecyclingData(new MaterialStack(Materials.Iron, M), new MaterialStack(Materials.Wood, M / 2)));
        GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(new ItemStack(Items.IRON_PICKAXE, 1), new RecyclingData(
                new MaterialStack(Materials.Iron, M * 3), new MaterialStack(Materials.Wood, M / 2)));
        GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(new ItemStack(Items.IRON_AXE, 1), new RecyclingData(
                new MaterialStack(Materials.Iron, M * 3), new MaterialStack(Materials.Wood, M / 2)));
        GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(new ItemStack(Items.IRON_SWORD, 1), new RecyclingData(
                new MaterialStack(Materials.Iron, M * 2), new MaterialStack(Materials.Wood, M / 2)));
        GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(new ItemStack(Items.IRON_HOE, 1), new RecyclingData(
                new MaterialStack(Materials.Iron, M * 2), new MaterialStack(Materials.Wood, M / 2)));

        GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(new ItemStack(Items.GOLDEN_HELMET, 1),
                new RecyclingData(new MaterialStack(Materials.Gold, M * 5)));
        GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(new ItemStack(Items.GOLDEN_CHESTPLATE, 1),
                new RecyclingData(new MaterialStack(Materials.Gold, M * 8)));
        GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(new ItemStack(Items.GOLDEN_LEGGINGS, 1),
                new RecyclingData(new MaterialStack(Materials.Gold, M * 7)));
        GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(new ItemStack(Items.GOLDEN_BOOTS, 1),
                new RecyclingData(new MaterialStack(Materials.Gold, M * 4)));
        GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(new ItemStack(Items.GOLDEN_HORSE_ARMOR, 1),
                new RecyclingData(new MaterialStack(Materials.Gold, M * 8)));
        GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(new ItemStack(Items.GOLDEN_SHOVEL, 1),
                new RecyclingData(new MaterialStack(Materials.Gold, M), new MaterialStack(Materials.Wood, M / 2)));
        GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(new ItemStack(Items.GOLDEN_PICKAXE, 1), new RecyclingData(
                new MaterialStack(Materials.Gold, M * 3), new MaterialStack(Materials.Wood, M / 2)));
        GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(new ItemStack(Items.GOLDEN_AXE, 1), new RecyclingData(
                new MaterialStack(Materials.Gold, M * 3), new MaterialStack(Materials.Wood, M / 2)));
        GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(new ItemStack(Items.GOLDEN_SWORD, 1), new RecyclingData(
                new MaterialStack(Materials.Gold, M * 2), new MaterialStack(Materials.Wood, M / 2)));
        GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(new ItemStack(Items.GOLDEN_HOE, 1), new RecyclingData(
                new MaterialStack(Materials.Gold, M * 2), new MaterialStack(Materials.Wood, M / 2)));

        GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(new ItemStack(Items.DIAMOND_HELMET, 1),
                new RecyclingData(new MaterialStack(Materials.Diamond, M * 5)));
        GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(new ItemStack(Items.DIAMOND_CHESTPLATE, 1),
                new RecyclingData(new MaterialStack(Materials.Diamond, M * 8)));
        GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(new ItemStack(Items.DIAMOND_LEGGINGS, 1),
                new RecyclingData(new MaterialStack(Materials.Diamond, M * 7)));
        GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(new ItemStack(Items.DIAMOND_BOOTS, 1),
                new RecyclingData(new MaterialStack(Materials.Diamond, M * 4)));
        GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(new ItemStack(Items.DIAMOND_HORSE_ARMOR, 1),
                new RecyclingData(new MaterialStack(Materials.Diamond, M * 8)));
        GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(new ItemStack(Items.DIAMOND_SHOVEL, 1), new RecyclingData(
                new MaterialStack(Materials.Diamond, M), new MaterialStack(Materials.Wood, M / 2)));
        GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(new ItemStack(Items.DIAMOND_PICKAXE, 1), new RecyclingData(
                new MaterialStack(Materials.Diamond, M * 3), new MaterialStack(Materials.Wood, M / 2)));
        GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(new ItemStack(Items.DIAMOND_AXE, 1), new RecyclingData(
                new MaterialStack(Materials.Diamond, M * 3), new MaterialStack(Materials.Wood, M / 2)));
        GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(new ItemStack(Items.DIAMOND_SWORD, 1), new RecyclingData(
                new MaterialStack(Materials.Diamond, M * 2), new MaterialStack(Materials.Wood, M / 2)));
        GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(new ItemStack(Items.DIAMOND_HOE, 1), new RecyclingData(
                new MaterialStack(Materials.Diamond, M * 2), new MaterialStack(Materials.Wood, M / 2)));

        GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(new ItemStack(Items.CHAINMAIL_HELMET, 1),
                new RecyclingData(new MaterialStack(Materials.Iron, M * 5 / 4)));
        GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(new ItemStack(Items.CHAINMAIL_CHESTPLATE, 1),
                new RecyclingData(new MaterialStack(Materials.Iron, M * 2)));
        GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(new ItemStack(Items.CHAINMAIL_LEGGINGS, 1),
                new RecyclingData(new MaterialStack(Materials.Iron, M * 7 / 4)));
        GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(new ItemStack(Items.CHAINMAIL_BOOTS, 1),
                new RecyclingData(new MaterialStack(Materials.Iron, M)));

        GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(new ItemStack(Items.WOODEN_SHOVEL, 1),
                new RecyclingData(new MaterialStack(Materials.Wood, M + M / 2)));
        GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(new ItemStack(Items.WOODEN_PICKAXE, 1),
                new RecyclingData(new MaterialStack(Materials.Wood, M * 3 + M / 2)));
        GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(new ItemStack(Items.WOODEN_AXE, 1),
                new RecyclingData(new MaterialStack(Materials.Wood, M * 3 + M / 2)));
        GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(new ItemStack(Items.WOODEN_HOE, 1),
                new RecyclingData(new MaterialStack(Materials.Wood, M * 2 + M / 2)));
        GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(new ItemStack(Items.WOODEN_SWORD, 1),
                new RecyclingData(new MaterialStack(Materials.Wood, M * 2 + M / 4)));

        GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(new ItemStack(Items.STONE_SHOVEL, 1),
                new RecyclingData(new MaterialStack(Materials.Stone, M), new MaterialStack(Materials.Wood, M / 2)));
        GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(new ItemStack(Items.STONE_PICKAXE, 1), new RecyclingData(
                new MaterialStack(Materials.Stone, M * 3), new MaterialStack(Materials.Wood, M / 2)));
        GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(new ItemStack(Items.STONE_AXE, 1), new RecyclingData(
                new MaterialStack(Materials.Stone, M * 3), new MaterialStack(Materials.Wood, M / 2)));
        GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(new ItemStack(Items.STONE_HOE, 1), new RecyclingData(
                new MaterialStack(Materials.Stone, M * 2), new MaterialStack(Materials.Wood, M / 2)));
        GregTechAPI.RECYCLING_MANAGER.registerRecyclingData(new ItemStack(Items.STONE_SWORD, 1), new RecyclingData(
                new MaterialStack(Materials.Stone, M * 2), new MaterialStack(Materials.Wood, M / 4)));

        WoodRecipeLoader.registerUnificationInfo();
    }
}
