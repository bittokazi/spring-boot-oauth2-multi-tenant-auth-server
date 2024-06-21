export interface EmailVerifyView {
  sendValidationEmailSuccess(response: any): void;
  sendValidationEmailError(error: any): void;
  verifyEmailSuccess(response: any): void;
  verifyEmailError(error: any): void;
}
