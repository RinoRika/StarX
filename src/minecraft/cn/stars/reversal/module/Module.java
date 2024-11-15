package cn.stars.reversal.module;

import cn.stars.reversal.GameInstance;
import cn.stars.reversal.Reversal;
import cn.stars.reversal.event.impl.*;
import cn.stars.reversal.setting.Setting;
import cn.stars.reversal.ui.notification.NotificationType;
import cn.stars.reversal.util.animation.rise.Animation;
import cn.stars.reversal.util.animation.rise.Easing;
import cn.stars.reversal.util.animation.simple.SimpleAnimation;
import cn.stars.reversal.util.misc.ModuleInstance;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.gui.ScaledResolution;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Getter
@Setter
public abstract class Module implements GameInstance {

    private ScaledResolution sr = new ScaledResolution(mc);
    public boolean shouldCallNotification = true;
    public int scaledWidth = sr.getScaledWidth();
    public int scaledHeight = sr.getScaledHeight();
    public boolean enabled;
    public int keyBind;
    public String suffix;

    // RenderPosition
    public float renderX, renderY;

    // Modern ClickGUI
    public float guiX, guiY;
    public Animation alphaAnimation = new Animation(Easing.EASE_OUT_EXPO, 400);
    public Animation yAnimation = new Animation(Easing.EASE_OUT_EXPO, 500);
    public Animation sizeAnimation = new Animation(Easing.EASE_OUT_EXPO, 500);

    public float sizeInGui;
    public boolean expanded;
    public float clickGuiOpacity;
    public float descOpacityInGui = 1;
    public boolean hidden = false;
    public int x, y, draggingX, draggingY;
    public int width, height;
    public boolean dragging, hide, canBeEdited;
    public SimpleAnimation buttonAnimation = new SimpleAnimation(0.0F);
    public SimpleAnimation buttonOpacityAnimation = new SimpleAnimation(0.0F);
    public SimpleAnimation selectAnimation = new SimpleAnimation(0.0F);
    public SimpleAnimation editOpacityAnimation = new SimpleAnimation(0.0F);
    public SimpleAnimation boxAnimation = new SimpleAnimation(0.0F);

    //Module Settings
    public List<Setting> settings = new ArrayList<>();
    private ModuleInfo moduleInfo;

    public Module() {
        if (this.getClass().isAnnotationPresent(ModuleInfo.class)) {
            this.moduleInfo = this.getClass().getAnnotation(ModuleInfo.class);
        } else {
            throw new RuntimeException("ModuleInfo annotation not found on " + this.getClass().getSimpleName());
        }
        keyBind = getModuleInfo().defaultKey();
        this.x = 100;
        this.y = 100;
        this.width = 100;
        this.height = 100;
    }

    public void registerNotification(String s) {
        Reversal.notificationManager.registerNotification(s);
    }

    public void registerNotification(String s, NotificationType t) {
        Reversal.notificationManager.registerNotification(s, t);
    }

    public void registerNotification(String s, NotificationType t, long d) {
        Reversal.notificationManager.registerNotification(s,d,t);
    }

    public void registerNotification(String s, String ti, NotificationType t, long d) {
        Reversal.notificationManager.registerNotification(s, ti, d, t);
    }

    public void addChatMessage(String s) {
        Reversal.showMsg(s);
    }

    public boolean toggleModule() {
        if (Reversal.isDestructed) return false;
        boolean canNoti = ModuleInstance.getBool("ClientSettings", "Show Notifications").isEnabled() && shouldCallNotification;
        enabled = !enabled;

        if (enabled) {
            onEnable();
            mc.getSoundHandler().playButtonPress();
            if (canNoti) Reversal.notificationManager.registerNotification(
                    "Enabled" + " " + getModuleInfo().name(), "Module", 2000, NotificationType.SUCCESS);
        }
        else {
            onDisable();
            mc.getSoundHandler().playButtonPress();
            if (canNoti) Reversal.notificationManager.registerNotification(
                    "Disabled" + " " + getModuleInfo().name(), "Module", 2000, NotificationType.ERROR);
        }

        renderX = mc.displayWidth;
        renderY = -mc.displayHeight;

        Reversal.moduleManager.setEdited(true);

        return enabled;
    }

    public Module getModule(String module) {
        return ModuleInstance.getModule(module);
    }

    public Module getModule(Class module) {
        return ModuleInstance.getModule(module);
    }

    public boolean hasSuffix() {
        return suffix != null;
    }

    // Control the whole module elements
    public boolean canBlur() {
        return ModuleInstance.getBool("PostProcessing", "Blur").isEnabled();
    }

    public boolean toggleNoEvent() {
        enabled = !enabled;
        return enabled;
    }

    protected void onEnable() {
    }

    protected void onDisable() {
    }

    public Setting getSetting(final String name) {
        for (final Setting setting : settings) {
            if (setting.name.equalsIgnoreCase(name)) {
                return setting;
            }
        }

        return null;
    }

    public Setting getSettingAlternative(final String name) {
        for (final Setting setting : settings) {
            final String comparingName = setting.name.replaceAll(" ", "");

            if (comparingName.equalsIgnoreCase(name)) {
                return setting;
            }
        }

        return null;
    }

    protected double randomDouble(final double min, final double max) {
        return ThreadLocalRandom.current().nextDouble(min, max);
    }

    protected int randomInt(final int min, final int max) {
        return ThreadLocalRandom.current().nextInt(min, max);
    }

    protected double random() {
        return ThreadLocalRandom.current().nextDouble(1);
    }

    /**
     * Event methods are here and can be overridden from subclasses for event listening.
     */
    public void onPreMotion(final PreMotionEvent event) {
    }

    public void onPostMotion(final PostMotionEvent event) {
    }

    public void onMove(final MoveEvent event) {
    }


    public void onPacketReceive(final PacketReceiveEvent event) {
    }

    public void onPacketSend(final PacketSendEvent event) {
    }

    public void onStrafe(final StrafeEvent event) {
    }

    public void onShader3D(final Shader3DEvent event) {
    }

    public void onAlpha(final AlphaEvent event) {
    }

    public void onRender3D(final Render3DEvent event) {
    }

    public void onRender2D(final Render2DEvent event) {
    }

    public void onCanPlaceBlock(final CanPlaceBlockEvent event) {
    }

    public void onBlockBreak(final BlockBreakEvent event) {
    }

    public void onAttack(final AttackEvent event) {
    }

    public void onMoveButton(final MoveButtonEvent event) {
    }

    public void onKey(final KeyEvent event) {
    }

    public void onUpdateAlwaysInGui() {
    }

    public void onUpdateAlways() {
    }

    public void onBlur(final BlurEvent event) {
    }

    public void onFadingOutline(final FadingOutlineEvent event) {
    }

    public void onPreBlur(final PreBlurEvent event) {
    }

    public void onOpenGUI(final OpenGUIEvent event) {
    }

    public void onWorld(final WorldEvent event) {
    }

    public void onUpdate(final UpdateEvent event) {
    }

    public void onBlockCollide(final BlockCollideEvent event) {
    }

    public void onTeleport(final TeleportEvent event) {
    }

    public void onTick(final TickEvent event) {
    }

    public void onClick(final ClickEvent event) {
    }

    public void onLoad() {
    }
}