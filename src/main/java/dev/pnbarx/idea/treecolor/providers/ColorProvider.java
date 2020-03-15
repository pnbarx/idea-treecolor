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

package dev.pnbarx.idea.treecolor.providers;

import dev.pnbarx.idea.treecolor.state.models.ColorSettings;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.fileEditor.impl.EditorTabColorProvider;
import com.intellij.openapi.fileEditor.impl.EditorWindow;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.ColorUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import dev.pnbarx.idea.treecolor.state.ProjectState;

import java.awt.*;


public class ColorProvider implements EditorTabColorProvider {

    private static final Logger LOG = Logger.getInstance(ColorProvider.class);

    @Nullable
    private Color getColor(@NotNull Project project, @NotNull VirtualFile file) {

        ProjectState projectState = ProjectState.getInstance(project);
        if(projectState == null) return null;

        projectState.triggerProjectOpened();

        Integer colorIndex = projectState.files.getNodeColorIndex(file);
        if(colorIndex == null) return null;

        ColorSettings colorSettings = projectState.colors.getColorSettings(colorIndex);
        if(!colorSettings.isEnabled()) return null;

        return ColorUtil.fromHex(colorSettings.getColorHex(), Color.DARK_GRAY);
    }

    @Nullable
    @Override
    public Color getEditorTabColor(@NotNull Project project, @NotNull VirtualFile file) {
        return getColor(project, file);
    }

    @Nullable
    @Override
    public Color getEditorTabColor(@NotNull Project project, @NotNull VirtualFile file, @Nullable EditorWindow editorWindow) {
        return getColor(project, file);
    }

    @Nullable
    @Override
    public Color getProjectViewColor(@NotNull Project project, @NotNull VirtualFile file) {
        return getColor(project, file);
    }

}
