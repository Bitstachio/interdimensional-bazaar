/**
 * UnauthorizedError — thrown when an unauthenticated user attempts an action
 * (checkout, writing reviews, accessing account or admin pages) that requires a logged-in session.
 */

import { BaseError } from "@/lib/errors/BaseError";

export class UnauthorizedError extends BaseError {
  constructor(message = "You must be logged in to perform this action.") {
    super(message);
  }
}
