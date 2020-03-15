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

package dev.pnbarx.idea.treecolor.ui;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.ui.ColorUtil;
import com.intellij.ui.JBColor;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.Map;
import java.util.HashMap;


public class ColoredCircleIcon implements Icon {

    private static final Logger LOG = Logger.getInstance(ColoredCircleIcon.class);

    private int width = 16;
    private int height = 16;

    private Color backgroundColor;
    private static final float strokeWidth = 0.3f;

    private static Map<String, ColoredCircleIcon> iconsCache = new HashMap<>();

    private ColoredCircleIcon(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public static ColoredCircleIcon getInstance(String backgroundColorHex) {
        Color backgroundColor = ColorUtil.fromHex(backgroundColorHex, Color.DARK_GRAY);
        return getInstance(backgroundColor);
    }

    public static ColoredCircleIcon getInstance(Color backgroundColor) {
        String backgroundColorHex = ColorUtil.toHex(backgroundColor);

        if (!iconsCache.containsKey(backgroundColorHex)) {
            iconsCache.put(backgroundColorHex, new ColoredCircleIcon(backgroundColor));
        }

        return iconsCache.get(backgroundColorHex);
    }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        Graphics2D g2d = (Graphics2D) g.create();

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

        Shape circle = new Ellipse2D.Double(x + 1, y + 1, width - 2, height - 2);

        g2d.setColor(backgroundColor);
        g2d.fill(circle);

        Color strokeColor = JBColor.namedColor("Tree.foreground", new JBColor(0x555555, 0xbbbbbb));
        g2d.setStroke(new BasicStroke(strokeWidth));
        g2d.setColor(strokeColor);
        g2d.draw(circle);

        g2d.dispose();
    }

    @Override
    public int getIconWidth() {
        return width;
    }

    @Override
    public int getIconHeight() {
        return height;
    }

}
