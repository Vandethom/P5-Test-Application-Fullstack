import { 
  HttpClientTestingModule, 
  HttpTestingController } from '@angular/common/http/testing';
import { TestBed }        from '@angular/core/testing';
import { expect }         from '@jest/globals';
import { User }           from '../interfaces/user.interface';
import { UserService }    from './user.service';
import { HttpClient }     from '@angular/common/http';
import { of }             from 'rxjs';

describe('UserService', () => {
  let service: UserService;
  let httpMock: HttpTestingController;

  const mockUser: User = {
    id       : 1,
    email    : 'test@test.com',
    lastName : 'Doe',
    firstName: 'John',
    admin    : false,
    password : 'password',
    createdAt: new Date(),
    updatedAt: new Date()
  };

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports  : [HttpClientTestingModule],
      providers: [UserService]
    });
    
    service  = TestBed.inject(UserService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify(); // Verify that no requests are outstanding
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  describe('getById', () => {
    it('should return a user by ID', () => {
      const userId = '1';
      let result: User | undefined;
      
      service.getById(userId).subscribe(user => {
        result = user;
      });

      const req = httpMock.expectOne(req => req.url.includes(`api//user/${userId}`));
      expect(req.request.method).toBe('GET');
      req.flush(mockUser);
      
      expect(result).toEqual(mockUser);
    });

    it('should handle error when fetching user fails', () => {
      const userId       = '999';
      const errorMessage = 'User not found';
      let error: any;
      
      service.getById(userId).subscribe({
        next : ()  => fail('Expected an error, not a user'),
        error: (e) => error = e
      });

      const req = httpMock.expectOne(req => req.url.includes(`api//user/${userId}`));
      req.flush(errorMessage, { status: 404, statusText: 'Not Found' });
      
      expect(error.status).toBe(404);
    });
  });

  describe('delete', () => {
    it('should delete a user by ID', () => {
      const userId  = '1';
      let completed = false;
      
      service.delete(userId).subscribe(() => {
        completed = true;
      });

      const req = httpMock.expectOne(req => req.url.includes(`api//user/${userId}`));
      expect(req.request.method).toBe('DELETE');
      req.flush({});
      
      expect(completed).toBeTruthy();
    });

    it('should handle error when deleting user fails', () => {
      const userId = '999';
      const errorMessage = 'Unauthorized';
      let error: any;
      
      service.delete(userId).subscribe({
        next : ()  => fail('Expected an error, not success'),
        error: (e) => error = e
      });

      const req = httpMock.expectOne(req => req.url.includes(`api//user/${userId}`));
      req.flush(errorMessage, { status: 403, statusText: 'Forbidden' });
      
      expect(error.status).toBe(403);
    });
  });
});
