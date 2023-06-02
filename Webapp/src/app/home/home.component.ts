import {Component} from '@angular/core';
import {DefaultService} from "flashframe-client";
import {AuthenticationService} from "../authentication.service";

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent {
  welcomeContent?: string;

  constructor(private defaultService: DefaultService, private authService: AuthenticationService) {
  }

  ngOnInit(): void {
    if (this.authService.isLoggedIn()) {
      this.defaultService.usersUsernameGet("test").subscribe(value => {
        this.welcomeContent = value.name
      })
    }
  }
}
