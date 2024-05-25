package cn.stars.starx.ui.gui;

import cn.stars.starx.GameInstance;
import cn.stars.starx.StarX;
import cn.stars.starx.font.CustomFont;
import cn.stars.starx.font.TTFFontRenderer;
import cn.stars.starx.ui.gui.mainmenu.MenuButton;
import cn.stars.starx.ui.gui.mainmenu.MenuTextButton;
import cn.stars.starx.util.StarXLogger;
import cn.stars.starx.util.animation.rise.Animation;
import cn.stars.starx.util.animation.rise.Easing;
import cn.stars.starx.util.math.TimeUtil;
import cn.stars.starx.util.render.ColorUtil;
import cn.stars.starx.util.render.RenderUtil;
import cn.stars.starx.util.shader.RiseShaders;
import cn.stars.starx.util.shader.base.ShaderRenderType;
import cn.stars.starx.util.shader.base.ShaderToy;
import cn.stars.starx.util.shader.impl.BackgroundShader;
import de.florianmichael.viamcp.gui.GuiProtocolSelector;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import java.awt.*;
import java.io.IOException;
import java.net.URI;

public class GuiMainMenuNew extends GuiScreen implements GameInstance {
    ScaledResolution sr;
    Minecraft mc = Minecraft.getMinecraft();
    TTFFontRenderer gs = CustomFont.FONT_MANAGER.getFont("PSM 20");
    private Animation animation = new Animation(Easing.EASE_OUT_QUINT, 600);
    private Animation textHoverAnimation = new Animation(Easing.EASE_OUT_SINE, 250);
    private Animation textHoverAnimation2 = new Animation(Easing.EASE_OUT_SINE, 250);

    private MenuTextButton singlePlayerButton, multiPlayerButton, settingsButton, viaVersionButton, exitButton, cbButton, authorButton, msLoginButton;
    private MenuButton[] menuButtons;

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        if (this.singlePlayerButton == null || this.multiPlayerButton == null) {
            return;
        }
        sr = new ScaledResolution(mc);
        // 背景
        try {
            drawMenuBackground(partialTicks, mouseX, mouseY);
        } catch (IOException e) {
            StarXLogger.error("(GuiMainMenu) Error while loading background", e);
        }

    //    NORMAL_BLUR_RUNNABLES.add(() -> RenderUtil.rectangle(0, 0, sr.getScaledWidth(), sr.getScaledHeight(), Color.BLACK));

        // blur
        RiseShaders.GAUSSIAN_BLUR_SHADER.update();
        RiseShaders.GAUSSIAN_BLUR_SHADER.run(ShaderRenderType.OVERLAY, partialTicks, NORMAL_BLUR_RUNNABLES);

        // bloom
        RiseShaders.POST_BLOOM_SHADER.update();
        RiseShaders.POST_BLOOM_SHADER.run(ShaderRenderType.OVERLAY, partialTicks, NORMAL_POST_BLOOM_RUNNABLES);

        GameInstance.clearRunnables();

        // 按钮
        this.singlePlayerButton.draw(mouseX, mouseY, partialTicks);
        this.multiPlayerButton.draw(mouseX, mouseY, partialTicks);
        this.settingsButton.draw(mouseX, mouseY, partialTicks);
        this.viaVersionButton.draw(mouseX, mouseY, partialTicks);
        this.exitButton.draw(mouseX, mouseY, partialTicks);
        this.cbButton.draw(mouseX, mouseY, partialTicks);
        this.authorButton.draw(mouseX, mouseY, partialTicks);
        this.msLoginButton.draw(mouseX, mouseY, partialTicks);

        // Logo
        final double destination = height / 2f - 150;
        animation.run(destination);
        RenderUtil.image(new ResourceLocation("starx/images/starx.png"), width / 2f - 110, (float) animation.getValue() - 50, 280, 190);

        // 字
        String s1 = "Aerolite Team © 2024, All rights reserved.";
        // 动画
        textHoverAnimation.run(RenderUtil.isHovered(0, height - 37, 140, 37, mouseX, mouseY) ? 255 : 155);
        textHoverAnimation2.run(RenderUtil.isHovered(width - gs.getWidth(s1), height - 13, gs.getWidth(s1), 13, mouseX, mouseY) ? 255 : 155);

        Color stringColor = new Color(50, 150, 250, 155);
        gs.drawString(s1, width - gs.getWidth(s1), height - 12, ColorUtil.withAlpha(stringColor, (int) textHoverAnimation2.getValue()).getRGB());
        gs.drawString("Minecraft 1.8.8 (Vanilla/StarX)", 2, height - 36, ColorUtil.withAlpha(stringColor, (int) textHoverAnimation.getValue()).getRGB());
        gs.drawString("OptiFine_1.8.8_HD_U_I7", 2, height - 24, ColorUtil.withAlpha(stringColor, (int) textHoverAnimation.getValue()).getRGB());
        gs.drawString("Current Background ID: " + StarX.INSTANCE.backgroundId, 2, height - 12, ColorUtil.withAlpha(stringColor, (int) textHoverAnimation.getValue()).getRGB());

