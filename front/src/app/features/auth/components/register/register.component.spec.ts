import { HttpClientModule }                 from '@angular/common/http';
import { ComponentFixture, TestBed }        from '@angular/core/testing';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { MatCardModule }                    from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule }                    from '@angular/material/icon';
import { MatInputModule }                   from '@angular/material/input';
import { BrowserAnimationsModule }          from '@angular/platform-browser/animations';
import { expect }                           from '@jest/globals';
import { RegisterComponent }                from './register.component';
import { AuthService }                      from '../../services/auth.service';
import { Router }                           from '@angular/router';
import { of, throwError }                   from 'rxjs';

describe('RegisterComponent', () => {
  let component  : RegisterComponent;
  let fixture    : ComponentFixture<RegisterComponent>;
  let authService: AuthService;
  let router     : Router;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [RegisterComponent],
      providers   : [
        {
          provide : AuthService,
          useValue: {
            register: jest.fn()
          }
        },
        {
          provide: Router,
          useValue: {
            navigate: jest.fn()
          }
        }
      ],
      imports: [
        BrowserAnimationsModule,
        HttpClientModule,
        ReactiveFormsModule,  
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule
      ]
    })
      .compileComponents();

    fixture     = TestBed.createComponent(RegisterComponent);
    component   = fixture.componentInstance;
    authService = TestBed.inject(AuthService);
    router      = TestBed.inject(Router);

    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
  
  it('should have form controls for email, firstName, lastName, and password', () => {
    expect(component.form.get('email')).toBeTruthy();
    expect(component.form.get('firstName')).toBeTruthy();
    expect(component.form.get('lastName')).toBeTruthy();
    expect(component.form.get('password')).toBeTruthy();
  });

  it('should mark the form as invalid when empty', () => {
    expect(component.form.valid).toBeFalsy();
  });

  it('should call register service and redirect to login on successful registration', () => {
    jest.spyOn(authService, 'register').mockReturnValue(of(void 0));
    jest.spyOn(router, 'navigate');
    
    component.form.setValue({
      email    : 'test@example.com',
      firstName: 'Test',
      lastName : 'User',
      password : 'password123'
    });
    
    component.submit();
    
    expect(authService.register).toHaveBeenCalledWith({
      email    : 'test@example.com',
      firstName: 'Test',
      lastName : 'User',
      password : 'password123'
    });
    expect(router.navigate).toHaveBeenCalledWith(['/login']);
  });

  it('should show error on registration failure', () => {
    jest.spyOn(authService, 'register').mockReturnValue(throwError(() => new Error('Registration failed')));
    
    component.form.setValue({
      email    : 'test@example.com',
      firstName: 'Test',
      lastName : 'User',
      password : 'password123'
    });
    
    component.submit();
    
    expect(component.onError).toBeTruthy();
  });
});
