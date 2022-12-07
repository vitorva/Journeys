/**
 * Request error thrown when an HTTP request fail
 */
export class RequestError extends Error {
  constructor(message, errorCode, response) {
    super(message);
    this.errorCode = errorCode
    this.response = response
  }
}
