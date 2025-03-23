describe('Register spec', () => {
    beforeEach(() => {
      cy.visit('/register');
    });
  
    it('Registers a new user successfully', () => {
      cy.intercept('POST', '/api/auth/register', {
        statusCode: 200
      }).as('registerRequest');
  
      cy.get('input[formControlName=firstName]').type('John');
      cy.get('input[formControlName=lastName]').type('Doe');
      cy.get('input[formControlName=email]').type('john.doe@example.com');
      cy.get('input[formControlName=password]').type('password123');
      cy.get('button[type=submit]').click();
  
      cy.wait('@registerRequest');
      cy.url().should('include', '/login');
    });
  
    it('Shows error when registration fails', () => {
      cy.intercept('POST', '/api/auth/register', {
        statusCode: 400,
        body: {
          message: 'Email already exists'
        }
      }).as('failedRegister');
  
      cy.get('input[formControlName=firstName]').type('John');
      cy.get('input[formControlName=lastName]').type('Doe');
      cy.get('input[formControlName=email]').type('existing@user.com');
      cy.get('input[formControlName=password]').type('password123');
      cy.get('button[type=submit]').click();
  
      cy.wait('@failedRegister');
      cy.contains('An error occurred').should('be.visible');
    });
  
    it('Shows validation errors for empty fields', () => {
      cy.get('button[type=submit]').click();
      cy.get('button[type=submit]').should('be.disabled');
      
      // Fill out only some fields
      cy.get('input[formControlName=firstName]').type('John');
      cy.get('button[type=submit]').should('be.disabled');
      
      cy.get('input[formControlName=lastName]').type('Doe');
      cy.get('button[type=submit]').should('be.disabled');
      
      cy.get('input[formControlName=email]').type('john.doe@example.com');
      cy.get('button[type=submit]').should('be.disabled');
    });
  });