        UI_BLOOM_RUNNABLES.forEach(Runnable::run);
        UI_BLOOM_RUNNABLES.clear();

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if (this.menuButtons == null) return;

        // 执行runnable
        if (mouseButton == 0) {
            for (MenuButton menuButton : this.menuButtons) {
                if (RenderUtil.isHovered(menuButton.getX(), menuButton.getY(), menuButton.getWidth(), menuButton.getHeight(), mouseX, mouseY)) {
                    mc.getSoundHandler().playButtonPress();
                    menuButton.runAction();
                    break;
                }
            }
        }
        if (mouseButton == 1) {
            for (MenuButton menuButton : this.menuButtons) {
                if (menuButton == cbButton && RenderUtil.isHovered(menuButton.getX(), menuButton.getY(), menuButton.getWidth(), menuButton.getHeight(), mouseX, mouseY)) {
                    mc.getSoundHandler().playButtonPress();
                    changeMenuBackground(true);
                    break;
                }
            }
        }
    }

    @Override
    public void initGui() {
        GameInstance.clearRunnables();
        int centerX = this.width / 2;
        int centerY = this.height / 2;
        this.animation = new Animation(Easing.EASE_OUT_QUINT, 600);
        this.textHoverAnimation = new Animation(Easing.EASE_OUT_SINE, 250);
        this.textHoverAnimation2 = new Animation(Easing.EASE_OUT_SINE, 250);
        RiseShaders.MAIN_MENU_SHADER.setActive(true);

        // 重新定义按钮
        this.singlePlayerButton = new MenuTextButton(centerX - 200, centerY - 25, 75, 75, () -> mc.displayGuiScreen(new GuiSelectWorld(this)), "Single", "K");
        this.multiPlayerButton = new MenuTextButton(centerX - 120, centerY - 25, 75, 75, () -> mc.displayGuiScreen(new GuiMultiplayer(this)), "Multi", "L");
        this.settingsButton = new MenuTextButton(centerX - 40, centerY - 25, 75, 75, () -> mc.displayGuiScreen(new GuiOptions(this, mc.gameSettings)), "Settings", "N");
        this.viaVersionButton = new MenuTextButton(centerX + 40, centerY - 25, 75, 75, () -> mc.displayGuiScreen(new GuiProtocolSelector(this)), "Protocol", "x");
        this.exitButton = new MenuTextButton(centerX + 120, centerY - 25, 75, 75, () -> mc.shutdown(), "Exit", "O");
        this.cbButton = new MenuTextButton(4, 4, 160, 25, () -> changeMenuBackground(false), "Change Background", "q", true, 10, 5);
        this.authorButton = new MenuTextButton(4, 64, 80, 25, this::openBiliBili, "Author", "e", true, 6, 5);
        this.msLoginButton = new MenuTextButton(4, 34, 125, 25, () -> mc.displayGuiScreen(new GuiMicrosoftLoginPending(this)), "Microsoft Login", "M", true, 6, 5);

        // 简化MouseClicked方法
        this.menuButtons = new MenuButton[]{this.singlePlayerButton, this.multiPlayerButton, this.settingsButton, this.viaVersionButton, this.exitButton
                ,this.cbButton, this.authorButton, this.msLoginButton};
        super.initGui();
    }

    @Override
    public void onGuiClosed() {
        BackgroundShader.BACKGROUND_SHADER.stopShader();
        RiseShaders.MAIN_MENU_SHADER.setActive(false);
        super.onGuiClosed();
    }

    public void changeMenuBackground(boolean previous) {
        RenderUtil.initTime = System.currentTimeMillis();
        if (!previous) {
            if (StarX.INSTANCE.backgroundId < 7) StarX.INSTANCE.backgroundId++;
            else StarX.INSTANCE.backgroundId = 0;
        } else {
            if (StarX.INSTANCE.backgroundId > 0) StarX.INSTANCE.backgroundId--;
            else StarX.INSTANCE.backgroundId = 7;
        }
        BackgroundShader.BACKGROUND_SHADER.stopShader();
        RiseShaders.MAIN_MENU_SHADER.setActive(false);
        StarXLogger.info("(GuiMainMenuNew) Current background id: " + StarX.INSTANCE.backgroundId);
    }


    public void openBiliBili() {
        final Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
        if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
            try {
                URI uri = new URI("https://space.bilibili.com/670866766");
                desktop.browse(uri);
                StarXLogger.info("(GuiMainMenuNew) Opened web page: " + uri);
            } catch (final Exception e) {
                StarXLogger.error("(GuiMainMenuNew) Error while opening web page.", e);
            }
        }
    }
}
