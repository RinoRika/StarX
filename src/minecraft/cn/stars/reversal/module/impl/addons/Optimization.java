package cn.stars.reversal.module.impl.addons;

import cn.stars.reversal.module.Category;
import cn.stars.reversal.module.Module;
import cn.stars.reversal.module.ModuleInfo;
import cn.stars.reversal.setting.impl.BoolValue;

@ModuleInfo(name = "Optimization", chineseName = "优化", description = "Optimize client performance", chineseDescription = "优化客户端性能", category = Category.ADDONS)
public class Optimization extends Module {
    private final BoolValue ai_lookAI = new BoolValue("AI Improvements - Look AI", this, true);
    private final BoolValue ai_lookIdle = new BoolValue("AI Improvements - Look Idle", this, true);
    private final BoolValue ai_lookHelper = new BoolValue("AI Improvements - Look Helper", this, true);
    private final BoolValue entityCulling = new BoolValue("Entity Culling", this, true);
}