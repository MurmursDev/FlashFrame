import {Component, HostListener} from '@angular/core';
import {AuthenticationService} from "./authentication.service";
import {Router} from "@angular/router";
import {environment} from "../environments/environment";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'FlashFrame';

  isLoggedIn: boolean = false;
  loginUrl: string;

  @HostListener('window:touchmove', ['$event'])
  handleKeyDown(event: TouchEvent) {
    return false;
  }

  constructor(private authService: AuthenticationService,
              private router: Router,
  ) {
    this.loginUrl = environment.loginUrl;
  }

  ngOnInit(): void {
    this.refreshLoginStatus();
  }

  refreshLoginStatus() {
    this.isLoggedIn = this.authService.isLoggedIn();
  }

  logout() {
    this.authService.logout()
    this.refreshLoginStatus();
    this.router.navigateByUrl('')

  }

}
