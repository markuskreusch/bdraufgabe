import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {Book} from '../book';
import {BookService} from '../book.service';
import {MatDialogRef} from '@angular/material/dialog';

@Component({
  selector: 'app-add-book',
  templateUrl: './add-book.component.html',
  styleUrls: ['./add-book.component.css']
})
export class AddBookComponent implements OnInit {

  newBookAuthor: string;
  newBookTitle: string;

  constructor(
    private bookService: BookService,
    public dialogRef: MatDialogRef<AddBookComponent>) { }

  ngOnInit(): void {
  }

  addBook(): void {
    this.bookService.add(this.newBookAuthor, this.newBookTitle).subscribe(
      book => {
        this.newBookTitle = '';
        this.newBookAuthor = '';
        this.dialogRef.close(book);
      }, _ => {
        console.log('Failed to create book');
      });
  }

  close(): void {
    this.dialogRef.close();
  }
}
