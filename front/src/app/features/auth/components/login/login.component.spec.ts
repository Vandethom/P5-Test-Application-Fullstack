import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { SessionService } from 'src/app/services/session.service';

import { LoginComponent } from './login.component';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';
import { of, throwError } from 'rxjs';

describe('LoginComponent', () => {
  let component     : LoginComponent;
  let fixture       : ComponentFixture<LoginComponent>;
  let authService   : AuthService;
  let sessionService: SessionService;
  let router        : Router;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [LoginComponent],
      providers: [
        SessionService,
        {
          provide: AuthService,
          useValue: {
            login: jest.fn()
          }
        }
      ],
      imports: [
        RouterTestingModule,
        BrowserAnimationsModule,
        HttpClientModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule
      ]
    })
      .compileComponents();
    fixture        = TestBed.createComponent(LoginComponent);
    component      = fixture.componentInstance;
    authService    = TestBed.inject(AuthService);
    sessionService = TestBed.inject(SessionService);
    router         = TestBed.inject(Router);

    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should have a form with email and password fields', () => {
    expect(component.form.get('email')).toBeTruthy();
    expect(component.form.get('password')).toBeTruthy();
  });

  it('should show validation errors when form is invalid', () => {
    component.form.setValue({
      email: '',
      password: ''
    });
    
    expect(component.form.invalid).toBeTruthy();
  });

  it('should call authService.login when form is submitted', () => {
    const loginResponse = {
      token: 'fake-token',
      type: 'Bearer',
      id: 1,
      username: 'test@test.com',
      firstName: 'Test',
      lastName: 'User',
      admin: false
    };
    
    jest.spyOn(authService, 'login').mockReturnValue(of(loginResponse));
    jest.spyOn(sessionService, 'logIn');
    jest.spyOn(router, 'navigate').mockImplementation(() => Promise.resolve(true));
    
    component.form.setValue({
      email: 'test@test.com',
      password: 'password123'
    });
    
    component.submit();
    
    expect(authService.login).toHaveBeenCalledWith({
      email: 'test@test.com',
      password: 'password123'
    });
    expect(sessionService.logIn).toHaveBeenCalledWith(loginResponse);
    expect(router.navigate).toHaveBeenCalledWith(['/sessions']);
  });

  it('should handle login errors', () => {
    jest.spyOn(authService, 'login').mockReturnValue(throwError(() => new Error('Login failed')));
    
    component.form.setValue({
      email: 'test@test.com',
      password: 'wrong-password'
    });
    
    component.submit();
    
    expect(component.onError).toBeTruthy();
  });
});
