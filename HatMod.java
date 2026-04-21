package com.example.hatmod;

import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;

public class HatMod implements ModInitializer {

    @Override
    public void onInitialize() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            registerHatCommand(dispatcher);
        });
    }

    private void registerHatCommand(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("hat")
            .executes(context -> {
                ServerPlayerEntity player = context.getSource().getPlayer();

                ItemStack handItem = player.getMainHandStack();

                if (handItem.isEmpty()) {
                    player.sendMessage(Text.literal("You are not holding anything!"), false);
                    return 0;
                }

                ItemStack helmet = player.getInventory().getArmorStack(3);

                player.getInventory().setStack(3, handItem.copy());
                player.setStackInHand(Hand.MAIN_HAND, helmet);

                player.sendMessage(Text.literal("Hat equipped!"), false);

                return 1;
            })
        );
    }
}
