import {isDevMode, NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {ServiceWorkerModule} from '@angular/service-worker';
import {CognitoLoginCallbackComponent} from './cognito-login-callback/cognito-login-callback.component';
import {HomeComponent} from './home/home.component';
import {AuthenticationService} from "./authentication.service";
import {HttpClientModule} from "@angular/common/http";
import {ApiModule, Configuration, ConfigurationParameters} from "flashframe-client";
import {MatToolbarModule} from "@angular/material/toolbar";
import {MatIconModule} from "@angular/material/icon";
import {MatSidenavModule} from "@angular/material/sidenav";
import {MatListModule} from "@angular/material/list";
import {MatButtonModule} from "@angular/material/button";
import {environment} from "../environments/environment";

export function apiConfigFactory(authService: AuthenticationService): Configuration {
  const params: ConfigurationParameters = {
    basePath: environment.apiBaseUrl,
    credentials: {
      "MyUserPool": () => {
        const idToken = authService.getIdToken();
        if (idToken) {
          return "Bearer " + idToken
        }
        return undefined;
      }
    }
  }
  return new Configuration(params);
}

@NgModule({
  declarations: [
    AppComponent,
    CognitoLoginCallbackComponent,
    HomeComponent
  ],
  imports: [
    {
      ngModule: ApiModule,
      providers: [
        {
          provide: Configuration,
          deps: [AuthenticationService],
          useFactory: apiConfigFactory
        }
      ]
    },
    HttpClientModule,
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    ServiceWorkerModule.register('ngsw-worker.js', {
      enabled: !isDevMode(),
      // Register the ServiceWorker as soon as the application is stable
      // or after 30 seconds (whichever comes first).
      registrationStrategy: 'registerWhenStable:30000'
    }),
    MatToolbarModule,
    MatIconModule,
    MatSidenavModule,
    MatListModule,
    MatButtonModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule {
}
