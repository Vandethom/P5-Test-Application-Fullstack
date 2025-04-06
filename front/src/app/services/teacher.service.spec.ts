import {
  HttpClientTestingModule, 
  HttpTestingController } from '@angular/common/http/testing';
import { TestBed }        from '@angular/core/testing';
import { expect }         from '@jest/globals';
import { Teacher }        from '../interfaces/teacher.interface';
import { TeacherService } from './teacher.service';

describe('TeacherService', () => {
  let service: TeacherService;
  let httpMock: HttpTestingController;

  const mockTeacher: Teacher = {
    id       : 1,
    lastName : 'Smith',
    firstName: 'John',
    createdAt: new Date(),
    updatedAt: new Date()
  };

  const mockTeachers: Teacher[] = [
    mockTeacher,
    {
      id       : 2,
      lastName : 'Doe',
      firstName: 'Jane',
      createdAt: new Date(),
      updatedAt: new Date()
    }
  ];

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports  : [HttpClientTestingModule],
      providers: [TeacherService]
    });
    
    service  = TestBed.inject(TeacherService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify(); // Verify that no requests are outstanding
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  describe('all', () => {
    it('should return all teachers', () => {
      let result: Teacher[] | undefined;
      
      service.all().subscribe(teachers => {
        result = teachers;
      });

      const req = httpMock.expectOne('api/teacher');
      expect(req.request.method).toBe('GET');
      req.flush(mockTeachers);
      
      expect(result).toEqual(mockTeachers);
    });

    it('should handle errors when fetching teachers fails', () => {
      const errorMessage = 'Server error';
      let error: any;
      
      service.all().subscribe({
        next: () => fail('Expected an error, not teachers'),
        error: (e) => error = e
      });

      const req = httpMock.expectOne('api/teacher');
      req.flush(errorMessage, { status: 500, statusText: 'Server Error' });
      
      expect(error.status).toBe(500);
    });
  });

  describe('detail', () => {
    it('should return a specific teacher by ID', () => {
      const teacherId = '1';
      let result: Teacher | undefined;
      
      service.detail(teacherId).subscribe(teacher => {
        result = teacher;
      });

      const req = httpMock.expectOne(`api/teacher/${teacherId}`);
      expect(req.request.method).toBe('GET');
      req.flush(mockTeacher);
      
      expect(result).toEqual(mockTeacher);
    });

    it('should handle errors when fetching a teacher fails', () => {
      const teacherId    = '999';
      const errorMessage = 'Teacher not found';
      let error: any;
      
      service.detail(teacherId).subscribe({
        next : ()  => fail('Expected an error, not a teacher'),
        error: (e) => error = e
      });

      const req = httpMock.expectOne(`api/teacher/${teacherId}`);
      req.flush(errorMessage, { status: 404, statusText: 'Not Found' });
      
      expect(error.status).toBe(404);
    });
  });
});