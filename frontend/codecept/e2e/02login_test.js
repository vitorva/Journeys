Feature("login");

const loginFromLPURL = "/";
const registerFromLPURL = "/";

const uniqueId = new Date().getTime();
const uniqueUsername = "02login_test-" + uniqueId;
const uniqueEmail = "twologin@" + uniqueId + ".ch";
const pwd = "passWord123";
const firstName = "Codecept";
const lastName = "JS";

const wrongUsername = "wrongusername";
const wrongPwd = "wrongpwd";

const wrongUserPwdMsg = "Wrong username and/or password";
const serverConnectionProblemMsg = "The connection to the server has had a problem";

Scenario("Test wrong username login", ({I}) => {
  I.amOnPage(loginFromLPURL)
  I.click("#Login")
  I.login(wrongUsername, pwd)
  I.see(wrongUserPwdMsg)
});

Scenario("Test wrong password login", ({I}) => {
  I.amOnPage(loginFromLPURL)
  I.click("#Login")
  I.login(uniqueUsername, wrongPwd)
  I.see(wrongUserPwdMsg)
});

Scenario("Test valid login", ({I}) => {
  I.amOnPage(registerFromLPURL)
  I.click("#Register")
  I.register(firstName, lastName, uniqueUsername, uniqueEmail, pwd)

  I.see(uniqueUsername.toUpperCase())
  I.see("Journeys".toUpperCase())

  I.click(uniqueUsername)
  I.click("Logout")

  I.see("Login".toUpperCase())
  I.click("#Login")
  I.login(uniqueUsername, pwd)

  I.see(uniqueUsername.toUpperCase())
  I.see("Journeys".toUpperCase())
});
