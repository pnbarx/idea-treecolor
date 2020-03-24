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

import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.diagnostic.Logger;
import dev.pnbarx.idea.treecolor.state.ColorsState;
import dev.pnbarx.idea.treecolor.state.ProjectState;
import dev.pnbarx.idea.treecolor.state.models.ColorSettings;
import dev.pnbarx.idea.treecolor.ui.ColoredCircleIcon;
import dev.pnbarx.idea.treecolor.utils.ActionUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;


public class HighlightActionGroup extends ActionGroup {

    private static final Logger LOG = Logger.getInstance(HighlightActionGroup.class);

    @Override
    public void update(AnActionEvent actionEvent) {
        Presentation presentation = actionEvent.getPresentation();
        presentation.setEnabledAndVisible(isEnabled(actionEvent));
    }

    @NotNull
    @Override
    public AnAction[] getChildren(@Nullable AnActionEvent actionEvent) {
        ArrayList<AnAction> actionGroupItems = new ArrayList<>();
        ProjectState projectState = ProjectState.getInstance(actionEvent);

        if (isEnabled(actionEvent) && projectState != null) {
            for (int i = 0; i < ColorsState.NUMBER_OF_COLOR_PRESETS; i++) {
                HighlightAction highlightAction = getHighlightAction(i, projectState);
                if (highlightAction != null) {
                    actionGroupItems.add(highlightAction);
                }
            }
            actionGroupItems.add(new Separator()); // -------
            actionGroupItems.add(new ClearAction());
            actionGroupItems.add(new ClearRecursivelyAction());
            actionGroupItems.add(new Separator()); // -------
            actionGroupItems.add(new DefineColorsAction());
        }

        return actionGroupItems.toArray(new AnAction[0]);
    }

    @Nullable
    private HighlightAction getHighlightAction(int colorIndex, @NotNull ProjectState projectState) {
        ColorSettings colorSettings = projectState.colors.getColorSettingsOrNull(colorIndex);
        if (colorSettings == null || !colorSettings.isEnabled()) return null;
        HighlightAction highlightAction = new HighlightAction(
            colorSettings.getSafeName(50),
            "Highlight in color #" + (colorIndex + 1),
            ColoredCircleIcon.getInstance(colorSettings.getColorHex())
        );
        highlightAction.setColorIndex(colorIndex);
        return highlightAction;
    }

    private boolean isEnabled(@Nullable AnActionEvent actionEvent) {
        return actionEvent != null && ActionUtils.getFiles(actionEvent) != null;
    }

}
