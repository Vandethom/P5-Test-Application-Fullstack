import { TestBed }            from '@angular/core/testing';
import { expect }             from '@jest/globals';
import { SessionService }     from './session.service';
import { SessionInformation } from '../interfaces/sessionInformation.interface';

describe('SessionService', () => {
  let service: SessionService;
  const mockSessionInfo: SessionInformation = {
    token    : 'fake-token',
    type     : 'Bearer',
    id       : 1,
    username : 'test@example.com',
    firstName: 'Test',
    lastName : 'User',
    admin    : true
  };

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SessionService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should have isLogged initially set to false', () => {
    expect(service.isLogged).toBe(false);
    expect(service.sessionInformation).toBeUndefined();
  });

  describe('logIn', () => {
    it('should set session information and isLogged to true', () => {
      service.logIn(mockSessionInfo);
      
      expect(service.isLogged).toBe(true);
      expect(service.sessionInformation).toEqual(mockSessionInfo);
    });

    it('should emit true through $isLogged observable', (done) => {
      service.$isLogged().subscribe(isLogged => {
        expect(isLogged).toBe(true);
        done();
      });
      
      service.logIn(mockSessionInfo);
    });
  });

  describe('logOut', () => {
    beforeEach(() => {
      // Setup logged in state
      service.logIn(mockSessionInfo);
      expect(service.isLogged).toBe(true);
    });

    it('should clear session information and set isLogged to false', () => {
      service.logOut();
      
      expect(service.isLogged).toBe(false);
      expect(service.sessionInformation).toBeUndefined();
    });

    it('should emit false through $isLogged observable', (done) => {
      service.$isLogged().subscribe(isLogged => {
        expect(isLogged).toBe(false);
        done();
      });
      
      service.logOut();
    });
  });

  describe('$isLogged', () => {
    it('should emit the current value of isLogged', (done) => {
      service.$isLogged().subscribe(isLogged => {
        expect(isLogged).toBe(false);
        done();
      });
    });

    it('should emit updated values when login status changes', (done) => {
      const emittedValues: boolean[] = [];
      const expectedSequence = [false, true, false];
      
      service.$isLogged().subscribe(isLogged => {
        emittedValues.push(isLogged);
        
        if (emittedValues.length === expectedSequence.length) {
          expect(emittedValues).toEqual(expectedSequence);
          done();
        }
      });
      
      service.logIn(mockSessionInfo);
      service.logOut();
    });
  });
});