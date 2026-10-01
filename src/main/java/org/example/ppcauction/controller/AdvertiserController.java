package org.example.ppcauction.controller;

import jakarta.validation.Valid;
import org.example.ppcauction.dto.AdvertiserForm;
import org.example.ppcauction.entity.Advertiser;
import org.example.ppcauction.exception.RelatedDataException;
import org.example.ppcauction.service.AdvertiserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller @RequestMapping("/advertisers")
public class AdvertiserController {
    private final AdvertiserService service;
    public AdvertiserController(AdvertiserService service){this.service=service;}
    @GetMapping public String list(@RequestParam(defaultValue="") String q,Model model){model.addAttribute("items",service.search(q));model.addAttribute("q",q);return "advertisers/list";}
    @GetMapping("/new") public String create(Model model){model.addAttribute("form",new AdvertiserForm());model.addAttribute("pageTitle","Thêm nhà quảng cáo");return "advertisers/form";}
    @GetMapping("/{id}") public String detail(@PathVariable Long id,Model model){model.addAttribute("item",service.get(id));return "advertisers/detail";}
    @GetMapping("/{id}/edit") public String edit(@PathVariable Long id,Model model){Advertiser a=service.get(id);AdvertiserForm f=new AdvertiserForm();f.setName(a.getName());f.setCompany(a.getCompany());f.setEmail(a.getEmail());f.setPhone(a.getPhone());f.setBudget(a.getBudget());f.setStatus(a.getStatus());model.addAttribute("form",f);model.addAttribute("pageTitle","Sửa nhà quảng cáo");model.addAttribute("id",id);return "advertisers/form";}
    @PostMapping public String save(@RequestParam(required=false) Long id,@Valid @ModelAttribute("form") AdvertiserForm form,BindingResult errors,Model model,RedirectAttributes flash){if(errors.hasErrors()){model.addAttribute("pageTitle",id==null?"Thêm nhà quảng cáo":"Sửa nhà quảng cáo");model.addAttribute("id",id);return "advertisers/form";}service.save(id,form);flash.addFlashAttribute("success","Đã lưu nhà quảng cáo.");return "redirect:/advertisers";}
    @PostMapping("/{id}/delete") public String delete(@PathVariable Long id,RedirectAttributes flash){try{service.delete(id);flash.addFlashAttribute("success","Đã xóa nhà quảng cáo.");}catch(RelatedDataException ex){flash.addFlashAttribute("error",ex.getMessage());}return "redirect:/advertisers";}
}
