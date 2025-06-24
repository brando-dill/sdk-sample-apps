/*
 * Copyright (c) 2024 - 2025 Ping Identity Corporation. All rights reserved.
 *
 * This software may be modified and distributed under the terms
 * of the MIT license. See the LICENSE file for details.
 */

package com.pingidentity.samples.app.davinci

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pingidentity.davinci.DaVinci
import com.pingidentity.davinci.module.Oidc
import com.pingidentity.logger.Logger
import com.pingidentity.logger.STANDARD
import com.pingidentity.orchestrate.ContinueNode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * The DaVinci instance.
 */
//If using this code snippet for you own Android App and not the sample apps,
//modify details accordingly in the PingOne app.
val daVinci = DaVinci {
    logger = Logger.STANDARD

    // Oidc as module
    module(Oidc) {
        clientId = "c98a2eaf-483d-469c-9990-1cc8e1142f02"
        discoveryEndpoint = "https://auth.pingone.com/4b69e4ad-03bd-4203-89bb-0504221d9a1c/as/.well-known/openid-configuration"
        scopes = mutableSetOf("email", "phone", "profile", "address", "openid") // Alter redirect URI specific to your app
        redirectUri = "org.forgerock.demo://oauth2redirect" // Alter the scopes based on your client configuration
    }
}

/**
 * The view model for the DaVinci app. Holds the state of the app.
 */
class DaVinciViewModel : ViewModel() {
    var state = MutableStateFlow(DaVinciState())
        private set

    var loading = MutableStateFlow(false)
        private set

    /**
     * Initialize the DaVinci flow.
     */
    init {
        start()
    }

    /**
     * Call the next node in the DaVinci flow.
     *
     * @param current The current node.
     */
    fun next(current: ContinueNode) {
        loading.update {
            true
        }
        viewModelScope.launch {
            val next = current.next()
            state.update {
                it.copy(node = next)
            }
            loading.update {
                false
            }
        }
    }

    /**
     * Start the DaVinci flow.
     */
    fun start() {
        loading.update {
            true
        }
        viewModelScope.launch {
            val next = daVinci.start()

            state.update {
                it.copy(node = next)
            }
            loading.update {
                false
            }
        }
    }

    /**
     * Refresh the state of the DaVinci flow.
     */
    fun refresh() {
        state.update {
            it.copy(node = it.node)
        }
    }
}
