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

package dev.pnbarx.idea.treecolor.state.models;

import com.intellij.util.xmlb.annotations.Attribute;
import com.intellij.util.xmlb.annotations.Property;
import com.intellij.util.xmlb.annotations.Tag;


@Tag("node")
@Property(surroundWithTag = false, flat = true)
public class HighlightedFile {

    @Attribute("color")
    public int colorIndex;

    @Attribute("path")
    public String path;

    // needed for xml serialization
    @SuppressWarnings({"UnusedDeclaration"})
    public HighlightedFile() {
    }

    public HighlightedFile(String path, int colorIndex) {
        this.path = path;
        this.colorIndex = colorIndex;
    }

    public String getPath() {
        return path;
    }

    public int getColorIndex() {
        return colorIndex;
    }
}
