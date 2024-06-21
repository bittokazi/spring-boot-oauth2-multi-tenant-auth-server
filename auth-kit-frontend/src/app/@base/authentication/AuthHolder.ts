import { LoginResponse } from '../shared/models/auth/LoginResponse';

export const AuthHolder = {
  getToken: (): LoginResponse => {
    if (localStorage.getItem('token') == null) {
      return JSON.parse(`{}`);
    }
    return JSON.parse(localStorage.getItem('token'));
  },
  setToken: (loginResponse: LoginResponse) => {
    localStorage.setItem('token', JSON.stringify(loginResponse));
  },
  setInstanceId: (data: string) => {
    return localStorage.setItem('instanceId', data);
  },
  getInstanceId: (): String => {
    if (
      localStorage.getItem('instanceId') &&
      localStorage.getItem('instanceId') != 'null'
    )
      return localStorage.getItem('instanceId');
    return '';
  },
  setTenant: (data: string) => {
    return localStorage.setItem('tenant', data);
  },
  getTenant: (): String => {
    if (
      localStorage.getItem('tenant') &&
      localStorage.getItem('tenant') != 'null'
    )
      return localStorage.getItem('tenant');
    return '';
  },
};
