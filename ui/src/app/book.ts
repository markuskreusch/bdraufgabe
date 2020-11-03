import {BookStatus} from './bookstatus';

export interface Book {
  id: string;
  title: string;
  author: string;
  status: BookStatus;
}
