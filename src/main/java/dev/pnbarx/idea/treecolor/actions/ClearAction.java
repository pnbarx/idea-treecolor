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
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.vfs.VirtualFile;
import dev.pnbarx.idea.treecolor.utils.ActionUtils;
import org.jetbrains.annotations.NotNull;


public class ClearAction extends AbstractAction {

    private static final Logger LOG = Logger.getInstance(ClearAction.class);

    public void actionPerformed(@NotNull AnActionEvent actionEvent) {

        VirtualFile[] files = getFiles(actionEvent);
        ProjectState projectState = getProjectState(actionEvent);
        Presentation presentation = actionEvent.getPresentation();

        if (projectState != null) {
            projectState.files.removeNodes(files, isRecursive(actionEvent));
            presentation.setEnabled(false);
        }
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent actionEvent) {
        VirtualFile[] files = ActionUtils.getFiles(actionEvent);
        ProjectState projectState = ProjectState.getInstance(actionEvent);

        if (projectState != null) {
            projectState.files.removeNodes(files);
        }
    }

    @Override
    public void update(@NotNull AnActionEvent actionEvent) {
        ProjectState projectState = ProjectState.getInstance(actionEvent);
        VirtualFile[] files = ActionUtils.getFiles(actionEvent);

        if (projectState != null && projectState.files.isHighlighted(files)) {
            ActionUtils.setEnabled(actionEvent, true);
        } else {
            ActionUtils.setEnabled(actionEvent, false);
        }
    }

}
