/*
 * Copyright (c) 2020 - 2025 Ping Identity Corporation. All rights reserved.
 *
 * This software may be modified and distributed under the terms
 * of the MIT license. See the LICENSE file for details.
 */

package org.forgerock.auth;

import android.net.Uri;

import androidx.annotation.NonNull;

import org.forgerock.android.auth.Action;
import org.forgerock.android.auth.FRRequestInterceptor;
import org.forgerock.android.auth.Request;
import org.forgerock.android.auth.RequestInterceptor;

import static org.forgerock.android.auth.Action.START_AUTHENTICATE;

/**
 * Sample {@link RequestInterceptor} to add ForceAuth
 */
public class ForceAuthRequestInterceptor implements FRRequestInterceptor<Action> {

    @NonNull
    @Override
    public Request intercept(@NonNull Request request, Action tag) {
        if (tag.getType().equals(START_AUTHENTICATE)) {
            return request.newBuilder()
                    .url(Uri.parse(request.url().toString())
                            .buildUpon()
                            .appendQueryParameter("ForceAuth", "true").toString())
                    .build();
        }
        return request;
    }
}
