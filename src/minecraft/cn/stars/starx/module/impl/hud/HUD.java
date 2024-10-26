package cn.stars.starx.module.impl.hud;

import cn.stars.starx.module.Category;
import cn.stars.starx.module.Module;
import cn.stars.starx.module.ModuleInfo;
import cn.stars.starx.setting.impl.BoolValue;

@ModuleInfo(name = "HUD", chineseName = "主界面", description = "Show a hud on your screen",
        chineseDescription = "在屏幕上显示你的HUD", category = Category.HUD)
public class HUD extends Module {
    private final BoolValue display_when_debugging = new BoolValue("Display when debugging", this, false);
    public HUD() {
        setWidth(0);
        setHeight(0);
        setCanBeEdited(false);
    }
}
