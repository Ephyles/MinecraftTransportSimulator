package minecrafttransportsimulator.vehicles.parts;

import minecrafttransportsimulator.jsondefs.JSONPart;
import minecrafttransportsimulator.jsondefs.JSONVehicle.VehiclePart;
import minecrafttransportsimulator.vehicles.main.EntityVehicleE_Powered;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.Vec3d;

public final class PartGroundDevicePontoon extends APartGroundDevice{
	
	public PartGroundDevicePontoon(EntityVehicleE_Powered vehicle, VehiclePart packVehicleDef, JSONPart definition, NBTTagCompound dataTag){
		super(vehicle, packVehicleDef, definition, dataTag);
	}
	
	@Override
	public NBTTagCompound getPartNBTTag(){
		return new NBTTagCompound();
	}
	
	@Override
	public float getWidth(){
		return this.definition.pontoon.width;
	}
	
	@Override
	public float getHeight(){
		return this.getWidth();
	}
	
	@Override
	public boolean isPartCollidingWithBlocks(Vec3d collisionOffset){
		if(super.isPartCollidingWithBlocks(collisionOffset)){
			return true;
    	}else{
    		return isPartCollidingWithLiquids(collisionOffset);
    	}
    }
	
	@Override
	public float getMotiveFriction(){
		return 0;
	}
	
	@Override
	public float getLateralFriction(){
		return this.definition.pontoon.lateralFriction;
	}
	
	@Override
	public float getLongPartOffset(){
		return definition.pontoon.extraCollisionBoxOffset;
	}
	
	@Override
	public boolean canBeDrivenByEngine(){
		return false;
	}
}
