package ru.DmN.fb;

import com.mojang.brigadier.arguments.BoolArgumentType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager;

import static net.fabricmc.fabric.api.client.command.v1.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command.v1.ClientCommandManager.literal;

public class Main implements ModInitializer {
    public static boolean State = false;

    @Override
    public void onInitialize() {
        ClientCommandManager.DISPATCHER.register(literal("dmn").then(literal("fb").then(argument("state", BoolArgumentType.bool()).executes(context -> {
            State = context.getArgument("state", boolean.class);
            return 1;
        }))));
    }
}
