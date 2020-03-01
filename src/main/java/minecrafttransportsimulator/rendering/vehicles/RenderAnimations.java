package minecrafttransportsimulator.rendering.vehicles;

import minecrafttransportsimulator.systems.ConfigSystem;
import minecrafttransportsimulator.vehicles.main.EntityVehicleA_Base;
import minecrafttransportsimulator.vehicles.main.EntityVehicleE_Powered;
import minecrafttransportsimulator.vehicles.main.EntityVehicleE_Powered.LightType;
import minecrafttransportsimulator.vehicles.main.EntityVehicleF_Air;
import minecrafttransportsimulator.vehicles.main.EntityVehicleF_Ground;
import minecrafttransportsimulator.vehicles.main.EntityVehicleG_Plane;
import minecrafttransportsimulator.vehicles.parts.APart;
import minecrafttransportsimulator.vehicles.parts.APartEngine;
import minecrafttransportsimulator.vehicles.parts.APartEngineGeared;
import minecrafttransportsimulator.vehicles.parts.APartGun;
import minecrafttransportsimulator.vehicles.parts.PartPropeller;

/**This class contains static methods for vehicle animations.  These are used to animate
 * the vehicle and its parts, as well as instruments.  All methods are designed to be as
 * global as possible to keep all animations in this class.
 *
 * @author don_bruce
 */
public final class RenderAnimations{
	
