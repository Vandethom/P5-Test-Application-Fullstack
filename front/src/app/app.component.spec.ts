import { HttpClientModule }          from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MatToolbarModule }          from '@angular/material/toolbar';
import { RouterTestingModule }       from '@angular/router/testing';
import { expect }                    from '@jest/globals';
import { AppComponent }              from './app.component';
import { Router }                    from '@angular/router';
import { SessionService }            from './services/session.service';
import { of }                        from 'rxjs';
import { By }                        from '@angular/platform-browser';


describe('AppComponent', () => {
  let fixture       : ComponentFixture<AppComponent>
  let app           : AppComponent;
  let router        : Router;
  let sessionService: SessionService;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        HttpClientModule,
        MatToolbarModule
      ],
      declarations: [
        AppComponent
      ],
    }).compileComponents();

    fixture        = TestBed.createComponent(AppComponent);
    app            = fixture.componentInstance;
    router         = TestBed.inject(Router);
    sessionService = TestBed.inject(SessionService);

  });

  it('should create appComponent', () => {
    expect(app).toBeTruthy();
  });

  it('should logged', (done) => {
    const loggedSpy = jest.spyOn(sessionService, '$isLogged').mockImplementation(() => of((true)));
    app.$isLogged().subscribe((isLogged) => {
      expect(isLogged).toBe(true);
      expect(loggedSpy).toHaveBeenCalledTimes(1);
      done();
    })
  })

  it('should log out', () => {
    const logoutSpy = jest.spyOn(sessionService, 'logOut');
    const routerSpy = jest.spyOn(router, 'navigate').mockImplementation(() => Promise.resolve(true));
    app.logout();
    expect(logoutSpy).toHaveBeenCalled;
    expect(routerSpy).toHaveBeenLastCalledWith(['']);
  })

  it('should log out by clicking the logout button', () => {
    jest.spyOn(sessionService,'$isLogged').mockReturnValue(of(true));
    fixture.detectChanges();

    const logoutSpy = jest.spyOn(sessionService, 'logOut');
    const routerSpy = jest.spyOn(router, 'navigate').mockImplementation(() => Promise.resolve(true));

    const logoutbutton = fixture.debugElement.query(By.css('.link:nth-child(3)')).nativeElement;
    logoutbutton.click();

    expect(logoutSpy).toHaveBeenCalled;
    expect(routerSpy).toHaveBeenLastCalledWith(['']);
  })
});


