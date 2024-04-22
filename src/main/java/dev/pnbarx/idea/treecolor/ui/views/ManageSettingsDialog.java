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

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.project.ProjectKt;
import dev.pnbarx.idea.treecolor.services.AppStateService;
import dev.pnbarx.idea.treecolor.services.ProjectStateService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;

public class ManageSettingsDialog extends DialogWrapper {

    private static final Logger LOG = Logger.getInstance(ManageSettingsDialog.class);

    private final Project project;
    private final ProjectStateService projectStateService;
    private final AppStateService appStateService;

    protected ManageSettingsDialog(@NotNull Project project) {
        super(project, true);

        this.project = project;
        this.projectStateService = ProjectStateService.getInstance(project);
        this.appStateService = AppStateService.getInstance();
        setTitle("Project Color Settings");
        init();
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        JPanel dialogPanel = new JPanel(new GridLayout(0, 2, 10, 10));

        JButton saveAsIDEDefaultsBtn = new JButton("Save current colors as IDE defaults");
        saveAsIDEDefaultsBtn.addActionListener(this::saveAsIDEDefaults);

        JButton openConfigFileBtn = new JButton("Open the project settings configuration file");
        openConfigFileBtn.addActionListener(this::openConfigFile);

        JButton loadFromIDEDefaultsBtn = new JButton("Load IDE defaults");
        loadFromIDEDefaultsBtn.addActionListener(this::loadFromIDEDefaults);

        JButton resetToDefaultsBtn = new JButton("Reset colors to factory defaults");
        resetToDefaultsBtn.addActionListener(this::resetToDefaults);

        JPanel leftPanel = new JPanel(new GridLayout(0, 1, 0, 10));
        leftPanel.setBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createEtchedBorder(),
                BorderFactory.createEmptyBorder(12, 10, 12, 10)
            )
        );

        JPanel rightPanel = new JPanel(new GridLayout(0, 1, 10, 10));
        rightPanel.setBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createEtchedBorder(),
                BorderFactory.createEmptyBorder(12, 10, 12, 10)
            )
        );

        leftPanel.add(saveAsIDEDefaultsBtn);
        leftPanel.add(openConfigFileBtn);

        rightPanel.add(loadFromIDEDefaultsBtn);
        rightPanel.add(resetToDefaultsBtn);

        dialogPanel.add(leftPanel);
        dialogPanel.add(rightPanel);

        return dialogPanel;
    }

    @NotNull
    @Override
    protected Action @NotNull [] createActions() {
        Action okAction = getOKAction();
        okAction.putValue(Action.NAME,"Close");
        return new Action[]{
            okAction
        };
    }

    private void saveAsIDEDefaults(ActionEvent e) {
        projectStateService.saveAsIDEDefaults();
        close(DialogWrapper.OK_EXIT_CODE);
    }

    private void loadFromIDEDefaults(ActionEvent e) {
        projectStateService.resetToIDEDefaults();
        close(DialogWrapper.OK_EXIT_CODE);
    }

    private void resetToDefaults(ActionEvent e) {
        projectStateService.resetToDefaults();
        close(DialogWrapper.OK_EXIT_CODE);
    }

    private void openConfigFile(ActionEvent e) {
        int offset = -1;
        VirtualFile configFile;

        projectStateService.saveState();

        VirtualFile projectFile = project.getProjectFile();
        if (projectFile == null) return;

        if (ProjectKt.isDirectoryBased(project)) {
            LOG.debug("Project is directory-based");
            configFile = projectFile.getParent().findChild("highlightedFiles.xml");
        } else {
            LOG.debug("Project is file-based");
            configFile = projectFile;
            try {
                offset = VfsUtil.loadText(configFile).indexOf("<component name=\"ProjectTreeColorHighlighter\">");
            } catch (IOException ignored) {
            }
        }

        if (configFile != null) {
            OpenFileDescriptor descriptor = new OpenFileDescriptor(project, configFile, offset);
            descriptor.navigateInEditor(project, true);
            close(ColorsDialog.DISPOSE_EXIT_CODE);
        }
    }

}
