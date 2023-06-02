import { Component } from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {LocalService} from "../local.service";

@Component({
  selector: 'app-cognito-login-callback',
  templateUrl: './cognito-login-callback.component.html',
  styleUrls: ['./cognito-login-callback.component.css']
})
export class CognitoLoginCallbackComponent {

  constructor(private activatedRoute: ActivatedRoute,
              private router: Router,
              private localService: LocalService) {
  }

  ngOnInit(): void {
    this.activatedRoute.fragment.subscribe(fragment => {
      if (fragment == null) {
        return
      }
      const urlParams = new URLSearchParams(fragment);
      const idToken = urlParams.get("id_token")
      const accessToken = urlParams.get("access_token")
      if (idToken != null && accessToken != null) {
        this.localService.saveData("idToken", idToken)
        this.localService.saveData("accessToken", accessToken)
        console.log('login success');
        this.router.navigate(["/"])
      }
    });
  }
}
