package cn.stars.starx.ui.hud;

import cn.stars.starx.GameInstance;
import cn.stars.starx.StarX;
import cn.stars.starx.font.FontManager;
import cn.stars.starx.module.Category;
import cn.stars.starx.module.Module;
import cn.stars.starx.module.impl.hud.*;
import cn.stars.starx.setting.impl.ModeValue;
import cn.stars.starx.util.StarXLogger;
import cn.stars.starx.util.math.MathUtil;
import cn.stars.starx.util.math.TimeUtil;
import cn.stars.starx.util.misc.ModuleInstance;
import cn.stars.starx.util.render.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.renderer.GlStateManager;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class Hud implements GameInstance {
    public static float ticks, ticksSinceClickgui;
    public static float positionOfLastModule;
    public static String key;
    static List<Object> modules;
    private static final TimeUtil timer2 = new TimeUtil();
    private static final TimeUtil timer = new TimeUtil();
    public static final KeystrokeUtil forward = new KeystrokeUtil();
    public static final KeystrokeUtil backward = new KeystrokeUtil();
    public static final KeystrokeUtil left = new KeystrokeUtil();
    public static final KeystrokeUtil right = new KeystrokeUtil();
    public static final KeystrokeUtil space = new KeystrokeUtil();
    static Keystrokes keystrokes;
    static BPSCounter bpsCounter;
    static Arraylist arraylist;
    static TextGui textGui;
    static ModuleComparator moduleComparator = new ModuleComparator();

    public static void renderKeyStrokes() {
        if (keystrokes.isEnabled()) {

            final int x = keystrokes.getX() + 35;
            final int y = keystrokes.getY();

            final int distanceBetweenButtons = 30;
            final int width = 26;

            forward.setUpKey(mc.gameSettings.keyBindForward);
            forward.updateAnimations();
            forward.drawButton(x, y, width);

            backward.setUpKey(mc.gameSettings.keyBindBack);
            backward.updateAnimations();
            backward.drawButton(x, y + distanceBetweenButtons, width);

            left.setUpKey(mc.gameSettings.keyBindLeft);
            left.updateAnimations();
            left.drawButton(x - distanceBetweenButtons, y + distanceBetweenButtons, width);

            right.setUpKey(mc.gameSettings.keyBindRight);
            right.updateAnimations();
            right.drawButton(x + distanceBetweenButtons, y + distanceBetweenButtons, width);

            space.setUpKey(mc.gameSettings.keyBindJump);
            space.updateAnimations();
            space.drawButton(x - 2, y + distanceBetweenButtons * 2, width);
        }
    }

    private static void renderBPS() {
        if (!bpsCounter.isEnabled()) return;

        final String mode = ModuleInstance.getMode("ClientSettings", "Theme").getMode();

        final double x = bpsCounter.getX(), y = bpsCounter.getY() + 15;
        final String bps = "BPS: " + MathUtil.round(mc.thePlayer.getSpeed(), 2);
        final String bps2 = "Speed: " + MathUtil.round(mc.thePlayer.getSpeed(), 2);
        switch (mode) {
            case "Minecraft": {
                Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(bps, (float) x, (float) y, ThemeUtil.getThemeColorInt(ThemeType.GENERAL));
                break;
            }

            case "StarX": {
                gs.drawStringWithShadow(bps2, (float) x, (float) y, ThemeUtil.getThemeColorInt(ThemeType.GENERAL));
                gs.drawStringWithShadow("X:" + MathUtil.round(mc.thePlayer.posX, 1) + " Y:" + MathUtil.round(mc.thePlayer.posY, 1) + " Z:" + MathUtil.round(mc.thePlayer.posZ,1),(float) x, (float) y - 10f, ThemeUtil.getThemeColorInt(ThemeType.GENERAL));
                break;
            }

            case "Modern": {
                psm16.drawString(bps2, (float) x, (float) y, ThemeUtil.getThemeColorInt(ThemeType.GENERAL));
                psm16.drawString("X:" + MathUtil.round(mc.thePlayer.posX, 1) + " Y:" + MathUtil.round(mc.thePlayer.posY, 1) + " Z:" + MathUtil.round(mc.thePlayer.posZ, 1), (float) x, (float) y - 8f, ThemeUtil.getThemeColorInt(ThemeType.GENERAL));
                break;
            }

            case "Simple": {
                regular16.drawString(bps2, (float) x, (float) y, ThemeUtil.getThemeColorInt(ThemeType.GENERAL));
                regular16.drawString("X:" + MathUtil.round(mc.thePlayer.posX, 1) + " Y:" + MathUtil.round(mc.thePlayer.posY, 1) + " Z:" + MathUtil.round(mc.thePlayer.posZ, 1), (float) x, (float) y - 8f, ThemeUtil.getThemeColorInt(ThemeType.GENERAL));
                break;
            }

            default: {
                regular16.drawStringWithShadow(bps, (float) x, (float) y, ThemeUtil.getThemeColorInt(ThemeType.GENERAL));
                break;
            }
        }
    }


    public static class ModuleComparator implements Comparator<Object> {
        @Override
        public int compare(final Object o1, final Object o2) {
            ModeValue setting = ModuleInstance.getMode("ClientSettings", "Theme");

            if (setting == null) return 1;

            final String mode = setting.getMode();
//
//            final String name = o1 instanceof Module ? ((Module) o1).getModuleInfo().name() : ((Script) o1).getName();
//            final String name2 = o2 instanceof Module ? ((Module) o2).getModuleInfo().name() : ((Script) o2).getName();

            final String name = mode.equals("Simple") && ModuleInstance.getBool("Arraylist", "Simple Chinese").isEnabled() && !((Module) o1).getModuleInfo().chineseName().isEmpty() ? ((Module) o1).getModuleInfo().chineseName() : ((Module) o1).getModuleInfo().name();
            final String name2 = mode.equals("Simple") && ModuleInstance.getBool("Arraylist", "Simple Chinese").isEnabled() && !((Module) o2).getModuleInfo().chineseName().isEmpty() ? ((Module) o2).getModuleInfo().chineseName() : ((Module) o2).getModuleInfo().name();

            switch (mode) {
                case "Minecraft": {
                    return Float.compare(Minecraft.getMinecraft().fontRendererObj.getStringWidth(name2), Minecraft.getMinecraft().fontRendererObj.getStringWidth(name));
                }

                case "StarX": {
                    return Float.compare(gs.getWidth(name2), gs.getWidth(name));
                }

                case "Simple": {
                    return Float.compare(regular16.getWidth(name2), regular16.getWidth(name));
                }

                case "Modern": {
                    return Float.compare(psm17.getWidth(name2), psm17.getWidth(name));
                }

                default: {
                    return Float.compare(regular16.getWidth(name2), regular16.getWidth(name));
                }
            }
        }
    }

    private static void renderArrayList() {
        if (!arraylist.isEnabled()) return;
        final String mode = ModuleInstance.getMode("ClientSettings", "Theme").getMode();

        final float offset = 6;

        final float arraylistX = arraylist.getX() + arraylist.getWidth();

        modules = new ArrayList<>();

        modules.addAll(StarX.moduleManager.getEnabledModules());

        modules.sort(moduleComparator);

        int moduleCount = 0;

        for (final Object n : modules) {

            float posOnArraylist = offset + moduleCount * 10.8f;

            assert n instanceof Module;
            final String name = mode.equals("Simple") && ModuleInstance.getBool("Arraylist", "Simple Chinese").isEnabled() && !((Module) n).getModuleInfo().chineseName().isEmpty() ? ((Module) n).getModuleInfo().chineseName() : ((Module) n).getModuleInfo().name();

            float finalX = 0;
            final float speed = 6;

            final float renderX = ((Module) n).getRenderX();
            final float renderY = arraylist.getY() + ((Module) n).getRenderY();

            if (((Module) n).getModuleInfo().category().equals(Category.RENDER) && ModuleInstance.getBool("Arraylist", "No Render Modules").isEnabled())
                continue;

            switch (mode) {
                case "Minecraft": {
                    finalX = arraylistX - mc.fontRendererObj.getStringWidth(name);

                    mc.fontRendererObj.drawStringWithShadow(name, renderX, renderY, ThemeUtil.getThemeColorInt(moduleCount, ThemeType.ARRAYLIST));

                    break;
                }

                case "Simple": {
                    final int offsetY = 2;
                    final int offsetX = 1;

                    final double stringWidth = regular16.getWidth(name);

                    RenderUtil.rect(renderX - offsetX, renderY - offsetY + 0.5, stringWidth + offsetX * 1.5 + 1, 8.8 + offsetY, new Color(0, 0, 0, 60));
                    RenderUtil.rect(renderX - offsetX + stringWidth + offsetX * 1.5 + 1, renderY - offsetY + 0.4, 1, 8.8 + offsetY, ThemeUtil.getThemeColor(moduleCount, ThemeType.ARRAYLIST));

                    finalX = arraylistX - regular16.getWidth(name);

                    regular16.drawString(name, renderX, renderY + 2, ThemeUtil.getThemeColorInt(moduleCount, ThemeType.ARRAYLIST));

                    final int mC = moduleCount;
                    if (ModuleInstance.getBool("PostProcessing", "Bloom").isEnabled()) {
                        MODERN_BLOOM_RUNNABLES.add(() -> {
                            RenderUtil.rect(renderX - offsetX, renderY - offsetY + 0.5, stringWidth + offsetX * 1.5 + 1, 8.8 + offsetY, Color.BLACK);
                            RenderUtil.rect(renderX - offsetX + stringWidth + offsetX * 1.5 + 1, renderY - offsetY + 0.4, 1, 8.8 + offsetY, ThemeUtil.getThemeColor(mC, ThemeType.ARRAYLIST));
                        });
                    }

                    if (ModuleInstance.getBool("PostProcessing", "Blur").isEnabled()) {
                        MODERN_BLUR_RUNNABLES.add(() -> {
                            RenderUtil.rect(renderX - offsetX, renderY - offsetY + 0.5, stringWidth + offsetX * 1.5 + 1, 8.8 + offsetY, Color.BLACK);
                        });

                    }
                }
                break;

                case "Empathy": {
                    final int offsetY = 2;
                    final int offsetX = 1;

                    final double stringWidth = regular16.getWidth(name);

                    RenderUtil.roundedRectangle(renderX - offsetX - 15, renderY - offsetY + 0.5, stringWidth + offsetX * 1.5 + 1, 9.15 + offsetY, 2f, new Color(20,20,20,200));
                    RenderUtil.rect(renderX - offsetX + stringWidth + offsetX * 1.5 - 2, renderY - offsetY + 0.4, 1, 8.8 + offsetY, ThemeUtil.getThemeColor(moduleCount, ThemeType.ARRAYLIST));

                    finalX = arraylistX - regular16.getWidth(name);

                    regular16.drawString(name, renderX - 15, renderY + 2.2, ThemeUtil.getThemeColorInt(moduleCount, ThemeType.ARRAYLIST));

                    RenderUtil.roundedRectangle(renderX - offsetX + stringWidth + offsetX * 1.5 + 0.5 - 13, renderY - offsetY + 0.5, 10, 9.15 + offsetY, 2f, new Color(20,20,20,200));
                    FontManager.getMi(16).drawString("j", renderX - offsetX + stringWidth + offsetX * 1.5 + 0.5 - 12, renderY + 2.2, ThemeUtil.getThemeColorInt(moduleCount, ThemeType.ARRAYLIST));

                    final int mC = moduleCount;
                    if (ModuleInstance.getBool("PostProcessing", "Bloom").isEnabled()) {
                        MODERN_BLOOM_RUNNABLES.add(() -> {
                            RenderUtil.roundedRectangle(renderX - offsetX - 15, renderY - offsetY + 0.5, stringWidth + offsetX * 1.5 + 1, 9.15 + offsetY, 2f, Color.BLACK);
                            RenderUtil.roundedRectangle(renderX - offsetX + stringWidth + offsetX * 1.5 + 0.5 - 13, renderY - offsetY + 0.5, 10, 9.15 + offsetY, 2f, new Color(20,20,20,200));
                            RenderUtil.rect(renderX - offsetX + stringWidth + offsetX * 1.5 - 2, renderY - offsetY + 0.4, 1, 8.8 + offsetY, ThemeUtil.getThemeColor(mC, ThemeType.ARRAYLIST));
                        });
                    }

                    if (ModuleInstance.getBool("PostProcessing", "Blur").isEnabled()) {
                        MODERN_BLUR_RUNNABLES.add(() -> {
                            RenderUtil.rect(renderX - offsetX, renderY - offsetY + 0.5, stringWidth + offsetX * 1.5 + 1, 8.8 + offsetY, Color.BLACK);
                        });

                    }
                }
                break;

                case "StarX": {
                    final int offsetY = 2;
                    final int offsetX = 1;

                    final double stringWidth = gs.getWidth(name);
                    posOnArraylist = offset + moduleCount * (gs.getHeight() + 1.3f);

                    RenderUtil.rect(renderX - offsetX, renderY - offsetY + 0.5, stringWidth + offsetX * 1.5, gs.getHeight() + offsetY - 0.7, new Color(0, 0, 0, 60));
                    RenderUtil.roundedRect(renderX + stringWidth, renderY - offsetY + 0.5, 2, gs.getHeight() + offsetY - 0.6, 2.5, ColorUtil.liveColorBrighter(new Color(0,255,255), 1f));

                    finalX = arraylistX - gs.getWidth(name);

                    gs.drawString(name, renderX, renderY + 2, ThemeUtil.getThemeColorInt(moduleCount, ThemeType.ARRAYLIST));
                }
                break;

                case "Modern": {
                    final int offsetY = 2;
                    final int offsetX = 1;

                    final double stringWidth = psm17.getWidth(name);
                    final int mC = moduleCount;

                    if (!ModuleInstance.getBool("ClientSettings", "ThunderHack").isEnabled()) {
                        Runnable shadowRunnable = () -> {
                            RenderUtil.rect(renderX - offsetX - 1, renderY - offsetY + 0.2, stringWidth + offsetX * 1.5 + 1, 10.3 + offsetY - 1.5,
                                    ModuleInstance.getBool("Arraylist", "Glow Shadow").isEnabled() ? ThemeUtil.getThemeColor(mC, ThemeType.ARRAYLIST) : Color.BLACK);
                            RenderUtil.roundedRectangle(renderX + stringWidth, renderY - offsetY, 2, 10.3 + offsetY - 0.5, 2.5, ColorUtil.liveColorBrighter(new Color(0, 255, 255), 1f));
                        };

                        Runnable blurRunnable = () -> {
                            RenderUtil.rect(renderX - offsetX - 1, renderY - offsetY + 0.2, stringWidth + offsetX * 1.5 + 1, 10.3 + offsetY - 1.5, Color.BLACK);
                        };

                        RenderUtil.rect(renderX - offsetX - 1, renderY - offsetY + 0.2, stringWidth + offsetX * 1.5 + 1, 10.3 + offsetY - 1.5, new Color(0, 0, 0, 80));
                        RenderUtil.roundedRectangle(renderX + stringWidth, renderY - offsetY, 2, 10.3 + offsetY - 0.5, 2.5, ColorUtil.liveColorBrighter(new Color(0, 255, 255), 1f));

                        if (ModuleInstance.getBool("PostProcessing", "Bloom").isEnabled()) {
                            MODERN_BLOOM_RUNNABLES.add(shadowRunnable);
                        }

                        if (ModuleInstance.getBool("PostProcessing", "Blur").isEnabled()) {
                            MODERN_BLUR_RUNNABLES.add(blurRunnable);
                        }

                        psm17.drawString(name, renderX - 1, renderY + 0.6, ThemeUtil.getThemeColorInt(mC, ThemeType.ARRAYLIST));
                    } else {
                        Runnable shadowRunnable = () -> {
                            RenderUtil.rect(renderX - offsetX - 1, renderY - offsetY + 0.2, stringWidth + offsetX * 1.5 + 1, 10.3 + offsetY - 1.5,
                                    ColorUtils.INSTANCE.interpolateColorsBackAndForth(5, mC * 25, Color.WHITE, Color.BLACK, true));
                            RenderUtil.roundedRectangle(renderX + stringWidth, renderY - offsetY, 2, 10.3 + offsetY - 0.5, 2.5, ColorUtils.INSTANCE.interpolateColorsBackAndForth(5, mC * 25, Color.WHITE, Color.BLACK, true));
                        };

                        Runnable blurRunnable = () -> {
                            RenderUtil.rect(renderX - offsetX - 1, renderY - offsetY + 0.2, stringWidth + offsetX * 1.5 + 1, 10.3 + offsetY - 1.5, Color.BLACK);
                        };

                        if (ModuleInstance.getBool("PostProcessing", "Bloom").isEnabled()) {
                            MODERN_BLOOM_RUNNABLES.add(shadowRunnable);
                        }

                        if (ModuleInstance.getBool("PostProcessing", "Blur").isEnabled()) {
                            MODERN_BLUR_RUNNABLES.add(blurRunnable);
                        }

                        RenderUtil.rect(renderX - offsetX - 1, renderY - offsetY + 0.2, stringWidth + offsetX * 1.5 + 1, 10.3 + offsetY - 1.5, new Color(0, 0, 0, 150));
                        RenderUtil.roundedRectangle(renderX + stringWidth, renderY - offsetY, 2, 10.3 + offsetY - 0.5, 2.5, ColorUtils.INSTANCE.interpolateColorsBackAndForth(5, moduleCount * 25, Color.WHITE, Color.BLACK, true));
                        psm17.drawString(name, renderX - 1, renderY + 0.6, ColorUtils.INSTANCE.interpolateColorsBackAndForth(5, moduleCount * 25, Color.WHITE, Color.BLACK, true).getRGB());
                    }

                    finalX = arraylistX - psm17.getWidth(name);
                }
                break;
            }

            moduleCount++;

            final String animationMode = ((ModeValue) Objects.requireNonNull(StarX.moduleManager.getSetting("ClientSettings", "List Animation"))).getMode();

            final Module m = ((Module) n);
            if (timer2.hasReached(1000 / 100)) {
                switch (animationMode) {
                    case "StarX":
                        m.renderX = (m.renderX * (speed - 1) + finalX) / speed;
                        m.renderY = (m.renderY * (speed - 1) + posOnArraylist) / speed;

                        break;
                    case "Slide":
                        m.renderX = (m.renderX * (speed - 1) + finalX) / speed;

                        if (m.renderY < positionOfLastModule) {
                            m.renderY = posOnArraylist;
                        } else {
                            m.renderY = (m.renderY * (speed - 1) + posOnArraylist) / (speed);
                        }
                        break;
                }
            }

            positionOfLastModule = posOnArraylist;

        }

        arraylist.setHeight(moduleCount * 12);

        // Resetting timer
        if (timer2.hasReached(1000 / 100)) {
            timer2.reset();
        }

        if (timer.hasReached(1000 / 60)) {
            timer.reset();

            if (mc.ingameGUI != null && !(mc.currentScreen instanceof GuiChat)) {
                if (ticksSinceClickgui <= 5)
                    ticksSinceClickgui++;
            } else {
                if (ticksSinceClickgui >= 1)
                    ticksSinceClickgui--;
            }

            forward.updateAnimations();
            backward.updateAnimations();
            left.updateAnimations();
            right.updateAnimations();
            space.updateAnimations();
        }
    }
    
    private static void renderClientName() {
        if (!textGui.isEnabled()) return;
        final String mode = ModuleInstance.getMode("ClientSettings", "Theme").getMode();
        final boolean useDefaultName = !ModuleInstance.getBool("TextGui", "Custom Name").isEnabled();

        final float offset;
        String name = StarX.NAME, customName = ThemeUtil.getCustomClientName();

        if (customName.isEmpty()) customName = "Use \".clientname <name>\" to set custom name.";
        switch (mode) {
            case "Minecraft": {
                if (useDefaultName) {
                    textGui.setWidth(75);
                    float off = 0;

                    for (int i = 0; i < name.length(); i++) {
                        final String character = String.valueOf(name.charAt(i));

                        mc.fontRendererObj.drawStringWithShadow(character, textGui.getX() + 1 + off, textGui.getY(), ThemeUtil.getThemeColorInt(i, ThemeType.ARRAYLIST));

                        off += mc.fontRendererObj.getStringWidth(character);
                    }

                    off = mc.fontRendererObj.getStringWidth(name);
                    mc.fontRendererObj.drawStringWithShadow("[" + StarX.VERSION + "]", textGui.getX() + off + 7, textGui.getY(), ThemeUtil.getThemeColorInt(ThemeType.ARRAYLIST));
                } else {
                    textGui.setWidth((int) (20 + mc.fontRendererObj.getStringWidth(customName) * 1.5));
                    float off = 0;

                    for (int i = 0; i < customName.length(); i++) {
                        final String character = String.valueOf(customName.charAt(i));
                        mc.fontRendererObj.drawStringWithShadow(character, textGui.getX() + 1 + off, textGui.getY(), ThemeUtil.getThemeColorInt(i, ThemeType.ARRAYLIST));

                        off += mc.fontRendererObj.getStringWidth(character);
                    }
                }
                break;
            }

            case "StarX": {
                if (useDefaultName) {
                    textGui.setWidth(100);
                    gsTitle.drawStringWithShadow("S", textGui.getX() + 7, textGui.getY() + 5, ThemeUtil.getThemeColorInt(ThemeType.ARRAYLIST));
                    gsTitle.drawStringWithShadow("tarX [" + StarX.VERSION + "]", textGui.getX() + 7 + gsTitle.getWidth("S"), textGui.getY() + 4.9f, new Color(230, 230, 230, 200).getRGB());
                } else {
                    textGui.setWidth((int) (20 + gsTitle.getWidth(customName)));
                    // FOOLISH
                    gsTitle.drawStringWithShadow(String.valueOf(customName.charAt(0)), textGui.getX() + 7, textGui.getY() + 5, ThemeUtil.getThemeColorInt(ThemeType.ARRAYLIST));
                    // 从字符串第二个字开始获取
                    gsTitle.drawStringWithShadow(customName.substring(1), textGui.getX() + 5 + gsTitle.getWidth(String.valueOf(customName.charAt(0))), textGui.getY() + 4.9f, new Color(230, 230, 230, 200).getRGB());
                }
                break;
            }

            case "Simple": {
                String extraText = " | " + Minecraft.getDebugFPS() + " FPS | " + mc.getSession().getUsername();

                if (useDefaultName) {
                    final String clientName = "STARX CLIENT";

                    textGui.setWidth((int) (100 + regular18.width(extraText)));
                    int x = textGui.getX() + 5;
                    int y = textGui.getY();
                    float off = 0;

                    RenderUtil.rect(x, y, regular20Bold.width(clientName) + regular18.width(extraText) + 6, regular20Bold.height() + 1.5, new Color(0, 0, 0, 80));

                    for (int i = 0; i < clientName.length(); i++) {
                        final String character = String.valueOf(clientName.charAt(i));

                        final float off1 = off;
                        regular20Bold.drawString(character, x + 4 + off1, y + 3.5, ThemeUtil.getThemeColorInt(i / 2, ThemeType.ARRAYLIST));
                        int finalI = i;
                        MODERN_BLOOM_RUNNABLES.add(() -> {
                            regular20Bold.drawString(character, x + 4 + off1, y + 3.5, ThemeUtil.getThemeColorInt(finalI / 2, ThemeType.ARRAYLIST));
                        });
                        off += regular20Bold.width(character);
                    }
                    
                    regular18.drawString(extraText, x + 4 + regular20Bold.width(clientName), y + 4,  new Color(250, 250, 250, 200).getRGB());

                    if (ModuleInstance.getBool("PostProcessing", "Bloom").isEnabled()) {
                        MODERN_BLOOM_RUNNABLES.add(() -> {
                            RenderUtil.rect(x, y, regular20Bold.width(clientName) + regular18.width(extraText) + 6, regular20Bold.height() + 1.5, Color.BLACK);
                        });
                    }

                    if (ModuleInstance.getBool("PostProcessing", "Blur").isEnabled()) {
                        MODERN_BLUR_RUNNABLES.add(() -> {
                            RenderUtil.rect(x, y, regular20Bold.width(clientName) + regular18.width(extraText) + 6, regular20Bold.height() + 1.5, Color.BLACK);
                        });
                    }

                } else {
                    final String clientName = customName;

                    textGui.setWidth((int) (20 + regular20Bold.width(clientName) + regular18.width(extraText)));
                    int x = textGui.getX() + 5;
                    int y = textGui.getY();
                    float off = 0;

                    RenderUtil.rect(x, y, regular20Bold.width(clientName) + regular18.width(extraText) + 6, regular20Bold.height() + 1.5, new Color(0, 0, 0, 80));

                    for (int i = 0; i < clientName.length(); i++) {
                        final String character = String.valueOf(clientName.charAt(i));

                        final float off1 = off;
                        regular20Bold.drawString(character, x + 4 + off1, y + 3.5, ThemeUtil.getThemeColorInt(i / 2, ThemeType.ARRAYLIST));
                        int finalI = i;
                        MODERN_BLOOM_RUNNABLES.add(() -> {
                            regular20Bold.drawString(character, x + 4 + off1, y + 3.5, ThemeUtil.getThemeColorInt(finalI / 2, ThemeType.ARRAYLIST));
                        });
                        off += regular20Bold.width(character);
                    }

                    regular18.drawString(extraText, x + 4 + off, y + 4, new Color(250, 250, 250, 200).getRGB());

                    if (ModuleInstance.getBool("PostProcessing", "Bloom").isEnabled()) {
                        MODERN_BLOOM_RUNNABLES.add(() -> {
                            RenderUtil.rect(x, y, regular20Bold.width(clientName) + regular18.width(extraText) + 6, regular20Bold.height() + 1.5, Color.BLACK);
                        });
                    }

                    if (ModuleInstance.getBool("PostProcessing", "Blur").isEnabled()) {
                        MODERN_BLUR_RUNNABLES.add(() -> {
                            RenderUtil.rect(x, y, regular20Bold.width(clientName) + regular18.width(extraText) + 6, regular20Bold.height() + 1.5, Color.BLACK);
                        });
                    }
                }
                break;
            }

            case "Empathy": {
                if (useDefaultName) {
                    final String clientName = "★ STARX";

                    textGui.setWidth(100);
                    int x = textGui.getX() + 5;
                    int y = textGui.getY();
                    float off = 0;

                    RenderUtil.roundedRectangle(x, y, regular20Bold.width(clientName) + 8, regular20Bold.height() + 1.5, 3f, new Color(20,20,20,200));
                    RenderUtil.roundedRectangle(x - 0.5, y + 2.5, 1.5, regular20Bold.height() - 3.5, 1f, ThemeUtil.getThemeColor(ThemeType.ARRAYLIST));

                    for (int i = 0; i < clientName.length(); i++) {
                        final String character = String.valueOf(clientName.charAt(i));

                        final float off1 = off;
                        regular20Bold.drawString(character, x + 4 + off1, y + 3.5, ThemeUtil.getThemeColorInt(i / 2, ThemeType.ARRAYLIST));
                        int finalI = i;
                        MODERN_BLOOM_RUNNABLES.add(() -> {
                            regular20Bold.drawString(character, x + 4 + off1, y + 3.5, ThemeUtil.getThemeColorInt(finalI / 2, ThemeType.ARRAYLIST));
                        });
                        off += regular20Bold.width(character);
                    }

                    if (ModuleInstance.getBool("PostProcessing", "Bloom").isEnabled()) {
                        MODERN_BLOOM_RUNNABLES.add(() -> {
                            RenderUtil.roundedRectangle(x, y, regular20Bold.width(clientName) + 8, regular20Bold.height() + 1.5, 3f, Color.BLACK);
                        });
                    }

                    if (ModuleInstance.getBool("PostProcessing", "Blur").isEnabled()) {
                        MODERN_BLUR_RUNNABLES.add(() -> {
                            RenderUtil.roundedRectangle(x, y, regular20Bold.width(clientName) + 8, regular20Bold.height() + 1.5, 3f, Color.BLACK);
                        });
                    }

                } else {
                    final String clientName = "★ " + customName;

                    textGui.setWidth((int) (20 + regular20Bold.width(clientName)));
                    int x = textGui.getX() + 5;
                    int y = textGui.getY();
                    float off = 0;

                    RenderUtil.roundedRectangle(x, y, regular20Bold.width(clientName) + 8, regular20Bold.height() + 1.5, 3f, new Color(20, 20, 20, 200));
                    RenderUtil.roundedRectangle(x - 0.5, y + 2.5, 1.5, regular20Bold.height() - 3.5, 1f, ThemeUtil.getThemeColor(ThemeType.ARRAYLIST));

                    for (int i = 0; i < clientName.length(); i++) {
                        final String character = String.valueOf(clientName.charAt(i));

                        final float off1 = off;
                        int finalI = i;
                        regular20Bold.drawString(character, x + 4 + off1, y + 3.5, ThemeUtil.getThemeColorInt(i / 2, ThemeType.ARRAYLIST));
                        MODERN_BLOOM_RUNNABLES.add(() -> {
                            regular20Bold.drawString(character, x + 4 + off1, y + 3.5, ThemeUtil.getThemeColorInt(finalI / 2, ThemeType.ARRAYLIST));
                        });
                        off += regular20Bold.width(character);
                    }

                    if (ModuleInstance.getBool("PostProcessing", "Bloom").isEnabled()) {
                        MODERN_BLOOM_RUNNABLES.add(() -> {
                            RenderUtil.roundedRectangle(x, y, regular20Bold.width(clientName) + 8, regular20Bold.height() + 1.5, 2f, Color.BLACK);
                        });
                    }

                    if (ModuleInstance.getBool("PostProcessing", "Blur").isEnabled()) {
                        MODERN_BLUR_RUNNABLES.add(() -> {
                            RenderUtil.roundedRectangle(x, y, regular20Bold.width(clientName) + 8, regular20Bold.height() + 1.5, 2f, Color.BLACK);
                        });
                    }
                }
                break;
            }

            case "Modern": {
                int x = textGui.getX() + 5;
                int y = textGui.getY();
                float off = 0;
                String extraText = " | " + Minecraft.getDebugFPS() + " FPS | " + mc.getSession().getUsername();
                float extraWidth = psm18.getWidth(extraText);

                if (!ModuleInstance.getBool("ClientSettings", "ThunderHack").isEnabled()) {
                    if (ModuleInstance.getBool("PostProcessing", "Blur").isEnabled()) {
                        MODERN_BLUR_RUNNABLES.add(() -> {
                            RoundedUtil.drawRound(x + 1, y + 1, 33 + extraWidth, 12, 4, Color.BLACK);
                        });
                    }

                    RoundedUtil.drawRound(x + 1, y + 1, 33 + extraWidth, 12, 4, new Color(0, 0, 0, 80));
                    RenderUtil.roundedOutlineRectangle(x, y, 35 + extraWidth, 14, 3, 1, ThemeUtil.getThemeColor(ThemeType.ARRAYLIST));
                //    RoundedUtil.drawRoundOutline(x, y, 35 + extraWidth, 14, 3, 0.1f, new Color(0, 0, 0, 80),
                //            ModuleInstance.getBool("TextGui", "Rainbow").isEnabled() ? ThemeUtil.getThemeColor(ThemeType.ARRAYLIST) : new Color(250, 250, 250, 200));
                    psm18.drawString(extraText, x + 30.5, y + 4f, new Color(250, 250, 250, 200).getRGB());

                    if (ModuleInstance.getBool("PostProcessing", "Bloom").isEnabled()) {
                        MODERN_BLOOM_RUNNABLES.add(() -> {
                            psm18.drawString(extraText, x + 30.5, y + 4f, ThemeUtil.getThemeColor(ThemeType.ARRAYLIST).getRGB());
                            RoundedUtil.drawRound(x + 1, y + 1, 33 + extraWidth, 12, 4, ThemeUtil.getThemeColor(ThemeType.ARRAYLIST));
                        });
                    }

                    textGui.setWidth((int) (44 + extraWidth));
                    for (int i = 0; i < name.length(); i++) {
                        final String character = String.valueOf(name.charAt(i));

                        final float off1 = off;
                        psb20.drawString(character, x + 4 + off1, y + 3.5, ThemeUtil.getThemeColorInt(i, ThemeType.ARRAYLIST));
                        int finalI = i;
                        MODERN_BLOOM_RUNNABLES.add(() -> {
                            psb20.drawString(character, x + 4 + off1, y + 3.5, ThemeUtil.getThemeColorInt(finalI, ThemeType.ARRAYLIST));
                        });
                        off += psb20.getWidth(character);
                    }
                } else {

                    if (ModuleInstance.getBool("PostProcessing", "Blur").isEnabled()) {
                        MODERN_BLUR_RUNNABLES.add(() -> {
                            RoundedUtil.drawGradientRound(x - 0.5f, y - 0.5f, 76 + extraWidth, 15, 3,
                                    ColorUtils.INSTANCE.interpolateColorsBackAndForth(3, 1000, Color.WHITE, Color.BLACK, true),
                                    ColorUtils.INSTANCE.interpolateColorsBackAndForth(3, 2000, Color.WHITE, Color.BLACK, true),
                                    ColorUtils.INSTANCE.interpolateColorsBackAndForth(3, 4000, Color.WHITE, Color.BLACK, true),
                                    ColorUtils.INSTANCE.interpolateColorsBackAndForth(3, 3000, Color.WHITE, Color.BLACK, true));
                            RoundedUtil.drawRound(x, y, 75 + extraWidth, 14, 3, new Color(0, 0, 0, 220));
                        });
                    }

                    //    RoundedUtil.drawRound(x, y, 35 + extraWidth, 14, 4, new Color(0, 0, 0, 80));
                    RoundedUtil.drawGradientRound(x - 0.5f, y - 0.5f, 76 + extraWidth, 15, 3,
                            ColorUtils.INSTANCE.interpolateColorsBackAndForth(3, 1000, Color.WHITE, Color.BLACK, true),
                            ColorUtils.INSTANCE.interpolateColorsBackAndForth(3, 2000, Color.WHITE, Color.BLACK, true),
                            ColorUtils.INSTANCE.interpolateColorsBackAndForth(3, 4000, Color.WHITE, Color.BLACK, true),
                            ColorUtils.INSTANCE.interpolateColorsBackAndForth(3, 3000, Color.WHITE, Color.BLACK, true));
                    RoundedUtil.drawRound(x, y, 75 + extraWidth, 14, 3, new Color(0, 0, 0, 220));

                    psm18.drawString(extraText, x + 72, y + 4f, new Color(200, 200, 200, 240).getRGB());

                    if (ModuleInstance.getBool("PostProcessing", "Bloom").isEnabled()) {
                        MODERN_BLOOM_RUNNABLES.add(() -> {
                            RoundedUtil.drawGradientRound(x - 0.5f, y - 0.5f, 76 + extraWidth, 15, 3,
                                    ColorUtils.INSTANCE.interpolateColorsBackAndForth(3, 1000, Color.WHITE, Color.BLACK, true),
                                    ColorUtils.INSTANCE.interpolateColorsBackAndForth(3, 2000, Color.WHITE, Color.BLACK, true),
                                    ColorUtils.INSTANCE.interpolateColorsBackAndForth(3, 4000, Color.WHITE, Color.BLACK, true),
                                    ColorUtils.INSTANCE.interpolateColorsBackAndForth(3, 3000, Color.WHITE, Color.BLACK, true));
                        });
                    }

                    textGui.setWidth((int) (84 + extraWidth));
                    name = "STARX CLIENT";

                    for (int i = 0; i < name.length(); i++) {
                        final String character = String.valueOf(name.charAt(i));

                        final float off1 = off;
                        psb20.drawString(character, x + 4 + off1, y + 3.5,
                                ColorUtils.INSTANCE.interpolateColorsBackAndForth(5, i * 10, Color.WHITE, Color.BLACK, true).getRGB());
                        off += psb20.getWidth(character);
                    }
                }
                break;
            }
        }
    }

    public static void initializeModules() {
        try {
            keystrokes = (Keystrokes) ModuleInstance.getModule(Keystrokes.class);
            bpsCounter = (BPSCounter) ModuleInstance.getModule(BPSCounter.class);
            arraylist = (Arraylist) ModuleInstance.getModule(Arraylist.class);
            textGui = (TextGui) ModuleInstance.getModule(TextGui.class);
        } catch (NullPointerException e) {
            StarXLogger.fatal("Error while initialize modules for HUD.", e);
        }
    }

    public static void renderGameOverlay() {
        if (StarX.isDestructed || !ModuleInstance.getModule(HUD.class).isEnabled()) return;
        if (!ModuleInstance.getBool("HUD", "Display when debugging").isEnabled() && mc.gameSettings.showDebugInfo) return;
        renderKeyStrokes();
        renderBPS();
        renderClientName();
        renderArrayList();
    }
}
