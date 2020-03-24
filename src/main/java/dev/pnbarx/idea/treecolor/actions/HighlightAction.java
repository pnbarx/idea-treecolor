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

import com.intellij.openapi.actionSystem.AnAction;
import dev.pnbarx.idea.treecolor.state.ProjectState;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.vfs.VirtualFile;
import dev.pnbarx.idea.treecolor.utils.ActionUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;


public class HighlightAction extends AnAction {

    private static final Logger LOG = Logger.getInstance(HighlightAction.class);

    @SuppressWarnings({"UnusedDeclaration"}) // action must have a no-argument constructor
    public HighlightAction() {
        super();
    }

    public HighlightAction(@Nullable String name, @Nullable String description, @Nullable Icon icon) {
        super(name, description, icon);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent actionEvent) {
        ProjectState projectState = ProjectState.getInstance(actionEvent);
        if (projectState == null) return;
        int colorIndex = getColorIndex();
        VirtualFile[] files = ActionUtils.getFiles(actionEvent);
        projectState.files.addNodes(files, colorIndex);
    }

    protected int getColorIndex() {
        Presentation presentation = getTemplatePresentation();
        try {
            return (int) presentation.getClientProperty("colorIndex");
        } catch (NullPointerException e) {
            LOG.debug("colorIndex is undefined");
            return -1;
        }
    }

    public void setColorIndex(int colorIndex) {
        Presentation presentation = getTemplatePresentation();
        presentation.putClientProperty("colorIndex", colorIndex);
    }

}
