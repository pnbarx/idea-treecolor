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

package dev.pnbarx.idea.treecolor.state;

import dev.pnbarx.idea.treecolor.state.models.HighlightedFile;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;


public class FilesState {

    private static final Logger LOG = Logger.getInstance(FilesState.class);

    public List<HighlightedFile> state;
    private ProjectState projectState;

    FilesState(ProjectState projectState) {
        this.projectState = projectState;
    }

    public void linkState(List<HighlightedFile> state) {
        this.state = state;
    }

    @Nullable
    public Integer getNodeColorIndex(VirtualFile file) {
        Integer colorIndex = null;
        String path = addTrailingSlash(file.getPath());

        for (HighlightedFile node : state) {
            if (path.startsWith(addTrailingSlash(node.path))) {
                colorIndex = node.colorIndex - 1;
            }
        }

        return colorIndex;
    }

    public boolean isHighlighted(VirtualFile[] files) {
        return Arrays.stream(files).anyMatch(
            file -> state.stream().anyMatch(
                node -> node.path.equals(file.getPath())
            )
        );
    }

    public boolean isHighlightedRecursively(VirtualFile[] files) {
        return Arrays.stream(files).anyMatch(
            file -> state.stream().anyMatch(
                node -> node.path.startsWith(addTrailingSlash(file.getPath()))
            )
        );
    }

    public void addNode(VirtualFile file, int colorIndex) {
        String path = file.getPath();
        state.removeIf(node -> node.path.equals(path));
        state.add(new HighlightedFile(path, colorIndex + 1));
        state.sort(Comparator.comparing(node -> node.path));
        projectState.updateUI();
    }

    public void addNodes(VirtualFile[] files, int colorIndex) {
        for (VirtualFile file : files) {
            addNode(file, colorIndex);
        }
    }

    public void removeNodes(@Nullable VirtualFile[] files) {
        if (files == null) return;
        for (VirtualFile file : files) {
            String path = file.getPath();
            state.removeIf(node -> node.path.equals(path));
        }
        projectState.updateUI();
    }

    public void removeNodesRecursively(@Nullable VirtualFile[] files) {
        if (files == null) return;
        for (VirtualFile file : files) {
            String path = file.getPath();
            state.removeIf(
                node -> addTrailingSlash(node.path).startsWith(addTrailingSlash(path))
            );
        }
        projectState.updateUI();
    }

    private String addTrailingSlash(String path) {
        return path.endsWith("/") ? path : path + "/";
    }
}
