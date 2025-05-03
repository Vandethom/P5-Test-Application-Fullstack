describe('Logout Feature', () => {
  beforeEach(() => {
    // Clear application state
    cy.window().then((window) => {
      window.localStorage.clear();
      window.sessionStorage.clear();
    });

    // Mock login request
    cy.intercept('POST', '/api/auth/login', {
      body: {
        id       : 2,
        username : 'user@example.com',
        firstName: 'Regular',
        lastName : 'User',
        admin    : false,
        token    : 'fake-jwt-token',
        type     : 'Bearer',
        session  : {
          id      : 2,
          username: 'user@example.com',
          token   : 'fake-jwt-token',
        },
      },
    }).as('loginRequest');
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
      
      // Login
      cy.visit('/login');
      cy.get('input[formControlName=email]').type("user@example.com");
      cy.get('input[formControlName=password]').type("password123");
      cy.get('button[type=submit]').click();
      cy.wait('@loginRequest');
      cy.url().should('include', '/sessions');
    
    });
    
    it('should successfully log out when clicking logout button', () => {
      // Click logout
      cy.contains('Logout').should('be.visible');
      cy.contains('Logout').click();
      
      // Verify we're logged out and redirected
      cy.url().should('eq', 'http://localhost:4200/');
      
      // Check UI changes - login/register links should appear
      cy.contains('Login').should('be.visible');
      cy.contains('Register').should('be.visible');
      cy.contains('Logout').should('not.exist');
    });
  });