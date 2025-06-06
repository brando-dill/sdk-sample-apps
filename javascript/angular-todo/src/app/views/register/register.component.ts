/*
 * angular-todo-prototype
 *
 * register.component.ts
 *
 * Copyright (c) 2021 - 2025 Ping Identity Corporation. All rights reserved.
 * This software may be modified and distributed under the terms
 * of the MIT license. See the LICENSE file for details.
 */

import { Component } from '@angular/core';

/**
 * Used to show a registration page
 */
@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
})
export class RegisterComponent {
  isWebAuthn = false;

  onSetIsWebAuthn(isWebAuthn: boolean): void {
    this.isWebAuthn = isWebAuthn;
  }
}
