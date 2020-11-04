import {browser, by, element, ElementArrayFinder, ElementFinder} from 'protractor';

export class AppPage {

  navigateTo(): Promise<unknown> {
    return browser.get(browser.baseUrl) as Promise<unknown>;
  }

  getTitleText(): Promise<string> {
    return element(by.css('app-root h1')).getText() as Promise<string>;
  }

  deleteAllBooks(): void {
    element.all(by.css('app-book .delete-button')).click();
  }

  getBookDeleteButton(): ElementFinder {
    return element(by.css('app-book .delete-button'));
  }

  getBookText(): Promise<string> {
    return element(by.css('app-book p')).getText() as Promise<string>;
  }

  getAddBookAuthor(): ElementFinder {
    return element(by.css('.add-book input[name="author"]'));
  }

  getAddBookTitle(): ElementFinder {
    return element(by.css('.add-book input[name="title"]'));
  }

  getAddBookButton(): ElementFinder {
    return element(by.css('.add-book button'));
  }

  getBooks(): ElementArrayFinder {
    return element.all(by.css('app-book'));
  }

}
