import { HttpClientModule }                         from '@angular/common/http';
import { CUSTOM_ELEMENTS_SCHEMA, NO_ERRORS_SCHEMA } from '@angular/core';
import { ComponentFixture, TestBed }                from '@angular/core/testing';
import { MatCardModule }                            from '@angular/material/card';
import { MatIconModule }                            from '@angular/material/icon';
import { expect }                                   from '@jest/globals';
import { SessionService }                           from 'src/app/services/session.service';
import { of }                                       from 'rxjs';
import { SessionApiService }                        from '../../services/session-api.service';
import { ListComponent }                            from './list.component';

describe('ListComponent', () => {
  let component     : ListComponent;
  let fixture       : ComponentFixture<ListComponent>;
  let sessionService: SessionService;

  const mockSessionService = {
    sessionInformation: {
      admin: true,
      id   : 1
    }
  };

  const mockSessionApiService = {
    all: jest.fn().mockReturnValue(of([]))
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ListComponent],
      imports: [HttpClientModule, MatCardModule, MatIconModule],
      providers: [
        { provide: SessionService,    useValue: mockSessionService },
        { provide: SessionApiService, useValue: mockSessionApiService }
      ],
      schemas: [CUSTOM_ELEMENTS_SCHEMA, NO_ERRORS_SCHEMA]
    })
      .compileComponents();

    fixture        = TestBed.createComponent(ListComponent);
    component      = fixture.componentInstance;
    sessionService = TestBed.inject(SessionService);
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should handle the case when user is undefined', async () => {
    const mockServiceWithoutUser = {
      sessionInformation: undefined
    };

    await TestBed.resetTestingModule()
      .configureTestingModule({
        declarations: [ListComponent],
        imports: [HttpClientModule, MatCardModule, MatIconModule],
        providers: [
          { provide: SessionService,    useValue: mockServiceWithoutUser },
          { provide: SessionApiService, useValue: mockSessionApiService }
        ],
        schemas: [CUSTOM_ELEMENTS_SCHEMA, NO_ERRORS_SCHEMA]
      })
      .compileComponents();

    fixture = TestBed.createComponent(ListComponent);
    component = fixture.componentInstance;
    
    expect(component.user).toBeUndefined();
  });
});