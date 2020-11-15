import { AppPage } from './app.po';
import { browser, logging } from 'protractor';

describe('workspace-project App', () => {
  let page: AppPage;

  beforeEach(() => {
    page = new AppPage();
    page.navigateTo();
    page.deleteAllBooks();
  });

  it('should display title', () => {
    expect(page.getTitleText()).toEqual('Books');
  });

  it('should create book', () => {
    page.getAddBookButton().click();
    page.getAddBookForm().getAuthor().sendKeys('TestAuthor');
    page.getAddBookForm().getTitle().sendKeys('TestTitle');
    page.getAddBookForm().getAddButton().click();

    expect(page.getBookText()).toEqual('TestAuthor - TestTitle');
  });

  it('should delete book', () => {
    page.getAddBookButton().click();
    page.getAddBookForm().getAuthor().sendKeys('TestAuthor');
    page.getAddBookForm().getTitle().sendKeys('TestTitle');
    page.getAddBookForm().getAddButton().click();

    page.getBookDeleteButton().click();

    expect(page.getBooks.length).toEqual(0);
  });

  afterEach(async () => {
    // Assert that there are no errors emitted from the browser
    const logs = await browser.manage().logs().get(logging.Type.BROWSER);
    expect(logs).not.toContain(jasmine.objectContaining({
      level: logging.Level.SEVERE,
    } as logging.Entry));
  });
});
