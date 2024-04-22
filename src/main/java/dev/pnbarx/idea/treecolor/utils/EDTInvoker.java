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

import com.intellij.openapi.Disposable;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.util.concurrency.Invoker;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.concurrency.CancellablePromise;

public class EDTInvoker {

    private static final Logger LOG = Logger.getInstance(EDTInvoker.class);
    private static final Disposable dummyDisposable = () -> LOG.debug("DummyDisposable disposed");

    public static CancellablePromise<?> invoke(@NotNull Runnable task) {
        return invoke(dummyDisposable, task);
    }

    public static CancellablePromise<?> invoke(@NotNull Disposable parent, @NotNull Runnable task) {
        return Invoker.forEventDispatchThread(parent).invoke(task);
    }

    public static CancellablePromise<?> invokeLater(@NotNull Runnable task, int delay) {
        return invokeLater(dummyDisposable, task, delay);
    }

    public static CancellablePromise<?> invokeLater(@NotNull Disposable parent, @NotNull Runnable task, int delay) {
        return Invoker.forEventDispatchThread(parent).invokeLater(task, delay);
    }
}
