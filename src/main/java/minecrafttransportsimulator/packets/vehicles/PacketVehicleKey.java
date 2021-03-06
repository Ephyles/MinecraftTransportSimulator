package minecrafttransportsimulator.packets.vehicles;

import io.netty.buffer.ByteBuf;
import minecrafttransportsimulator.vehicles.main.EntityVehicleE_Powered;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketVehicleKey extends APacketVehicle{
	private boolean isLocked;
	
	public PacketVehicleKey(){}
	
	public PacketVehicleKey(EntityVehicleE_Powered vehicle){
		super(vehicle);
		this.isLocked = vehicle.locked;
	}
	
	@Override
	public void fromBytes(ByteBuf buf){
		super.fromBytes(buf);
		this.isLocked = buf.readBoolean();
	}

	@Override
	public void toBytes(ByteBuf buf){
		super.toBytes(buf);
		buf.writeBoolean(this.isLocked);
	}
	
	public static class Handler implements IMessageHandler<PacketVehicleKey, IMessage>{
		@Override
		public IMessage onMessage(final PacketVehicleKey message, final MessageContext ctx){
			FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(new Runnable(){
				@Override
				public void run(){
					EntityVehicleE_Powered vehicle = getVehicle(message, ctx);
					if(vehicle != null){
						vehicle.locked = message.isLocked;
					}
				}
			});
			return null;
		}
	}
}
