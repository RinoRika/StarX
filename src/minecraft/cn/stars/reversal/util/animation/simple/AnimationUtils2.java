package cn.stars.reversal.util.animation.simple;

import cn.stars.reversal.util.math.MathUtil;
import net.minecraft.util.MathHelper;

public class AnimationUtils2 {
    long mc;
    public float anim;
    public float to;
    public float speed;

    public AnimationUtils2(float anim, float to, float speed) {
        this.anim = anim;
        this.to = to;
        this.speed = speed;
        this.mc = System.currentTimeMillis();
    }

    public float getAnim() {
        int count = (int) ((System.currentTimeMillis() - this.mc) / 5L);
        if (count > 0) {
            this.mc = System.currentTimeMillis();
        }
        for (int i = 0; i < count; ++i) {
            this.anim = MathUtil.lerp(this.anim, this.to, this.speed);
        }
        return this.anim;
    }

    public float getAngleAnim() {
        int count = (int) ((System.currentTimeMillis() - this.mc) / 5L);
        if (count > 0) {
            this.mc = System.currentTimeMillis();
        }
        for (int i = 0; i < count; ++i) {
            this.anim = (float) this.lerpAngle(this.anim, this.to, this.speed);
        }
        return MathHelper.wrapAngleTo180_float(this.anim);
    }

    public void setAnim(float anim) {
        this.anim = anim;
        this.mc = System.currentTimeMillis();
    }

    double lerpAngle(float start, float end, float amount) {
        float minAngle = (end - start + 180.0f) % 360.0f - 180.0f;
        return minAngle * amount + start;
    }
}