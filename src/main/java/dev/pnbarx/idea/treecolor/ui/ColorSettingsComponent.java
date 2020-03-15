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

import dev.pnbarx.idea.treecolor.state.models.ColorSettings;
import dev.pnbarx.idea.treecolor.state.ColorsState;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.ui.*;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.util.Collections;
import java.util.List;

public class ColorSettingsComponent extends JPanel {

    private static final Logger LOG = Logger.getInstance(ColorSettingsComponent.class);

    private ColorsState colorsState;
    private int colorIndex;
    private String colorName;
    private String colorHex;
    private boolean isEnabled;

    private ColorChooserButton colorChooserButton;
    private JCheckBox enabledCheckbox;

    public ColorSettingsComponent(ColorsState colorsState, int colorIndex) {
        super(new GridLayout(2, 1));

        this.colorsState = colorsState;
        this.colorIndex = colorIndex;

        ColorSettings colorSettings = colorsState.getColorSettings(colorIndex);
        colorName = colorSettings.getName();
        colorHex = colorSettings.getColorHex();
        isEnabled = colorSettings.isEnabled();

        colorChooserButton = new ColorChooserButton(colorName, colorHex);
        colorChooserButton.setMinimumSize(new Dimension(110, 40));
        colorChooserButton.setPreferredSize(new Dimension(160, 40));

        enabledCheckbox = new JCheckBox("enabled", isEnabled);
        enabledCheckbox.setHorizontalAlignment(JCheckBox.CENTER);
        enabledCheckbox.setVerticalAlignment(JCheckBox.TOP);
        enabledCheckbox.setFont(UIManager.getFont("Button.font").deriveFont(Font.PLAIN, 10));
        enabledCheckbox.setFocusable(false);
        enabledCheckbox.addActionListener(this::enabledCheckboxHandle);

        add(colorChooserButton);
        add(enabledCheckbox);
    }

    void enabledCheckboxHandle(ActionEvent actionEvent) {
        isEnabled = enabledCheckbox.isSelected();
        update();
    }

    private void update() {
        colorChooserButton.setText(colorName);
        colorChooserButton.setBackground(colorHex);
        enabledCheckbox.setSelected(isEnabled);
        colorsState.setColorSettings(colorIndex, colorHex, isEnabled, colorName);
    }


    private class ColorChooserButton extends ColoredButtonComponent {

        protected ColorChooserButton(String text, String colorHex) {
            super(text, colorHex);
        }

        @Override
        protected void onClick(ActionEvent e) {

            List<ColorPickerListener> listeners = Collections.singletonList(new ColorChangeListener());
            Color currentColor = colorsState.getColorSettings(colorIndex).getColor();
            Color newColor = ColorChooser.chooseColor(this, "Choose Color", currentColor, false, listeners, false);

            if (newColor != null) {
                colorHex = "#" + ColorUtil.toHex(newColor);
                isEnabled = true;
            } else {
                colorHex = "#" + ColorUtil.toHex(currentColor);
            }
            ColorSettingsComponent.this.update();
        }

        @Override
        protected void onRightClick(MouseEvent e) {
            new ColorNameDialog(ColorSettingsComponent.this).show();
        }
    }


    private class ColorNameDialog extends DialogWrapper {

        private JTextField colorNameInput;

        public ColorNameDialog(Component parent) {
            super(parent, true);
            setTitle("Color Name");
            init();
        }

        @Nullable
        @Override
        protected JComponent createCenterPanel() {
            JPanel dialogPanel = new JPanel(new BorderLayout());
            colorNameInput = new JTextField(colorName);
            colorNameInput.setPreferredSize(new Dimension(370, 35));
            dialogPanel.add(colorNameInput);
            return dialogPanel;
        }

        @Override
        public JComponent getPreferredFocusedComponent() {
            return colorNameInput;
        }

        @Override
        protected void doOKAction() {
            colorName = colorNameInput.getText().trim();
            ColorSettingsComponent.this.update();
            super.doOKAction();
        }
    }


    private class ColorChangeListener implements ColorPickerListener {

        @Override
        public void colorChanged(Color color) {
            colorsState.setColorSettings(colorIndex, color);
        }

        @Override
        public void closed(@Nullable Color color) {
        }
    }

}
