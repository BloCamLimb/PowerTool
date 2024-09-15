package org.teacon.powertool.network.attachment;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.server.permission.PermissionAPI;
import net.neoforged.neoforge.server.permission.events.PermissionGatherEvent;
import net.neoforged.neoforge.server.permission.nodes.PermissionNode;
import net.neoforged.neoforge.server.permission.nodes.PermissionTypes;
import org.teacon.powertool.PowerTool;
import org.teacon.powertool.network.client.UpdatePermissionPacket;
import org.teacon.powertool.utils.VanillaUtils;

import java.util.Optional;

public class Permission {
    
    public static final ResourceLocation KEY = VanillaUtils.resourceLocationOf(PowerTool.MODID, "permission");

    private Boolean canUseGameMasterBlock;
    private Boolean canSwitchGameMode;
    // canUseSelector is superseded by NeoForgeMod.USE_SELECTORS_PERMISSION

    public Optional<Boolean> isCanUseGameMasterBlock() {
        return Optional.ofNullable(canUseGameMasterBlock);
    }

    public void setCanUseGameMasterBlock(boolean canUseGameMasterBlock) {
        this.canUseGameMasterBlock = canUseGameMasterBlock;
    }

    public Optional<Boolean> isCanSwitchGameMode() {
        return Optional.ofNullable(canSwitchGameMode);
    }

    public void setCanSwitchGameMode(boolean canSwitchGameMode) {
        this.canSwitchGameMode = canSwitchGameMode;
    }

    @EventBusSubscriber
    public static class Provider  {
        
        private static final PermissionNode<Boolean> GAMEMODE = new PermissionNode<>(
            "minecraft", "command.gamemode", PermissionTypes.BOOLEAN,
            (player, uuid, context) -> player != null && player.hasPermissions(2)
        );
        private static final PermissionNode<Boolean> COMMAND_BLOCK = new PermissionNode<>(
            "minecraft", "use_gamemaster_block", PermissionTypes.BOOLEAN,
            (player, uuid, context) -> player != null && player.getAbilities().instabuild && player.hasPermissions(2)
        );
        // ENTITY_SELECTOR is replaced by NeoForgeMod.USE_SELECTORS_PERMISSION

        @SubscribeEvent
        public static void on(PermissionGatherEvent.Nodes event) {
            event.addNodes(GAMEMODE, COMMAND_BLOCK);
        }
    }

    public static void updatePermission(ServerPlayer player) {
        PacketDistributor.sendToPlayer(player,
            new UpdatePermissionPacket(
                PermissionAPI.getPermission(player, Provider.COMMAND_BLOCK),
                PermissionAPI.getPermission(player, Provider.GAMEMODE)
            )
        );
    }
}
