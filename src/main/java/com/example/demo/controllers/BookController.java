package com.example.demo.controllers;

import com.example.demo.core.service.BookService;
import com.example.demo.model.Book;
import com.example.demo.support.exceptions.BookAlreadyExistsException;
import com.example.demo.support.exceptions.BookNameFieldRequiredException;
import com.example.demo.support.exceptions.BookNotFoundException;
import com.example.demo.support.responses.CustomError;
import com.example.demo.support.responses.CustomResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/v2/book")
@CrossOrigin
public class BookController {

    BookService bookService;

    @Autowired
    public void setService(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping()
    public ResponseEntity getBooks(){
        List<Book> books = bookService.getBooks();
        return new ResponseEntity(new CustomResponse(books), HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity createBook(@RequestBody Book book){
        Book newBook = null;
        try {
            newBook = bookService.createBook(book.getName(),book.getAuthor());
            return new ResponseEntity(new CustomResponse(newBook), HttpStatus.OK);

        } catch (BookAlreadyExistsException e){
            return new ResponseEntity(new CustomError(e.getMessage()),HttpStatus.BAD_REQUEST);
        } catch (BookNameFieldRequiredException e){
            return new ResponseEntity(new CustomError(e.getMessage()),HttpStatus.CONFLICT);
        } catch (Exception ex) {
            return new ResponseEntity(new CustomError(ex.getMessage()),HttpStatus.BAD_REQUEST);
        }

    }

    @PutMapping("/{id}")
    public ResponseEntity updateBook(@PathVariable int id, @RequestBody Book book) throws Exception {
        try{
            Book returnBook = bookService.updateBook(id,book.getName(),book.getAuthor());
            return new ResponseEntity(new CustomResponse(returnBook),HttpStatus.OK);
        }catch (BookAlreadyExistsException e){
            return new ResponseEntity(new CustomError(e.getMessage()),HttpStatus.BAD_REQUEST);
        } catch (BookNameFieldRequiredException e){
            return new ResponseEntity(new CustomError(e.getMessage()),HttpStatus.CONFLICT);
        } catch(BookNotFoundException e){
            return new ResponseEntity(new CustomError(e.getMessage()),HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("/{id}")
    public ResponseEntity getBook(@PathVariable int id) throws Exception {
        try{
            Book returnBook = bookService.getBook(id);
            return new ResponseEntity(new CustomResponse(returnBook),HttpStatus.OK);
        } catch (BookNotFoundException e){
            return new ResponseEntity(new CustomError(e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteBook(@PathVariable int id) throws Exception {
        try{
            bookService.deleteBook(id);
            return new ResponseEntity(HttpStatus.OK);
        } catch (BookNotFoundException e){
            return new ResponseEntity(new CustomError(e.getMessage()), HttpStatus.NOT_FOUND);
        }


    }

}
