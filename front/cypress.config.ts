import { defineConfig } from 'cypress'

export default defineConfig({
  projectId: '5w3cdg',
  videosFolder: 'cypress/videos',
  screenshotsFolder: 'cypress/screenshots',
  fixturesFolder: 'cypress/fixtures',
  video: false,
  reporter: 'cypress-mochawesome-reporter',
  reporterOptions: {
    charts: true,
    reportPageTitle: 'Yoga App E2E Tests',
    embeddedScreenshots: true,
    inlineAssets: true,
    saveAllAttempts: false,
    reportDir: 'cypress/reports/mocha',
    reportFilename: 'report',
    overwrite: false,
    html: false,
    json: true
  },
  env: {
    // ...other env vars
    codeCoverage: {
      outputDir: '.nyc_output/cypress',
      exclude: ['cypress/**/*.*']
    }
  },
  e2e: {
    // We've imported your old cypress plugins here.
    // You may want to clean this up later by importing these.
    setupNodeEvents(on, config) {
      require('@cypress/code-coverage/task')(on, config);
      require('cypress-mochawesome-reporter/plugin')(on);
      require('./cypress/plugins/index.ts').default(on, config)

      return config
    },
    baseUrl: 'http://localhost:4200',
    supportFile: 'cypress/support/e2e.ts'
  }
})