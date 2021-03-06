package undercurrentcore.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.DimensionManager;
import undercurrentcore.persist.UCBlockDTO;
import undercurrentcore.persist.UCPlayerDTO;
import undercurrentcore.persist.UCPlayersWorldData;

import java.util.ArrayList;
import java.util.List;

public class UCCommandList extends CommandBase {
    @Override
    public String getCommandName() {
        return "uclist";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/uclist";
    }


    @Override
    public void processCommand(ICommandSender sender, String[] params) {

        if (sender instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) sender;
            try {

                UCPlayersWorldData data = (UCPlayersWorldData) DimensionManager.getWorld(0).perWorldStorage.loadData(UCPlayersWorldData.class, UCPlayersWorldData.GLOBAL_TAG);

                String secretKey = data.getPlayerSecretKeyForUUID(player.getUniqueID());

                if (secretKey == null) {
                    return;
                }

                List<UCBlockDTO> blocks = data.getPlayerBlocks(secretKey);

                if (blocks.size() < 1) {
                    sender.addChatMessage(new ChatComponentText(EnumChatFormatting.AQUA +
                            "UnderCurrent: " +
                            EnumChatFormatting.WHITE +
                            StatCollector.translateToLocal("uclist.noblocks")));
                    return;
                }

                for (UCBlockDTO block : blocks) {

                    sender.addChatMessage(new ChatComponentText(EnumChatFormatting.GOLD +
                            block.getName() +
                            ": " +
                            EnumChatFormatting.WHITE +
                            "<X: " + block.getxCoord() + ">" +
                            "<Y: " + block.getyCoord() + ">" +
                            "<Z: " + block.getzCoord() + ">" +
                            "<Dim: " + DimensionManager.getProvider(block.getDim()).getDimensionName() + ">"
                    ));
                }

            } catch (Exception e) {
                sender.addChatMessage(new ChatComponentText(getCommandUsage(sender)));
            }
        }
    }
}