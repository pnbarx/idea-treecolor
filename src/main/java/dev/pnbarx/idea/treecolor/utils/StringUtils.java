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

package dev.pnbarx.idea.treecolor.utils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class StringUtils {

    @NotNull
    public static String getSafeLabelString(@Nullable String labelString, String fallbackString, int maxLength) {
        String safeLabelString = labelString != null ? labelString.trim() : "";
        if(safeLabelString.isEmpty()) {
            return fallbackString;
        }
        if(safeLabelString.length() > maxLength) {
            return safeLabelString.substring(0, maxLength) + "...";
        }
        return safeLabelString;
    }

    public static String addTrailingSlash(@NotNull String path) {
        return path.endsWith("/") ? path : path + "/";
    }

}
