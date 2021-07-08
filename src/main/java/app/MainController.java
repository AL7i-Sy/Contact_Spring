package app;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
public class MainController {

	@Autowired
	private CompanyRepository companyRepo;

	@RequestMapping("/")
	public String root() {
		return "home";
	}

	@RequestMapping("/home")
	public String home() {
		return "home";
	}

	@Restrict(admin = true, localOnly = true)
	@RequestMapping("/company")
	public String company(Model model) {
		model.addAttribute("company", new Company());
		return "company";
	}

	@RequestMapping("/company/edit/{id}")
	public String editcompany(@PathVariable int id, Model model) {

		Company c = companyRepo.findOne(id);

		model.addAttribute("company", c);
		return "company";
	}

	
	@RequestMapping(method = RequestMethod.POST, path = "/company")
	public String company(@Valid @ModelAttribute("company") Company company) {
		try {
			companyRepo.save(company);
		} catch (Exception e) {
			return "company";
		}
		return "redirect:/companies";
	}

	@RequestMapping("/companies")
	public String companies(Model model) {

		model.addAttribute("companies", companyRepo.findAll());
		return "companies";
	}

	@RequestMapping("/company/delete/{id}")
	public String deletecompany(@PathVariable int id, Model model) {

		companyRepo.delete(id);

		return "redirect:/companies";
	}

}
