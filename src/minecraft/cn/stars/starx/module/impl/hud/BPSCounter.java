package cn.stars.starx.module.impl.hud;

import cn.stars.starx.module.Category;
import cn.stars.starx.module.Module;
import cn.stars.starx.module.ModuleInfo;
import cn.stars.starx.setting.impl.BoolValue;
import net.minecraft.client.gui.ScaledResolution;

@ModuleInfo(name = "BPSCounter", description = "Show your BPS on screen",
        chineseDescription = "显示你的移动速度", category = Category.HUD)
public class BPSCounter extends Module {
    public BPSCounter() {
        setCanBeEdited(true);
        setX(5);
        setY(300);
        setWidth(100);
        setHeight(30);
    }

}
