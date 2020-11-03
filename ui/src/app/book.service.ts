import {Injectable} from '@angular/core';
import {Book} from './book';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {BookStatus} from './bookstatus';

@Injectable({
  providedIn: 'root'
})
export class BookService {

  private baseUri = 'http://localhost:8080/';

  constructor(private http: HttpClient) { }

  getBooks(): Observable<Book[]> {
    return this.http.get(`${this.baseUri}/books`) as Observable<Book[]>;
  }

  add(author: string, title: string): Observable<Book> {
    const book: Book = {
      id: null,
      title,
      author,
      status: BookStatus.Available
    };
    return this.http.post(`${this.baseUri}/books`, book) as Observable<Book>;
  }

  delete(book: Book): Observable<any> {
    return this.http.delete(`${this.baseUri}/books/${book.id}`) as Observable<any>;
  }

  updateStatus(book: Book, status: BookStatus): Observable<any> {
    return this.http.put(`${this.baseUri}/books/${book.id}`, {
      status
    }) as Observable<any>;
  }

  updateBook(book: Book, author: string, title: string): Observable<any> {
    return this.http.put(`${this.baseUri}/books/${book.id}`, {
      author,
      title
    }) as Observable<any>;
  }

}
