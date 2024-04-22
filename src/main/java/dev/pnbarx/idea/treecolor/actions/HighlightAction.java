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
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.vfs.VirtualFile;
import dev.pnbarx.idea.treecolor.services.ProjectStateService;
import dev.pnbarx.idea.treecolor.utils.ActionUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;


public class HighlightAction extends AnAction {

    private static final Logger LOG = Logger.getInstance(HighlightAction.class);
    private static final Key<Integer> COLOR_ID_KEY = Key.create("colorId");

    @SuppressWarnings({"UnusedDeclaration"}) // action must have a no-argument constructor
    public HighlightAction() {
        super();
    }

    public HighlightAction(@Nullable String name, @Nullable String description, @Nullable Icon icon) {
        super(name, description, icon);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent actionEvent) {
        ProjectStateService projectStateService = ProjectStateService.getInstance(actionEvent);
        if (projectStateService == null) return;
        int colorId = getColorId();
        VirtualFile[] files = ActionUtils.getFiles(actionEvent);
        projectStateService.files.addNodes(files, colorId);
        projectStateService.saveState();
    }

    protected int getColorId() {
        Presentation presentation = getTemplatePresentation();
        try {
            //noinspection ConstantConditions
            return presentation.getClientProperty(COLOR_ID_KEY);
        } catch (NullPointerException e) {
            LOG.debug("colorIndex is undefined");
            return -1;
        }
    }

    public void setColorId(int colorId) {
        Presentation presentation = getTemplatePresentation();
        presentation.putClientProperty(COLOR_ID_KEY, colorId);
    }
}
