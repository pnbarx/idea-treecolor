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

import com.intellij.openapi.actionSystem.ActionUpdateThread;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.vfs.VirtualFile;
import dev.pnbarx.idea.treecolor.services.ProjectStateService;
import dev.pnbarx.idea.treecolor.utils.ActionUtils;
import org.jetbrains.annotations.NotNull;


public class ClearAction extends AnAction {

    private static final Logger LOG = Logger.getInstance(ClearAction.class);

    public ClearAction() {
        super(
            "Clear",
            "Remove highlighting",
            null
        );
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent actionEvent) {
        VirtualFile[] files = ActionUtils.getFiles(actionEvent);
        ProjectStateService projectStateService = ProjectStateService.getInstance(actionEvent);

        if (projectStateService != null) {
            projectStateService.files.removeNodes(files);
            projectStateService.saveState();
        }
    }

    @Override
    public void update(@NotNull AnActionEvent actionEvent) {
        ProjectStateService projectStateService = ProjectStateService.getInstance(actionEvent);
        VirtualFile[] files = ActionUtils.getFiles(actionEvent);

        boolean isActionEnabled = projectStateService != null
                && projectStateService.files.isHighlighted(files);
        ActionUtils.setActionEnabled(actionEvent, isActionEnabled);
    }

    @Override
    public @NotNull ActionUpdateThread getActionUpdateThread() {
        return ActionUpdateThread.BGT;
    }
}
