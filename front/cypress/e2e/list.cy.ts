describe('list spec', () => {
    const sessions = [
      {
        id         : 1,
        name       : 'yoga',
        description: 'yoga for beginners',
        date       : new Date(),
        teacher_id : 1,
        users      : [],
        createdAt  : new Date(),
        updatedAt  : new Date(),
      },
      {
        id         : 2,
        name       : 'hypnose',
        description: 'hypnose for beginners',
        date       : new Date(),
        teacher_id : 2,
        users      : [],
        createdAt  : new Date(),
        updatedAt  : new Date(),
      },
    ];
  
    it('should display sessions for admin', () => {
  
      cy.visit('/login');
  
      cy.intercept('POST', '/api/auth/login', {
        body: {
          id       : 1,
          username : 'userName',
          firstName: 'firstName',
          lastName : 'lastName',
          admin    : true,
        },
      });
  
      cy.intercept(
        {
          method: 'GET',
          url   : '/api/session',
        },
        sessions
      );
  
      cy.get('input[formControlName=email]').type('yoga@studio.com');
      cy.get('input[formControlName=password]').type(`${'test!1234'}{enter}{enter}`);
  
      cy.url().should('include', '/sessions');
  
      cy.get('button').contains('Create').should('be.visible');
  
      cy.contains('yoga').should('be.visible');
      cy.contains('yoga for beginners').should('be.visible');
  
      cy.contains('hypnose').should('be.visible');
      cy.contains('hypnose for beginners').should('be.visible');
  
      cy.get('img').should('have.length', 2);
      cy.get('mat-card.item > mat-card-actions > button:first-child').should('have.length',2);
      cy.get('mat-card.item > mat-card-actions > button:nth-child(2)').should('have.length',2);
    });
  
  
    it('should display sessions for user', () => {
  
      cy.visit('/login');
  
      cy.intercept('POST', '/api/auth/login', {
        body: {
          id       : 1,
          username : 'userName',
          firstName: 'firstName',
          lastName : 'lastName',
          admin    : false,
        },
      });
  
      cy.intercept(
        {
          method: 'GET',
          url   : '/api/session',
        },
        sessions
      );
  
      cy.get('input[formControlName=email]').type('yoga@studio.com');
      cy.get('input[formControlName=password]').type(`${'test!1234'}{enter}{enter}`);
  
      cy.url().should('include', '/sessions');
  
      cy.contains('button', 'create').should('not.exist');
  
      cy.contains('yoga').should('be.visible');
      cy.contains('yoga for beginners').should('be.visible');
  
      cy.contains('hypnose').should('be.visible');
      cy.contains('hypnose for beginners').should('be.visible');
  
      cy.get('img').should('have.length', 2);
      cy.get('mat-card.item > mat-card-actions > button:first-child').should('have.length',2);
      cy.contains('button', 'Edit').should('not.exist');
    });
  });