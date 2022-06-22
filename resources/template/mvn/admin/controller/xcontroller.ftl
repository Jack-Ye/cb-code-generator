package ${basePackageName}.${projectName}.admin.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import ${basePackageName}.common.pagination.PaginationUtils;
import ${basePackageName}.${projectName}.admin.constant.Constants;
import ${basePackageName}.${projectName}.bo.${modelClassSimpleName}BO;
import ${basePackageName}.${projectName}.model.${modelClassSimpleName};

@Controller
@RequestMapping(value = "/${viewDirName}")
public class ${modelClassSimpleName}Controller extends PaginationController {
    @Autowired
    private ${modelClassSimpleName}BO ${modelClassSimpleName?uncap_first}BO;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(@ModelAttribute("q") ${modelClassSimpleName} q, @RequestParam(defaultValue = "1") int pageNumber, Model model) {
        int totalNumberOfElements = ${modelClassSimpleName?uncap_first}BO.count(q);
        List<${modelClassSimpleName}> elements = ${modelClassSimpleName?uncap_first}BO.find(q, Constants.DEFAULT_PAGE_SIZE, pageNumber);

        model.addAttribute("p", PaginationUtils.newPagination(Constants.DEFAULT_PAGE_SIZE, pageNumber, totalNumberOfElements, elements));

        return "/${viewDirName}/index";
    }

    @RequestMapping(value = "/new", method = RequestMethod.GET)
    public String editNew(@ModelAttribute("redirectUrl") String redirectUrl, Model model) {
        return "/${viewDirName}/edit";
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String create(${modelClassSimpleName} ${modelClassSimpleName?uncap_first}, String redirectUrl) {
        ${modelClassSimpleName?uncap_first}BO.insert(${modelClassSimpleName?uncap_first});

        return "redirect:" + redirectUrl;
    }

    @RequestMapping(value = "/{id}/edit", method = RequestMethod.GET)
    public String edit(@PathVariable @ModelAttribute("id") String id, @ModelAttribute("redirectUrl") String redirectUrl, Model model) {
        ${modelClassSimpleName} ${modelClassSimpleName?uncap_first} = ${modelClassSimpleName?uncap_first}BO.get(id);

        model.addAttribute("${modelClassSimpleName?uncap_first}", ${modelClassSimpleName?uncap_first});

        return "/${viewDirName}/edit";
    }

    @RequestMapping(value = "/{id}/update", method = RequestMethod.POST)
    public String update(${modelClassSimpleName} ${modelClassSimpleName?uncap_first}, @PathVariable String id, String redirectUrl) {
        ${modelClassSimpleName?uncap_first}BO.update(${modelClassSimpleName?uncap_first}, id);

        return "redirect:" + redirectUrl;
    }

    @RequestMapping(value = "/{id}/remove", method = RequestMethod.GET)
    public String remove(@PathVariable String id, String redirectUrl) {
        ${modelClassSimpleName?uncap_first}BO.remove(id);

        return "redirect:" + redirectUrl;
    }
}
