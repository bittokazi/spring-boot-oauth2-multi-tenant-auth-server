import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { TokenInterceptor } from './@base/authentication/interceptors/token.interceptor';
import { RefreshTokenService } from './@base/authentication/interceptors/refresh.token.service';
import { AuthService } from './@base/authentication/services/auth.service';

@NgModule({
  declarations: [AppComponent],
  imports: [BrowserModule, AppRoutingModule],
  providers: [
    { provide: HTTP_INTERCEPTORS, useClass: TokenInterceptor, multi: true },
    RefreshTokenService,
    AuthService,
  ],
  bootstrap: [AppComponent],
})
export class AppModule {}
