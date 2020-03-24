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

import dev.pnbarx.idea.treecolor.state.models.ColorSettings;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.ui.ColorUtil;
import com.intellij.ui.JBColor;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;


public class ColorsState {

    public static final int NUMBER_OF_COLOR_PRESETS = 16;
    private static final Logger LOG = Logger.getInstance(ColorsState.class);

    private ProjectState projectState;
    private List<ColorSettings> state;
    private List<ColorSettings> realState;
    private List<ColorSettings> shadowState;
    private boolean shadowMode = false;

    ColorsState(ProjectState projectState) {
        this.projectState = projectState;
    }

    public void linkState(List<ColorSettings> state) {
        this.realState = state;
        if (this.realState.size() == 0) {
            initWithDefaults();
        }
        if (!shadowMode) {
            this.state = this.realState;
        }
    }

    public void useShadow() {
        if (shadowMode) return;
        shadowState = new ArrayList<>();
        try {
            for (ColorSettings colorSettings : realState) {
                shadowState.add(colorSettings.clone());
            }
        } catch (CloneNotSupportedException e) {
            return;
        }
        shadowMode = true;
        state = shadowState;
    }

    public void applyShadow() {
        if (!shadowMode) return;
        realState.clear();
        realState.addAll(shadowState);
        shadowMode = false;
        state = realState;
    }

    public void dismissShadow() {
        if (!shadowMode) return;
        shadowMode = false;
        state = realState;
    }

    private void initWithDefaults() {
        JBColor[] defaultColors = {
                new JBColor(0xde2c2c, 0x4f060d),
                new JBColor(0xff6d0d, 0x44220e),
                new JBColor(0xfefc22, 0x3f371b),
                new JBColor(0x28ff2e, 0x162c16),
                new JBColor(0x00f2ff, 0x0f2f47),
                new JBColor(0x00b5ff, 0x171a34),
                new JBColor(0xb892db, 0x311333),
                new JBColor(0xbebebe, 0x1e1e1e)
        };
        realState.clear();
        for (int i = 0; i < defaultColors.length; i++) {
            realState.add(new ColorSettings(
                    ColorUtil.toHtmlColor(defaultColors[i]),
                    true,
                    "Color " + (i + 1)
            ));
        }
    }

    public ColorSettings getColorSettings(int index) {
        ColorSettings colorSettings = getColorSettingsOrNull(index);
        if (colorSettings == null) {
            return getDefaultColorSettings(index);
        }
        return colorSettings;
    }

    public ColorSettings getDefaultColorSettings(int index) {
        Color defaultColor = ColorSettings.getDefaultColor();
        String defaultColorHex = "#" + ColorUtil.toHex(defaultColor);
        return new ColorSettings(defaultColorHex, false, "Color " + (index + 1));
    }

    @Nullable
    public ColorSettings getColorSettingsOrNull(int index) {
        try {
            return state.get(index);
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    public void setColorSettings(int index, String colorHex, boolean isEnabled, String name) {
        ColorSettings colorSettings = getColorSettings(index);
        colorSettings.setColor(colorHex);
        colorSettings.setEnabled(isEnabled);
        colorSettings.setName(name);
        setColorSettings(index, colorSettings);
    }

    public void setColorSettings(int index, String colorHex, boolean isEnabled) {
        ColorSettings colorSettings = getColorSettings(index);
        colorSettings.setColor(colorHex);
        colorSettings.setEnabled(isEnabled);
        setColorSettings(index, colorSettings);
    }

    public void setColorSettings(int index, String colorHex) {
        ColorSettings colorSettings = getColorSettings(index);
        colorSettings.setColor(colorHex);
        setColorSettings(index, colorSettings);
    }

    public void setColorSettings(int index, Color color) {
        ColorSettings colorSettings = getColorSettings(index);
        colorSettings.setColor(color);
        setColorSettings(index, colorSettings);
    }

    public void setColorSettings(int index, ColorSettings colorSettings) {
        for (int i = state.size(); i <= index; i++) {
            state.add(getDefaultColorSettings(i));
        }
        state.set(index, colorSettings);
        projectState.updateUI();
    }

}
