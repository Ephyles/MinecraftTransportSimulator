package minecrafttransportsimulator.items.parts;

import java.util.List;

import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public final class ItemPartEngineAircraft extends AItemPartEngine{
	
	@Override
	@SideOnly(Side.CLIENT)
	protected void addExtraInformation(ItemStack stack, List<String> tooltipLines){
		tooltipLines.add(I18n.format("info.item.engine.gearratios") + packComponent.pack.engine.gearRatios[0]);
	}
}