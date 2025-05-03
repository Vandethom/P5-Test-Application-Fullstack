/// <reference types="cypress" />

describe('detail spec', () => {
    let sessions = [
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
    ];
  
    const teacher = {
      id       : 1,
      lastName : 'lastName',
      firstName: 'firstName',
      createdAt: new Date(),
      updatedAt: new Date(),
    };
  
    it('should display detail session for admin', () => {
  
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
  
      cy.intercept(
        {
          method: 'GET',
          url   : '/api/session/1',
        },
        sessions[0]
      );
  
      cy.intercept(
        {
          method: 'GET',
          url   : '/api/teacher/1',
        },
        teacher
      );
  
      cy.contains('button', 'Detail').click();
  
      cy.url().should('include', '/sessions/detail/1');
  
      cy.contains('h1', 'Yoga').should('be.visible');
      cy.contains(`${teacher.firstName} ${teacher.lastName.toUpperCase()}`).should('be.visible');
      cy.contains('button', 'Delete').should('be.visible');
      cy.contains('button', 'Participate').should('not.exist');
      cy.get('img').should('be.visible');
      cy.contains('yoga for beginners').should('be.visible');
    });
  
  
    it('should display detail session for user', () => {
  
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
  
      cy.intercept(
        {
          method: 'GET',
          url   : '/api/session/1',
        },
        sessions[0]
      );
  
      cy.intercept(
        {
          method: 'GET',
          url   : '/api/teacher/1',
        },
        teacher
      );
  
      cy.contains('button', 'Detail').click();
  
      cy.url().should('include', '/sessions/detail/1');
  
      cy.contains('h1', 'Yoga').should('be.visible');
      cy.contains(`${teacher.firstName} ${teacher.lastName.toUpperCase()}`).should('be.visible');
      cy.contains('button', 'Delete').should('not.exist');
      cy.contains('button', 'Participate').should('be.visible');
      cy.get('img').should('be.visible');
      cy.contains('yoga for beginners').should('be.visible');
    });
  
  
    it('should display detail session for user who participate', () => {
  
      sessions = [
        {
          id         : 1,
          name       : 'yoga',
          description: 'yoga for beginners',
          date       : new Date(),
          teacher_id : 1,
          users      : [1],
          createdAt  : new Date(),
          updatedAt  : new Date(),
        },
      ];
  
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
  
      cy.intercept(
        {
          method: 'GET',
          url   : '/api/session/1',
        },
        sessions[0]
      );
  
      cy.intercept(
        {
          method: 'GET',
          url   : '/api/teacher/1',
        },
        teacher
      );
  
      cy.contains('button', 'Detail').click();
  
      cy.url().should('include', '/sessions/detail/1');
  
      cy.contains('h1', 'Yoga').should('be.visible');
      cy.contains(`${teacher.firstName} ${teacher.lastName.toUpperCase()}`).should('be.visible');
      cy.contains('button', 'Delete').should('not.exist');
      cy.contains('button', 'Participate').should('not.exist');
      cy.contains('button', 'Do not participate').should('be.visible');
      cy.contains('1 attendees').should('be.visible');
      cy.get('img').should('be.visible');
      cy.contains('yoga for beginners').should('be.visible');
    });
  
  
    it('should display "Do not participate" button and add a participation when user click on participate', () => {
  
      sessions = [
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
      ];
  
      let updatedSession = [
        {
          id         : 1,
          name       : 'yoga',
          description: 'yoga for beginners',
          date       : new Date(),
          teacher_id : 1,
          users      : [1],
          createdAt  : new Date(),
          updatedAt  : new Date(),
        },
      ];
  
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
  
      cy.intercept(
        {
          method: 'GET',
          url   : '/api/session/1',
        },
        sessions[0]
      );
  
      cy.intercept(
        {
          method: 'GET',
          url   : '/api/teacher/1',
        },
        teacher
      );
  
      cy.contains('button', 'Detail').click();
  
      cy.url().should('include', '/sessions/detail/1');
  
      cy.contains('button', 'Participate').should('be.visible');
      cy.contains('button', 'Do not participate').should('not.exist');
      cy.contains('0 attendees').should('be.visible');
  
      cy.intercept(
        {
          method: 'POST',
          url   : '/api/session/1/participate/1',
        },
        []
      );
  
      cy.intercept(
        {
          method: 'GET',
          url   : '/api/session/1',
        },
        updatedSession[0]
      );
  
      cy.intercept(
        {
          method: 'GET',
          url   : '/api/teacher/1',
        },
        teacher
      );
  
      cy.contains('button', 'Participate').click();
      cy.contains('button', 'Do not participate').should('be.visible');
      cy.contains('button', 'Participate').should('not.exist');
      cy.contains('1 attendees').should('be.visible');
    });
  
  
    it('should display Participate button and withdraw a participation when user click on Unparticipate', () => {
  
      sessions = [
        {
          id         : 1,
          name       : 'yoga',
          description: 'yoga for beginners',
          date       : new Date(),
          teacher_id : 1,
          users      : [1],
          createdAt  : new Date(),
          updatedAt  : new Date(),
        },
      ];
  
      let updatedSession = [
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
      ];
  
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
  
      cy.intercept(
        {
          method: 'GET',
          url   : '/api/session/1',
        },
        sessions[0]
      );
  
      cy.intercept(
        {
          method: 'GET',
          url   : '/api/teacher/1',
        },
        teacher
      );
  
      cy.contains('button', 'Detail').click();
  
      cy.url().should('include', '/sessions/detail/1');
  
      cy.contains('button', 'Participate').should('not.exist');
      cy.contains('button', 'Do not participate').should('be.visible');
      cy.contains('1 attendees').should('be.visible');
  
      cy.intercept(
        {
          method: 'DELETE',
          url   : '/api/session/1/participate/1',
        },
        []
      );
  
      cy.intercept(
        {
          method: 'GET',
          url   : '/api/session/1',
        },
        updatedSession[0]
      );
  
      cy.intercept(
        {
          method: 'GET',
          url   : '/api/teacher/1',
        },
        teacher
      );
  
      cy.contains('button', 'Do not participate').click();
      cy.contains('button', 'Participate').should('be.visible');
      cy.contains('button', 'Do not participate').should('not.exist');
      cy.contains('0 attendees').should('be.visible');
    });
  
  
    it('should delete session and redirect to the session list page', () => {
  
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
  
      cy.intercept(
        {
          method: 'GET',
          url   : '/api/session/1',
        },
        sessions[0]
      );
  
      cy.intercept(
        {
          method: 'GET',
          url   : '/api/teacher/1',
        },
        teacher
      );
  
      cy.contains('button', 'Detail').click();
  
      cy.url().should('include', '/sessions/detail/1');
  
      cy.intercept(
        {
          method: 'DELETE',
          url   : '/api/session/1',
        },
        []
      );
  
      cy.intercept(
        {
          method: 'GET',
          url   : '/api/session',
        },
        []
      );
  
      cy.contains('button', 'Delete').click();
      cy.contains('snack-bar-container', 'Session deleted !').should('be.visible');
      cy.url().should('include', '/sessions');
    });
  });