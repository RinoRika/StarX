/*
 * StarX Client - A PVP Client with hack visual.
 * Copyright 2024 Starlight, All rights reserved.
 */
package cn.stars.starx.module.impl.misc;

import cn.stars.starx.module.Category;
import cn.stars.starx.module.Module;
import cn.stars.starx.module.ModuleInfo;
import cn.stars.starx.setting.impl.NoteValue;

@ModuleInfo(name = "CustomName", chineseName = "自定义名称", description = "Customize your minecraft name", chineseDescription = "自定义你的我的世界名称", category = Category.MISC)
public class CustomName extends Module {
    private final NoteValue note = new NoteValue("Use \".name <your name>\" to edit custom name!", this);
}
