package app;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class SecurityController {

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private CompanyRepository companyRepo;

	@Autowired
	private MyUserDetailsService myUserDetailsService;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private TempUserValidator tempUserValidator;

	@InitBinder("tempuser")
	public void initBinderTempUser(WebDataBinder binder) {
		binder.addValidators(tempUserValidator);
	}

	@RequestMapping("/login")
	public String loginForm(Authentication auth) {
		if (auth == null) {
			return "login";
		} else {
			return "redirect:/";
		}

	}

	@RequestMapping("/accessdenied")
	public String accessdeniedForm() {
		return "accessdenied";
	}

	@RequestMapping("/signup")
	public String signup(Model model) {
		model.addAttribute("tempuser", new TempUser());
		model.addAttribute("companies", companyRepo.findAll());
		return "signup";
	}

	@RequestMapping(value = "/signup", method = RequestMethod.POST)
	public String signup(@Valid @ModelAttribute("tempuser") TempUser tempuser, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return "signup";
		}
		try {

			User temp = new User();
			temp.setFullname(tempuser.getFullname());
			temp.setUsername(tempuser.getUsername());
			temp.setPasswordHash(new BCryptPasswordEncoder().encode(tempuser.getPassword()));
			temp.setEmail(tempuser.getEmail());
			temp.setRole("User");
			temp.setCompany(tempuser.getCompany());
			userRepo.save(temp);

			LoggedUser cu = myUserDetailsService.loadUserByUsername(temp.getUsername());
			UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
					cu, tempuser.getPassword(), cu.getAuthorities());

			authenticationManager.authenticate(usernamePasswordAuthenticationToken);

			if (usernamePasswordAuthenticationToken.isAuthenticated()) {
				SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
			}

		} catch (Exception e) {
			ObjectError error = new ObjectError("user.signup", e.getMessage());

			bindingResult.addError(error);
			return "signup";
		}
		return "redirect:/home";
	}

	@RequestMapping("/users/newuser")
	public String createuser(Model model) {
		model.addAttribute("tempuser", new TempUser());
		model.addAttribute("companies", companyRepo.findAll());
		return "newuser";
	}

	@RequestMapping(value = "/users/newuser", method = RequestMethod.POST)
	public String createuser(@Valid @ModelAttribute("tempuser") TempUser tempuser, BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			return "newuser";
		}
		try {
			User original = null;
			User temp = new User();
			if (tempuser.getId() != 0) {
				original = userRepo.findOne(tempuser.getId());
				temp.setId(original.getId());
				temp.setFullname(tempuser.getFullname());
				temp.setUsername(tempuser.getUsername());
				temp.setPasswordHash(original.getPasswordHash());
				temp.setEmail(tempuser.getEmail());
				temp.setRole(tempuser.getRole());
				temp.setCompany(tempuser.getCompany());
			} else {
				temp.setFullname(tempuser.getFullname());
				temp.setUsername(tempuser.getUsername());
				temp.setPasswordHash(new BCryptPasswordEncoder().encode(tempuser.getPassword()));
				temp.setEmail(tempuser.getEmail());
				temp.setRole(tempuser.getRole());
				temp.setCompany(tempuser.getCompany());
			}

			userRepo.save(temp);
		} catch (Exception e) {
			bindingResult.reject("user.create", e.getMessage());
			return "newuser";
		}
		return "redirect:/users/users";
	}

	@RequestMapping("/users/edit/{id}")
	public String edituser(@PathVariable int id, Model model) {

		User u = userRepo.findOne(id);
		TempUser ucf = new TempUser();
		ucf.setId(u.getId());
		ucf.setEmail(u.getEmail());
		ucf.setFullname(u.getFullname());
		ucf.setPassword("");
		ucf.setPassword2("");
		ucf.setCompany(u.getCompany());
		ucf.setRole(u.getRole());
		ucf.setUsername(u.getUsername());

		model.addAttribute("tempuser", ucf);
		model.addAttribute("companies", companyRepo.findAll());
		return "newuser";

	}
	
	@RequestMapping("/users/view/{id}")
	public String viewuser(@PathVariable int id, Model model) {
		model.addAttribute("user", userRepo.findOne(id));
		return "viewuser";

	}
	
	@RequestMapping("/users/users")
	public String users(Model model) {
		model.addAttribute("users", userRepo.findAll());
		return "users";
	}

	

	

	@RequestMapping("/users/delete/{id}")
	public String deleteuser(@PathVariable int id) {
		userRepo.delete(id);
		return "redirect:/users/users";

	}
}
