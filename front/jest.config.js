module.exports = {
  moduleNameMapper: {
    '@core/(.*)': '<rootDir>/src/app/core/$1',
    '\\.(html)$': '<rootDir>/src/test-mocks/html-mock.js',
    '\\.(scss|css)$': '<rootDir>/src/test-mocks/style-mock.js'
  },
  preset: 'jest-preset-angular',
  setupFilesAfterEnv: ['<rootDir>/setup-jest.ts'],
  testEnvironment: 'jsdom',
  bail: false,
  verbose: false,
  collectCoverage: false,
  coverageDirectory: './coverage/jest',
  testPathIgnorePatterns: ['<rootDir>/node_modules/'],
  coveragePathIgnorePatterns: ['<rootDir>/node_modules/'],
  coverageThreshold: {
    global: {
      statements: 80
    },
  },
  roots: [
    "<rootDir>"
  ],
  modulePaths: [
    "<rootDir>"
  ],
  moduleDirectories: [
    "node_modules"
  ]
};
