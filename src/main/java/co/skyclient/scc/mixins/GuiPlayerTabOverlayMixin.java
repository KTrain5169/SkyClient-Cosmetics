package co.skyclient.scc.mixins;

import cc.polyfrost.oneconfig.utils.hypixel.HypixelUtils;
import co.skyclient.scc.SkyclientCosmetics;
import co.skyclient.scc.config.Settings;
import co.skyclient.scc.cosmetics.Tag;
import co.skyclient.scc.hooks.NetworkPlayerInfoHook;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.client.network.NetworkPlayerInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GuiPlayerTabOverlay.class)
public class GuiPlayerTabOverlayMixin {
    @Inject(method = "getPlayerName", at = @At("RETURN"), cancellable = true)
    private void getPlayerName(NetworkPlayerInfo networkPlayerInfoIn, CallbackInfoReturnable<String> cir) {
        if (SkyclientCosmetics.config.enabled && Settings.showTags && HypixelUtils.INSTANCE.isHypixel()) {
            Tag tag = ((NetworkPlayerInfoHook) networkPlayerInfoIn).scc$getTag();
            if (tag != null) {
                cir.setReturnValue(tag.getTag() + " " + cir.getReturnValue());
            }
        }
    }
}
