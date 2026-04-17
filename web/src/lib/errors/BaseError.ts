/**
 * BaseError — Foundation class for all custom application errors.
 *
 * errors (UnauthorizedError, ValidationError, etc.) extend this class.
 */

export class BaseError extends Error {
  readonly name: string;

  constructor(message: string) {
    super(message);
    this.name = this.constructor.name;
    Object.setPrototypeOf(this, new.target.prototype);
  }
}
