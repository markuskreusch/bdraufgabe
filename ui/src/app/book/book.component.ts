import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Book} from '../book';
import {BookStatus} from '../bookstatus';
import {BookService} from '../book.service';

@Component({
  selector: 'app-book',
  templateUrl: './book.component.html',
  styleUrls: ['./book.component.css']
})
export class BookComponent implements OnInit {

  enumBookStatus = BookStatus;

  @Input() book: Book;

  @Output() deleted = new EventEmitter<Book>();

  edit = false;
  newAuthor: string;
  newTitle: string;

  constructor(private bookService: BookService) {
  }

  ngOnInit(): void {
  }

  changeStatusTo(status: BookStatus): void {
    this.bookService.updateStatus(this.book, status).subscribe(_ => {
      this.book.status = status;
    }, _ => {
      console.log('Failed to update status');
    });
  }

  delete(): void {
    this.deleted.emit(this.book);
  }

  startEditing(): void {
    this.newAuthor = this.book.author;
    this.newTitle = this.book.title;
    this.edit = true;
  }

  save(): void {
    const title = this.newTitle;
    const author = this.newAuthor;
    this.bookService.updateBook(this.book, author, title).subscribe(
      _ => {
        this.edit = false;
        this.book.title = title;
        this.book.author = author;
      }, _ => {
        console.log('Failed to save book');
        this.edit = false;
      });
  }

}
