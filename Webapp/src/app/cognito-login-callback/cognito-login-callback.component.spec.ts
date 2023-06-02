import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CognitoLoginCallbackComponent } from './cognito-login-callback.component';

describe('CognitoLoginCallbackComponent', () => {
  let component: CognitoLoginCallbackComponent;
  let fixture: ComponentFixture<CognitoLoginCallbackComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CognitoLoginCallbackComponent]
    });
    fixture = TestBed.createComponent(CognitoLoginCallbackComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
