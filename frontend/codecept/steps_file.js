// Custom step methods to 'I' object
module.exports = function () {
  const journeysURL = "http://myjourneys.ch"

  return actor({
    // Define custom steps here, use 'this' to access default methods of I.
    // It is recommended to place a general 'login' function here.
    register: function (firstName, lastName, userName, email, password) {
      this.fillField("firstName", firstName)
      this.fillField("lastName", lastName)
      this.fillField("userName", userName)
      this.fillField("email", email)
      this.fillField("password", password)
      this.click({id: "registerBtn"})
    },

    login: function (userName, password) {
      this.fillField("username", userName)
      this.fillField("password", password)
      this.click({id: "loginBtn"})
    }
  });
}
