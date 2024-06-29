package cn.stars.starx.module.impl.hud;

import cn.stars.starx.event.impl.Render2DEvent;
import cn.stars.starx.module.Category;
import cn.stars.starx.module.Module;
import cn.stars.starx.module.ModuleInfo;
import cn.stars.starx.setting.impl.ModeValue;
import cn.stars.starx.setting.impl.NoteValue;
import cn.stars.starx.setting.impl.NumberValue;
import cn.stars.starx.util.misc.ModuleInstance;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.util.ResourceLocation;

import static org.lwjgl.opengl.GL11.*;

@ModuleInfo(name = "BASticker", description = "ARIS doesn't have a b",
        chineseDescription = "哎,等等,爱丽丝怎么没有b~", category = Category.HUD)
public class BASticker extends Module {
    private final ModeValue character = new ModeValue("Character", this, "Aris",
            "Aris", "Shiroko", "Azusa", "Hina Swimsuit", "Ui", "Hoshino Swimsuit", "Mika", "Ibuki");
    private final NumberValue scale = new NumberValue("Scale", this, 1, 0.1, 2, 0.1);
    private final NoteValue note = new NoteValue("Add scale to make pictures look better,", this);
    private final NoteValue note2 = new NoteValue("which may HIDE the screen.", this);
    ScaledResolution sr;

    public BASticker() {
        setCanBeEdited(true);
        setWidth(100);
        setHeight(100);
        setX(100);
        setY(100);
    }

    @Override
    public void onUpdateAlways() {
        setSuffix(character.getMode());
    }

    @Override
    public void onRender2D(Render2DEvent event) {
        if (!ModuleInstance.getBool("HUD", "Display when debugging").isEnabled() && mc.gameSettings.showDebugInfo) return;
        sr = new ScaledResolution(mc);
        int x = getX() + 5;
        int y = getY() + 5;
        switch (character.getMode()) {
            case "Aris": {
                setRoundedWidth(160, scale.getValue());
                setRoundedHeight(170, scale.getValue());
                drawImage(new ResourceLocation("starx/images/box_weapon/aris.png"), x, y, 150, 160, scale.getValue());
                break;
            }
            case "Shiroko": {
                setRoundedWidth(95, scale.getValue());
                setRoundedHeight(165, scale.getValue());
                drawImage(new ResourceLocation("starx/images/box_weapon/shiroko.png"), x, y, 85, 160, scale.getValue());
                break;
            }
            case "Azusa": {
                setRoundedWidth(130, scale.getValue());
                setRoundedHeight(170, scale.getValue());
                drawImage(new ResourceLocation("starx/images/box_weapon/azusa.png"), x, y, 120, 160, scale.getValue());
                break;
            }
            case "Hina Swimsuit": {
                setRoundedWidth(110, scale.getValue());
                setRoundedHeight(170, scale.getValue());
                drawImage(new ResourceLocation("starx/images/box_weapon/hina_swimsuit.png"), x, y, 100, 160, scale.getValue());
                break;
            }
            case "Ui": {
                setRoundedWidth(110, scale.getValue());
                setRoundedHeight(170, scale.getValue());
                drawImage(new ResourceLocation("starx/images/box_weapon/ui.png"), x, y, 100, 160, scale.getValue());
                break;
            }
            case "Hoshino Swimsuit": {
                setRoundedWidth(135, scale.getValue());
                setRoundedHeight(170, scale.getValue());
                drawImage(new ResourceLocation("starx/images/box_weapon/hoshino_swimsuit.png"), x, y, 125, 160, scale.getValue());
                break;
            }
            case "Mika": {
                setRoundedWidth(110, scale.getValue());
                setRoundedHeight(170, scale.getValue());
                drawImage(new ResourceLocation("starx/images/box_weapon/mika.png"), x, y, 95, 160, scale.getValue());
                break;
            }
            case "Ibuki": {
                setRoundedWidth(110, scale.getValue());
                setRoundedHeight(180, scale.getValue());
                drawImage(new ResourceLocation("starx/images/box_weapon/ibuki.png"), x, y, 100, 170, scale.getValue());
                break;
            }
        }
    }

    public void setRoundedWidth(int width, double scale) {
        setWidth(Math.round(width * (float)scale));
    }

    public void setRoundedHeight(int height, double scale) {
        setHeight(Math.round(height * (float)scale));
    }


    public static void drawImage(ResourceLocation image, int x, int y, int width, int height, double scale) {
        glDisable(GL_DEPTH_TEST);
        glEnable(GL_BLEND);
        glDepthMask(false);
        OpenGlHelper.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA, GL_ONE, GL_ZERO);
        glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.getTextureManager().bindTexture(image);
        Gui.drawModalRectWithCustomSizedTexture(x, y, 0, 0,
                (int) Math.round(width * scale), (int) Math.round(height * scale), Math.round(width * scale), Math.round(height * scale));
        glDepthMask(true);
        glDisable(GL_BLEND);
        glEnable(GL_DEPTH_TEST);
    }
}
