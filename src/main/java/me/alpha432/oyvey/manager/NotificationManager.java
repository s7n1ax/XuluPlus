/*
 * Decompiled with CFR 0.150.
 */
package me.alpha432.oyvey.manager;

import java.util.ArrayList;
import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.features.Notifications.Notifications;
import me.alpha432.oyvey.features.modules.client.HUD;

public class NotificationManager {
    private final ArrayList<Notifications> notifications = new ArrayList();

    public void handleNotifications(int posY) {
        for (int i = 0; i < this.getNotifications().size(); ++i) {
            this.getNotifications().get(i).onDraw(posY);
            posY -= OyVey.moduleManager.getModuleByClass(HUD.class).renderer.getFontHeight() + 5;
        }
    }

    public void addNotification(String text, long duration) {
        this.getNotifications().add(new Notifications(text, duration));
    }

    public ArrayList<Notifications> getNotifications() {
        return this.notifications;
    }
}

