package org.example.ppcauction.controller;

import jakarta.validation.Valid;
import org.example.ppcauction.dto.CampaignForm;
import org.example.ppcauction.entity.Campaign;
import org.example.ppcauction.exception.RelatedDataException;
import org.example.ppcauction.repository.AdvertiserRepository;
import org.example.ppcauction.repository.KeywordRepository;
import org.example.ppcauction.service.CampaignService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller @RequestMapping("/campaigns")
public class CampaignController {
    private final CampaignService service; private final AdvertiserRepository advertisers; private final KeywordRepository keywords;
    public CampaignController(CampaignService service,AdvertiserRepository advertisers,KeywordRepository keywords){this.service=service;this.advertisers=advertisers;this.keywords=keywords;}
    private void references(Model m){m.addAttribute("advertisers",advertisers.findAll());m.addAttribute("keywords",keywords.findAll());}
    @GetMapping public String list(@RequestParam(defaultValue="") String q,@RequestParam(defaultValue="") String status,Model model){model.addAttribute("items",service.search(q,status));model.addAttribute("q",q);model.addAttribute("status",status);return "campaigns/list";}
    @GetMapping("/new") public String create(Model model){model.addAttribute("form",new CampaignForm());model.addAttribute("pageTitle","Thêm chiến dịch");references(model);return "campaigns/form";}
    @GetMapping("/{id}") public String detail(@PathVariable Long id,Model model){model.addAttribute("item",service.get(id));return "campaigns/detail";}
    @GetMapping("/{id}/edit") public String edit(@PathVariable Long id,Model model){Campaign c=service.get(id);CampaignForm f=new CampaignForm();f.setName(c.getName());f.setAdvertiserId(c.getAdvertiser().getId());f.setBudget(c.getBudget());f.setDailyBudget(c.getDailyBudget());f.setStartDate(c.getStartDate());f.setEndDate(c.getEndDate());f.setStatus(c.getStatus());f.setDescription(c.getDescription());f.setKeywordIds(c.getKeywordSettings().stream().map(s->s.getKeyword().getId()).toList());model.addAttribute("form",f);model.addAttribute("pageTitle","Sửa chiến dịch");model.addAttribute("id",id);references(model);return "campaigns/form";}
    @PostMapping public String save(@RequestParam(required=false) Long id,@Valid @ModelAttribute("form") CampaignForm form,BindingResult errors,Model model,RedirectAttributes flash){if(form.getStartDate()!=null&&form.getEndDate()!=null&&form.getEndDate().isBefore(form.getStartDate()))errors.rejectValue("endDate","date.order","Ngày kết thúc phải bằng hoặc sau ngày bắt đầu.");if(errors.hasErrors()){model.addAttribute("pageTitle",id==null?"Thêm chiến dịch":"Sửa chiến dịch");model.addAttribute("id",id);references(model);return "campaigns/form";}service.save(id,form);flash.addFlashAttribute("success","Đã lưu chiến dịch.");return "redirect:/campaigns";}
    @PostMapping("/{id}/delete") public String delete(@PathVariable Long id,RedirectAttributes flash){try{service.delete(id);flash.addFlashAttribute("success","Đã xóa chiến dịch.");}catch(RelatedDataException ex){flash.addFlashAttribute("error",ex.getMessage());}return "redirect:/campaigns";}
}
