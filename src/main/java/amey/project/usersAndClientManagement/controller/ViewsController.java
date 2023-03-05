package amey.project.usersAndClientManagement.controller;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

@Controller
public class ViewsController {
    @GetMapping(value = "/registerClient")
    @ResponseBody
    public ModelAndView registerClient(){
        ModelAndView mv = new ModelAndView("registerClient");
        return mv;
    }

}
