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

import com.intellij.openapi.util.text.NaturalComparator;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.vfs.VirtualFile;
import dev.pnbarx.idea.treecolor.services.ProjectStateService;
import dev.pnbarx.idea.treecolor.state.beans.HighlightedFile;
import dev.pnbarx.idea.treecolor.state.beans.ProjectState;
import dev.pnbarx.idea.treecolor.utils.StringUtils;
import dev.pnbarx.idea.treecolor.utils.UIUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


public class ProjectFiles {

    private static final Logger LOG = Logger.getInstance(ProjectFiles.class);

    private final ProjectStateService projectStateService;
    private final ProjectState projectState;

    public ProjectFiles(ProjectStateService projectStateService) {
        this.projectStateService = projectStateService;
        this.projectState = projectStateService.getState();
    }

    public List<HighlightedFile> getHighlightedFileList() {
        return projectState.highlightedFileList;
    }

    public List<HighlightedFile> getHighlightedFileList(String parentPath) {
        String parentPathWithSlash = StringUtils.addTrailingSlash(parentPath);
        return projectState.highlightedFileList.stream().filter(
            node -> node.getPath().startsWith(parentPathWithSlash)
        ).collect(Collectors.toList());
    }

    public void addNodes(@Nullable VirtualFile[] files, int colorId) {
        if (files == null) return;
        for (VirtualFile file : files) {
            String filePath = file.getPath();
            projectState.highlightedFileList.removeIf(node -> node.getPath().equals(filePath));
            projectState.highlightedFileList.add(new HighlightedFile(filePath, colorId));
        }
        projectState.highlightedFileList.sort(
            (node1, node2) -> NaturalComparator.INSTANCE.compare(node1.getPath(), node2.getPath())
        );
        UIUtils.updateUI(projectStateService.getProject());
    }

    public void removeNodes(@Nullable VirtualFile[] files) {
        if (files == null) return;
        for (VirtualFile file : files) {
            String path = file.getPath();
            projectState.highlightedFileList.removeIf(node -> node.getPath().equals(path));
        }
        UIUtils.updateUI(projectStateService.getProject());
    }

    public void removeNodesRecursively(@Nullable VirtualFile[] files) {
        if (files == null) return;
        for (VirtualFile file : files) {
            String filePathWithSlash = StringUtils.addTrailingSlash(file.getPath());
            projectState.highlightedFileList.removeIf(
                node -> StringUtils.addTrailingSlash(node.getPath()).startsWith(filePathWithSlash)
            );
        }
        UIUtils.updateUI(projectStateService.getProject());
    }

    @Nullable
    public Integer getNodeColorId(@NotNull VirtualFile file) {
        Integer colorId = null;
        String pathWithSlash = StringUtils.addTrailingSlash(file.getPath());

        for (HighlightedFile node : projectState.highlightedFileList) {
            if (pathWithSlash.startsWith(StringUtils.addTrailingSlash(node.getPath()))) {
                colorId = node.getColorId();
            }
        }

        return colorId;
    }

    public boolean isHighlighted(VirtualFile[] files) {
        return Arrays.stream(files).anyMatch(
            file -> projectState.highlightedFileList.stream().anyMatch(
                node -> node.getPath().equals(file.getPath())
            )
        );
    }

    public boolean isHighlightedRecursively(VirtualFile[] files) {
        return Arrays.stream(files).anyMatch(
            file -> {
                String filePathWithSlash = StringUtils.addTrailingSlash(file.getPath());
                return projectState.highlightedFileList.stream().anyMatch(
                    node -> node.getPath().startsWith(filePathWithSlash)
                );
            }
        );
    }

    public boolean isHighlightedInColor(VirtualFile[] files, int colorId) {
        return Arrays.stream(files).anyMatch(
            file -> projectState.highlightedFileList.stream().anyMatch(
                node -> node.getPath().equals(file.getPath()) && node.getColorId() == colorId
            )
        );
    }

    public boolean isHighlightedInColorRecursively(VirtualFile[] files, int colorId) {
        return Arrays.stream(files).anyMatch(
            file -> {
                String filePathWithSlash = StringUtils.addTrailingSlash(file.getPath());
                return projectState.highlightedFileList.stream().anyMatch(
                    node -> node.getPath().startsWith(filePathWithSlash) && node.getColorId() == colorId
                );
            }
        );
    }

}
