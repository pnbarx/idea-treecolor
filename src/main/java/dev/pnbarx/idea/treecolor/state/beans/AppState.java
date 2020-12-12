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

package dev.pnbarx.idea.treecolor.state.beans;

import com.intellij.util.xmlb.annotations.Tag;
import com.intellij.util.xmlb.annotations.XCollection;
import dev.pnbarx.idea.treecolor.state.DefaultSettings;

import java.util.List;

public class AppState {

    @Tag("colors")
    @XCollection()
    public List<ColorSettings> colorSettingsList;

    public String marksForCollapsedHighlights;

    public AppState() {
        colorSettingsList = DefaultSettings.getColorSettingsList();
        marksForCollapsedHighlights = DefaultSettings.getMarksForCollapsedHighlights();
    }

}