	/**
	 *  Returns the current value for the passed-in variable on the passed-in vehicle.  A part may or
	 *  may not be passed in to allow for part-specific animations (such as a specific engine's RPM).
	 */
	public static double getVariableValue(String variable, float partialTicks, EntityVehicleE_Powered vehicle, APart<? extends EntityVehicleE_Powered> optionalPart){
		//If we have a variable with a suffix, we need to get that part first and pass
		//it into this method rather than trying to run through the code now.
		if(variable.substring(variable.length() - 2).matches("[0-9]+")){
			//Take off one because we are zero-indexed.
			int partNumber = Integer.parseInt(variable.substring(variable.length() - 2)) - 1;
			final Class<?> partClass;
			switch(variable.substring(variable.lastIndexOf('_') + 1, variable.length() - 3)){
				case("engine"): partClass = APartEngine.class; break;
				case("propeller"): partClass = PartPropeller.class; break;
				case("gun"): partClass = APartGun.class; break;
				default: throw new IllegalArgumentException("ERROR: Tried to find indexed part:" + variable.substring(variable.lastIndexOf('_') + 1, variable.length() - 3) + " for rotation definition: " + variable + " but could not.  Is your formatting correct?");
			}
			for(APart<? extends EntityVehicleA_Base> part : vehicle.getVehicleParts()){
				if(partClass.isInstance(part.getClass())){
					if(partNumber == 0){
						//We found the part we were supposed to link to.  Return the value for it.
						return getVariableValue(variable.substring(variable.lastIndexOf('_')), partialTicks, vehicle, part);
					}else{
						--partNumber;
					}
				}
			}
			
			//We couldn't find the part we were supposed to.  Likely because it hasn't been placed yet.
			return 0;
		}else if(optionalPart != null){
			//If we passed-in a part, check for part-specific animations first.
			if(optionalPart instanceof APartEngine){
				APartEngine<? extends EntityVehicleE_Powered> engine = (APartEngine<? extends EntityVehicleE_Powered>) optionalPart;
				switch(variable){
					case("engine_rotation"): return engine.getEngineRotation(partialTicks);
					case("engine_driveshaft_rotation"): return engine.getDriveshaftRotation(partialTicks);
					case("engine_driveshaft_sin"): return 1 + Math.cos(Math.toRadians(engine.getDriveshaftRotation(partialTicks) + 180D))/2D;
					case("engine_driveshaft_sin_offset"): return Math.sin(Math.toRadians(engine.getDriveshaftRotation(partialTicks) + 180D));
					case("engine_rpm"): return engine.definition.engine.maxRPM < 15000 ? engine.RPM : engine.RPM/10D;
					case("engine_rpm_max"): return engine.definition.engine.maxRPM < 15000 ? engine.definition.engine.maxRPM : engine.definition.engine.maxRPM/10D;
					case("engine_fuel_flow"): return engine.fuelFlow*20D*60D/1000D;
					case("engine_temp"): return engine.temp;
					case("engine_oil"): return engine.oilPressure;
					case("engine_gear"): return ((APartEngineGeared<? extends EntityVehicleE_Powered>) engine).currentGear;
					case("engine_gearshift"): return ((APartEngineGeared<? extends EntityVehicleE_Powered>) engine).getGearshiftRotation();
					case("engine_gearshift_hvertical"): return ((APartEngineGeared<? extends EntityVehicleE_Powered>) engine).getGearshiftPosition_Vertical();
					case("engine_gearshift_hhorizontal"): return ((APartEngineGeared<? extends EntityVehicleE_Powered>) engine).getGearshiftPosition_Horizontal();
					case("engine_magneto"): return engine.state.magnetoOn ? 1 : 0;
					case("engine_starter"): return engine.state.esOn ? 1 : 0;
				}
			}else if(optionalPart instanceof PartPropeller){
				switch(variable){
					case("propeller_pitch_deg"): return Math.toDegrees(Math.atan(((PartPropeller) optionalPart).currentPitch / (((PartPropeller) optionalPart).definition.propeller.diameter*0.75D*Math.PI)));
					case("propeller_pitch_in"): return((PartPropeller) optionalPart).currentPitch;
					case("propeller_pitch_percent"): return((PartPropeller) optionalPart).currentPitch/optionalPart.definition.propeller.pitch;
				}
			}else if(optionalPart instanceof APartGun){
				APartGun gun = (APartGun) optionalPart;
				switch(variable){
					case("gun_pitch"): return gun.currentPitch;
					case("gun_yaw"): return gun.currentYaw;
					case("gun_ammo"): return gun.bulletsLeft;
				}
			}
			
			//We didn't find any part-specific animations.
			//We could, however, be wanting the animations of our parent part.
			//If we have a parent part, get it, and try this loop again.
			if(optionalPart.parentPart != null){
				return getVariableValue(variable, partialTicks, vehicle, optionalPart.parentPart);
			}
		}
		
		//Either we don't have a part, or we have a part and we don't want a part-specific variable.
		//Try vehicle variables now.
		switch(variable){
			//Vehicle world position cases.	
			case("yaw"): return -vehicle.rotationYaw;
			case("pitch"): return vehicle.rotationPitch;
			case("roll"): return vehicle.rotationRoll;
			case("altitude"): return vehicle.posY - (ConfigSystem.configObject.client.seaLvlOffset.value ? vehicle.world.provider.getAverageGroundLevel() : 0);
			case("speed"): return Math.abs(vehicle.velocity*vehicle.speedFactor*20);
			case("vertical_speed"): return vehicle.motionY;
			case("turn_coordinator"): return ((vehicle.rotationRoll - vehicle.prevRotationRoll)/10 + vehicle.rotationYaw - vehicle.prevRotationYaw)/0.15D*25;
			case("turn_indicator"): return (vehicle.rotationYaw - vehicle.prevRotationYaw)/0.15F*25F;
			
			//Vehicle state cases.
			case("throttle"): return vehicle.throttle;
			case("fuel"): return vehicle.fuel/vehicle.definition.motorized.fuelCapacity*100D;
			case("electric_power"): return vehicle.electricPower;
			case("electric_usage"): return vehicle.electricFlow*20D;
			case("brake"): return vehicle.brakeOn ? 1 : 0;
			case("p_brake"): return vehicle.prevParkingBrakeAngle + (vehicle.parkingBrakeAngle - vehicle.prevParkingBrakeAngle)*partialTicks;
			case("steeringwheel"): return vehicle.getSteerAngle();
			case("horn"): return vehicle.hornOn ? 1 : 0;
			case("hood"): return vehicle.engines.isEmpty() ? 1 : 0;
		}
		
		//Check if this is a light variable.
		for(LightType light : LightType.values()){
			if(light.name().toLowerCase().equals(variable)){
				return vehicle.isLightOn(light) ? 1 : 0;
			}
		}
		
		//Not a generic variable.  Check vehicle-class-specific variables.
		if(vehicle instanceof EntityVehicleF_Ground){
			EntityVehicleF_Ground ground = (EntityVehicleF_Ground) vehicle;
			switch(variable){
				case("trailer"): return ground.towingAngle;
				case("hookup"): return ground.towedByVehicle != null ? ground.towedByVehicle.towingAngle/30D : 0;
			}
		}else if(vehicle instanceof EntityVehicleF_Air){
			EntityVehicleF_Air aircraft = (EntityVehicleF_Air) vehicle;
			switch(variable){
				case("aileron"): return aircraft.aileronAngle/10D;
				case("elevator"): return aircraft.elevatorAngle/10D;
				case("rudder"): return aircraft.rudderAngle/10D;
				case("trim_aileron"): return aircraft.aileronTrim/10D;
				case("trim_elevator"): return aircraft.elevatorTrim/10D;
				case("trim_rudder"): return aircraft.rudderTrim/10D;
				case("reverser"): return aircraft.reversePercent/20D;
				case("slip"): return 75*aircraft.sideVec.dotProduct(vehicle.velocityVec);
				case("lift_reserve"): return aircraft.trackAngle*3 + 20;
			}
			if(aircraft instanceof EntityVehicleG_Plane){
				EntityVehicleG_Plane plane = (EntityVehicleG_Plane) aircraft;
				switch(variable){	
					case("flaps_setpoint"): return plane.flapDesiredAngle/10D;
					case("flaps_actual"): return plane.flapCurrentAngle/10D;
				}
			}
		}
		
		//No variable found for anything.  We could have an error, but likely we have an older pack.
		//Return 0 here to prevent pack crashes.
		return 0;
	}
}
