/**
 * ValidationError — thrown when user-submitted input fails validation.
 * This includes login, registration, checkout address and payment forms, and reviews.
 *
 * Usage:
 *   throw new ValidationError("Email is required.", "email");
 *   throw new ValidationError("Card number must be 16 digits.", "cardNumber");
 *   throw new ValidationError("Please fill in all required fields.");
 */

import { BaseError } from "@/lib/errors/BaseError";

export class ValidationError extends BaseError {
  readonly field?: string;

  constructor(message: string, field?: string) {
    super(message);
    this.field = field;
  }
}
