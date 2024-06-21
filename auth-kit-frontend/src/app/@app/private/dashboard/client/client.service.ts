import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root',
})
export class ClientService {
  constructor(private http: HttpClient) {}

  public add(client: any) {
    return this.http
      .post<any>(`${environment.baseUrl}/api/clients`, client)
      .toPromise();
  }

  public getAll() {
    return this.http.get<any>(`${environment.baseUrl}/api/clients`).toPromise();
  }

  public get(id: any) {
    return this.http
      .get<any>(`${environment.baseUrl}/api/clients/${id}`)
      .toPromise();
  }

  public update(client: any) {
    return this.http
      .put<any>(`${environment.baseUrl}/api/clients/${client.id}`, client)
      .toPromise();
  }

  public delete(id: any) {
    return this.http
      .delete<any>(`${environment.baseUrl}/api/clients/${id}`)
      .toPromise();
  }

  generateForm(client: any) {
    let form = {
      clientId: new FormControl(client?.clientId ? client?.clientId : '', []),
      clientSecret: new FormControl(
        client?.clientSecret ? client?.clientSecret : '',
        []
      ),
      resourceIds: new FormControl(
        client?.resourceIds ? client?.resourceIds : '',
        [Validators.required, Validators.minLength(2)]
      ),
      scope: new FormControl(client?.scope ? client?.scope.join(',') : '', [
        Validators.required,
      ]),
      clientAuthenticationMethod: new FormControl(
        client?.clientAuthenticationMethod
          ? client?.clientAuthenticationMethod
          : '',
        [Validators.required, Validators.minLength(3)]
      ),
      authorizedGrantTypes: new FormControl(
        client?.authorizedGrantTypes
          ? client?.authorizedGrantTypes.join(',')
          : '',
        [Validators.required]
      ),
      webServerRedirectUri: new FormControl(
        client?.webServerRedirectUri
          ? client?.webServerRedirectUri.join(',')
          : '',
        []
      ),
      accessTokenValidity: new FormControl(
        client?.accessTokenValidity ? client?.accessTokenValidity : '',
        [Validators.required]
      ),
      refreshTokenValidity: new FormControl(
        client?.refreshTokenValidity ? client?.refreshTokenValidity : '',
        [Validators.required]
      ),
      authorities: new FormControl(
        client?.authorities ? client?.authorities : '',
        []
      ),
      additionalInformation: new FormControl(
        client?.additionalInformation
          ? JSON.stringify(client?.additionalInformation)
          : '',
        []
      ),
      requireConsent: new FormControl(
        client?.requireConsent ? client?.requireConsent : false
      ),
      postLogoutUrl: new FormControl(
        client?.postLogoutUrl ? client?.postLogoutUrl : '',
        []
      ),
      tokenType: new FormControl(client?.tokenType ? client?.tokenType : '', [
        Validators.required,
      ]),
      generateSecret: new FormControl(false),
    };
    if (client?.id)
      form['id'] = new FormControl(client.id, [Validators.required]);
    return new FormGroup(form);
  }

  generateError() {
    return {
      // user: [
      //   {
      //     key: 'exist',
      //     message: 'Username already exist',
      //   },
      // ],
      // email: [
      //   {
      //     key: 'exist',
      //     message: 'Email already exist',
      //   },
      // ],
    };
  }
}
