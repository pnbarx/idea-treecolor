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

import com.intellij.openapi.diagnostic.Logger;
import dev.pnbarx.idea.treecolor.services.ProjectStateService;
import dev.pnbarx.idea.treecolor.state.beans.ColorSettings;
import dev.pnbarx.idea.treecolor.state.beans.ProjectState;
import dev.pnbarx.idea.treecolor.utils.UIUtils;
import org.jetbrains.annotations.NotNull;

import java.util.List;


public class ProjectColors {

    private static final Logger LOG = Logger.getInstance(ProjectColors.class);

    public static final int NUMBER_OF_COLOR_PRESETS = 16;

    private final ProjectStateService projectStateService;
    private final ProjectState projectState;

    public ProjectColors(@NotNull ProjectStateService projectStateService) {
        this.projectStateService = projectStateService;
        this.projectState = projectStateService.getState();
    }

    public void reassignColorIdsIfNeeded() {
        if (projectState.colorSettingsList.stream().anyMatch(colorSettings -> colorSettings.getId() < 1)) {
            int colorId = 1;
            for (ColorSettings colorSettings : projectState.colorSettingsList) {
                colorSettings.setId(colorId++);
            }
        }
    }

    public List<ColorSettings> getColorSettingsList() {
        return projectState.colorSettingsList;
    }

    @NotNull
    public ColorSettings getColorSettingsById(int colorId) {
        return projectState.colorSettingsList.stream()
            .filter(colorSettings -> colorSettings.getId() == colorId)
            .findFirst()
            .orElse(new ColorSettings(colorId, null, "Color " + colorId, false));
    }

    public void setColorSettingsById(int colorId, ColorSettings colorSettings) {
        int insertIndex = 0;
        boolean isNewColor = true;

        for (int i = 0; i < projectState.colorSettingsList.size(); i++) {
            int itemColorId = projectState.colorSettingsList.get(i).getId();
            if (itemColorId == colorId) {
                isNewColor = false;
                insertIndex = i;
                break;
            } else if (itemColorId < colorId) {
                insertIndex = i + 1;
            }
        }

        if (isNewColor) {
            projectState.colorSettingsList.add(insertIndex, colorSettings);
        } else {
            projectState.colorSettingsList.set(insertIndex, colorSettings);
        }

        UIUtils.updateUI(projectStateService.getProject());
    }

}
