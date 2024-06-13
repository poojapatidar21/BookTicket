package com.myplantdiary.enterprise.controller;

import com.myplantdiary.enterprise.entity.TrainDetails;
import com.myplantdiary.enterprise.entity.User;
import com.myplantdiary.enterprise.service.TrainService;
import com.myplantdiary.enterprise.service.TrainService1;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class TrainController {

    @Autowired
    private TrainService1 trainService1;

    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        else if (!user.getRole().equalsIgnoreCase("admin"))
        {
            // SET ERROR MESSAGE WHILE REDIRECTING TO HOMEPAGE
            return "redirect:/home";
        }
        trainService1.saveDataFromCsv(file);
        redirectAttributes.addFlashAttribute("message", "File uploaded successfully!");
        return "redirect:/admin/confirmationpage";
    }
    @GetMapping("/admin/confirmationpage")
    public String showConfirmationPage(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        else if (!user.getRole().equalsIgnoreCase("admin"))
        {
            // SET ERROR MESSAGE WHILE REDIRECTING TO HOMEPAGE
            return "redirect:/home";
        }
        List<TrainDetails> trains = trainService1.getAllTrains(); // Replace with your actual method
        model.addAttribute("trains", trains);
        // Your code here
        return "/admin/confirmationpage";
    }
    @GetMapping("/trains")
    public String showTrainsInfo(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        List<TrainDetails> trains = trainService1.getAllTrains(); // Replace with your actual method
        model.addAttribute("trains", trains);
        return "trains";
    }

//    @RequestMapping("/trains")
//    public String trains(HttpSession session) {
////        User user = (User) session.getAttribute("user");
////        if (user == null) {
////            return "redirect:/login";
////        }
//        return "trains";
//    }


    // Add additional mappings as needed
}

