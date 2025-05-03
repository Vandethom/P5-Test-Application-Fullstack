import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { User } from '../interfaces/user.interface';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private pathService = '/user';

  constructor(private httpClient: HttpClient) { }

  public getById(id: string): Observable<User> {
    return this.httpClient.get<User>(`${environment.baseUrl}${this.pathService}/${id}`);
  }

  public delete(id: string): Observable<any> {
    return this.httpClient.delete(`${environment.baseUrl}${this.pathService}/${id}`);
  }
}
