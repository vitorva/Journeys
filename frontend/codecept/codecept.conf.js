const {setHeadlessWhen} = require('@codeceptjs/configure');

// turn on headless mode when running with HEADLESS=true environment variable
// export HEADLESS=true && npx codeceptjs run
setHeadlessWhen(process.env.HEADLESS);

exports.config = {
  tests: 'e2e/*_test.js',
  output: './output',
  helpers: {
    Puppeteer: {
      url: 'http://myjourneys.ch',
      show: false,
      keepBrowserState: true,
      windowSize: '1200x900',
    },
    /*REST: {
      endpoint: 'http://myjourneys.ch/api',
      defaultHeaders: {
        'Auth': '111',
        'Content-Type': 'application/json',
        'Accept': 'application/json',
        'credentials': 'include'
      },
    }*/
  },
  include: {
    I: './steps_file.js'
  },
  bootstrap: null,
  mocha: {},
  name: 'frontend',
  plugins: {
    pauseOnFail: {},
    retryFailedStep: {
      enabled: true
    },
    tryTo: {
      enabled: true
    },
    screenshotOnFail: {
      enabled: true
    }
  }
}
