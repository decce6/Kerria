package me.decce.kerria;

import net.minecraft.network.chat.Component;

//? <=1.18.2 {
/*import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
*///? }

public class VersionCompatUtils {
    public static Component literal(String s) {
        //? >=1.19 {
        return Component.literal(s);
        //? } else {
        /*return new TextComponent(s);
        *///? }
    }

    public static Component translatable(String s) {
        //? >=1.19 {
        return Component.translatable(s);
        //? } else {
        /*return new TranslatableComponent(s);
        *///? }
    }
}
