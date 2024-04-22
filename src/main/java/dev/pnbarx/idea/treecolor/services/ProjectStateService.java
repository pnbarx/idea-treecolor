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

package dev.pnbarx.idea.treecolor.services;

import com.intellij.configurationStore.StateStorageManagerKt;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.components.*;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.util.xmlb.XmlSerializer;
import com.intellij.util.xmlb.XmlSerializerUtil;
import dev.pnbarx.idea.treecolor.state.DefaultSettings;
import dev.pnbarx.idea.treecolor.state.ProjectColors;
import dev.pnbarx.idea.treecolor.state.ProjectFiles;
import dev.pnbarx.idea.treecolor.state.beans.MarkType;
import dev.pnbarx.idea.treecolor.state.beans.ProjectState;
import dev.pnbarx.idea.treecolor.utils.UIUtils;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;


@State(name = "ProjectTreeColorHighlighter", storages = {@Storage("highlightedFiles.xml")})
public class ProjectStateService implements PersistentStateComponent<ProjectState> {

    private static final Logger LOG = Logger.getInstance(ProjectStateService.class);

    private final AppStateService appStateService = AppStateService.getInstance();

    private final ProjectState state = new ProjectState();
    private Project project;

    public ProjectColors colors = new ProjectColors(this);
    public ProjectFiles files = new ProjectFiles(this);

    private ProjectStateService() {
        state.colorSettingsList = DefaultSettings.getColorSettingsList();
    }

    @Nullable
    public static ProjectStateService getInstance(@Nullable Project project) {
        if (project == null) return null;
        ProjectStateService instance = project.getService(ProjectStateService.class);
        instance.project = project;
        return instance;
    }

    @Nullable
    public static ProjectStateService getInstance(@Nullable AnActionEvent actionEvent) {
        if (actionEvent == null) return null;
        return getInstance(actionEvent.getProject());
    }

    @Override
    @NotNull
    public ProjectState getState() {
        return state;
    }

    @Override
    public void loadState(@NotNull ProjectState state) {
        XmlSerializerUtil.copyBean(state, this.state);
        colors.reassignColorIdsIfNeeded();
        UIUtils.updateUI(project);
    }

    @Nullable
    public Element getStateSnapshot() {
        try {
            return XmlSerializer.serialize(state);
        } catch (Exception ignored) {
            return null;
        }
    }

    public void restoreStateFromSnapshot(@Nullable Element snapshot) {
        try {
            assert snapshot != null;
            ProjectState restoredState = XmlSerializer.deserialize(snapshot, ProjectState.class);
            loadState(restoredState);
        } catch (Exception ignored) {
        }
    }

    public void saveAsIDEDefaults() {
        appStateService.setColorSettingsList(state.colorSettingsList);
        appStateService.setMarksForCollapsedHighlights(state.marksForCollapsedHighlights);
    }

    public void resetToIDEDefaults() {
        state.colorSettingsList = appStateService.getColorSettingsList();
        state.marksForCollapsedHighlights = appStateService.getMarksForCollapsedHighlights();
    }

    public void resetToDefaults() {
        state.colorSettingsList = DefaultSettings.getColorSettingsList();
        state.marksForCollapsedHighlights = DefaultSettings.getMarksForCollapsedHighlights();
    }

    public Project getProject() {
        return project;
    }

    /**
     * Saves state for all project-related persistent-state-components
     * Used for keeping our state file always-up-to-date
     */
    public void saveState() {
        try {
            Method saveComponentManager = StateStorageManagerKt.class
                .getMethod("saveComponentManager", ComponentManager.class, boolean.class);
            saveComponentManager.invoke(null, project, true);
        } catch (Exception ignored) {
            LOG.debug("Can't force save state in older versions prior to 191.4212.41");
        }
    }

    public MarkType getMarkType() {
        return MarkType.valueOfLabel(state.marksForCollapsedHighlights);
    }

    public void setMarkType(@NotNull MarkType markType) {
        state.marksForCollapsedHighlights = markType.toString();
    }

}
