// ***********************************************
// This example commands.js shows you how to
// create various custom commands and overwrite
// existing commands.
//
// For more comprehensive examples of custom
// commands please read more here:
// https://on.cypress.io/custom-commands
// ***********************************************
import './commands'
import '@cypress/code-coverage/support'
import 'cypress-mochawesome-reporter/register'
// Declare the login command on the Cypress namespace
declare global {
  namespace Cypress {
    interface Chainable {
      /**
       * Custom command to log in a user
       * @param email - The email to use for login
       * @param password - The password to use for login
       * @param isAdmin - Whether the user should be logged in as admin
       * @example cy.login('user@example.com', 'password', false)
       */
      login(email: string, password: string, isAdmin?: boolean): void;
    }
  }
}

// Add custom login command
Cypress.Commands.add('login', (email: string, password: string, isAdmin: boolean = false) => {
  // Mock the login process
  cy.intercept('POST', '/api/auth/login', {
    body: {
      id       : 1,
      username : email,
      firstName: isAdmin ? 'Admin' : 'User',
      lastName : 'Test',
      admin    : isAdmin,
      token    : 'fake-jwt-token',
      type     : 'Bearer'
    },
  }).as('loginRequest');
  
  // Visit login page
  cy.visit('/login');
  
  // Fill login form
  cy.get('input[formControlName=email]').type(email);
  cy.get('input[formControlName=password]').type(password);
  cy.get('button[type=submit]').click();
  
  // Wait for login request to complete
  cy.wait('@loginRequest');
});

// Add other custom commands here

declare global {
  namespace Cypress {
    interface Chainable<Subject = any> {
      loginWithSessions(
        email: string, 
        password: string, 
        isAdmin?: boolean
      ): Chainable<Subject>
    }
  }
}

export {}; // Make this file a module

// In cypress/support/commands.ts
Cypress.Commands.add('loginWithSessions', (email: string, password: string, isAdmin = false) => {
  // 1. Intercept login
  cy.intercept('POST', '/api/auth/login', {
    body: {
      id       : isAdmin ? 1 : 2,
      username : email,
      firstName: 'Test',
      lastName : 'User',
      admin    : isAdmin,
      token    : 'fake-jwt-token',
      type     : 'Bearer'
    },
  }).as('loginRequest');
  
  // 2. Intercept sessions list
  cy.intercept('GET', '/api/session', {
    statusCode: 200,
    body: [
      {
        id         : 1,
        name       : 'Yoga Session 1',
        description: 'A relaxing session',
        date       : '2023-06-15T10:00:00',
        teacher_id : 1,
        users      : [],
        createdAt  : '2023-01-01',
        updatedAt  : '2023-01-01'
      }
    ]
  }).as('getSessions');
  
  // 3. Login via UI
  cy.visit('/login');
  cy.get('input[formControlName=email]').type(email);
  cy.get('input[formControlName=password]').type(password);
  cy.get('button[type=submit]').click();
  cy.wait('@loginRequest');
  
  // 4. Verify navigation to sessions page
  cy.url().should('include', '/sessions');
});
  
  // Add more custom commands as needed