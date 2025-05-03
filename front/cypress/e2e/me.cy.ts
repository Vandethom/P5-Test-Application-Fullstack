describe('me spec', () => {
  it('should display user information', () => {
    const user = {
      id: 1,
      email: 'yoga@studio.com',
      lastName: 'Studio',
      firstName: 'Yoga',
      admin: false,
      createdAt: new Date().toISOString(),
      updatedAt: new Date().toISOString(),
    };

    cy.intercept('POST', '/api/auth/login', {
      statusCode: 200,
      body: {
        token: 'fake-jwt-token',
        type: 'Bearer',
        id: 1,
        username: 'yoga@studio.com',
        firstName: 'Yoga',
        lastName: 'Studio',
        admin: false
      }
    }).as('loginRequest');

    cy.intercept('GET', '/api/auth/validate', {
      statusCode: 200,
      body: { valid: true }
    }).as('validateAuth');

    cy.intercept('GET', '/api/user/1', {
      statusCode: 200,
      body: user
    }).as('userRequest');

    cy.intercept('GET', '/api/session', {
      statusCode: 200,
      body: []
    }).as('sessionsRequest');

    cy.visit('/login');
    cy.get('input[formControlName=email]').type('yoga@studio.com');
    cy.get('input[formControlName=password]').type('test!1234');
    cy.get('button[type="submit"]').click();

    cy.wait('@loginRequest');
    
    // Set the session properly via window object
    cy.window().then(win => {
      win.localStorage.setItem('token', 'fake-jwt-token');
      
      const appRoot = win.document.querySelector('app-root');
      if (appRoot) {
        const appComponent = win.ng.getComponent(appRoot);
        if (appComponent && appComponent.sessionService) {
          appComponent.sessionService.logIn({
            token: 'fake-jwt-token',
            type: 'Bearer',
            id: 1,
            username: 'yoga@studio.com',
            firstName: 'Yoga',
            lastName: 'Studio',
            admin: false
          });
        }
      }
    });

    cy.url().should('include', '/sessions');
    
    cy.get('mat-toolbar').contains('Account').click();
    
    cy.wait('@userRequest');
    
    cy.contains('h1', 'User information').should('be.visible');
    cy.contains('Name:').should('be.visible');
    cy.contains('Email:').should('be.visible');
    cy.contains('Delete').should('be.visible');
  });
});