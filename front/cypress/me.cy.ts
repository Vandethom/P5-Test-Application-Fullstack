describe('me spec', () => {
  it('should display user information', () => {
    const user = {
      id: 1,
      email: 'user@mail.com',
      lastName: 'lastName',
      firstName: 'firstName',
      admin: false,
      password: 'password',
      createdAt: new Date().toISOString(),
      updatedAt: new Date().toISOString()
    };

    // Set up intercepts BEFORE visiting page
    cy.intercept('POST', '/api/auth/login', {
      statusCode: 200,
      body: {
        token: 'fake-jwt-token',
        type: 'Bearer',
        id: 1,
        username: 'yoga@studio.com',
        firstName: 'firstName',
        lastName: 'lastName', 
        admin: true
      }
    }).as('loginRequest');
    
    // Intercept the sessions request to fix auth state
    cy.intercept('GET', '/api/session', []).as('sessionRequest');
    
    // Visit login page
    cy.visit('/login');
    
    // Fill and submit login form
    cy.get('input[formControlName=email]').type('yoga@studio.com');
    cy.get('input[formControlName=password]').type('test!1234');
    cy.get('button[type="submit"]').click();
    cy.wait('@loginRequest');
    
    // Wait for redirection - use alternative URL checks since redirect might vary
    cy.url().should('not.include', '/login');
    
    // IMPORTANT: Set local storage to ensure session persistence
    cy.window().then((win) => {
      win.localStorage.setItem('token', 'fake-jwt-token');
      // Also store user session info if your app uses it
      win.localStorage.setItem('user', JSON.stringify({id: 1, admin: true}));
    });
    
    // Now set up the user request intercept BEFORE navigation
    cy.intercept('GET', '/api/user/1', user).as('userRequest');
    
    // Try a direct visit to the Me page instead of navigation which might be unreliable
    cy.visit('/me');
    
    // Now we should see our mocked user request
    cy.wait('@userRequest', { timeout: 10000 });
    
    // Verify the content is displayed
    cy.contains(`Name: ${user.firstName} ${user.lastName.toUpperCase()}`).should('be.visible');
    cy.contains(`Email: ${user.email}`).should('be.visible');
  });
});