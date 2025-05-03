import { 
  HttpClientTestingModule, 
  HttpTestingController }    from '@angular/common/http/testing';
import { TestBed }           from '@angular/core/testing';
import { expect }            from '@jest/globals';
import { Session }           from '../interfaces/session.interface';
import { SessionApiService } from './session-api.service';

describe('SessionApiService', () => {
  let service: SessionApiService;
  let httpMock: HttpTestingController;

  const mockSession: Session = {
    id         : 1,
    name       : 'Yoga Session',
    description: 'A relaxing yoga session',
    date       : new Date(),
    teacher_id : 1,
    users      : [1, 2],
    createdAt  : new Date(),
    updatedAt  : new Date()
  };

  const mockSessions: Session[] = [
    mockSession,
    {
      id         : 2,
      name       : 'Meditation Session',
      description: 'Learn meditation techniques',
      date       : new Date(),
      teacher_id : 2,
      users      : [1],
      createdAt  : new Date(),
      updatedAt  : new Date()
    }
  ];

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports  : [HttpClientTestingModule],
      providers: [SessionApiService]
    });
    
    service  = TestBed.inject(SessionApiService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  describe('all', () => {
    it('should return all sessions', () => {
      let result: Session[] | undefined;
      
      service.all().subscribe(sessions => {
        result = sessions;
      });

      const req = httpMock.expectOne('api/session');
      expect(req.request.method).toBe('GET');
      req.flush(mockSessions);
      
      expect(result).toEqual(mockSessions);
    });

    it('should handle error when fetching all sessions fails', () => {
      const errorMessage = 'Error fetching sessions';
      let   error: any;
      
      service.all().subscribe({
        next : ()  => fail('Expected an error, not sessions'),
        error: (e) => error = e
      });

      const req = httpMock.expectOne('api/session');
      req.flush(errorMessage, { status: 500, statusText: 'Server Error' });
      
      expect(error.status).toBe(500);
    });
  });

  describe('detail', () => {
    it('should return a specific session by ID', () => {
      const sessionId = '1';
      let   result: Session | undefined;
      
      service.detail(sessionId).subscribe(session => {
        result = session;
      });

      const req = httpMock.expectOne(`api/session/${sessionId}`);
      expect(req.request.method).toBe('GET');
      req.flush(mockSession);
      
      expect(result).toEqual(mockSession);
    });

    it('should handle error when fetching a session fails', () => {
      const sessionId    = '999';
      const errorMessage = 'Session not found';
      let   error: any;
      
      service.detail(sessionId).subscribe({
        next : ()  => fail('Expected an error, not a session'),
        error: (e) => error = e
      });

      const req = httpMock.expectOne(`api/session/${sessionId}`);
      req.flush(errorMessage, { status: 404, statusText: 'Not Found' });
      
      expect(error.status).toBe(404);
    });
  });

  describe('delete', () => {
    it('should delete a session by ID', () => {
      const sessionId = '1';
      let completed   = false;
      
      service.delete(sessionId).subscribe(() => {
        completed = true;
      });

      const req = httpMock.expectOne(`api/session/${sessionId}`);
      expect(req.request.method).toBe('DELETE');
      req.flush({});
      
      expect(completed).toBeTruthy();
    });

    it('should handle error when deleting a session fails', () => {
      const sessionId    = '999';
      const errorMessage = 'Unauthorized';
      let   error: any;
      
      service.delete(sessionId).subscribe({
        next : ()  => fail('Expected an error, not success'),
        error: (e) => error = e
      });

      const req = httpMock.expectOne(`api/session/${sessionId}`);
      req.flush(errorMessage, { status: 403, statusText: 'Forbidden' });
      
      expect(error.status).toBe(403);
    });
  });

  describe('create', () => {
    it('should create a new session', () => {
      const newSession: Session = {
        name       : 'New Session',
        description: 'Brand new session',
        date       : new Date(),
        teacher_id : 1,
        users      : []
      } as Session;

      let result: Session | undefined;
      
      service.create(newSession).subscribe(session => {
        result = session;
      });

      const req = httpMock.expectOne('api/session');
      expect(req.request.method).toBe('POST');
      expect(req.request.body).toEqual(newSession);
      
      const createdSession = {...newSession, id: 3};
      req.flush(createdSession);
      
      expect(result).toEqual(createdSession);
    });

    it('should handle error when creating a session fails', () => {
      const newSession = {} as Session; // Invalid session
      let error: any;
      
      service.create(newSession).subscribe({
        next : ()  => fail('Expected an error, not success'),
        error: (e) => error = e
      });

      const req = httpMock.expectOne('api/session');
      req.flush('Invalid session data', { status: 400, statusText: 'Bad Request' });
      
      expect(error.status).toBe(400);
    });
  });

  describe('update', () => {
    it('should update an existing session', () => {
      const sessionId = '1';
      const updatedSession: Session = {
        ...mockSession,
        name: 'Updated Session Name'
      };

      let result: Session | undefined;
      
      service.update(sessionId, updatedSession).subscribe(session => {
        result = session;
      });

      const req = httpMock.expectOne(`api/session/${sessionId}`);
      expect(req.request.method).toBe('PUT');
      expect(req.request.body).toEqual(updatedSession);
      req.flush(updatedSession);
      
      expect(result).toEqual(updatedSession);
    });

    it('should handle error when updating a session fails', () => {
      const sessionId = '999';
      let   error: any;
      
      service.update(sessionId, mockSession).subscribe({
        next : ()  => fail('Expected an error, not success'),
        error: (e) => error = e
      });

      const req = httpMock.expectOne(`api/session/${sessionId}`);
      req.flush('Session not found', { status: 404, statusText: 'Not Found' });
      
      expect(error.status).toBe(404);
    });
  });

  describe('participate', () => {
    it('should allow a user to participate in a session', () => {
      const sessionId = '1';
      const userId    = '3';
      let completed   = false;
      
      service.participate(sessionId, userId).subscribe(() => {
        completed = true;
      });

      const req = httpMock.expectOne(`api/session/${sessionId}/participate/${userId}`);
      expect(req.request.method).toBe('POST');
      expect(req.request.body).toBeNull();
      req.flush({});
      
      expect(completed).toBeTruthy();
    });

    it('should handle error when participation fails', () => {
      const sessionId = '1';
      const userId    = '999';
      let   error: any;
      
      service.participate(sessionId, userId).subscribe({
        next: () => fail('Expected an error, not success'),
        error: (e) => error = e
      });

      const req = httpMock.expectOne(`api/session/${sessionId}/participate/${userId}`);
      req.flush('User already participating', { status: 400, statusText: 'Bad Request' });
      
      expect(error.status).toBe(400);
    });
  });

  describe('unParticipate', () => {
    it('should allow a user to un-participate from a session', () => {
      const sessionId = '1';
      const userId    = '2';
      let completed   = false;
      
      service.unParticipate(sessionId, userId).subscribe(() => {
        completed = true;
      });

      const req = httpMock.expectOne(`api/session/${sessionId}/participate/${userId}`);
      expect(req.request.method).toBe('DELETE');
      req.flush({});
      
      expect(completed).toBeTruthy();
    });

    it('should handle error when un-participation fails', () => {
      const sessionId = '1';
      const userId    = '999';
      let   error: any;
      
      service.unParticipate(sessionId, userId).subscribe({
        next : ()  => fail('Expected an error, not success'),
        error: (e) => error = e
      });

      const req = httpMock.expectOne(`api/session/${sessionId}/participate/${userId}`);
      req.flush('User not found in session', { status: 404, statusText: 'Not Found' });
      
      expect(error.status).toBe(404);
    });
  });
});