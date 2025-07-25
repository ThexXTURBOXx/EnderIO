package crazypants.enderio.power;

import buildcraft.api.power.IPowerReceptor;
import net.minecraftforge.common.ForgeDirection;

public interface IInternalPowerReceptor extends IPowerReceptor {

  MutablePowerProvider getPowerHandler();

  void applyPerdition();

  int powerRequest(ForgeDirection side);

}
