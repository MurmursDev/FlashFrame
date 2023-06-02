import {Injectable, isDevMode} from '@angular/core';
import {LocalService} from "./local.service";
import jwtDecode from "jwt-decode";
import {Token} from "./model/token";

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {

  private static TOKEN_NAME_ID = 'idToken'
  private static TOKEN_NAME_ACCESS = 'accessToken'

  constructor(private localService: LocalService) {
  }

  getIdToken() {
    return this.localService.getData(AuthenticationService.TOKEN_NAME_ID);
  }

  isLoggedIn() {
    const idToken = this.localService.getData(AuthenticationService.TOKEN_NAME_ID);
    const accessToken = this.localService.getData(AuthenticationService.TOKEN_NAME_ACCESS);
    if (idToken == null || accessToken == null) {
      return false
    }
    const decodedIdToken = (<Token>jwtDecode(idToken));

    const currentSeconds = new Date().getTime() / 1000;
    if (isDevMode()) {
      console.log("token expire at " + decodedIdToken.exp);
      console.log("current time is " + currentSeconds);
    }
    return decodedIdToken.exp > currentSeconds;
  }

  logout() {
    localStorage.removeItem(AuthenticationService.TOKEN_NAME_ID)
    localStorage.removeItem(AuthenticationService.TOKEN_NAME_ACCESS)
  }

}
