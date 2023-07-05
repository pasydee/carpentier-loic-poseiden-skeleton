package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.services.CurvePointService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/curvePoint")
public class CurveController {

    private final CurvePointService curvePointService;

    public CurveController(CurvePointService curvePointService) {
        this.curvePointService = curvePointService;
    }

    @GetMapping("/list")
    public String home(Model model) {
        model.addAttribute("curvePoints", curvePointService.findAll());
        return "curvePoint/list";
    }

    @GetMapping("/add")
    public String addCurveForm(CurvePoint curvePoint) {
        return "curvePoint/add";
    }

    @PostMapping("/validate")
    public String validate(@Valid CurvePoint curvePoint, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "curvePoint/add";
        }

        curvePointService.save(curvePoint);
        return "redirect:/curvePoint/list";
    }

    @GetMapping("/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        CurvePoint curvePoint = curvePointService.findById(id);
        model.addAttribute("curvePoint", curvePoint);
        return "curvePoint/update";
    }

    @PostMapping("/update/{id}")
    public String updateCurve(@PathVariable("id") Integer id,
                              @Valid CurvePoint curvePoint,
                              BindingResult result,
                              Model model) {

        if (result.hasErrors()) {
            curvePoint.setId(id);
            return "curvePoint/update";
        }

        curvePointService.update(id, curvePoint);
        return "redirect:/curvePoint/list";
    }

    @GetMapping("/delete/{id}")
    public String deleteCurve(@PathVariable("id") Integer id) {
        curvePointService.delete(id);
        return "redirect:/curvePoint/list";
    }
}
