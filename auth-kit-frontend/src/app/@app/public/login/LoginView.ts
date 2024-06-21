import { LoginResponse } from 'src/app/@base/shared/models/auth/LoginResponse';

export interface LoginView {
  loginSuccess(loginResponse: LoginResponse);
  loginError(error: any);
  userAuthCheckSuccess();
  userAuthCheckError();
}
