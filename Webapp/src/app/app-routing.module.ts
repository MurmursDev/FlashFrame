import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {HomeComponent} from "./home/home.component";
import {CognitoLoginCallbackComponent} from "./cognito-login-callback/cognito-login-callback.component";

const routes: Routes = [
  {
    path: 'callback', component: CognitoLoginCallbackComponent
  },
  {
    path: '', component: HomeComponent
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
