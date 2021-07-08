package app;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.hibernate.StaleObjectStateException;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
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
public class ContactController {

	@Autowired
	private ContactRepository contactRepo;
	@Autowired
	private UserRepository userRepo;

	@Autowired
	private ContactValidator contactValidator;

	@Autowired
	private RuntimeService runtimeService;

	@Autowired
	private TaskService taskService;

	@InitBinder("contact")
	public void initBinderContact(WebDataBinder binder) {
		binder.addValidators(contactValidator);
	}

	@RequestMapping("/contact")
	public String contact(Model model) {
		model.addAttribute("contact", new Contact());

		return "contact";
	}

	@RequestMapping("/contact/edit/{id}")
	public String editcontact(@PathVariable int id, Model model) {

		Contact add = contactRepo.findOne(id);

		model.addAttribute("contact", add);
		return "contact";
	}

	@Timed
	@RequestMapping(method = RequestMethod.POST, path = "/contact")
	public String contact(@Valid @ModelAttribute("contact") Contact contact, BindingResult bindingResult,
			Authentication auth, Model model) {

		if (bindingResult.hasErrors()) {
			return "contact";
		}

		
		try {
			LoggedUser user = (LoggedUser) auth.getPrincipal();
			contact.setUser(user.getUser());
			if (contact.getId() == 0)// new
			{
				contactRepo.save(contact);
				
				String[] mails = userRepo.findAdministratorsEmails();
				
				
				Map<String, Object> vars = new HashMap<String, Object>();
				
				vars.put("role", user.getRole());
				vars.put("contact", contact);
				vars.put("adminEmails", String.join(",", mails));
				
				runtimeService.startProcessInstanceByKey("myProcess", vars);
			}
			else
			{
				contactRepo.save(contact);
				
			}
			
		}
		catch (Exception e) {
			ObjectError error = new ObjectError("*", e.getMessage());

			bindingResult.addError(error);
			return "contact";
		}
		
		
		
		return "contactview";
	}

	@RequestMapping("/contacts")
	public String contacts(Model model, Authentication auth) {

		LoggedUser user = (LoggedUser) auth.getPrincipal();

		if (user.getRole().equals("Administrator")) {
			model.addAttribute("contacts", contactRepo.findAll());

		} else {
			model.addAttribute("contacts", contactRepo.findByCompany(user.getCompany().getId()));
			}

		return "contacts";
	}

	@RequestMapping("/contact/view/{id}")
	public String showcontact(@PathVariable int id, Model model) {

		Contact c = contactRepo.findOne(id);

		model.addAttribute("contact", c);
		return "contactview";

	}

	@RequestMapping("/contact/delete/{id}")
	public String deletecontact(@PathVariable int id, Model model) {

		contactRepo.delete(id);
		return "redirect:/contacts";

	}


	
	@RequestMapping("/admin/tasks")
	public String tasks(Model model) {

		List<Task> tasks = taskService.createTaskQuery()
				.processDefinitionKey("myProcess").list();
	
		List<TaskRepresentation> tasksrep = tasks.stream()
				.map(task -> new TaskRepresentation(task.getId(), task.getName(), task.getProcessInstanceId()))
				.collect(Collectors.toList());
		
		model.addAttribute("tasks", tasksrep);
		
		return "tasks";

	}

	@RequestMapping("/admin/tasks/complete/{tid}")
	public String completeTaskGet(@PathVariable String tid, Model model) {

		Contact c = (Contact) runtimeService
				.getVariable(taskService.createTaskQuery().taskId(tid).list().get(0).getExecutionId(), "contact");
		model.addAttribute("tid", tid);
		model.addAttribute("message", "There is a new Contact: " + c.toString());
		return "task";

	}

	@RequestMapping("/admin/tasks/complete/{tid}/{agree}")
	public String completeTaskComplete(@PathVariable String tid, @PathVariable int agree) {

		Map<String, Object> vars = new HashMap<String, Object>();
		vars.put("agree", agree);
		taskService.complete(tid, vars);
		return "redirect:/admin/tasks";

	}

	
	@Autowired
    JobLauncher jobLauncher;

    @Autowired
    Job job;

   
    
    
	@Restrict(admin = true, localOnly = true)
	@RequestMapping("/admin/export")
	public String export(Model model) {

		try {
			JobParameters jp= new JobParametersBuilder(new JobParameters()).addLong("run.id", System.currentTimeMillis()).toJobParameters();
			jobLauncher.run(job, jp);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		return "home";
		
	}

	
}
