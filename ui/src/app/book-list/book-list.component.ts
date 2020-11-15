import {Component, OnInit} from '@angular/core';
import {Book} from '../book';
import {BookService} from '../book.service';
import {MatDialog} from '@angular/material/dialog';
import {AddBookComponent} from '../add-book/add-book.component';

@Component({
  selector: 'app-book-list',
  templateUrl: './book-list.component.html',
  styleUrls: ['./book-list.component.css']
})
export class BookListComponent implements OnInit {

  books: Book[] = [];

  loading = true;
  error = false;

  constructor(
    private bookService: BookService,
    private dialog: MatDialog) {
  }

  ngOnInit(): void {
    this.bookService.getBooks().subscribe(
      books => {
        this.books = books;
        this.loading = false;
      }, _ => {
        console.log('Failed to load books');
        this.error = true;
        this.loading = false;
      });
  }

  onDeleted(book: Book): void {
    this.bookService.delete(book).subscribe(
      _ => {
        const index: number = this.books.indexOf(book);
        this.books.splice(index, 1);
      }, _ => {
        console.log('Failed to delete book');
      });
  }

  add(): void {
    this.dialog.open(AddBookComponent).afterClosed().subscribe(book => {
      if (book != null) {
        this.books.push(book);
      }
    });
  }
}
