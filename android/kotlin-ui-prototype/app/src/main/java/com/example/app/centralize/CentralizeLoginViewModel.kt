/*
 * Copyright (c) 2023-2025 Ping Identity Corporation. All rights reserved.
 *
 *  This software may be modified and distributed under the terms
 *  of the MIT license. See the LICENSE file for details.
 */

package com.example.app.centralize

import androidx.browser.customtabs.CustomTabsIntent
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import net.openid.appauth.browser.BrowserDenyList
import net.openid.appauth.browser.VersionedBrowserMatcher
import org.forgerock.android.auth.FRListener
import org.forgerock.android.auth.FRUser

class CentralizeLoginViewModel : ViewModel() {

    var state = MutableStateFlow(CentralizeState())
        private set

    fun login(fragmentActivity: FragmentActivity) {
        FRUser.browser().appAuthConfigurer().customTabsIntent {
            it.setColorScheme(CustomTabsIntent.COLOR_SCHEME_DARK)
        }.appAuthConfiguration { appAuthConfiguration ->
            /*
            appAuthConfiguration.setBrowserMatcher(
                BrowserDenyList(
                    VersionedBrowserMatcher.CHROME_CUSTOM_TAB,
                    VersionedBrowserMatcher.CHROME_BROWSER
                )
            )
             */
        }
            .done()
            .login(fragmentActivity,
                object : FRListener<FRUser> {
                    override fun onSuccess(result: FRUser) {
                        state.update {
                            it.copy(user = result, exception = null)
                        }
                    }

                    override fun onException(e: Exception) {
                        state.update {
                            it.copy(user = null, exception = e)
                        }
                    }
                })
    }


}