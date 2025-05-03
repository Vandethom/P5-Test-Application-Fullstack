describe('form spec', () => {
    let session = {
      id         : 1,
      name       : 'yoga',
      description: 'yoga for beginners',
      date       : new Date('2025-08-25'),
      teacher_id : 1,
      users      : [],
      createdAt  : new Date(),
      updatedAt  : new Date(),
    };
    const teacher = {
      id: 1,
      lastName : 'teacherLastName',
      firstName: 'teacherFirstName',
      createdAt: new Date(),
      updatedAt: new Date(),
    };
  
  
    it('should create a session', () => {
  
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
        []
      );
  
      cy.get('input[formControlName=email]').type('yoga@studio.com');
      cy.get('input[formControlName=password]').type(`${'test!1234'}{enter}{enter}`);
  
      cy.url().should('include', '/sessions');
  
      cy.intercept(
        {
          method: 'GET',
          url   : '/api/teacher',
        },
        [teacher]
      );
  
      cy.contains('button', 'Create').click();
  
      cy.url().should('include', '/create');
  
      cy.get('input[formControlName=name]').type('yoga');
      cy.get('input[formControlName=date]').type('2025-08-25');
      cy.get('mat-select').click();
      cy.get('mat-option').contains('teacherFirstName teacherLastName').click();
      cy.get('textarea[formControlName=description]').type('yoga for beginners');
  
      cy.intercept(
        {
          method: 'POST',
          url   : '/api/session',
        },
        session
      );
  
      cy.intercept(
        {
          method: 'GET',
          url   : '/api/session',
        },
        [session]
      );
  
      cy.contains('button', 'Save').click();
      cy.url().should('include', '/sessions');
      cy.contains('snack-bar-container', 'Session created !').should('be.visible');
      cy.contains('yoga').should('be.visible');
      cy.contains('yoga for beginners').should('be.visible');
    });
  
  
    it('should update a session', () => {
  
      let sessionUpdated = {
        id         : 1,
        name       : 'relaxation',
        description: 'relaxation for beginners',
        date       : new Date('2025-08-25'),
        teacher_id : 1,
        users      : [],
        createdAt  : new Date(),
        updatedAt  : new Date(),
      };
  
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
        [session]
      );
  
      cy.get('input[formControlName=email]').type('yoga@studio.com');
      cy.get('input[formControlName=password]').type(`${'test!1234'}{enter}{enter}`);
  
      cy.url().should('include', '/sessions');
  
      cy.intercept(
        {
          method: 'GET',
          url   : '/api/session/1',
        },
        session
      );
  
      cy.intercept(
        {
          method: 'GET',
          url   : '/api/teacher',
        },
        [teacher]
      );
  
      cy.contains('button', 'Edit').click();
  
      cy.url().should('include', '/sessions/update/1');
  
      cy.get('input[formControlName=name]').should('have.value', 'yoga');
      cy.get('input[formControlName=date]').should('have.value', '2025-08-25');
      cy.get('mat-select').click();
      cy.get('mat-option').contains('teacherFirstName teacherLastName').click();
      cy.get('textarea[formControlName=description]').should('have.value','yoga for beginners');
  
      cy.get('input[formControlName=name]').clear();
      cy.get('input[formControlName=name]').type('relaxation');
      cy.get('input[formControlName=name]').should('have.value', 'relaxation');
      cy.get('textarea[formControlName=description]').clear();
      cy.get('textarea[formControlName=description]').type('relaxation for beginners');
      cy.get('textarea[formControlName=description]').should('have.value','relaxation for beginners');
  
      cy.intercept(
        {
          method: 'PUT',
          url   : '/api/session/1',
        },
        sessionUpdated
      );
  
      cy.intercept(
        {
          method: 'GET',
          url: '/api/session',
        },
        [sessionUpdated]
      );
  
      cy.contains('button', 'Save').click();
      cy.url().should('include', '/sessions');
      cy.contains('snack-bar-container', 'Session updated !').should('be.visible');
      cy.contains('relaxation').should('be.visible');
      cy.contains('relaxation for beginners').should('be.visible');
    });
  });