import cypress from 'cypress';

describe('Login spec', () => {
  beforeEach(() => {
    cy.visit('/login');
  });
  
  it('Login successful', () => {
    cy.intercept('POST', '/api/auth/login', {
      body: {
        id       : 1,
        username : 'userName',
        firstName: 'firstName',
        lastName : 'lastName',
        admin    : true,
        token    : 'fake-token',
        type     : 'Bearer'
      },
    }).as('loginRequest');

    cy.intercept('GET', '/api/session', []).as('sessionsRequest');

    cy.get('input[formControlName=email]').type("yoga@studio.com");
    cy.get('input[formControlName=password]').type("test!1234");
    cy.get('button[type=submit]').click();

    cy.wait('@loginRequest');
    cy.url().should('include', '/sessions');
  });

  it('Login fails with incorrect credentials', () => {
    cy.intercept('POST', '/api/auth/login', {
      statusCode: 401,
      body: {
        message: 'Invalid credentials'
      }
    }).as('failedLogin');

    cy.get('input[formControlName=email]').type("wrong@email.com");
    cy.get('input[formControlName=password]').type("wrongpassword");
    cy.get('button[type=submit]').click();

    cy.wait('@failedLogin');
    cy.contains('An error occurred').should('be.visible');
  });

  it('Shows validation errors for empty fields', () => {
    cy.get('button[type=submit]').click();
    cy.get('button[type=submit]').should('be.disabled');
    
    // Add email only
    cy.get('input[formControlName=email]').type("test@email.com");
    cy.get('button[type=submit]').should('be.disabled');
    
    // Clear email and add password only
    cy.get('input[formControlName=email]').clear();
    cy.get('input[formControlName=password]').type("password123");
    cy.get('button[type=submit]').should('be.disabled');
  });
});