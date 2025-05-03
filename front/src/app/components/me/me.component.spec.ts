import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { ComponentFixture, TestBed }                      from '@angular/core/testing';
import { MatCardModule }                                  from '@angular/material/card';
import { MatFormFieldModule }                             from '@angular/material/form-field';
import { MatIconModule }                                  from '@angular/material/icon';
import { MatInputModule }                                 from '@angular/material/input';
import { MatSnackBar, MatSnackBarModule }                 from '@angular/material/snack-bar';
import { SessionService }                                 from 'src/app/services/session.service';
import { expect, jest }                                   from '@jest/globals';
import { MeComponent }                                    from './me.component';
import { Router }                                         from '@angular/router';
import { UserService }                                    from 'src/app/services/user.service';
import { of, throwError }                                 from 'rxjs';

describe('MeComponent', () => {
  let meComponent          : MeComponent;
  let fixture              : ComponentFixture<MeComponent>;
  let userService          : UserService;
  let router               : Router;
  let matSnackBar          : MatSnackBar;
  let httpTestingController: HttpTestingController;

  const mockSessionService = {
    sessionInformation: {
      admin: true,
      id   : 1,
    },
    logOut: jest.fn(),
  };
  
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [MeComponent],
      imports: [
        MatSnackBarModule,
        HttpClientTestingModule,
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule,
      ],
      providers: [{ 
        provide : SessionService, 
        useValue: mockSessionService 
      }],
    }).compileComponents();

    fixture               = TestBed.createComponent(MeComponent);
    meComponent           = fixture.componentInstance;
    userService           = TestBed.inject(UserService);
    router                = TestBed.inject(Router);
    matSnackBar           = TestBed.inject(MatSnackBar);
    httpTestingController = TestBed.inject(HttpTestingController);
  });
  
  // After each test, verify no pending requests
  afterEach(() => {
    httpTestingController.verify();
  });

  it('should create MeComponent', () => {
    // Use the spy to prevent real HTTP calls
    jest.spyOn(userService, 'getById').mockReturnValue(of({
      id       : 1,
      email    : 'test@example.com',
      firstName: 'Test',
      lastName : 'User',
      admin    : false,
      password : '',
      createdAt: new Date(),
      updatedAt: new Date()
    }));
    fixture.detectChanges();
    expect(meComponent).toBeTruthy();
  });

  it('should delete the user', () => {
    const userSpy = jest
      .spyOn(userService, 'delete')
      .mockImplementation(() => of(undefined));

    const matSnackBarSpy = jest.spyOn(matSnackBar, 'open').mockImplementation(() => {
      return {
        afterDismissed   : () => of({}),
        afterOpened      : () => of({}),
        onAction         : () => of({}),
        dismiss          : () => {},
        dismissWithAction: () => {},
        instance         : {}
      } as any;
    });

    const sessionSpy = jest.spyOn(mockSessionService, 'logOut');
    const routerSpy  = jest.spyOn(router, 'navigate')
                           .mockImplementation(
                            () => Promise.resolve(true)
                          );

    meComponent.delete();

    expect(userSpy).toHaveBeenCalledWith(mockSessionService.sessionInformation.id.toString());
    expect(matSnackBarSpy).toHaveBeenCalledWith("Your account has been deleted !", "Close", {"duration": 3000});
    expect(sessionSpy).toHaveBeenCalled();
    expect(routerSpy).toHaveBeenCalledWith(['/']);
  });
});