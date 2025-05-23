/*
 * Copyright (c) 2024 - 2025 Ping Identity Corporation. All rights reserved.
 *
 * This software may be modified and distributed under the terms
 * of the MIT license. See the LICENSE file for details.
 */


package com.example.app

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

/**
 * Alert component to show the alert dialog.
 * @param throwable Throwable the throwable object.
 */
@Composable
fun Alert(throwable: Throwable) {

    var showConfirmation by remember {
        mutableStateOf(true)
    }

    if (showConfirmation) {
        AlertDialog(
            onDismissRequest = { showConfirmation = false },
            confirmButton = {},
            dismissButton = {
                TextButton(onClick = { showConfirmation = false })
                { Text(text = "Ok") }
            },
            text = {
                Text(text = throwable.toString())
            }
        )
    }
}

@Composable
fun Alert(throwable: Throwable?, onDismiss: () -> Unit) {
    throwable?.let {
        AlertDialog(
            onDismissRequest = { onDismiss() },
            confirmButton = {},
            dismissButton = {
                TextButton(onClick = { onDismiss() })
                { Text(text = "Ok") }
            },
            text = {
                Text(text = throwable.toString())
            }
        )
    }
}

@Composable
fun Alert(msg: String, onOK: () -> Unit) {

    var showConfirmation by remember {
        mutableStateOf(true)
    }

    if (showConfirmation) {
        AlertDialog(
            onDismissRequest = { showConfirmation = false },
            confirmButton = {},
            dismissButton = {
                TextButton(onClick = {
                    showConfirmation = false
                    onOK()
                })
                { Text(text = "Ok") }
            },
            text = {
                Text(text = msg)
            }
        )
    }
}