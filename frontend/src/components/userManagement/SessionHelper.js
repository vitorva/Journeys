import React from "react";
import {sendMyJourneysApiRequest} from "../common/ApiHelper";
import moment from "moment";

export const SESSION_DURATION = 600000

export class SessionHelper {
  /**
   *
   * @param {Object} userSession
   * @param {React.Dispatcher} setUserSession
   */
  constructor(userSession, setUserSession) {
    this.userSession = userSession
    this.setUserSession = setUserSession
  }

  /**
   * Checks if the user is connected by checking if the token is not expired and userSession Object has attributes
   */
  isUserLoggedIn = () => {
    return (
      (!this.isExpired() && Object.keys(this.userSession).length > 0 &&
        this.userSession.constructor === Object)
    )
  }

  /**
   * Return the username of the user stored in userSession Object
   */
  getUserName = () => {
    return this.userSession.username
  }

  /**
   * Return the firstname of the user stored in userSession Object
   */
  getUserFirstName = () => {
    return this.userSession.firstname
  }

  /**
   * Return the lastname of the user stored in userSession Object
   */
  getUserLastName = () => {
    return this.userSession.lastname
  }

  /**
   * Return the email of the user stored in userSession Object
   */
  getUserEmail = () => {
    return this.userSession.email
  }

  /**
   * Logs the user out by removing the JourneysAccessToken cookie, calling the React dispatcher and removing the local storage item
   */
  logout = () => {
    const LOGOUT_ENDPOINT = "/auth/logout"

    sendMyJourneysApiRequest(LOGOUT_ENDPOINT, "GET")
      .then(result => console.log(result),
        error => {
          console.log(error)
        })

    this.setUserSession({})
    localStorage.removeItem('user')
  }

  /**
   * Logs the user in by calling the React dispatcher and by adding a local storage item (for persistence after refresh)
   */
  login = user => {
    this.setUserSession(user)
    localStorage.setItem('user', JSON.stringify(user))
  }

  /**
   * Verifies token expiration
   * @returns {boolean} true if token is expired, false otherwise
   */
  isExpired = () => {
    return !(moment(this.userSession.tokenDuration).isAfter(moment()))
  }

  /**
   *
   * @returns {*} expiration date
   */
  getExpirationDate = () => {
    return this.userSession.tokenDuration;
  }
}

export const SessionContext = React.createContext({user: {}})
