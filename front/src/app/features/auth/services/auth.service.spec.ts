import { 
  HttpClientTestingModule, 
  HttpTestingController }     from '@angular/common/http/testing';
import { TestBed }            from '@angular/core/testing';
import { expect }             from '@jest/globals';
import { SessionInformation } from 'src/app/interfaces/sessionInformation.interface';
import { LoginRequest }       from '../interfaces/loginRequest.interface';
import { RegisterRequest }    from '../interfaces/registerRequest.interface';
import { AuthService }        from './auth.service';

describe('AuthService', () => {
  let service : AuthService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports  : [HttpClientTestingModule],
      providers: [AuthService]
    });
    
    service  = TestBed.inject(AuthService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify(); // Verify that no requests are outstanding
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  describe('register', () => {
    it('should register a new user', () => {
      const registerRequest: RegisterRequest = {
        email    : 'test@test.com',
        firstName: 'Test',
        lastName : 'User',
        password : 'password123'
      };
      
      let completed = false;
      
      service.register(registerRequest).subscribe(() => {
        completed = true;
      });

      const req = httpMock.expectOne('api/auth/register');
      expect(req.request.method).toBe('POST');
      expect(req.request.body).toEqual(registerRequest);
      req.flush({});
      
      expect(completed).toBeTruthy();
    });

    it('should handle registration errors', () => {
      const registerRequest: RegisterRequest = {
        email    : 'existing@test.com',
        firstName: 'Test',
        lastName : 'User',
        password : 'password123'
      };
      
      let error: any;
      
      service.register(registerRequest).subscribe({
        next : () => fail('Expected an error, not success'),
        error: (e) => error = e
      });

      const req = httpMock.expectOne('api/auth/register');
      req.flush('Email already exists', { status: 400, statusText: 'Bad Request' });
      
      expect(error.status).toBe(400);
    });
  });

  describe('login', () => {
    it('should login and return session information', () => {
      const loginRequest: LoginRequest = {
        email   : 'test@test.com',
        password: 'password123'
      };
      
      const mockSessionInfo: SessionInformation = {
        token    : 'fake-token',
        type     : 'Bearer',
        id       : 1,
        username : 'test@test.com',
        firstName: 'Test',
        lastName : 'User',
        admin    : false
      };
      
      let result: SessionInformation | undefined;
      
      service.login(loginRequest).subscribe(session => {
        result = session;
      });

      const req = httpMock.expectOne('api/auth/login');
      expect(req.request.method).toBe('POST');
      expect(req.request.body).toEqual(loginRequest);
      req.flush(mockSessionInfo);
      
      expect(result).toEqual(mockSessionInfo);
    });

    it('should handle login errors', () => {
      const loginRequest: LoginRequest = {
        email   : 'wrong@test.com',
        password: 'wrongpassword'
      };
      
      let error: any;
      
      service.login(loginRequest).subscribe({
        next : () => fail('Expected an error, not session info'),
        error: (e) => error = e
      });

      const req = httpMock.expectOne('api/auth/login');
      req.flush('Invalid credentials', { status: 401, statusText: 'Unauthorized' });
      
      expect(error.status).toBe(401);
    });
  });

  it('should handle different HTTP error status codes', () => {
    const registerRequest: RegisterRequest = {
      email    : 'test@test.com',
      firstName: 'Test',
      lastName : 'User',
      password : 'password'
    };
    
    // Test server error (500)
    let serverError: any;
    service.register(registerRequest).subscribe({
      next : () => fail('Should have failed with 500 error'),
      error: (e) => serverError = e
    });
    
    const serverReq = httpMock.expectOne('api/auth/register');
    serverReq.flush('Server error', { status: 500, statusText: 'Server Error' });
    expect(serverError.status).toBe(500);
  });
  
  // Separate test for network error
  it('should handle network errors', () => {
    const registerRequest: RegisterRequest = {
      email    : 'test@test.com',
      firstName: 'Test',
      lastName : 'User',
      password : 'password'
    };
    
    let connectionError: any = null;
    service.register(registerRequest).subscribe({
      next : () => fail('Should have failed with network error'),
      error: (e) => connectionError = e
    });
    
    const connectionReq = httpMock.expectOne('api/auth/register');
    connectionReq.error(new ErrorEvent('Network error'));
    
    // Check for properties that would indicate a network error
    expect(connectionError).toBeTruthy();
    expect(connectionError.name).toBe('HttpErrorResponse');
    // Angular's HttpErrorResponse has a property error that contains the original error
    expect(connectionError.error instanceof ErrorEvent).toBe(true);
  });
});