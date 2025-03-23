describe('Hello World Test', () => {
    it('visits the application and checks for expected elements', () => {
        cy.visit('http://localhost:3000');
        cy.contains('Welcome to the application');
        cy.get('button').should('be.visible');
    });
});