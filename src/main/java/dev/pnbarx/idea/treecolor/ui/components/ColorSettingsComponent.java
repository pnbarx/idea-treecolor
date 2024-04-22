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
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.ui.ColorChooserService;
import com.intellij.ui.ColorPickerListener;
import dev.pnbarx.idea.treecolor.services.ProjectStateService;
import dev.pnbarx.idea.treecolor.state.beans.ColorSettings;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.util.Collections;
import java.util.List;

public class ColorSettingsComponent extends JPanel {

    private static final Logger LOG = Logger.getInstance(ColorSettingsComponent.class);

    private final ProjectStateService project;
    private final int colorId;
    private ColorSettings colorSettings;

    private final ColorChooserButton colorChooserButton;
    private final JCheckBox enabledCheckbox;

    public ColorSettingsComponent(ProjectStateService project, int colorId) {
        super(new GridLayout(2, 1));

        this.project = project;
        this.colorId = colorId;
        this.colorSettings = this.project.colors.getColorSettingsById(colorId);

        colorChooserButton = new ColorChooserButton(colorSettings.getName(), colorSettings.getColor());
        colorChooserButton.setMinimumSize(new Dimension(110, 40));
        colorChooserButton.setPreferredSize(new Dimension(160, 40));

        enabledCheckbox = new JCheckBox("enabled", colorSettings.isEnabled());
        enabledCheckbox.setHorizontalAlignment(JCheckBox.CENTER);
        enabledCheckbox.setVerticalAlignment(JCheckBox.TOP);
        enabledCheckbox.setFont(UIManager.getFont("Button.font").deriveFont(Font.PLAIN, 10));
        enabledCheckbox.setFocusable(false);
        enabledCheckbox.addActionListener(this::enabledCheckboxHandle);

        add(colorChooserButton);
        add(enabledCheckbox);
    }

    public void applyColorSettings() {
        colorChooserButton.setText(colorSettings.getName());
        colorChooserButton.setBackground(colorSettings.getColor());
        updateUI();
        LOG.debug("SET BUTTON COLOR TO " + colorSettings.getColor());
        enabledCheckbox.setSelected(colorSettings.isEnabled());
        project.colors.setColorSettingsById(colorId, colorSettings);
    }

    public void reloadColorSettings() {
        colorSettings = project.colors.getColorSettingsById(colorId);
    }

    private void enabledCheckboxHandle(ActionEvent actionEvent) {
        colorSettings.setEnabled(enabledCheckbox.isSelected());
        applyColorSettings();
    }

    private class ColorChooserButton extends ColoredButton {

        protected ColorChooserButton(String text, @Nullable Color color) {
            super(text, color);
        }

        @Override
        protected void onClick(ActionEvent e) {
            List<ColorPickerListener> listeners = Collections.singletonList(new ColorChangeListener());
            Color currentColor = colorSettings.getColor();
            Color newColor = ColorChooserService.getInstance().showDialog(project.getProject(), this, "Choose Color",
                    currentColor, true, listeners, true);
            if (newColor != null) {
                colorSettings.setColor(newColor);
                colorSettings.setEnabled(true);
            } else {
                colorSettings.setColor(currentColor);
            }
            ColorSettingsComponent.this.applyColorSettings();
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
            colorNameInput = new JTextField(colorSettings.getName());
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
            colorSettings.setName(colorNameInput.getText().trim());
            ColorSettingsComponent.this.applyColorSettings();
            super.doOKAction();
        }
    }


    private class ColorChangeListener implements ColorPickerListener {

        @Override
        public void colorChanged(Color color) {
            colorSettings.setColor(color);
            project.colors.setColorSettingsById(colorId, colorSettings);
        }

        @Override
        public void closed(@Nullable Color color) {
        }
    }

}
