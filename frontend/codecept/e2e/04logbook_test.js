Feature('logbook');

const logbookURL = "/logbook"

const username = "userTest"
const pwd = "1234"

const seeJourney1 = "test journey"
const seeJourney2 = "test journey 2"

const seeJ1Experience1 = "Signal de Belmont"
const seeJ1Experience2 = "Rivaz"
const seeJ1Experience3 = "Monts de Chardonne"

const seeJ2Experience1 = "Arbre classè"
const seeJ2Experience2 = "Beauregard"
const seeJ2Experience3 = "A la découverte des terrasses de Lavaux"

Scenario("User Journeys appear in logbook", ({I}) => {
  I.amOnPage(logbookURL)
  I.login(username, pwd)
  I.see(seeJourney1)
  I.see(seeJourney2)
});

Scenario("Related journey's experiences appear in logbook", ({I}) => {
  I.amOnPage(logbookURL)
  I.login(username, pwd)

  I.see(seeJ1Experience1)
  I.see(seeJ1Experience2)
  I.see(seeJ1Experience3)
  I.see(seeJ2Experience1)
  I.see(seeJ2Experience2)
  I.see(seeJ2Experience3)

  I.seeNumberOfVisibleElements(".experienceCard", 6);
});

Scenario("All 6 experiences are empty by default", ({I}) => {
  I.amOnPage(logbookURL)
  I.login(username, pwd)
  I.seeNumberOfElements(".addExperienceIcon", 6)
});

Scenario("Clicking on a journey shows its details", ({I}) => {
  I.amOnPage(logbookURL)
  I.login(username, pwd)
  I.click(seeJourney1)
  I.see(seeJourney1)
  I.see(seeJ1Experience1)
  I.see(seeJ1Experience2)
  I.see(seeJ1Experience3)
});

/*
Scenario("User can delete a journey and it is immediately removed from page", ({I}) => {
  I.amOnPage(logbookURL)
  I.login(username, pwd)
  // Click on menu

  // Click on delete
  I.click("#deleteJourneyBtn")
  I.dontSee(seeJourney1)
  I.see(seeJourney2)
});*/
