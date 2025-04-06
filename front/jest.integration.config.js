const baseConfig = require('./jest.config');

module.exports = {
  ...baseConfig,
  testMatch: [
    '**/services/**/*.spec.ts',
    '**/components/**/detail.component.spec.ts',
    '**/components/**/form.component.spec.ts',
    '**/components/**/login.component.spec.ts',
    '**/components/**/register.component.spec.ts',
    '**/components/**/list.component.spec.ts',
    '**/components/**/me.component.spec.ts', // Added me.component
    '**/app.component.spec.ts'
  ],
  testNamePattern: '[Integration]'
};