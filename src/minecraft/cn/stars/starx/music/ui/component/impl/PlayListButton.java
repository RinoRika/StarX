package cn.stars.starx.music.ui.component.impl;

import cn.stars.starx.StarX;
import cn.stars.starx.music.thread.FetchPlayListThread;
import cn.stars.starx.music.ui.ThemeColor;
import cn.stars.starx.music.ui.component.Button;
import cn.stars.starx.music.api.base.PlayList;
import cn.stars.starx.music.ui.gui.MusicPlayerGUI;
import cn.stars.starx.music.ui.gui.impl.PlayListGUI;
import cn.stars.starx.util.animation.advanced.Animation;
import cn.stars.starx.util.animation.advanced.Direction;
import cn.stars.starx.util.animation.advanced.impl.DecelerateAnimation;
import cn.stars.starx.util.render.RenderUtil;
import net.minecraft.client.renderer.texture.DynamicTexture;
import org.lwjgl.opengl.GL11;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;

/**
 * @author ChengFeng
 * @since 2024/8/13
 **/
public class PlayListButton extends Button {
    private final PlayList playList;
    public DynamicTexture coverTexture;
    private final PlayListGUI gui;

    private final Animation scaleAnim = new DecelerateAnimation(200, 0.1f, Direction.BACKWARDS);

    public PlayListButton(PlayList playList, MusicPlayerGUI parent) {
        this.playList = playList;
        gui = new PlayListGUI(playList, parent);
    }

    @Override
    public void draw() {
        height = 40f;
        gui.setWidth(width);
        if (coverTexture == null) {
            try {
                coverTexture = new DynamicTexture(ImageIO.read(playList.getCoverImage()));
            } catch (Exception e) {
            }
        }
        if (hovering && scaleAnim.getDirection() == Direction.BACKWARDS) {
            scaleAnim.changeDirection();
        } else if (!hovering && scaleAnim.getDirection() == Direction.FORWARDS) {
            scaleAnim.changeDirection();
        }
    //    RenderUtil.scaleStart(posX + 40, posY + 25, 1 + scaleAnim.getOutput().floatValue());
        if (coverTexture != null) RenderUtil.image(coverTexture, posX + 20, posY, 40, 40);
    //    RenderUtil.scaleEnd();
        regular16.drawString(playList.getName(), posX + 70, posY + 3, Color.WHITE.getRGB());
        regular16.drawString(regular16.trimStringToWidth(playList.getDescription(), 290f, false, true), posX + 70, posY + 18, ThemeColor.greyColor.getRGB());
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) {
        if (hovering && button == 0) {
            StarX.musicManager.screen.setCurrentGUI(gui);
            if (playList.getMusicList().isEmpty()) {
                new FetchPlayListThread(playList, gui.buttons).start();
            }
        }
    }
}
