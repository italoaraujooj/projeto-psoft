package com.ufcg.psoft.scrumboard.advice;

import com.ufcg.psoft.scrumboard.exception.*;
import com.ufcg.psoft.scrumboard.exception.model.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ApiExceptionHandler {

    // Genreal Exception
    @ExceptionHandler(value = {ApiRequestException.class})
    public ResponseEntity<Object> handleApiRequestException(ApiRequestException e) {
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;

        ApiException apiException = new ApiException(
                badRequest,
                e.getMessage()
        );

        return new ResponseEntity<>(apiException, badRequest);
    }

    //User Exceptions
    @ExceptionHandler(value = {UserNotFoundException.class})
    public ResponseEntity<Object> handleApiRequestException(UserNotFoundException e) {
        HttpStatus noFoundRequest = HttpStatus.NOT_FOUND;

        ApiException apiException = new ApiException(
                noFoundRequest,
                e.getMessage()
        );

        return new ResponseEntity<>(apiException, noFoundRequest);
    }

    @ExceptionHandler(value = {UserNotBeScrumMaster.class})
    public ResponseEntity<Object> handleApiRequestException(UserNotBeScrumMaster e) {
        HttpStatus noFoundRequest = HttpStatus.NOT_FOUND;

        ApiException apiException = new ApiException(
                noFoundRequest,
                e.getMessage()
        );

        return new ResponseEntity<>(apiException, noFoundRequest);
    }

    @ExceptionHandler(value = {UserNotBeProjectOwner.class})
    public ResponseEntity<Object> handleApiRequestException(UserNotBeProjectOwner e) {
        HttpStatus noFoundRequest = HttpStatus.NOT_FOUND;

        ApiException apiException = new ApiException(
                noFoundRequest,
                e.getMessage()
        );

        return new ResponseEntity<>(apiException, noFoundRequest);
    }


    @ExceptionHandler(value = {UserCanNotBeProjectOwner.class})
    public ResponseEntity<Object> handleApiRequestException(UserCanNotBeProjectOwner e) {
        HttpStatus noFoundRequest = HttpStatus.NOT_FOUND;

        ApiException apiException = new ApiException(
                noFoundRequest,
                e.getMessage()
        );

        return new ResponseEntity<>(apiException, noFoundRequest);
    }

    @ExceptionHandler(value = {ScrumMasterAutoDeleteException.class})
    public ResponseEntity<Object> handleApiRequestException(ScrumMasterAutoDeleteException e) {
        HttpStatus noFoundRequest = HttpStatus.NOT_FOUND;

        ApiException apiException = new ApiException(
                noFoundRequest,
                e.getMessage()
        );

        return new ResponseEntity<>(apiException, noFoundRequest);
    }

    @ExceptionHandler(value = {ScrumMasterEditPositionException.class})
    public ResponseEntity<Object> handleApiRequestException(ScrumMasterEditPositionException e) {
        HttpStatus noFoundRequest = HttpStatus.NOT_FOUND;

        ApiException apiException = new ApiException(
                noFoundRequest,
                e.getMessage()
        );

        return new ResponseEntity<>(apiException, noFoundRequest);
    }

    //Task Exceptions

    @ExceptionHandler(value = {EmptyException.class})
    public ResponseEntity<Object> handleApiRequestException(EmptyException e) {
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;

        ApiException apiException = new ApiException(
                badRequest,
                e.getMessage()
        );

        return new ResponseEntity<>(apiException, badRequest);
    }

    @ExceptionHandler(value = {EmptyTitleException.class})
    public ResponseEntity<Object> handleApiRequestException(EmptyTitleException e) {
        HttpStatus partialRequest = HttpStatus.PARTIAL_CONTENT;

        ApiException apiException = new ApiException(
                partialRequest,
                e.getMessage()
        );

        return new ResponseEntity<>(apiException, partialRequest);
    }

    @ExceptionHandler(value = {EmptyDescriptionException.class})
    public ResponseEntity<Object> handleApiRequestException(EmptyDescriptionException e) {
        HttpStatus partialRequest = HttpStatus.PARTIAL_CONTENT;

        ApiException apiException = new ApiException(
                partialRequest,
                e.getMessage()
        );

        return new ResponseEntity<>(apiException, partialRequest);
    }

    @ExceptionHandler(value = {AlreadyExistException.class})
    public ResponseEntity<Object> handleApiRequestException(AlreadyExistException e) {
        HttpStatus conflictRequest = HttpStatus.CONFLICT;

        ApiException apiException = new ApiException(
                conflictRequest,
                e.getMessage()
        );

        return new ResponseEntity<>(apiException, conflictRequest);
    }

    @ExceptionHandler(value = {TitleAlreadyExistException.class})
    public ResponseEntity<Object> handleApiRequestException(TitleAlreadyExistException e) {
        HttpStatus conflictRequest = HttpStatus.CONFLICT;

        ApiException apiException = new ApiException(
                conflictRequest,
                e.getMessage()
        );

        return new ResponseEntity<>(apiException, conflictRequest);
    }

    @ExceptionHandler(value = {DescriptionAlreadyExistException.class})
    public ResponseEntity<Object> handleApiRequestException(DescriptionAlreadyExistException e) {
        HttpStatus conflictRequest = HttpStatus.CONFLICT;

        ApiException apiException = new ApiException(
                conflictRequest,
                e.getMessage()
        );

        return new ResponseEntity<>(apiException, conflictRequest);
    }

    @ExceptionHandler(value = {TaskNotFoundException.class})
    public ResponseEntity<Object> handleApiRequestException(TaskNotFoundException e) {
        HttpStatus noFoundRequest = HttpStatus.NOT_FOUND;

        ApiException apiException = new ApiException(
                noFoundRequest,
                e.getMessage()
        );

        return new ResponseEntity<>(apiException, noFoundRequest);
    }

    @ExceptionHandler(value = {EmptyRepositoryException.class})
    public ResponseEntity<Object> handleApiRequestException(EmptyRepositoryException e) {
        HttpStatus noContentRequest = HttpStatus.NO_CONTENT;

        ApiException apiException = new ApiException(
                noContentRequest,
                e.getMessage()
        );

        return new ResponseEntity<>(apiException, noContentRequest);
    }

    //Project Exceptions

    @ExceptionHandler(value = {ProjectNotFoundException.class})
    public ResponseEntity<Object> handleApiRequestException(ProjectNotFoundException e) {
        HttpStatus noContentRequest = HttpStatus.NOT_FOUND;

        ApiException apiException = new ApiException(
                noContentRequest,
                e.getMessage()
        );

        return new ResponseEntity<>(apiException, noContentRequest);
    }


    //User Story Exceptions

    @ExceptionHandler(value = {UserStoryNotFoundException.class})
    public ResponseEntity<Object> handleApiRequestException(UserStoryNotFoundException e) {
        HttpStatus noFoundRequest = HttpStatus.NOT_FOUND;

        ApiException apiException = new ApiException(
                noFoundRequest,
                e.getMessage()
        );

        return new ResponseEntity<>(apiException, noFoundRequest);
    }

    @ExceptionHandler(value = {WithoutPermissionException.class})
    public ResponseEntity<Object> handleApiRequestException(WithoutPermissionException e) {
        HttpStatus noFoundRequest = HttpStatus.CONFLICT;

        ApiException apiException = new ApiException(
                noFoundRequest,
                e.getMessage()
        );

        return new ResponseEntity<>(apiException, noFoundRequest);
    }

}
