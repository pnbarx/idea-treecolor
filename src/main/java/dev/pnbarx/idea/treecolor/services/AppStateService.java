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

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.*;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.util.xmlb.XmlSerializerUtil;
import dev.pnbarx.idea.treecolor.state.beans.AppState;
import dev.pnbarx.idea.treecolor.state.beans.ColorSettings;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@State(name = "ProjectTreeColorHighlighter", storages = {@Storage("projectTreeColorHighlighter.xml")})
public class AppStateService implements PersistentStateComponent<AppState> {

    private static final Logger LOG = Logger.getInstance(AppStateService.class);

    private final AppState state = new AppState();

    public static AppStateService getInstance() {
        return ApplicationManager.getApplication().getService(AppStateService.class);
    }

    @Nullable
    @Override
    public AppState getState() {
        return state;
    }

    @Override
    public void loadState(@NotNull AppState state) {
        XmlSerializerUtil.copyBean(state, this.state);
        LOG.debug("AppState loaded");
    }

    @Nullable
    public List<ColorSettings> getColorSettingsList() {
        return state.colorSettingsList.stream()
            .map(ColorSettings::new)
            .collect(Collectors.toCollection(ArrayList::new));
    }

    public void setColorSettingsList(@NotNull List<ColorSettings> colorSettingsList) {
        state.colorSettingsList = colorSettingsList.stream()
            .map(ColorSettings::new)
            .collect(Collectors.toCollection(ArrayList::new));
    }

    @Nullable
    public String getMarksForCollapsedHighlights() {
        return state.marksForCollapsedHighlights;
    }

    public void setMarksForCollapsedHighlights(String marks) {
        state.marksForCollapsedHighlights = marks;
    }

}
