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

import com.intellij.util.xmlb.annotations.Attribute;
import com.intellij.util.xmlb.annotations.Property;
import com.intellij.util.xmlb.annotations.Tag;
import com.intellij.util.xmlb.annotations.Transient;

@Tag("node")
@Property(surroundWithTag = false, alwaysWrite = true, flat = true, style = Property.Style.ATTRIBUTE)
public class HighlightedFile {

    // attributes must be public for PersistentStateComponent xml serialization
    @Attribute("color")
    public Integer _colorId;

    @Attribute("path")
    public String _path;

    // an empty constructor is needed for PersistentStateComponent xml serialization
    @SuppressWarnings({"UnusedDeclaration"})
    public HighlightedFile() {
    }

    public HighlightedFile(String path, Integer colorId) {
        this._path = path;
        this._colorId = colorId;
    }

    @Transient
    public String getPath() {
        return _path;
    }

    @Transient
    public int getColorId() {
        return _colorId;
    }

}
