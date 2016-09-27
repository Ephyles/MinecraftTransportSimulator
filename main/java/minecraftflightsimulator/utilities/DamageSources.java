package minecraftflightsimulator.utilities;

import minecraftflightsimulator.MFS;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IChatComponent;

public class DamageSources{
	public static double propellerDamageFactor;
	public static double crashDamageFactor;
	
	public static void init(){
		propellerDamageFactor = MFS.config.get(MFS.config.CATEGORY_GENERAL, "PropellerDamageFactor", 1.0F, "Factor for damage caused by a propeller.").getDouble();
		crashDamageFactor = MFS.config.get(MFS.config.CATEGORY_GENERAL, "CrashDamageFactor", 1.0F, "Factor for damage caused by plane crashes.").getDouble();
		MFS.config.save();
	}

	private static class DamageSourceMFS extends DamageSource{
		//Source of the damage.  This will be the vehicle controller or the player who shot the gun.
		private final Entity playerResponsible;
		
		public DamageSourceMFS(String name, Entity playerResponsible){
			super(name);
			this.playerResponsible = playerResponsible;
		}
		
		@Override
		public IChatComponent func_151519_b(EntityLivingBase player){
			EntityLivingBase recentEntity = player.func_94060_bK();
			if(recentEntity != null){//Player engaged with another player...
				if(playerResponsible != null){//and then was killed by another player.
					return new ChatComponentTranslation("death.attack." + this.damageType + ".player.player", 
							new Object[] {player.func_145748_c_(), playerResponsible.func_145748_c_(), recentEntity.func_145748_c_()});
				}else{//and then was killed by something.
					return new ChatComponentTranslation("death.attack." + this.damageType + ".null.player", 
							new Object[] {player.func_145748_c_(), recentEntity.func_145748_c_()});
				}
			}else{//Player was minding their own business...
				if(playerResponsible != null){//and was killed by another player.
					return new ChatComponentTranslation("death.attack." + this.damageType + ".player.null", 
							new Object[] {player.func_145748_c_(), playerResponsible.func_145748_c_()});
				}else{//and then was killed by something.
					return new ChatComponentTranslation("death.attack." + this.damageType + ".null.null", 
							new Object[] {player.func_145748_c_()});
				}
			}
		                
		}
	};
	
	public static class DamageSourcePropellor extends DamageSourceMFS{
		public DamageSourcePropellor(Entity playerResponsible){
			super("propellor", playerResponsible);
		}
	};

	public static class DamageSourcePlaneCrash extends DamageSourceMFS{
		public DamageSourcePlaneCrash(Entity playerResponsible){
			super("planecrash", playerResponsible);
		}
	};
}