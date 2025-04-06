/// <reference types="cypress" />

declare namespace Cypress {
  interface Chainable<Subject> {
    /**
     * Custom command to login to the application
     * @example cy.login('user@example.com', 'password123')
     */
    login(email: string, password: string): Chainable<Element>
    
    // Add more custom command type definitions here
  }
}