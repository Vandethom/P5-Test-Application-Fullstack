describe('Session Details', () => {
    beforeEach(() => {
      cy.login('user@yoga.com', 'user123');
    });
    
    it('Views session details', () => {
      cy.intercept('GET', '/api/session/1', { fixture: 'session-detail.json' }).as('getSessionDetail');
      
      cy.visit('/sessions/detail/1');
      cy.wait('@getSessionDetail');
      
      cy.contains('Session Details').should('be.visible');
    });
    
    it('Allows user to participate in a session', () => {
      cy.intercept('GET', '/api/session/1', { fixture: 'session-detail.json' }).as('getSessionDetail');
      cy.intercept('POST', '/api/session/1/participate/*', { statusCode: 200 }).as('participateSession');
      
      cy.visit('/sessions/detail/1');
      cy.contains('button', 'Participate').click();
      
      cy.wait('@participateSession');
      cy.contains('button', 'Do not participate anymore').should('be.visible');
    });
    
    // More session detail tests
  });