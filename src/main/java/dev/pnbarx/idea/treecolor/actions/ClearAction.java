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
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.vfs.VirtualFile;
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

    public void update(@NotNull AnActionEvent actionEvent) {
        ProjectState projectState = getProjectState(actionEvent);
        VirtualFile[] files = getFiles(actionEvent);
        Presentation presentation = actionEvent.getPresentation();

        if (projectState != null && projectState.files.isHighlighted(files, isRecursive(actionEvent))) {
            presentation.setEnabled(true);
        } else {
            presentation.setEnabled(false);
        }
    }

    private boolean isRecursive(AnActionEvent actionEvent) {
        return getId(actionEvent).equals("clearRecursive");
    }

}
