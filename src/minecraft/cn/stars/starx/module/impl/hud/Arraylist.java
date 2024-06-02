package cn.stars.starx.module.impl.hud;

import cn.stars.starx.module.Category;
import cn.stars.starx.module.Module;
import cn.stars.starx.module.ModuleInfo;
import cn.stars.starx.setting.impl.BoolValue;
import net.minecraft.client.gui.ScaledResolution;

@ModuleInfo(name = "Arraylist", description = "Show the modules you enabled", category = Category.HUD)
public class Arraylist extends Module {
    private final BoolValue shadow = new BoolValue("Shadow", this, true);
    final ScaledResolution SR = new ScaledResolution(mc);
    final float offset = 6;
    final float arraylistX = SR.getScaledWidth() - offset;

    public Arraylist() {
        setCanBeEdited(true);
        setX((int) arraylistX);
    }
}
