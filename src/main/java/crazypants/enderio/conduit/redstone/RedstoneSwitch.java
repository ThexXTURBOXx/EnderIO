package crazypants.enderio.conduit.redstone;

import crazypants.enderio.compat.AtlasResolver;
import java.util.List;
import java.util.Set;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import crazypants.enderio.ModObject;
import crazypants.enderio.conduit.RaytraceResult;
import crazypants.enderio.conduit.geom.CollidableComponent;
import crazypants.enderio.conduit.geom.ConduitGeometryUtil;
import crazypants.enderio.conduit.geom.Offset;
import crazypants.render.BoundingBox;
import crazypants.util.BlockCoord;
import crazypants.vecmath.Vector3d;

public class RedstoneSwitch extends RedstoneConduit {

  static final String SWITCH_TAG = "switch";
  public static final String SWITHC_ICON_OFF_KEY = "enderio:redstoneConduitSwitchOff";
  public static final String SWITCH_ICON_ON_KEY = "enderio:redstoneConduitSwitchOn";

  private boolean isOn;

  public static void initIcons() {
    ICONS.put(RedstoneSwitch.SWITHC_ICON_OFF_KEY, AtlasResolver.getLocationIndex(SWITHC_ICON_OFF_KEY));
    ICONS.put(RedstoneSwitch.SWITCH_ICON_ON_KEY, AtlasResolver.getLocationIndex(SWITCH_ICON_ON_KEY));
  }

  @Override
  public boolean isReplaceableByControl(Class<? extends IRedstoneConduit> replacenebtType) {
    return false;
  }

  @Override
  public ItemStack createItem() {
    return new ItemStack(ModObject.itemRedstoneConduit.actualId, 1, 1);
  }

  @Override
  public boolean isProvidingStrongPower(ForgeDirection toDirection) {
    if (network == null || !network.isNetworkEnabled()) {
      return false;
    }
    return isOn;
  }

  @Override
  public void writeToNBT(NBTTagCompound nbtRoot) {
    super.writeToNBT(nbtRoot);
    nbtRoot.setBoolean("switchOn", isOn);
  }

  @Override
  public void readFromNBT(NBTTagCompound nbtRoot) {
    super.readFromNBT(nbtRoot);
    isOn = nbtRoot.getBoolean("switchOn");
  }

  String getSwitchIconFile() {
    return AtlasResolver.getTextureFile("enderio");
  }

  int getSwitchIcon() {
    return isOn ? ICONS.get(SWITCH_ICON_ON_KEY) : ICONS.get(SWITHC_ICON_OFF_KEY);
  }

  @Override
  public int getTextureForState(CollidableComponent component) {
    if (SWITCH_TAG.equals(component.data)) {
      return isOn ? ICONS.get(SWITCH_ICON_ON_KEY) : ICONS.get(SWITHC_ICON_OFF_KEY);
    }
    return super.getTextureForState(component);
  }

  @Override
  public List<CollidableComponent> getCollidableComponents() {
    if (collidables != null && !collidablesDirty) {
      return collidables;
    }

    Offset o = getBundle().getOffset(getBaseConduitType(), ForgeDirection.UNKNOWN);
    Vector3d trans = ConduitGeometryUtil.instance.getTranslation(ForgeDirection.UNKNOWN, o);

    List<CollidableComponent> result = super.getCollidableComponents();
    BoundingBox[] aabb = RedstoneSwitchBounds.getInstance().getAABB();

    for (BoundingBox bb : aabb) {
      result.add(new CollidableComponent(IRedstoneConduit.class, bb.translate(trans), ForgeDirection.UNKNOWN, SWITCH_TAG));
    }

    return result;
  }

  @Override
  public boolean onBlockActivated(EntityPlayer player, RaytraceResult res) {
    if (res.component.data != null && res.component.data.equals(SWITCH_TAG)) {
      toggleSwitch();
      return true;
    }
    return false;
  }

  private void toggleSwitch() {
    isOn = !isOn;
    if (network == null) {
      return;
    }
    TileEntity te = bundle.getEntity();
    Signal signal = new Signal(te.xCoord, te.yCoord, te.zCoord, 15, SignalColor.RED);
    if (isOn) {
      network.addSignal(signal);
    } else {
      network.removeSignal(signal);
    }
  }

  @Override
  public Set<Signal> getNetworkInputs() {
    Set<Signal> res = super.getNetworkInputs();
    if (isOn) {
      BlockCoord loc = getLocation();
      Signal signal = new Signal(loc.x, loc.y, loc.z, 15, SignalColor.RED);
      res.add(signal);
    }
    return res;
  }

}
