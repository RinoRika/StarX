package cn.stars.reversal.util.misc;

import cn.stars.reversal.Reversal;
import cn.stars.reversal.module.Module;
import cn.stars.reversal.value.impl.BoolValue;
import cn.stars.reversal.value.impl.ModeValue;
import cn.stars.reversal.value.impl.NumberValue;
import cn.stars.reversal.value.impl.TextValue;
import lombok.NonNull;

@NonNull
public class ModuleInstance {
    public static Module getModule(String moduleName) {
        return Reversal.moduleManager.getModule(moduleName);
    }
    public static <T extends Module> T getModule(Class<T> clazz) {
        return (T) Reversal.moduleManager.getByClass(clazz);
    }
    public static ModeValue getMode(String moduleName, String settingName) throws ClassCastException {
        return (ModeValue) Reversal.moduleManager.getSetting(moduleName, settingName);
    }
    public static BoolValue getBool(String moduleName, String settingName) throws ClassCastException {
        return (BoolValue) Reversal.moduleManager.getSetting(moduleName, settingName);
    }
    public static TextValue getText(String moduleName, String settingName) throws ClassCastException {
        return (TextValue) Reversal.moduleManager.getSetting(moduleName, settingName);
    }
    public static NumberValue getNumber(String moduleName, String settingName) throws ClassCastException {
        return (NumberValue) Reversal.moduleManager.getSetting(moduleName, settingName);
    }
}
