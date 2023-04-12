package com.bekrenov.clinic.rest_controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/addresses")
public class AddressRestController {

    @GetMapping("/cities")
    public List<String> findCitiesByPattern(@RequestParam("pattern") String pattern) throws IOException {
        List<String> list = Files.readAllLines(Paths.get("src/main/resources/cities.txt"), StandardCharsets.UTF_8);
        List<String> result = new ArrayList<>();
        list.forEach(line -> {
            if(line.toLowerCase().contains(pattern.toLowerCase())){
                result.add(line);
            }
        });
        return result;
    }
}
