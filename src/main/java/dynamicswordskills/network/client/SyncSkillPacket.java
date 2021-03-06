/**
    Copyright (C) <2017> <coolAlias>

    This file is part of coolAlias' Dynamic Sword Skills Minecraft Mod; as such,
    you can redistribute it and/or modify it under the terms of the GNU
    General Public License as published by the Free Software Foundation,
    either version 3 of the License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package dynamicswordskills.network.client;

import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.relauncher.Side;
import dynamicswordskills.entity.DSSPlayerInfo;
import dynamicswordskills.network.AbstractMessage.AbstractClientMessage;
import dynamicswordskills.skills.SkillBase;

/**
 * 
 * Synchronizes the client-side version of a skill with the server-side data.
 *
 */
public class SyncSkillPacket extends AbstractClientMessage<SyncSkillPacket>
{
	/** The ID of the skill to update */
	private byte id;

	/** Stores the skill's data */
	private NBTTagCompound compound;

	public SyncSkillPacket() {}

	/**
	 * Synchronizes the client side skill with the server skill's data
	 * @param skill A level 0 skill will be removed
	 */
	public SyncSkillPacket(SkillBase skill) {
		id = skill.getId();
		compound = new NBTTagCompound();
		skill.writeToNBT(compound);
	}

	@Override
	protected void read(PacketBuffer buffer) throws IOException {
		id = buffer.readByte();
		compound = buffer.readCompoundTag();
	}

	@Override
	protected void write(PacketBuffer buffer) throws IOException {
		buffer.writeByte(id);
		buffer.writeCompoundTag(compound);
	}

	@Override
	protected void process(EntityPlayer player, Side side) {
		DSSPlayerInfo.get(player).syncClientSideSkill(id, compound);
	}
}
