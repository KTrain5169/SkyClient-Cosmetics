/*
 * SkyclientCosmetics - Cool cosmetics for a mod installer Skyclient!
 * Copyright (C) koxx12-dev [2021 - 2021]
 *
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under the certain conditions that can be found here
 * https://www.gnu.org/licenses/lgpl-3.0.en.html
 *
 * If you have any questions or concerns, please create
 * an issue on the github page that can be found under this url
 * https://github.com/koxx12-dev/Skyclient-Cosmetics
 *
 * If you have a private concern, please contact me on
 * Discord: Koxx12#8061
 */

package co.skyclient.scc.listeners;

import cc.polyfrost.oneconfig.utils.Notifications;
import co.skyclient.scc.SkyclientCosmetics;
import co.skyclient.scc.config.Settings;
import co.skyclient.scc.cosmetics.Tag;
import co.skyclient.scc.cosmetics.TagCosmetics;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;

public class PlayerListeners {

    @SubscribeEvent
    public void onPlayerLoggedIn(FMLNetworkEvent.ClientConnectedToServerEvent event) {
        if (Settings.joinMessage) {
            Notifications.INSTANCE.send(SkyclientCosmetics.MOD_NAME, "Welcome to SkyClient Cosmetics!\nType /scc in chat to get started!\nType /api new in chat to set your Hypixel API Key!");
            Settings.joinMessage = false;
            SkyclientCosmetics.config.save();
        }
    }

    @SubscribeEvent
    public void onNameFormat(PlayerEvent.NameFormat event) {
        if (event.entity == null || event.entityPlayer == null || event.entityPlayer.isDead || event.displayname == null) return;

        if (Settings.displayTags) {
            Tag tag = TagCosmetics.getInstance().getTag(event.displayname);
            if (tag != null) {
                event.displayname = tag.getTag() + " " + event.displayname;
            }
        }
    }
}
