package org.example.ppcauction.controller;

import jakarta.validation.Valid;
import org.example.ppcauction.dto.KeywordForm;
import org.example.ppcauction.entity.Keyword;
import org.example.ppcauction.exception.RelatedDataException;
import org.example.ppcauction.service.KeywordService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller @RequestMapping("/keywords")
public class KeywordController {
    private final KeywordService service;
    public KeywordController(KeywordService service){this.service=service;}
    @GetMapping public String list(@RequestParam(defaultValue="") String q,@RequestParam(defaultValue="") String status,Model model){model.addAttribute("items",service.search(q,status));model.addAttribute("q",q);model.addAttribute("status",status);return "keywords/list";}
    @GetMapping("/new") public String create(Model model){model.addAttribute("form",new KeywordForm());model.addAttribute("pageTitle","Thêm từ khóa");return "keywords/form";}
    @GetMapping("/{id}") public String detail(@PathVariable Long id,Model model){model.addAttribute("item",service.get(id));return "keywords/detail";}
    @GetMapping("/{id}/edit") public String edit(@PathVariable Long id,Model model){Keyword k=service.get(id);KeywordForm f=new KeywordForm();f.setPhrase(k.getPhrase());f.setSearchVolume(k.getSearchVolume());f.setCompetition(k.getCompetition());f.setQualityScore(k.getQualityScore());f.setStatus(k.getStatus());model.addAttribute("form",f);model.addAttribute("pageTitle","Sửa từ khóa");model.addAttribute("id",id);return "keywords/form";}
    @PostMapping public String save(@RequestParam(required=false) Long id,@Valid @ModelAttribute("form") KeywordForm form,BindingResult errors,Model model,RedirectAttributes flash){if(errors.hasErrors()){model.addAttribute("pageTitle",id==null?"Thêm từ khóa":"Sửa từ khóa");model.addAttribute("id",id);return "keywords/form";}service.save(id,form);flash.addFlashAttribute("success","Đã lưu từ khóa.");return "redirect:/keywords";}
    @PostMapping("/{id}/delete") public String delete(@PathVariable Long id,RedirectAttributes flash){try{service.delete(id);flash.addFlashAttribute("success","Đã xóa từ khóa.");}catch(RelatedDataException ex){flash.addFlashAttribute("error",ex.getMessage());}return "redirect:/keywords";}
}
