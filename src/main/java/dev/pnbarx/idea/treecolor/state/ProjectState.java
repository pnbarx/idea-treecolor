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

import com.intellij.openapi.actionSystem.AnActionEvent;
import dev.pnbarx.idea.treecolor.state.models.ColorSettings;
import dev.pnbarx.idea.treecolor.state.models.HighlightedFile;
import com.intellij.ide.projectView.ProjectView;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.fileEditor.ex.FileEditorManagerEx;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.xmlb.XmlSerializerUtil;

import com.intellij.openapi.components.State;
import com.intellij.util.xmlb.annotations.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;


@State(name = "ProjectTreeColorHighlighter", storages = {@Storage("highlightedFiles.xml")})
public class ProjectState implements PersistentStateComponent<ProjectState.State> {

    private static final Logger LOG = Logger.getInstance(ProjectState.class);

    private State state = new State();
    private Project project;
    private boolean projectOpened = false;

    public ColorsState colors = new ColorsState(this);
    public FilesState files = new FilesState(this);

    @Nullable
    public static ProjectState getInstance(@Nullable Project project) {
        if (project == null) return null;
        ProjectState instance = ServiceManager.getService(project, ProjectState.class);
        instance.project = project;
        instance.colors.linkState(instance.state.colors);
        instance.files.linkState(instance.state.files);
        return instance;
    }

    @Nullable
    public static ProjectState getInstance(@Nullable AnActionEvent actionEvent) {
        if (actionEvent == null) return null;
        return getInstance(actionEvent.getProject());
    }

    @Override
    @NotNull
    public State getState() {
        return state;
    }

    @Override
    public void loadState(@NotNull State state) {
        XmlSerializerUtil.copyBean(state, this.state);
        colors.linkState(this.state.colors);
        files.linkState(this.state.files);
        updateUI();
    }

    public void updateUI() {
        if (project == null) return;

        FileEditorManagerEx editorManagerEx = FileEditorManagerEx.getInstanceEx(project);
        VirtualFile[] openFiles = editorManagerEx.getOpenFiles();
        for (VirtualFile openFile : openFiles) {
            editorManagerEx.updateFilePresentation(openFile);
        }
        ProjectView.getInstance(project).refresh();
    }

    public void triggerProjectOpened() {
        if (!projectOpened) {
            LOG.debug("Project is opened: " + project);
            projectOpened = true;
            updateUI();
        }
    }


    public static class State {

        @Tag("files")
        @XCollection()
        public List<HighlightedFile> files = new ArrayList<>();

        @Tag("colors")
        @XCollection()
        public List<ColorSettings> colors = new ArrayList<>();

    }

}
