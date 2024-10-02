package cn.stars.starx.music.ui.gui;

import cn.stars.starx.GameInstance;
import cn.stars.starx.music.ui.MusicPlayerScreen;
import cn.stars.starx.util.animation.advanced.composed.CustomAnimation;
import cn.stars.starx.util.animation.advanced.impl.SmoothStepAnimation;
import lombok.Getter;
import lombok.Setter;
import org.lwjgl.input.Mouse;

/**
 * @author ChengFeng
 * @since 2024/8/13
 **/
@Getter
@Setter
public abstract class MusicPlayerGUI implements GameInstance {
    protected float posX, posY, width, height;
    protected boolean hovering, isBottom;
    public MusicPlayerGUI parent;

    public MusicPlayerGUI(MusicPlayerGUI parent) {
        this.parent = parent;
    }

    protected CustomAnimation scrollAnim = new CustomAnimation(SmoothStepAnimation.class, 100);
    public void handleScroll() {
        // Scroll
        int wheel = Mouse.getDWheel() * 400;
        if (wheel != 0) {
            scrollAnim.setStartPoint(scrollAnim.getOutput());
            if (wheel > 0) {
                scrollAnim.setEndPoint(scrollAnim.getEndPoint() + 20f);
            } else {
                scrollAnim.setEndPoint(scrollAnim.getEndPoint() - 20f);
            }
            if (scrollAnim.getEndPoint() > 0) scrollAnim.setEndPoint(0f);
            float maxScroll = height - (MusicPlayerScreen.height - MusicPlayerScreen.topWidth - MusicPlayerScreen.bottomWidth - 3f);
            if (-scrollAnim.getEndPoint() > maxScroll) {
                scrollAnim.setEndPoint(-maxScroll);
                isBottom = true;
            } else isBottom = false;
            scrollAnim.getAnimation().reset();
        }
    }

    public abstract boolean draw(float x, float y, int mouseX, int mouseY, float cx, float cy, float scale);

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {

    }

    public void freeMemory() {

    }
}
