Feature('register');

const registerFromLPURL = "/"

const uniqueID = new Date().getTime();
const firstName = "Codecept";
const lastName = "JS";
const uniqueUsername = "1register_test1-" + uniqueID;
const uniqueEmail = "oneregister@" + uniqueID + ".ch";
const pwd = "passWord123";

const emptyStr = "";

const wrongEmail = "CodeceptJS.ch";

const missingUserNameMsg = "Missing username";
const misformatedEmailMsg = "Email address is misformated";

Scenario('Test missing field (username) on registration', ({I}) => {
  I.amOnPage(registerFromLPURL)
  I.click("#Register")
  I.register(firstName, lastName, emptyStr, uniqueEmail, pwd)
  I.see(missingUserNameMsg)
})

Scenario('Test misformatted email on registration', ({I}) => {
  I.amOnPage(registerFromLPURL)
  I.click("#Register")
  I.register(firstName, lastName, uniqueUsername, wrongEmail, pwd)
  I.see(misformatedEmailMsg)
})

Scenario('Test valid registration', ({I}) => {
  I.amOnPage(registerFromLPURL)
  I.click("#Register")
  I.register(firstName, lastName, uniqueUsername, uniqueEmail, pwd)
  I.see(uniqueUsername.toUpperCase())
  I.see("Journeys".toUpperCase())
})
