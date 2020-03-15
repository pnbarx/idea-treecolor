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

import dev.pnbarx.idea.treecolor.ui.ColorSettingsComponent;
import dev.pnbarx.idea.treecolor.state.ProjectState;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;


public class ColorsDialog extends DialogWrapper {

    private static final Logger LOG = Logger.getInstance(ColorsDialog.class);

    private ProjectState projectState;

    public ColorsDialog(@NotNull Project project) {
        super(project, true);

        projectState = ProjectState.getInstance(project);
        assert projectState != null;
        projectState.colors.useShadow();

        //noinspection DialogTitleCapitalization
        setTitle("Define Colors (right-click to set color name)");
        init();
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {

        JPanel colorsPanel = new JPanel(new GridLayout(0, 4, 10, 0));
        colorsPanel.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0));
        for (int i = 0; i < 16; i++) {
            ColorSettingsComponent component = new ColorSettingsComponent(projectState.colors, i);
            colorsPanel.add(component);
        }

        return colorsPanel;
    }

    @Override
    protected void doOKAction() {
        projectState.colors.applyShadow();
        projectState.updateUI();
        super.doOKAction();
    }

    @Override
    public void doCancelAction() {
        projectState.colors.dismissShadow();
        projectState.updateUI();
        super.doCancelAction();
    }

}
