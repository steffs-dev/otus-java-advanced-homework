package ru.otus.java.advanced.project.lesson_29_spring_data_jdbc.controllers;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.otus.java.advanced.project.lesson_29_spring_data_jdbc.dto.ClientDtoResp;
import ru.otus.java.advanced.project.lesson_29_spring_data_jdbc.services.ClientService;

import java.util.List;

@Controller
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ClientController {
    ClientService clientService;

 @GetMapping("/api/client")
    public String getAllClientsView(Model model){
        List<ClientDtoResp> resp = clientService.findAll();
        model.addAttribute("clients", resp);
        return "clients";
    }

    @GetMapping("/")
    public String getDef(Model model){
        return "error";
    }

}
