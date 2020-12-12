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

import com.intellij.ui.ColorUtil;
import com.intellij.util.xmlb.Converter;
import com.intellij.util.xmlb.annotations.Attribute;
import com.intellij.util.xmlb.annotations.Property;
import com.intellij.util.xmlb.annotations.Tag;
import com.intellij.util.xmlb.annotations.Transient;
import dev.pnbarx.idea.treecolor.utils.UIUtils;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

@Tag("color")
@Property(surroundWithTag = false, alwaysWrite = true, flat = true, style = Property.Style.ATTRIBUTE)
public class ColorSettings {

    // attributes must be public for PersistentStateComponent xml serialization
    @Attribute("id")
    public int _id;

    @Attribute(value = "value", converter = ColorHexConverter.class)
    public Color _color;

    @Attribute("name")
    public String _name;

    @Attribute("enabled")
    public boolean _enabled;

    // an empty constructor is needed for PersistentStateComponent xml serialization
    @SuppressWarnings({"UnusedDeclaration"})
    public ColorSettings() {
    }

    public ColorSettings(int id, Color color, String name, boolean isEnabled) {
        setId(id);
        setColor(color);
        setName(name);
        setEnabled(isEnabled);
    }

    public ColorSettings(ColorSettings colorSettings) {
        setId(colorSettings.getId());
        setColor(colorSettings.getColor());
        setName(colorSettings.getName());
        setEnabled(colorSettings.isEnabled());
    }

    @Transient
    public int getId() {
        return _id;
    }

    public void setId(int _id) {
        this._id = _id;
    }

    @Transient
    public Color getColor() {
        return _color;
    }

    public void setColor(Color color) {
        this._color = color;
    }

    @Transient
    public String getName() {
        return _name;
    }

    public void setName(String name) {
        this._name = name;
    }

    @Transient
    public boolean isEnabled() {
        return _enabled;
    }

    public void setEnabled(boolean isEnabled) {
        this._enabled = isEnabled;
    }

    @Transient
    public boolean isSetAndEnabled() {
        return _enabled && _color != null;
    }


    public static class ColorHexConverter extends Converter<Color> {

        @Nullable
        @Override
        public Color fromString(@Nullable String colorHex) {
            return ColorUtil.fromHex(colorHex, null);
        }

        @Nullable
        @Override
        public String toString(@Nullable Color color) {
            return color != null ? UIUtils.toHtmlColorWithOptionalAlpha(color) : "";
        }
    }
}
