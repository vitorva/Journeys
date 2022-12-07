import {RequestError} from "./RequestError";

export const MYJOURNEYS_API = "/api"

/**
 * Prepares and formats a request to My Journeys backend API
 * @param endpoint to send request to
 * @param verb request type (GET, POST, PUT, DELETE, etc.)
 * @param data body of the request if needed
 * @param params url params if needed
 * @param id id of user or journey for uuid-related requests
 * @param item the element to get based on the uuid
 * @returns {Promise<Response>}
 */
export function sendMyJourneysApiRequest(endpoint, verb = "GET", data = {}, params = {}, id = null, item = null) {
  let strUrl
  // Checks for specific id and item combination
  if (id !== null) {
    strUrl = MYJOURNEYS_API.concat(endpoint).concat(`${encodeURIComponent(id)}`)

    if (item !== null) {
      strUrl = strUrl.concat(item)
    }
  } else {
    strUrl = MYJOURNEYS_API.concat(endpoint)
  }

  const url = prepareUrl(strUrl, verb, params)

  return sendRequest(url, verb, data)
}

/**
 * Sends the formatted request
 * @param url the complete url of the request (including potential params
 * @param verb request type (GET, POST, PUT, DELETE, etc.)
 * @param data body of the request if needed
 * @returns {Promise<Response>}
 */
export function sendRequest(url, verb = "GET", data = {}) {
  const requestInfo = {
    method: verb,
    credentials: 'include',
    mode: 'cors',
    headers: {
      'Content-Type': 'application/json',
    },
  }

  if (verb !== 'GET' && verb !== 'HEAD' && verb !== 'PUT') {
    if (verb === 'PUT') {
      requestInfo.body = data;
    } else {
      requestInfo.body = JSON.stringify(data) // body data type must match "Content-Type" header
    }
  }

  return fetch(url, requestInfo).then(response => {
    if (response.status >= 200 && response.status <= 299) {
      return response.json()
    } else {
      throw new RequestError("Request error", response.status, response)
    }
  })
}

/**
 * Prepares a URL object by appending potential query params
 * @param strURL base url
 * @param verb request type (GET, POST, PUT, DELETE, etc.)
 * @param params url params if needed
 * @returns {URL} a formatted URL to the endpoint containing potential query params
 */
export function prepareUrl(strURL, verb = "GET", params = {}) {
  const url = new URL(strURL, document.baseURI)
  if (verb === "GET") {
    // Add GET params to url
    Object.keys(params).forEach(key =>
      url.searchParams.append(key, `${encodeURIComponent(params[key])}`)
    )
  }
  
  return url
}
