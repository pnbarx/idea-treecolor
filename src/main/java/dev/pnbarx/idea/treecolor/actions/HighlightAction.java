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

package dev.pnbarx.idea.treecolor.actions;

import dev.pnbarx.idea.treecolor.state.ProjectState;
import dev.pnbarx.idea.treecolor.state.models.ColorSettings;
import dev.pnbarx.idea.treecolor.ui.ColoredCircleIcon;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;


public class HighlightAction extends AbstractAction {

    private static final Logger LOG = Logger.getInstance(HighlightAction.class);

    public void actionPerformed(@NotNull AnActionEvent actionEvent) {
        ProjectState projectState = getProjectState(actionEvent);
        if (projectState == null) return;
        int colorIndex = getColorIndex(actionEvent);

        VirtualFile[] files = getFiles(actionEvent);
        projectState.files.addNodes(files, colorIndex);
    }

    public void update(@NotNull AnActionEvent actionEvent) {

        ColorSettings colorSettings = getColorSettings(actionEvent);
        Presentation presentation = actionEvent.getPresentation();

        if (colorSettings == null || !colorSettings.isEnabled()) {
            presentation.setVisible(false);
        } else {
            Icon actionIcon = ColoredCircleIcon.getInstance(colorSettings.getColorHex());
            String actionName = colorSettings.getName().trim();
            if (actionName.equals("")) {
                actionName = " ";
            } else if (actionName.length() > 50) {
                actionName = actionName.substring(0, 50) + "...";
            }
            presentation.setIcon(actionIcon);
            presentation.setText(actionName);
        }
    }

    @Nullable
    protected ColorSettings getColorSettings(AnActionEvent actionEvent) {
        int colorIndex = getColorIndex(actionEvent);
        ProjectState projectState = getProjectState(actionEvent);
        if (projectState == null) return null;

        return projectState.colors.getColorSettingsOrNull(colorIndex);
    }

    protected int getColorIndex(AnActionEvent actionEvent) {
        String actionId = getId(actionEvent);
        try {
            return Integer.parseInt(actionId.replaceAll("\\D", ""));
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
