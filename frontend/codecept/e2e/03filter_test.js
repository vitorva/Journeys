Feature('auth filter');

const logbookURL = "/logbook";
const mapURL = "/map";

const uniqueId = new Date().getTime();
const uniqueUsername = "02login_test-" + uniqueId;
const uniqueEmail = "twologin@" + uniqueId + ".ch";
const pwd = "passWord123";
const firstName = "Codecept";
const lastName = "JS";

const seeMyJourneys = "My journeys"
const seeMyExperiences = "My experiences"
const seeLogbookBtn = "Journeys".toUpperCase()

Scenario("/logbook requires authentication", ({I}) => {
  I.amOnPage(logbookURL)
  I.see("Login")
});

Scenario("/logbook is available after register", ({I}) => {
  I.amOnPage(mapURL)
  I.click("#Register")
  I.register(firstName, lastName, uniqueUsername, uniqueEmail, pwd)
  I.see(seeLogbookBtn)
  I.click("#myjourneysBtn")
  I.see(seeMyJourneys)
  I.see(seeMyExperiences)
});

Scenario("/logbook is available after login", ({I}) => {
  I.amOnPage(mapURL)
  I.click("#Login")
  I.login(uniqueUsername, pwd)
  I.see(seeLogbookBtn)
  I.click("#myjourneysBtn")
  I.see(seeMyJourneys)
  I.see(seeMyExperiences)
});

Scenario("/logbook is available after login from same page", ({I}) => {
  I.amOnPage(logbookURL)
  I.see("Login")
  I.login(uniqueUsername, pwd)
  I.see(seeMyJourneys)
  I.see(seeMyExperiences)
});

Scenario("Saving an itinerary requires authentication", ({I}) => {
  I.amOnPage(mapURL)
  I.seeElement('#saveBtn[disabled]')
});
