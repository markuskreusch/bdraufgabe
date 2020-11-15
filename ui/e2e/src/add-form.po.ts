import {browser, by, element, ElementArrayFinder, ElementFinder} from 'protractor';

export class AddForm {

  getAuthor(): ElementFinder {
    return element(by.css('app-add-book input[name="author"]'));
  }

  getTitle(): ElementFinder {
    return element(by.css('app-add-book input[name="title"]'));
  }

  getAddButton(): ElementFinder {
    return element(by.css('app-add-book button.add'));
  }

  getCancelButton(): ElementFinder {
    return element(by.css('app-add-book button.cancel'));
  }

}
