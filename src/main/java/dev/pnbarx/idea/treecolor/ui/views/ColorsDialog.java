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

package dev.pnbarx.idea.treecolor.ui.views;

import com.intellij.openapi.ui.ComboBox;
import dev.pnbarx.idea.treecolor.state.ProjectColors;
import dev.pnbarx.idea.treecolor.services.ProjectStateService;
import dev.pnbarx.idea.treecolor.state.beans.MarkType;
import dev.pnbarx.idea.treecolor.ui.components.ColorSettingsComponent;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import dev.pnbarx.idea.treecolor.utils.UIUtils;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;


public class ColorsDialog extends DialogWrapper {

    private static final Logger LOG = Logger.getInstance(ColorsDialog.class);

    public static final int DISPOSE_EXIT_CODE = 0xD1;

    private final Project project;
    private final ProjectStateService projectStateService;
    private final Element stateSnapshot;

    private final List<ColorSettingsComponent> colorSettingsComponents = new ArrayList<>();
    private final ComboBox<MarkType> markCollapsedComboBox = new ComboBox<>(
        new MarkType[]{MarkType.DOTS, MarkType.CIRCLES, MarkType.NONE}
    );

    public ColorsDialog(@NotNull Project project) {
        super(project, true);

        this.project = project;
        this.projectStateService = ProjectStateService.getInstance(project);

        assert projectStateService != null;
        stateSnapshot = projectStateService.getStateSnapshot();

        //noinspection DialogTitleCapitalization
        setTitle("Define Colors (right-click to set the color name)");
        init();
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {

        JPanel dialogPanel = new JPanel();
        dialogPanel.setLayout(new BoxLayout(dialogPanel, BoxLayout.PAGE_AXIS));
        dialogPanel.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0));

        JPanel colorsPanel = new JPanel(new GridLayout(0, 4, 10, 0));
        colorSettingsComponents.clear();
        for (int i = 0; i < ProjectColors.NUMBER_OF_COLOR_PRESETS; i++) {

            ColorSettingsComponent component = new ColorSettingsComponent(projectStateService, i + 1);
            colorsPanel.add(component);
            colorSettingsComponents.add(component);
        }

        JPanel markCollapsedPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 10));
        JLabel markCollapsedLabel = new JLabel("Marks for collapsed highlights: ");
        markCollapsedComboBox.setSelectedItem(projectStateService.getMarkType());
        markCollapsedComboBox.addActionListener(actionEvent -> {
            Object selectedItem = markCollapsedComboBox.getSelectedItem();
            if (selectedItem != null) {
                projectStateService.setMarkType((MarkType) selectedItem);
                UIUtils.updateUI(project);
            }
        });

        markCollapsedPanel.add(markCollapsedLabel);
        markCollapsedPanel.add(markCollapsedComboBox);

        dialogPanel.add(colorsPanel);
        dialogPanel.add(new JSeparator());
//        dialogPanel.add(markCollapsedPanel);

        return dialogPanel;
    }

    @NotNull
    @Override
    protected Action @NotNull [] createLeftSideActions() {
        return new Action[]{
            new AbstractAction("Manage Color Settings...") {
                @Override
                public void actionPerformed(ActionEvent e) {
                    DialogWrapper manageSettingsDialog = new ManageSettingsDialog(project);
                    manageSettingsDialog.show();
                    update();

                    if (manageSettingsDialog.getExitCode() == DISPOSE_EXIT_CODE) {
                        dispose();
                    }
                }
            },
        };
    }

    public void update() {
        for (ColorSettingsComponent component : colorSettingsComponents) {
            component.reloadColorSettings();
            component.applyColorSettings();
        }
        markCollapsedComboBox.setSelectedItem(projectStateService.getMarkType());
    }

    @Override
    protected void doOKAction() {
        projectStateService.saveState();

        UIUtils.updateUI(projectStateService.getProject());
        super.doOKAction();
    }

    @Override
    public void doCancelAction() {
        projectStateService.restoreStateFromSnapshot(stateSnapshot);
        UIUtils.updateUI(projectStateService.getProject());
        super.doCancelAction();
    }


}
