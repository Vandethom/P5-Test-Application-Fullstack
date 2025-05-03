describe('Register Feature', () => {
  beforeEach(() => {
    cy.visit('/register');
  });

  it('should successfully register a new user', () => {
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

  it('should show error when registration fails (email already exists)', () => {
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

  it('should disable submit button when form is incomplete', () => {
    // Empty form
    cy.get('button[type=submit]').should('be.disabled');
    
    // Fill in email only - button should remain disabled
    cy.get('input[formControlName=email]').type("test@email.com");
    cy.get('button[type=submit]').should('be.disabled');
    
    // Add firstName but keep other fields empty
    cy.get('input[formControlName=firstName]').type("John");
    cy.get('button[type=submit]').should('be.disabled');
    
    // Add lastName but still missing password
    cy.get('input[formControlName=lastName]').type("Doe");
    cy.get('button[type=submit]').should('be.disabled');
    
    // Fill password to make form valid
    cy.get('input[formControlName=password]').type("password123");
    cy.get('button[type=submit]').should('not.be.disabled');
  });
  
  it('should validate email format', () => {
    // invalid email
    cy.get('input[formControlName=email]').type("invalidemail");
    cy.get('input[formControlName=email]').blur();
    
    // Fill in other required fields to isolate email validation
    cy.get('input[formControlName=firstName]').type('Test');
    cy.get('input[formControlName=lastName]').type('User');
    cy.get('input[formControlName=password]').type('Password123');
    
    // The form should be invalid with an invalid email
    cy.get('button[type=submit]').should('be.disabled');
    
    // valid email to check if the button becomes enabled
    cy.get('input[formControlName=email]').clear();
    cy.get('input[formControlName=email]').type("valid@example.com");
    cy.get('button[type=submit]').should('be.enabled');
  });
});