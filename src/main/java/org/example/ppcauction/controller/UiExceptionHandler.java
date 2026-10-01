package org.example.ppcauction.controller;

import org.example.ppcauction.exception.ResourceNotFoundException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ControllerAdvice(assignableTypes={AdvertiserController.class,KeywordController.class,CampaignController.class,PageController.class,AuctionController.class,CampaignSimulationController.class,HistoryController.class})
public class UiExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class) @ResponseStatus(HttpStatus.NOT_FOUND)
    public String notFound(ResourceNotFoundException ex,Model model){model.addAttribute("message",ex.getMessage());return "error/404";}
    @ExceptionHandler(IllegalArgumentException.class) @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String invalid(IllegalArgumentException ex,Model model){model.addAttribute("message",ex.getMessage());return "error/500";}
    @ExceptionHandler(Exception.class) @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String internal(Exception ex,Model model){model.addAttribute("message","Đã xảy ra lỗi khi xử lý yêu cầu. Vui lòng thử lại.");return "error/500";}
}
