package minecrafttransportsimulator.vehicles.parts;

import java.util.List;

import minecrafttransportsimulator.MTS;
import minecrafttransportsimulator.packets.general.PacketChat;
import minecrafttransportsimulator.packs.components.PackComponentPart;
import minecrafttransportsimulator.packs.objects.PackObjectVehicle.PackPart;
import minecrafttransportsimulator.vehicles.main.EntityVehicleE_Powered;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;

public final class PartSeat extends APart{
	
	public PartSeat(EntityVehicleE_Powered vehicle, PackComponentPart packComponent, PackPart vehicleDefinition, NBTTagCompound dataTag){
		super(vehicle, packComponent, vehicleDefinition, dataTag);
	}
	
	@Override
	public boolean interactPart(EntityPlayer player){
		//See if we can enter this vehicle.
		//Alternately, we can switch seats if we're already in the vehicle.
		if(!vehicle.locked || vehicle.equals(player.getRidingEntity())){
			Entity seatRider = vehicle.getRiderForSeat(this);
			if(seatRider != null){
				if(!player.equals(seatRider)){
					MTS.MTSNet.sendTo(new PacketChat("interact.failure.seattaken"), (EntityPlayerMP) player);
				}
				return true;
			}
			//If we got here we must have the seat number and it must be free.
			//Let the player start riding at this point.
			vehicle.setRiderInSeat(player, this);
		}else{
			MTS.MTSNet.sendTo(new PacketChat("interact.failure.vehiclelocked"), (EntityPlayerMP) player);
		}
		return true;
    }
	
	@Override
	public void removePart(List<APart> partsToRemove){
		super.removePart(partsToRemove);
		if(vehicle.getRiderForSeat(this) != null){
			vehicle.getRiderForSeat(this).dismountRidingEntity();
		}
	}
	
	@Override
	public NBTTagCompound getPartNBTTag(){
		return new NBTTagCompound();
	}

	@Override
	public float getWidth(){
		return 0.75F;
	}

	@Override
	public float getHeight(){
		return 0.75F;
	}
}