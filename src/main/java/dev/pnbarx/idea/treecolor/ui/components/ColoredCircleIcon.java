/*
 * Copyright (c) 2018-2020 Pavel Barykin.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dev.pnbarx.idea.treecolor.ui.components;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.ui.JBColor;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.Map;
import java.util.HashMap;


public class ColoredCircleIcon implements Icon {

    private static final Logger LOG = Logger.getInstance(ColoredCircleIcon.class);

    private final int ICON_WIDTH = 16;
    private final int ICON_HEIGHT = 16;
    private final float ICON_STROKE_WIDTH = 0.3f;
    private final int MARK_WIDTH = 5;
    private final int MARK_HEIGHT = 5;
    private final float MARK_STROKE_WIDTH = 0.6f;

    public enum Mode {
        DEFAULT,
        HIGHLIGHTED,
        HIGHLIGHTED_RECURSIVELY
    }

    private final Color backgroundColor;
    private final Mode mode;

    private static final Map<String, ColoredCircleIcon> iconsCache = new HashMap<>();

    private ColoredCircleIcon(Color backgroundColor, Mode mode) {
        this.backgroundColor = backgroundColor;
        this.mode = mode;
    }

    public static ColoredCircleIcon getInstance(@NotNull Color backgroundColor) {
        return getInstance(backgroundColor, Mode.DEFAULT);
    }

    public static ColoredCircleIcon getInstance(@NotNull Color backgroundColor, @NotNull Mode mode) {
        String hashCode = "#" + backgroundColor.hashCode() + ":" + mode.hashCode();
        if (!iconsCache.containsKey(hashCode)) {
            iconsCache.put(hashCode, new ColoredCircleIcon(backgroundColor, mode));
        }
        return iconsCache.get(hashCode);
    }

    @Override
    public void paintIcon(Component c, @NotNull Graphics g, int x, int y) {
        Graphics2D g2d = (Graphics2D) g.create();

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

        Shape circle = new Ellipse2D.Double(
            x + 1,
            y + 1,
            ICON_WIDTH - 2,
            ICON_HEIGHT - 2
        );

        g2d.setColor(backgroundColor);
        g2d.fill(circle);

        Color strokeColor = JBColor.namedColor("Tree.foreground", new JBColor(0x555555, 0xbbbbbb));
        g2d.setStroke(new BasicStroke(ICON_STROKE_WIDTH));
        g2d.setColor(strokeColor);
        g2d.draw(circle);

        g2d.setStroke(new BasicStroke(MARK_STROKE_WIDTH));
        Shape innerCircle = new Ellipse2D.Double(
            x + (ICON_WIDTH - MARK_WIDTH) / 2f,
            y + (ICON_HEIGHT - MARK_HEIGHT) / 2f,
            MARK_WIDTH,
            MARK_HEIGHT
        );

        switch (mode) {
            case HIGHLIGHTED:
                g2d.fill(innerCircle);
            case HIGHLIGHTED_RECURSIVELY:
                g2d.draw(innerCircle);
        }

        g2d.dispose();
    }

    @Override
    public int getIconWidth() {
        return ICON_WIDTH;
    }

    @Override
    public int getIconHeight() {
        return ICON_HEIGHT;
    }

}
