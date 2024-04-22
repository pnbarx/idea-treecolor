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

import com.intellij.ui.JBColor;
import dev.pnbarx.idea.treecolor.state.beans.ColorSettings;
import dev.pnbarx.idea.treecolor.state.beans.MarkType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DefaultSettings {
    public static List<ColorSettings> getColorSettingsList() {
        return new ArrayList<>(Arrays.asList(
                new ColorSettings(1, new JBColor(0xde2c2c, 0x4f060d), "Color 1", true),
                new ColorSettings(2, new JBColor(0xff6d0d, 0x44220e), "Color 2", true),
                new ColorSettings(3, new JBColor(0xfefc22, 0x3f371b), "Color 3", true),
                new ColorSettings(4, new JBColor(0x28ff2e, 0x162c16), "Color 4", true),
                new ColorSettings(5, new JBColor(0x00f2ff, 0x0f2f47), "Color 5", true),
                new ColorSettings(6, new JBColor(0x00b5ff, 0x171a34), "Color 6", true),
                new ColorSettings(7, new JBColor(0xb892db, 0x311333), "Color 7", true),
                new ColorSettings(8, new JBColor(0xbebebe, 0x1e1e1e), "Color 8", true)
        ));
    }

    public static String getMarksForCollapsedHighlights() {
        return MarkType.DOTS.toString();
    }
}
