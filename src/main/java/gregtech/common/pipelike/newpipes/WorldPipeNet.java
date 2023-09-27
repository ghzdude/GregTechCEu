package gregtech.common.pipelike.newpipes;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.util.Constants.NBT;

import javax.annotation.Nonnull;
import java.lang.ref.WeakReference;
import java.util.*;

public abstract class WorldPipeNet<NDT> extends WorldSavedData {

    private WeakReference<World> worldRef = new WeakReference<>(null);
    protected List<PipeNet<NDT>> pipeNets = new ArrayList<>();
    protected final Map<ChunkPos, List<PipeNet<NDT>>> pipeNetsByChunk = new HashMap<>();

    public WorldPipeNet(String name) {
        super(name);
    }

    public World getWorld() {
        return this.worldRef.get();
    }

    protected void setWorldAndInit(World world) {
        if (world != this.worldRef.get()) {
            this.worldRef = new WeakReference<>(world);
            onWorldSet();
        }
    }

    public static String getDataID(final String baseID, final World world) {
        if (world == null || world.isRemote)
            throw new RuntimeException("WorldPipeNet should only be created on the server!");
        int dimension = world.provider.getDimension();
        return dimension == 0 ? baseID : baseID + '.' + dimension;
    }

    protected void onWorldSet() {
        this.pipeNets.forEach(PipeNet::onNodeConnectionsUpdate);
    }

    public void addNode(BlockPos nodePos, NDT nodeData, int mark, int openConnections, boolean isActive) {
        PipeNet<NDT> myPipeNet = null;
        Node<NDT> node = new Node<>(nodeData, openConnections, mark, isActive);
        for (EnumFacing facing : EnumFacing.VALUES) {
            BlockPos offsetPos = nodePos.offset(facing);
            PipeNet<NDT> pipeNet = getNetFromPos(offsetPos);
            Node<NDT> secondNode = pipeNet == null ? null : pipeNet.getAllNodes().get(offsetPos);
            if (pipeNet != null && pipeNet.canAttachNode(nodeData) &&
                    pipeNet.canNodesConnect(secondNode, facing.getOpposite(), node, null)) {
                if (myPipeNet == null) {
                    myPipeNet = pipeNet;
                    myPipeNet.addNode(nodePos, node);
                } else if (myPipeNet != pipeNet) {
                    myPipeNet.uniteNetworks(pipeNet);
                }
            }

        }
        if (myPipeNet == null) {
            myPipeNet = createNetInstance();
            myPipeNet.addNode(nodePos, node);
            addPipeNet(myPipeNet);
            markDirty();
        }
    }

    protected void addPipeNetToChunk(ChunkPos chunkPos, PipeNet<NDT> pipeNet) {
        this.pipeNetsByChunk.computeIfAbsent(chunkPos, any -> new ArrayList<>()).add(pipeNet);
    }

    protected void removePipeNetFromChunk(ChunkPos chunkPos, PipeNet<NDT> pipeNet) {
        List<PipeNet<NDT>> list = this.pipeNetsByChunk.get(chunkPos);
        if (list != null) {
            list.remove(pipeNet);
            if (list.isEmpty()) {
                this.pipeNetsByChunk.remove(chunkPos);
            }
        }
    }

    public void removeNode(BlockPos nodePos) {
        PipeNet<NDT> pipeNet = getNetFromPos(nodePos);
        if (pipeNet != null) {
            pipeNet.removeNode(nodePos);
        }
    }

    public void updateBlockedConnections(BlockPos nodePos, EnumFacing side, boolean isBlocked) {
        PipeNet<NDT> pipeNet = getNetFromPos(nodePos);
        if (pipeNet != null) {
            pipeNet.updateBlockedConnections(nodePos, side, isBlocked);
            pipeNet.onPipeConnectionsUpdate();
        }
    }

    public void updateMark(BlockPos nodePos, int newMark) {
        PipeNet<NDT> pipeNet = getNetFromPos(nodePos);
        if (pipeNet != null) {
            pipeNet.updateMark(nodePos, newMark);
        }
    }

    public PipeNet<NDT> getNetFromPos(BlockPos blockPos) {
        List<PipeNet<NDT>> pipeNetsInChunk = pipeNetsByChunk.getOrDefault(new ChunkPos(blockPos), Collections.emptyList());
        for (PipeNet<NDT> pipeNet : pipeNetsInChunk) {
            if (pipeNet.containsNode(blockPos))
                return pipeNet;
        }
        return null;
    }

    protected void addPipeNet(PipeNet<NDT> pipeNet) {
        addPipeNetSilently(pipeNet);
    }

    protected void addPipeNetSilently(PipeNet<NDT> pipeNet) {
        this.pipeNets.add(pipeNet);
        pipeNet.getContainedChunks().forEach(chunkPos -> addPipeNetToChunk(chunkPos, pipeNet));
        pipeNet.isValid = true;
    }

    protected void removePipeNet(PipeNet<NDT> pipeNet) {
        this.pipeNets.remove(pipeNet);
        pipeNet.getContainedChunks().forEach(chunkPos -> removePipeNetFromChunk(chunkPos, pipeNet));
        pipeNet.isValid = false;
    }

    protected abstract PipeNet<NDT> createNetInstance();

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        this.pipeNets = new ArrayList<>();
        NBTTagList allEnergyNets = nbt.getTagList("PipeNets", NBT.TAG_COMPOUND);
        for (int i = 0; i < allEnergyNets.tagCount(); i++) {
            NBTTagCompound pNetTag = allEnergyNets.getCompoundTagAt(i);
            PipeNet<NDT> pipeNet = createNetInstance();
            pipeNet.deserializeNBT(pNetTag);
            addPipeNetSilently(pipeNet);
        }
    }

    @Nonnull
    @Override
    public NBTTagCompound writeToNBT(@Nonnull NBTTagCompound compound) {
        NBTTagList allPipeNets = new NBTTagList();
        for (PipeNet<NDT> pipeNet : pipeNets) {
            NBTTagCompound pNetTag = pipeNet.serializeNBT();
            allPipeNets.appendTag(pNetTag);
        }
        compound.setTag("PipeNets", allPipeNets);
        return compound;
    }
}
