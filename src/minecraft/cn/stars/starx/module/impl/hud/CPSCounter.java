package cn.stars.starx.module.impl.hud;

import cn.stars.starx.event.impl.ClickEvent;
import cn.stars.starx.event.impl.Render2DEvent;
import cn.stars.starx.event.impl.Shader3DEvent;
import cn.stars.starx.event.impl.TickEvent;
import cn.stars.starx.font.FontManager;
import cn.stars.starx.font.MFont;
import cn.stars.starx.module.Category;
import cn.stars.starx.module.Module;
import cn.stars.starx.module.ModuleInfo;
import cn.stars.starx.setting.impl.BoolValue;
import cn.stars.starx.setting.impl.ModeValue;
import cn.stars.starx.util.misc.ModuleInstance;
import cn.stars.starx.util.render.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@ModuleInfo(name = "CPSCounter", chineseName = "CPS显示", description = "Show your CPS on screen",
        chineseDescription = "显示你的点击速度", category = Category.HUD)
public class CPSCounter extends Module {
    private final ModeValue mode = new ModeValue("Mode", this, "Simple", "Simple", "Modern", "ThunderHack");
    private final BoolValue displayOnClick = new BoolValue("Display On Click", this, false);
    private final BoolValue rainbow = new BoolValue("Rainbow", this, false);
    private final BoolValue outline = new BoolValue("Background", this, true);
    public CPSCounter() {
        setCanBeEdited(true);
        setWidth(100);
        setHeight(20);
        this.Lclicks = new ArrayList<>();
        this.Rclicks = new ArrayList<>();
    }
    private final List<Long> Lclicks;
    private final List<Long> Rclicks;
    MFont psm = FontManager.getPSM(18);
    MFont icon = FontManager.getMi(24);

    @Override
    public void onShader3D(Shader3DEvent event) {
        if (displayOnClick.isEnabled() && (Lclicks.isEmpty() && Rclicks.isEmpty())) return;
        String cpsString = Lclicks.size() + " CPS | " + Rclicks.size() + " CPS";

        Color color = rainbow.isEnabled() ? ThemeUtil.getThemeColor(ThemeType.LOGO) : new Color(250, 250, 250, 200);

        if (mode.getMode().equals("Modern")) {
            if (event.isBloom()) RoundedUtil.drawRound(getX() + 2, getY() - 1, 19 + psm.getWidth(cpsString), psm.getHeight() + 3, 4, ColorUtil.withAlpha(color, 255));
            else RoundedUtil.drawRound(getX() + 2, getY() - 1, 19 + psm.getWidth(cpsString), psm.getHeight() + 3, 4, Color.BLACK);
        } else if (mode.getMode().equals("ThunderHack")) {
            RoundedUtil.drawGradientRound(getX() + 0.5f, getY() - 2.5f, 22 + psm.getWidth(cpsString), psm.getHeight() + 6, 4,
                    ColorUtils.INSTANCE.interpolateColorsBackAndForth(3, 1000, Color.WHITE, Color.BLACK, true),
                    ColorUtils.INSTANCE.interpolateColorsBackAndForth(3, 2000, Color.WHITE, Color.BLACK, true),
                    ColorUtils.INSTANCE.interpolateColorsBackAndForth(3, 4000, Color.WHITE, Color.BLACK, true),
                    ColorUtils.INSTANCE.interpolateColorsBackAndForth(3, 3000, Color.WHITE, Color.BLACK, true));
        } else if (mode.getMode().equals("Simple")) {
            RenderUtil.rect(getX() + 2, getY() - 1, 19 + psm.getWidth(cpsString), psm.getHeight() + 3, Color.BLACK);
        }
    }

    @Override
    public void onRender2D(Render2DEvent event) {
        if (displayOnClick.isEnabled() && (Lclicks.isEmpty() && Rclicks.isEmpty())) return;
        String cpsString = Lclicks.size() + " CPS | " + Rclicks.size() + " CPS";
        Color color = rainbow.isEnabled() ? ThemeUtil.getThemeColor(ThemeType.LOGO) : new Color(250, 250, 250, 200);


        if (outline.isEnabled()) {
            if (mode.getMode().equals("Modern")) {
                RoundedUtil.drawRound(getX() + 2, getY() - 1, 19 + psm.getWidth(cpsString), psm.getHeight() + 3, 4, new Color(0, 0, 0, 80));
                RenderUtil.roundedOutlineRectangle(getX() + 1, getY() - 2, 21 + psm.getWidth(cpsString), psm.getHeight() + 5, 3, 1, color);
            } else if (mode.getMode().equals("ThunderHack")) {
                RoundedUtil.drawGradientRound(getX() + 0.5f, getY() - 2.5f, 22 + psm.getWidth(cpsString), psm.getHeight() + 6, 4,
                        ColorUtils.INSTANCE.interpolateColorsBackAndForth(3, 1000, Color.WHITE, Color.BLACK, true),
                        ColorUtils.INSTANCE.interpolateColorsBackAndForth(3, 2000, Color.WHITE, Color.BLACK, true),
                        ColorUtils.INSTANCE.interpolateColorsBackAndForth(3, 4000, Color.WHITE, Color.BLACK, true),
                        ColorUtils.INSTANCE.interpolateColorsBackAndForth(3, 3000, Color.WHITE, Color.BLACK, true));
                RoundedUtil.drawRound(getX() + 1, getY() - 2, 21 + psm.getWidth(cpsString), psm.getHeight() + 5, 4, new Color(0, 0, 0, 220));
            } else if (mode.getMode().equals("Simple")) {
                RenderUtil.rect(getX() + 2, getY() - 1, 19 + psm.getWidth(cpsString), psm.getHeight() + 3, new Color(0, 0, 0, 80));
            }
        }
        icon.drawString("P", getX() + 4, getY() + 2, new Color(250, 250, 250, 200).getRGB());
        psm.drawString(cpsString, getX() + 17, getY() + 2.5f, new Color(250, 250, 250, 200).getRGB());
    }

    @Override
    public void onTick(TickEvent event) {
        this.Lclicks.removeIf(l -> l < System.currentTimeMillis() - 1000L);
        this.Rclicks.removeIf(t -> t < System.currentTimeMillis() - 1000L);
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getType() == ClickEvent.ClickType.LEFT) {
            this.Lclicks.add(System.currentTimeMillis());
        }
        if (event.getType() == ClickEvent.ClickType.RIGHT) {
            this.Rclicks.add(System.currentTimeMillis());
        }
    }
}
