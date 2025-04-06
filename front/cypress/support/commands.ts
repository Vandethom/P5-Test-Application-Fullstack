// Custom command for login
Cypress.Commands.add('login', (email, password) => {
    cy.intercept('POST', '/api/auth/login', {
      body: {
        id: email.includes('admin') ? 1 : 2,
        username: email,
        firstName: 'Test',
        lastName: 'User',
        admin: email.includes('admin'),
        token: 'fake-token',
        type: 'Bearer'
      },
    }).as('loginRequest');
  
    cy.visit('/login');
    cy.get('input[formControlName=email]').type(email);
    cy.get('input[formControlName=password]').type(password);
    cy.get('button[type=submit]').click();
    cy.wait('@loginRequest');
  });
  
  // Add more custom commands as needed