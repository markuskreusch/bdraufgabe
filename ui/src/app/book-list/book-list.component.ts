import { Component, OnInit } from '@angular/core';
import {Book} from '../book';
import {BookService} from '../book.service';

@Component({
  selector: 'app-book-list',
  templateUrl: './book-list.component.html',
  styleUrls: ['./book-list.component.css']
})
export class BookListComponent implements OnInit {

  books: Book[] = [];

  newBookAuthor: string;
  newBookTitle: string;

  constructor(private bookService: BookService) { }

  ngOnInit(): void {
    this.bookService.getBooks().subscribe(books => {
      this.books = books;
    });
  }

  addBook(): void {
    this.bookService.add(this.newBookAuthor, this.newBookTitle).subscribe(book => {
      this.books.push(book);
      this.newBookTitle = '';
      this.newBookAuthor = '';
    });
  }

  onDeleted(book: Book): void {
    this.bookService.delete(book).subscribe(_ => {
      const index: number = this.books.indexOf(book);
      this.books.splice(index, 1);
    });
  }

}
