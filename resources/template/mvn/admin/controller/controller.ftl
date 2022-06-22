package ${basePackageName}.${projectName}.admin.controller;

import ${basePackageName}.${projectName}.admin.constant.Constants;
import ${basePackageName}.${projectName}.bo.${modelClassSimpleName}BO;
import ${basePackageName}.${projectName}.common.pagination.PaginationUtils;
import ${basePackageName}.${projectName}.model.${modelClassSimpleName};
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/${viewDirName}")
public class ${modelClassSimpleName}Controller extends PaginationController {
    private final ${modelClassSimpleName}BO ${modelClassSimpleName?uncap_first}BO;

    @GetMapping("/")
    public String index(@ModelAttribute("q") ${modelClassSimpleName} q, @RequestParam(defaultValue = "1") int pageNumber, Model model) {
        int totalNumberOfElements = ${modelClassSimpleName?uncap_first}BO.count(q, false);
        List<${modelClassSimpleName}> elements = ${modelClassSimpleName?uncap_first}BO.find(q, Constants.DEFAULT_PAGE_SIZE, pageNumber, false);

        model.addAttribute("p", PaginationUtils.newPagination(Constants.DEFAULT_PAGE_SIZE, pageNumber, totalNumberOfElements, elements));

        return "/${viewDirName}/index";
    }

    @GetMapping("/new")
    public String editNew(@ModelAttribute("redirectUrl") String redirectUrl, Model model) {
        return "/${viewDirName}/edit";
    }

    @PostMapping("/create")
    public String create(${modelClassSimpleName} ${modelClassSimpleName?uncap_first}, String redirectUrl) {
        ${modelClassSimpleName?uncap_first}BO.insert(${modelClassSimpleName?uncap_first});

        return "redirect:" + redirectUrl;
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable @ModelAttribute("id") long id, @ModelAttribute("redirectUrl") String redirectUrl, Model model) {
        ${modelClassSimpleName} ${modelClassSimpleName?uncap_first} = ${modelClassSimpleName?uncap_first}BO.get(id);

        model.addAttribute("${modelClassSimpleName?uncap_first}", ${modelClassSimpleName?uncap_first});

        return "/${viewDirName}/edit";
    }

    @PostMapping("/{id}/update")
    public String update(${modelClassSimpleName} ${modelClassSimpleName?uncap_first}, @PathVariable long id, String redirectUrl) {
        ${modelClassSimpleName?uncap_first}BO.update(${modelClassSimpleName?uncap_first}, id);

        return "redirect:" + redirectUrl;
    }

    @GetMapping("/{id}/remove")
    public String remove(@PathVariable long id, String redirectUrl) {
        ${modelClassSimpleName?uncap_first}BO.remove(id);

        return "redirect:" + redirectUrl;
    }
}
