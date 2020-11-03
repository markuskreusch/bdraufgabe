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

  constructor(private bookService: BookService) { }

  ngOnInit(): void {
  }

  changeStatusTo(status: BookStatus): void {
    this.bookService.updateStatus(this.book, status).subscribe(_ => {
      this.book.status = status;
    });
  }

  delete(): void {
    this.deleted.emit(this.book);
  }

  save(): void {
    this.bookService.updateBook(this.book, this.book.author, this.book.title).subscribe(_ => {
      this.edit = false;
    });
  }

}
