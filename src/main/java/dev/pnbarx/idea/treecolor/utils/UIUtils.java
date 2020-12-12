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

import com.intellij.ide.projectView.ProjectView;
import com.intellij.ide.projectView.impl.AbstractProjectViewPane;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.fileEditor.ex.FileEditorManagerEx;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.ColorUtil;
import com.intellij.ui.JBColor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;


public class UIUtils {

    private static final Logger LOG = Logger.getInstance(UIUtils.class);

    private static volatile long lastInvokeTimeMs = 0;
    private static volatile long lastUpdateTimeMs = 0;
    private static final int UPDATE_THRESHOLD = 100;

    public static void updateUI(@Nullable Project project) {
        debounce(() -> {
            updateProjectView(project);
            updateOpenTabs(project);
        });
    }

    public static void updateOpenTabs(@Nullable Project project) {
        if (project == null) return;
        FileEditorManagerEx editorManagerEx = FileEditorManagerEx.getInstanceEx(project);
        VirtualFile[] openFiles = editorManagerEx.getOpenFiles();
        for (VirtualFile openFile : openFiles) {
            editorManagerEx.updateFilePresentation(openFile);
        }
    }

    public static void updateProjectView(@Nullable Project project) {
        if (project == null) return;
        ProjectView projectView = ProjectView.getInstance(project);
        AbstractProjectViewPane projectViewPane = projectView.getCurrentProjectViewPane();
        if (projectViewPane != null) {
            projectViewPane.updateFromRoot(true);
        }
    }

    private static void debounce(@NotNull Runnable task) {
        lastInvokeTimeMs = System.currentTimeMillis();
        EDTInvoker.invokeLater(() -> {
            long currentTimeMs = System.currentTimeMillis();
            long invokeExpirationTimeMs = lastInvokeTimeMs + UPDATE_THRESHOLD - 10;
            long updateExpirationTimeMs = lastUpdateTimeMs + UPDATE_THRESHOLD;
            if ((currentTimeMs >= invokeExpirationTimeMs) || (currentTimeMs >= updateExpirationTimeMs)) {
                task.run();
                lastUpdateTimeMs = currentTimeMs;
            }
        }, UPDATE_THRESHOLD);
    }

    public static Color getDefaultTreeBackgroundColor() {
        return JBColor.namedColor("Tree.background", new JBColor(0x555555, 0xbbbbbb));
    }


    @NotNull
    public static String toHtmlColorWithOptionalAlpha(@NotNull Color color) {
        return "#" + ColorUtil.toHex(color, color.getAlpha() < 255);
    }

}
