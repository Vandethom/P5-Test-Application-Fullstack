describe('Sessions', () => {
    beforeEach(() => {
      // Login as admin
      cy.login('admin@yoga.com', 'admin123');
      cy.visit('/sessions');
    });
    
    it('Displays sessions list', () => {
      cy.intercept('GET', '/api/session', { fixture: 'sessions.json' }).as('getSessions');
      cy.wait('@getSessions');
      
      cy.get('.session-card').should('have.length.greaterThan', 0);
    });
    
    it('Creates a new session', () => {
      cy.intercept('GET', '/api/teacher', { fixture: 'teachers.json' }).as('getTeachers');
      cy.intercept('POST', '/api/session', { statusCode: 201 }).as('createSession');
      
      cy.contains('Create').click();
      cy.url().should('include', '/sessions/form');
      
      cy.get('input[formControlName=name]').type('New Yoga Session');
      cy.get('textarea[formControlName=description]').type('Description for the new session');
      cy.get('input[formControlName=date]').type('2025-12-31');
      cy.get('mat-select[formControlName=teacher_id]').click();
      cy.get('mat-option').first().click();
      
      cy.get('button[type=submit]').click();
      cy.wait('@createSession');
      
      cy.url().should('include', '/sessions');
    });
    
    it('Views session details', () => {
      cy.intercept('GET', '/api/session/1', { fixture: 'session-detail.json' }).as('getSessionDetail');
      
      cy.get('.session-card').first().click();
      cy.url().should('include', '/sessions/detail/');
      
      cy.contains('h1', 'Session Details').should('be.visible');
      cy.contains('button', 'Participate').should('be.visible');
    });
    
    it('Updates a session (admin only)', () => {
      cy.intercept('GET', '/api/session/1', { fixture: 'session-detail.json' }).as('getSessionDetail');
      cy.intercept('PUT', '/api/session/1', { statusCode: 200 }).as('updateSession');
      
      cy.visit('/sessions/detail/1');
      cy.contains('button', 'Edit').click();
      
      cy.get('input[formControlName=name]').clear().type('Updated Session Name');
      cy.get('button[type=submit]').click();
      
      cy.wait('@updateSession');
      cy.url().should('include', '/sessions');
    });
    
    it('Deletes a session (admin only)', () => {
      cy.intercept('DELETE', '/api/session/*', { statusCode: 200 }).as('deleteSession');
      
      cy.visit('/sessions/detail/1');
      cy.contains('button', 'Delete').click();
      
      cy.get('.confirmation-dialog').should('be.visible');
      cy.get('.confirmation-dialog').contains('button', 'Yes').click();
      
      cy.wait('@deleteSession');
      cy.url().should('include', '/sessions');
    });
    
    it('Allows user to participate in a session', () => {
      cy.login('user@yoga.com', 'user123'); // Regular user
      
      cy.intercept('GET', '/api/session/1', { fixture: 'session-detail.json' }).as('getSessionDetail');
      cy.intercept('POST', '/api/session/1/participate/*', { statusCode: 200 }).as('participateSession');
      
      cy.visit('/sessions/detail/1');
      cy.contains('button', 'Participate').click();
      
      cy.wait('@participateSession');
      cy.contains('button', 'Do not participate anymore').should('be.visible');
    });
  });