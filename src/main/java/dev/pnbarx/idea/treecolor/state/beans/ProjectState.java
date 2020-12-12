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

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.util.xmlb.annotations.Property;
import com.intellij.util.xmlb.annotations.Tag;
import com.intellij.util.xmlb.annotations.XCollection;
import dev.pnbarx.idea.treecolor.services.AppStateService;

import java.util.ArrayList;
import java.util.List;

public class ProjectState {

    private static final Logger LOG = Logger.getInstance(ProjectState.class);

    @Tag("files")
    @XCollection()
    @Property(alwaysWrite = true)
    public List<HighlightedFile> highlightedFileList;

    @Tag("colors")
    @XCollection()
    @Property(alwaysWrite = true)
    public List<ColorSettings> colorSettingsList;

    @Property(alwaysWrite = true)
    public String marksForCollapsedHighlights;

    public ProjectState() {
        AppStateService appStateService = AppStateService.getInstance();
        LOG.debug("*** INITIALIZED PROJECT STATE");
        highlightedFileList = new ArrayList<>();
        colorSettingsList = appStateService.getColorSettingsList();
        marksForCollapsedHighlights = appStateService.getMarksForCollapsedHighlights();
        LOG.debug("COLOR SETTINGS LIST: " + colorSettingsList);
    }

}
