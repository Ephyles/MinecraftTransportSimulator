package minecrafttransportsimulator.items.parts;

import java.util.List;

import javax.annotation.Nullable;

import minecrafttransportsimulator.packs.objects.PackObjectVehicle.PackPart;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemPartBullet extends AItemPart{
	
	@Override
	public boolean isPartValidForPackDef(PackPart packPart){
		float bulletDiameter = packComponent.pack.bullet.diameter;
		return packPart.minValue <= bulletDiameter && packPart.maxValue >= bulletDiameter ? super.isPartValidForPackDef(packPart) : false;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltipLines, ITooltipFlag flagIn){
		tooltipLines.add(I18n.format("info.item.bullet.type." + packComponent.pack.bullet.type));
		tooltipLines.add(I18n.format("info.item.bullet.diameter") + packComponent.pack.bullet.diameter);
		tooltipLines.add(I18n.format("info.item.bullet.quantity") + packComponent.pack.bullet.quantity);
	}
}