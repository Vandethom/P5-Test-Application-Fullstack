/// <reference types="cypress" />

describe('Login Feature', () => {
  beforeEach(() => {
    cy.visit('/login');
  });
  
  it('should successfully log in with valid credentials', () => {
    cy.intercept('POST', '/api/auth/login', {
      body: {
        id       : 1,
        username : 'yoga@studio.com',
        firstName: 'Admin',
        lastName : 'User',
        admin    : true,
        token    : 'fake-jwt-token',
        type     : 'Bearer'
      },
    }).as('loginRequest');

    cy.intercept('GET', '/api/session', []).as('sessionsRequest');

    cy.get('input[formControlName=email]').type("yoga@studio.com");
    cy.get('input[formControlName=password]').type("test!1234");
    cy.get('button[type=submit]').click();

    cy.wait('@loginRequest').its('response.statusCode').should('eq', 200);
    cy.wait('@sessionsRequest').its('response.statusCode').should('eq', 200);
    cy.url().should('include', '/sessions');
  });

  it('should show error message with invalid credentials', () => {
    cy.intercept('POST', '/api/auth/login', {
      statusCode: 401,
      body: { message: 'Invalid credentials' }
    }).as('failedLogin');

    cy.get('input[formControlName=email]').type("wrong@email.com");
    cy.get('input[formControlName=password]').type("wrongpassword");
    cy.get('button[type=submit]').click();

    cy.wait('@failedLogin').its('response.statusCode').should('eq', 401);
    cy.contains('An error occurred').should('be.visible');
  });

  it('should disable submit button when form is incomplete', () => {
    // Empty form
    cy.get('button[type=submit]').should('be.disabled');
    
    // Email only
    cy.get('input[formControlName=email]').type("test@email.com");
    cy.get('button[type=submit]').should('be.disabled');
    
    // Password only
    cy.get('input[formControlName=email]').clear();
    cy.get('input[formControlName=password]').type("password123");
    cy.get('button[type=submit]').should('be.disabled');
    
    cy.get('input[formControlName=email]').type("test@email.com");
    cy.get('button[type=submit]').should('not.be.disabled');
  });
  
  it('should show validation errors for invalid email', () => {
    cy.get('input[formControlName=email]').type("invalidemail");
    
    cy.get('input[formControlName=email]').blur();
    cy.get('input[formControlName=password]').click();
    
    // examine what's actually in the form to debug
    cy.get('form').then($form => {
      cy.log('Form HTML:', $form.html());
    });
    
    cy.get('input[formControlName=email].ng-invalid').should('exist');
    cy.get('form').contains(/invalid|email|format/i).should('be.visible');
    cy.get('input[formControlName=password]').type("Password123");
    cy.get('button[type=submit]').should('be.disabled');
  });
});