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
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractAction extends AnAction {

    protected String getId(AnActionEvent actionEvent) {
        return actionEvent.getActionManager().getId(this);
    }

    protected VirtualFile[] getFiles(AnActionEvent actionEvent) {
        return actionEvent.getData(CommonDataKeys.VIRTUAL_FILE_ARRAY);
    }

    @Nullable
    protected ProjectState getProjectState(AnActionEvent actionEvent) {
        Project project = actionEvent.getProject();
        return ProjectState.getInstance(project);
    }

}
