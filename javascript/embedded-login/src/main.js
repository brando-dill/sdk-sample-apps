import * as forgerock from '@forgerock/javascript-sdk';

/*
 * @forgerock/javascript-sdk
 *
 * index.html
 *
 * Copyright (c) 2020 - 2025 Ping Identity Corporation. All rights reserved.
 * This software may be modified and distributed under the terms
 * of the MIT license. See the LICENSE file for details.
 */

const FATAL = 'Fatal';
await forgerock.Config.setAsync({
 serverConfig: {
     // The OIDC well known endpoint
     wellknown: 'https://openam-as-de2.forgeblocks.com/am/oauth2/alpha/.well-known/openid-configuration',
   },
   // Client ID for the OAuth2 Client.
   clientId: 'BrandoTest',
   // By default, all scopes configured in the client are added,
   // modify as needed.
   scope: 'openid profile email address phone',
   // Redirect URI back to client, be default, uses the
   // location origin. Update if you have another
   // redirect URI to add.
   redirectUri: `${window.location.origin}/callback.html`
});

// Define custom handlers to render and submit each expected step
const handlers = {
  UsernamePassword: (step) => {
    const panel = document.querySelector('#UsernamePassword');
    panel.querySelector('.btn').addEventListener('click', () => {
      const nameCallback = step.getCallbackOfType('NameCallback');
      const passwordCallback = step.getCallbackOfType('PasswordCallback');
      nameCallback.setName(panel.querySelector('input[name=username]').value);
      passwordCallback.setPassword(panel.querySelector('input[type=password]').value);
      nextStep(step);
    });
  },
  Error: (step) => {
    document.querySelector('#Error span').innerHTML = step.getCode();
  },
  [FATAL]: (step) => {},
};

// Show only the view for this handler
const showStep = (handler) => {
  document.querySelectorAll('#steps > div').forEach((x) => x.classList.remove('active'));
  const panel = document.getElementById(handler);
  if (!panel) {
    console.error(`No panel with ID "${handler}"" found`);
    return false;
  }
  document.getElementById(handler).classList.add('active');
  return true;
};

const showUser = (user) => {
  document.querySelector('#User pre').innerHTML = JSON.stringify(user, null, 2);
  const panel = document.querySelector('#User');
  panel.querySelector('.btn').addEventListener('click', () => {
    logout();
  });
  showStep('User');
};

const getStage = (step) => {
  // Check if the step contains callbacks for capturing username and password
  const usernameCallbacks = step.getCallbacksOfType('NameCallback');
  const passwordCallbacks = step.getCallbacksOfType('PasswordCallback');

  if (usernameCallbacks.length && passwordCallbacks.length) {
    return 'UsernamePassword';
  }

  return undefined;
};

// Display and bind the handler for this stage
const handleStep = async (step) => {
  switch (step.type) {
    case 'LoginSuccess': {
      if (goto != null) {
        window.location.replace(goto);
        return;
      } else {
        // If we have a session token, get user information

        // eslint-disable-next-line no-unused-vars
        const sessionToken = step.getSessionToken();
        // eslint-disable-next-line no-unused-vars
        const tokens = await forgerock.TokenManager.getTokens();
        const user = await forgerock.UserManager.getCurrentUser();
        return showUser(user);
      }
    }

    case 'LoginFailure': {
      showStep('Error');
      handlers.Error(step);
      return;
    }

    default: {
      const stage = getStage(step) || FATAL;
      if (!showStep(stage)) {
        showStep(FATAL);
        handlers[FATAL](step);
      } else {
        handlers[stage](step);
      }
    }
  }
};

const handleFatalError = (err) => {
  console.error('Fatal error', err);
  showStep(FATAL);
};

// Get the next step using the FRAuth API
const nextStep = (step) => {
  forgerock.FRAuth.next(step).then(handleStep).catch(handleFatalError);
};

const logout = async () => {
  try {
    await forgerock.FRUser.logout();
    location.reload(true);
  } catch (error) {
    console.error(error);
  }
};

// Check URL for query parameters
const url = new URL(document.location);
const params = url.searchParams;
const goto = params.get('goto');

// Begin the login flow
nextStep();

document.getElementById('Error').addEventListener('click', nextStep);
document.getElementById('start-over').addEventListener('click', nextStep);
document.getElementById('Fatal').addEventListener('click', nextStep);
