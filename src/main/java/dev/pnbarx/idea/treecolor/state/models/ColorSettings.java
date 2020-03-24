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

import com.intellij.ui.ColorUtil;
import com.intellij.ui.JBColor;
import com.intellij.util.xmlb.annotations.Tag;
import com.intellij.util.xmlb.annotations.Property;
import com.intellij.util.xmlb.annotations.Attribute;
import com.intellij.util.xmlb.annotations.Transient;

import java.awt.*;


@Tag("color")
@Property(surroundWithTag = false, flat = true)
public class ColorSettings implements Cloneable {

    // attributes must be public for PersistentStateComponent xml serialization
    @Attribute("name")
    public String _name;

    @Attribute("value")
    public String _colorHex;

    @Attribute("enabled")
    public boolean _enabled;

    // needed for xml serialization
    @SuppressWarnings({"UnusedDeclaration"})
    public ColorSettings() {
    }

    public ColorSettings(String colorHex, boolean isEnabled, String name) {
        setName(name);
        setColor(colorHex);
        setEnabled(isEnabled);
    }

    public ColorSettings(Color color, boolean isEnabled, String name) {
        setName(name);
        setColor(color);
        setEnabled(isEnabled);
    }

    @Transient
    public String getName() {
        return this._name;
    }

    @Transient
    public String getSafeName(int maxLength) {
        String safeName = this._name.trim();
        if(safeName.equals("")) {
            safeName = " ";
        } else if(safeName.length() > maxLength) {
            safeName = safeName.substring(0, maxLength) + "...";
        }
        return safeName;
    }

    public void setName(String name) {
        this._name = name;
    }

    @Transient
    public String getColorHex() {
        return this._colorHex;
    }

    @Transient
    public Color getColor() {
        return ColorUtil.fromHex(this._colorHex, getDefaultColor());
    }

    public void setColor(String colorHex) {
        this._colorHex = colorHex;
    }

    public void setColor(Color color) {
        if (color == null) color = getDefaultColor();
        this._colorHex = ColorUtil.toHtmlColor(color);
    }

    @Transient
    public static Color getDefaultColor() {
        return JBColor.namedColor("Tree.background", new JBColor(0x555555, 0xbbbbbb));
    }

    @Transient
    public boolean isEnabled() {
        return this._enabled;
    }

    public void setEnabled(boolean isEnabled) {
        this._enabled = isEnabled;
    }

    @Override
    public ColorSettings clone() throws CloneNotSupportedException {
        return (ColorSettings) super.clone();
    }

}
