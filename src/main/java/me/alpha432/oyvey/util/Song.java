/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraft.client.audio.ISound
 *  net.minecraft.client.audio.ISound$AttenuationType
 *  net.minecraft.client.audio.Sound
 *  net.minecraft.client.audio.Sound$Type
 *  net.minecraft.client.audio.SoundEventAccessor
 *  net.minecraft.client.audio.SoundHandler
 *  net.minecraft.util.ResourceLocation
 *  net.minecraft.util.SoundCategory
 */
package me.alpha432.oyvey.util;

import javax.annotation.Nullable;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.Sound;
import net.minecraft.client.audio.SoundEventAccessor;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;

public class Song {
    public static final ISound sound;
    private static final ResourceLocation song;

    static {
        song = new ResourceLocation("textures/song.ogg");
        sound = new ISound(){
            private final int pitch = 1;
            private final int volume = 1;

            public ResourceLocation func_147650_b() {
                return song;
            }

            @Nullable
            public SoundEventAccessor func_184366_a(SoundHandler soundHandler) {
                return new SoundEventAccessor(song, "awesome");
            }

            public Sound func_184364_b() {
                return new Sound("song", 1.0f, 1.0f, 1, Sound.Type.SOUND_EVENT, false);
            }

            public SoundCategory func_184365_d() {
                return SoundCategory.VOICE;
            }

            public boolean func_147657_c() {
                return true;
            }

            public int func_147652_d() {
                return 2;
            }

            public float func_147653_e() {
                return 1.0f;
            }

            public float func_147655_f() {
                return 1.0f;
            }

            public float func_147649_g() {
                return 1.0f;
            }

            public float func_147654_h() {
                return 0.0f;
            }

            public float func_147651_i() {
                return 0.0f;
            }

            public ISound.AttenuationType func_147656_j() {
                return ISound.AttenuationType.LINEAR;
            }
        };
    }
}

