import { HttpClientModule }                         from '@angular/common/http';
import { CUSTOM_ELEMENTS_SCHEMA, NO_ERRORS_SCHEMA } from '@angular/core';
import { ComponentFixture, TestBed }                from '@angular/core/testing';
import { ReactiveFormsModule }                      from '@angular/forms';
import { MatCardModule }                            from '@angular/material/card';
import { MatIconModule }                            from '@angular/material/icon';
import { MatSnackBar, MatSnackBarModule }           from '@angular/material/snack-bar';
import { ActivatedRoute, Router }                   from '@angular/router';
import { RouterTestingModule }                      from '@angular/router/testing';
import { expect }                                   from '@jest/globals';
import { SessionService }                           from 'src/app/services/session.service';
import { TeacherService }                           from 'src/app/services/teacher.service';
import { SessionApiService }                        from '../../services/session-api.service';
import { of, throwError }                           from 'rxjs';
import { Session }                                  from '../../interfaces/session.interface';
import { Teacher }                                  from 'src/app/interfaces/teacher.interface';
import { DetailComponent }                          from './detail.component';

describe('DetailComponent', () => {
  let component        : DetailComponent;
  let fixture          : ComponentFixture<DetailComponent>;
  let sessionService   : SessionService;
  let sessionApiService: SessionApiService;
  let teacherService   : TeacherService;
  let matSnackBar      : MatSnackBar;
  let router           : Router;

  const mockSessionInfo = {
    token    : 'fake-token',
    type     : 'Bearer',
    id       : 1,
    username : 'test@example.com',
    firstName: 'Test',
    lastName : 'User',
    admin    : true
  };

  const mockSession: Session = {
    id         : 1,
    name       : 'Yoga Session',
    description: 'A relaxing yoga session',
    date       : new Date(),
    teacher_id : 1,
    users      : [2, 3],
    createdAt  : new Date(),
    updatedAt  : new Date()
  };

  const mockTeacher: Teacher = {
    id       : 1,
    lastName : 'Smith',
    firstName: 'John',
    createdAt: new Date(),
    updatedAt: new Date()
  };

  const mockSessionService = {
    sessionInformation: mockSessionInfo
  };

  const mockSessionApiService = {
    detail       : jest.fn().mockReturnValue(of(mockSession)),
    delete       : jest.fn().mockReturnValue(of({})),
    participate  : jest.fn().mockReturnValue(of({})),
    unParticipate: jest.fn().mockReturnValue(of({}))
  };

  const mockTeacherService = {
    detail: jest.fn().mockReturnValue(of(mockTeacher))
  };

  const mockMatSnackBar = {
    open: jest.fn()
  };

  const mockRouter = {
    navigate: jest.fn()
  };

  const mockActivatedRoute = {
    snapshot: {
      paramMap: {
        get: jest.fn().mockReturnValue('1')
      }
    }
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        HttpClientModule,
        MatSnackBarModule,
        ReactiveFormsModule,
        MatCardModule,
        MatIconModule
      ],
      declarations: [DetailComponent],
      providers: [
        { provide: SessionService,    useValue: mockSessionService },
        { provide: SessionApiService, useValue: mockSessionApiService },
        { provide: TeacherService,    useValue: mockTeacherService },
        { provide: MatSnackBar,       useValue: mockMatSnackBar },
        { provide: Router,            useValue: mockRouter },
        { provide: ActivatedRoute,    useValue: mockActivatedRoute }
      ],
      schemas: [CUSTOM_ELEMENTS_SCHEMA, NO_ERRORS_SCHEMA]
    }).compileComponents();

    // Reset mocks before each test
    jest.clearAllMocks();

    fixture           = TestBed.createComponent(DetailComponent);
    component         = fixture.componentInstance;
    sessionService    = TestBed.inject(SessionService);
    sessionApiService = TestBed.inject(SessionApiService);
    teacherService    = TestBed.inject(TeacherService);
    matSnackBar       = TestBed.inject(MatSnackBar);
    router            = TestBed.inject(Router);
    
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should fetch session and teacher data on init', () => {
    expect(sessionApiService.detail).toHaveBeenCalledWith('1');
    expect(teacherService.detail).toHaveBeenCalledWith('1');
    expect(component.session).toEqual(mockSession);
    expect(component.teacher).toEqual(mockTeacher);
  });

  it('should navigate back when back() is called', () => {
    const historySpy = jest.spyOn(window.history, 'back').mockImplementation(() => {});
    
    component.back();
    
    expect(historySpy).toHaveBeenCalled();
    historySpy.mockRestore();
  });

  it('should delete a session', () => {
    component.delete();
    
    expect(sessionApiService.delete).toHaveBeenCalledWith('1');
    expect(matSnackBar.open).toHaveBeenCalledWith('Session deleted !', 'Close', { duration: 3000 });
    expect(router.navigate).toHaveBeenCalledWith(['sessions']);
  });

  it('should allow a user to participate in a session', () => {
    component.isParticipate = false;
    
    jest.clearAllMocks();
    
    component.participate();
    
    expect(sessionApiService.participate).toHaveBeenCalledWith('1', '1');
    expect(sessionApiService.detail).toHaveBeenCalledTimes(1); 
  });

  it('should allow a user to un-participate from a session', () => {
    component.isParticipate = true;
    
    jest.clearAllMocks();
    
    component.unParticipate();
    
    expect(sessionApiService.unParticipate).toHaveBeenCalledWith('1', '1');
    expect(sessionApiService.detail).toHaveBeenCalledTimes(1); 
  });

  it('should determine if user is participating', () => {
    const participatingSession = {...mockSession, users: [1, 2, 3]};
    jest.spyOn(sessionApiService, 'detail').mockReturnValueOnce(of(participatingSession));
    
    component.ngOnInit();
    
    expect(component.isParticipate).toBeTruthy();
  });

  it('should handle API errors during session deletion', () => {
    const error = new Error('Delete failed');
    jest.spyOn(sessionApiService, 'delete').mockReturnValue(throwError(() => error));
    
    jest.clearAllMocks();
    
    try {
      component.delete();
    } catch (e) {
      // Errors might be thrown and unhandled by the component
    }
    
    expect(sessionApiService.delete).toHaveBeenCalled();
    expect(matSnackBar.open).not.toHaveBeenCalled();
    expect(router.navigate).not.toHaveBeenCalled();
  });
  
  it('should correctly process users array to determine participation status', () => {
    const sessionWithUser = {...mockSession, users: [1, 2, 3]};

    jest.spyOn(sessionApiService, 'detail').mockReturnValueOnce(of(sessionWithUser));
    component.ngOnInit();
    expect(component.isParticipate).toBeTruthy();
    
    const sessionWithoutUser = {...mockSession, users: [2, 3]};
    
    jest.spyOn(sessionApiService, 'detail').mockReturnValueOnce(of(sessionWithoutUser));
    component.ngOnInit();
    expect(component.isParticipate).toBeFalsy();
  });
});