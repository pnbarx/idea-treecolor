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
import com.intellij.ui.ColorUtil;
import com.intellij.ui.JBColor;
import dev.pnbarx.idea.treecolor.utils.UIUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.plaf.ButtonUI;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Path2D;
import java.awt.geom.RoundRectangle2D;


public abstract class  ColoredButton extends JButton {

    private static final Logger LOG = Logger.getInstance(ColoredButton.class);

    private static final int hPadding = 15;
    private static final int vPadding = 12;

    private Color backgroundColor;
    private Color foregroundColor;

    public ColoredButton(String text, @Nullable Color backgroundColor) {
        super(text);
        setBackground(backgroundColor);
        setUI(createUI());
        setOpaque(false);
        setRolloverEnabled(true);
        setBorder(BorderFactory.createEmptyBorder(vPadding, hPadding, vPadding, hPadding));
        addActionListener(this::onClick);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    onRightClick(e);
                }
                super.mouseClicked(e);
            }
        });
    }

    protected void onClick(ActionEvent e) {
    }

    protected void onRightClick(MouseEvent e) {
    }

    @Override
    public Color getBackground() {
        return backgroundColor;
    }

    @Override
    public void setBackground(@Nullable Color color) {
        backgroundColor = color != null ? color : UIUtils.getDefaultTreeBackgroundColor();
        foregroundColor = ColorUtil.isDark(backgroundColor) ? JBColor.WHITE : JBColor.BLACK;
    }

    @Override
    public Color getForeground() {
        return foregroundColor;
    }

    protected ButtonUI createUI() {
        return new ColoredButtonUI();
    }


    protected static class ColoredButtonUI extends BasicButtonUI {

        private static final float fontSize = 12.0f;
        private static final float borderWidth = 0.5f;

        @Override
        protected void installDefaults(final AbstractButton button) {
            super.installDefaults(button);
            button.setFont(UIManager.getFont("Button.font").deriveFont(Font.BOLD, fontSize));
        }

        @Override
        public void paint(@NotNull final Graphics g, final JComponent c) {

            Graphics2D g2d = (Graphics2D) g.create();
            ColoredButton button = (ColoredButton) c;

            try {
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

                int width = button.getWidth();
                int height = button.getHeight();
                int arcSize = getArcSize();

                if (button.isOpaque()) {
                    g2d.setColor(button.getBackground());
                    g2d.fill(new Rectangle(button.getSize()));
                }

                Shape outerShape = new RoundRectangle2D.Float(0, 0, width, height, arcSize, arcSize);

                ButtonModel model = button.getModel();
                g2d.setColor(getBackgroundColor(button));
                g2d.fill(outerShape);

                if (model.isRollover()) {
                    g2d.setColor(getFocusedBorderColor(button));
                } else {
                    g2d.setColor(getBorderColor(button));
                }

                Path2D border = new Path2D.Float(Path2D.WIND_EVEN_ODD);
                border.append(outerShape, false);
                //noinspection SuspiciousNameCombination
                border.append(new RoundRectangle2D.Float(
                    borderWidth, borderWidth,
                    width - borderWidth * 2, height - borderWidth * 2,
                    arcSize - borderWidth, arcSize - borderWidth
                ), false);
                g2d.fill(border);

            } finally {
                g2d.dispose();
            }

            super.paint(g, button);
        }

        protected Color getBackgroundColor(@NotNull ColoredButton button) {
            return button.getBackground();
        }

        protected Color getBorderColor(@NotNull ColoredButton button) {
            return new JBColor(0x999999, 0x777777);
        }

        protected Color getFocusedBorderColor(@NotNull ColoredButton button) {
            return new JBColor(0x555555, 0xaaaaaa);
        }

        @SuppressWarnings("SameReturnValue")
        protected int getArcSize() {
            return 20;
        }
    }

}
